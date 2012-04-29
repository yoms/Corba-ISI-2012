package fr.corba.client.gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.corba.client.UserIHM;
import fr.corba.idl.Code.NameAlreadyUsed;

public class FormulaireFrame extends JFrame {
	protected class Panel extends JPanel {

		public Panel(String name, int rows, int cols) {
			setBorder(BorderFactory.createTitledBorder(name));
			setLayout(new GridLayout(rows, cols));
		}

		/*
		 * Ajoute un label et un champ de texte
		 */
		public void addTextField(String name) {
			this.add(new JLabel(name));
			JTextField t = new JTextField(15);
			t.setName(name);
			this.add(t);
		}

		/*
		 * Ajoute un label et une liste deroulante
		 */
		public void addComboBox(String name, Object[] content) {
			this.add(new JLabel(name));
			JComboBox j = new JComboBox(content);
			j.setName(name);
			this.add(j);
		}
	}

	private static FormulaireFrame instance;

	private Panel avatar;
	private ArrayList<String> attributs;

	private UserIHM userIHM;

	public ArrayList<String> getAttributs() {
		return attributs;
	}

	public static FormulaireFrame getInstance(UserIHM u) {
		if (instance == null)
			instance = new FormulaireFrame(u);

		instance.setFocusable(true);
		return instance;
	}

	private FormulaireFrame(UserIHM u) {
		super("Création de compte");

		this.userIHM = u;
		// Ajout d'un conteneur pour mettre un label par ligne
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.avatar = new Panel("Votre avatar", 0, 2);
		this.avatar.addTextField("Pseudo");
		this.avatar.addComboBox("Taille", new Object[] { "Nain", "Petit", "Moyen", "Grand", "Géant" });
		this.avatar.addComboBox("Sexe", new Object[] { "Masculin", "Féminin" });

		// Ajout des boutons
		JPanel p = new JPanel();
		JButton create = new JButton("Créer");
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Créer");
				attributs = new ArrayList<String>();
				for (int i = 0; i < avatar.getComponentCount(); i++) {
					Component c = avatar.getComponent(i);
					if (c instanceof JTextField) {
						System.out.println(((JTextField) c).getName() + " : " + ((JTextField) c).getText());
						if (((JTextField) c).getText() == null || ((JTextField) c).getText().trim().equalsIgnoreCase("")) {
							JOptionPane.showMessageDialog(getContentPane(), "Tous les champs doivent être renseignés.", "Erreur à la création du compte", JOptionPane.ERROR_MESSAGE);
							return;
						}
						attributs.add(((JTextField) c).getText());
					} else if (c instanceof JComboBox) {
						System.out.println(((JComboBox) c).getName() + " : " + ((JComboBox) c).getSelectedItem());
						attributs.add(((JComboBox) c).getSelectedItem().toString());
					}
				}

				String mdp = "";
				try {
					mdp = userIHM.getServer().addUser(attributs.get(0), attributs.get(1), attributs.get(2));
				} catch (NameAlreadyUsed e1) {
					JOptionPane.showMessageDialog(getContentPane(), "Ce pseudo est déjà utilisé.", "Erreur à la création du compte", JOptionPane.ERROR_MESSAGE);
					return;
				}
				JOptionPane.showMessageDialog(getContentPane(), "Voici vos identifiants de connexion :\nPseudo : " + attributs.get(0) + "\nCode d'accès : " + mdp, "Votre compte a été créé", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}
		});
		JButton reinit = new JButton("Réinitialiser");
		reinit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < avatar.getComponentCount(); i++) {
					Component c = avatar.getComponent(i);
					if (c instanceof JTextField) {
						((JTextField) c).setText("");
					}
				}
			}
		});
		JButton cancel = new JButton("Annuler");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		p.add(create);
		p.add(reinit);
		p.add(cancel);

		// Ajout des composants
		((JPanel) getContentPane()).add(this.avatar);
		((JPanel) getContentPane()).add(p);

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
		this.avatar.getComponent(1).requestFocusInWindow();
		this.avatar.getComponent(1).requestFocus();
	}
}
