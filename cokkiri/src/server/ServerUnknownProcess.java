package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

import utill_network.Version;
import utill_network.MessageHeader;
import utill_network.Peer;

public class ServerUnknownProcess {
	
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private Peer conPeer;
	
	public ServerUnknownProcess(ObjectOutputStream outputStream, ObjectInputStream inputStream, Peer conPeer) {
		this.outputStream = outputStream;
		this.inputStream = inputStream;
		this.conPeer = conPeer;
	}
	
	public boolean init() {
		if(!initProcess()) {
			System.out.println("not valid init process");
			return false;
		}
		else {
			return true;
		}
	}
	

	/* version message -> verack
	 * false : 실패, true : 성공
	 */
	private boolean initProcess() {
		try {
			MessageHeader header = (MessageHeader) inputStream.readObject();
			if(!String.valueOf(header.command).startsWith("version")) {
				return false;
			}
			
			Version version = (Version)inputStream.readObject();
			if(!validateVersion(version)) {
				return false;
			}
			
			System.out.println("send to client version message");
			//header 재사용
			//header payload, checksum 다시 계산 추후 적용. 지금은 에러 발생...
			outputStream.writeObject(header);
			outputStream.flush();
			
			//version 추후 다시 계산 코드 적용
			InetAddress temp = version.addr_recv_ip_address;
			version.addr_recv_ip_address = version.addr_trans_ip_address;
			version.addr_trans_ip_address = temp;
			outputStream.writeObject(version);
			outputStream.flush();
			
			MessageHeader verack = new MessageHeader();
			verack.setcommand(new char[] {'v', 'e', 'r', 'a', 'c', 'k'});
			verack.calculatePayload(0);
			outputStream.writeObject(verack);
			outputStream.flush();
			
			verack = (MessageHeader) inputStream.readObject();
			
			if(!String.valueOf(verack.command).startsWith("verack")) {
				System.out.println("nooooooooo: "+String.valueOf(verack.command));
				return false;
			}
			else {
				System.out.println("verack ok!: "+String.valueOf(verack.command)+"\ninitial process ok.");
			}
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * version 메세지 검증
	 * peer의 보내는 ip, port 검증
	 * id도 검증하고 싶은데 Version 메세지에 id를 넣는 것은 없다..
	 */
	private boolean validateVersion(Version version) {
		if(conPeer.getPort() != version.addr_trans_port) {
			return false;
		}
		if(!conPeer.getIpAddress().equals(version.addr_trans_ip_address.getHostAddress())) {
			return false;
		}
		return true;
	}
}
