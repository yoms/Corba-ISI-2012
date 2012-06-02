package fr.corba.client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;

import fr.corba.client.gui.BienvenueFrame;
import fr.corba.client.gui.FormulaireFrame;
import fr.corba.client.gui.VirtualWorldFrame;
import fr.corba.idl.Code.NameAlreadyUsed;
import fr.corba.idl.Code.Server;
import fr.corba.idl.Code.ServerHelper;
import fr.corba.idl.Code.UnknownID;
import fr.corba.idl.Code.User;
import fr.corba.idl.Code.WrongPassword;

public class UserIHM {

	private Thread userRunnableThread;
	private Server server;
	private User user;

	private UserPOAImpl userPoa;

	private VirtualWorldFrame chat;
	private FormulaireFrame formulaire;
	private BienvenueFrame bienvenue;

	public UserIHM(String[] args) {
		initializeORB(args);

		final UserIHM userIHM = this;
		bienvenue = new BienvenueFrame();
		bienvenue.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				if (chat != null) {
					try {
						server.unsubscribe(UserRunnable.id);
					} catch (UnknownID e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				System.exit(0);
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		bienvenue.getCreate().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formulaire = FormulaireFrame.getInstance(userIHM);
				formulaire.setLocationRelativeTo(null);
				formulaire.setVisible(true);
			}
		});
		bienvenue.getConnect().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectionDialog();
				if (userPoa.getAvatar().pseudo != null && userPoa.getAvatar().code_acces != null) {
					chat = VirtualWorldFrame.getInstance(userIHM);
					chat.setLocationRelativeTo(null);
					chat.setVisible(true);
					bienvenue.setVisible(false);
				}
			}
		});
		bienvenue.getQuit().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chat != null) {
					try {
						server.unsubscribe(UserRunnable.id);
					} catch (UnknownID e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				System.exit(0);
			}
		});
	}

	public User getUser() {
		return user;
	}

	public Server getServer() {
		return server;
	}

	public UserPOAImpl getUserPoa() {
		return userPoa;
	}

	public static void main(final String[] args) {
		try {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					new UserIHM(args);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initializeORB(String[] args) {
		try {
			Properties p = new Properties();
			// UTF-8, UTF-16
			p.setProperty("com.sun.CORBA.codeset.charsets", "0x05010001, 0x00010109");
			// UTF-16, UTF-8
			p.setProperty("com.sun.CORBA.codeset.wcharsets", "0x00010109, 0x05010001");
			UserRunnable.orb = ORB.init(args, p);
			// getting NameService
			org.omg.CORBA.Object obj = UserRunnable.orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = org.omg.CosNaming.NamingContextExtHelper.narrow(obj);

			// resolving servant name
			obj = ncRef.resolve_str("chatserver_yzioaw");
			server = ServerHelper.narrow(obj);

			// creating servant
			userPoa = new UserPOAImpl(this);
			// connecting servant to ORB
			user = userPoa._this(UserRunnable.orb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void connectionDialog() {
		if (this.chat == null) {
			String nick = "";
			char[] mdp = null;
			int cancel = 0;
			JTextField utilisateur = new JTextField();
			JPasswordField passe = new JPasswordField();
			while (cancel != -1 && cancel != 2 && (nick == null || nick.equalsIgnoreCase("") || mdp == null || mdp.toString().equalsIgnoreCase(""))) {
				cancel = JOptionPane.showOptionDialog(null, new Object[] { "Pseudo :", utilisateur, "Mot de passe :", passe }, "Connexion", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				nick = utilisateur.getText();
				mdp = passe.getPassword();
				if (cancel == 0 && !nick.equalsIgnoreCase("") && !mdp.toString().equalsIgnoreCase("")) {
					this.userPoa.getAvatar().pseudo = nick;
					this.userPoa.getAvatar().code_acces = new String(mdp);
					if (!this.subscribe()) {
						nick = "";
						mdp = new char[0];
					}
				}
			}
		} else {
			this.chat.setLocationRelativeTo(null);
			this.chat.setVisible(true);
		}
	}

	public void kickedDialog() {
		System.out.println("kickedDialog " + userPoa.getAvatar().pseudo);
		try {
			server.unsubscribe(UserRunnable.id);
		} catch (UnknownID e1) {
			e1.printStackTrace();
		}
		System.exit(0);
		// JOptionPane.showMessageDialog(chat,
		// "L'administrateur vous a déconnecté.", "Déconnexion",
		// JOptionPane.INFORMATION_MESSAGE);
	}

	public boolean subscribe() {
		userRunnableThread = new Thread(new UserRunnable());
		try {
			UserRunnable.id = server.subscribe(this.userPoa.getAvatar().pseudo, this.userPoa.getAvatar().code_acces, user);
			userRunnableThread.start();
		} catch (NameAlreadyUsed e) {
			JOptionPane.showMessageDialog(null, "Cet utilisateur est déjà connecté.", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
			this.userPoa.getAvatar().pseudo = null;
			this.userPoa.getAvatar().code_acces = null;
			return false;
		} catch (WrongPassword e) {
			JOptionPane.showMessageDialog(null, "Les informations de connexion ne correspondent pas.", "Erreur d'identification", JOptionPane.ERROR_MESSAGE);
			this.userPoa.getAvatar().pseudo = null;
			this.userPoa.getAvatar().code_acces = null;
			return false;
		}
		this.userPoa.setAvatar(server.getAvatar(this.userPoa.getAvatar().pseudo));
		return true;
	}

	public void updateAvatarTable() {
		this.chat.updatePiece();
		this.chat.getAvatarTable().setModel(this.chat.createAvatarModel());
	}
}
