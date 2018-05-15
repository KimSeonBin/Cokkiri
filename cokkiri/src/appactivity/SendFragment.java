package appactivity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import coin.Coin;
import appview.SendView;
import client.Client;
import transaction.Transaction;
import transaction.createTransaction;
import utill_network.MsgType;
import utill_network.Peer;
import wallet.Address;
import wallet.Wallet;

public class SendFragment {
	private SendView sendview;
	private JTextField publickeytext;
	private JFormattedTextField coin_valuetext;
	private JPasswordField passwordtext;
	private JButton sendbutton;
	
	public SendFragment(SendView sendview) {
		this.sendview = sendview;
		publickeytext = sendview.getPublickey();
		coin_valuetext = sendview.getCoin_value();
		passwordtext = sendview.getPassword();
		sendbutton = sendview.getSendButton();
		setbuttonclick();
	}
	public void setbuttonclick() {
		sendbutton.addActionListener(new sendClickListener());
	}
	private class sendClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
		

			
			String receiver=publickeytext.getText();
			float value=Float.valueOf(coin_valuetext.getText());
			String password=String.valueOf(passwordtext.getPassword());
			
			

			//receiver, value, password 검증 내용
			
			Coin.wallet = new Wallet(Coin.id+password, false);
			
			Address receiverAdd=new Address();
			receiverAdd.setAddress(receiver);
			Transaction t=createTransaction.createTx(Coin.wallet, receiverAdd, value);
			
			if(t!=null) {
				//-------------tx전파---------------//
				System.out.println("[ClientSendlog] : BroadCast Transaction");
				
				new Thread() {
					public void run() {
						try {
							Client.broadcast(MsgType.TRANSACTION_MSG+t.toJSONObject());
						} catch (Exception e) {}
					}
				}.start();
				//--------------------------------//
				//Coin.blockchain.transactionPool.put(t.TxId, t); //hashmap 사용할 때
				
				Coin.blockchain.transactionPool.add(t);
				log.Logging.consoleLog("**transaction created** : "+t.getString());
			}else {
				log.Logging.consoleLog("failed to create transaction");
			}

			//wallet private key 제거 과정 필요
			
			//////////////////////////////////////////////////////////////////////
		}
	}
}
