package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utill_network.Peer;
import utill_network.PeerQueue;

public class CentralServer {

	private final static int port = 3333;
	private final static int numThreads = 30;
	private static PeerQueue<Socket> peerQueue; 
	private static HashMap<String, Peer> peerList;
	
	//putPeerList
	private static void putPeerList() {
		peerList.put("1000",new Peer("1000","192.168.10.4",3333));
		peerList.put("1001",new Peer("1001","192.168.10.5",3333));
//		peerList.put("1002",new Peer("1002","192.168.10.6",3333));
	}
	

	
	//waiting for peer
	public static void connection() throws IOException {
		
		ServerSocket serverSocket = new ServerSocket(port);
		
		while(true) {
			System.out.println("\n[server] Waiting for peer");
			Socket socket = serverSocket.accept();
			synchronized(peerQueue) {
				peerQueue.add(socket);
			}
		}//while
	}//connection
	
	//���ӽ�
	public static void income() throws IOException {
		ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
		System.out.println("\n[server] income() start\n");
		while(true) {
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(peerQueue.peek() == null)
				continue;
			synchronized (peerQueue) {
				
				Socket socket = peerQueue.pool();
				//peerList.add(new Peer(socket.getInetAddress().getHostAddress(),socket.getPort()));
				executorService.execute(new Server(socket));	
			}
		}//while
	}//income
	
	
	//���ο� ��� peerList�� �߰�
	public static void peerRegistry(Peer newPeer) {
		peerList.put(newPeer.getId(), newPeer);
	}
	
	//������ ��尡 peerlist�� �ִ��� Ȯ��
	public static boolean isexistPeer(Peer newPeer) {//���� peerList�� ���ϴ� �κ�
		
		//peerlist�� �����ϸ� true
		if(peerList.containsKey(newPeer.getId())) {
			System.out.println("PreExist Peer");
			return true;
		}
		//�������� ������ false
		else {
			System.out.println("New Peer");
			return false;
		}
		
	
	}
	
	//peerlist����� �ش�.
	public static HashMap<String, Peer> getPeerList() {
		return peerList;	
	}
	
	
	
	public static void CentralServer() {
		// TODO Auto-generated method stub
		
		peerList = new HashMap();
		putPeerList();
		peerQueue = new PeerQueue<Socket>();
		System.out.println("\n[server] CentralServer start");

		new Thread() {
			public void run() {
				try {
					connection();
				} catch (Exception e) {}
			}
		}.start();
		
		
		new Thread() {
			public void run() {
				try {
					income();
				} catch (Exception e) {}
			}
		}.start();
		
	}

}
