package blockchain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import client.Client;
import coin.Coin;
import log.Logging;
import transaction.Transaction;
import transaction.TransactionInput;
import transaction.TransactionOutput;
import utill_store.BlockStore;
import wallet.Address;

public class BlockChain {
	public static List<Block> blockchain= new ArrayList<Block>();
	public static String pathDir;
	public static int DIFFICULTY = 3; //채굴 난이도

	//public static HashMap<String, Transaction> transactionPool = new HashMap<String, Transaction>();
	public static ArrayList<Transaction> transactionPool = new ArrayList<Transaction>();
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	
	public static ArrayList<Transaction> allTx=new ArrayList<Transaction>();
	private static int checkBlocknum=0;
	
	public BlockChain(String path) {
		pathDir=path;
	}

	public boolean isChainValid() { //블록 헤더의 previousblockheader항목과 이전 블록의 해쉬값을 비교하여 블록체인 검증
		//genesisblock 확인
		Block genesis = blockchain.get(0);
		if(genesis.getBlockIndex()!=0) {
			System.out.println("i'm invalid genesis 1");
			return false;
		}
		if(genesis.getBlockHeader().getTimestamp()!=3) {
			System.out.println("i'm invalid genesis 2");
			return false;
		}if(!genesis.getBlockHeader().getPreviousBlockHash().equals("0")) {
			System.out.println("i'm invalid genesis 3");
			return false;
		}
		
		for(int i=1;i<blockchain.size();i++){
			Block currentBlock=blockchain.get(i);
			Block previousBlock=blockchain.get(i-1);
			if(currentBlock.getBlockIndex()!=i) {
				System.out.println("i'm invalid block 1");
				return false;
			}
			if(!currentBlock.isBlockValid(previousBlock.getBlockHash(), previousBlock.getBlockIndex())) {
				System.out.println("i'm invalid block 2");

				return false;
			}
			if(!currentBlock.getBlockHash().equals(currentBlock.calculateHash())) {
				System.out.println("i'm invalid block 3");

				return false; //해쉬값 확인
			}
			if(!currentBlock.getBlockHeader().getPreviousBlockHash().equals(previousBlock.getBlockHash())) {
				System.out.println("i'm invalid block 4");
				return false; //이전블록해쉬값 비교
			}
			if(!(currentBlock.getBlockIndex()==previousBlock.getBlockIndex()+1)) {
			
				System.out.println("i'm invalid block 5");
				return false; //index값 비교
			}
			//if(!currentBlock.mineCheck()) return false;
			//블록안 거래에 대한 검증
		}
		return true;
	}
	
	public String getPreviousBlockHash() {
		return blockchain.get(blockchain.size()-1).getBlockHash(); //블록체인에 저장되어있는 가장 최근 블록의 블록 해쉬값을 리턴
	}

	public long getPreviousBlockIndex() {
		return blockchain.get(blockchain.size()-1).getBlockIndex();
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
	
	//블록체인에 저장되어있는 가장 최근의 블록을 리턴
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
				Logging.consoleLog(String.valueOf(i) + "번째 block : " + block.getString());
				Logging.consoleLog(" => json : "+block.toJSONObject());
			}
		}
		store.closeDB();
		getUTXOs(); //일단 여기에
	}
	
	//block에서 utxo 가져오기
	public void getUTXOs() {
		System.out.println("GETUTXOS//");
		if(allTx==null) return;
		
		ArrayList<Transaction> tmpAllTx = new ArrayList<Transaction>();
		
		if(getAllTx()==0) return;
		
		tmpAllTx.addAll(allTx);
		
		//ArrayList<TransactionInput> txInput = new ArrayList<TransactionInput>();
		ArrayList<TransactionOutput> usedTxOutput = new ArrayList<TransactionOutput>();
		HashMap<String, TransactionOutput>txOutput = new HashMap<String, TransactionOutput>();

		//ArrayList<TransactionOutput> txOutput = new ArrayList<TransactionOutput>();

		Iterator<Transaction> itTx=tmpAllTx.iterator();
		while(itTx.hasNext()) { //거래하나 확인
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
			//System.out.println("chek ..   "+test.toJSONObject());
		}
		
		Iterator<String> keys = txOutput.keySet().iterator();
		while(keys.hasNext()) {
			String tmpkey=keys.next();
			UTXOs.put(tmpkey, txOutput.get(tmpkey));
			//Logging.consoleLog("add UTXO~~ : "+txOutput.get(tmpkey).toJSONObject());

		}
	}

	public int getAllTx() {
		int plus=0;
		System.out.println(" GETALLTX()");
		if(blockchain.size()==checkBlocknum-1) {
			//아마 새로운 블록 데이터 없는 것
			return plus;
		}
			
		Iterator<Block> itBlock = blockchain.iterator();
		System.out.println("$$$check : blockchaini size : "+String.valueOf(blockchain.size()));
		for(int i=0;i<checkBlocknum;i++) itBlock.next(); //이미 확인한 블록 중복 넘어가기 위해

		ArrayList<Transaction> txs=new ArrayList<Transaction>();
			
		while(itBlock.hasNext()){ //블록 하나 확인
			System.out.println(" block 하나 확인");
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

	//block을 매개변수로 받아 transaction pool 에 겹치는 블록 제거 (block 전파 받은 경우 수행, 아직 호출하지는 않음)
	public void removeTx(Block newblock) {
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		transactions.addAll(newblock.transactions);
		Transaction tmp=new Transaction();
		int size=transactions.size();
		System.out.println("@@@removeTx()");
		ArrayList<Transaction> txpool = new ArrayList<Transaction>();
		txpool.addAll(Coin.blockchain.transactionPool);
		for(int i=0;i<size;i++) {
			tmp=transactions.get(i);
			System.out.println(tmp.toJSONObject());
			//synchronized(Coin.blockchain.transactionPool) { //싱크 확인위해 잠시 추가	
			Iterator it = txpool.iterator();
			while(it.hasNext()) {
				Transaction tmpp = (Transaction) it.next();
				if(tmp.TxId.equals(tmpp.TxId)) transactionPool.remove(tmpp);
			//}
			}
			/*
			transactionPool.remove(tmp);
			if(transactionPool.contains(tmp)) {
				System.out.println("removed...");
				transactionPool.remove(tmp);
			}*/
			/////////////////////
			// + 현재 채굴중인 블록있다면 그것에 대해서도 해야한다
			/////////////////////
		}
		System.out.println("------------------------");
	}
		
}
