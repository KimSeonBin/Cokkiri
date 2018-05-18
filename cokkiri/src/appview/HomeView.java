package appview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import coin.Cash;
import coin.Coin;

/*
 * http://kamang-it.tistory.com/118
 * gridbaglayout 사용 및 다양한 layout 소개
 */
public class HomeView extends JPanel{
	private int width_size = 800;
	private int left_margin = 20;
	private int right_margin = 20;
	
	private JLabel label_address_value;
	private JLabel label_totalcoin_value;
	private JLabel label_exchange_value;
	
	public HomeView() {
		super();
		//this.setPreferredSize(new Dimension(800, 700));
		this.setBorder(BorderFactory.createEmptyBorder(10, left_margin, 0, right_margin));
		this.setBackground(Color.white);
		width_size -= (left_margin + right_margin);
		
		GridBagLayout gblayout = new GridBagLayout();
		this.setLayout(gblayout);
		initComponent();
	}
	
	private void initComponent() {
		addPanelAddress();
		addPanelCoin();
		addPanelExchange();
		addBlank();
	}

	private void addPanelAddress() {
		JLabel label_address = new JLabel("주소");
		System.out.println("*****"+Coin.wallet.getAddress().getString());
		label_address_value = new JLabel(Coin.wallet.getAddress().getString());
		label_address.setPreferredSize(new Dimension(100, 50));
		
		JPanel panel = new JPanel();
				
		panel.setBackground(Color.white);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.setPreferredSize(new Dimension(width_size, 50));
		panel.add(label_address);
		panel.add(label_address_value);
		
		GridBagConstraints panelGBC = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 10);
		this.add(panel, panelGBC);
	}
	

	private void addPanelCoin() {
		JLabel label_totalcoin = new JLabel("자산");
		Coin.blockchain.getUTXOs();
		label_totalcoin_value = new JLabel(String.valueOf(Coin.wallet.getBalance()));
		label_totalcoin.setPreferredSize(new Dimension(100, 50));
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(width_size, 50));
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.add(label_totalcoin);
		panel.add(label_totalcoin_value);
		
		GridBagConstraints panelGBC = new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		this.add(panel, panelGBC);
	}
	
	private void addPanelExchange() {
		JLabel label_exchange = new JLabel("현금자산");
		label_exchange_value = new JLabel(String.valueOf(Cash.getCash()));
		label_exchange.setPreferredSize(new Dimension(100, 50));
		label_exchange_value.setSize(new Dimension(200, 100));
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(width_size, 50));
		panel.setOpaque(false);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.add(label_exchange);
		panel.add(label_exchange_value);

		GridBagConstraints panelGBC = new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		this.add(panel, panelGBC);
	}
	

	private void addBlank() {
		JLabel labelBlank = new JLabel();
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(width_size, 50));
		panel.setOpaque(false);
		panel.add(labelBlank);
		
		GridBagConstraints panelGBC = new GridBagConstraints(0, 3, 1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		this.add(panel, panelGBC);
		
	}

	public JLabel getLabel_address_value() {
		return label_address_value;
	}

	public JLabel getLabel_totalcoin_value() {
		return label_totalcoin_value;
	}

	public JLabel getLabel_exchange_value() {
		return label_exchange_value;
	}
}
