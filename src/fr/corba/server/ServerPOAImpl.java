package fr.corba.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import fr.corba.idl.chat.ChatClient;
import fr.corba.idl.chat.ChatServerPOA;
import fr.corba.idl.chat.NameAlreadyUsed;
import fr.corba.idl.chat.UnknownID;

public class ServerPOAImpl extends ChatServerPOA {

	protected class Client {
		public ChatClient chatclient;
		public String nick;

		public Client(String nick, ChatClient chatclient) {
			this.chatclient = chatclient;
			this.nick = nick;
		}
	}

	protected Map<String, Client> clients = new HashMap<String, Client>();
	protected List<String> nicks = new Vector<String>();

	public String subscribe(String nick, ChatClient c) throws NameAlreadyUsed {
		if (nicks.contains(nick))
			throw new NameAlreadyUsed();
		nicks.add(nick);
		String id = UUID.randomUUID().toString();
		System.out.println("subscribe: " + nick + " -> " + id);
		clients.put(id, new Client(nick, c));
		return id;
	}

	public void unsubscribe(String id) throws UnknownID {
		System.out.println("unsubscribe: " + id);
		Client c = clients.remove(id);
		if (c == null)
			throw new UnknownID();
		nicks.remove(c.nick);
	}

	public void comment(String id, String text) throws UnknownID {
		Client from = clients.get(id);
		if (from == null)
			throw new UnknownID();
		System.out.println("comment: " + text + " by " + id + " [" + from.nick + "]");
		for (Client to : clients.values()) {
			to.chatclient.update(from.nick, text);
		}
	}
}