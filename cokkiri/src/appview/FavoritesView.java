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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bookmark.Bookmark;
import coin.Coin;

public class FavoritesView extends JPanel{
	private JFrame frame;
	private JPanel contentPanel;
	
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
		fillcontent();
		
		frame.validate();
	}

	private void setFrame() {
		frame = new JFrame("Áñ°Ü Ã£±â");
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
		GridBagConstraints panelgb = new GridBagConstraints(0, 0, 1, 1, 1, 0.05, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		this.add(panel, panelgb);
	}
	
	private void contentPanel() {
		contentPanel = new JPanel(new GridBagLayout());
		contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
//		contentPanel.setOpaque(false);
		contentPanel.setBackground(Color.white);
		contentPanel.setPreferredSize(new Dimension(700, 700));
		JScrollPane scroll = new JScrollPane(contentPanel);
		scroll.setBorder(null);
		
		GridBagConstraints panelgb = new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		this.add(scroll, panelgb);
	}

	private void fillcontent() {
		Iterator<String> keys = Coin.bookmark.keySet().iterator();
		for(keys.hasNext(); keys.hasNext(); ) {
			String key = keys.next();
			System.out.println("name : "+key);
			System.out.println("add : "+Coin.bookmark.get(key));
			addcontent(Coin.bookmark.get(key), key);
		}
		
		blankArea();
	}
	
	public void addcontent(String addressString, String nicknameString) {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		
		JLabel address = new JLabel("ÁÖ¼Ò");
		JTextField addresstext = new JTextField(addressString);
		addresstext.setEditable(false);
		JLabel nickname = new JLabel("º°Äª");
		JTextField nicknametext = new JTextField(nicknameString);
		nicknametext.setEditable(false);
		
		JSeparator sperator = new JSeparator();
		
//		GridBagConstraints addressgb = new GridBagConstraints(0, component_count, 1, 1, 1, 1, GridBagConstraints.CENTER,
//				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
//		contentPanel.add(address, addressgb);
//		GridBagConstraints addresstextgb = new GridBagConstraints(0, component_count+1, 1, 1, 1, 1, GridBagConstraints.CENTER,
//				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
//		contentPanel.add(addresstext, addresstextgb);
//		GridBagConstraints nicknamegb = new GridBagConstraints(0, component_count+2, 1, 1, 1, 1, GridBagConstraints.CENTER,
//				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
//		contentPanel.add(nickname, nicknamegb);
//		GridBagConstraints nicknametextgb = new GridBagConstraints(0, component_count+3, 1, 1, 1, 1, GridBagConstraints.CENTER,
//				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
//		contentPanel.add(nicknametext, nicknametextgb);
//		component_count += 4;
		GridBagConstraints addressgb = new GridBagConstraints(0, 0, 1, 1, 0.2, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(address, addressgb);
		GridBagConstraints addresstextgb = new GridBagConstraints(1, 0, 1, 1, 0.8, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(addresstext, addresstextgb);
		GridBagConstraints nicknamegb = new GridBagConstraints(0, 1, 1, 1, 0.2, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(nickname, nicknamegb);
		GridBagConstraints nicknametextgb = new GridBagConstraints(1, 1, 1, 1, 0.8, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		GridBagConstraints sptextgb = new GridBagConstraints(0, 2, 2, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 0, 0, 0), 0, 0);
		panel.add(sperator, sptextgb);
		panel.add(nicknametext, nicknametextgb);
		GridBagConstraints panelgb = new GridBagConstraints(0, component_count++, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0);
		contentPanel.add(panel, panelgb);
		//component_count += 4;
	}
	
	private void blankArea() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.white);
		
		GridBagConstraints panelgb = new GridBagConstraints(0, 100, 1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		contentPanel.add(panel, panelgb);
	}
	public JButton getAddButton() {
		return addButton;
	}

}
