package fr.corba.client;

import java.awt.Canvas;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import fr.corba.idl.Code.Avatar;
import fr.corba.idl.Code.Piece;
import fr.corba.idl.Code.UserPOA;

public class UserPOAImpl extends UserPOA {
	private JTextPane chatHistory;
	private Canvas canvas;
	private Avatar avatar;
	private UserIHM userIHM;

	public UserPOAImpl(UserIHM userIHM) {
		super();
		this.userIHM = userIHM;
		this.avatar = new Avatar();
	}

	public Avatar getAvatar() {
		return avatar;
	}

	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	public JTextPane getChatHistory() {
		return chatHistory;
	}

	public void setChatHistory(JTextPane chatHistory) {
		this.chatHistory = chatHistory;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void receiveChatMessage(String pseudo, String text) {
		// TODO Auto-generated method stub
		System.out.println("receiveChatMessage(" + pseudo + ", " + text + ")");
		if (chatHistory == null)
			return;

		if (!this.getAvatar().pseudo.equalsIgnoreCase(pseudo)) {
			StyledDocument doc = chatHistory.getStyledDocument();
			try {
				Calendar calendar = new GregorianCalendar();
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				String time = hour + ":" + minute;

				doc.insertString(doc.getLength(), pseudo + " a écrit : \n", doc.getStyle("AEcrit"));
				doc.insertString(doc.getLength(), text + "\n", doc.getStyle("Ecrit"));
				doc.insertString(doc.getLength(), time + "\n\n", doc.getStyle("Heure"));
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void receiveKicked() {
		try {
			System.out.println("receiveKicked");
			this.userIHM.kickedDialog();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void receivePiece(Piece room) {
		// TODO Auto-generated method stub
		System.out.println("receiveRoom(Object room)");
	}

	@Override
	public void receiveMoved(String nick, short x, short y) {
		// TODO Auto-generated method stub
		System.out.println("receiveMoved(String nick, short x, short y)");

	}

}
