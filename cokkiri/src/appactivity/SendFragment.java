package appactivity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import coin.Coin;
import mining.Mining;
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
			
			//receiver, value, password 검증 내용//
			if(!checksendinputs(receiver, password)) {
				String message = receiver + "와의 거래 생성을 실패했습니다.";
	            JOptionPane.showMessageDialog(sendview, message, "거래 생성", JOptionPane.WARNING_MESSAGE);
				log.Logging.consoleLog("failed to create transaction");
				return;
			}
			
			Wallet sender = new Wallet(Coin.id+password, false);
			Address receiverAdd=new Address();
			receiverAdd.setAddress(receiver);
			Transaction t=createTransaction.createTx(sender, receiverAdd, value);
			
			if(t!=null) {
				//-------------tx전파---------------//
				System.out.println("[ClientSendlog] : BroadCast Transaction");
				String message = receiver + "와의 거래 생성을 성공했습니다.";
	            JOptionPane.showMessageDialog(sendview, message, "거래 생성", JOptionPane.INFORMATION_MESSAGE);

				new Thread() {
					public void run() {
						try {
							Client.broadcast(MsgType.TRANSACTION_MSG+t.toJSONObject());
						} catch (Exception e) {}
					}
				}.start();
				//--------------------------------//
				
				Mining.transactionPool.add(t);
				log.Logging.consoleLog("**transaction created** : "+t.getString());
			}else {
				String message = receiver + "와의 거래 생성을 실패했습니다.";
	            JOptionPane.showMessageDialog(sendview, message, "거래 생성", JOptionPane.WARNING_MESSAGE);

				log.Logging.consoleLog("failed to create transaction");
			}
			
		}
	}
	
	private boolean checksendinputs(String receiver, String password) {
		if(receiver.length()!=28) {
			//System.out.println("*1*");
			return false;
		}
		if(Coin.wallet.authenticate(Coin.id+password)!=1) { //비밀 번호 입력 실패
			//System.out.println("*2*");
			return false;
		}
		return true;
	}
}
