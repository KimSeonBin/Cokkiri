package appview;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FavoriteDialog extends JFrame{
	private JPanel mainPanel;
	
	private JTextField addresstext;
	private JTextField nicknametext;
	
	private JButton okbutton;
	private JButton cancelbutton;
	
	public FavoriteDialog() {
		super();
		initComponent();
		contentComponent();
		buttonComponent();
		
		mainPanel.setBackground(Color.white);
		this.setContentPane(mainPanel);
		setVisible(true);
		validate();
	}

	private void initComponent() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
		this.setSize(new Dimension(400, 300));
		this.setVisible(true);
		
		mainPanel = new JPanel(new GridBagLayout());
	}

	private void contentComponent() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.white);
		
		JLabel addresslabel = new JLabel("주소");
		GridBagConstraints addresslabelgb = new GridBagConstraints(0, 0, 1, 1, 0.2, 0.5, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0);
		//this.add(addresslabel, addresslabelgb);
		panel.add(addresslabel, addresslabelgb);
		
		addresstext = new JTextField();
		GridBagConstraints addressgb = new GridBagConstraints(1, 0, 1, 1, 0.8, 0.5, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0);
		//this.add(addresstext, addressgb);
		panel.add(addresstext, addressgb);
		
		JLabel nicknamelabel = new JLabel("별칭");
		GridBagConstraints nicklabelgb = new GridBagConstraints(0, 1, 1, 1, 0.2, 0.5, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0);
		//this.add(nicknamelabel, nicklabelgb);
		panel.add(nicknamelabel, nicklabelgb);
		
		nicknametext = new JTextField();
		GridBagConstraints nickgb = new GridBagConstraints(1, 1, 1, 1, 0.2, 0.5, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0);
		//this.add(nicknametext, nickgb);
		panel.add(nicknametext, nickgb);
		
		GridBagConstraints panelgb = new GridBagConstraints(0, 0, 1, 1, 0.2, 0.6, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(20, 0, 0, 0), 0, 0);
		mainPanel.add(panel, panelgb);
	}
	
	private void buttonComponent() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.white);
		
		JLabel blank = new JLabel();
		GridBagConstraints blankgb = new GridBagConstraints(0, 1, 1, 1, 0.4, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(blank, blankgb);
		
		JLabel blankTop = new JLabel();
		GridBagConstraints blankTopgb = new GridBagConstraints(0, 0, 3, 1, 1, 0.4, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(blankTop, blankTopgb);
				
		okbutton = new JButton("확인");
		GridBagConstraints okgb = new GridBagConstraints(1, 1, 1, 1, 0, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(okbutton, okgb);
		
		JLabel blank2 = new JLabel();
		GridBagConstraints blank2gb = new GridBagConstraints(2, 1, 1, 1, 0.4, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(blank2, blank2gb);
		
		JLabel blankBottom = new JLabel();
		GridBagConstraints blankBottomgb = new GridBagConstraints(0, 2, 3, 1, 1, 0.4, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(blankBottom, blankBottomgb);
		
		GridBagConstraints panelgb = new GridBagConstraints(0, 1, 1, 1, 0.2, 0.4, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(20, 0, 20, 0), 0, 0);
		mainPanel.add(panel, panelgb);
	}

	public JTextField getAddresstext() {
		return addresstext;
	}

	public JTextField getNicknametext() {
		return nicknametext;
	}
	
	public JButton getOKButton() {
		return okbutton;
	}
}
