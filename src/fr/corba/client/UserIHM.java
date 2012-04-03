package fr.corba.client;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

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
	private String nick;
	private Canvas canvas;
	private JTextPane chatHistory;
	private ConnectionDialog connectDialog;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserIHM window = new UserIHM(args);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initializeORB(String[] args) {
		try {
			// TODO Auto-generated method stub
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
	public void subscribe(){

		userRunnableThread = new Thread(new UserRunnable());
		try {
			UserRunnable.id = server.subscribe(nick, user);
			userPoa.setChatHistory(chatHistory);
		} catch (NameAlreadyUsed e) {
			// TODO Auto-generated catch block
			connectDialog.setVisible(true);
		}
		userRunnableThread.start();
	}
	/**
	 * Create the application.
	 * @param args 
	 */
	public UserIHM(String[] args) {
		connectDialog = new ConnectionDialog(this);
		initializeORB(args);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
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
		
		chatHistory = new JTextPane();
		chatPanel.add(chatHistory);
		chatHistory.setEditable(false);
		
		JPanel sendPanel = new JPanel();
		chatPanel.add(sendPanel);
		sendPanel.setLayout(new BoxLayout(sendPanel, BoxLayout.X_AXIS));
		
		chatTextBox = new JTextField();
		sendPanel.add(chatTextBox);
		chatTextBox.setHorizontalAlignment(SwingConstants.TRAILING);
		chatTextBox.setMaximumSize(new Dimension(1250, 500));
		chatTextBox.setColumns(10);
		
		JButton sendButton = new JButton("Send");
		sendPanel.add(sendButton);
		sendButton.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				try {
						server.comment(UserRunnable.id, chatTextBox.getText());
						chatTextBox.setText("");
					}
				catch (Exception exp) {
					// TODO: handle exception
				}
				
			}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
		});
	}

}
