package appview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

public class SendView extends JPanel {
	private JTextField publickey;
	private JFormattedTextField coin_value;
	private JPasswordField password;
	private JButton sendButton;
	
	private int width_size = 800;
	private int height_size = 700;
	private int left_margin = 20;
	private int right_margin = 20;
	
	public SendView() {
		super();
		initComponent();
	}

	private void initComponent() {
		this.setPreferredSize(new Dimension(width_size, height_size));
		this.setBorder(BorderFactory.createEmptyBorder(10, left_margin, 0, right_margin));
		this.setBackground(Color.white);
		width_size -= (left_margin + right_margin);
		this.setLayout(new GridBagLayout());
		
		publickeyComponent();
		coin_valueComponent();
		passwordComponent();
		sendButtonComponent();
		blankareaComponent(0, 4);
	}

	private void publickeyComponent() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));
		
		JLabel label = new JLabel("송금 계좌");
		label.setFont(new Font("Serif", Font.BOLD, 20));
		GridBagConstraints labelgb = new GridBagConstraints();
		labelgb.gridx = 0;
		labelgb.gridy = 0;
		labelgb.ipady = 20;
		panel.add(label, labelgb);
		
		JLabel labelblank = new JLabel();
		GridBagConstraints labelblankgb = new GridBagConstraints();
		labelblankgb.gridx = 1;
		labelblankgb.gridy = 0;
		labelblankgb.weightx = 1;
		panel.add(labelblank, labelblankgb);
		
		publickey = new JTextField();
		publickey.setMinimumSize(new Dimension(40, 30));
		publickey.setPreferredSize(new Dimension(40, 40));
		GridBagConstraints keygb = new GridBagConstraints();
		keygb.gridx = 0;
		keygb.gridy = 1;
		keygb.gridwidth = 2;
		keygb.weightx = 1;
		keygb.fill = GridBagConstraints.BOTH;
		panel.add(publickey, keygb);
		
		GridBagConstraints panelgb = new GridBagConstraints();
		panelgb.gridx = 0;
		panelgb.gridy = 0;
		panelgb.weightx = 1;
		panelgb.fill = GridBagConstraints.BOTH;
		this.add(panel, panelgb);
	}

	private void coin_valueComponent() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
		
		JLabel label = new JLabel("코인양");
		label.setFont(new Font("Serif", Font.BOLD, 20));
		GridBagConstraints labelgb = new GridBagConstraints();
		labelgb.gridx = 0;
		labelgb.gridy = 0;
		labelgb.ipady = 20;
		panel.add(label, labelgb);
		
		JLabel labelblank = new JLabel();
		GridBagConstraints labelblankgb = new GridBagConstraints();
		labelblankgb.gridx = 1;
		labelblankgb.gridy = 0;
		labelblankgb.weightx = 1;
		panel.add(labelblank, labelblankgb);
		
		//coin_value = new JTextField();
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Double.class);
		formatter.setMaximum(0);
		formatter.setMaximum(Double.MAX_VALUE);
		formatter.setAllowsInvalid(false);
		coin_value = new JFormattedTextField(formatter);
		coin_value.setPreferredSize(new Dimension(40, 40));
		GridBagConstraints coingb = new GridBagConstraints();
		coingb.gridx = 0;
		coingb.gridy = 1;
		coingb.gridwidth = 2;
		coingb.weightx = 1;
		coingb.fill = GridBagConstraints.BOTH;
		panel.add(coin_value, coingb);
		
		GridBagConstraints panelgb = new GridBagConstraints();
		panelgb.gridx = 0;
		panelgb.gridy = 1;
		panelgb.weightx = 1;
		panelgb.fill = GridBagConstraints.BOTH;
		this.add(panel, panelgb);
	}
	
	private void passwordComponent() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));
		
		JLabel label = new JLabel("password");
		label.setFont(new Font("Serif", Font.BOLD, 20));
		GridBagConstraints labelgb = new GridBagConstraints();
		labelgb.gridx = 0;
		labelgb.gridy = 0;
		labelgb.ipady = 20;
		panel.add(label, labelgb);
		
		JLabel labelblank = new JLabel();
		GridBagConstraints labelblankgb = new GridBagConstraints();
		labelblankgb.gridx = 1;
		labelblankgb.gridy = 0;
		labelblankgb.weightx = 1;
		panel.add(labelblank, labelblankgb);
		
		password = new JPasswordField();
		password.setMinimumSize(new Dimension(40, 30));
		password.setPreferredSize(new Dimension(40, 40));
		GridBagConstraints keygb = new GridBagConstraints();
		keygb.gridx = 0;
		keygb.gridy = 1;
		keygb.gridwidth = 2;
		keygb.weightx = 1;
		keygb.fill = GridBagConstraints.BOTH;
		panel.add(password, keygb);
		
		GridBagConstraints panelgb = new GridBagConstraints();
		panelgb.gridx = 0;
		panelgb.gridy = 2;
		panelgb.weightx = 1;
		panelgb.fill = GridBagConstraints.BOTH;
		this.add(panel, panelgb);
	}
	
	private void sendButtonComponent() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));
		
		JLabel labelblank = new JLabel();
		GridBagConstraints labelblankgb = new GridBagConstraints();
		labelblankgb.gridx = 0;
		labelblankgb.gridy = 0;
		labelblankgb.weightx = 1;
		panel.add(labelblank, labelblankgb);
		
		sendButton = new JButton("송금");
		sendButton.setFont(new Font("Serif", Font.BOLD, 15));
		sendButton.setPreferredSize(new Dimension(80, 40));
		sendButton.setBackground(new Color(52, 152, 219));
		sendButton.setBorder(BorderFactory.createEmptyBorder());
		GridBagConstraints buttongb = new GridBagConstraints();
		buttongb.gridx = 1;
		buttongb.gridy = 0;
		panel.add(sendButton, buttongb);
		
		GridBagConstraints panelgb = new GridBagConstraints();
		panelgb.gridx = 0;
		panelgb.gridy = 3;
		panelgb.weightx = 1;
		panelgb.fill = GridBagConstraints.BOTH;
		this.add(panel, panelgb);
	}
	
	private void blankareaComponent(int gridx, int gridy) {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		
		JLabel blank = new JLabel();
		panel.add(blank);
		
		GridBagConstraints panelgb = new GridBagConstraints();
		panelgb.gridx = gridx;
		panelgb.gridy = gridy;
		panelgb.weightx = 1;
		panelgb.weighty = 1;
		panelgb.fill = GridBagConstraints.BOTH;
		this.add(panel, panelgb);
	}

	public JTextField getPublickey() {
		return publickey;
	}

	public JFormattedTextField getCoin_value() {
		return coin_value;
	}

	public JPasswordField getPassword() {
		return password;
	}
	
	public JButton getSendButton() {
		return sendButton;
	}
}
