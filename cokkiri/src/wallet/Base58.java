package wallet;

import java.math.BigInteger; 
 
/**
 * base58 관련 연산 수행 위한 클래스
 * @author user
 *
 */
public class Base58  { 
	private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"; 
    private static final BigInteger BASE = BigInteger.valueOf(58); 
 
    public static String encode(byte[] input){ 
        // This could be a lot more efficient. 
        BigInteger bi = new BigInteger(1, input); 
        StringBuffer s = new StringBuffer(); 
        while (bi.compareTo(BASE) >= 0)  
        { 
            BigInteger mod = bi.mod(BASE); 
            s.insert(0, ALPHABET.charAt(mod.intValue())); 
            bi = bi.subtract(mod).divide(BASE); 
        } 
        s.insert(0, ALPHABET.charAt(bi.intValue())); 
        // Convert leading zeros too. 
        for (byte anInput : input){ 
            if (anInput == 0) s.insert(0, ALPHABET.charAt(0)); 
            else break; 
        } 
        return s.toString(); 
    } 
 
    public static byte[] decode(String input){ 
        byte[] bytes = decodeToBigInteger(input).toByteArray(); 
     
        boolean stripSignByte = bytes.length > 1 && bytes[0] == 0 && bytes[1] < 0; 
        // Count the leading zeros, if any. 
        int leadingZeros = 0; 
        for (int i = 0; input.charAt(i) == ALPHABET.charAt(0); i++) leadingZeros++; 
        
        // Now cut/pad correctly. Java 6 has a convenience for this, but Android can't use it. 
        byte[] tmp = new byte[bytes.length - (stripSignByte ? 1 : 0) + leadingZeros]; 
        System.arraycopy(bytes, stripSignByte ? 1 : 0, tmp, leadingZeros, tmp.length - leadingZeros); 
        return tmp; 
    } 
 
    protected static BigInteger decodeToBigInteger(String input){ 
     BigInteger bi = BigInteger.valueOf(0); 
         
        // Work backwards through the string. 
        for (int i = input.length() - 1; i >= 0; i--){ 
            int alphaIndex = ALPHABET.indexOf(input.charAt(i)); 
            if (alphaIndex == -1){ 
             throw new IllegalArgumentException("In Base58.decodeToBigInteger(), Illegal character " + input.charAt(i) + " at index " + i + ". Throwing new IlleglArgumentException."); 
            } 
            bi = bi.add(BigInteger.valueOf(alphaIndex).multiply(BASE.pow(input.length() - 1 - i))); 
        } 
        return bi; 
    } 
}