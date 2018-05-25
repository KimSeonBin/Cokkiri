package wallet;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import hash.Sha256;

//base58check? 추가해야함
public class Address {
	
	private String address;
	
	/**
	 * 공개키로부터 sha256, ripemd160, base58 인코딩을 거쳐 address 생성
	 * @param publicKey
	 */
	protected Address(PublicKey publicKey){
		this.address=getAddress(publicKey);
	}
	
	public Address() {
	}

	public String getString() {return address;}
	
	private static String getAddress(PublicKey publicKey) {
		//System.out.println("check getAddress : "+new String(publicKey.getEncoded()));
		String address=Sha256.hash(new String(publicKey.getEncoded()));
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("RIPEMD160");
			byte[] hashedString = messageDigest.digest(address.getBytes());
			address=Base58.encode(hashedString);
			return address;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setAddress(String address) {
		this.address=address;
	}
}
