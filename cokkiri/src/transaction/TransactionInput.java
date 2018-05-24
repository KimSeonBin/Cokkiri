package transaction;

import org.json.simple.JSONObject;

import coin.Coin;

public class TransactionInput {
	public String txOutputId; //Reference to TransactionOutputs -> transactionId
	public TransactionOutput UTXO; //Contains the Unspent transaction output
	
	public TransactionInput(String txOutputId) {
		this.txOutputId = txOutputId;
		this.UTXO=Coin.blockchain.UTXOs.get(txOutputId);//맞는지는 모름 일단..
	}
	
	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();
		json.put("txOutputId", txOutputId);
		json.put("UTXO", UTXO.toJSONObject());
		return json;
	}
}
