package appactivity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import appview.FavoriteDialog;
import appview.FavoritesView;

public class FavoriteActivity {
	private FavoritesView view;
	private JButton addbutton;
	public FavoriteActivity() {
		this.view = new FavoritesView();
		this.addbutton = this.view.getAddButton();
		
		setButtonClickListener();
	}

	private void setButtonClickListener() {
		this.addbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				FavoriteDialog inputs = new FavoriteDialog();
//				JOptionPane.showInputDialog(null, inputs, "aaa", JOptionPane.PLAIN_MESSAGE);
				FavoriteDialog fDialog = new FavoriteDialog();
				
				
			}
		});
	}
}
