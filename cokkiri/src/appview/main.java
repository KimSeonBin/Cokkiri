package appview;

import java.security.Security;

import appactivity.LoginActivity;
import server.CentralServer;
import login.Login;


public class main {
	static LoginActivity login;
	public static void main(String[] args) {
		
		//프로그램 시작시 먼저 기존 블록체인 전부 다 받아야한다.+피어 리스트도..
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		//////////////////////////////////////////////////////////////////////
		
		//----------------------------server start-----------------------//
		new Thread() {
			public void run() {
				try {
					CentralServer.CentralServer();
				} catch (Exception e) {}
			}
		}.start();
		////////////////////////////////////////////////////////////////////

		
		login = new LoginActivity();
		login.start();
	}

}
