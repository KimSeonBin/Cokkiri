package client;


import java.util.ArrayList;
import java.util.HashMap;
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
	


}
