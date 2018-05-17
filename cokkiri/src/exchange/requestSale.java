package exchange;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PublicKey;

import org.json.simple.JSONObject;

import coin.Coin;

public class requestSale {
	private String server_url = "http://192.168.10.7:8000/sale";
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
