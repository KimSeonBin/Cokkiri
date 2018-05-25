package mining;

import java.util.ArrayList;
import java.util.Iterator;

import blockchain.Block;
import client.Client;
import coin.Coin;
import log.Logging;
import transaction.Transaction;
import utill_network.MsgType;
import wallet.Address;

public class Mining {
	
	public static ArrayList<Transaction> transactionPool = new ArrayList<Transaction>();

	public static Block mining(Address address) {
		Block block=new Block(Coin.blockchain.getPreviousBlockHash(), Coin.blockchain.getPreviousBlockIndex(), address); 
		ArrayList<Transaction> miningTx=new ArrayList<Transaction>();

		int size = transactionPool.size();
	
		for(int k=0;k<size;k++){
			Transaction t = transactionPool.get(k);
			miningTx.add(t);
			block.addTx(t);
		}
		
		Coin.blockchain.addBlock(block);
		if(Coin.blockchain.isChainValid()){
			Coin.blockchain.storeBlock(block);
			//---------------------블록 전파--------------------//
			new Thread() {
				public void run() {
					try {
						System.out.println("run broadcast");
						Client.broadcastToAdmin(MsgType.BLOCK_TRANSFER_MSG+block.getBlockHeader().getPreviousBlockHash()+" "+block.toJSONObject());
						Client.broadcastToPC(MsgType.BLOCK_TRANSFER_MSG+block.getBlockHeader().getPreviousBlockHash()+" "+block.toJSONObject());
					} catch (Exception e) {}
				}
			}.start();
			//----------------------------------------------//
			Iterator<Transaction> it = miningTx.iterator();
			while(it.hasNext()) {
				//Coin.blockchain.transactionPool.remove(it.next());
				transactionPool.remove(it.next());
			}
			
			return block;
		}
		else {
			Logging.consoleLog("*error : the chain is not valid");
			Coin.blockchain.remove(block);
			return null;
		}
	}
}
