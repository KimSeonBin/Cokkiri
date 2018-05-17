package client;


import java.util.ArrayList;
import java.util.HashMap;
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
	


}
