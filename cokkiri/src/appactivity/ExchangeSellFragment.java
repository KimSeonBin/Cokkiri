package appactivity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import appview.ExchangeSellView;
import client.Client;
import coin.Coin;
import exchange.RequestSell;

public class ExchangeSellFragment {
	private static ExchangeSellView sellview;
	private JFormattedTextField coin_value;
	private JTextField coincash;
	private JButton sellbutton;
//	private JPasswordField passwordtext;
	
	public ExchangeSellFragment(ExchangeSellView sellview) {
		this.sellview=sellview;
		this.coin_value = sellview.getCoin_value();
		this.coincash = sellview.getCoin_value();
		this.sellbutton = sellview.getSellbutton();
	//	this.passwordtext = sellview.getPassword();
		
		sellbutton.addActionListener(new sellClickListener());
	}
	
	public static void showSuccessDialog() {
		JOptionPane.showMessageDialog(sellview, "코인 판매 성공하였습니다.", "코인 판매", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void showFailDialog() {
		JOptionPane.showMessageDialog(sellview, "코인 판매 실패하였습니다.", "코인 판매", JOptionPane.ERROR_MESSAGE);
	}
	
	public static String showInputPasswordDialog() {
		return JOptionPane.showInputDialog(sellview, "계정 비밀번호을 입력해주세요.", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private class sellClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
		
			String value_string=coin_value.getText();
			if(value_string.matches("^[0-9]+$") == false) {
				JOptionPane.showMessageDialog(sellview, "코인 양에 숫자만 입력 가능합니다.", "코인 입력", JOptionPane.ERROR_MESSAGE);
				return;
			}	
			

			Client.processSell(Float.parseFloat(value_string));
//			boolean res = Client.processSell(Float.parseFloat(value_string));
//			
//			if(res) {
//				
//				showSuccessDialog();
//			}else {
//				showFailDialog();
//			}
			flushText();

		}
		private void flushText() {
			coin_value.setText(null);
			coincash.setText(null);
		}
	}

}
