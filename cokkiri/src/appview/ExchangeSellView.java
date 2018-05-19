package appview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

public class ExchangeSellView extends JPanel{
	private JFormattedTextField coin_value;
	private JTextField coincash;
	private JButton sellbutton;
	private JPasswordField password;
	
	public ExchangeSellView() {
		super();
		initComponent();
	}

	private void initComponent() {
		this.setBackground(new Color(0xff, 0xff, 0xff));
		this.setLayout(new GridBagLayout());
		
		coin_valueComponent();
		coincashComponent();
		//passwordComponent();
		sellbuttonComponent();
		blankareaComponent(0, 4);
	}

	private void coin_valueComponent() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
		
		JLabel label = new JLabel("판매 코인양");
		label.setFont(new Font("Serif", Font.BOLD, 20));
		GridBagConstraints labelgb = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 20);
		panel.add(label, labelgb);
		
		JLabel labelblank = new JLabel();
		GridBagConstraints labelblankgb = new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(labelblank, labelblankgb);
		
		
		coin_value = getNumberTextField();
		GridBagConstraints coin_valuegb = new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(coin_value, coin_valuegb);
		
		GridBagConstraints panelgb = new GridBagConstraints(0, 0, 1, 1, 1, 0.2, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		this.add(panel, panelgb);
	}

	private void coincashComponent() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));
		
		JLabel label = new JLabel("현금 가치");
		label.setFont(new Font("Serif", Font.BOLD, 20));
		GridBagConstraints labelgb = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 20);
		panel.add(label, labelgb);
		
		JLabel labelblank = new JLabel();
		GridBagConstraints labelblankgb = new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(labelblank, labelblankgb);
		
		
		coincash = getNumberTextField();
		coincash.setEditable(false);
		GridBagConstraints coincashgb = new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(coincash, coincashgb);
		
		GridBagConstraints panelgb = new GridBagConstraints(0, 1, 1, 1, 1, 0.2, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
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
	private void sellbuttonComponent() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));
		
		JLabel labelblank = new JLabel();
		GridBagConstraints labelblankgb = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(labelblank, labelblankgb);
		
		sellbutton = new JButton("판매 요청");
		sellbutton.setFont(new Font("Serif", Font.BOLD, 15));
		sellbutton.setPreferredSize(new Dimension(80, 40));
		sellbutton.setBackground(new Color(52, 152, 219));
		sellbutton.setBorder(BorderFactory.createEmptyBorder());
		
		GridBagConstraints buttongb = new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(sellbutton, buttongb);
		
		GridBagConstraints panelgb = new GridBagConstraints(0, 3, 1, 1, 1, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		this.add(panel, panelgb);		
	}
	
	private void blankareaComponent(int gridx, int gridy) {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		
		JLabel blank = new JLabel();
		panel.add(blank);
		
		GridBagConstraints panelgb = new GridBagConstraints(gridx, gridy, 1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		this.add(panel, panelgb);
	}
	
	private JFormattedTextField getNumberTextField() {
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Double.class);
		formatter.setMaximum(0);
		formatter.setMaximum(Double.MAX_VALUE);
		formatter.setAllowsInvalid(false);
		
		return new JFormattedTextField(formatter);
	}
	
	public JFormattedTextField getCoin_value() {
		return coin_value;
	}

	public JTextField getCoincash() {
		return coincash;
	}
	
	public JPasswordField getPassword() {
		return password;
	}

	public JButton getSellbutton() {
		return sellbutton;
	}
	
}
