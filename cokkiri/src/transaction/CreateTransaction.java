package transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import log.Logging;
import wallet.Address;
import wallet.Wallet;

public class CreateTransaction {

	/**
	 * Generates and returns a new transaction from this wallet.
	 */
	public static Transaction createTx(Wallet sender, Address _recipient,float value ) {
				
		if(sender.getBalance() < value) { 
			System.out.println("#fail.. Not Enough coin to create Tx");
			return null;
		}

		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		HashMap<String, TransactionOutput> sortedUTXOs=sortByValue(sender.UTXOs);
		
		float total = 0;
		for (Map.Entry<String, TransactionOutput> item: sortedUTXOs.entrySet()){
			TransactionOutput UTXO = item.getValue();
			total += UTXO.value;
			TransactionInput tmp=new TransactionInput(UTXO.id);
			inputs.add(tmp);
			if(total >= value) break;
		}
		
		Transaction newTransaction = new Transaction(sender.getAddress(), _recipient , value, inputs);
		
		newTransaction.setSenderPubkey(sender.getPublicKey());
		newTransaction.generateSignature(sender.getPrivateKey());
		
		if(newTransaction.processTransaction()) {
			return newTransaction;
		}else {
			System.out.println("Transaction failed..");
			return null;
		}
	}
	

	/**
	 * �Ķ���ͷ� ���� (transactionoutput) hashmap�� valueũ�� ��(��������)���� ������ hashmap ����
	 * @param map
	 * @return
	 */
	private static HashMap sortByValue(HashMap map) {
	
		if(map.size()==1) return (HashMap) map;
		
	    List<TransactionOutput> list = new ArrayList();
	    list.addAll(map.keySet());

	    Collections.sort(list, new Comparator() {
	    	@Override
	        public int compare(Object o1,Object o2) {
	            float value1=((TransactionOutput)map.get(o1)).value;
	            float value2=((TransactionOutput)map.get(o2)).value;
	        
	            if(value1 > value2) return -1;
	            else if(value1 < value2) return 1;
	            else return 0;
	        }
	    });

        HashMap<String,TransactionOutput> sortedUTXOs=new LinkedHashMap();
        for(Iterator it=list.iterator();it.hasNext();) {
        	String key=(String) it.next();
        	//System.out.println("*****key="+key+" / value=" + ((TransactionOutput) map.get(key)).value);  // Ȯ�ο� ���
        	sortedUTXOs.put(key, (TransactionOutput) map.get(key));
        }
	    return sortedUTXOs;

	}

}
