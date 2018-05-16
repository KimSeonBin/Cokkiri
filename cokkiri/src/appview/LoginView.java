package appview;

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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginView extends JFrame {
	private String iconimg_path = "/img/appicon.png";
	private String personimg_path = "/img/person.png";
	private String keyimg_path = "/img/keyimage.png";
	private String app_name = "아주 코인";

	private JFrame frame;
	private JTextField idtext;
	private JPasswordField passwordtext;
	private JButton loginbutton;
	private JButton signinbutton;

	public LoginView() {
		super();
		frame = this;
		initJFrame();
		initComponent();
	}

	private void initJFrame() {
		this.setVisible(true);
		this.setSize(800, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension frameSize = this.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}

	private void initComponent() {
		JPanel mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setSize(this.getSize());
		mainPanel.setBackground(Color.white);

		IconComponent(mainPanel);
		loginTextFieldComponent(mainPanel);
		buttonComponent(mainPanel);
		
		this.setContentPane(mainPanel);
		this.validate();
	}

	private void IconComponent(JPanel mainPanel) {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);

		JLabel icon = new JLabel();
		try {
			Image img = ImageIO.read(getClass().getResource(iconimg_path));
			Image newimage = img.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
			icon.setIcon(new ImageIcon(newimage));
			icon.setOpaque(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		icon.setHorizontalAlignment(JLabel.CENTER);
		GridBagConstraints icongb = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		panel.add(icon, icongb);

//		JLabel name = new JLabel(app_name);
//		name.setHorizontalAlignment(JLabel.CENTER);
//		name.setFont(new Font("Serif", Font.BOLD, 30));
//		GridBagConstraints namegb = new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.CENTER,
//				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 20);
//		panel.add(name, namegb);

		GridBagConstraints panelgb = new GridBagConstraints(0, 0, 1, 1, 0.6, 0.4, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 150, 0, 150), 0, 0);
		mainPanel.add(panel, panelgb);
	}

	private void loginTextFieldComponent(JPanel mainPanel) {
		Insets margin = new Insets(0, 0, 5, 0);

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);

		textfieldicon(panel, personimg_path, 0, 0, margin);
		idtext = new JTextField();

		GridBagConstraints idgb = new GridBagConstraints(1, 0, 1, 1, 1, 0.5, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, margin, 0, 0);
		panel.add(idtext, idgb);

		textfieldicon(panel, keyimg_path, 0, 1, margin);

		passwordtext = new JPasswordField();
		GridBagConstraints pwgb = new GridBagConstraints(1, 1, 1, 1, 1, 0.5, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, margin, 0, 0);
		panel.add(passwordtext, pwgb);

		GridBagConstraints panelgb = new GridBagConstraints(0, 1, 1, 1, 1, 0.2, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 200, 0, 200), 0, 0);
		mainPanel.add(panel, panelgb);
	}

	private void buttonComponent(JPanel mainPanel) {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);

		loginbutton = new JButton("로그인");
		buttonDesign(loginbutton);
		GridBagConstraints logingb = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 25), 0, 0);
		panel.add(loginbutton, logingb);

		signinbutton = new JButton("계좌 생성");
		buttonDesign(signinbutton);

		GridBagConstraints signgb = new GridBagConstraints(1, 0, 1, 1, 0.9, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 25, 0, 0), 0, 0);
		panel.add(signinbutton, signgb);

		GridBagConstraints panelgb = new GridBagConstraints(0, 2, 1, 1, 1, 0.2, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(20, 200, 150, 200), 0, 0);
		mainPanel.add(panel, panelgb);
	}

	private void buttonDesign(JButton button) {
		button.setFont(new Font("Serif", Font.BOLD, 15));
		button.setBackground(new Color(52, 152, 219));
		button.setBorder(BorderFactory.createEmptyBorder());
	}

	private void textfieldicon(JPanel panel, String path, int gridx, int gridy, Insets margin) {
		JLabel icon = new JLabel();
		try {
			Image img = ImageIO.read(getClass().getResource(path));
			icon.setIcon(new ImageIcon(img));
			icon.setOpaque(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		icon.setHorizontalAlignment(JLabel.CENTER);
		icon.setBorder(BorderFactory.createLineBorder(Color.black));
		GridBagConstraints icongb = new GridBagConstraints();
		icongb.gridx = gridx;
		icongb.gridy = gridy;
		icongb.insets = margin;
		icongb.fill = GridBagConstraints.BOTH;
		panel.add(icon, icongb);
	}

	public JTextField getIdtext() {
		return idtext;
	}

	public JPasswordField getPasswordtext() {
		return passwordtext;
	}

	public JButton getLoginbutton() {
		return loginbutton;
	}

	public JButton getSigninbutton() {
		return signinbutton;
	}	
	
}