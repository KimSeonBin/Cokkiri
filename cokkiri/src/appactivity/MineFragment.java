package appactivity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JTextArea;

import appview.MineView;
import blockchain.Block;
import coin.Coin;
import mining.Mining;
import wallet.Wallet;

public class MineFragment {
	private MineView mineview;
	private JTextArea minearea;
	private JButton button_mine;
	
	public MineFragment(MineView mineview) {
		this.mineview = mineview;
		minearea = mineview.getMinearea();
		button_mine = mineview.getButton_mine();
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
			button_mine.setText("ä�� ����");

			if(button_text.equals("ä�� ����")) {
				Block minedBlock = Mining.mining(Coin.wallet.getAddress());
				if(minedBlock==null) minearea.append("failed to mining\n"+"===========================================================\n");
				else {
					minearea.append(minedBlock.getString()+"===========================================================\n");
					Coin.blockchain.getUTXOs();
					System.out.println("^^check utxos after mining ");
					Wallet.printHashmap(Coin.blockchain.UTXOs);
				
				}
				
			}
			else {
				button_mine.setText("ä�� ����");
			}
		}
	}
	
}
