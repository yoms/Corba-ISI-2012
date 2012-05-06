package fr.corba.client.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import fr.corba.client.UserIHM;
import fr.corba.idl.Code.Avatar;
import fr.corba.idl.Code.UnknownID;

public class AvatarsAdminPanel extends JPanel {
	private JTable table;

	private UserIHM userIHM;

	/**
	 * Create the panel.
	 */
	public AvatarsAdminPanel(UserIHM u) {
		this.userIHM = u;
		setLayout(new BorderLayout());
		table = new JTable();
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};
		model.addColumn("Pseudo");
		model.addColumn("Taille");
		model.addColumn("Humeur");
		model.addColumn("Sexe");
		model.addColumn("Piece");
		Avatar avatars[] = userIHM.getServer().requestExistingAvatars();
		for (Avatar attributs : avatars) {
			String[] att = { attributs.pseudo, attributs.taille, attributs.humeur, attributs.sexe, Integer.toString(attributs.id_piece) };
			model.addRow(att);
		}
		table.setModel(model);

		JButton unsubscribe = new JButton("Déconnecter");

		unsubscribe.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				if (table.getSelectedRow() != -1 && !table.getValueAt(table.getSelectedRow(), 0).toString().equalsIgnoreCase(userIHM.getUserPoa().getAvatar().pseudo)) {
					System.out.println("To kick : " + table.getValueAt(table.getSelectedRow(), 0));
					try {
						userIHM.getServer().requestKick(table.getValueAt(table.getSelectedRow(), 0).toString());
					} catch (UnknownID e) {
						JOptionPane.showMessageDialog(((JButton) event.getSource()).getRootPane(), "Cet utilisateur n'est pas connecté.", "Erreur à la déconnexion du compte", JOptionPane.ERROR_MESSAGE);
					}
					catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					System.out.println("Kicked : " + table.getValueAt(table.getSelectedRow(), 0));
				}
			}
		});
		add("Center", scrollPane);
		JPanel inputPanel = new JPanel();
		inputPanel.add(unsubscribe);
		add("South", inputPanel);

	}
}
