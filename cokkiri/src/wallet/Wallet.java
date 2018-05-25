package wallet;

import java.security.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import transaction.*;
import coin.Coin;
import coin.Constant;
import hash.Sha256;
import log.Logging;

public class Wallet {
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private Address address;
	public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); //only UTXOs owned by this wallet.
	//public HashMap<String,TransactionOutput> removeUTXOs=new HashMap<String,TransactionOutput>(); //거래 생성한 다음 없앨 UTXO 임시 저장 (full UTXO set 에서는 아직 잇어서 생기는 문제때문에 만들었음)
	
	/**
	 * password 입력받아 key 생성 or load
	 * @param passwd
	 * @param check
	 */
	public Wallet(String passwd, boolean check){ //check true이면 새로운 key 생성, false면 키파일 로드
		KeyPair keyPair;
		passwd=Sha256.hash(passwd);
		if(check){
			keyPair=KeyUtil.generateKeyPair(passwd);
			
			if (keyPair.getPrivate()==null || keyPair.getPublic()==null){
				System.out.println("[error]wallet.java >> keypair is null");
				return;
			}else { 
				if(!KeyUtil.checkKeyfile(Constant.pathDir+"/"+Coin.id+"/key")) {
					KeyUtil.saveKeyPair(Constant.pathDir+"/"+Coin.id+"/key", keyPair, passwd);
					KeyUtil.savePubKey(Constant.pathDir+"/"+Coin.id+"/key", keyPair.getPublic());
				}
			}
		}
		else {
			keyPair = KeyUtil.loadKeyPair(Constant.pathDir+"/"+Coin.id+"/key", passwd);
			privateKey=keyPair.getPrivate();
		}
		
		publicKey=keyPair.getPublic();
		address=new Address(publicKey);
	}
	
	//public key만 load
	public Wallet() {
		publicKey=KeyUtil.loadPubKey(Constant.pathDir+"/"+Coin.id+"/key");
		address=new Address(publicKey);
	}
	
	/**
	 * password를 매개변수로 받아 암호화된 공개키 파일 복호화하여 원본 공개키 파일과 비교
	 * @param passwd
	 * @return -1(인증 실패), 0(키파일 없음), 1(인증 성공)
	 */
	public int authenticate(String passwd){
		System.out.println("pw : "+passwd);
		passwd=Sha256.hash(passwd);
		if(KeyUtil.checkKeyfile(Constant.pathDir+"/"+Coin.id+"/key")){
			if(!KeyUtil.authenticate(Constant.pathDir+"/"+Coin.id+"/key", passwd)){
				//System.out.println("checkHash "+checkHash);
				//System.out.println("keypairhash "+keyPairHash);
				return -1; //인증 실패
			}
			return 1; //인증 성공
		}
		return 0; //키파일 없음
	}
	
	/**
	 * 확인용 - 파라미터로 받은 hashmap 콘솔창에 출력
	 * @param hashmap
	 */
	public static void printHashmap(HashMap hashmap) {
		Iterator<String> iterator = hashmap.keySet().iterator();
		// 반복자를 이용해서 출력
		while (iterator.hasNext()) { 
			String key = (String)iterator.next(); 
			System.out.println("key="+key+" / value=" + ((TransactionOutput)hashmap.get(key)).value
					+" / receiver=" + ((TransactionOutput)hashmap.get(key)).receiver.getString());  // 출력
		}
	}
	
	/**
	 * returns balance and stores the UTXO's owned by this wallet in this.UTXOs
	 */
	public float getBalance() {
		float total = 0;	
		Logging.consoleLog("function call - getBalnace()");
		for (Map.Entry<String, TransactionOutput> item: Coin.blockchain.UTXOs.entrySet()){
			TransactionOutput UTXO = item.getValue();
			System.out.println("UTXO : "+UTXO.toJSONObject());
			if(UTXO.isMine(address)) { //if output belongs to me ( if coins belong to me )
	           	//if(removeUTXOs.get(item.getKey())==null) {
	           		UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
	           		total += UTXO.value ;
	           		//System.out.println("plus : "+UTXO.value); //확인용
	           	//}
	       	} 	
		}  
		printHashmap(UTXOs); //확인용
	       
		return total;
	}
	
	/**getter
	 */
	public PrivateKey getPrivateKey() {return privateKey;}
	public PublicKey getPublicKey() {return publicKey;}
	public Address getAddress() {return address;}

	
}
