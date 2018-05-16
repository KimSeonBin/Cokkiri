package transaction;

import java.security.*;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import appactivity.*;
import hash.Sha256;
import transaction.*;
import wallet.Address;
import coin.Coin;

public class Transaction {
	public String TxId; // this is also the hash of the transaction.
	public Address sender; // senders address/public key.
	public Address receiver; // Recipients address/public key.
	public float value;
	public byte[] signature; // this is to prevent anybody else from spending funds in our wallet.
	public long timestamp;
	
	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

	public Transaction(Address sender, Address receiver, float value, ArrayList<TransactionInput> inputs) {
		this.sender = sender;
		this.receiver = receiver;
		this.value = value;
		if(inputs!=null) this.inputs.addAll(inputs);
		else this.inputs=null;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		this.timestamp=timestamp.getTime();	
		
		if(sender.getString().equals(Coin.pathDir)) { //benefit tx
			TxId=calulateTxId();
		}
	}
	
	public Transaction() {
		// TODO Auto-generated constructor stub
	}

	//Returns true if new transaction could be created.	
	public boolean processTransaction() {
			
		if(verifySignature() == false) {
			System.out.println("#Transaction Signature failed to verify");
			return false;
		}

		//gather transaction inputs (Make sure they are unspent):
		for(TransactionInput i : inputs) {
			i.UTXO = Coin.blockchain.UTXOs.get(i.txOutputId);
		}
		//check if transaction is valid:
		//if(getInputsValue() < Coin.minimumTransaction) {
		if(getInputsValue() < 0) {
			System.out.println("#Transaction Inputs to small: " + getInputsValue());
			return false;
		}
		if(getInputsValue() > Coin.coinMax) {
			System.out.println("#Transaction Inputs to big: " + getInputsValue());
			return false;
		}

		//generate transaction outputs:
		float leftOver = getInputsValue() - value; //get value of inputs then the left over change:

		if(leftOver<0) {
			System.out.println("#Not enough inputs: "+getInputsValue()+" < "+getOutputsValue());
			return false;
		}

		TxId = calulateTxId();
		outputs.add(new TransactionOutput(this.receiver, value,TxId)); //send value to recipient
		if(leftOver>0)outputs.add(new TransactionOutput(sender, leftOver,TxId)); //send the left over 'change' back to sender		
		
		//add outputs to Unspent list
		for(TransactionOutput o : outputs) {
			Coin.blockchain.UTXOs.put(o.id , o);
		}

		//remove transaction inputs from UTXO lists as spent:
		for(TransactionInput i : inputs) {
			if(i.UTXO == null) continue; //if Transaction can't be found skip it 
			Coin.blockchain.UTXOs.remove(i.UTXO.id);
		}

		return true;
	}
	
	// transaction 유효성 검증 위함.. 추가할 요소 잇으면 추가 필요
	public boolean isTransactionValid() {
		System.out.println("--------isTransactionValid()----------");
		if(sender.getString().equals(Coin.pathDir)) {
			
			if(!inputs.isEmpty()){
				System.out.println("i'm invalid 1");
				return false;
			}
			if(value !=10) {
				System.out.println("i'm invalid 01");
				return false;
			}
			/*if(inputs==null && value==10) {
				System.out.println("i'm benefit");
				return true;
			}
			else {
				System.out.println("i'm invalid 1");
				return false;
			}*/
			return true;
		}
		/*else if(verifySignature()==true && getInputsValue() < Coin.minimumTransaction) {
			System.out.println("i'm valid");
			return true;
		}*/
		
		if(verifySignature() == false) {
			System.out.println("i'm invalid 2");
			return false;
		}
		if(getInputsValue() < Coin.minimumTransaction) {
			System.out.println("i'm invalid 3");
			return false;
		}
		System.out.println("i'm valid");
		return true;
	}
		
	//returns sum of inputs(UTXOs) values
	public float getInputsValue() {
		float total = 0;
		for(TransactionInput i : inputs) {
			if(i.UTXO == null) continue; //if Transaction can't be found skip it 
			total += i.UTXO.value;
		}
		return total;
	}

