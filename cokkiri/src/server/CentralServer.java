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
import utill_network.PeerList;
import utill_network.PeerQueue;

public class CentralServer {

	private final static int port = 3333;
	private final static int numThreads = 30;
	private static PeerQueue<Socket> peerQueue; 
	private static HashMap<String, Peer> peerList;
	private static ArrayList<Peer> peerArray;
	
	//putPeerList
	private static void putPeerList() {
		peerArray=PeerList.getPeerList();
		
		for(Peer peer : peerArray) {
			peerList.put(peer.getId(), peer);
		}
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
	
	//접속시
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
	
	
	//새로운 노드 peerList에 추가
	public static void peerRegistry(Peer newPeer) {
		peerList.put(newPeer.getId(), newPeer);
	}
	
	//접속한 노드가 peerlist에 있는지 확인
	public static boolean isexistPeer(Peer newPeer) {//기존 peerList와 비교하는 부분
		
		//peerlist에 존재하면 true
		if(peerList.containsKey(newPeer.getId())) {
			System.out.println("PreExist Peer");
			return true;
		}
		//존재하지 않으면 false
		else {
			System.out.println("New Peer");
			return false;
		}
		
	
	}
	
	//peerlist목록을 준다.
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
