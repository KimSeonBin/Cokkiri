package client;


import java.util.ArrayList;
import java.util.HashMap;
import utill_network.Peer;
import utill_network.PeerList;


public class Client {
	
	

	
<<<<<<< HEAD
=======
	
	//�ӽ�
	private static void putPeerList() {
		peerList = new HashMap<String, Peer>();

	//	peerList.put("1000",new Peer("1000","192.168.10.4",3333));
	//	peerList.put("1001",new Peer("1001","192.168.10.5",3333));

		//peerList.put("1002",new Peer("1002","192.168.10.6",3333));
//		peerList.put("1003",new Peer("1003","192.168.10.7",3333));

		peerList.put("2000", new Peer("2000", "128.0.0.1", 3333));

	}
	
>>>>>>> branch 'master' of https://github.com/KimSeonBin/Cokkiri.git
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
