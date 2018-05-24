package coin;

import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import transaction.TransactionOutput;
import wallet.Address;

public class TransferUTXO {
    private ArrayList<TransactionOutput> UTXO= new ArrayList<>();
    
    public void setUTXOList() {  //pc������
    	for(Map.Entry<String,TransactionOutput> item: Coin.blockchain.UTXOs.entrySet()) {
    		System.out.println("utxo " +item.getValue().id);
    		UTXO.add(item.getValue());
    	}
    }
    
    public ArrayList<TransactionOutput> getUTXO(){ //�ȵ忡����
    	return UTXO;
    }
    
    public JSONObject toJSONObject() { //PC������
    	
		JSONObject json = new JSONObject();
		
		JSONArray array = new JSONArray();
		if (UTXO == null) array = null;
		for(TransactionOutput tx: UTXO) {
			array.add(tx.toJSONObject());
		}
		
		json.put("utxo", array);
		return json;
	}
    
	public void convertClassObject(JSONObject json) { //�ȵ忡����
		
		JSONArray jsonArray = (JSONArray) json.get("utxo");
		JSONObject temp;
		
		for(int i = 0; i < jsonArray.size(); i++) {
			temp = (JSONObject) jsonArray.get(i);
			Address tmprec=new Address();
			tmprec.setAddress((String) temp.get("receiver"));
			UTXO.add(new TransactionOutput(tmprec, 
					((Number)temp.get("value")).floatValue(), (String)temp.get("parentTxId")));
		}
	}
    
}