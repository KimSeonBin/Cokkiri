package appactivity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import appview.FavoriteDialog;
import appview.FavoritesView;
import bookmark.Bookmark;

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
				FavoriteDialog fDialog = new FavoriteDialog();
				JButton okButton = fDialog.getOKButton();
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// ���ã�� 'ok' ������ ��
						String faddress = fDialog.getAddresstext().getText();
						String fnickname = fDialog.getNicknametext().getText();
						if(Bookmark.addBookmark(fnickname, faddress)) {
							System.out.println("�߰� �Ϸ�");
							view.addcontent(faddress, fnickname);
							view.validate();
						}
						else {
							System.out.println("�߰� ����");
						}
					}
				});
			}
		});
	}
}
