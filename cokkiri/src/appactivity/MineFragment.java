package appactivity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import appview.MineView;
import blockchain.Block;
import coin.Coin;
import mining.Mining;
import wallet.Address;
import wallet.Wallet;

public class MineFragment {
	private MineView mineview;
	private JTextArea minearea;
	private JButton button_mine;
	private JTextField publickeytext;
	
	public MineFragment(MineView mineview) {
		this.mineview = mineview;
		minearea = mineview.getMinearea();
		button_mine = mineview.getButton_mine();
		publickeytext = mineview.getPublickeyText();
		setbuttonclick();
	}
	
	public void setbuttonclick() {
		button_mine.addActionListener(new MineClickListener());
	}
	

	private class MineClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			String button_text = b.getText();
			button_mine.setText("√§±º ¡ﬂ¡ˆ");

			if(button_text.equals("√§±º Ω√¿€")) {
				Address miner = new Address();
				miner.setAddress(publickeytext.getText());
				Block minedBlock = Mining.mining(miner);
				
				if(minedBlock==null) minearea.append("failed to mining\n"+"===========================================================\n");
				else {
					minearea.append(minedBlock.getString()+"===========================================================\n");
					Coin.blockchain.getUTXOs();
					System.out.println("^^check utxos after mining ");
					Wallet.printHashmap(Coin.blockchain.UTXOs);
				
				}
				
			}
			else {
				button_mine.setText("√§±º Ω√¿€");
			}
		}
	}
	
}
