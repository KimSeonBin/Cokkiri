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
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

public class ExchangeBuyView extends JPanel {
	private JFormattedTextField coin_value;
	private JTextField coincash;
	private JButton buybutton;
	
	public ExchangeBuyView() {
		super();
		initComponent();
	}

	private void initComponent() {
		this.setBackground(new Color(0xff, 0xff, 0xff));
		this.setLayout(new GridBagLayout());
		
		coin_valueComponent();
		coincashComponent();
		sellbuttonComponent();
		blankareaComponent(0, 3);
	}

	private void coin_valueComponent() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
		
		JLabel label = new JLabel("구매 코인양");
		label.setFont(new Font("Serif", Font.BOLD, 20));
		GridBagConstraints labelgb = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 20);
		panel.add(label, labelgb);
		
		JLabel labelblank = new JLabel();
		GridBagConstraints labelblankgb = new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
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
		panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));
		panel.setOpaque(false);
		
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
		GridBagConstraints coincashgb = new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(coincash, coincashgb);
		
		GridBagConstraints panelgb = new GridBagConstraints(0, 1, 1, 1, 1, 0.2, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		this.add(panel, panelgb);
	}
	

	private void sellbuttonComponent() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));
		
		JLabel labelblank = new JLabel();
		GridBagConstraints labelblankgb = new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(labelblank, labelblankgb);
		
		buybutton = new JButton("구매 요청");
		buybutton.setFont(new Font("Serif", Font.BOLD, 15));
		buybutton.setPreferredSize(new Dimension(80, 40));
		buybutton.setBackground(new Color(52, 152, 219));
		buybutton.setBorder(BorderFactory.createEmptyBorder());
		GridBagConstraints buttongb = new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(buybutton, buttongb);
		
		GridBagConstraints panelgb = new GridBagConstraints(0, 2, 1, 1, 1, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		this.add(panel, panelgb);		
	}
	
	private void blankareaComponent(int gridx, int gridy) {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.white);
		
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

	public JButton getBuybutton() {
		return buybutton;
	}
	
}
