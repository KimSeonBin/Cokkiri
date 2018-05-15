package appview;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainView extends JFrame{
	private JPanel panel;
	
	private int menuCount = 5;
	
	private CardLayout card;
	private JPanel card_panel;
	
	private JButton button_home;
	private JButton button_history;
	private JButton button_send;
	private JButton button_exchange;
	private JButton button_mine;
	
	public MainView() {
		super();
		initComponent();
	}
	
	private void initComponent() {
		this.setVisible(true);
		this.setSize(800, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension frameSize = this.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		
		setEntireLayout();
		
		this.setContentPane(panel);
		this.validate();
	}
	
	private void setEntireLayout() {
		FlowLayout flow = new FlowLayout();
		flow.setVgap(0);
		panel = new JPanel(flow);
		panel.setBackground(new Color(0xff, 0xfd, 0xe7));
		
		JPanel menuPanel = new JPanel(new GridBagLayout());
		setMenu(menuPanel);
		panel.add(menuPanel);
		
		setContent();
	}
	
	private void setMenu(JPanel panel) {
		GridBagConstraints[] gbc = new GridBagConstraints[menuCount];
		
		gbc[0] = new GridBagConstraints(0, 0, 1, 1, 0.2, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		button_home = new JButton();
		button_home.setOpaque(true);
		button_home.setPreferredSize(new Dimension(200, 50));
		try {
			Image img = ImageIO.read(getClass().getResource("/img/ic_home_black.png"));
			button_home.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			e.printStackTrace();
		}
		panel.add(button_home, gbc[0]);

		gbc[1] = new GridBagConstraints(1, 0, 1, 1, 0.2, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		button_history = new JButton("거래 내역");
		button_history.setOpaque(true);
		button_history.setPreferredSize(new Dimension(150, 50));
		panel.add(button_history, gbc[1]);
		
		gbc[2] = new GridBagConstraints(2, 0, 1, 1, 0.2, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		button_send = new JButton("송금");
		button_send.setOpaque(true);
		button_send.setPreferredSize(new Dimension(150, 50));
		panel.add(button_send, gbc[2]);
		
		gbc[3] = new GridBagConstraints(3, 0, 1, 1, 0.2, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		button_exchange = new JButton("거래소");
		button_exchange.setOpaque(true);
		button_exchange.setPreferredSize(new Dimension(150, 50));
		panel.add(button_exchange, gbc[3]);
		
		gbc[4] = new GridBagConstraints(4, 0, 1, 1, 0.2, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		button_mine = new JButton("채굴");
		button_mine.setOpaque(true);
		button_mine.setPreferredSize(new Dimension(150, 50));
		panel.add(button_mine, gbc[4]);
	}
	
	private void setContent() {
		card_panel = new JPanel();
		card = new CardLayout();
		card_panel.setLayout(card);
		panel.add(card_panel);
	}
		
	public JPanel getCard_panel() {
		return card_panel;
	}

	public CardLayout getCard() {
		return card;
	}
	
	public JButton getButton_home() {
		return button_home;
	}

	public JButton getButton_history() {
		return button_history;
	}

	public JButton getButton_send() {
		return button_send;
	}

	public JButton getButton_exchange() {
		return button_exchange;
	}

	public JButton getButton_mine() {
		return button_mine;
	}
}
