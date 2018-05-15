package appview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import coin.Coin;
import blockchain.History;


public class HistoryView extends JPanel {
	private JTextArea sendtextarea;
	private JTextArea receivetextarea;
	
	private int width_size = 800;
	private int height_size = 700;
	private int left_margin = 20;
	private int right_margin = 20;
	
	public HistoryView() {
		super();
		initComponent();		
	}

	private void initComponent() {
		this.setPreferredSize(new Dimension(800, 700));
		this.setBorder(BorderFactory.createEmptyBorder(10, left_margin, 0, right_margin));
		this.setBackground(Color.white);
		width_size -= (left_margin + right_margin);
		
		this.setLayout(new GridBagLayout());
		
		sendHistoryComponent();
		blankArea(1, 0, 0.1, 0);
		receiveHistoryComponent();
	}
	
	private void sendHistoryComponent() {
		JPanel sendPanel = new JPanel(new BorderLayout());
		sendPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
		sendPanel.setOpaque(false);
		
		JLabel sendSentence = new JLabel("송금 내역");
		sendSentence.setFont(new Font("Serif", Font.BOLD, 20));
		sendPanel.add(sendSentence, BorderLayout.NORTH);
		
		sendtextarea = new JTextArea(5, 10);
		sendtextarea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(sendtextarea);
		sendPanel.add(scrollPane, BorderLayout.CENTER);
		
		GridBagConstraints panelgb = new GridBagConstraints(0, 0, 1, 1, 0.5, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		this.add(sendPanel, panelgb);
	}
	
	private void receiveHistoryComponent() {
		JPanel receivePanel = new JPanel(new BorderLayout());
		receivePanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
		receivePanel.setOpaque(false);
		
		JLabel receiveSentence = new JLabel("수신 내역");
		receiveSentence.setFont(new Font("Serif", Font.BOLD, 20));
		receivePanel.add(receiveSentence, BorderLayout.NORTH);
		
		receivetextarea = new JTextArea(5, 10);
		receivetextarea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(receivetextarea);
		receivePanel.add(scrollPane, BorderLayout.CENTER);
		
		GridBagConstraints panelgb = new GridBagConstraints(2, 0, 1, 1, 0.5, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		this.add(receivePanel, panelgb);
	}
	
	private void blankArea(int x, int y, double weightx, double weighty) {
		JPanel blankPanel = new JPanel();
		blankPanel.setOpaque(false);
		JLabel blank = new JLabel();
		
		blankPanel.add(blank);
		
		GridBagConstraints panelgb = new GridBagConstraints();
		panelgb.gridx = x;
		panelgb.gridy = y;
		panelgb.weightx = weightx;
		panelgb.weighty = weighty;
		this.add(blankPanel, panelgb);
	}

	public JTextArea getSendtextarea() {
		sendtextarea.setText("sended... (receiver, value)\n");
		History.getHistory(Coin.wallet.getAddress());
		String tmp = History.stringSended(Coin.wallet.getAddress());
		log.Logging.consoleLog("HistoryView - getSendtextarea()\n"+tmp);
		if(tmp!="null") sendtextarea.append(tmp);
		
		return sendtextarea;
	}

	public JTextArea getReceivetextarea() {
		receivetextarea.setText("received... (sender, value)\n");
		History.getHistory(Coin.wallet.getAddress());
		String tmp = History.stringReceived(Coin.wallet.getAddress());
		log.Logging.consoleLog("HistoryView - getReceivetextarea()\n"+tmp);

		if(tmp!="null") receivetextarea.append(tmp);

		return receivetextarea;
	}
}