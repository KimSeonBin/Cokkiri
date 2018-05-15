package appactivity;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import appview.ExchangeView;
import appview.HistoryView;
import appview.HomeView;
import appview.MainView;
import appview.MineView;
import appview.SendView;
import coin.Coin;


public class MainActivity {
	private MainView mainview;
	private JPanel card_panel;
	private CardLayout card;
	private JButton button_home;
	private JButton button_history;
	private JButton button_send;
	private JButton button_exchange;
	private JButton button_mine;
	
	private HomeFragment homefragment;
	private HistoryFragment historyfragment;
	private ExchangeFragment exchangefragment;
	private SendFragment sendfragment;
	private MineFragment minefragment;
	
	public MainActivity() {
	}

	public void start() {
		mainview = new MainView();
		card_panel = mainview.getCard_panel();
		card = mainview.getCard();
		button_home = mainview.getButton_home();
		button_history = mainview.getButton_history();
		button_send = mainview.getButton_send();
		button_exchange = mainview.getButton_exchange();
		button_mine = mainview.getButton_mine();
		setContent();
		setbuttonClick();
	}
	
	private void setContent() {
		HomeView fragment_home = new HomeView();
		HistoryView fragment_history = new HistoryView();
		SendView fragment_send = new SendView();
		ExchangeView fragment_exchange = new ExchangeView();
		MineView fragment_mine = new MineView();
		card_panel.add(fragment_home, "home");
		card_panel.add(fragment_history, "history");
		card_panel.add(fragment_send, "send");
		card_panel.add(fragment_exchange, "exchange");
		card_panel.add(fragment_mine, "mine");
		
		homefragment = new HomeFragment(fragment_home);
		historyfragment = new HistoryFragment(fragment_history);
		sendfragment = new SendFragment(fragment_send);
		exchangefragment = new ExchangeFragment(fragment_exchange);
		minefragment = new MineFragment(fragment_mine);
	}
	
	private void setbuttonClick() {
		button_home.addActionListener(new MenuClickListener());
		button_history.addActionListener(new MenuClickListener());
		button_send.addActionListener(new MenuClickListener());
		button_exchange.addActionListener(new MenuClickListener());
		button_mine.addActionListener(new MenuClickListener());
	}
	
	private class MenuClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			String button_text = b.getText();
			if (button_text.equals(button_home.getText())) {
				buttonColorChange(0);				
				Coin.blockchain.getUTXOs();
				homefragment.refresh();
				
				card.show(card_panel, "home");
			}
			else if(button_text.equals(button_history.getText())) {
				buttonColorChange(1);
				//AjouCoin.blockchain.getUTXOs();
				historyfragment.refresh();   //
				card.show(card_panel, "history");
			}
			else if(button_text.equals(button_send.getText())) {
				buttonColorChange(2);
				card.show(card_panel, "send");
			}
			else if(button_text.equals(button_exchange.getText())) {
				buttonColorChange(3);
				card.show(card_panel, "exchange");
			}
			else if(button_text.equals(button_mine.getText())) {
				buttonColorChange(4);
				card.show(card_panel, "mine");
			}
		}
	}
	
	private void buttonColorChange(int index) {
		Color click = new Color(0xe8, 0xf5, 0xe9);
		Color notclick = new Color(0xfb, 0xfd, 0xfc);
		if(index == 0) {
			button_home.setBackground(click);
			button_history.setBackground(notclick);
			button_send.setBackground(notclick);
			button_exchange.setBackground(notclick);
			button_mine.setBackground(notclick);
		}
		else if(index == 1) {
			button_home.setBackground(notclick);
			button_history.setBackground(click);
			button_send.setBackground(notclick);
			button_exchange.setBackground(notclick);
			button_mine.setBackground(notclick);
		}
		else if(index == 2) {
			button_home.setBackground(notclick);
			button_history.setBackground(notclick);
			button_send.setBackground(click);
			button_exchange.setBackground(notclick);
			button_mine.setBackground(notclick);
		}
		else if(index == 3) {
			button_home.setBackground(notclick);
			button_history.setBackground(notclick);
			button_send.setBackground(notclick);
			button_exchange.setBackground(click);
			button_mine.setBackground(notclick);
		}
		else if(index == 4) {
			button_home.setBackground(notclick);
			button_history.setBackground(notclick);
			button_send.setBackground(notclick);
			button_exchange.setBackground(notclick);
			button_mine.setBackground(click);
		}
	}

}
