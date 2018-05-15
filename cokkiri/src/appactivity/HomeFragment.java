package appactivity;

import javax.swing.JLabel;

import appview.HomeView;

import coin.Coin;
public class HomeFragment {
	private HomeView homeview;
	private JLabel label_address_value;
	private JLabel label_totalcoin_value;
	private JLabel label_exchange_value;
	
	public HomeFragment(HomeView homeview) {
		this.homeview = homeview;
		this.label_address_value = homeview.getLabel_address_value();
		this.label_totalcoin_value = homeview.getLabel_totalcoin_value();
		this.label_exchange_value = homeview.getLabel_exchange_value();
	}
	
	public void refresh() {
		refreshTotalcoin();
	}
	private void refreshTotalcoin() {
		this.label_totalcoin_value.setText(String.valueOf(Coin.wallet.getBalance()));
	}

}
