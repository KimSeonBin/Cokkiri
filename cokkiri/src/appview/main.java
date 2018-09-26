package appview;

import java.io.File;
import java.security.Security;


import appactivity.LoginActivity;
import blockchain.Block;
import blockchain.BlockChain;
import client.Client;
import coin.Coin;
import coin.Constant;
import server.CentralServer;
import utill_network.NodeId;
import utill_network.PeerList;
import login.Login;


public class main {
	static LoginActivity login;
	public static void main(String[] args) {
		
		//���α׷� ���۽� ���� ���� ���ü�� ���� �� �޾ƾ��Ѵ�.+�Ǿ� ����Ʈ��..
		
		File file=new File(Constant.pathDir);
		if(!file.exists()) {
			file.mkdirs();
			/****************************/
			//peer�� ���ü�� �޾ƿ���
			
			/**************************/
			
		}
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		//////////////////////////////////////////////////////////////////////
		PeerList.init();
		NodeId.setNodeId();
		//----------------------------server start-----------------------//
		new Thread() {
			public void run() {
				try {
					CentralServer.CentralServer();
				} catch (Exception e) {}
			}
		}.start();
		////////////////////////////////////////////////////////////////////

		Coin.blockchain= new BlockChain("ajoucoin", Coin.DIFFICULTY);

		File file2=new File(Constant.pathDir);
		if(!file2.exists()) {
			file2.mkdirs();
		}
		Coin.blockchain.loadFullBlock();
		if(Coin.blockchain.getSize() == 0) {		
			Block genesisBlock=new Block(3);
			Coin.blockchain.blockchain.add(genesisBlock);
			Coin.blockchain.storeBlock(genesisBlock);
		}
		
		Client.requestBlock(Coin.blockchain.blockchain.size(), -1);
		
		login = new LoginActivity();
		login.start();
		
	}


}
