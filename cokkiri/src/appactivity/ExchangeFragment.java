package appactivity;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import appview.ExchangeBuyView;
import appview.ExchangeSellView;
import appview.ExchangeView;

public class ExchangeFragment {
	private ExchangeView exchangeview;
	private JButton menuSellbutton;
	private JButton menuBuybutton;
	private JPanel card_panel;
	private CardLayout card;

	private ExchangeBuyFragment buyfragment;
	private ExchangeSellFragment sellfragment;
	
	public ExchangeFragment(ExchangeView exchangeview) {
		this.exchangeview = exchangeview;
		this.menuSellbutton = exchangeview.getMenuSellbutton();
		this.menuBuybutton = exchangeview.getMenuBuybutton();
		this.card_panel = exchangeview.getCard_panel();
		this.card = exchangeview.getCard();
		buttonColorChange(0);
		
		setContent();
		setbuttonclick();
	}
	
	private void setContent() {
		ExchangeSellView sell = new ExchangeSellView();
		ExchangeBuyView buy = new ExchangeBuyView();
		card_panel.add(sell, "sell");
		card_panel.add(buy, "buy");
		
		buyfragment = new ExchangeBuyFragment(buy);
		sellfragment = new ExchangeSellFragment(sell);
	}
	
	private void setbuttonclick() {
		menuSellbutton.addActionListener(new ExchangeMenuClickListener());
		menuBuybutton.addActionListener(new ExchangeMenuClickListener());
	}
	
	private void buttonColorChange(int index) {
		Color click = new Color(0xe8, 0xf5, 0xe9);
		Color notclick = new Color(0xfb, 0xfd, 0xfc);
		if(index == 0) {
			menuSellbutton.setBackground(click);
			menuBuybutton.setBackground(notclick);
		}
		else if(index == 1) {
			menuSellbutton.setBackground(notclick);
			menuBuybutton.setBackground(click);
		}
	}
	
	private class ExchangeMenuClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			String button_text = b.getText();
			if (button_text.equals(menuSellbutton.getText())) {
				buttonColorChange(0);
				card.show(card_panel, "sell");
			}
			else if(button_text.equals(menuBuybutton.getText())) {
				buttonColorChange(1);
				card.show(card_panel, "buy");
			}
		}
	}
}
