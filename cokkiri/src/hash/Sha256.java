package hash;

import java.security.MessageDigest;

public class Sha256 {
    public static String hash(String text){
        MessageDigest md;
        
        // get SHA-256 from MessageDigest
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (java.security.NoSuchAlgorithmException e) {
            System.err.println("No such algorithm SHA-256!" + e.getMessage());
            return null;
        }
        
        String password = text;
        md.update(password.getBytes());
        byte byteData[] = md.digest();
        
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();   
    }
}