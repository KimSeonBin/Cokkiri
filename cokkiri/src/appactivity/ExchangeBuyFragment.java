package appactivity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import appview.ExchangeBuyView;
import appview.SendView;
import client.Client;
import coin.Cash;
import coin.Coin;
import coin.Constant;
import exchange.RequestBuy;
import transaction.Transaction;
import transaction.CreateTransaction;
import utill_network.MsgType;
import wallet.Address;
import wallet.Wallet;

public class ExchangeBuyFragment {
	private static ExchangeBuyView buyview;
	private JFormattedTextField coin_value;
	private JTextField coincash;
	private JButton buybutton;
	
	public ExchangeBuyFragment(ExchangeBuyView buyview) {
		this.buyview=buyview;
		this.coin_value = buyview.getCoin_value();
		this.coincash = buyview.getCoin_value();
		this.buybutton = buyview.getBuybutton();

		buybutton.addActionListener(new buyClickListener());
	}
	public static void showSuccessDialog() {
		JOptionPane.showMessageDialog(buyview, "코인 구매 성공하였습니다.", "코인 구매", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void showFailDialog() {
		JOptionPane.showMessageDialog(buyview, "코인 구매 실패하였습니다.", "코인 구매", JOptionPane.ERROR_MESSAGE);
	}
	
	private class buyClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
		
			String value_string=coin_value.getText();
			if(value_string.matches("^[0-9]+$") == false) {
				JOptionPane.showMessageDialog(buyview, "코인 양에 숫자만 입력 가능합니다.", "코인 입력", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if(Float.parseFloat(value_string)*Constant.compasionValue > Cash.getCash()) {
				JOptionPane.showMessageDialog(buyview, "잔고가 부족합니다.", "코인 구매", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			boolean res =Client.processBuy(Float.parseFloat(value_string));

			flushText();
			
		}
	}
	private void flushText() {
		coin_value.setText(null);
		coincash.setText(null);
	}
}
