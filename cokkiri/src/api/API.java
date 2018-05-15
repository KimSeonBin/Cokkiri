package api;

import wallet.Address;
import wallet.Wallet;

public class API {
	public static Wallet wallet=null;	
	public static String id="";

	public void requestLogin(String id, String pw) {
		
	}
	
	public void requestUserInfo(Address address) {
		
	}
	
	public void requestHistory(Address adress) {
		
	}
	
	public void requestSendCoin(Wallet sender, float value, Address receiver) {
		
	}
	
	public boolean requestPurchase(float value) {
		return false;
	}
	
	public boolean requestSell(float value) {
		return false;
	}
}
