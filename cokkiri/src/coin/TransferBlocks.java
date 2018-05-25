
package coin;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import blockchain.Block;

public class TransferBlocks {
	private ArrayList<Block> blocks;
	
	private long startIndex, endIndex;
	
	public void setIndex(long start, long end) {
		startIndex=start;  //가지고 있는 블록 중 가장 큰 index +1 (요청 블록의 첫번째 index)
		endIndex=end;   //-1이면 끝까지
	}
	
	public JSONObject toReqJSON() {
		JSONObject json = new JSONObject();
		json.put("startIndex", startIndex);
		json.put("endIndex", endIndex);
		return json;
	}
	
	public void convertReqObject(JSONObject json) {
		startIndex=((Number)json.get("startIndex")).longValue();
		endIndex=((Number)json.get("endIndex")).longValue();
	}
	
	public void setBlock() {
		ArrayList<Block> allblocks = new ArrayList<Block>();
		allblocks.addAll(Coin.blockchain.blockchain);
		
		System.out.println("setBlock()\r\ncheck index : "+String.valueOf(allblocks.size()-1));
		System.out.println("                       ; "+String.valueOf(allblocks.get(allblocks.size()-1).getBlockIndex()));
		
		if(endIndex==-1) endIndex = allblocks.get(allblocks.size()-1).getBlockIndex();
		this.blocks=new ArrayList<Block>();
		
		for(int i=(int) startIndex;i<=endIndex;i++) {
			this.blocks.add(allblocks.get(i));
		}
	}
	
	public JSONObject toResJSON() {
		JSONObject json = new JSONObject();
		
		json.put("startIndex", startIndex);
		json.put("endIndex", endIndex);
		
		JSONArray array = new JSONArray();
		if (blocks == null) array = null;
		for(Block block : this.blocks) {
			array.add(block.toJSONObject());
		}
		json.put("blocks", array);
		return json;
	}
	
	public void convertResObject(JSONObject json) {
		startIndex=((Number)json.get("startIndex")).longValue();
		endIndex=((Number)json.get("endIndex")).longValue();
		
		blocks.clear();
		JSONArray jsonArray = (JSONArray) json.get("blocks");
		JSONObject temp;
		
		for(int i = 0; i < jsonArray.size(); i++) {
			Block block = new Block();
			block.convertClassObject((JSONObject) jsonArray.get(i));
			this.blocks.add(block);
		}
	}
	
	public boolean check() {
		if(blocks.get(0).getBlockIndex() != startIndex) {
			return false;
		}
		if(blocks.get(blocks.size()-1).getBlockIndex()!=endIndex) {
			return false;
		}
		if((endIndex-startIndex+1)!= blocks.size()){
			return false;
		}
		
		return true;
		
	}
	public long getStartIndex() {
		return startIndex;
		
	}
}
