package blockchain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import client.Client;
import log.Logging;
import transaction.Transaction;
import transaction.TransactionInput;
import transaction.TransactionOutput;
import utill_store.BlockStore;
import wallet.Address;

public class BlockChain {
	public static List<Block> blockchain= new ArrayList<Block>();
	public static String pathDir;
	public static int DIFFICULTY = 3; //ä�� ���̵�

	//public static HashMap<String, Transaction> transactionPool = new HashMap<String, Transaction>();
	public static ArrayList<Transaction> transactionPool = new ArrayList<Transaction>();
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	
	public static ArrayList<Transaction> allTx=new ArrayList<Transaction>();
	private static int checkBlocknum=0;
	
	public BlockChain(String path) {
		pathDir=path;
	}

	public boolean isChainValid() { //��� ����� previousblockheader�׸�� ���� ����� �ؽ����� ���Ͽ� ���ü�� ����
		//genesisblock Ȯ���ʿ�//
		for(int i=1;i<blockchain.size();i++){
			Block currentBlock=blockchain.get(i);
			Block previousBlock=blockchain.get(i-1);
			if(!currentBlock.getBlockHash().equals(currentBlock.calculateHash())) {
				return false; //�ؽ��� Ȯ��
			}
			if(!currentBlock.getBlockHeader().getPreviousBlockHash().equals(previousBlock.getBlockHash())) {
				return false; //��������ؽ��� ��
			}
			//if(!currentBlock.mineCheck()) return false;
			//��Ͼ� �ŷ��� ���� ����
		}
		return true;
	}
	
	public String getPreviousBlockHash() {
		return blockchain.get(blockchain.size()-1).getBlockHash(); //���ü�ο� ����Ǿ��ִ� ���� �ֱ� ����� ��� �ؽ����� ����
	}

	public void addBlock(Block block) {
		block.getBlockHeader().setDifficulty(DIFFICULTY);
		block.mineBlock();
		blockchain.add(block);
	}
	
	public void storeBlock(Block block) {
		BlockStore store = new BlockStore();
		store.writeBlock(block, blockchain.size(), 0);
		store.closeDB();
	}
	
	//���ü�ο� ����Ǿ��ִ� ���� �ֱ��� ����� ����
	public Block getNewestBlock(){
		return blockchain.get(blockchain.size()-1); 
	}
	
	public void loadFullBlock() {
		BlockStore store = new BlockStore();
		for(int i = 1; true; i++) {
			Block block = store.readBlock(i);

			if(block == null) break;
			else {
				blockchain.add(block);
				Logging.consoleLog(String.valueOf(i) + "��° block : " + block.getString());
				Logging.consoleLog(" => json : "+block.toJSONObject());
			}
		}
		store.closeDB();
		getUTXOs(); //�ϴ� ���⿡
	}
	
	//block���� utxo ��������
	public void getUTXOs() {
		if(allTx==null) return;
		
		ArrayList<Transaction> tmpAllTx = new ArrayList<Transaction>();
		
		if(getAllTx()==0) return;
		
		tmpAllTx.addAll(allTx);
		
		//ArrayList<TransactionInput> txInput = new ArrayList<TransactionInput>();
		ArrayList<TransactionOutput> usedTxOutput = new ArrayList<TransactionOutput>();
		HashMap<String, TransactionOutput>txOutput = new HashMap<String, TransactionOutput>();

		//ArrayList<TransactionOutput> txOutput = new ArrayList<TransactionOutput>();

		Iterator<Transaction> itTx=tmpAllTx.iterator();
		while(itTx.hasNext()) { //�ŷ��ϳ� Ȯ��
		    Transaction tmp = itTx.next();
			Logging.consoleLog("[all Tx]tx : "+tmp.toJSONObject());
			
			//extracts all inputs
			if(tmp.inputs !=null) {
				Iterator<TransactionInput> itInput = tmp.inputs.iterator();
				while(itInput.hasNext()) {
					usedTxOutput.add(itInput.next().UTXO);
				    //txInput.add(itInput.next());
				}
			}
			//extracts all outputs
			Iterator<TransactionOutput> itOutput = tmp.outputs.iterator();
			while(itOutput.hasNext()) {
				TransactionOutput txout=itOutput.next();
				txOutput.put(txout.id, txout);
			}
		}
			
		//outputs - inputs = utxo
		Iterator<TransactionOutput> itInput = usedTxOutput.iterator();
		while(itInput.hasNext()) {
			txOutput.remove(itInput.next().id);
		}
		
		Iterator<String> keys = txOutput.keySet().iterator();
		while(keys.hasNext()) {
			String tmpkey=keys.next();
			UTXOs.put(tmpkey, txOutput.get(tmpkey));
			Logging.consoleLog("add UTXO~~ : "+txOutput.get(tmpkey).toJSONObject());

		}
	}

	public int getAllTx() {
		int plus=0;
		System.out.println(" GETALLTX()");
		if(blockchain.size()==checkBlocknum-1) {
			//�Ƹ� ���ο� ��� ������ ���� ��
			return plus;
		}
			
		Iterator<Block> itBlock = blockchain.iterator();
		System.out.println("$$$check : blockchaini size : "+String.valueOf(blockchain.size()));
		for(int i=0;i<checkBlocknum;i++) itBlock.next(); //�̹� Ȯ���� ��� �ߺ� �Ѿ�� ����

		ArrayList<Transaction> txs=new ArrayList<Transaction>();
			
		while(itBlock.hasNext()){ //��� �ϳ� Ȯ��
			System.out.println(" block �ϳ� Ȯ��");
		    txs.addAll(itBlock.next().transactions);    
		    plus++;
		}
		checkBlocknum+=plus;
		allTx.addAll(txs);	
		return plus;
	}
		
	public int getSize() {
		return blockchain.size();
	}

	public void remove(Block block) {
		blockchain.remove(block);
	}
	

	
	//block�� �Ű������� �޾� transaction pool �� ��ġ�� ��� ���� (block ���� ���� ��� ����, ���� ȣ�������� ����)
	public void removeTx(Block newblock) {
		ArrayList<Transaction> transactions = newblock.transactions;
		Transaction tmp=new Transaction();
		for(int i=0;i<transactions.size();i++) {
			tmp=transactions.get(i);
			if(transactionPool.contains(tmp))transactionPool.remove(tmp);
			/////////////////////
			// + ���� ä������ ����ִٸ� �װͿ� ���ؼ��� �ؾ��Ѵ�
			/////////////////////
		}
	}
		
}
