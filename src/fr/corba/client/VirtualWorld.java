package fr.corba.client;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Canvas;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JSplitPane;
import java.awt.Component;

public class VirtualWorld extends JPanel {

	/**
	 * Create the panel.
	 */
	public VirtualWorld() {
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
