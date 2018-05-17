package appactivity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import appview.ExchangeSellView;

public class ExchangeSellFragment {

	private JFormattedTextField coin_valuetext;
	private JTextField coincash;
	private JButton sellbutton;
	private JPasswordField passwordtext;
	
	public ExchangeSellFragment(ExchangeSellView sellview) {
		this.coin_valuetext = sellview.getCoin_value();
		this.coincash = sellview.getCoin_value();
		this.sellbutton = sellview.getSellbutton();
		this.passwordtext = sellview.getPassword();
	}
	
	public void setbuttonclick() {
		sellbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				float value=Float.valueOf(coin_valuetext.getText());
				String password=String.valueOf(passwordtext.getPassword());
				
				
				
				
			}
		});
	}

}
