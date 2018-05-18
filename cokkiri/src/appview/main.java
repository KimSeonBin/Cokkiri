package appview;

import java.io.File;
import java.security.Security;

import appactivity.LoginActivity;
import blockchain.BlockChain;
import coin.Coin;
import server.CentralServer;
import utill_network.PeerList;
import login.Login;


public class main {
	static LoginActivity login;
	public static void main(String[] args) {
		
		//���α׷� ���۽� ���� ���� ���ü�� ���� �� �޾ƾ��Ѵ�.+�Ǿ� ����Ʈ��..
		

		File file=new File(Coin.pathDir);
		if(!file.exists()) {
			file.mkdirs();
			/****************************/
			//peer�� ���ü�� �޾ƿ���
			
			/**************************/
			
		}
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		//////////////////////////////////////////////////////////////////////
		PeerList.init();
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
