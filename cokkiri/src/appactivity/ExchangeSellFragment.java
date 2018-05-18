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
	private ExchangeSellView sellview;
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
	private class sellClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
		
			String value_string=coin_value.getText();
			if(value_string.matches("^[0-9]+$") == false) {
				JOptionPane.showMessageDialog(sellview, "���� �翡 ���ڸ� �Է� �����մϴ�.", "���� �Է�", JOptionPane.ERROR_MESSAGE);
				return;
			}	
			

			boolean res = Client.processSell(Float.parseFloat(value_string));
			
			if(res) {
				
				JOptionPane.showMessageDialog(sellview, "���� �Ǹ� �����Ͽ����ϴ�.", "���� �Ǹ�", JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(sellview, "���� �Ǹ� �����Ͽ����ϴ�.", "���� �Ǹ�", JOptionPane.ERROR_MESSAGE);
			}
			flushText();

		}
		private void flushText() {
			coin_value.setText(null);
			coincash.setText(null);
		}
	}

}
