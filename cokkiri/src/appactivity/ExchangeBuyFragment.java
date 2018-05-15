package appactivity;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

import appview.ExchangeBuyView;

public class ExchangeBuyFragment {
	private JFormattedTextField coin_value;
	private JTextField coincash;
	private JButton buybutton;
	
	public ExchangeBuyFragment(ExchangeBuyView buyview) {
		this.coin_value = buyview.getCoin_value();
		this.coincash = buyview.getCoin_value();
		this.buybutton = buyview.getBuybutton();
	}

}
