package utill_network;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*https://bitcoin.org/en/developer-reference#message-headers
 * 총 24byte로 고정
 * payload는 최대 0x02000000(33,554,432)
 */
public class MessageHeader implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public final char[] magic = {0xf9, 0xbe, 0xb4, 0xd9};
	public char[] command;
	public int payload;
	public char[] checksum = {0x5d, 0xf6, 0xe0, 0xe2};
	
	public MessageHeader() {
		this.command = new char[12];
		payload = 0;
	}
	
	/* class : command 관계
	 * Version : version
	 * VersionAck : verack
	 * GetBlockMessage : getblocks
	 */
	public void setcommand(char command[]) {
		// command는 char[12]
		for (int i = 0; i < command.length; i++) {
			this.command[i] = command[i];
		}
		for (int i = command.length; i < 12; i++) {
			this.command[i] = '\0';
		}
	}

	public void calculatePayload(int payload) {
		if(payload == 0) {
			return;
		}
		this.payload = payload;
		calculateChecksum();
	}
	
	/*
	 * checksum = sha256(sha256(payload))
	 * 단 verack, getaddr은 0x5df6e0e2로 고정
	 */
	public void calculateChecksum() {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(ByteBuffer.allocate(Integer.BYTES).putInt(payload).array());
			byte[] hash2= digest.digest(hash);
			for(int i = 0; i < 4; i++) {
				checksum[i] = (char) hash2[i];
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
