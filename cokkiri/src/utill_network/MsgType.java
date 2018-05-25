package utill_network;

public class MsgType {

	public final static String TRANSACTION_MSG="send Transaction";
	public final static String BLOCK_TRANSFER_MSG="send Block";
	public final static String EXCHANGE_MSG = "request exchange";
	public final static String PREEXISTNODE_MSG = "I know you";
	public final static String NEWNODE_MSG = "I don't know you";
	public final static String DNS_MSG = "dnsserver";
	public final static String GETDATA_MSG = "getdata";
	public final static String REQUEST_SELL= "cash_amount";
	public final static String REQUEST_PURCHASE = "coin_amount";
	public final static String ANSWER_OK = "OK";
	public final static String ANSWER_NO = "NO";
	public static final String MYTX_REQ_MSG = "request my tx";
	public static final String FULLBLOCK_REQ_MSG = "request full block";
}
