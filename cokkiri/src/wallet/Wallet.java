package wallet;

import java.security.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import transaction.*;
import coin.Coin;

public class Wallet {
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private Address address;
	public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); //only UTXOs owned by this wallet.
	//public HashMap<String,TransactionOutput> removeUTXOs=new HashMap<String,TransactionOutput>(); //�ŷ� ������ ���� ���� UTXO �ӽ� ���� (full UTXO set ������ ���� �վ ����� ���������� �������)
	
	/**
	 * password �Է¹޾� key ���� or load
	 * @param passwd
	 * @param check
	 */
	public Wallet(String passwd, boolean check){ //check true�̸� ���ο� key ����, false�� Ű���� �ε�
		KeyPair keyPair;
		
		if(check){
			keyPair=KeyUtil.generateKeyPair(passwd);
			
			if (keyPair.getPrivate()==null || keyPair.getPublic()==null){
				System.out.println("[error]wallet.java >> keypair is null");
				return;
			}else { 
				if(!KeyUtil.checkKeyfile(Coin.pathDir+"/key_"+Coin.id)) {
					KeyUtil.SaveKeyPair(Coin.pathDir+"/key_"+Coin.id, keyPair, passwd);
					KeyUtil.SaveKeyPairHash(Coin.pathDir+"/key_"+Coin.id, KeyUtil.hashKeyPair(keyPair));
				}
			}
		}
		else keyPair = KeyUtil.LoadKeyPair(Coin.pathDir+"/key_"+Coin.id, passwd);
		
		privateKey=keyPair.getPrivate();
		publicKey=keyPair.getPublic();
		address=new Address(publicKey);
	}
	
	//public key�� load
	public Wallet() {
		publicKey=KeyUtil.LoadPubKey(Coin.pathDir+"/key_"+Coin.id);
		address=new Address(publicKey);
	}
	
	/**
	 * password�� �Ű������� �޾� keypair�����ϰ� hash ���� ���Ͽ� ������ ������ Ű�� ���� ���� ����
	 * @param passwd
	 * @return -1(���� ����), 0(Ű���� ����), 1(���� ����)
	 */
	public static int authenticate(String passwd){
		if(KeyUtil.checkKeyfile(Coin.pathDir+"/key_"+Coin.id)){
			KeyPair check=KeyUtil.generateKeyPair(passwd);
			String checkHash=KeyUtil.hashKeyPair(check);
			String keyPairHash=KeyUtil.LoadKeyPairHash(Coin.pathDir+"/key_"+Coin.id);
			if(!checkHash.equals(keyPairHash)){
				return -1; //���� ����
			}
			return 1; //���� ����
		}
		return 0; //Ű���� ����
	}
	
	/**
	 * Ȯ�ο� - �Ķ���ͷ� ���� hashmap �ܼ�â�� ���
	 * @param hashmap
	 */
	public static void printHashmap(HashMap hashmap) {
		Iterator<String> iterator = hashmap.keySet().iterator();
		// �ݺ��ڸ� �̿��ؼ� ���
		while (iterator.hasNext()) { 
			String key = (String)iterator.next(); 
			System.out.println("key="+key+" / value=" + ((TransactionOutput)hashmap.get(key)).value
					+" / receiver=" + ((TransactionOutput)hashmap.get(key)).receiver.getString());  // ���
		}
	}
	
	/**
	 * returns balance and stores the UTXO's owned by this wallet in this.UTXOs
	 */
	public float getBalance() {
		float total = 0;	
		System.out.println("***function getBalnace()****");
		for (Map.Entry<String, TransactionOutput> item: Coin.blockchain.UTXOs.entrySet()){
			TransactionOutput UTXO = item.getValue();
			System.out.println("UTXO : "+UTXO.toJSONObject());
			if(UTXO.isMine(address)) { //if output belongs to me ( if coins belong to me )
	           	//if(removeUTXOs.get(item.getKey())==null) {
	           		UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
	           		total += UTXO.value ;
	           		//System.out.println("plus : "+UTXO.value); //Ȯ�ο�
	           	//}
	       	} 	
		}  
		printHashmap(UTXOs); //Ȯ�ο�
	       
		return total;
	}
	
	/**getter
	 */
	public PrivateKey getPrivateKey() {return privateKey;}
	public PublicKey getPublicKey() {return publicKey;}
	public Address getAddress() {return address;}

	
}