	//returns sum of outputs:
	public float getOutputsValue() {
		float total = 0;
		for(TransactionOutput o : outputs) {
			total += o.value;
		}
		return total;
	}
				
	// This Calculates the transaction hash (which will be used as its Id)
	private String calulateTxId() {
		return Sha256.hash(
				sender.getString() + receiver.getString() +
			Float.toString(value) + Long.toString(timestamp)
		);
	}
		
	//Signs all the data we dont wish to be tampered with.
	public void generateSignature(PrivateKey privateKey) {
		String data = sender.getString()+ receiver.getString() + Float.toString(value) + Long.toString(timestamp)	;
		signature = ECDSAUtil.applyECDSASig(privateKey,data);		
	}
	
	//Verifies the data we signed hasnt been tampered with
	public boolean verifySignature() {
		String data = sender.getString() + receiver.getString() + Float.toString(value) + Long.toString(timestamp);
		if(Coin.wallet.getAddress().equals(sender)) {
			return ECDSAUtil.verifyECDSASig(Coin.wallet.getPublicKey(), data, signature);
		}
		else return false;
	}
		
	public Address getSender() {return sender;}
	public Address getReceiver() {return receiver;}
	public float getValue() {return value;}
	
	public String getString(){ //transaction 내용을 string으로 리턴하기위함
		String transStr="\r\n tx id : "+TxId+"\r\n sender : "+sender.getString()+"\r\n receiver : "+receiver.getString()+"\r\n value : "+String.valueOf(value)+"\r\n";
		return transStr;
	}
	
	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();
		json.put("TxId", TxId);
		json.put("sender", sender.getString());
		json.put("receiver", receiver.getString());
		json.put("value", value);
		json.put("signature", Base64.encodeBase64String(signature));
		json.put("inputs", convertInput_toJSONArray());
		json.put("outputs", convertOutput_toJSONArray());
		return json;
	}
	
	public void convertClassObject(JSONObject json) {
		this.TxId = (String) json.get("TxId");
		this.sender = new Address();
		this.sender.setAddress((String) json.get("sender"));
		this.receiver = new Address();
		this.receiver.setAddress((String) json.get("receiver"));
		this.value = ((Number) json.get("value")).floatValue();
		this.signature = Base64.decodeBase64((String) json.get("signature"));
		
		JSONArray jsonArray = (JSONArray) json.get("inputs");
		JSONObject temp;
		if(jsonArray != null) {
			for(int i = 0; i < jsonArray.size(); i++) {
				temp = (JSONObject) jsonArray.get(i);
				TransactionInput txinput = new TransactionInput((String) temp.get("txOutputId"));
				temp = (JSONObject) temp.get("UTXO");
				Address tmp = new Address();
				tmp.setAddress((String) temp.get("receiver"));
				txinput.UTXO = new TransactionOutput(tmp, 
						 ((Number)temp.get("value")).floatValue(), (String)temp.get("parentTxId"));
				inputs.add(txinput);  //현영 추가 (맞는지 불확실)
			}
		}
		
		jsonArray = (JSONArray) json.get("outputs");
		for(int i = 0; i < jsonArray.size(); i++) {
			temp = (JSONObject) jsonArray.get(i);
			Address tmprec=new Address();
			tmprec.setAddress((String) temp.get("receiver"));
			outputs.add(new TransactionOutput(tmprec, 
					((Number)temp.get("value")).floatValue(), (String)temp.get("parentTxId")));
		}
	}
	
	private JSONArray convertInput_toJSONArray() {
		JSONArray array = new JSONArray();
		if(inputs == null) {
			return null;
		}
		for(TransactionInput tx: inputs) {
			if(tx==null) {
				continue;
			}
			array.add(tx.toJSONObject());
		}
		
		return array;
	}
	
	private JSONArray convertOutput_toJSONArray() {
		JSONArray array = new JSONArray();
		if (outputs == null) {
			return null;
		}
		for(TransactionOutput tx: outputs) {
			array.add(tx.toJSONObject());
		}
		return array;
	}
}
