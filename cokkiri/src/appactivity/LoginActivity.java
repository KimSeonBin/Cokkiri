package appactivity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import coin.Coin;
import login.Login;
import appview.LoginView;

public class LoginActivity {
	private LoginView loginview;
	private JTextField idtext;
	private JPasswordField pwtext;
	private JButton loginbutton;
	private JButton signinbutton;
	
	public LoginActivity() {
		
	}
	
	public void start() {
		loginview = new LoginView();
		idtext = loginview.getIdtext();
		pwtext = loginview.getPasswordtext();
		loginbutton = loginview.getLoginbutton();
	//	signinbutton = loginview.getSigninbutton();
		
		buttonClickListener();
	}
	
	private void buttonClickListener() {
		loginbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				String id = idtext.getText();
				String pw = pwtext.getText();
				
				//String id = "201420951";
				//String pw = "201420951";
//				
				String identifier = Login.login(id, pw);
				
				if(identifier != null) {
					Coin.id=identifier;
					loginview.dispose();
					Coin.loginSuccess();
					//log.Logging.consoleLog("login success");
					MainActivity main = new MainActivity();
					main.start();
				}
				else {
					/* login 실패 처리
					*/	
				}
			}

			
		});
		/*signinbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("!!!");
			}
		});*/
	}

}
