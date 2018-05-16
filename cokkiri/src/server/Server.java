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
		
		
		//---------------------ó�� Ŭ���̾�Ʈ�� ���� ping �޴� �κ�-------------------------------------------//
		String clientMsg=readMessage();
		System.out.println("[server] peer id : "+clientMsg);
		
		
		if(clientMsg.equals(MsgType.DNS_MSG)) {
			sendMessage(MsgType.DNS_MSG);
			disconnectAll();
		}
		
		//----------peerlist�� �˻���  �������, ���ο������� msg ����------------------//
		Peer conPeer=new Peer(clientMsg,socket.getInetAddress().getHostAddress(),socket.getLocalPort());
		synchronized(this){
			isexist=CentralServer.isexistPeer(conPeer);
		}
		
		
		if(isexist) {//���� peerlist�� �ִ� ��������ϰ�� Ŭ���̾�Ʈ���� ����������� �˸�
			sendMessage(MsgType.PREEXISTNODE_MSG);			
		}
		
		else {//peerlist�� ���� new node�ϰ�� -----client�κ��� ����msg �޾� ���� �� ���ο� �����߰�
			sendMessage(MsgType.NEWNODE_MSG);
				
			if(peerAuthentication(conPeer)) {		//�����ؾ��� �κ�!!!
				synchronized (this) {
					CentralServer.peerRegistry(conPeer);//peerAuthentication�� �޷�Ǹ� peelist�� �ֱ�
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
	
	//Ŭ���̾�Ʈ�� ���ϴ� �۾� ó��. 
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
					synchronized(Coin.blockchain.transactionPool) { //��ũ Ȯ������ ��� �߰�
					Coin.blockchain.transactionPool.add(tx);
					}
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*
			if(checkTransaction(tx)) {
				//---------------------------NEW transaction ó���κ� �߰�------------------------//
				//---------------------------------------------------------------------------//
				
				//---------------------------NEW transaction�� �ٸ� peer���� broadcast------------//
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
						//-----------------------------NEW Block ��ȿ�� �˻�---------------------------//
						//--------------------------------------------------------------------------//
						
						Coin.blockchain.blockchain.add(block);	
						Coin.blockchain.removeTx(block);
						
						
						//---------------------------NEW transaction�� �ٸ� peer���� broadcast------------//
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
		//transaction ����
		return true;
	}
	
	public boolean checkPreBlockHash(String preBlockHash) {
		//preBlockHash Ȯ��
		return true;
	}
	
	public boolean checkBlock(Block block) {
		//Block ��ȿ���˻�
		return true;
	}
	
	
	//���ο� ����� ��� ���������� �����Ѵ�. 
	public boolean peerAuthentication(Peer conPeer) {
		ServerUnknownProcess unknownProcess = new ServerUnknownProcess(objectOutputStream, objectInputStream, conPeer);
		return unknownProcess.init();
	}
	
	//client�� peerlist ��û�� peerlist �ҷ���
	public void getPeerlist() {
		synchronized (this) {
			//CentralServer.getPeerList();
		}
	}	

	//�޽����� ������
	public void sendMessage(String msg) {
		try {
			bufferedWriter.write(msg);
			bufferedWriter.newLine();//readLine()���� �����Ƿ� ���ٳ��� �˸�
			bufferedWriter.flush();
		}//try 
		catch (Exception e) {
			// TODO: handle exception
		}//catch
	}
	
	//�޽����� �о�´�
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
			socket.close();//��������
			System.out.println("[server] finish run()");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
