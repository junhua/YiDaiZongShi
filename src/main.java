import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import flex.messaging.io.amf.client.AMFConnection;
import flex.messaging.io.amf.ASObject;
import flex.messaging.io.amf.client.exceptions.ClientStatusException;
import flex.messaging.io.amf.client.exceptions.ServerStatusException;
import flex.messaging.messages.RemotingMessage;

public class test2
{
	public static AMFConnection amfConnection;
	public static int userID; public static String userName;

	public static void main(String args[]) throws Exception
	{
		String _userID="andyhidy",userPWD="andyhidyyy"; userName = "vivajh"; String server = "35";
		int minLvl=91, maxLvl=95;int city=1;
		
		_userID = "andyhidy1";  userPWD = "andyhidy2";  minLvl=60;  maxLvl=68;  userName = "西门昭雪";
		
		
		
		amfConnection = connect(_userID,userPWD,server);
//		doJingJi();
//		explorePetHill(3,3); //探險 String  int mountain, int round
//		serveInnGuest();
//		getReward();	//每日獎勵
		
		while(true){
			try{
				amfConnection = connect(_userID,userPWD,server);
				tradeGoodsBuildShip2();
//				tradeGoodsBuildShip();
//				tradeGoods();
//				tradePrice();
//				getReward();	//每日獎勵
//				
//				serveInnGuest();

//				serveInnGuest();
//				doZhixian(150); // 支線。 60：光明頂	
//				jiebiao(minLvl,maxLvl); //劫鏢
//				jiebiaolist(minLvl,maxLvl);toSleep(5000);
//				while(true)doHijack(3629); // 劫鏢一次
//				while(true)doHijack(16985); // 劫鏢一次
//				doYunYou( 100, 1);  //青城山
//				doYunYou( 10000, 2);  //武当山
//				doYunYou( 20, 3);  //昆仑山
//				doYunYou( 5, 4);  //华山
//				doYunYou( 398, 5);  //黄山
//				doYunYou( 20, 6);  //峨眉山
//				doYunYou( 30, 7);  //泰山
//				doYunYou2( 1, 6); //雲遊全部
//				tradeGoods();
//				playStory();
				
//				doFishing(110); // 釣魚。 session id, no of times
//				
//				upSkill();
//				inCity(city);	//搶城， 1中間，2左上，3右上，4左下，5右下
//				inCity2();	//搶城1次
			
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}
	
	
	public static void tradePrice() throws Exception
	{

		Map<Integer,String> town = new HashMap<Integer,String>();
		Map<Integer,String> goods = new HashMap<Integer,String>();
		town.put(1, "高丽");
		town.put(2, "泉州");
		town.put(3, "麻逸");
		town.put(4, "婆罗洲");
		town.put(5, "苏门答腊");
		town.put(6, "天竺");
		town.put(7, "波斯");
		town.put(8, "马达加斯加");
		goods.put(6101,"鱼肉");
		goods.put(6102,"谷物");
		goods.put(6201,"木材");
		goods.put(6202,"矿石");
		goods.put(6301,"染料");
		goods.put(6302,"工艺品");
		goods.put(6401,"茶叶");
		goods.put(6402,"贵金属");
		goods.put(6501,"香料");
		goods.put(6502,"陶瓷");
		String[][] output = new String[goods.size()+1][town.size()+1];
		Map<String,TreeMap> pricelist = new TreeMap<String,TreeMap>();

		for(int i=1;i<town.size()+1;++i){
			TreeMap<String,String> thisGoods = new TreeMap<String,String>();
			ASObject thisTown = (ASObject)execute("ShippingService.showShippingCtiy", new Object[] {userID,i});
			ASObject content = (ASObject)thisTown.get("content");
			content = (ASObject)content.get("result");
			ArrayList<ASObject> city_goods = (ArrayList<ASObject>)content.get("goods_info");
			
			//buy
			for (int j=0;j<city_goods.size();++j){
				ASObject citygood = city_goods.get(j);
				int city_good_id = Integer.parseInt(citygood.get("goods_id").toString());
				String city_price = citygood.get("price").toString();
				String goodname=goods.get(city_good_id);
				thisGoods.put(goodname, city_price);
			}
			
			pricelist.put(town.get(i), thisGoods);
		}
		for(int i=0;i<pricelist.size();++i){
			System.out.println(pricelist.keySet().toArray()[i].toString() + ": "+pricelist.values().toArray()[i]);
		}
	}
	
	public static void lastTrade() throws Exception{
		//get goods
		ASObject shop = (ASObject)execute("ShippingService.showShop", new Object[] {userID});
		ASObject shopContent = (ASObject)shop.get("content");
		shopContent=(ASObject)shopContent.get("result");

		ArrayList<ASObject> city_goods = (ArrayList<ASObject>)shopContent.get("city_goods");
		ArrayList<ASObject> user_goods = (ArrayList<ASObject>)shopContent.get("user_goods");

		//sell
		for (int i=0;i<user_goods.size();++i){
			ASObject good = user_goods.get(i);

			int goods_id = Integer.parseInt(good.get("goods_id").toString());
			int qty = Integer.parseInt(good.get("num").toString());

			for (int j=0;j<city_goods.size();++j){
				ASObject citygood = city_goods.get(j);
				int city_good_id = Integer.parseInt(citygood.get("goods_id").toString());
				int city_price = Integer.parseInt(citygood.get("price").toString());

				if(goods_id == city_good_id){
					for (int k=0;k<qty;++k){
						execute("ShippingService.sellGoods", new Object[] {userID,goods_id,1,city_price});
						execute("ShippingService.addShipExp", new Object[] {userID,0});
						System.out.println(userName +": "+ k);
					}
				}
				j=city_goods.size();
			}
		}
	}

		
	
public static void tradeGoodsBuildShip2() throws Exception{

		//get ticket and time
		ASObject myShip= (ASObject)execute("ShippingService.showShipping", new Object[] {userID});
		ASObject content = (ASObject)myShip.get("content");
		System.out.println(userName+" " + content);
		content = (ASObject)content.get("result");
		String ticket=content.get("ticket").toString();
		int left_time=Integer.parseInt(content.get("left_time").toString());
		String city=content.get("to_city_id").toString();
		
		System.out.println("Begin......User: "+userName+ " 商票： "+ticket);
		
		//get city goods
		ASObject shop = (ASObject)execute("ShippingService.showShop", new Object[] {userID});
		ASObject shopContent = (ASObject)shop.get("content");
		shopContent=(ASObject)shopContent.get("result");

		ArrayList<ASObject> city_goods = (ArrayList<ASObject>)shopContent.get("city_goods");
		LinkedHashMap<Integer,Integer> city_goods_map = new LinkedHashMap<Integer,Integer>();
		for(int i=0;i<city_goods.size()-1;++i){
			int id1 = Integer.parseInt(city_goods.get(i).get("goods_id").toString());
			int price1 = Integer.parseInt(city_goods.get(i).get("price").toString());
			int id2 = Integer.parseInt(city_goods.get(i+1).get("goods_id").toString());
			int price2 = Integer.parseInt(city_goods.get(i+1).get("price").toString());
			if(price1<price2){
				city_goods_map.put(id1, price1);
				city_goods_map.put(id2, price2);
			}
			else{
				city_goods_map.put(id2, price2);
				city_goods_map.put(id1, price1);
			}
		}
	
		//get user goods
		ArrayList<ASObject> user_goods = (ArrayList<ASObject>)shopContent.get("user_goods");
		LinkedHashMap<Integer,Integer[]> user_goods_map = new LinkedHashMap<Integer,Integer[]>();
		
		
			
		if(user_goods.size()>1)
			for(int i=0;i<user_goods.size()-1;++i){

				ASObject userGood1 = user_goods.get(i);
				int user_good_id = Integer.parseInt(userGood1.get("goods_id").toString());
				int user_price = Integer.parseInt(userGood1.get("price").toString());
				int qty = Integer.parseInt(userGood1.get("num").toString());
				Integer[] p_q = {user_price,qty};

				ASObject userGood2 = user_goods.get(i);
				int user_good_id2 = Integer.parseInt(userGood2.get("goods_id").toString());
				int user_price2 = Integer.parseInt(userGood2.get("price").toString());
				int qty2 = Integer.parseInt(userGood2.get("num").toString());
				Integer[] p_q2 = {user_price2,qty2};

				if(user_price < user_price2){
					user_goods_map.put(user_good_id, p_q);
					user_goods_map.put(user_good_id2, p_q2);
				}else{
					user_goods_map.put(user_good_id2, p_q2);
					user_goods_map.put(user_good_id, p_q);
				}
			}
		else if (user_goods.size()==1){
			ASObject userGood1 = user_goods.get(0);
			int user_good_id = Integer.parseInt(userGood1.get("goods_id").toString());
			int user_price = Integer.parseInt(userGood1.get("price").toString());
			int qty = Integer.parseInt(userGood1.get("num").toString());
			Integer[] p_q = {user_price,qty};
		}
		int totalqty=0;
		for (ASObject userGood:user_goods){
			int user_good_id = Integer.parseInt(userGood.get("goods_id").toString());
			int user_price = Integer.parseInt(userGood.get("price").toString());
			int qty = Integer.parseInt(userGood.get("num").toString());
			totalqty+=qty;
			Integer[] p_q = {user_price,qty};
			user_goods_map.put(user_good_id, p_q);
		}
		
		//sell
		if(totalqty<49){		
			if(user_goods_map.size()>=1){
				for (int id: user_goods_map.keySet()){
					Integer[] p_q = user_goods_map.get(id);		
					int price = p_q[0];
					int qty = p_q[1];
					if(city_goods_map.containsKey(id) && price*2<city_goods_map.get(id)) 
					{ 
						int city_price = city_goods_map.get(id);
						System.out.println(userName + " is selling all goods...");
						System.out.println(userName + " sell "+ id + " price: "+ city_price);
						execute("ShippingService.sellGoods", new Object[] {userID,id,qty,city_price});
					}
				}
			}
		}else
		if(user_goods_map.size()>=1){
			for (int id: user_goods_map.keySet()){
				Integer[] p_q = user_goods_map.get(id);		
				int price = p_q[0];
				int qty = p_q[1];
				if(city_goods_map.containsKey(id) && price*2<city_goods_map.get(id)) 
				{
					
					int trade_id = id;
					int city_price = city_goods_map.get(id);
					int good_index=-1;
					while(Integer.parseInt(user_goods_map.keySet().toArray()[++good_index].toString())!=id);
					
					for(int i=0; i<qty/2;++i){
						//sell one piece 
						System.out.println(userName + " sell "+ trade_id + " price: "+ city_price);
						execute("ShippingService.sellGoods", new Object[] {userID,trade_id,1,city_price});
													
						//buy all next
						int next_price = Integer.parseInt(city_goods_map.values().toArray()[good_index-1].toString());
						int next_id = Integer.parseInt(city_goods_map.keySet().toArray()[good_index-1].toString());

						String error_msg = "";
						while(error_msg.length()<1){
							System.out.println(userName + " buy: "+ next_id+ " price: "+ next_price);
							ASObject buy = (ASObject)execute("ShippingService.buyGoods", new Object[] {userID,next_id,1,next_price});
							error_msg = buy.get("error_msg").toString();
						}
						
						//build once after buying all
						System.out.println(userName+" build ship..");
						execute("ShippingService.addShipExp", new Object[] {userID,0});
						if((i==qty/2-1)){
							if(--good_index>0){
								i=0;
								trade_id = next_id;
								city_price = next_price;
							}else{
								error_msg = "";
								while(error_msg.length()<1){
									System.out.println(userName + " sell: "+ next_id+ " price: "+ next_price);
									ASObject buy = (ASObject)execute("ShippingService.sellGoods", new Object[] {userID,next_id,1,next_price});
									error_msg = buy.get("error_msg").toString();
									System.out.println(userName+" build ship..");
									execute("ShippingService.addShipExp", new Object[] {userID,0});
								}
							}
						}
					}
					System.out.println(userName+" sell remaining "+id+" ...");
					execute("ShippingService.sellGoods", new Object[] {userID,id,qty/2,city_goods_map.get(id)});
				}
			}
		}
						
		
		//buy
		for (int i=0;i<city_goods.size();++i){
			ASObject citygood = city_goods.get(i);
			int city_good_id = Integer.parseInt(citygood.get("goods_id").toString());
			int city_price = Integer.parseInt(citygood.get("price").toString());
			if((city_good_id==6501||city_good_id==6502)&&city_price<800){
				String error_msg = "";
				int countbuy = 1;
				while(error_msg.length()<1){
					ASObject buy = (ASObject)execute("ShippingService.buyGoods", new Object[] {userID,city_good_id,1,city_price});
					System.out.println(userName + " buy"+ city_good_id+" : "  + countbuy++);
					error_msg = buy.get("error_msg").toString();
				}
			}
		}	

		refresh();
		String usernames = "vivajh 西门昭雪 元化";
		for(int i=0;i<20;++i){
			refresh();
			
			if(usernames.contains(userName)){
				System.out.println(userName+" refresh good: "+ execute("ShippingService.showDrunkery", new Object[] {userID,city}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6201}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6202}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6301}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6302}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6401}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6402}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6501}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6502}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6101}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6102}));
			}
			toSleep(left_time*1000/20);
		}
	}
	
	
	public static void tradeGoodsBuildShip() throws Exception{

		//get ticket and time
		ASObject myShip= (ASObject)execute("ShippingService.showShipping", new Object[] {userID});
		ASObject content = (ASObject)myShip.get("content");
		System.out.println(userName+" " + content);
		content = (ASObject)content.get("result");
		String ticket=content.get("ticket").toString();
		int left_time=Integer.parseInt(content.get("left_time").toString());
		String city=content.get("to_city_id").toString();

		//get goods
		ASObject shop = (ASObject)execute("ShippingService.showShop", new Object[] {userID});
		ASObject shopContent = (ASObject)shop.get("content");
		shopContent=(ASObject)shopContent.get("result");

		System.out.println("Begin......User: "+userName+ " 商票： "+ticket);

		ArrayList<ASObject> city_goods = (ArrayList<ASObject>)shopContent.get("city_goods");
		ArrayList<ASObject> user_goods = (ArrayList<ASObject>)shopContent.get("user_goods");

		//sell

		for (int i=0;i<user_goods.size();++i){
			ASObject good = user_goods.get(i);

			int goods_id = Integer.parseInt(good.get("goods_id").toString());
			int price = Integer.parseInt(good.get("my_price").toString());
			int qty = Integer.parseInt(good.get("num").toString());

			System.out.println(userName+ ", good: "+goods_id +", Price: "+price+ ", qty: "+qty);
			
				
			for (int j=0;j<city_goods.size();++j){
				ASObject citygood = city_goods.get(j);

				int city_good_id = Integer.parseInt(citygood.get("goods_id").toString());
				int city_price = Integer.parseInt(citygood.get("price").toString());
				
				if(goods_id == city_good_id && city_price>1600){
					System.out.println("Sell good: "+city_good_id+", Price: "+city_price);
//					if(qty<45)
//						System.out.println(execute("ShippingService.sellGoods", new Object[] {userID,goods_id,qty,city_price}));
//					else
					for (int k=0;k<qty;++k){
						execute("ShippingService.sellGoods", new Object[] {userID,goods_id,1,city_price});
						System.out.println(userName+ " sell good: " + k);
						
						if(k<qty/2 && price*2<city_price){
							//build ship
							execute("ShippingService.addShipExp", new Object[] {userID,0});
							System.out.println(userName+ " add exp: " + k);
						}
					}
					j=city_goods.size();


				}
			}	
		}
		//buy
		for (int i=0;i<city_goods.size();++i){
			ASObject citygood = city_goods.get(i);
			int city_good_id = Integer.parseInt(citygood.get("goods_id").toString());
			int city_price = Integer.parseInt(citygood.get("price").toString());
			if((city_good_id==6501||city_good_id==6502)&&city_price<800){
				String error_msg = "";
				int countbuy = 1;
				while(error_msg.length()<1){
					ASObject buy = (ASObject)execute("ShippingService.buyGoods", new Object[] {userID,city_good_id,1,city_price});
					System.out.println(userName + " buy"+ city_good_id+" : "  + countbuy++);
					error_msg = buy.get("error_msg").toString();
				}
			}
		}	

		refresh();
		String usernames = "vivajh 西门昭雪 元化";
		for(int i=0;i<20;++i){
			refresh();
			
			if(usernames.contains(userName)){
				System.out.println(userName+" refresh good: "+ execute("ShippingService.showDrunkery", new Object[] {userID,city}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6201}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6202}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6301}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6302}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6401}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6402}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6501}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6502}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6101}));
				System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6102}));
			}
			toSleep(left_time*1000/20);
		}
	}
	
	public static int tradeGoods() throws Exception
	{

		
		while(true){
			//get ticket and time
			ASObject myShip=myShip= (ASObject)execute("ShippingService.showShipping", new Object[] {userID});
			ASObject content = (ASObject)myShip.get("content");
			System.out.println(userName+" " + content);
			content = (ASObject)content.get("result");
			String ticket=content.get("ticket").toString();
			int left_time=Integer.parseInt(content.get("left_time").toString());
			String city=content.get("to_city_id").toString();

			
			//get goods
			ASObject shop = (ASObject)execute("ShippingService.showShop", new Object[] {userID});
			ASObject shopContent = (ASObject)shop.get("content");
			shopContent=(ASObject)shopContent.get("result");

			System.out.println("Begin......User: "+userName+ " 商票： "+ticket);
			
			ArrayList<ASObject> city_goods = (ArrayList<ASObject>)shopContent.get("city_goods");
			ArrayList<ASObject> user_goods = (ArrayList<ASObject>)shopContent.get("user_goods");

			//sell

			for (int i=0;i<user_goods.size();++i){
				ASObject good = user_goods.get(i);

				int goods_id = Integer.parseInt(good.get("goods_id").toString());
				int price = Integer.parseInt(good.get("my_price").toString());
				int qty = Integer.parseInt(good.get("num").toString());
				
				System.out.println("My good: "+goods_id +", Price: "+price+ ", qty: "+qty);
				
				for (int j=0;j<city_goods.size();++j){
					ASObject citygood = city_goods.get(j);
					
					int city_good_id = Integer.parseInt(citygood.get("goods_id").toString());
					int city_price = Integer.parseInt(citygood.get("price").toString());
					if(goods_id == city_good_id && price<city_price){
						System.out.println("Sell good: "+city_good_id+", Price: "+city_price);
						System.out.println(execute("ShippingService.sellGoods", new Object[] {userID,goods_id,qty,city_price}));
						j=city_goods.size();
					}
				}	
			}

			//buy
			for (int i=0;i<city_goods.size();++i){
				int countbuy=0;
				String error_msg="";
				ASObject citygood = city_goods.get(i);
				int city_good_id = Integer.parseInt(citygood.get("goods_id").toString());
				int city_price = Integer.parseInt(citygood.get("price").toString());
				if((city_good_id==6501||city_good_id==6502)&&city_price<1000){
					error_msg = "";
					while(error_msg.length()<1){
						ASObject buy = (ASObject)execute("ShippingService.buyGoods", new Object[] {userID,city_good_id,1,city_price});
						System.out.println(userName+ " buy: "+ ++countbuy);
						error_msg = buy.get("error_msg").toString();
					}
				}else if((city_good_id==6401||city_good_id==6402)&&city_price<300){
					error_msg = "";
					while(error_msg.length()<1){
						ASObject buy = (ASObject)execute("ShippingService.buyGoods", new Object[] {userID,city_good_id,1,city_price});
						System.out.println(userName+ " buy: "+ ++countbuy);
						error_msg = buy.get("error_msg").toString();
					}
				}
			}	
			
			refresh();
			for(int i=0;i<20;++i){
				refresh();
				String usernames = "vivajh 西门昭雪 元化";
				if(usernames.contains(userName)){
					System.out.println(userName+" refresh good: "+ execute("ShippingService.showDrunkery", new Object[] {userID,city}));
					System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6201}));
					System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6202}));
					System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6301}));
					System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6302}));
					System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6401}));
					System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6402}));
					System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6501}));
					System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6502}));
					System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6101}));
					System.out.println(userName+" refresh good: "+ execute("ShippingService.shareGoods", new Object[] {userID,6102}));
				}
				toSleep(left_time*1000/20);
			}
			
		}
	}
	
	public static void upSkill() throws Exception{
		String error=",";
		while(error.length()>0){
			//365005 一气	
			error=((ASObject)execute("UserSkillService.next", new Object[] {userID,363007})).get("error_msg").toString();
			System.out.println(error);
		}
	}
	
	public static void doJingJi() throws Exception
	{
		
		
		for (int i=0;i<20;++i){

			if(i==0)
				System.out.println(execute("ChampionService.championTop100Award", new Object[] {userID}));
			System.out.println(execute("ChampionService.championMatch", new Object[] {userID}));
		}
	}
	
	
	public static void inCity2() throws Exception
	{
		String error = ((ASObject)execute("AllianceBattleService.inAllianceBattle", new Object[] {userID})).get("error_msg").toString();
		while (error.contains("活動還未開始"))
			error = ((ASObject)execute("AllianceBattleService.inAllianceBattle", new Object[] {userID})).get("error_msg").toString();
		while (true){
			for (int i=1;i<6;++i){
			System.out.println(execute("AllianceBattleService.inCity", new Object[] {userID,i}));
			}
		}
	}

	public static void inCity(int city) throws Exception
	{
		String error = ((ASObject)execute("AllianceBattleService.inAllianceBattle", new Object[] {userID})).get("error_msg").toString();
		while (error.contains("活動還未開始"))
			error = ((ASObject)execute("AllianceBattleService.inAllianceBattle", new Object[] {userID})).get("error_msg").toString();
		while (true){
			System.out.println(userName);
			System.out.println(execute("AllianceBattleService.inCity", new Object[] {userID,city}));
			}
		
	}
	
	public static void jiebiaolist(int minLvl, int maxLvl) throws Exception
	{
		//get my hijack number and orange car list
		ASObject output = (ASObject)execute("BiaojuService.goList", new Object[] {userID});
		ASObject content = (ASObject)output.get("content");

		//List of car
		@SuppressWarnings("unchecked")
		ArrayList<ASObject> carlist = (ArrayList<ASObject>)content.get("list");
		ArrayList<String> robDieYouList = new ArrayList<String>();
		for (int i=0;i<carlist.size();++i){
			//				System.out.println(robDieYouList);

			ASObject thisCar = carlist.get(i);
			int car_color = Integer.parseInt(thisCar.get("car_color").toString());
			ASObject tips = (ASObject)thisCar.get("tips");

			int hijack_me_number = Integer.parseInt(tips.get("hijack_me_num").toString());
			int lvl = Integer.parseInt(tips.get("level").toString());
			String clan = tips.get("alliance_name").toString();
			String name = tips.get("zm_name").toString();

			boolean robHim = (car_color == 5) && 
					(hijack_me_number<5) && 
					(lvl>=minLvl) && 
					(lvl<=maxLvl);
//					&&!clan.equals("英雄聯盟") && !clan.equals("惜敗哥聯盟")&& !clan.equals("夏日摸摸茶");
			if (robHim){
				//					System.out.println(((ASObject)thisCar.get("tips")).get("zm_name"));

				robDieYouList.add("UserID: " +thisCar.get("uid")+" Name: "+ name+  " Level: " + lvl + " Clan: "+ clan);
//				System.out.println(thisCar);
			}
		}
		for (int i=0;i<robDieYouList.size();++i){
			System.out.println(robDieYouList.get(i));

		}
			//			ASObject output = (ASObject)execute("BiaojuService.hijack", new Object[] {userID, 17044});
			//			System.out.println(output);
	}

	public static void jiebiao(int lowestLvl, int highestLvl) throws Exception
	{
		int hijack_num=5;
		try{
			ASObject myCar = (ASObject)execute("BiaojuService.myBiaoJu", new Object[] {userID});
			ASObject obj = (ASObject)myCar.get("content");
			obj=(ASObject)obj.get("my_biaoju");
			int freshtime = Integer.parseInt(obj.get("free_refesh").toString());

			while (freshtime>0){
				freshtime-=1;
				System.out.println(execute("BiaojuService.refreshCar", new Object[] {userID}));
				System.out.println("Refreshing..."+ freshtime +" time left");
			}
		}catch(Exception e){
			System.out.println(e);
		}finally{
			System.out.println(execute("BiaojuService.submitBiaoju", new Object[] {userID}));
		}
		while (true){
			//getcar
		
			//get my hijack number and orange car list
			ASObject output = (ASObject)execute("BiaojuService.goList", new Object[] {userID});
			ASObject content = (ASObject)output.get("content");


			//first escape constraint: hijack number=0
			hijack_num=Integer.parseInt(((ASObject)content.get("my_biaoju")).get("hijack_num").toString());
			if (hijack_num<=0) {System.out.println("已經劫完啦！"); return;}

			//List of car
			@SuppressWarnings("unchecked")
			ArrayList<ASObject> carlist = (ArrayList<ASObject>)content.get("list");
			ArrayList<Integer> robDieYouList = new ArrayList<Integer>();
			for (int i=0;i<carlist.size();++i){
				//				System.out.println(robDieYouList);

				ASObject thisCar = carlist.get(i);
				int car_color = Integer.parseInt(thisCar.get("car_color").toString());
				ASObject tips = (ASObject)thisCar.get("tips");
				//				System.out.println(tips);
				int hijack_me_number = Integer.parseInt(tips.get("hijack_me_num").toString());
				int lvl = Integer.parseInt(tips.get("level").toString());
				String clan = tips.get("alliance_name").toString();
				String name = tips.get("zm_name").toString();

				boolean robHim = (car_color == 5) && 
						(hijack_me_number<5) && 
						(lvl>=lowestLvl) && 
						(lvl<=highestLvl)&&
						!(userName.equals("心")&&clan.equals("笑傲江湖")) && !clan.equals("八月居")&&
						!clan.equals("英雄聯盟") && !clan.equals("惜敗哥聯盟") &&
						!clan.equals("夏日摸摸茶") && !userName.equals("难懂");

				if (robHim){
					robDieYouList.add(Integer.parseInt(thisCar.get("uid").toString()));
				}
			}
			for (int i=0;i<robDieYouList.size();++i){
				System.out.print("User: "+userName +" Left: "+hijack_num+" , ");
				System.out.println(execute("BiaojuService.hijack", new Object[] {userID,robDieYouList.get(i)}));
			}
		}

	}


	public static void doHijack( int uid) throws Exception
	{

		
		// Make a remoting call and retrieve the result.
		ASObject output = (ASObject)execute("BiaojuService.goList", new Object[] {userID});
		ASObject content = (ASObject)output.get("content");
		int hijack_num=Integer.parseInt(((ASObject)content.get("my_biaoju")).get("hijack_num").toString());
		


//		System.out.println("Start Hijack...");
		System.out.println("User: "+userName+", " +"times left: "+ hijack_num);
//		while (true){	
		execute("BiaojuService.hijack", new Object[] {userID,uid});
			
//		}
		//System.out.println("Sleeping ....... "+ i);	
		//long timeToSleep = (1000*showRandomInteger(1,3,new Random()));
		//System.out.println("Sleep Time2:"+timeToSleep);
		//toSleep(timeToSleep);


	}
	
	public static void explorePetHill( int mountain, int round) throws Exception
	{
		int mtID = 1000+mountain;
		String[] intList={"10069","10072","10062","10061","10081","10063","10045","10037","10033","10031","10057","10027","10028","10001","10025","10013","10003","10081","10083","10089"};
		String[] bosses={"10060","10030"};
		ArrayList<String> secondChoice = new ArrayList<String>();
		ArrayList<String> bossList = new ArrayList<String>();
		for(int i=0;i<bosses.length;++i){
			bossList.add(bosses[i]);
		}
		for (int i=0;i<intList.length;++i){
			secondChoice.add(intList[i]);
		}
		
		for (int i=0;i<round;++i){
			String eventId = "";
			do
			{
				System.out.println(userName);

				ASObject output = (ASObject)execute("PetService.find", new Object[] {userID, mtID, 0});
				String error_msg = output.get("error_msg").toString();
				System.out.println(error_msg);
				if(error_msg.contains("物品不足")){
					System.out.println(execute("PetService.find", new Object[] {userID, mtID, 2}));
					continue;
				}
				if(error_msg.contains("探索次數用完"))
					return;
				output=(ASObject)output.get("content");
				String now_group = output.get("now_group").toString();
//				if(userName.equals("vivajh")&&mountain==3){
//					if(Integer.parseInt(now_group)>8){
//						System.out.println(execute("LianGongService.setSkillPositions", new Object[] {userID, 10747,100035, 1}));
//					}
//					else if(Integer.parseInt(now_group)>6){
//						System.out.println(execute("LianGongService.setSkillPositions", new Object[] {userID, 10747,100045, 1}));
//					}
//				}
				System.out.println(output.get("event_info"));
				output=(ASObject)output.get("event_info");
			
				eventId = output.get("info").toString();
				int choice=secondChoice.contains(eventId)?2:1;
				toSleep(1200);
				refresh();
				System.out.println(execute("PetService.find", new Object[] {userID, mtID, choice}));
				toSleep(1200);
				refresh();
			}while (!bossList.contains(eventId));
			System.out.println("Round "+ (i+1) +" completed...");
		}
	}

	public static void playStory() throws Exception
	{
		int startTask=0;
		int lastTask=0;
		int thisTask=0;
		int count=1;
//		toSleep(420000);
//		for (int i=0;i<6;++i){
//			System.out.println("User: "+ userName +" Sleeping: "+ i);
//			toSleep(420000/6);
//		}
		do{
			System.out.println("User: "+ userName);
			lastTask=thisTask;


			//get rewards
			//			ASObject output = (ASObject)execute("StoryService.playStory", new Object[] {userID});
			//			System.out.println(execute("StoryService.playStory", new Object[] {userID}));
			ASObject output = (ASObject)execute("StoryService.playStory", new Object[] {userID});
			System.out.println(output);
			output=(ASObject)output.get("content");
			output=(ASObject)output.get("status");

			thisTask=Integer.parseInt(output.get("story_id").toString());
			if (lastTask==0) lastTask=thisTask;
			System.out.println("User: "+userName+" \nStarttask: "+startTask+" Lasttask: "+lastTask+" Thistask: "+thisTask+" Count: "+ count++);
			//levelUp
			if(!userName.equals("vivajh")) levelUp();
			toSleep(50000);
			refresh();
			if (startTask==0)
				startTask = thisTask;
		}while(true);
	}

	public static void getReward() throws Exception
	{
		
		//levelUp
		levelUp();
		
		//JingJi
		doJingJi();
		

		
		//Daily reward
		System.out.println(execute("MonthRewardService.show", new Object[] {userID}));
		System.out.println(execute("MonthRewardService.receive", new Object[] {userID}));
		
		ASObject responses = (ASObject)execute("ActivityYunYingService.activityList", new Object[] {userID,0});
		ASObject content = (ASObject)responses.get("content");
		@SuppressWarnings("unchecked")
		ArrayList<ASObject> list = (ArrayList<ASObject>)content.get("list");
		for (int i=0;i<list.size();++i){

			String serial_num = (String)list.get(i).get("serial_num");

			ASObject info = (ASObject)execute("ActivityYunYingService.getInfo", new Object[] {userID,serial_num});
			info=(ASObject)info.get("content");
			info=(ASObject)info.get("result");

			ArrayList<ASObject> reward_list = (ArrayList<ASObject>) info.get("reward_list");
			for (int j=0;j<reward_list.size();++j){
				String id = reward_list.get(j).get("id").toString();
				//get rewards
				if(!id.equals("9949"))
				System.out.println(execute("ActivityYunYingService.getReward", new Object[] {userID,serial_num,id}));
			}
		}


		//			Map<String, String> map = getQueryMap(list.toString());
		//			BufferedReader rd  = new BufferedReader(new InputStreamReader());


		//Inn
		serveInnGuest();
		// pet
		explorePetHill(1,2);

	}
	
	public static void doZhixian(int type) throws Exception
	{

		// Make a remoting call and retrieve the result.
		//System.out.println("Results1 = [-"+execute("TequanService.tequanList", new Object[] {3629})+"-]");
		refresh();

//		type=70;
		while (true){
			// level up
			levelUp();
			System.out.println("Start Zhixian...");
			String getReward = "Results = [-"+execute("ZhixianService.getZhixianAward", new Object[] {userID,type});
			String doZhixian = "Results = [-"+execute("ZhixianService.autoZhixianStart", new Object[] {userID,type});
			while (doZhixian.contains("未開啟")||doZhixian.contains("挑戰次數已用完")||doZhixian.contains("不存在")||doZhixian.contains("未通關")){
				type-=10;
				doZhixian = "Results = [-"+execute("ZhixianService.autoZhixianStart", new Object[] {userID,type-10});
				if (type<10){
					System.out.println("No more..");
					return;
				}
			}
		if(doZhixian.contains("體力"))
			return;
		System.out.println(userName);
		System.out.println(getReward);
		System.out.println(doZhixian);
		for (int i=0;i<20;++i){
			System.out.println(userName + " is leeping... "+(20-i)+" min more");
			toSleep(60000);
			refresh();
		}
		}
		//System.out.println("Sleeping ....... "+ i);	
		//long timeToSleep = (1000*showRandomInteger(1,3,new Random()));
		//System.out.println("Sleep Time2:"+timeToSleep);
		//toSleep(timeToSleep);


	}

	public static void doYunYou(int times, int type) throws Exception
	{
		int loopTimes=20;

		if (times==0)
			loopTimes=100;
		else
			loopTimes=times;

		for(int i=1;i<=loopTimes;i++)
		{				


			// Make a remoting call and retrieve the result.
			//System.out.println("Results1 = [-"+execute("TequanService.tequanList", new Object[] {3629})+"-]");
			refresh();

			System.out.println("User:" +userName + " No:"+ i);
			boolean sell = false;
			// 1
			ASObject output = (ASObject)execute("YunyouService.doYunyou", new Object[] {userID,type,1});
			sell = (sell||output.get("error_msg").toString().contains("白銀")||output.get("error_msg").toString().contains("通行證"));
			System.out.println(output);
			// 2
			output = (ASObject)execute("YunyouService.doYunyou", new Object[] {userID,type,2});
			sell = (sell||output.get("error_msg").toString().contains("白銀")||output.get("error_msg").toString().contains("通行證"));
			System.out.println(output);
			// 3
			output = (ASObject)execute("YunyouService.doYunyou", new Object[] {userID,type,3});
			sell = (sell||output.get("error_msg").toString().contains("白銀")||output.get("error_msg").toString().contains("通行證"));
			System.out.println(output);
			// 4
			output = (ASObject)execute("YunyouService.doYunyou", new Object[] {userID,type,4});
			sell = (sell||output.get("error_msg").toString().contains("白銀")||output.get("error_msg").toString().contains("通行證"));
			System.out.println(output);
			// 5
			output = (ASObject)execute("YunyouService.gameOne", new Object[] {userID,type});
			System.out.println("Results4 = [-"+ output +"-]");
			System.out.println(output.get("error_msg").toString());
			if (sell){
				ASObject myPackage = (ASObject)execute("PackageService.getPackageList", new Object[] {userID});
				ASObject content = (ASObject)myPackage.get("content");
				content = (ASObject)content.get("other");
				ArrayList<ASObject>suipians =(ArrayList<ASObject>)myPackage.get("tuzhi");
				for(int j=0;i<suipians.size();++j){
					ASObject suipian = suipians.get(j);
					String id = suipian.get("id").toString();
					String matterId = suipian.get("sys_matter_id").toString();
					int qty = Integer.parseInt(suipian.get("own_num").toString());
					if (qty>20){
						System.out.println("Selling..."+ execute("PackageService.sell", new Object[] {userID,matterId,id,qty}));
					}
				}
			}
			refresh();

			//System.out.println("Sleeping ....... "+ i);	
			//long timeToSleep = (1000*showRandomInteger(1,3,new Random()));
			//System.out.println("Sleep Time2:"+timeToSleep);
			//toSleep(timeToSleep);

		}					
	}
	
	public static void doYunYou2(int type, int endType) throws Exception
	{
		//sell all first
		ASObject myPackage = (ASObject)execute("PackageService.getPackageList", new Object[] {userID});
		ASObject content = (ASObject)myPackage.get("content");
		content = (ASObject)content.get("other");
		ArrayList<ASObject>suipians =(ArrayList<ASObject>)content.get("tuzhi");
		for(int j=0;j<suipians.size();++j){
			ASObject suipian = suipians.get(j);
			String id = suipian.get("id").toString();
			String matterId = suipian.get("sys_matter_id").toString();
			int qty = Integer.parseInt(suipian.get("own_num").toString());
			if (qty>50){
				System.out.println("Selling..."+ execute("PackageService.sell", new Object[] {userID,matterId,id,qty}));
			}
		}
		
		int count=0;
		while (type  < endType+1)
		{				
			// Make a remoting call and retrieve the result.
			//System.out.println("Results1 = [-"+execute("TequanService.tequanList", new Object[] {3629})+"-]");
			refresh();

			System.out.println("User:" +userName + " No:"+ count++ +" Type: "+type);
			boolean sell = false;
			// 1
			ASObject output = (ASObject)execute("YunyouService.doYunyou", new Object[] {userID,type,1});
			sell = (sell||output.get("error_msg").toString().contains("白銀")||output.get("error_msg").toString().contains("通行證"));
			System.out.println(output);
			// 2
			output = (ASObject)execute("YunyouService.doYunyou", new Object[] {userID,type,2});
			sell = (sell||output.get("error_msg").toString().contains("白銀")||output.get("error_msg").toString().contains("通行證"));
			System.out.println(output);
			// 3
			output = (ASObject)execute("YunyouService.doYunyou", new Object[] {userID,type,3});
			sell = (sell||output.get("error_msg").toString().contains("白銀")||output.get("error_msg").toString().contains("通行證"));
			System.out.println(output);
			// 4
			output = (ASObject)execute("YunyouService.doYunyou", new Object[] {userID,type,4});
			sell = (sell||output.get("error_msg").toString().contains("白銀")||output.get("error_msg").toString().contains("通行證"));
			System.out.println(output);
			// 5
			output = (ASObject)execute("YunyouService.gameOne", new Object[] {userID,type});
			System.out.println("Results4 = [-"+ output +"-]");
			System.out.println(output.get("error_msg").toString());
			if (sell){
				myPackage = (ASObject)execute("PackageService.getPackageList", new Object[] {userID});
				content = (ASObject)myPackage.get("content");
				content = (ASObject)content.get("other");
				suipians =(ArrayList<ASObject>)content.get("tuzhi");
				for(int j=0;j<suipians.size();++j){
					ASObject suipian = suipians.get(j);
					String id = suipian.get("id").toString();
					String matterId = suipian.get("sys_matter_id").toString();
					int qty = Integer.parseInt(suipian.get("own_num").toString());
					if (qty>50&&type<6){
						System.out.println("Selling..."+ execute("PackageService.sell", new Object[] {userID,matterId,id,qty}));
					}
				}
				System.out.println("Yunyou type = " + ++type);
			}
			refresh();

			//System.out.println("Sleeping ....... "+ i);	
			//long timeToSleep = (1000*showRandomInteger(1,3,new Random()));
			//System.out.println("Sleep Time2:"+timeToSleep);
			//toSleep(timeToSleep);

		}					
	}
	
	public static void sellSuiPian() throws Exception
	{
		ArrayList<Integer> itemlist = new ArrayList<Integer>();
		ASObject pakg = (ASObject)execute("PackageService.getPackageList", new Object[] {userID});
		ASObject other = (ASObject)pakg.get("other");
		
		ArrayList<ASObject> items = (ArrayList<ASObject>)other.get("other");
		for (int i=0;i<items.size();++i){
			ASObject item = items.get(i);
			if( Integer.parseInt(item.get("own_num").toString())>29){
				itemlist.add(Integer.parseInt(item.get("id").toString()));
			}
		}
		
		for(int i=0;i<itemlist.size();++i){
			System.out.println(execute("ZhixianService.autoZhixianStart", new Object[] {userID,1,2,3}));
		}

	}

	public static void doFishing( int times) throws Exception
	{
		int loopTimes=1;

		if (times==0)
			loopTimes=100;
		else
			loopTimes=times;

		for(int i=1;i<=loopTimes;i++)
		{			

			// Make a remoting call and retrieve the result.
			//Object result = amfConnection.call("TequanService.tequanList", new Object[] {7669});
			execute("LoginService.refresh", new Object[] {userID,0});

			Object result = execute("AllianceActivityService.beginFish", new Object[] {userID,2});
			System.out.println("Results1 = [-"+result+"-]");
			amfConnection.close();

			//long timeToSleep = (1000*showRandomInteger(1,2,new Random()));
			//System.out.println("Sleep Time1:"+timeToSleep);
			//toSleep(timeToSleep);

			try
			{				
				Object result2 = execute("AllianceActivityService.doneFish", new Object[] {userID,1});
				System.out.println("Results2 = [-"+result2+"-]");							
				amfConnection.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Object result2 = execute("AllianceActivityService.doneFish", new Object[] {userID,1});
				System.out.println("Results2 = [-"+result2+"-]");							
				amfConnection.close();					
			}						
			System.out.println("Running ....... "+ i);			
		}					
	}

	public static Map<String, String> getQueryMap(String query)   
	{   
		String response = query;
		response = response.replace('{',' ');
		response = response.replace('}',' ');		
		String[] params = response.split(",");
		Map<String, String> map = new HashMap<String, String>();   
		for (String param : params)
		{   
			String name = param.split(":")[0];   
			String value = param.split(":")[1];   
			//System.out.println("Here = "+name+","+value);
			map.put(name.trim(), value.trim());
		}   
		return map;   
	}  	

	public static void toSleep(long milisec)
	{
		try
		{
			System.out.println("Sleeping for "+milisec+" millisec");
			Thread.currentThread();
			Thread.sleep(milisec);
		}
		catch(Exception e)		
		{
			System.out.println("Error in sleep function :"+e.getMessage());
		}	
	}
	
	public static void serveInnGuest() throws Exception
	{
		/**
		 * InnService.clean(uid,rid), 2561,2562,2563,2564...
		 * Type 1: drink(uid,nid,wid), 97000000,97000001,97000002
		 * Type 2: rest
		 * Type 3: getDice(uid,nid)->dice(uid,nid,0)->receiveDice(uid,nid)
		 * Type 4:
		 * Type 5:
		 * Type 6
		 * Type 7: npc(uid,nid)
		 */
		
		String[] celebreties = {"楊左使", "張公子","朱元璋", "喬幫主","鎮南王"};
		ArrayList<String> cList = new ArrayList<String>();
		for (int i=0;i<celebreties.length;++i){
			cList.add(celebreties[i]);
		}
		
		String error_msg=((ASObject)execute("InnService.getInfo", new Object[] {userID})).get("error_msg").toString();
		int countRefresh=0;
		while(error_msg.trim().length()<1){
			ASObject newGuest=(ASObject)execute("InnService.refreshKezhan", new Object[] {userID});
			System.out.println(countRefresh++ +". "+error_msg);
			error_msg=newGuest.get("error_msg").toString();

		}
		
		//get my hijack number and orange car list
		ASObject output = (ASObject)execute("InnService.getInfo", new Object[] {userID});
		ASObject content = (ASObject)output.get("content");

		
		@SuppressWarnings("unchecked")
		ArrayList<ASObject> roomlist = (ArrayList<ASObject>)content.get("room_list");
		ArrayList<Integer> roomID = new ArrayList<Integer>();  
		ArrayList<ASObject> npclist = (ArrayList<ASObject>)content.get("npc_list");
		
		ArrayList<Integer> vip = new ArrayList<Integer>();
		ArrayList<Integer> group1 = new ArrayList<Integer>();
		ArrayList<Integer> group2 = new ArrayList<Integer>();
//		ArrayList<Integer> group3 = new ArrayList<Integer>();
//		ArrayList<Integer> group4 = new ArrayList<Integer>();
//		ArrayList<Integer> group5 = new ArrayList<Integer>();
//		ArrayList<Integer> group6 = new ArrayList<Integer>();
//		ArrayList<Integer> group7 = new ArrayList<Integer>();
//		ArrayList<Integer> group8 = new ArrayList<Integer>();
		
		for (int i=0;i<npclist.size();++i){
			
			ASObject thisNpc = npclist.get(i);
			int type = Integer.parseInt(thisNpc.get("type").toString());
			int id = Integer.parseInt(thisNpc.get("id").toString());
			String name = thisNpc.get("name").toString();
//			System.out.println(thisNpc + "type: "+ type);
			boolean doneVIP=false;
			
			//VIP
			if (type==7 && cList.contains(name)){						
				vip.add(id);
				
			}else{
				if(type==2)
					group2.add(id);
				else{
					group1.add(id);
				}
			}
		}	
		if (userName.equals("vivajh")&&vip.size()<1){
			System.out.println("No vip");
			return;
		}else if (vip.size()>0){
			System.out.println("*******************vip********************");
			System.out.println("User: " + userName + execute("InnService.npc", new Object[] {userID,vip.get(0)}));
			System.out.println("*******************vip********************");
		}
		for(int i=0;i<group1.size();++i){
			System.out.println("User: " + userName + execute("InnService.drink", new Object[] {userID,group1.get(i),0}));
			System.out.println("User: " + userName + execute("InnService.getDice", new Object[] {userID,group1.get(i)}));
			System.out.println("User: " + userName + execute("InnService.dice", new Object[] {userID,group1.get(i),0}));
			System.out.println("User: " + userName + execute("InnService.receiveDice", new Object[] {userID,group1.get(i)}));
			System.out.println("User: " + userName + execute("InnService.npc", new Object[] {userID,group1.get(i)}));
			System.out.println("User: " + userName + execute("InnService.eatFood", new Object[] {userID,group1.get(i),-1}));
			System.out.println("User: " + userName + execute("InnService.eatFood", new Object[] {userID,group1.get(i),0}));
		}
			
		while(group2.size()>vip.size()){
			for(int k=0;k<roomlist.size();++k){
				String id = roomlist.get(k).get("id").toString();
				System.out.println("User: " + userName + " "+execute("InnService.clean", new Object[] {userID,id}));
			}
			int count=Math.min(roomlist.size(), group2.size());

			for(int k=0;k<count;++k){
				System.out.println("User: " + userName + execute("InnService.rest", new Object[] {userID,group2.get(0)}));
				group2.remove(0);
			}
			
			for(int k=0;k<4;++k){
				System.out.println("Sleeping...in the counts of... "+(3-k));
				toSleep(130000/4);
				execute("LoginService.refresh", new Object[] {userID,0});
				
			}
		}
		//				System.out.println("Name = "+name+", type = "+type+", id = "+id);
	}
	
	public static long myTimer(long milisec){
		long count = 0;
		long startTime = System.currentTimeMillis();
		
		while(count<milisec){
			count = System.currentTimeMillis() - startTime;
		}
		
		return count;
	}
	public static void levelUp() throws Exception
	{
		System.out.println(execute("UserService.levelUp", new Object[] {userID}));	
	}
	
	public static Object refresh() throws Exception{
		return execute("LoginService.refresh", new Object[] {userID,0});
	}
	
	public static Object execute(String command, Object[] input) throws Exception{
		String error="";
		
		ASObject content = (ASObject)amfConnection.call(command, input);
		error = content.get("error_msg").toString();
		if (error.contains("大俠，您的帳號好久沒活動了，請退出遊戲重新登錄")){
			toSleep(2*60*1000);
			throw new Exception();
		}
		return content;
	}
	public static AMFConnection connect(String _userID,String userPWD,String server){
		String baseUrl="http://et-"+server+".play168.com.tw";
		String uid = "";
		String strSID = "";
		String responses = "";
		URL serverAddress = null;
		HttpURLConnection connection = null;
		DataOutputStream wr2 = null;
		BufferedReader rd  = null;

		try 
		{
			serverAddress = new URL(baseUrl+"/passport.php");

			//set up out communications stuff
			connection = (HttpURLConnection)serverAddress.openConnection();
			//connection = (HttpURLConnection)serverAddress.openConnection(proxy);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);          
			connection.setReadTimeout(10000);                              

			//35
			String urlParameters = "opid=67&server_id=22600"+server+"&login_pt="+_userID+"&app_type=5&device=Linux 3.0.31-383479&action=login&password="+userPWD+"&version=1.9.0";
			

			wr2 = new DataOutputStream(connection.getOutputStream());
			wr2.writeBytes(urlParameters);
			wr2.flush();
			wr2.close();
			connection.connect();

			rd  = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = rd.readLine()) != null)
			{              
				sb.append(line + '\n');    
			}                  
			System.out.println("[-"+sb.toString()+"-]");
			responses = sb.toString();
			
		} 
		catch (Exception e) 
		{          
			e.printStackTrace();      
		} 
		finally      
		{          
			//close the connection, set all objects to null          
			connection.disconnect();          
			rd = null;          
			wr2 = null;          
			connection = null;      
		}	

		Map<String, String> map = getQueryMap(responses);
		strSID = map.get("\"sid\"");
		strSID = strSID.substring(1, strSID.length()-1);
		uid = map.get("\"uid\"");
		userID = Integer.parseInt(uid.substring(1,uid.length()-1));
		
		
		AMFConnection amfConnection = new AMFConnection();
		try{
		amfConnection.addHttpRequestHeader("Content-type", "application/x-amf");
		amfConnection.addHttpRequestHeader("Cookie", "UUZUSESSID="+strSID);
		amfConnection.addHttpRequestHeader("x-flash-version", "11,6,602,169");
		amfConnection.addHttpRequestHeader("Referer	app", "/MainTwAd.swf");
		amfConnection.connect(baseUrl+"/amf/gateway.php?UUZUSESSID="+strSID);
		}catch(Exception e){
			e.printStackTrace();
		}
		return amfConnection;
		
	}
	public static long showRandomInteger(int aStart, int aEnd, Random aRandom)
	{
		if ( aStart > aEnd ) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		//get the range, casting to long to avoid overflow problems
		long range = (long)aEnd - (long)aStart + 1;
		// compute a fraction of the range, 0 <= frac < range
		long fraction = (long)(range * aRandom.nextDouble());
		return (fraction + aStart);    
	}	
}
