package appview;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ExchangeView extends JPanel {
	private JButton menuSellbutton;
	private JButton menuBuybutton;
	
	private JPanel card_panel;
	private CardLayout card;
	
	private int width_size = 800;
	private int height_size = 700;
	private int left_margin = 20;
	private int right_margin = 20;
	
	public ExchangeView() {
		super();
		this.setBackground(Color.white);
		this.setLayout(new GridBagLayout());
		initComponent();
		setContent();
	}

	private void initComponent() {
		menuComponent();
	}

	private void menuComponent() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));
		
		menuSellbutton = new JButton("판매");
		menuSellbutton.setFont(new Font("Serif", Font.BOLD, 15));
		panel.add(menuSellbutton);
		
		menuBuybutton = new JButton("구매");
		menuBuybutton.setFont(new Font("Serif", Font.BOLD, 15));
		panel.add(menuBuybutton);
		
		GridBagConstraints panelgb = new GridBagConstraints(0, 0, 1, 1, 1, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		this.add(panel, panelgb);
	}
	
	private void setContent() {
		card = new CardLayout();
		card_panel = new JPanel(card);
		card_panel.setOpaque(false);
		
		GridBagConstraints panelgb = new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		this.add(card_panel, panelgb);
	}

	public JButton getMenuSellbutton() {
		return menuSellbutton;
	}

	public JButton getMenuBuybutton() {
		return menuBuybutton;
	}

	public JPanel getCard_panel() {
		return card_panel;
	}

	public CardLayout getCard() {
		return card;
	}
}
