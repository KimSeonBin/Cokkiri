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
	

	//�ʱ� id ������ ping
	public void run() {
		
		String serverMsg;
		
		//server���� id�� ������
		sendMessage(NodeId.getNodeId());
		
		//server�κ��� server���� ����������� ���ο������� �޽��� ����
		serverMsg = readMessage();
		System.out.println("[Client Debug] serverMsg : " +serverMsg);
		if(serverMsg==null) {
			System.out.println("err");
			return;
		}
		if(serverMsg.equals(MsgType.NEWNODE_MSG)) {//���ο� ����̸�
			System.out.println("[client] I have to send authentication message");

			//������������
			ClientUnknownProcess unknown = new ClientUnknownProcess(socket, objectOutputStream, objectInputStream);
			if(unknown.initProcess()) {
				System.out.println("[client] finish authentication message");
			}
			else {
				
				return;
			}
		}
		else if(serverMsg.equals(MsgType.PREEXISTNODE_MSG)) {//��������̸�
			//���ϴ� ��������
			
		}
		else {
			System.out.println("err");
			return;
		}
		
		process(data);//���ϴ� ���� ����
	}	
		
	//Ŭ���̾�Ʈ�� ���ϴ� ������ �������� ������ ����
	public void process(String data) {
				
		if(data.contains(MsgType.TRANSACTION_MSG)) {
			
			sendMessage(MsgType.TRANSACTION_MSG);
			sendMessage(data.replaceFirst(MsgType.TRANSACTION_MSG, ""));//transaction data
			
		}
		else if(data.contains(MsgType.BLOCK_TRANSFER_MSG)) {
			 
			sendMessage(MsgType.BLOCK_TRANSFER_MSG);
			String preBlockHash = data.replaceFirst(MsgType.BLOCK_TRANSFER_MSG, "").split(" ")[0];//blockHeader previous blockHash
			sendMessage(preBlockHash);
			
			//---------------�󸶵��� ��ٸ��� ���� ������ ���� ���� �κ� �߰� ���� ----------------------------//
			//-----------------------------------------------------------------------------//
			
			if(readMessage().equals(MsgType.GETDATA_MSG)) {
				String blockData = data.replaceFirst(MsgType.BLOCK_TRANSFER_MSG+preBlockHash+" ", "");
				sendMessage(blockData);
			}
			
		}
		
		//Transaction transaction = new Transaction("A", "B", 10000);
		//sendMessage(""+transaction.getSender()+"/"+transaction.getReceiver()+"/"+transaction.getAmount());
		
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
	
	//�޽����� �޴´�
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
			socket.close();//��������
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
					
		
		
}
