package fr.corba.client;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import java.awt.GridLayout;
import java.awt.Label;
import javax.swing.table.DefaultTableModel;

public class AvatarsPanel extends JPanel {
	private JTable table;

	/**
	 * Create the panel.
	 */
	public AvatarsPanel() {
		setLayout(new GridLayout(0, 1, 0, 0));
		table = new JTable();
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
			},
			new String[] {
				"Pseudo", "Taille", "Humeur", "Piece"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				true, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		add(scrollPane);

	}

}
