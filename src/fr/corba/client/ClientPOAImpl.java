package fr.corba.client;
import fr.corba.idl.chat.*;

public class ClientPOAImpl extends ChatClientPOA{

	@Override
	public void update(String nick, String text) {
		System.out.println("<" + nick + "> " + text);
	}

}
