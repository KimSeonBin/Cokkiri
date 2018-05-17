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
	public static float coinMax=10000; //가상으로(코인 시스템 내의 코인 갯수)
	public final static String pathDir="ajoucoin";
	public final static int DIFFICULTY=3;
	public static String id="";

	public final static int minPWLen = 16; //pw 최소길이
	
	//user에게 pw 입력받음 (현재는 3회로 제한)++++++++++++++++++++++++++++
	public static String getPW() {
		String passwd;
		
		for(int i=0;i<3;i++) {
			System.out.print("password : ");
<<<<<<< HEAD
//			passwd=scn.next();
//			scn.nextLine();
			passwd = JOptionPane.showInputDialog("계좌 비밀번호를 입력하세요.");
=======
			passwd = JOptionPane.showInputDialog("계좌 비밀번호를 입력하세요(16자 이상)");
			
>>>>>>> branch 'master' of https://github.com/KimSeonBin/Cokkiri.git
			if(!checkPWInput(passwd)) {
				System.out.println("it's too short(16 letters or more)");
				continue;
			}else return passwd;
		}
		return null;
	}
	
	//user input for pw 검사(16자 이상이면 true, 아니면 false)
	public static boolean checkPWInput(String input) {
		if(input==null)return false;
		if(input.length()<minPWLen) return false;
		return true;
	}

	//로그인 성공하면 호출됨. 
	//공개키, 주소 로드, block 로드
	public static void loginSuccess() {
		blockchain= new BlockChain("ajoucoin", DIFFICULTY);

		File file=new File(pathDir);
		if(!file.exists()) {
			file.mkdirs();
			/****************************/
			//peer에 블록체인 받아오기
			
			/**************************/
			
		}
		String passwd;

		if(!KeyUtil.checkKeyfile(pathDir+"/key_"+id)){
			System.out.println("plz enter pw, then the key will be created");
			passwd=getPW();
			if(passwd==null) {
				JOptionPane.showMessageDialog(null, "프로그램 종료");
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
	}
	
}
