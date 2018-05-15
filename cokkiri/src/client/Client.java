package client;


import java.util.HashMap;
import utill_network.Peer;


public class Client {
	
	
	private static HashMap<String, Peer> peerList;

	
	
	//임시
	private static void putPeerList() {
		peerList = new HashMap<String, Peer>();
//		peerList.put("1000",new Peer("1000","192.168.10.4",3333));
		peerList.put("1001",new Peer("1001","192.168.10.5",3333));
		peerList.put("1002",new Peer("1002","192.168.10.6",3333));
	}
	
	//broadcast
	public static void broadcast(String data) {
		
		//임시
		putPeerList();
		
		for(Peer peer : peerList.values()) {
			new Connection(data,peer).start();
		}
	}
	
	//함수 이름 바꿀거임 
	//하나의 peer에게 data 전송
	public static void sendMsg(String data, Peer peer) {
		new Connection(data,peer).start();
	}
	


}
