package wallet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;  
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;  
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 *AES256 암호화를 지원하는 클래스
 */
public class AES256Util {  
    private String iv;
    private Key keySpec;

    /**
     * 16자리의 키값을 입력하여 객체를 생성한다.
     * @param key 암/복호화를 위한 키값
     * @throws UnsupportedEncodingException 키값의 길이가 16이하일 경우 발생
     */
    public AES256Util(String key){
    	//key=key.substring(0, 16);
        this.iv = key.substring(0, 16);
        byte[] keyBytes = new byte[16];
        byte[] b;
		try {
			b = key.getBytes("UTF-8");
			int len = b.length;
			if(len > keyBytes.length){
				len = keyBytes.length;
			}
			System.arraycopy(b, 0, keyBytes, 0, len);
			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

			this.keySpec = keySpec;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }

    
    public byte[] encrypt(byte[] plain) {
        Cipher c;
		try {
			c = Cipher.getInstance("AES/CBC/PKCS5Padding");

			c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
		
			byte[] encrypted;
		
			encrypted = c.doFinal(plain);
		 
			//String enStr = new String(Base64.encodeBase64(encrypted));
			return encrypted;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException
				| IllegalBlockSizeException | BadPaddingException  e) {
			e.printStackTrace();
		}
		return null;
    }
    
    
    public byte[] decrypt(byte[] cipher)  {
        Cipher c;
		try {
			c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
	        byte[] byteStr = Base64.decodeBase64(cipher);
			return c.doFinal(cipher);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException 
				 | IllegalBlockSizeException | BadPaddingException e) {
			
			e.printStackTrace();
		} 
        return null;
    }
    
    
    
    
    
    
    /**
     * 
     * @param is
     * @param os
     */
    private void copy(InputStream is, OutputStream os){
        int i;
        byte[] b = new byte[1024];
        try {
			while((i=is.read(b))!=-1) {
			  os.write(b, 0, i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * file을 AES256으로 암호화
     * @param in : plain
     * @param out : cipher
     */
    public void encrypt(File in, File out) {
    	Cipher c;
		try {
			c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));

	        FileInputStream is = new FileInputStream(in);
	        CipherOutputStream os = new CipherOutputStream(new FileOutputStream(out), c);
	        
	        copy(is, os);
	        
	        is.close();
  	        os.close();
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException 
				| IOException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
        
      }
      
    /**
     * AES256으로 암호화된 file을 복호화
     * @param in : cipher
     * @param out : plain
     */
    public void decrypt(File in, File out){
    	  Cipher c;
    	  try {
  			c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));

  			CipherInputStream is = new CipherInputStream(new FileInputStream(in), c);
  	        FileOutputStream os = new FileOutputStream(out);
  	        
  	        copy(is, os);
  	        
  	        is.close();
  	        os.close();
    	  }catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException 
    			  | IOException | InvalidAlgorithmParameterException e) {
  			e.printStackTrace();
    	  }
    }
    
      /**
       * AES256 으로 암호화 한다.
       * @param str 암호화할 문자열
       * @return
       * @throws NoSuchAlgorithmException
       * @throws GeneralSecurityException
       * @throws UnsupportedEncodingException
       */
	public String encrypt(String str) {
          Cipher c;
  		try {
  			c = Cipher.getInstance("AES/CBC/PKCS5Padding");

  			c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
  		
  			byte[] encrypted;
  		
  			encrypted = c.doFinal(str.getBytes("UTF-8"));
  		 
  			String enStr = new String(Base64.encodeBase64(encrypted));
  			return enStr;
  		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException
  				| IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
  			e.printStackTrace();
  		}
  		return null;
      }

      /**
       * AES256으로 암호화된 txt 를 복호화한다.
       * @param str 복호화할 문자열
       * @return
       * @throws NoSuchAlgorithmException
       * @throws GeneralSecurityException
       * @throws UnsupportedEncodingException
       */
	public String decrypt(String str)  {
          Cipher c;
  		try {
  			c = Cipher.getInstance("AES/CBC/PKCS5Padding");
  			c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
  	        byte[] byteStr = Base64.decodeBase64(str.getBytes());
  			return new String(c.doFinal(byteStr), "UTF-8");

  		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException 
  				| UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
  			e.printStackTrace();
  		} 
          return null;
      }
    
}