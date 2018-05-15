package wallet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
import java.util.Base64;

import hash.Sha256;

public class KeyUtil {
	private static SecureRandom secureRandom=new SecureRandom();
	final static String algorithm="ECDSA";
	final static String provider="BC";
	
	/**
	 * key를 input으로 받아 string 타입으로 리턴
	 * @param key
	 * @return
	 */
	public static String getStringFromKey(Key key) {
		if(key==null) return "null"; //일단..
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	public static PublicKey getPubKeyFromString(String key) {
		if(key.equals("null")) return null;
		try {
			byte[] publicBytes = Base64.getDecoder().decode(key);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm, provider);
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			return pubKey;
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 암호화하여 저장한 privatekey 파일, publickey 파일, keypairhash파일의 유무 확인
	 * @return
	 */
	public static boolean checkKeyfile(String pathDir){
		File file=new File(pathDir);
		if(!file.exists()) {
			file.mkdirs();
			return false;
		}
		File priv=new File(pathDir+"/enc_private.key");
		File pub = new File(pathDir+"/public.key");
		File hash = new File(pathDir+"/keypairhash");
		if(!(priv.exists()) || !(pub.exists()) || !(hash.exists())){
			return false;
		}
		if(priv.length()==0 || pub.length()==0 || hash.length()==0){
			return false;
		}
		return true;
	}
	
	/**
	 * keypair생성
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
	 * keypair의 hash값을 keypairhash 파일로 저장
	 * @param path
	 * @param hash
	 * @return
	 */
	static boolean SaveKeyPairHash(String path, String hash){
		 try {
			 BufferedWriter out = new BufferedWriter(new FileWriter(path+"/keypairhash"));
			 out.write(hash); 
			 out.close();
			 return true;
		 } catch (IOException e) {
			 System.err.println(e);
		 }
		 return false; 
	}
	
	/**
	 * keypairhash 파일로부터 keypairhash 값 얻어 리턴
	 * @param pathdir
	 * @return
	 */
	static String LoadKeyPairHash(String pathdir) {
		String hash="";
		try {
			BufferedReader in = new BufferedReader(new FileReader(pathdir+"/keypairhash"));
			String s;
			while ((s = in.readLine()) != null) hash+=s;
			in.close();
		} catch (IOException e) {
			System.err.println(e);
		}
		return hash;
	}
	
	/**
	 * keyPair의 hash값을 계산하여 리턴
	 * @param keyPair
	 * @return
	 */
	static String hashKeyPair(KeyPair keyPair){
		String input="";
		int tmp=keyPair.getPrivate().hashCode();
		input+=Integer.toString(tmp);
		tmp=keyPair.getPublic().hashCode();
		input+=Integer.toString(tmp);
		return Sha256.hash(input);
	}
	
	/**
	 * keypair를 매개변수로 받아 지정된 path에 저장
	 * @param path
	 * @param keyPair
	 * @param passwd
	 * @return
	 */
	static boolean SaveKeyPair(String path, KeyPair keyPair, String passwd){
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
 		
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
		
		FileOutputStream fos;
		try {
			// Store Public Key.
			fos = new FileOutputStream(path + "/public.key");
			fos.write(x509EncodedKeySpec.getEncoded());
			fos.close();
			// Store Private Key.
			fos = new FileOutputStream(path + "/private.key");
			fos.write(pkcs8EncodedKeySpec.getEncoded());
			fos.close();
			
			File plain=new File(path+"/private.key");
			File cipher=new File(path+"/enc_private.key");
			AES256Util aes=new AES256Util(passwd);
			aes.encrypt(plain, cipher);
			
			if(!plain.delete()){
				System.out.println("failed file delete");
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * path를 매개변수로 받아 key 파일로부터 keypair 얻어내어 리턴
	 * @param path
	 * @param passwd => 암호화하여 저장한 private key 파일 복호화하기위함
	 * @return
	 */
	static KeyPair LoadKeyPair(String path, String passwd){
		File filePublicKey = new File(path + "/public.key");
		FileInputStream fis;
		byte[] encodedPublicKey, encodedPrivateKey;

		try {
			// Read Public Key.
			fis = new FileInputStream(path + "/public.key");
			encodedPublicKey = new byte[(int) filePublicKey.length()];
			fis.read(encodedPublicKey);
			fis.close();
			
			File plain=new File(path+"/private.key");
			File cipher=new File(path+"/enc_private.key");
			AES256Util aes=new AES256Util(passwd);
			aes.decrypt(cipher, plain);
			
			// Read Private Key.
			fis = new FileInputStream(plain);
			encodedPrivateKey = new byte[(int) plain.length()];
			fis.read(encodedPrivateKey);
			fis.close();
			
			if(!plain.delete()){
				System.out.println("failed file delete");
			}
			
			KeyFactory keyFactory;
			keyFactory = KeyFactory.getInstance(algorithm, provider);
			
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
		
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
			PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
			
			return new KeyPair(publicKey, privateKey);
		} catch (IOException | NoSuchAlgorithmException 
				| NoSuchProviderException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return new KeyPair(null, null);
	}
	
	static PublicKey LoadPubKey(String path) {
		File filePublicKey = new File(path + "/public.key");
		FileInputStream fis;
		byte[] encodedPublicKey, encodedPrivateKey;

		try {
			// Read Public Key.
			fis = new FileInputStream(path + "/public.key");
			encodedPublicKey = new byte[(int) filePublicKey.length()];
			fis.read(encodedPublicKey);
			fis.close();
			
			KeyFactory keyFactory;
			keyFactory = KeyFactory.getInstance(algorithm, provider);
			
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
			
			return publicKey;
		} catch (IOException | NoSuchAlgorithmException 
				| NoSuchProviderException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
}
