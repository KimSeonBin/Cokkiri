package appview;

import java.security.Security;

import appactivity.LoginActivity;
import server.CentralServer;
import login.Login;


public class main {
	static LoginActivity login;
	public static void main(String[] args) {
		
		//���α׷� ���۽� ���� ���� ���ü�� ���� �� �޾ƾ��Ѵ�.+�Ǿ� ����Ʈ��..
		
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
