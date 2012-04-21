package fr.corba.client;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import fr.corba.idl.Code.Server;
import fr.corba.idl.Code.UnknownID;

public class Chat extends JFrame {
	private static Chat instance;
	private Canvas canvas;
	private JTextField chatTextBox;
	private UserPOAImpl userPoa;
	private Server server;

	public static Chat getInstance(UserPOAImpl userPoa, Server server) {
		if (instance == null)
			instance = new Chat(userPoa, server);

		instance.setFocusable(true);
		return instance;
	}

	private Chat(UserPOAImpl u, Server s) {
		super(u.getNick());

		this.userPoa = u;
		this.server = s;
		this.setBounds(100, 100, 836, 605);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new GridLayout(0, 2, 0, 0));
		this.setPreferredSize(new Dimension(800, 500));
		this.setMinimumSize(this.getPreferredSize());
		this.addWindowListener(new WindowListener() {

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
		this.getContentPane().add(worldPanel);

		canvas = new Canvas();
		worldPanel.add(canvas);

		JPanel chatPanel = new JPanel();
		this.getContentPane().add(chatPanel);
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

		JTextPane chatHistory = new JTextPane();
		chatHistory.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(chatHistory);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(200, 50));
		chatPanel.add(scrollPane);

		StyledDocument doc = chatHistory.getStyledDocument();

		Style style = doc.addStyle("AEcrit", null);
		StyleConstants.setBold(style, true);
		StyleConstants.setFontFamily(style, "SansSerif");
		StyleConstants.setFontSize(style, 16);

		style = doc.addStyle("Ecrit", null);
		StyleConstants.setFontFamily(style, "SansSerif");
		StyleConstants.setFontSize(style, 14);
		this.userPoa.setChatHistory(chatHistory);

		style = doc.addStyle("Heure", null);
		StyleConstants.setItalic(style, true);
		StyleConstants.setFontFamily(style, "SansSerif");
		StyleConstants.setFontSize(style, 10);
		this.userPoa.setChatHistory(chatHistory);

		JPanel sendPanel = new JPanel();
		chatPanel.add(sendPanel);
		sendPanel.setLayout(new BoxLayout(sendPanel, BoxLayout.X_AXIS));

		chatTextBox = new JTextField();
		sendPanel.add(chatTextBox);
		chatTextBox.setHorizontalAlignment(SwingConstants.TRAILING);
		chatTextBox.setMaximumSize(new Dimension(800, 500));
		chatTextBox.setColumns(10);
		chatTextBox.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER && !chatTextBox.getText().trim().equalsIgnoreCase("")) {
					addPost();
				}
			}
		});

		JButton sendButton = new JButton("Envoyer");
		sendPanel.add(sendButton);
		sendButton.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (!chatTextBox.getText().trim().equalsIgnoreCase("")) {
					addPost();
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

		this.setLocationRelativeTo(null);
		this.setVisible(true);
		chatTextBox.requestFocusInWindow();
		chatTextBox.requestFocus();
	}

	protected void addPost() {
		try {
			Calendar calendar = new GregorianCalendar();
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			String time = hour + ":" + minute;

			this.server.comment(UserRunnable.id, chatTextBox.getText());
			StyledDocument doc = this.userPoa.getChatHistory().getStyledDocument();
			doc.insertString(doc.getLength(), "moi : \n", doc.getStyle("AEcrit"));
			doc.insertString(doc.getLength(), chatTextBox.getText() + "\n", doc.getStyle("Ecrit"));
			doc.insertString(doc.getLength(), time + "\n\n", doc.getStyle("Heure"));
			chatTextBox.setText("");
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
