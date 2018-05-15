package appactivity;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

import appview.ExchangeSellView;

public class ExchangeSellFragment {

	private JFormattedTextField coin_value;
	private JTextField coincash;
	private JButton sellbutton;
	
	public ExchangeSellFragment(ExchangeSellView sellview) {
		this.coin_value = sellview.getCoin_value();
		this.coincash = sellview.getCoin_value();
		this.sellbutton = sellview.getSellbutton();
	}

}
