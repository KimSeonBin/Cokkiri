package blockchain;

import java.sql.Timestamp;
import java.util.Date;

import org.json.simple.JSONObject;

import hash.Sha256;

public class BlockHeader {

	private static int version=1;
	private String previousBlockHash;
	protected String merkleRootHash;
	protected long timestamp;
	protected int difficulty;
	protected long nonce;

	public BlockHeader(String previousBlockHash){
		this.setPreviousBlockHash(previousBlockHash);
	}

	public BlockHeader() { }
	public BlockHeader(long genesistime) { //for genesis block
		timestamp=genesistime;
		previousBlockHash="0";
	}

	public boolean printBlockHeader(){
		//System.out.println("Block version : "+version);
		String prevBlockHash;
		if(getPreviousBlockHash()==null) prevBlockHash="null";
		prevBlockHash=getPreviousBlockHash();
		System.out.println("Previous Block Hash : "+prevBlockHash);
		//System.out.println("Merkle Root Hash : "+merkleRootHash);
		System.out.println("Timestamp : "+new Date(timestamp));
		//System.out.println("Difficulty : "+difficulty);
		System.out.println("Nonce : "+nonce);
		return true;
	}
	
	public String getString(){ //block header 데이터를 string으로 리턴
		String blockHdStr = "previous block hash : ";
		//blockHdStr+=String.valueOf(version)+"\r\n";
		//if(getPreviousBlockHash()==null) blockHdStr+="null\r\n";
		if(getPreviousBlockHash()==null) blockHdStr+="null\r\n";
		//else blockHdStr+=getPreviousBlockHash()+"\r\n";
		//blockHdStr+=merkleRootHash+"\r\n"+new Date(timestamp)+"\r\n"+difficulty+"\r\n"+nonce+"\r\n";
		else blockHdStr+=getPreviousBlockHash()+"\r\n";
		blockHdStr+="time stamp : "+new Date(timestamp)+"\r\nnonce : "+nonce+"\r\n";
		return blockHdStr;
	}

	public String getPreviousBlockHash() {
		return previousBlockHash;
	}
	public void setMerkleRootHash(String merkleroot) {
		this.merkleRootHash=Sha256.hash(merkleroot);	
	}
	public void setPreviousBlockHash(String previousBlockHash) {
		this.previousBlockHash = previousBlockHash;
	}
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	public void setTimestamp() { //timestamp 값 설정 위한 메소드. 블록 생성(채굴)시 호출하면 될것으로 보임
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		this.timestamp=timestamp.getTime();	 
	}
	public long getTimestamp() {
		return timestamp;
	}
	
	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();
		json.put("version", version);
		json.put("previousBlockHash", previousBlockHash);
		json.put("merkleRootHash", merkleRootHash);
		json.put("timestamp", timestamp);
		json.put("difficulty", difficulty);
		json.put("nonce", nonce);
		return json;
	}
	
	public void convertClassObject(JSONObject json) {
		this.version = ((Number) json.get("version")).intValue();
		this.previousBlockHash = (String) json.get("previousBlockHash");
		this.merkleRootHash = (String) json.get("merkleRootHash");
		this.timestamp = (long) json.get("timestamp");
		this.difficulty = ((Number) json.get("difficulty")).intValue();
		this.nonce = (long) json.get("nonce");
	}
}
