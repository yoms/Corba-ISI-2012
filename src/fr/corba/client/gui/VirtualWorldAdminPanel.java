package fr.corba.client.gui;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import fr.corba.client.UserIHM;

public class VirtualWorldAdminPanel extends JPanel {

	private UserIHM userIHM;

	/**
	 * Create the panel.
	 */
	public VirtualWorldAdminPanel(UserIHM u) {
		userIHM = u;
		setLayout(new GridLayout(1, 0, 0, 0));

		Canvas canvas = new Canvas();
		add(canvas);

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel pieceName = new JLabel("Unset");
		pieceName.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(pieceName);

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));

		JList portesList = new JList();
		panel_1.add(portesList);

		JList avatarsList = new JList();
		panel_1.add(avatarsList);

	}

}
