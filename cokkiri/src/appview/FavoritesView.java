package appview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class FavoritesView extends JPanel{
	private JFrame frame;
	
	private JButton addButton;
	private JButton deleteButton;
	
	private int width_size = 800;
	private int height_size = 700;
	private int left_margin = 20;
	private int right_margin = 20;
	
	private int component_count = 0;
	
	public FavoritesView() {
		super();
		setFrame();
		initComponent();
		
		frame.validate();
	}

	private void setFrame() {
		frame = new JFrame("Αρ°ά Γ£±β");
		frame.setVisible(true);
		frame.setSize(800, 800);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		Dimension frameSize = frame.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		frame.setContentPane(this);
	}
	
	private void initComponent() {
		this.setPreferredSize(new Dimension(width_size, height_size));
		this.setBorder(BorderFactory.createEmptyBorder(10, left_margin, 0, right_margin));
		this.setBackground(Color.white);
		this.setLayout(new GridBagLayout());
		
		buttonPanel();
		contentPanel();
	}
	
	private void buttonPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
		panel.setOpaque(false);
		
		JLabel blank = new JLabel();
		GridBagConstraints blankgb = new GridBagConstraints(0, 0, 1, 1, 0.5, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(blank, blankgb);
		
		addButton = new JButton();
		addButton.setBackground(Color.WHITE);
		addButton.setBorderPainted(false);
		try {
			Image img = ImageIO.read(getClass().getResource("/img/add_icon.png"));
			addButton.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		GridBagConstraints addgb = new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(addButton, addgb);
		
		/*
		deleteButton = new JButton();
		deleteButton.setBackground(Color.WHITE);
		deleteButton.setBorderPainted(false);
		try {
			Image img = ImageIO.read(getClass().getResource("/img/add_icon.png"));
			deleteButton.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			e.printStackTrace();
		}
		GridBagConstraints deletegb = new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(deleteButton, deletegb);
		*/
		GridBagConstraints panelgb = new GridBagConstraints(0, 0, 1, 1, 1, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		this.add(panel, panelgb);
	}
	
	private void contentPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
		panel.setOpaque(false);
		
		JScrollPane scroll = new JScrollPane(panel);
		scroll.setOpaque(false);
		
		GridBagConstraints panelgb = new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		this.add(scroll, panelgb);
	}

	public JButton getAddButton() {
		return addButton;
	}
	
	public JFrame getFrame() {
		return frame;
	}

}
