package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import utill_network.MsgType;
import utill_network.NodeId;
import utill_network.Peer;

public class Connection extends Thread{
	
	private Socket socket;
	private BufferedWriter bufferedWriter;
	private BufferedReader bufferedReader;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	
	
	private String data = null;
	private Peer peer = null;
	
	public Connection(String data, Peer peer) {
		
		this.data = data;
		this.peer = peer;
		
		try {
			socket = new Socket(peer.getIpAddress(),peer.getPort());			
			
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));	
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			
		}catch(Exception e) {}
		
	}
	

	//초기 id 보내고 ping
	public void run() {
		
		String serverMsg;
		
		//server에게 id를 보낸다
		sendMessage(NodeId.getNodeId());
		
		//server로부터 server에게 기존노드인지 새로운노드인지 메시지 받음
		serverMsg = readMessage();
		System.out.println("[Client Debug] serverMsg : " +serverMsg);
		if(serverMsg==null) {
			System.out.println("err");
			return;
		}
		if(serverMsg.equals(MsgType.NEWNODE_MSG)) {//새로운 노드이면
			System.out.println("[client] I have to send authentication message");

			//인증과정수행
			ClientUnknownProcess unknown = new ClientUnknownProcess(socket, objectOutputStream, objectInputStream);
			if(unknown.initProcess()) {
				System.out.println("[client] finish authentication message");
			}
			else {
				
				return;
			}
		}
		else if(serverMsg.equals(MsgType.PREEXISTNODE_MSG)) {//기존노드이면
			//원하는 과정수행
			
		}
		else {
			System.out.println("err");
			return;
		}
		
		process(data);//원하는 과정 수행
	}	
		
	//클라이언트가 원하는 과정을 서버에게 보내고 수행
	public void process(String data) {
				
		if(data.contains(MsgType.TRANSACTION_MSG)) {
			
			sendMessage(MsgType.TRANSACTION_MSG);
			sendMessage(data.replaceFirst(MsgType.TRANSACTION_MSG, ""));//transaction data
			
		}
		else if(data.contains(MsgType.BLOCK_TRANSFER_MSG)) {
			 
			sendMessage(MsgType.BLOCK_TRANSFER_MSG);
			String preBlockHash = data.replaceFirst(MsgType.BLOCK_TRANSFER_MSG, "").split(" ")[0];//blockHeader previous blockHash
			sendMessage(preBlockHash);
			
			//---------------얼마동안 기다리다 답이 없으면 연결 끊는 부분 추가 예정 ----------------------------//
			//-----------------------------------------------------------------------------//
			
			if(readMessage().equals(MsgType.GETDATA_MSG)) {
				String blockData = data.replaceFirst(MsgType.BLOCK_TRANSFER_MSG+preBlockHash+" ", "");
				sendMessage(blockData);
			}
			
		}
		
		//Transaction transaction = new Transaction("A", "B", 10000);
		//sendMessage(""+transaction.getSender()+"/"+transaction.getReceiver()+"/"+transaction.getAmount());
		
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
	
	//메시지를 받는다
	public String readMessage() {
		
		String message = null;
		try {
			message = bufferedReader.readLine();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return message;
	}

	public void disconnectAll() {
		try {
			bufferedReader.close();
			objectInputStream.close();
			socket.close();//접속종료
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
					
		
		
}
