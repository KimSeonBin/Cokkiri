package transaction;

import org.json.simple.JSONObject;

import hash.Sha256;
import wallet.Address;

public class TransactionOutput {
	public String id;
	public Address receiver; //also known as the new owner of these coins.
	public float value; //the amount of coins they own
	public String parentTxId; //the id of the transaction this output was created in
	
	//Constructor
	public TransactionOutput(Address receiver, float value, String parentTxId) {
		this.receiver = receiver;
		this.value = value;
		this.parentTxId = parentTxId;
		this.id = Sha256.hash(receiver.getString()+Float.toString(value)+parentTxId);
	}
	
	//Check if coin belongs to you
	public boolean isMine(Address address) {
		return (address.getString().equals(receiver.getString()));
	}
	
	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("receiver", receiver.getString());
		json.put("value", value);
		json.put("parentTxId", parentTxId);
		return json;
	}
}
