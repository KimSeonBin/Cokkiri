package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import blockchain.Block;
import client.Client;
import coin.Coin;
import transaction.Transaction;
import utill_network.MsgType;
import utill_network.Peer;

public class Server extends Thread {
	private Socket socket;
	BufferedWriter bufferedWriter;
	BufferedReader bufferedReader;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	//final static String TRANSACTION_MSG = "send Transaction";

	public Server(Socket socket) {
		this.socket = socket;
	}
	
	
	public void run() {
		
		boolean isexist = true;
		
		
		try {
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));	
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectInputStream = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//---------------------처음 클라이언트로 부터 ping 받는 부분-------------------------------------------//
		String clientMsg=readMessage();
		System.out.println("[server] peer id : "+clientMsg);
		
		
		if(clientMsg.equals(MsgType.DNS_MSG)) {
			sendMessage(MsgType.DNS_MSG);
			disconnectAll();
		}
		
		//----------peerlist를 검색해  기존노드, 새로운노드인지 msg 보냄------------------//
		Peer conPeer=new Peer(clientMsg,socket.getInetAddress().getHostAddress(),socket.getLocalPort());
		synchronized(this){
			isexist=CentralServer.isexistPeer(conPeer);
		}
		
		
		if(isexist) {//서버 peerlist에 있던 기존노드일경우 클라이언트에게 기존노드임을 알림
			sendMessage(MsgType.PREEXISTNODE_MSG);			
		}
		
		else {//peerlist에 없는 new node일경우 -----client로부터 인증msg 받아 인증 후 새로운 노드로추가
			sendMessage(MsgType.NEWNODE_MSG);
				
			if(peerAuthentication(conPeer)) {		//수정해야할 부분!!!
				synchronized (this) {
					CentralServer.peerRegistry(conPeer);//peerAuthentication이 왼료되면 peelist에 넣기
				}
			}
			else {
				return;
				}
		}
		//--------------------------------------------------------------------//
		
		process();
		disconnectAll();
		
	}
	
	//클라이언트가 원하는 작업 처리. 
	public void process() {
	
		String clientMsg = readMessage();
		
		System.out.println("[server] peer msg : "+ clientMsg);
		
		
		if(clientMsg.equals(MsgType.TRANSACTION_MSG)) {
			
			System.out.println("[server] received data\r\n =>" );
			
			JSONObject txStr = null;
			try {
				txStr = (JSONObject) new JSONParser().parse(readMessage());
				System.out.println("Json : "+txStr);
				
				Transaction tx = new Transaction();
				tx.convertClassObject(txStr);
				
				if(checkTransaction(tx)) {
					synchronized(Coin.blockchain.transactionPool) { //싱크 확인위해 잠시 추가
					Coin.blockchain.transactionPool.add(tx);
					}
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*
			if(checkTransaction(tx)) {
				//---------------------------NEW transaction 처리부분 추가------------------------//
				//---------------------------------------------------------------------------//
				
				//---------------------------NEW transaction를 다른 peer에게 broadcast------------//
				Client.broadcast(MsgType.TRANSACTION_MSG+txStr);
				//---------------------------------------------------------------------------//
			}
			*/
			
		}
		else if(clientMsg.equals(MsgType.BLOCK_TRANSFER_MSG)) {
			System.out.println("[server] received block\r\n =>");
			
			String preBlockHash = readMessage();
			if(checkPreBlockHash(preBlockHash)){
				sendMessage(MsgType.GETDATA_MSG);
				
				JSONObject blockStr = null;
				try {
					blockStr = (JSONObject) new JSONParser().parse(readMessage());
					System.out.println("[Server] BroadcastedBlock log Block Json : "+blockStr);
					Block block = new Block();
					block.convertClassObject(blockStr);
					
					System.out.println("[Server] : Broadcasted Block is  : "+ block.getString());
				
					if(checkBlock(block)) {
						//-----------------------------NEW Block 유효성 검사---------------------------//
						//--------------------------------------------------------------------------//
						
						Coin.blockchain.blockchain.add(block);	
						Coin.blockchain.removeTx(block);
						
						
						//---------------------------NEW transaction를 다른 peer에게 broadcast------------//
						//Client.broadcast(MsgType.BLOCK_TRANSFER_MSG+preBlockHash+" "+blockMsg);
						//---------------------------------------------------------------------------//
					}
					else {return;}
				
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				

			}else {return;}
		}
		else {
			System.out.println("err");
			return;
		}	
	}
	
	public boolean checkTransaction(Transaction tx) {
		//transaction 검증
		return true;
	}
	
	public boolean checkPreBlockHash(String preBlockHash) {
		//preBlockHash 확인
		return true;
	}
	
	public boolean checkBlock(Block block) {
		//Block 유효성검사
		return true;
	}
	
	
	//새로운 노드일 경우 인증과정을 수행한다. 
	public boolean peerAuthentication(Peer conPeer) {
		ServerUnknownProcess unknownProcess = new ServerUnknownProcess(objectOutputStream, objectInputStream, conPeer);
		return unknownProcess.init();
	}
	
	//client가 peerlist 요청시 peerlist 불러옴
	public void getPeerlist() {
		synchronized (this) {
			//CentralServer.getPeerList();
		}
	}	

	//메시지를 보낸다
	public void sendMessage(String msg) {
		try {
			bufferedWriter.write(msg);
			bufferedWriter.newLine();//readLine()으로 읽으므로 한줄끝을 알림
			bufferedWriter.flush();
		}//try 
		catch (Exception e) {
			// TODO: handle exception
		}//catch
	}
	
	//메시지를 읽어온다
	public String readMessage() {
		String message = null;
		try {
			message = bufferedReader.readLine();
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return message;
	}
	
	
	public void disconnectAll() {
		try {
			bufferedReader.close();
			objectInputStream.close();
			socket.close();//접속종료
			System.out.println("[server] finish run()");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
