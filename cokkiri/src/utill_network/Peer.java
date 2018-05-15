package utill_network;

public class Peer {
	
	private String id;
	private String ipAddress;
	private int port;
	
	public Peer(String id,String ipAddress ,int port ) {
		this.id = id;
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public String getId() {return id;}
	public String getIpAddress() {return ipAddress;}
	public int getPort() {return port;}
}
