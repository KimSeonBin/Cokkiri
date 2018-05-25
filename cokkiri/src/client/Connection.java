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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import appactivity.ExchangeBuyFragment;
import appactivity.ExchangeSellFragment;
import coin.Cash;
import coin.Coin;
import exchange.RequestBuy;
import exchange.RequestSell;
import mining.Mining;
import server.Server;
import transaction.Transaction;
import transaction.TransactionInput;
import transaction.TransactionOutput;
import transaction.CreateTransaction;
import utill_network.MsgType;
import utill_network.NodeId;
import utill_network.Peer;
import wallet.Address;
import wallet.Wallet;

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
		
		//server에게 Node id를 보낸다
		sendMessage(NodeId.getNodeId());
		
		//server로부터 기존노드인지 새로운노드인지 메시지 받음
		serverMsg = readMessage();
		if(serverMsg == null) {
			System.out.println("err");
			disconnectAll();
			return;
		}
		
		else if(serverMsg.equals(MsgType.NEWNODE_MSG)) {//새로운 노드이면
			System.out.println("[client] I have to send authentication message");
			authenticateProcess();
			
		}
		else if(!serverMsg.equals(MsgType.PREEXISTNODE_MSG)) {
			System.out.println("err");
			disconnectAll();
			return;
		}
		
		process(data);//원하는 과정 수행
		disconnectAll();
	}	
		
	//클라이언트가 원하는 과정을 서버에게 보내고 수행
	public void process(String data) {
				
		if(data.contains(MsgType.TRANSACTION_MSG)) {
			System.out.println("[Client] TRANSACTION Transfer......................." );
			sendMessage(MsgType.TRANSACTION_MSG);
			sendMessage(data.replaceFirst(MsgType.TRANSACTION_MSG, ""));//transaction data
			System.out.println("[Client] TRANSACTION Transfer complete..............." );
		}// if MsgType.TRANSACTION_MSG
		
		else if(data.contains(MsgType.BLOCK_TRANSFER_MSG)) {
			System.out.println("[Client] BLOCK Transfer.............................." );
			sendMessage(MsgType.BLOCK_TRANSFER_MSG);
			String preBlockHash = data.replaceFirst(MsgType.BLOCK_TRANSFER_MSG, "").split(" ")[0];//blockHeader previous blockHash
			sendMessage(preBlockHash);
			System.out.println("[Client] BLOCK Transfer complete....................." );
			//---------------얼마동안 기다리다 답이 없으면 연결 끊는 부분 추가 예정 ----------------------------//
			//-----------------------------------------------------------------------------//
			
			if(readMessage().equals(MsgType.GETDATA_MSG)) {
				String blockData = data.replaceFirst(MsgType.BLOCK_TRANSFER_MSG+preBlockHash+" ", "");
				sendMessage(blockData);
			}
		}// else if MsgType.BLOCK_TRANSFER_MSG
		
		else if(data.contains(MsgType.REQUEST_SELL)) {
			System.out.println("[Client] coin sell to exchange server.............................." );
			System.out.println("checkkkkkk data : "+data);
			sendMessage(MsgType.REQUEST_SELL);
			data=data.replaceFirst(MsgType.REQUEST_SELL, "");
			sendMessage(data);
			
			String responseData = readMessage();
			
			if(responseData.contains(MsgType.ANSWER_OK)) {
				String address = readMessage();
				String password = ExchangeSellFragment.showInputPasswordDialog();
				if(Coin.wallet.authenticate(Coin.id+password)!=1) { //passwd 체크
					return;					
			    }
				
				Address serverAdd= new Address();
				serverAdd.setAddress(address);
				Wallet sender = new Wallet(Coin.id+password, false);
				System.out.println("$$$$$check  data : "+data);
				RequestSell sell = new RequestSell(data);
				System.out.println("$$$ sell.getCoin() "+String.valueOf(sell.getCoin()));
				
				String senddata = sell.txJSONObject(sender, serverAdd).toJSONString();
				System.out.println("senddata : "+senddata);
				sendMessage(senddata);
				
				String responseCash=readMessage();
				double cash = Double.parseDouble(responseCash);
				Cash.plus(cash);
				System.out.println("cash : "+ cash);
				ExchangeSellFragment.showSuccessDialog();

				
			}
			else if(responseData.contains(MsgType.ANSWER_NO)) {
				ExchangeSellFragment.showFailDialog();
				return;
			}
		}// else if MsgType.REQUEST_SELL
		
		else if(data.contains(MsgType.REQUEST_PURCHASE)) {
			System.out.println("[Client] coin buy to exchange server.............................." );
			sendMessage(MsgType.REQUEST_PURCHASE);
			data=data.replaceFirst(MsgType.REQUEST_PURCHASE, "");
			sendMessage(data);
			
			String responseData = readMessage();
			
			if(responseData.contains(MsgType.ANSWER_OK)) {
				String password = ExchangeSellFragment.showInputPasswordDialog();
				if(Coin.wallet.authenticate(Coin.id+password)!=1) { //passwd 체크
					System.out.println("return");
					return;					
			    }
				RequestBuy buy = new RequestBuy(data);
				System.out.println("buy message : "+ buy.buyJSONObject().toJSONString());
				sendMessage(buy.buyJSONObject().toJSONString());
				
				String responseTx = readMessage();
				
				if(responseTx.contains(MsgType.ANSWER_NO)) {
					ExchangeBuyFragment.showFailDialog();
					return;
				}
				else if(responseTx.contains(MsgType.TRANSACTION_MSG)) {
					receivedTransaction();
					Cash.minus(buy.getCash());

					ExchangeBuyFragment.showSuccessDialog();

				}			
			}	
			else if(responseData.contains(MsgType.ANSWER_NO)) {
				ExchangeBuyFragment.showFailDialog();
				return;
			}
		}//else if MsgType.REQUEST_PURCHASE
		else if(data.contains(MsgType.FULLBLOCK_REQ_MSG)) {
			sendMessage(MsgType.FULLBLOCK_REQ_MSG);
			receivedFullBlock(readMessage());
		}
	}
	
	public void receivedTransaction() {
		//System.out.println("[server] received data\r\n =>" );
		
	JSONObject txStr = null;
	try {
		String txx = readMessage();
		System.out.println("server tx : "+txx);

		txStr = (JSONObject) new JSONParser().parse(txx);
		System.out.println("Json : "+txStr);
				
		Transaction tx = new Transaction();
		tx.convertClassObject(txStr);
			
		//if(checkTransaction(tx)) {
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
			
		//}
				
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	public void receivedFullBlock(String data) {
		
	}
	
	public void authenticateProcess() {
		//인증과정수행
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
			socket.close();//접속종료
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
					
		
		
}
