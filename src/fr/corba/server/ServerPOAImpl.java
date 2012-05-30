package fr.corba.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.corba.idl.Code.Avatar;
import fr.corba.idl.Code.NameAlreadyUsed;
import fr.corba.idl.Code.Piece;
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
		db.setConnected(nick, true);
		nicks.add(nick);
		String id = UUID.randomUUID().toString();
		System.out.println("subscribe: " + nick + " -> " + id);
		clients.put(id, new Client(nick, c));
		return id;
	}

	public void unsubscribe(String id) throws UnknownID {
		String nick = clients.get(id).nick;
		System.out.println("unsubscribe: " + nick + " -> " + id);
		Client c = clients.remove(id);
		db.setConnected(nick, false);
		if (c == null)
			throw new UnknownID();
		nicks.remove(c.nick);
	}

	public void comment(String id, String text) throws UnknownID {
		Client from = clients.get(id);
		if (from == null)
			throw new UnknownID();
		Pattern p = Pattern.compile("^[a-zA-Z0-9]{3,}:");
	    Matcher m = p.matcher(text);
	    
	    // just try to find a match
	    if (m.find())
		{
			System.out.println("comment: " + text + " by " + id + " [" + from.nick + "] to martine");
			for (Client to : clients.values()) {
				System.out.println("Substring"+text.substring(0,text.indexOf(":")));
				if(to.nick.equals(text.substring(0,text.indexOf(":"))))
				{
					System.out.println("eguals");
					to.user.receiveChatMessage(from.nick, text);
				}
			}
		}
	    else
	    {
			System.out.println("comment: " + text + " by " + id + " [" + from.nick + "]");
			for (Client to : clients.values()) {
				to.user.receiveChatMessage(from.nick, text);
			}
	    }
	}

	@Override
	public String addUser(String nick, String taille, String humeur) throws NameAlreadyUsed {
		System.out.println(nick + "," + taille + "," + humeur);
		if (db.existsInAvatar("pseudo", nick.toLowerCase()))
			throw new NameAlreadyUsed();
		String[] values = { nick, taille, humeur };
		String code_acces = db.addUser(values);
		System.out.println("addUser: (" + nick + "," + code_acces + ")");
		return code_acces;
	}

	@Override
	public boolean isAdmin(String nick, String password) {
		return db.isAdmin(nick, password);
	}

	@Override
	public Avatar[] requestExistingAvatars() {
		return db.selectAllAvatars();
	}

	@Override
	public void requestKick(String nick) throws UnknownID {
		Client kicked = null;
		for (Client client : clients.values()) {
			if (client.nick.equalsIgnoreCase(nick)) {
				kicked = client;
			}
		}
		if (kicked == null)
			throw new UnknownID();
		System.out.println("requestKick: " + nick);
		kicked.user.receiveKicked();
	}

	@Override
	public void requestMove(short x, short y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changePiece(String myId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Piece requestPieceContent(int idPiece) {
		// TODO Auto-generated method stub
		return db.getPiece(idPiece);
	}

	@Override
	public Avatar getAvatar(String nick) {
		// TODO Auto-generated method stub
		System.out.println("test");
		return db.getAvatar(nick);
	}

}