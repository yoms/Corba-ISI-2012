package fr.corba.client;
import org.omg.CORBA.Object;

import fr.corba.idl.Code.UserPOA;

public class UserPOAImpl extends UserPOA{

	@Override
	public void receiveRoom(Object room) {
		// TODO Auto-generated method stub
		System.out.println("receiveRoom(Object room)");
	}

	@Override
	public void receiveChatMessage(String nick, String text) {
		// TODO Auto-generated method stub
		System.out.println("receiveChatMessage(String nick, String text)");
		
	}

	@Override
	public void receiveMoved(String nick, short x, short y) {
		// TODO Auto-generated method stub
		System.out.println("receiveMoved(String nick, short x, short y)");
		
	}

}
