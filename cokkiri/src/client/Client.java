package client;


import java.util.HashMap;
import utill_network.Peer;


public class Client {
	
	
	private static HashMap<String, Peer> peerList;

	
	
	//�ӽ�
	private static void putPeerList() {
		peerList = new HashMap<String, Peer>();
//		peerList.put("1000",new Peer("1000","192.168.10.4",3333));
		peerList.put("1001",new Peer("1001","192.168.10.5",3333));
		peerList.put("1002",new Peer("1002","192.168.10.6",3333));
	}
	
	//broadcast
	public static void broadcast(String data) {
		
		//�ӽ�
		putPeerList();
		
		for(Peer peer : peerList.values()) {
			new Connection(data,peer).start();
		}
	}
	
	//�Լ� �̸� �ٲܰ��� 
	//�ϳ��� peer���� data ����
	public static void sendMsg(String data, Peer peer) {
		new Connection(data,peer).start();
	}
	


}
