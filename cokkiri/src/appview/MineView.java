package appview;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import coin.Coin;

public class MineView extends JPanel{
	private int width_size = 800;
	private int left_margin = 20;
	private int right_margin = 20;
	
	private JTextArea minearea;
	private JButton button_mine;
	private JTextField publickeyText;
	
	public MineView() {
		super(new BorderLayout(40, 40));
		initComponent();
	}

	private void initComponent() {
//		this.setPreferredSize(new Dimension(800, 300));
		this.setBorder(BorderFactory.createEmptyBorder(10, left_margin, 0, right_margin));
		this.setOpaque(false);
		width_size -= (left_margin + right_margin);

		minearea = new JTextArea(5, 10);
		minearea.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(minearea);
		this.add(scrollPane, BorderLayout.CENTER);
		
		
		//여기서부터 버튼 UI
		JPanel panel_button = new JPanel(new GridBagLayout());
		panel_button.setOpaque(false);
		
		publickeyText = new JTextField(Coin.wallet.getAddress().getString());
		GridBagConstraints pubk = new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 30), 0, 0);
		panel_button.add(publickeyText, pubk);
		
		JLabel blankArea = new JLabel("채굴 주소");
		GridBagConstraints ba = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel_button.add(blankArea, ba);
		
		button_mine = new JButton("채굴 시작");
		GridBagConstraints bmk = new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel_button.add(button_mine, bmk);
		
		//this.add(panel_button, BorderLayout.AFTER_LAST_LINE);
		this.add(panel_button, BorderLayout.NORTH);
	}
	
	public JTextArea getMinearea() {
		return minearea;
	}

	public JButton getButton_mine() {
		return button_mine;
	}
	
	public JTextField getPublickeyText() {
		return publickeyText;
	}

}
