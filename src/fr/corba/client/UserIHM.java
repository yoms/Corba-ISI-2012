package fr.corba.client;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;

import fr.corba.idl.Code.NameAlreadyUsed;
import fr.corba.idl.Code.Server;
import fr.corba.idl.Code.ServerHelper;
import fr.corba.idl.Code.UnknownID;
import fr.corba.idl.Code.User;

public class UserIHM {

	private JFrame frame;
	private JTextField chatTextBox;
	private Thread userRunnableThread;
	private Server server;
	private User user;
	private UserPOAImpl userPoa;
	private Canvas canvas;

	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserIHM window = new UserIHM(args);
					if (window.frame != null)
						window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initializeORB(String[] args) {
		try {
			UserRunnable.orb = ORB.init(args, null);
			// getting NameService
			org.omg.CORBA.Object obj = UserRunnable.orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = org.omg.CosNaming.NamingContextExtHelper.narrow(obj);

			// resolving servant name
			obj = ncRef.resolve_str("chatserver_yzioaw");
			server = ServerHelper.narrow(obj);

			// creating servant
			userPoa = new UserPOAImpl();
			// connecting servant to ORB
			user = userPoa._this(UserRunnable.orb);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public boolean subscribe() {
		userRunnableThread = new Thread(new UserRunnable());
		try {
			UserRunnable.id = server.subscribe(this.userPoa.getNick(), user);
			userRunnableThread.start();
		} catch (NameAlreadyUsed e) {
			JOptionPane.showMessageDialog(null, "Nom déjà existant", "Erreur", JOptionPane.ERROR_MESSAGE);
			this.userPoa.setNick(null);
			this.userPoa.setMdp(null);
			return false;
		}
		return true;
	}

	public void connectionDialog() {
		String nick = "";
		char[] mdp = null;
		int cancel = 0;
		JTextField utilisateur = new JTextField();
		JPasswordField passe = new JPasswordField();
		while (cancel != -1 && cancel != 2 && (nick == null || nick.equalsIgnoreCase("") || mdp == null || mdp.toString().equalsIgnoreCase(""))) {
			cancel = JOptionPane.showOptionDialog(null, new Object[] { "Identifiant :", utilisateur, "Mot de passe :", passe }, "Connexion", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			nick = utilisateur.getText();
			mdp = passe.getPassword();
			if (cancel == 0 && !nick.equalsIgnoreCase("") && !mdp.toString().equalsIgnoreCase("")) {
				this.userPoa.setNick(nick);
				this.userPoa.setMdp(mdp);
				if (!this.subscribe()) {
					nick = "";
					mdp = new char[0];
				}
			}
		}
	}

	/**
	 * Create the application.
	 * 
	 * @param args
	 */
	public UserIHM(String[] args) {
		initializeORB(args);
		int retour = JOptionPane.showOptionDialog(null, "Avez-vous déjà un compte ?", "Bienvenue", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		switch (retour) {
		// Connexion
		case 0:
			this.connectionDialog();
			if (this.userPoa.getNick() != null && this.userPoa.getMdp() != null) {
				initialize();
				System.out.println("Chat de " + this.userPoa.getNick());
			}
			break;
		// Creation d'un compte
		case 1:
			new Formulaire();
			break;
		// Annuler
		case 2:
			System.exit(0);

			break;
		default:
			break;
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame(this.userPoa.getNick());
		frame.setBounds(100, 100, 836, 605);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 2, 0, 0));
		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				try {
					server.unsubscribe(UserRunnable.id);
				} catch (UnknownID e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		JPanel worldPanel = new JPanel();
		frame.getContentPane().add(worldPanel);

		canvas = new Canvas();
		worldPanel.add(canvas);

		JPanel chatPanel = new JPanel();
		frame.getContentPane().add(chatPanel);
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

		JTextPane chatHistory = new JTextPane();
		chatPanel.add(chatHistory);
		chatHistory.setEditable(false);
		StyledDocument doc = chatHistory.getStyledDocument();

		Style style = doc.addStyle("AEcrit", null);
		StyleConstants.setItalic(style, true);
		StyleConstants.setFontFamily(style, "SansSerif");
		StyleConstants.setFontSize(style, 16);

		style = doc.addStyle("Ecrit", null);
		StyleConstants.setItalic(style, false);
		StyleConstants.setFontFamily(style, "SansSerif");
		StyleConstants.setFontSize(style, 14);
		userPoa.setChatHistory(chatHistory);

		JPanel sendPanel = new JPanel();
		chatPanel.add(sendPanel);
		sendPanel.setLayout(new BoxLayout(sendPanel, BoxLayout.X_AXIS));

		chatTextBox = new JTextField();
		sendPanel.add(chatTextBox);
		chatTextBox.setHorizontalAlignment(SwingConstants.TRAILING);
		chatTextBox.setMaximumSize(new Dimension(1250, 500));
		chatTextBox.setColumns(10);
		chatTextBox.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER && !chatTextBox.getText().trim().equalsIgnoreCase("")) {
					try {
						server.comment(UserRunnable.id, chatTextBox.getText());
						StyledDocument doc = userPoa.getChatHistory().getStyledDocument();
						doc.insertString(doc.getLength(), "moi : \n", doc.getStyle("AEcrit"));
						doc.insertString(doc.getLength(), chatTextBox.getText() + "\n", doc.getStyle("Ecrit"));
						chatTextBox.setText("");
					} catch (Exception exp) {
						// TODO: handle exception
					}
				}
			}
		});

		JButton sendButton = new JButton("Send");
		sendPanel.add(sendButton);
		sendButton.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (!chatTextBox.getText().trim().equalsIgnoreCase("")) {
					// TODO Auto-generated method stub
					try {
						server.comment(UserRunnable.id, chatTextBox.getText());
						StyledDocument doc = userPoa.getChatHistory().getStyledDocument();
						doc.insertString(doc.getLength(), "moi : \n", doc.getStyle("AEcrit"));
						doc.insertString(doc.getLength(), chatTextBox.getText() + "\n", doc.getStyle("Ecrit"));
						chatTextBox.setText("");
					} catch (Exception exp) {
						// TODO: handle exception
					}
				}
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
			}

			public void mouseReleased(MouseEvent arg0) {
			}
		});
		frame.setVisible(true);
		chatTextBox.requestFocusInWindow();
		chatTextBox.requestFocus();
	}

}
