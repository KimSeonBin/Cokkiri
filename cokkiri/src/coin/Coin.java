package coin;
import java.io.File;
import java.util.HashMap;

import javax.swing.JOptionPane;

import blockchain.Block;
import blockchain.BlockChain;
import bookmark.Bookmark;
import wallet.*;

public class Coin {
	public static BlockChain blockchain;
	public static Wallet wallet=null;	
	public static HashMap<String, String> bookmark = new HashMap<String, String>();  //name, address
	
	public static float minimumTransaction=0.1f;

	public final static int DIFFICULTY=3;
	public static String id="";

	
	//user���� pw �Է¹��� (����� 3ȸ�� ����)++++++++++++++++++++++++++++
	public static String getPW() {
		String passwd;
		
		for(int i=0;i<3;i++) {
			passwd = JOptionPane.showInputDialog("���� ��й�ȣ�� �Է��ϼ���(16�� �̻�)");

			if(!checkPWInput(passwd)) {
				System.out.println("it's too short(16 letters or more)");
				continue;
			}else return passwd;
		}
		return null;
	}
	
	//user input for pw �˻�(16�� �̻��̸� true, �ƴϸ� false)
	public static boolean checkPWInput(String input) {
		if(input==null)return false;
		if(input.length()<Constant.minPWLen) return false;
		return true;
	}

	//�α��� �����ϸ� ȣ���. 
	//����Ű, �ּ� �ε�, block �ε�
	public static void loginSuccess() {
		System.out.println("Coin.java loginSuccess()");
		blockchain= new BlockChain("ajoucoin", DIFFICULTY);

		File file=new File(Constant.pathDir);
		if(!file.exists()) {
			file.mkdirs();
			/****************************/
			//peer�� ���ü�� �޾ƿ���
			
			/**************************/
			
		}
		String passwd;

		if(!KeyUtil.checkKeyfile(Constant.pathDir+"/"+id+"/key")){
			System.out.println("plz enter pw, then the key will be created");
			passwd=getPW();
			if(passwd==null) {
				JOptionPane.showMessageDialog(null, "���α׷� ����");
				System.exit(0);
			}
			Wallet resChk = new Wallet(id+passwd, true);
			if (resChk.getPrivateKey()!=null&&resChk.getPublicKey()!=null){
				System.out.println("the key is created");
			}else{
				System.out.println("failed to create key");
				return;
			}
		}
		
		wallet=new Wallet();
		blockchain.loadFullBlock();
		if(blockchain.getSize() == 0) {		
			Block genesisBlock=new Block(3);
			blockchain.blockchain.add(genesisBlock);
			blockchain.storeBlock(genesisBlock);
		}
		
		Bookmark.loadBookmark();
		Cash.loadCash();
	}
	
}
