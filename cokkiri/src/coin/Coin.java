package coin;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import blockchain.Block;
import blockchain.BlockChain;
import client.*;
import log.Logging;
import server.*;
import transaction.*;
import utill_store.BlockStore;
import wallet.*;

public class Coin {
	public static BlockChain blockchain;
	public static Wallet wallet=null;	
	
	public static float minimumTransaction=0.1f;
	public static float coinMax=1000; //��������(���� �ý��� ���� ���� ����)
	public final static String pathDir="ajoucoin";
	public final static int minPWLen = 16; //pw �ּұ���
	public static String id="";
	
	//user���� pw �Է¹��� (����� 3ȸ�� ����)++++++++++++++++++++++++++++
	public static String getPW() {
//		Scanner scn = new Scanner(System.in);
		String passwd;
		
		for(int i=0;i<3;i++) {
			System.out.print("password : ");
//			passwd=scn.next();
//			scn.nextLine();
			passwd = JOptionPane.showInputDialog("���� �ĺ��ڸ� �Է��ϼ���.");
			if(!checkPWInput(passwd)) {
				System.out.println("it's too short(16 letters or more)");
				continue;
			}else return passwd;
		}
		return null;
	}
	
	//user input for pw �˻�(16�� �̻��̸� true, �ƴϸ� false)
	public static boolean checkPWInput(String input) {
		if(input.length()<minPWLen) return false;
		return true;
	}

	//�α��� �����ϸ� ȣ���. 
	//����Ű~�ּ� �ε�, block �ε�
	public static void loginSuccess() {
		blockchain= new BlockChain("ajoucoin");

		File file=new File(pathDir);
		if(!file.exists()) {
			file.mkdirs();
			/****************************/
			//peer�� ���ü�� �޾ƿ���
			
			/**************************/
			
		}
		String passwd;

		if(!KeyUtil.checkKeyfile(pathDir+"/key_"+id)){
			System.out.println("plz enter pw, then the key will be created");
			passwd=getPW();
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
		

	}
	
}
