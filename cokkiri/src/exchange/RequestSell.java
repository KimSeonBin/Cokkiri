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
	
	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();
		json.put("coin", coin);
		json.put("cash", cash);
		return json;
	}
	
	public JSONObject txJSONObject() {
		String server = "tmp"; //서버 어드레스 스트링
		Address serveradd =new Address();
		serveradd.setAddress(server);
		Transaction tx = createTransaction.createTx(Coin.wallet, serveradd, coin);
		
		Mining.transactionPool.add(tx);
		
		return tx.toJSONObject();
	}
	
	
	
	
	
	
	
	
	public static float sellcoin(float value, ExchangeSellView sellview) {
		
		//첫번째 통신 (가능여부)
		boolean check=true;
		if(check) {
			String passwd = JOptionPane.showInputDialog("계좌 비밀번호를 입력하세요(16자 이상)");
		    if(Coin.wallet.authenticate(Coin.id+passwd)!=1) {
		    	String message = "코인 판매에 실패했습니다.";
				JOptionPane.showMessageDialog(sellview, message, "거래 실패", JOptionPane.WARNING_MESSAGE);
				log.Logging.consoleLog("failed to sell coin .. wrong password");
		    	return 0;
			}
		    else {

				Wallet seller= new Wallet(Coin.id+passwd, false);
				
				Address receiverAdd=new Address();
				receiverAdd.setAddress("거래소주소 박아야함");
				Transaction t=createTransaction.createTx(seller, receiverAdd, value);
				
				if(t!=null) {
					
					//서버와 통신
					
					log.Logging.consoleLog("**transaction created** : "+t.getString());
				
					return 1;
				}
		    }
		}
		return 0;
	}


	
}
	/*private String server_url = "http://192.168.10.7:8000/sale";
	private String sale_page = "?page=";
	private URL url;
	private HttpURLConnection conn;
	
	public void setupPostConnection() {
		try {
			this.url = new URL(server_url);
			this.conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("accept", "application/json");
			conn.connect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void registerSaletoExchange(String verification, JSONObject jsono) {
		setupPostConnection();

		jsono.put("verification", verification);
		try {
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(jsono.toJSONString());
			dos.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				System.out.println("error");
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String output;
			System.out.println("from server..\n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn.disconnect();
	}
	
	public JSONObject convertPOSTToExchange(float value) {
		JSONObject jsono = new JSONObject();
		
		jsono.put("seller_id", Coin.wallet.getAddress().getString());
		jsono.put("amount", value);
		jsono.put("key", Coin.wallet.getPublicKey());
		jsono.put("identifier", "ajou");
		
		return jsono;
	}
}
*/