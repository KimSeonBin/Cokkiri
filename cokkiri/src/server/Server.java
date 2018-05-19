package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import blockchain.Block;
import client.Client;
import coin.Coin;
import mining.Mining;
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
		connectAll();
	}
	
	
	public void run() {
		
		//---------------------처음 클라이언트로 부터 ping 받는 부분-------------------------------------------//
		String clientMsg=readMessage();
		System.out.println("[server] connected peer id : "+clientMsg+"...................");
		
		if(clientMsg.equals(MsgType.DNS_MSG)) {
			sendMessage(MsgType.DNS_MSG);
			disconnectAll();
		}
		
		//----------peerlist를 검색해  기존노드, 새로운노드인지 msg 보냄------------------//
		Peer conPeer=new Peer(clientMsg,socket.getInetAddress().getHostAddress(),socket.getLocalPort());
		boolean isexist = true;
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
				System.out.println("[ServerErr] peerAuthentication Fail");
				disconnectAll();
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
			receivedTransction();
			
		}
		else if(clientMsg.equals(MsgType.BLOCK_TRANSFER_MSG)) {
			receivedBlock();
			
		}
		else {
			System.out.println("err");
			return;
		}	
	}
	
	public void receivedTransction() {
		//System.out.println("[server] received data\r\n =>" );
		
	JSONObject txStr = null;
	try {
		txStr = (JSONObject) new JSONParser().parse(readMessage());
		System.out.println("Json : "+txStr);
				
		Transaction tx = new Transaction();
		tx.convertClassObject(txStr);
			
		if(checkTransaction(tx)) {
		//Coin.blockchain.transactionPool.add(tx);
		Mining.transactionPool.add(tx);
		}
				
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	public void receivedBlock() {
		//System.out.println("[server] received block\r\n =>");
		
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
					Coin.blockchain.storeBlock(block);
					removeTx(block);
					Coin.blockchain.getUTXOs();
					//---------------------------NEW transaction를 다른 peer에게 broadcast------------//
					//Client.broadcast(MsgType.BLOCK_TRANSFER_MSG+preBlockHash+" "+blockMsg);
					//---------------------------------------------------------------------------//
				}
				else {return;}
			
				
			} catch (ParseException e) {
				e.printStackTrace();
				return;
			}
			

		}else {return;}
	}
	
	//block을 매개변수로 받아 transaction pool 에 겹치는 블록 제거 (block 전파 받은 경우 수행)
	public void removeTx(Block newblock) {

		ArrayList<Transaction> transactions = new ArrayList<Transaction>(); //새로운 블록안의 tx
		transactions.addAll(newblock.transactions);
		Transaction tmp=new Transaction();
		int size=transactions.size();
		System.out.println("@@@removeTx()");
		ArrayList<Transaction> txpool = new ArrayList<Transaction>(); //txpool의 ㅅㅌ
		txpool.addAll(Mining.transactionPool);
		
		for(int i=0;i<size;i++) {
			tmp=transactions.get(i);
			System.out.println(tmp.toJSONObject());
			Iterator it = txpool.iterator();
			while(it.hasNext()) {
				Transaction tmpp = (Transaction) it.next();
				if(tmp.TxId.equals(tmpp.TxId)) Mining.transactionPool.remove(tmpp);
			}
			/////////////////////
			// + 현재 채굴중인 블록있다면 그것에 대해서도 해야한다
			/////////////////////
		}
		System.out.println("------------------------");
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
	
	
	public void connectAll() {
		
		try {
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));	
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectInputStream = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	public void disconnectAll() {
		try {
			bufferedReader.close();
			objectInputStream.close();
			socket.close();//접속종료
			System.out.println("[server] finish run()............................");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}