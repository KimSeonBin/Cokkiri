package wallet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
//import java.util.Base64;

public class KeyUtil {
	private static SecureRandom secureRandom=new SecureRandom();
	final static String algorithm="ECDSA";
	final static String provider="BC";
	
	/**
	 * key�� input���� �޾� string Ÿ������ ����
	 * @param key
	 * @return
	 */
	public static String getStringFromKey(Key key) {
		if(key==null) return "null"; //�ϴ�..
		
		//return Base64.getEncoder().encodeToString(key.getEncoded());
		return org.bouncycastle.util.encoders.Base64.toBase64String(key.getEncoded());
	}
	
	public static PublicKey getPubKeyFromString(String key) {
		System.out.println("getPubKeyFromString() : key ="+key);
		byte[] tmp = key.getBytes();
		key=new String(tmp);
		System.out.println("check : " +Arrays.toString(tmp));
		if(key.equals("null")) return null;
		try {
			//byte[] publicBytes = Base64.deco
			byte[] publicBytes =org.bouncycastle.util.encoders.Base64.decode(key);
			//byte[] publicBytes = Base64.getDecoder().decode(key);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm, provider);
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			return pubKey;
		} catch (NoSuchAlgorithmException | NoSuchProviderException| InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ��ȣȭ�Ͽ� ������ privatekey ����, publickey ����, keypairhash������ ���� Ȯ��
	 * @return
	 */
	public static boolean checkKeyfile(String pathDir){
		File file=new File(pathDir);
		if(!file.exists()) {
			file.mkdirs();
			return false;
		}
		File priv=new File(pathDir+"/enc_private.key");
		File pub = new File(pathDir+"/enc_public.key");
		File orgpub = new File(pathDir+"/public.key");
		if(!(priv.exists()) || !(pub.exists()) || !(orgpub.exists())){
			return false;
		}
		if(priv.length()==0 || pub.length()==0 || orgpub.length()==0){
			return false;
		}
		return true;
	}
	
	/**
	 * keypair����
	 * @param passwd
	 * @return
	 */
	static KeyPair generateKeyPair(String passwd){
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		try {
			secureRandom=SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		secureRandom.setSeed(passwd.getBytes());
		
		try {
			KeyPairGenerator keyGen=KeyPairGenerator.getInstance(algorithm, provider);
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
			keyGen.initialize(ecSpec, secureRandom);
			return keyGen.generateKeyPair();
		} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * keypair�� �Ű������� �޾� aes ��ȣȭ�� ����Ű, ����Ű�� ���� ����Ű�� ������ path�� ����
	 * @param path
	 * @param keyPair
	 * @param passwd
	 * @return
	 */
	static void saveKeyPair(String path, KeyPair keyPair, String passwd){
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
 		
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
		
		AES256Util aes=new AES256Util(passwd);

		byte[] encPub=aes.encrypt(x509EncodedKeySpec.getEncoded());
		byte[] encPriv=aes.encrypt(pkcs8EncodedKeySpec.getEncoded());
		
		// Store Public Key.
		if(!fileWrite(path + "/enc_public.key", encPub)) {
			System.out.println("����Ű ��ȣȭ ���� ���� ����");
		}
		if(!fileWrite(path + "/enc_private.key", encPriv)){
			System.out.println("����Ű ��ȣȭ ���� ���� ����");
		}
			
	}

	/**
	 * ����Ű�� �Ű������� �޾� ���� ����
	 */
	static void savePubKey(String path, PublicKey key){
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key.getEncoded());
		if(!fileWrite(path+"/public.key", x509EncodedKeySpec.getEncoded())){
			System.out.println("����Ű ���� ���� ����");
		}
	}

	/**
	 * path�� �Ű������� �޾� key��ȣȭ ���Ϸκ��� keypair ���� ����
	 * @param path
	 * @param passwd => ��ȣȭ�Ͽ� ������ private key ���� ��ȣȭ�ϱ�����
	 * @return
	 */
	static KeyPair loadKeyPair(String path, String passwd){

		byte[] encodedPublicKey, encodedPrivateKey;
		AES256Util aes=new AES256Util(passwd);

		try {
			encodedPublicKey = fileRead(path+"/enc_public.key");
			encodedPublicKey=aes.decrypt(encodedPublicKey);
			
			encodedPrivateKey = fileRead(path+"/enc_private.key");
			encodedPrivateKey=aes.decrypt(encodedPrivateKey);
			
			KeyFactory keyFactory;
			keyFactory = KeyFactory.getInstance(algorithm, provider);
			
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
		
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
			PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
			
			return new KeyPair(publicKey, privateKey);
		} catch ( NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return new KeyPair(null, null);
	}
	
	static PublicKey loadPubKey(String path) {
		byte[] encodedPublicKey;

		try {
			// Read Public Key.
			encodedPublicKey = fileRead(path+"/public.key");
			KeyFactory keyFactory= KeyFactory.getInstance(algorithm, provider);
			
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
			
			return publicKey;
		} catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * path, byte[] ������ �� �Ű������� �޾� ���Ϸ� ����
	 */
	private static boolean fileWrite(String path, byte[]data) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(path);
			fos.write(data);
			fos.close();
			return true;
		} catch (IOException e) {e.printStackTrace();}
		return false;
	}
	
	/**
	 * path�� �Ű������� �޾� ������ ������ ����
	 */
	private static byte[] fileRead(String path) {
		File file = new File(path);
		if(!file.exists()) { return null;}
		FileInputStream fis;
		byte[] data = new byte[(int) file.length()];
		try {
			fis = new FileInputStream(path);
			fis.read(data);
			fis.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static boolean authenticate(String path, String passwd) {
		AES256Util aes=new AES256Util(passwd);
		byte[] encodedPublicKey= fileRead(path+"/public.key");
		byte[] encPublicKey = fileRead(path+"/enc_public.key");
		if(Arrays.equals(aes.decrypt(encPublicKey), encodedPublicKey)) {
			return true;
		};

		return false;
	}
}
