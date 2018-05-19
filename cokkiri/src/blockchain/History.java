package blockchain;

import java.util.ArrayList;
import java.util.Iterator;

import coin.Coin;
import transaction.Transaction;
import transaction.TransactionOutput;
import wallet.Address;

public class History{
	public static ArrayList<Transaction> sendTx = new ArrayList<Transaction>();
	public static ArrayList<Transaction> receiveTx = new ArrayList<Transaction>();
	
	public static void getHistory(Address address) {
		Coin.blockchain.getAllTx();
		ArrayList<Transaction> allTx = new ArrayList<Transaction>();
		allTx.addAll(Coin.blockchain.allTx);
		if(allTx==null) return;
		
		Iterator<Transaction> itTx=allTx.iterator();
	    System.out.println("getHistory()");

		while(itTx.hasNext()) { //거래하나 확인
		    Transaction tmp = itTx.next();
		    
		    System.out.println(tmp.toJSONObject());
		    
		    //extract user history
		    if(tmp.sender!=null) {
		    	if(tmp.sender.getString().equals(address.getString()) && !sendTx.contains(tmp)) {
		    		sendTx.add(tmp);
		    	}
		    }
		    if(tmp.receiver.getString().equals(address.getString())&& !receiveTx.contains(tmp)) {
		    	receiveTx.add(tmp);
		    }
		}
	}
	
	public static String stringSended(Address address) {
		String result="";
		Iterator<Transaction> send = sendTx.iterator();
		while(send.hasNext()) {
			
			Transaction t= send.next();
			System.out.println("sended string : "+ t.toJSONObject());
			String receiver =  t.receiver.getString();
			float value = 0;
			Iterator<TransactionOutput> itOutput = t.outputs.iterator();
	    	while(itOutput.hasNext()) {
	    		TransactionOutput tmpOutput=itOutput.next();
	    		System.out.println("i'm txout : "+tmpOutput.toJSONObject());
	    		if(tmpOutput.receiver.getString().equals(receiver)) {
	    			value+=tmpOutput.value;
	    		}
	    	}
	    	result+=receiver+"   "+String.valueOf(value)+"\n";
		}
		if(result=="") return null;
		return result;
	}
	
	public static String stringReceived(Address address) {
		String result="";
		Iterator<Transaction> receive = receiveTx.iterator();
		
		while(receive.hasNext()) {
			Transaction t = receive.next();
			String sender;
			
			if(t.sender.getString().equals("null")) sender="By mining";
			else  sender=t.sender.getString();
			
			float value = 0;
			Iterator<TransactionOutput> itOutput = t.outputs.iterator();
	    	while(itOutput.hasNext()) {
	    		TransactionOutput tmpOutput=itOutput.next();
	    		if(tmpOutput.receiver.getString().equals(Coin.wallet.getAddress().getString())) {
	    			value+=tmpOutput.value;
	    		}
	    	}	
	    	result+=sender+"   "+String.valueOf(value)+"\n";
		}
		if(result=="") return null;
		return result;
	}
}
