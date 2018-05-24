package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import coin.Constant;


public class ClientPeerReceive {
	private String DnsServerIp = "http://192.168.16.204:8000/peerlist";
	private URL url;
	private HttpURLConnection conn;
	
	/*
	 * Dnsserver와 연결
	 * 위의 Ip는 고정 Ip는 아니라 서버를 킬 때 서버 ip를 확인하고 바꿔야 한다.
	 */
	private void connectDns() {
		
		try {
			url = new URL(DnsServerIp);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.connect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getPeerList() {
		connectDns();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuffer stringBuffer = new StringBuffer();
			String json = null;
			
			while((json = br.readLine()) != null) {
				stringBuffer.append(json);
			}
			
			br.close();
			JSONArray jsonObject = convertJson(stringBuffer.toString());
			storePeerList(jsonObject);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * 받는 JSON data
	 * [
	 * 	{peer_id="asdad", ip_address="132.518.321.70", port=3333},
	 * 	{peer_id="qweqw", ip_address="229.538.771.70", port=3333},
	 * 	...
	 * ]
	 * JSONArray 안 JSONObject가 있는 형식
	 */
	private JSONArray convertJson(String json) {
		JSONParser parser = new JSONParser();
		JSONArray jsonObject;
		try {
			jsonObject = (JSONArray) parser.parse(json);
			return jsonObject;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * txt 형식으로 peerlist를 저장
	 * 총 개수(처음에만)
	 * peer_id
	 * ip_address
	 * port
	 */
	private void storePeerList(JSONArray json) {
		File file = new File(Constant.pathDir + "peerlist.txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			JSONObject jsonObject;
			writer.write(String.valueOf(json.size()));writer.newLine();
			for(int i = 0; i < json.size(); i++) {
				jsonObject = (JSONObject) json.get(i);
				writer.write((String)jsonObject.get("peer_id"));writer.newLine();
				writer.write((String)jsonObject.get("ip_address"));writer.newLine();
				writer.write(String.valueOf(jsonObject.get("port")));writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean checkexistsList() {
		File file = new File(Constant.pathDir + "peerlist.txt");
		if (!file.exists()) {
			return false;
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			if(Integer.parseInt(reader.readLine() ) == 0) {
				return false;
			}
			
			reader.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
