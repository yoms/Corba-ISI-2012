package fr.corba.client;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Bienvenue extends JFrame {

	private JButton create;
	private JButton connect;
	private JButton quit;

	public JButton getCreate() {
		return create;
	}

	public JButton getConnect() {
		return connect;
	}

	public JButton getQuit() {
		return quit;
	}

	public Bienvenue() {
		super("Bienvenue");

		// Ajout d'un conteneur pour mettre un label par ligne
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createTitledBorder("Bienvenue"));

		// Ajout des boutons
		this.connect = new JButton("Se connecter");
		this.create = new JButton("Créer un compte");
		this.quit = new JButton("Quitter");
		p.add(this.connect);
		p.add(this.create);
		p.add(this.quit);

		// Ajout du composant
		this.add(p);
		// Ajuste la fenetre
		pack();
		// Centre la fenetre sur l'ecran
		setLocationRelativeTo(null);
		// Cache la fenetre et libere toutes les ressources systemes associees
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		// Empeche de redimensionner
		setResizable(false);
		// Affiche la fenetre
		setVisible(true);
	}

}
