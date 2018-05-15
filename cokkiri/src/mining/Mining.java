package mining;

import blockchain.Block;
import client.Client;
import coin.Coin;
import log.Logging;
import transaction.Transaction;
import utill_network.MsgType;
import wallet.Address;

public class Mining {
	public static Block mining(Address address) {
		System.out.println("mining()");
		Block block=new Block(Coin.blockchain.getPreviousBlockHash(), address); 
		int size=Coin.blockchain.transactionPool.size();
		System.out.println("size : "+String.valueOf(size));
		for(int k=0;k<size;k++){
			Transaction t = Coin.blockchain.transactionPool.get(k);
					
			//		Coin.blockchain.transactionPool.get();
			System.out.println("transaction added...");
			System.out.println(t.toJSONObject());
			block.addTx(t);
		}
		System.out.println("before add this block..");
		System.out.println(block.toJSONObject());
		Coin.blockchain.addBlock(block);
		if(Coin.blockchain.isChainValid()){
			Coin.blockchain.storeBlock(block);
			//---------------------블록 전파--------------------//
			new Thread() {
				public void run() {
					try {
						Client.broadcast(MsgType.BLOCK_TRANSFER_MSG+block.getBlockHeader().getPreviousBlockHash()+" "+block.toJSONObject());
					} catch (Exception e) {}
				}
			}.start();
			//----------------------------------------------//
			return block;
		}
		else {
			Logging.consoleLog("*error : the chain is not valid");
			Coin.blockchain.remove(block);
			return null;
		}
	}
}
