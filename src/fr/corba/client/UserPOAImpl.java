package fr.corba.client;
import java.awt.Canvas;

import javax.swing.JTextPane;

import org.omg.CORBA.Object;

import fr.corba.idl.Code.UserPOA;

public class UserPOAImpl extends UserPOA{
	private JTextPane chatHistory;
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
		System.out.println("receiveChatMessage(String nick, String text)");
		if(chatHistory == null)
			return;
		StringBuffer textBuffer = new StringBuffer(chatHistory.getText());
		textBuffer.append("<"+nick+">:"+text+"\n");
		chatHistory.setText(textBuffer.toString());
		
		
	}

	@Override
	public void receiveMoved(String nick, short x, short y) {
		// TODO Auto-generated method stub
		System.out.println("receiveMoved(String nick, short x, short y)");
		
	}

}
