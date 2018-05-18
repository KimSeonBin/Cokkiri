package exchange;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PublicKey;

import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import appview.ExchangeSellView;
import client.Client;
import coin.Coin;
import coin.Constant;
import mining.Mining;
import transaction.Transaction;
import transaction.createTransaction;
import utill_network.MsgType;
import utill_network.Peer;
import wallet.Address;
import wallet.Wallet;

public class RequestSell {
	
	private double cash;
	private float coin;
	
	public RequestSell(Float value) {
		this.coin=value;
		this.cash=value*Constant.compasionValue;
	}
	
	public RequestSell(String json) {
	      JSONParser paser = new JSONParser();
	      JSONObject jsonO;
	      try {
	         jsonO = (JSONObject) paser.parse(json);
	         this.cash = ((Number)jsonO.get("cash")).doubleValue();
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
		json.put("cash", cash);
		return json;
	}
	
	public JSONObject txJSONObject(Wallet wallet, Address serverAdd) {
		String server = "tmp"; //서버 어드레스 스트링
	
		Transaction tx = createTransaction.createTx(wallet, serverAdd, coin);
		
		Mining.transactionPool.add(tx);
		
		System.out.println("[ClientSendlog] : BroadCast Transaction");
		
		new Thread() {
			public void run() {
				try {
					Client.broadcast(MsgType.TRANSACTION_MSG+tx);
				} catch (Exception e) {}
			}
		}.start();
		
		return tx.toJSONObject();
	}
}
