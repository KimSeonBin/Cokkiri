package client;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

import exchange.RequestBuy;
import exchange.RequestSell;
import mining.Mining;
import utill_network.MsgType;
import utill_network.Peer;
import utill_network.PeerList;


public class Client {
	
	

	//broadcast
	public static void broadcast(String data) {
		
		//임시  
		ArrayList<Peer> peerList = PeerList.getPeerList();
		
		for(Peer peer : peerList) {
			new Connection(data,peer).start();
		}
	}
	
	//함수 이름 바꿀거임 
	//하나의 peer에게 data 전송
	public static void sendMsg(String data, Peer peer) {
		new Connection(data,peer).start();
	}
	
	
	public static boolean processBuy(Float coinvalue) {
		
		RequestBuy buy = new RequestBuy(coinvalue);
		Peer server = new Peer("tmp", "tmp" ,3333);
		
		String req = "tmp";
		//buy.toJSONObject() string으로 변환하여..
		new Connection(req, server).start(); //이렇게 하면 안될거같긴한데 일단..
		
		String requestType = "tmp";		//응답 받아오기

		if(requestType.equals(MsgType.ANSWER_OK)) {
			String buyinfo = "tmp";
			//buy.buyJSONObject() string으로 변환하여..

			new Connection(buyinfo, server).start();
			
			String response="tmp"; //응답 받아오기
			
			if(response.equals(MsgType.ANSWER_NO)) {
				return false;
			}
			
			buy.checkTxMsg(response);
			
			return true;
			
		}else if(requestType.equals(MsgType.ANSWER_NO)){
			//통신 끝
			return false;
		}
		else {
			System.out.println("client.java processExchange() err");
		}
		return false;
	}
	
	public static boolean processSell(Float coinvalue) {
		
		RequestSell sell = new RequestSell(coinvalue);
		Peer server = PeerList.getPeerList().get(0);
		
		String req = MsgType.REQUEST_SELL + sell.toJSONObject().toJSONString();
		//buy.toJSONObject() string으로 변환하여..
		new Connection(req, server).start(); //이렇게 하면 안될거같긴한데 일단..
		
		String requestType = "tmp";		//응답 받아오기
/*
		if(requestType.equals(MsgType.ANSWER_OK)) {
			JSONObject tx = sell.txJSONObject();
			
			String sellinfo = tx.toJSONString();
			//tx string으로 변환하여..

			new Connection(sellinfo, server).start();
			
			String response="tmp"; //응답 받아오기
			
			//response에는 cash 금액 담겨있음
			//노드의 cash 에 이 금액을 + 하여적용
			

			//-------------tx전파---------------//
			System.out.println("[ClientSendlog] : BroadCast Transaction");
			
			new Thread() {
				public void run() {
					try {
						Client.broadcast(MsgType.TRANSACTION_MSG+tx);
					} catch (Exception e) {}
				}
			}.start();
			//--------------------------------//
			
			return true;
			
		}else if(requestType.equals(MsgType.ANSWER_NO)){
			//통신 끝
			return false;
		}
		else {
			System.out.println("client.java processExchange() err");
		}*/
		return false;
	}
}
