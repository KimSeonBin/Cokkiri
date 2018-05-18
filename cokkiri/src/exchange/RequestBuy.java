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
		this.cash=value*Constant.compasionValue;
	}
	
	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();
		json.put("coin", coin);
		json.put("cash", cash);
		return json;
	}
	
	public JSONObject buyJSONObject() {
		JSONObject json = new JSONObject();
		json.put("cash", cash);
		json.put("address", Coin.wallet.getAddress());
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
}
