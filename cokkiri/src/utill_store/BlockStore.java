package utill_store;

import org.iq80.leveldb.*;
import static org.fusesource.leveldbjni.JniDBFactory.*;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import blockchain.Block;
import blockchain.BlockHeader;
import transaction.*;
import coin.Coin;

/*
 * https://en.wikipedia.org/wiki/LevelDB
 * levelDB ��?
 * 
 * https://medium.com/@wishmithasmendis/leveldb-from-scratch-in-java-c300e21c7445
 * levelDB ����
 */

public class BlockStore {
	private DB levelDBStore;
	private Options options;
	//public final static String path="C:/Users/user/ajoucoin/BlockChain";
	
	public BlockStore() {
		options = new Options();
		try {
			levelDBStore = factory.open(new File(Coin.pathDir+"/BlockChain"), options);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/* levelDB�� ���� NoSQL�� key : data �̷� �������� ����
	 * key : "block" + index + "_" + "fork_index"
	 * data: Block�� JSON ����
	 * 
	 * fork_index : Block�� fork���� ��츦 ������ index
	 * 				ó���� 0���� �����ؼ� ��� ���´ٸ� 1�� �����Ѵ�.
	 * 				
	 */
	public void writeBlock(Block block, int index, int fork_index) {
		JSONObject json = block.toJSONObject();
		String key = "block"+String.valueOf(index)+"_"+String.valueOf(fork_index);
		
		levelDBStore.put(bytes(key), bytes(json.toJSONString()));System.out.println(key + ":key");
	}
	
	public Block readBlock(int index) {
		String key = "block"+String.valueOf(index)+"_"+String.valueOf(0);
		String block_value = asString(levelDBStore.get(bytes(key)));
		if(block_value == null) {
			return null;
		}
		
		JSONParser parser = new JSONParser();
		try {
			JSONObject json = (JSONObject) parser.parse(block_value);
			JSONObject json_blockheader = (JSONObject)json.get("blockHeader");
			JSONArray jsontx = (JSONArray)json.get("transactions");
			
			BlockHeader blockHeader = new BlockHeader();
			blockHeader.convertClassObject(json_blockheader);
			
			Block block = new Block(((Number)json.get("blockSize")).intValue(), 
					((Number)json.get("transactionCount")).intValue(), (String)json.get("blockHash"));
			block.setBlockHeader(blockHeader);
			
			for(int i = 0; i < jsontx.size(); i++) {
				Transaction tx = new Transaction();
				tx.convertClassObject((JSONObject)jsontx.get(i));
				block.transactions.add(tx);
			}
			
			return block;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void deleteForkBlock(int index, int fork_index) {
		levelDBStore.delete(bytes("block"+String.valueOf(index)+"_"+String.valueOf(fork_index)));
	}
	
	public void closeDB() {
		try {
			levelDBStore.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
