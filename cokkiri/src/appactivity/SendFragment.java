package appactivity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import coin.Coin;
import coin.Constant;
import mining.Mining;
import appview.SendView;
import client.Client;
import transaction.Transaction;
import transaction.CreateTransaction;
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
				JOptionPane.showMessageDialog(sendview, "���� �翡 ���ڸ� �Է� �����մϴ�.", "���� �Է�", JOptionPane.ERROR_MESSAGE);
				return;
			}
			float value=Float.valueOf(value_string);
			String password=String.valueOf(passwordtext.getPassword());
			
			if(!checksendinputs(receiver, password)) {
				String message = receiver + "���� �ŷ� ������ �����߽��ϴ�.";
				JOptionPane.showMessageDialog(sendview, message, "�Է°� ����", JOptionPane.WARNING_MESSAGE);
				return;
			}
			Wallet sender = new Wallet(Coin.id+password, false);
			
			Address receiverAdd=new Address();
			receiverAdd.setAddress(receiver);
			Transaction t=CreateTransaction.createTx(sender, receiverAdd, value);
			
			if(t!=null) {
				//-------------tx����---------------//
				//System.out.println("[ClientSendlog] : BroadCast Transaction");
				new Thread() {
					public void run() {
						try {
							Client.broadcastToAdmin(MsgType.TRANSACTION_MSG+t.toJSONObject());
							Client.broadcastToPC(MsgType.TRANSACTION_MSG+t.toJSONObject());
							Client.broadcastToAndorid(MsgType.TRANSACTION_MSG+t.toJSONObject());
						} catch (Exception e) {}
					}
				}.start();
				//--------------------------------//
				
				Mining.transactionPool.add(t);
				//log.Logging.consoleLog("**transaction created** : "+t.getString());
				String message = receiver + "���� �ŷ� ������ �����߽��ϴ�.";
				JOptionPane.showMessageDialog(sendview, message, "�ŷ� ����", JOptionPane.INFORMATION_MESSAGE);
				flushText();
			}else {
				String message = receiver + "���� �ŷ� ������ �����߽��ϴ�.";
				JOptionPane.showMessageDialog(sendview, message, "�ŷ� ����", JOptionPane.WARNING_MESSAGE);
				//log.Logging.consoleLog("failed to create transaction");
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
