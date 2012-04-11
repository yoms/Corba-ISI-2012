package fr.corba.client;

import java.awt.Canvas;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.omg.CORBA.Object;

import fr.corba.idl.Code.UserPOA;

public class UserPOAImpl extends UserPOA {
	private JTextPane chatHistory;
	private String nick;
	private char[] mdp;

	public char[] getMdp() {
		return mdp;
	}

	public void setMdp(char[] mdp) {
		this.mdp = mdp;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public JTextPane getChatHistory() {
		return chatHistory;
	}

	public void setChatHistory(JTextPane chatHistory) {
		this.chatHistory = chatHistory;
	}

	private Canvas canvas;

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public void receiveRoom(Object room) {
		// TODO Auto-generated method stub
		System.out.println("receiveRoom(Object room)");
	}

	@Override
	public void receiveChatMessage(String nick, String text) {
		// TODO Auto-generated method stub
		System.out.println("receiveChatMessage("+nick+", "+text+")");
		if (chatHistory == null)
			return;
		
		if(!this.getNick().equalsIgnoreCase(nick)) {
			StyledDocument doc = chatHistory.getStyledDocument();
			try {
				doc.insertString(doc.getLength(), nick + " a écrit : \n", doc.getStyle("AEcrit"));
				doc.insertString(doc.getLength(), text+"\n", doc.getStyle("Ecrit"));
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void receiveMoved(String nick, short x, short y) {
		// TODO Auto-generated method stub
		System.out.println("receiveMoved(String nick, short x, short y)");

	}

}
