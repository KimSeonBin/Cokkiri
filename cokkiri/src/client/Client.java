package client;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

import coin.TransferBlocks;
import exchange.RequestBuy;
import exchange.RequestSell;
import mining.Mining;
import utill_network.MsgType;
import utill_network.Peer;
import utill_network.PeerList;


public class Client {
	
	private static ArrayList<Peer> peerList;	

	
	//broadcast
	public static void broadcastToAdmin(String data) {
		

		peerList = PeerList.getAdminPeerList();

		System.out.println("broadcastToAdmin");
		System.out.println(peerList);
		for(Peer peer : peerList) {
			new Connection(data,peer).start();
		}
		
		
	
	}

	//broadcast
	public static void broadcastToPC(String data) {
		
		peerList = PeerList.getPcPeerList();
		System.out.println("broadcastToPC");
		System.out.println(peerList);
		for(Peer peer : peerList) {
			new Connection(data,peer).start();
		}
		
	}

	//broadcast
	public static void broadcastToAndorid(String data) {
		
		peerList = PeerList.getAndroidPeerList();
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
		String req = MsgType.REQUEST_PURCHASE + buy.toJSONObject().toJSONString();
		broadcastToAdmin(req);
	
		return false;
	}
	
	public static boolean processSell(Float coinvalue) {
		System.out.println("Client.java processSell()");
		
		RequestSell sell = new RequestSell(coinvalue);
		String req = MsgType.REQUEST_SELL + sell.toJSONObject().toJSONString();
		broadcastToAdmin(req);
	

		return false;
	}
	
	public static void requestBlock(long start, long end) {
		TransferBlocks req = new TransferBlocks();
		req.setIndex(start, end);
		String data = MsgType.BLOCK_REQ_MSG+req.toReqJSON().toJSONString();
		System.out.println("requestBlock");
		sendMsg(data,new Peer("1000","192.168.10.4",3333));
		//broadcastToAdmin(data);

	}
	
	
}
