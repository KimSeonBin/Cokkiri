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
			//���� ip ���
			InetAddress ip = InetAddress.getLocalHost();
			
			//��Ʈ��ũ �������̽� ���
			NetworkInterface netif = NetworkInterface.getByInetAddress(ip);
			
			//��Ʈ��ũ �������̽��� NULL�� �ƴϸ�
			if(netif != null) {
				
				//�ƾ�巹�� ���
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