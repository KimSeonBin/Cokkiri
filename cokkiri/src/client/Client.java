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
		
		//�ӽ�  
		ArrayList<Peer> peerList = PeerList.getPeerList();
		
		for(Peer peer : peerList) {
			new Connection(data,peer).start();
		}
	}
	
	//�Լ� �̸� �ٲܰ��� 
	//�ϳ��� peer���� data ����
	public static void sendMsg(String data, Peer peer) {
		new Connection(data,peer).start();
	}
	
	
	public static boolean processBuy(Float coinvalue) {
		
		RequestBuy buy = new RequestBuy(coinvalue);
		Peer server = new Peer("tmp", "tmp" ,3333);
		
		String req = "tmp";
		//buy.toJSONObject() string���� ��ȯ�Ͽ�..
		new Connection(req, server).start(); //�̷��� �ϸ� �ȵɰŰ����ѵ� �ϴ�..
		
		String requestType = "tmp";		//���� �޾ƿ���

		if(requestType.equals(MsgType.ANSWER_OK)) {
			String buyinfo = "tmp";
			//buy.buyJSONObject() string���� ��ȯ�Ͽ�..

			new Connection(buyinfo, server).start();
			
			String response="tmp"; //���� �޾ƿ���
			
			if(response.equals(MsgType.ANSWER_NO)) {
				return false;
			}
			
			buy.checkTxMsg(response);
			
			return true;
			
		}else if(requestType.equals(MsgType.ANSWER_NO)){
			//��� ��
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
		//buy.toJSONObject() string���� ��ȯ�Ͽ�..
		new Connection(req, server).start(); //�̷��� �ϸ� �ȵɰŰ����ѵ� �ϴ�..
		
		String requestType = "tmp";		//���� �޾ƿ���
/*
		if(requestType.equals(MsgType.ANSWER_OK)) {
			JSONObject tx = sell.txJSONObject();
			
			String sellinfo = tx.toJSONString();
			//tx string���� ��ȯ�Ͽ�..

			new Connection(sellinfo, server).start();
			
			String response="tmp"; //���� �޾ƿ���
			
			//response���� cash �ݾ� �������
			//����� cash �� �� �ݾ��� + �Ͽ�����
			

			//-------------tx����---------------//
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
			//��� ��
			return false;
		}
		else {
			System.out.println("client.java processExchange() err");
		}*/
		return false;
	}
}
