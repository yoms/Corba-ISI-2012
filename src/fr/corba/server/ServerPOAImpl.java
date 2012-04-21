package fr.corba.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import fr.corba.idl.Code.IdAlreadyUsed;
import fr.corba.idl.Code.NameAlreadyUsed;
import fr.corba.idl.Code.NotAllowed;
import fr.corba.idl.Code.ServerPOA;
import fr.corba.idl.Code.UnknownID;
import fr.corba.idl.Code.User;
import fr.corba.idl.Code.WrongPassword;

public class ServerPOAImpl extends ServerPOA {

	private DataBase db;

	protected class Client {
		public User user;
		public String nick;

		public Client(String nick, User user) {
			this.user = user;
			this.nick = nick;
		}
	}

	public ServerPOAImpl() {
		super();
		db = new DataBase();
	}

	// Map<id, Client>
	protected Map<String, Client> clients = new HashMap<String, Client>();
	protected List<String> nicks = new Vector<String>();

	public String subscribe(String nick, String password, User c) throws NameAlreadyUsed, WrongPassword {
		if (nicks.contains(nick))
			throw new NameAlreadyUsed();
		if (!db.verifyUser(nick, password))
			throw new WrongPassword();
		nicks.add(nick);
		String id = UUID.randomUUID().toString();
		System.out.println("subscribe: " + nick + " -> " + id);
		clients.put(id, new Client(nick, c));
		return id;
	}

	public void unsubscribe(String id) throws UnknownID {
		System.out.println("unsubscribe: " + clients.get(id).nick + " -> " + id);
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
			to.user.receiveChatMessage(from.nick, text);
		}
	}

	@Override
	public void requestMove(short x, short y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeRoom(String myId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestExistingAvatars(String myId) throws NotAllowed {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestKick(String myId, String kickedNick) throws NotAllowed {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestRoomContent(String myId, String roomName) throws NotAllowed {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestConnectedUsers(String myId) throws NotAllowed {
		// TODO Auto-generated method stub

	}

	@Override
	public String addUser(String identifiant, String pseudo, String taille, String humeur) throws NameAlreadyUsed, IdAlreadyUsed {
		System.out.println(identifiant + "," + pseudo + "," + taille + "," + humeur);
		if (db.existsOnAvatar("identifiant", identifiant))
			throw new IdAlreadyUsed();
		if (db.existsOnAvatar("pseudo", pseudo))
			throw new NameAlreadyUsed();
		String[] values = { identifiant, pseudo, taille, humeur };
		String code_acces = db.addUser(values);
		System.out.println("addUser: " + identifiant + " (" + pseudo + "," + code_acces + ")");
		return code_acces;
	}
}