package blockchain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import log.Logging;
import transaction.Transaction;
import transaction.TransactionInput;
import transaction.TransactionOutput;
import utill_store.BlockStore;

public class BlockChain {
	public static List<Block> blockchain= new ArrayList<Block>();
	public static String pathDir;
	public static int difficulty; //ä�� ���̵�

	//public static HashMap<String, Transaction> transactionPool = new HashMap<String, Transaction>();
	//public static ArrayList<Transaction> transactionPool = new ArrayList<Transaction>();
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	
	public static ArrayList<Transaction> allTx=new ArrayList<Transaction>();
	private static int checkBlocknum=0;
	
	public BlockChain(String path, int difficulty) {
		pathDir=path;
		this.difficulty = difficulty;
	}

	public boolean isChainValid() { //��� ����� previousblockheader�׸�� ���� ����� �ؽ����� ���Ͽ� ���ü�� ����
		System.out.println("BlockChain.java isChainValid()");
		ArrayList<Block> tmp = new ArrayList<Block>();
		tmp.addAll(blockchain);
		Block genesis = tmp.get(0);
		if(genesis.getBlockIndex()!=0) {
			System.out.println("i'm invalid genesis 1");
			return false;
		}
		if(genesis.getBlockHeader().getTimestamp()!=3) {
			System.out.println("i'm invalid genesis 2");
			return false;
		}
		if(!genesis.getBlockHeader().getPreviousBlockHash().equals("0")) {
			System.out.println("i'm invalid genesis 3");
			return false;
		}

		for(int i=1;i<blockchain.size();i++){
			Block currentBlock=tmp.get(i);
			Block previousBlock=tmp.get(i-1);
			if(currentBlock.getBlockIndex()!=i) {
				//System.out.println("------------------------");
				//System.out.println(currentBlock.getString());
				//System.out.println(previousBlock.getString());
				System.out.println("i'm invalid block 1");
				return false;
			}
			if(!currentBlock.isBlockValid(previousBlock.getBlockHash(), previousBlock.getBlockIndex())) {
				System.out.println("i'm invalid block 2");
				return false;
			}
		}
		return true;
	}
	
	public String getPreviousBlockHash() {
		return blockchain.get(blockchain.size()-1).getBlockHash(); //���ü�ο� ����Ǿ��ִ� ���� �ֱ� ����� ��� �ؽ����� ����
	}

	public long getPreviousBlockIndex() {
		return blockchain.get(blockchain.size()-1).getBlockIndex();
	}
	
	public void addBlock(Block block) {
		block.getBlockHeader().setDifficulty(difficulty);
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
		System.out.println("BlockChain.java loadFullBlock()");
		BlockStore store = new BlockStore();
		for(int i = 1; true; i++) {
			Block block = store.readBlock(i);

			if(block == null) break;
			else {
				blockchain.add(block);
				Logging.consoleLog(String.valueOf(i) + "��° block : " + block.getString());
				//Logging.consoleLog(" => json : "+block.toJSONObject());
			}
		}
		store.closeDB();
		getUTXOs(); //�ϴ� ���⿡
	}
	
	//blockchain���� utxo ��������
	public void getUTXOs() {
		System.out.println("BlockChain.java getUTXOs()");
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
			TransactionOutput test=itInput.next();
			txOutput.remove(test.id);
			System.out.println("chek ..   "+test.toJSONObject());
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
		//System.out.println(" GETALLTX()");
		System.out.println("BlockChain.java getAllTx()");
		//System.out.println("checkBlocknum="+String.valueOf(checkBlocknum));
		
		if(blockchain.size()==checkBlocknum-1) {
			//�Ƹ� ���ο� ��� ������ ���� ��
			return plus;
		}
			
		Iterator<Block> itBlock = blockchain.iterator();
		for(int i=0;i<checkBlocknum;i++) itBlock.next(); //�̹� Ȯ���� ��� �ߺ� �Ѿ�� ����

		ArrayList<Transaction> txs=new ArrayList<Transaction>();
			
		while(itBlock.hasNext()){ //��� �ϳ� Ȯ��
			System.out.println("*��� Ȯ��");
		    txs.addAll(itBlock.next().transactions);    
		    plus++;
		}
		checkBlocknum+=plus;
		allTx.addAll(txs);	
		
		/*
		Iterator it = txs.iterator();
		while(it.hasNext()) {
			System.out.println("check all tx : "+ ((Transaction)it.next()).getString());
		}
		*/
		
		return plus;
	}
		
	public int getSize() {
		return blockchain.size();
	}

	public void remove(Block block) {
		blockchain.remove(block);
	}
		
}
