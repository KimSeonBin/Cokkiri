package appactivity;

import java.util.Iterator;

import javax.swing.JTextArea;

import appview.HistoryView;

public class HistoryFragment {
	private HistoryView historyview;
	private JTextArea sendtextarea;
	private JTextArea receivetextarea;
	
	public HistoryFragment(HistoryView historyview) {
		this.historyview = historyview;
		this.sendtextarea = historyview.getSendtextarea();
		this.receivetextarea = historyview.getReceivetextarea();
	}
	
	public void refresh() {
		refreshSendarea();
		refreshReceivearea();
	}
	private void refreshSendarea() {
		this.sendtextarea = historyview.getSendtextarea();
	}
	private void refreshReceivearea() {
		this.receivetextarea = historyview.getReceivetextarea();
	}
}
