package fr.corba.client.gui;

import java.awt.Canvas;
import java.awt.Color;
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
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import fr.corba.client.UserIHM;
import fr.corba.client.UserRunnable;
import fr.corba.idl.Code.Avatar;
import fr.corba.idl.Code.Piece;
import fr.corba.idl.Code.UnknownID;

public class VirtualWorldFrame extends JFrame {
	private static VirtualWorldFrame instance;
	private Canvas canvas;
	private JTextField chatTextBox;
	private Piece piece;

	private UserIHM userIHM;

	public static VirtualWorldFrame getInstance(UserIHM u) {
		if (instance == null)
			instance = new VirtualWorldFrame(u);

		instance.setFocusable(true);
		return instance;
	}

	private VirtualWorldFrame(final UserIHM u) {
		super(u.getUserPoa().getAvatar().pseudo);

		this.userIHM = u;
		this.piece = userIHM.getServer().requestPieceContent(u.getUserPoa().getAvatar().id_piece);
		this.setBounds(100, 100, 836, 605);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
					userIHM.getServer().unsubscribe(UserRunnable.id);
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

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		if (userIHM.getServer().isAdmin(userIHM.getUserPoa().getAvatar().pseudo, userIHM.getUserPoa().getAvatar().code_acces)) {
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			getContentPane().add(tabbedPane);
			tabbedPane.addTab("Jeu", null, panel, null);
			tabbedPane.addTab("Monde virtuel", null, new VirtualWorldAdminPanel(userIHM), null);
			tabbedPane.addTab("Avatars", null, new AvatarsAdminPanel(userIHM), null);
		} else {
			getContentPane().add(panel);
		}

		JPanel worldPanel = new JPanel();
		panel.add(worldPanel);

		worldPanel.setLayout(new BoxLayout(worldPanel, BoxLayout.Y_AXIS));
		canvas = new Canvas();
		canvas.setBackground(Color.WHITE);
		canvas.setPreferredSize(new Dimension(400, 650));
		worldPanel.add(canvas);

		worldPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

		JPanel worldPanelBottom = new JPanel();
		worldPanelBottom.setLayout(new GridLayout(0, 2, 0, 0));

		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};
		model.addColumn("Avatars");
		final Avatar avatars[] = this.piece.avatars;
		for (Avatar attributs : avatars) {
			String[] att = { attributs.pseudo };
			model.addRow(att);
		}
		JTable table = new JTable(model) {
			public String getToolTipText(MouseEvent e) {
				String tip = "Error";
				java.awt.Point p = e.getPoint();
				int rowIndex = rowAtPoint(p);
				int realRowIndex = convertRowIndexToModel(rowIndex);
				try {
					tip = avatars[realRowIndex].taille + " - " + avatars[realRowIndex].humeur + " - " + avatars[realRowIndex].sexe;
				} catch (RuntimeException e1) {
					// catch null pointer exception if mouse is over an empty line
				}
				return tip;
			}
		};
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumn("Avatars").setCellRenderer(dtcr);
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		worldPanelBottom.add(scrollPane);

		JPanel direction = new JPanel();
		direction.setLayout(new GridLayout(2, 3, 0, 0));
		direction.add(new JPanel());
		JButton jb = new JButton("Nord");
		jb.setPreferredSize(new Dimension(50, 30));
		direction.add(jb);
		direction.add(new JPanel());
		jb = new JButton("Est");
		jb.setPreferredSize(new Dimension(50, 30));
		direction.add(jb);
		jb = new JButton("Sud");
		jb.setPreferredSize(new Dimension(50, 30));
		direction.add(jb);
		jb = new JButton("Ouest");
		jb.setPreferredSize(new Dimension(50, 30));
		direction.add(jb);
		worldPanelBottom.add(direction);
		worldPanel.add(worldPanelBottom);

		JPanel chatPanel = new JPanel();
		panel.add(chatPanel);
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

		JTextPane chatHistory = new JTextPane();
		chatHistory.setEditable(false);

		scrollPane = new JScrollPane(chatHistory);
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

		style = doc.addStyle("Heure", null);
		StyleConstants.setItalic(style, true);
		StyleConstants.setFontFamily(style, "SansSerif");
		StyleConstants.setFontSize(style, 10);

		userIHM.getUserPoa().setChatHistory(chatHistory);
		userIHM.getUserPoa().setChatHistory(chatHistory);

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
		chatTextBox.requestFocusInWindow();
		chatTextBox.requestFocus();

		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	protected void addPost() {
		try {
			Calendar calendar = new GregorianCalendar();
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			String time = hour + ":" + minute;

			userIHM.getServer().comment(UserRunnable.id, chatTextBox.getText());
			StyledDocument doc = userIHM.getUserPoa().getChatHistory().getStyledDocument();
			doc.insertString(doc.getLength(), "moi : \n", doc.getStyle("AEcrit"));
			doc.insertString(doc.getLength(), chatTextBox.getText() + "\n", doc.getStyle("Ecrit"));
			doc.insertString(doc.getLength(), time + "\n\n", doc.getStyle("Heure"));
			chatTextBox.setText("");
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
