package blockchain;

import java.security.PublicKey;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import coin.Coin;
import appactivity.*;
import hash.Sha256;
import log.Logging;
import transaction.*;
import utill_store.BlockStore;
import wallet.Address;
import wallet.Wallet;

public class Block {
	private long index;
	private int blockSize;
	private BlockHeader blockHeader;
	private int transactionCount;
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	private String blockHash;
	
	
	public Block() {}
	public Block(long genesistime) {
		this.blockHeader=new BlockHeader(genesistime);
		blockHash=calculateHash();
		index=0;
	}
	public Block(long index, int blockSize, int transactionCount, String blockHash) {
		this.index=index;
		this.blockSize = blockSize;
		this.transactionCount = transactionCount;
		this.blockHash = blockHash;
	}
	
	public Block(String prevBlockHash, long prevBlockIndex, Address minerAddress){
		index=prevBlockIndex+1;
		System.out.println("*&*& index : "+index);
		if(index <1000) { // ä�� ���� ���� 1000�� �������
			Address coinbase=new Address();
			coinbase.setAddress(Coin.pathDir);
			Transaction benefit = new Transaction(coinbase, minerAddress, 10, null);
			//genesisTransaction.generateSignature(coinbase.getPrivateKey());	 //signature ��� �ؾ����� �𸣰���
			benefit.inputs=new ArrayList<TransactionInput>();
			benefit.outputs.add(new TransactionOutput(benefit.receiver, benefit.value, benefit.TxId)); //manually add the Transactions Output
			addTx(benefit);
			Coin.blockchain.UTXOs.put(benefit.outputs.get(0).id, benefit.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
			//���� ���� ���⼭ ����ߵ����� �𸣰��� - �ŷ� ��ȿ�� ���� ������ ����..
		}
		this.blockHeader=new BlockHeader(prevBlockHash);
	}

	public boolean addTx(Transaction transaction) {
		if(transaction == null) return false;		
		if(blockHeader != null) { //null�� ���� benefit tx
			if((!"0".equals(blockHeader.getPreviousBlockHash()))) { //genesisblock �ƴ� ���
				/*
				if((transaction.processTransaction() != true)) {
					System.out.println("Transaction failed to process. Discarded.");
					return false;
				}
				//�ϴ��� processTransaction ������ wallet���� ó�� sendfund �� �� �����ϰ� ����.
				*/
			}
		}
		transactionCount++;
		transactions.add(transaction);
		System.out.println("Transaction Successfully added to Block");
		return true;
	}
	
	private String calculteMerkleRoot(ArrayList<Transaction> transactions) {
		int count = transactions.size();
		ArrayList<String> previousTreeLayer=new ArrayList<String>();
		for(Transaction t : transactions){
			System.out.println("tx : "+t.toJSONObject());
			previousTreeLayer.add(t.TxId);
		}
		ArrayList<String> treeLayer = previousTreeLayer;
		while(count > 1){
			treeLayer=new ArrayList<String>();
			for(int i=1;i<previousTreeLayer.size();i++){
				treeLayer.add(Sha256.hash(previousTreeLayer.get(i-1)+previousTreeLayer.get(i)));
			}
			count=treeLayer.size();
			previousTreeLayer=treeLayer;
		}
		String merkleRoot=(treeLayer.size()==1)?treeLayer.get(0) : "";
		System.out.println("merkle root : "+merkleRoot);
		return merkleRoot;
	}

	public String calculateHash() {
		String hash = Sha256.hash( 
				blockHeader.getPreviousBlockHash()+
				Long.toString(blockHeader.getTimestamp()) +
				Long.toString(blockHeader.nonce) + 
				blockHeader.getMerkleRootHash()
				);
		return hash;
	}

	/**
	 * �Ű������� ���� ��� �ؽ� ���� �ε��� �޾� ��� ����, �߰��� ��� ������ �߰� �ʿ�
	 */
	public boolean isBlockValid(String prevBlockHash, long prevBlockIndex){
		System.out.println("---isBlockvalid---");
		if(!(prevBlockHash.equals(blockHeader.getPreviousBlockHash()))) {
			System.out.println("invalid block TT 1");
			return false;
		}
		if(!(index==prevBlockIndex+1)) {
			System.out.println("invalid block TT 3");
			return false;
		}
		if(!isBlockValid()) {
			System.out.println("invalid block TT 2");
			return false; 
		}
		
		return true;
	}
	
	/**
	 * ����� Ʈ����� üũ, ��� �ε��� Ȯ���Ͽ� ä������ ������Ѵٸ� �ش���� üũ
	 */
	public boolean isBlockValid() {
		System.out.println("---isBlockValid()---");
		for(Transaction t: transactions) {
			if (t.isTransactionValid() == false) {
				System.out.println("invalid block 1");
				return false;
			}
		}
		if(index > 1000) {  // ä���������� �ϴ� ��� ������ üũ
			for(Transaction t: transactions) {
				if(t.sender.equals("null")) {
					System.out.println("invalid block2");
					return false;
				}
			}
		}
		return true;
	}
	
	public void mineBlock() {
		blockHeader.setTimestamp();
		blockHeader.setMerkleRootHash(calculteMerkleRoot(transactions));
		String target =  new String(new char[blockHeader.difficulty]).replace('\0', '0');//Create a string with difficulty * "0" 
		this.blockHash=calculateHash();
		
		while(!this.getBlockHash().substring( 0, blockHeader.difficulty).equals(target)) {
			blockHeader.nonce++;
			this.blockHash=calculateHash();
		}
		
		log.Logging.consoleLog("Block Mined!");
		log.Logging.consoleLog("-----------------------------------------------------------------------------------------");
		System.out.println(getString());
		log.Logging.consoleLog("-----------------------------------------------------------------------------------------");
		System.out.println(toJSONObject());
		log.Logging.consoleLog("-----------------------------------------------------------------------------------------");

	}
		
	public long getBlockIndex() {return index;}
	public String getBlockHash() {return blockHash;}
	public BlockHeader getBlockHeader() {return blockHeader;}
	public void setBlockHeader(BlockHeader blockHeader) {this.blockHeader = blockHeader;}

	public String getString(){
		String blockStr="";
		blockStr+="index : "+String.valueOf(index)+"\r\n";
		blockStr+=getBlockHeader().getString();
		for(int i=0;i<transactions.size();i++) blockStr+="transaction "+String.valueOf(i)+transactions.get(i).getString();
		
		return blockStr;
	}

	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();
		json.put("index", index);
		json.put("blockSize", blockSize);
		json.put("blockHeader", blockHeader.toJSONObject());
		json.put("transactionCount", transactionCount);
		json.put("transactions", convertTX_toJSONArray());
		json.put("blockHash", blockHash);
		
		System.out.println("!!new block json!!");
		System.out.println(json);
		return json;
	}
	
	public void convertClassObject(JSONObject json) {
		
		Logging.consoleLog("function call - convertClassObject()");
		
		this.index=((Number)json.get("index")).longValue();
		this.blockSize = ((Number)json.get("blockSize")).intValue();
		this.blockHeader = new BlockHeader();
		blockHeader.convertClassObject((JSONObject)json.get("blockHeader"));
		this.transactionCount = ((Number)json.get("transactionCount")).intValue();
		JSONArray array = new JSONArray();
		array = (JSONArray)json.get("transactions");
		converJSONArray_toTX(array);
		this.blockHash = (String)json.get("blockHash");
		
	}
	
	private void converJSONArray_toTX(JSONArray array){
		for(Object txJson : array) {
			Transaction tx = new Transaction();
			tx.convertClassObject((JSONObject)txJson);
			this.transactions.add(tx);
		}
	}
	
	private JSONArray convertTX_toJSONArray() {
		JSONArray array = new JSONArray();
		for(Transaction tx: transactions) {
			array.add(tx.toJSONObject());
		}
		return array;
	}
	
}
