package appactivity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import coin.Coin;
import appview.SendView;
import client.Client;
import transaction.Transaction;
import transaction.createTransaction;
import utill_network.MsgType;
import wallet.Address;
import wallet.Wallet;

public class SendFragment {
	private SendView sendview;
	private JTextField publickeytext;
	private JTextField coin_valuetext;
	private JPasswordField passwordtext;
	private JButton sendbutton;
	
	private JButton favoritebutton;
	
	public SendFragment(SendView sendview) {
		this.sendview = sendview;
		publickeytext = sendview.getPublickey();
		coin_valuetext = sendview.getCoin_value();
		passwordtext = sendview.getPassword();
		sendbutton = sendview.getSendButton();
		
		favoritebutton = sendview.getFavoriteButton();
		setbuttonclick();
	}
	public void setbuttonclick() {
		sendbutton.addActionListener(new sendClickListener());
		favoritebutton.addActionListener(new favoriteClickListener());
	}
	private class sendClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
		

			
			String receiver=publickeytext.getText();
			String value_string = coin_valuetext.getText();
			if(value_string.matches("^[0-9]+$") == false) {
				JOptionPane.showMessageDialog(sendview, "코인 양에 숫자만 입력 가능합니다.", "코인 입력", JOptionPane.ERROR_MESSAGE);
				return;
			}
			float value=Float.valueOf(value_string);
			String password=String.valueOf(passwordtext.getPassword());
			
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
				JOptionPane.showMessageDialog(sendview, message, "거래 생성", JOptionPane.INFORMATION_MESSAGE);
				flushText();
			}else {
				String message = receiver + "와의 거래 생성을 실패했습니다.";
				JOptionPane.showMessageDialog(sendview, message, "거래 생성", JOptionPane.WARNING_MESSAGE);
				log.Logging.consoleLog("failed to create transaction");
			}			
		}
	}
	
	private class favoriteClickListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			FavoriteActivity favority = new FavoriteActivity();
			System.out.println("click");
		}
		
	}
	
	private void flushText() {
		publickeytext.setText(null);
//		coin_valuetext.setValue(null);
		coin_valuetext.setText(null);
		passwordtext.setText(null);
	}
	
	private boolean checksendinputs(String receiver, String password) {
		if(receiver.length()!=28) {
			return false;
	    }
	    if(Coin.wallet.authenticate(Coin.id+password)!=1) {
	    
	    	return false;
	    }
	 
	    return true;
	}
}
