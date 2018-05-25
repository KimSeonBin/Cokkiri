package appactivity;

import javax.swing.JLabel;

import appview.HomeView;
import client.Client;
import coin.Cash;
import coin.Coin;
import utill_network.PeerList;
public class HomeFragment {
	private HomeView homeview;
	private JLabel label_address_value;
	private JLabel label_totalcoin_value;
	private JLabel label_exchange_value;
	
	public HomeFragment(HomeView homeview) {

<<<<<<< HEAD
=======
		//Client.sendMsg("hello", PeerList.getPeerList().get(0));
		//Client.sendMsg("hello", PeerList.getPeerList().get(1));
>>>>>>> branch 'master' of https://github.com/KimSeonBin/Cokkiri.git
		this.homeview = homeview;
		this.label_address_value = homeview.getLabel_address_value();
		this.label_totalcoin_value = homeview.getLabel_totalcoin_value();
		this.label_exchange_value = homeview.getLabel_exchange_value();
	}
	
	public void refresh() {
		refreshTotalcoin();
		refreshExchange();
	}
	private void refreshExchange() {
		this.label_exchange_value.setText(String.valueOf(Cash.getCash()));
	}

	private void refreshTotalcoin() {
		this.label_totalcoin_value.setText(String.valueOf(Coin.wallet.getBalance()));
	}

}
