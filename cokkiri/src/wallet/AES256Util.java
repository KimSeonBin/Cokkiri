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
 *AES256 ��ȣȭ�� �����ϴ� Ŭ����
 */
public class AES256Util {  
    private String iv;
    private Key keySpec;

    /**
     * 16�ڸ��� Ű���� �Է��Ͽ� ��ü�� �����Ѵ�.
     * @param key ��/��ȣȭ�� ���� Ű��
     * @throws UnsupportedEncodingException Ű���� ���̰� 16������ ��� �߻�
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
     * file�� AES256���� ��ȣȭ
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
     * AES256���� ��ȣȭ�� file�� ��ȣȭ
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
       * AES256 ���� ��ȣȭ �Ѵ�.
       * @param str ��ȣȭ�� ���ڿ�
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
       * AES256���� ��ȣȭ�� txt �� ��ȣȭ�Ѵ�.
       * @param str ��ȣȭ�� ���ڿ�
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