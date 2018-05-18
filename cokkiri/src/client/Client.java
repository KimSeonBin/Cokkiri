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
		Peer server = PeerList.getPeerList().get(0);
		
		String req = MsgType.REQUEST_PURCHASE + buy.toJSONObject().toJSONString();
		new Connection(req, server).start(); //이렇게 하면 안될거같긴한데 일단..
		
		return false;
	}
	
	public static boolean processSell(Float coinvalue) {
		
		RequestSell sell = new RequestSell(coinvalue);
		Peer server = PeerList.getPeerList().get(0);
		
		String req = MsgType.REQUEST_SELL + sell.toJSONObject().toJSONString();
		new Connection(req, server).start(); //이렇게 하면 안될거같긴한데 일단..
		

		return false;
	}
	
	
}
