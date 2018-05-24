package coin;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import transaction.Transaction;
import wallet.Address;

//user �� ���õ� tx�� ���� (sender�̰ų�, receiver�̰ų�)
public class TransferTxs {
	private ArrayList<Transaction> userTxs;
	
	///pc���� ��///
	public void setTxList(Address user) { //pc������
		System.out.println("TransferTxs.java setTxList()");
		userTxs=new ArrayList<>();
		ArrayList<Transaction> tmp = new ArrayList<>();
		Coin.blockchain.getAllTx();
		tmp.addAll(Coin.blockchain.allTx);
		for(int i=0;i<tmp.size();i++) {
			Transaction check = tmp.get(i);
			System.out.println("all "+check.getString());
			if(check.receiver.getString().equals(user.getString())||check.sender.getString().equals(user.getString()) ) {
				System.out.println("added "+check.getString());
				userTxs.add(check); 
			}	
		}
		
	}
	
	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();
		
		JSONArray array = new JSONArray();
		if (userTxs == null) array = null;
		for(Transaction tx: userTxs) {
			array.add(tx.toJSONObject());
		}
		
		json.put("allTxs", array);
		return json;
	}
	/////////////
	
	////////�ȵ� ������ ////////
	public void convertClassObject(JSONObject json) {
		
		userTxs.clear();
		JSONArray jsonArray = (JSONArray) json.get("allTxs");
		JSONObject temp;
		
		for(int i = 0; i < jsonArray.size(); i++) {
			Transaction tx = new Transaction();
			tx.convertClassObject((JSONObject) jsonArray.get(i));
			userTxs.add(tx);
		}
	}
	//////////////////////
}
