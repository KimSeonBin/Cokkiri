package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import utill_network.Version;
import utill_network.MessageHeader;

public class ClientUnknownProcess {
	private final Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
	public ClientUnknownProcess(Socket socket, ObjectOutputStream outputStream, ObjectInputStream inputStream) {
		this.socket = socket;
		this.outputStream = outputStream;
		this.inputStream = inputStream;
	}

	public boolean initProcess() {
		boolean result = true;
		Version version = new Version(1, 0x01, System.currentTimeMillis(), 0x01, socket.getInetAddress(), socket.getPort(), 0x01,
				socket.getLocalAddress(), socket.getPort(), 0, 0, 0, false);
		
		result = sendVersionMessage(version);
		result = receiveVersionMessage();

		result = receiveVerack();
		result = sendVerack(version);
		
		return result;
	}

	private boolean sendHeader(char command[], int payload) {
		MessageHeader header = new MessageHeader();
		header.setcommand(command);
		header.calculatePayload(payload);
		try {
			outputStream.writeObject(header);
			outputStream.flush();
			System.out.println("send header: " + String.valueOf(command));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	// 본래는 payload를 계산해서 header에 넣어야 하지만 현재 payload 구하는 함수가 오류 투성이라 잠시 0으로 두고 진행
	private boolean sendVersionMessage(Version version) {
		sendHeader(new char[] { 'v', 'e', 'r', 's', 'i', 'o', 'n' }, 0);
		try {
			outputStream.writeObject(version);
			outputStream.flush();
			System.out.println("send version");
			
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private boolean sendVerack(Version version) {
		boolean result = sendHeader(new char[] { 'v', 'e', 'r', 'a', 'c', 'k' }, 0);
		return result;
	}

	private boolean receiveVersionMessage() {
		try {
			MessageHeader header = (MessageHeader) inputStream.readObject();
			
			if(!String.valueOf(header.command).startsWith("version")) {
				return false;
			}
			
			Version receiveVersion = (Version) inputStream.readObject();
			return validateVersion(receiveVersion);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean receiveVerack() {
		try {
			MessageHeader header = (MessageHeader) inputStream.readObject();
			if(!String.valueOf(header.command).startsWith("verack")) {
				return false;
			}
			
			return true;
		} catch (IOException e) {
			System.out.println("dont receive verack");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("not verack. what is it?");
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean validateVersion(Version version) {
		if(socket.getPort() != version.addr_trans_port) {
			return false;
		}
		if(!socket.getInetAddress().getHostAddress().equals(version.addr_trans_ip_address.getHostAddress())) {
			return false;
		}
		return true;
	}
}
