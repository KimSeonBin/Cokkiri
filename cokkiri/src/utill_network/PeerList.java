package utill_network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import client.Connection;
import coin.Coin;
import coin.Constant;

public class PeerList {

	public static void init() {
		File file = new File(Constant.pathDir+"/peerlist.txt");
		if(file.exists()) {
			file.delete();	
		}
		
		ArrayList<Peer> peerArray = new ArrayList<Peer>();
		peerArray.add(new Peer("0A0C5893A5F48", "192.168.10.98", 3333));//관리자(선빈 pc)
//		peerArray.add(new Peer("1FCF8AEA1A9E6", "192.168.10.5", 3333));//슬비 pc
//	      peerArray.add(new Peer("19CB6D01A37A1", "192.168.10.6", 3333));//현영 pc
//	      //peerArray.add(new Peer("0DCA971606AE9", "192.168.10.7", 3333));//현영 미니어처 pc
//
//
//	      peerArray.add(new Peer("294:8B:C1:20:57:F0", "192.168.10.2", 3333));//슬비 android
//	      //peerArray.add(new Peer("208:00:27:CE:E7:19", "192.168.10.6", 3333));//현영 녹스 android
//	      peerArray.add(new Peer("25C:70:A3:7D:11:DE", "192.168.10.8", 3333));//선빈 android
	      
		for(Peer peer : peerArray) {
			PeerList.storePeerList(peer);	
		}
	}
	
	
	
	public static HashMap<String, Peer> getPeerList() {
		HashMap<String, Peer> peerList = new HashMap<String, Peer>();
		try {
			FileReader fr = new FileReader(Constant.pathDir+"/peerList.txt");
			BufferedReader br = new BufferedReader(fr);
			
			String str = null;
			
			while((str=br.readLine())!=null) {
	            JSONObject json = (JSONObject)new JSONParser().parse(str);
	            if(!((String)json.get("peer_id")).equals(NodeId.getNodeId())){
	               peerList.put((String)json.get("peer_id"), new Peer((String)json.get("peer_id"),(String)json.get("ip_address"),((Number)json.get("port")).intValue()));
	            }
	         }

			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return peerList;
		
	}
	
	public static ArrayList<Peer> getAdminPeerList(){
		HashMap<String, Peer> peerList = getPeerList();
		ArrayList<Peer> adminPeerList = new ArrayList<Peer>();
		
		for(String NodeID : peerList.keySet()) {
			if(String.valueOf(NodeID.charAt(0)).equals("0")) {
				adminPeerList.add(peerList.get(NodeID));
			}
			
		}
		
		return adminPeerList;
	}
	
	public static ArrayList<Peer> getPcPeerList(){
		HashMap<String, Peer> peerList = getPeerList();
		ArrayList<Peer> PcPeerList = new ArrayList<Peer>();
		
		for(String NodeID : peerList.keySet()) {
			System.out.println("NodeID : "+NodeID);
			if(String.valueOf(NodeID.charAt(0)).equals("1")) {
				PcPeerList.add(peerList.get(NodeID));
			}
			
		}
		
		return PcPeerList;
	}
	
	public static ArrayList<Peer> getAndroidPeerList(){
		HashMap<String, Peer> peerList = getPeerList();
		ArrayList<Peer> androidPeerList = new ArrayList<Peer>();
		
		for(String NodeID : peerList.keySet()) {
			if(String.valueOf(NodeID.charAt(0)).equals("2")) {
				androidPeerList.add(peerList.get(NodeID));
			}
			
		}
		
		return androidPeerList;
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
