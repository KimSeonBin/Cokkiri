package exchange;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import coin.Coin;
import coin.Constant;
import transaction.Transaction;
import utill_network.Peer;
import wallet.Address;

public class RequestBuy {
	
	private double cash;
	private float coin;
	
	public RequestBuy(Float value) {
		this.coin=value;
		this.setCash(value*Constant.compasionValue);
	}
	
	public RequestBuy(String json) {
		JSONParser paser = new JSONParser();
		JSONObject jsonO;
		try {
			jsonO = (JSONObject) paser.parse(json);
			this.setCash(((Number)jsonO.get("cash")).doubleValue());
			this.coin = ((Number)jsonO.get("coin")).floatValue();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	   
	public float getCoin() {
		return coin;
	}
	
	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();
		json.put("coin", coin);
		json.put("cash", getCash());
		return json;
	}
	
	public JSONObject buyJSONObject() {
		JSONObject json = new JSONObject();
		json.put("cash", getCash());
		json.put("address", Coin.wallet.getAddress().getString());
		return json;
	}
	

	public void checkTxMsg(String txmsg) {
		JSONParser parser = new JSONParser();
		try {
			JSONObject json = (JSONObject)parser.parse(txmsg);
			Transaction tx = new Transaction();
			tx.convertClassObject(json);
			
			
			//tx »Æ¿Œ
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}
}
