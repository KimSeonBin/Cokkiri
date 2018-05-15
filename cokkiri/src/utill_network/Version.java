package utill_network;

import java.io.Serializable;
import java.net.InetAddress;

/*
 * https://bitcoin.org/en/developer-reference#version
 */
public class Version implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//services 종류
	public static final int UNNAMED = 0x00;
	public static final int NODE_NETWORK = 0x01;

	/*
	 * version : 클라이언트 프로그램 버전 services : 0x00 : 노말 노드, 0x01: full node
	 * timestamp : 현재시간
	 */

	public int version; // int32_t
	public int services; // uint64_t
	public long timestamp; // int64_t
	public long addr_recv_services; // uint64_t
	public InetAddress addr_recv_ip_address;// char
	public int addr_recv_port; // uint16_t
	public long addr_trans_service; // uint64_t
	public InetAddress addr_trans_ip_address;// char
	public int addr_trans_port; // uint16_t
	public long nonce; // uint64_t
	public int user_agent_bytes; // varies
	public String user_agent;
	public int start_height;
	public boolean relay;
	
	public Version() {
		
	}
	
	public Version(int version, int services, long timestamp, long addr_recv_services,
			InetAddress addr_recv_ip_address, int addr_recv_port, long addr_trans_service,
			InetAddress addr_trans_ip_address, int addr_trans_port, long nonce, int user_agent_bytes, int start_height,
			boolean relay) {
		super();
		//char[] temp = {'v','e','r','s','i','o','n'};
		//super.setcommand(temp);
		
		this.version = version;
		this.services = services;
		this.timestamp = timestamp;
		this.addr_recv_services = addr_recv_services;
		this.addr_recv_ip_address = addr_recv_ip_address;
		this.addr_recv_port = addr_recv_port;
		this.addr_trans_service = addr_trans_service;
		this.addr_trans_ip_address = addr_trans_ip_address;
		this.addr_trans_port = addr_trans_port;
		this.nonce = nonce;
		this.user_agent_bytes = user_agent_bytes;
		this.start_height = start_height;
		this.relay = relay;
	}
	
	public boolean hasBlockChain() {
		return (services & NODE_NETWORK) == NODE_NETWORK; 
	}
	
}
