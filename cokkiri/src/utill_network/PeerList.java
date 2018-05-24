package utill_network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import coin.Coin;
import coin.Constant;

public class PeerList {

	public static void init() {
		File file = new File(Constant.pathDir+"/peerlist.txt");
		if(file.exists()) {
			file.delete();	
		}
		
		ArrayList<Peer> peerArray = new ArrayList<Peer>();
		peerArray.add(new Peer("2", "192.168.0.17", 3333));
		//peerArray.add(new Peer("2", "172.17.99.15", 3333));
		peerArray.add(new Peer("3", "172.17.100.15", 3333));
		//peerArray.add(new Peer("1","192.168.25.121",3333)); //서버
		//peerArray.add(new Peer("1","192.168.51.72",3333)); //서버
		//peerArray.add(new Peer("1000","192.168.10.4",3333));//선빈
		//peerArray.add(new Peer("1001","192.168.10.5",3333));//슬비
		//peerArray.add(new Peer("1002","192.168.10.6",3333));//현영
		//peerArray.add(new Peer("1003","192.168.10.7",3333));//현영미니어처

		
		for(Peer peer : peerArray) {
			PeerList.storePeerList(peer);	
		}
	}
	
	public static ArrayList<Peer> getPeerList() {
		ArrayList<Peer> peerList = new ArrayList<Peer>();
		try {
			FileReader fr = new FileReader(Constant.pathDir+"/peerList.txt");
			BufferedReader br = new BufferedReader(fr);
			
			String str = null;
			
			while((str=br.readLine())!=null) {								
				JSONObject json = (JSONObject)new JSONParser().parse(str);
				peerList.add(new Peer((String)json.get("peer_id"),(String)json.get("ip_address"),((Number)json.get("port")).intValue()));
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return peerList;
		
	}
	
	public static void storePeerList(Peer peer) {
	
		JSONObject json = toJSONObject(peer);
		System.out.println("json : "+ json);
		File file = new File(Constant.pathDir + "/peerlist.txt");
		
		FileWriter fw;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter writer = new BufferedWriter(fw);
			writer.append(json.toString());writer.newLine();
		
			writer.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static JSONObject toJSONObject(Peer peer) {
		JSONObject json = new JSONObject();
		json.put("peer_id", peer.getId());
		json.put("ip_address", peer.getIpAddress());
		json.put("port", peer.getPort());
		
		return json;
	}
	
	
}
