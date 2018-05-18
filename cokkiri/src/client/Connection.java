package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

import org.json.simple.JSONObject;

import appactivity.ExchangeSellFragment;
import exchange.RequestSell;
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
	private static int timeout = 3000;
	
	
	public Connection(String data, Peer peer) {
		
		this.data = data;
		this.peer = peer;
		
		SocketAddress socketAddress = new InetSocketAddress(peer.getIpAddress(), peer.getPort());
		socket = new Socket();	
		
		try {		
			socket.connect(socketAddress,timeout);
			
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));	
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			
		}
		catch(Exception e) { 
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	

	public void run() {
		
		String serverMsg;
		
		if(socket.isClosed()) {return;}
		
		//server���� Node id�� ������
		sendMessage(NodeId.getNodeId());
		
		//server�κ��� ����������� ���ο������� �޽��� ����
		serverMsg = readMessage();
		if(serverMsg == null) {
			System.out.println("err");
			disconnectAll();
			return;
		}
		
		else if(serverMsg.equals(MsgType.NEWNODE_MSG)) {//���ο� ����̸�
			System.out.println("[client] I have to send authentication message");
			authenticateProcess();
			
		}
		else if(!serverMsg.equals(MsgType.PREEXISTNODE_MSG)) {
			System.out.println("err");
			disconnectAll();
			return;
		}
		
		process(data);//���ϴ� ���� ����
		disconnectAll();
	}	
		
	//Ŭ���̾�Ʈ�� ���ϴ� ������ �������� ������ ����
	public void process(String data) {
				
		if(data.contains(MsgType.TRANSACTION_MSG)) {
			System.out.println("[Client] TRANSACTION Transfer......................." );
			sendMessage(MsgType.TRANSACTION_MSG);
			sendMessage(data.replaceFirst(MsgType.TRANSACTION_MSG, ""));//transaction data
			System.out.println("[Client] TRANSACTION Transfer complete..............." );
		}
		else if(data.contains(MsgType.BLOCK_TRANSFER_MSG)) {
			System.out.println("[Client] BLOCK Transfer.............................." );
			sendMessage(MsgType.BLOCK_TRANSFER_MSG);
			String preBlockHash = data.replaceFirst(MsgType.BLOCK_TRANSFER_MSG, "").split(" ")[0];//blockHeader previous blockHash
			sendMessage(preBlockHash);
			System.out.println("[Client] BLOCK Transfer complete....................." );
			//---------------�󸶵��� ��ٸ��� ���� ������ ���� ���� �κ� �߰� ���� ----------------------------//
			//-----------------------------------------------------------------------------//
			
			if(readMessage().equals(MsgType.GETDATA_MSG)) {
				String blockData = data.replaceFirst(MsgType.BLOCK_TRANSFER_MSG+preBlockHash+" ", "");
				sendMessage(blockData);
			}
		}
		else if(data.contains(MsgType.REQUEST_SELL)) {
			System.out.println("[Client] coin sell to exchange server.............................." );
			sendMessage(MsgType.REQUEST_SELL);
			sendMessage(data.replaceFirst(MsgType.REQUEST_SELL, ""));
			
			String responseData = readMessage();

			if(responseData.contains(MsgType.ANSWER_OK)) {
				String password = ExchangeSellFragment.showInputPasswordDialog();
				
			}
			else if(responseData.contains(MsgType.ANSWER_NO)) {
				ExchangeSellFragment.showFailDialog();
				return;
			}
		}
		else if(data.contains(MsgType.REQUEST_PURCHASE)) {
			
		}
		
	
	}
	public void authenticateProcess() {
		//������������
		ClientUnknownProcess unknown = new ClientUnknownProcess(socket, objectOutputStream, objectInputStream);
		if(unknown.initProcess()) {
			System.out.println("[client] finish authentication message");
		}
		else {
			
			return;
		}
	}
		
	public void sendMessage(String msg) {
		
		try {
			bufferedWriter.write(msg);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
