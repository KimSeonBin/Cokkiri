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
import coin.Coin;
import coin.TransferTxs;
import mining.Mining;
import transaction.Transaction;
import transaction.TransactionInput;
import transaction.TransactionOutput;
import utill_network.MsgType;
import utill_network.Peer;
import wallet.Address;

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
		System.out.println("Server.java run()");
		//---------------------ó�� Ŭ���̾�Ʈ�� ���� ping �޴� �κ�-------------------------------------------//
		String clientMsg=readMessage();
		System.out.println("[server] connected peer id : "+clientMsg+"...................");
		System.out.println("[server] connected peer info: "+socket.toString());
		System.out.println("ip : "+socket.getInetAddress());
		if(clientMsg.equals(MsgType.DNS_MSG)) {
			sendMessage(MsgType.DNS_MSG);
			disconnectAll();
		}
		
		//----------peerlist�� �˻���  �������, ���ο������� msg ����------------------//
		Peer conPeer=new Peer(clientMsg,socket.getInetAddress().getHostAddress(),socket.getLocalPort());
		boolean isexist = true;
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
				System.out.println("[ServerErr] peerAuthentication Fail");
				disconnectAll();
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
			receivedTransction();
		}
		else if(clientMsg.equals(MsgType.BLOCK_TRANSFER_MSG)) {
			receivedBlock();
			
		}else if(clientMsg.equals(MsgType.MYTX_REQ_MSG)) {
			receivedUserTxReq();
		}
		else {
			System.out.println("err");
			return;
		}	
	}
	
	private void receivedUserTxReq() {
		System.out.println("Server.java receivedUserTxReq()");
		String addressStr = readMessage();
		Address user = new Address();
		user.setAddress(addressStr);
		
		TransferTxs txs = new TransferTxs();
		txs.setTxList(user);
		String msg = txs.toJSONObject().toJSONString();
		
		sendMessage(msg);
		System.out.println("send user txs : "+msg);
		
	}


	public void receivedTransction() {
		System.out.println("Server.java receivedTransaction()");
		
		JSONObject txStr = null;
		try {
			txStr = (JSONObject) new JSONParser().parse(readMessage());
			System.out.println("[server] tx : "+txStr);
					
			Transaction tx = new Transaction();
			tx.convertClassObject(txStr);
				
			if(checkTransaction(tx)) {
				Mining.transactionPool.add(tx);
				
				//add outputs to Unspent list
				for(TransactionOutput o : tx.outputs) {
					Coin.blockchain.UTXOs.put(o.id , o);
				}

				//remove transaction inputs from UTXO lists as spent:
				for(TransactionInput i : tx.inputs) {
					if(i.UTXO == null) continue; //if Transaction can't be found skip it 
					Coin.blockchain.UTXOs.remove(i.UTXO.id);
				}
				
			}
					
			} catch (ParseException e) {e.printStackTrace();}
		
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
					//-----------------------------NEW Block ��ȿ�� �˻�---------------------------//
					//--------------------------------------------------------------------------//
					Coin.blockchain.blockchain.add(block);
					Coin.blockchain.storeBlock(block);
					removeTx(block);
					Coin.blockchain.getUTXOs();
					//---------------------------NEW transaction�� �ٸ� peer���� broadcast------------//
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
	
	//block�� �Ű������� �޾� transaction pool �� ��ġ�� ��� ���� (block ���� ���� ��� ����)
	public void removeTx(Block newblock) {

		ArrayList<Transaction> transactions = new ArrayList<Transaction>(); //���ο� ��Ͼ��� tx
		transactions.addAll(newblock.transactions);
		Transaction tmp=new Transaction();
		int size=transactions.size();
		//System.out.println("@@@removeTx()");
		ArrayList<Transaction> txpool = new ArrayList<Transaction>(); //txpool�� ����
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
			// + ���� ä������ ����ִٸ� �װͿ� ���ؼ��� �ؾ��Ѵ�
			/////////////////////
		}
		System.out.println("------------------------");
	}	
	
	public boolean checkTransaction(Transaction tx) {
		//transaction ����
		Iterator check = Mining.transactionPool.iterator();
		while(check.hasNext()) {
			if(tx.TxId.equals(((Transaction)check).TxId)) return false;
		}
		return true;
	}
	
	public boolean checkPreBlockHash(String preBlockHash) {
		//preBlockHash Ȯ��
		return true;
	}
	
	public boolean checkBlock(Block block) {
		Iterator it = Coin.blockchain.blockchain.iterator();
		while(it.hasNext()) {
			if(block.getBlockIndex()==((Block)it.next()).getBlockIndex()){
				return false;
			}
		}
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
			socket.close();//��������
			System.out.println("[server] finish run()............................");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}