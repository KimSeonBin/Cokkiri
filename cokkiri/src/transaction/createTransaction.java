package transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import coin.Coin;
import log.Logging;
import wallet.Address;
import wallet.Wallet;

public class createTransaction {

	/**
	 * Generates and returns a new transaction from this wallet.
	 */
	public static Transaction createTx(Wallet sender, Address _recipient,float value ) {
		
		Logging.consoleLog("createTx()");
		
		if(sender.getBalance() < value) { //gather balance and check funds.
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}

		//create array list of inputs
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
        
        //HashMap<String,TransactionOutput> sortedUTXOs=sortByValue(Coin.blockchain.UTXOs);
        HashMap<String, TransactionOutput> sortedUTXOs=sortByValue(sender.UTXOs);
		
		
		float total = 0;
		for (Map.Entry<String, TransactionOutput> item: sortedUTXOs.entrySet()){
			TransactionOutput UTXO = item.getValue();
			total += UTXO.value;
			System.out.println("UTXO : "+UTXO.toJSONObject());
			TransactionInput tmp=new TransactionInput(UTXO.id);
			System.out.println("tmp : "+tmp.toJSONObject());
			inputs.add(tmp);

			if(total >= value) break;
		}
		Transaction newTransaction = new Transaction(sender.getAddress(), _recipient , value, inputs);
		/*
		System.out.println("check inputs");
		Iterator<TransactionInput> it3=inputs.iterator();
		while(it3.hasNext()) System.out.println(it3.next().toJSONObject());
		System.out.println("first- tx check ");
		System.out.println(newTransaction.getString());
		System.out.println("first - txinput check");
		Iterator<TransactionInput> it1 = newTransaction.inputs.iterator();	
		while(it1.hasNext()) System.out.println(it1.next().toJSONObject());
		System.out.println("first - txoutput check");
		Iterator<TransactionOutput> it2 = newTransaction.outputs.iterator();	
		while(it2.hasNext()) System.out.println(it2.next().toJSONObject());
		*/
		
		newTransaction.generateSignature(sender.getPrivateKey());
		
		
		/*
		Iterator<String> iterator1 = UTXOs.keySet().iterator();
		// �ݺ��ڸ� �̿��ؼ� ���
		while (iterator1.hasNext()) { 
			String key = (String)iterator1.next(); // Ű ���
			System.out.println("*key="+key+" / value=" + UTXOs.get(key).value);  // ���
		}
		*/
		if(newTransaction.processTransaction()) { //�ϴ� ���⼭..
			/*for(TransactionInput input: inputs){
				//System.out.println("remove : "+ UTXOs.get(input.txOutputId).value); //Ȯ�ο�
				//removeUTXOs.put(input.txOutputId, UTXOs.get(input.txOutputId));
				Coin.blockchain.UTXOs.remove(input.txOutputId);
			}*/
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
