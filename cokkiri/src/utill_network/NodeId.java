package utill_network;

public class NodeId {

	private static String id= "1001";
	
	
	
	public static String getNodeId() {

		return id;
	}
	
	
}

/* util_blockchain.NodeId.java
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NodeId {

	private static String id = null;
	
	public static void setNodeId() {
		
		try {
			//로컬 ip 취득
			InetAddress ip = InetAddress.getLocalHost();
			
			//네트워크 인터페이스 취득
			NetworkInterface netif = NetworkInterface.getByInetAddress(ip);
			
			//네트워크 인터페이스가 NULL이 아니면
			if(netif != null) {
				
				//맥어드레스 취득
				byte[] mac = netif.getHardwareAddress();
				
				
				//byte to string
				StringBuilder sb = new StringBuilder();
				
				for(byte b : mac) {
					sb.append(String.format("%02X", b));
				}
	
				id = sb.toString();
				
			}
		} catch (UnknownHostException | SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static String getNodeId() {
		
		return id;
	}
	
	
}*/