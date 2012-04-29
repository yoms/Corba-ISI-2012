package fr.corba.server;

import java.io.FileInputStream;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import fr.corba.idl.Code.Avatar;

public class DataBase {
	Connection conn;

	public DataBase() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:corba.db");
			createDataBase();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean createDataBase() {
		try {
			int ch;
			StringBuffer strContent = new StringBuffer();
			URI uri = DataBase.class.getResource("../../../ressource/schema.sql").toURI();
			FileInputStream fin = new FileInputStream(uri.getPath());

			while ((ch = fin.read()) != -1)
				strContent.append((char) ch);

			Statement stat = conn.createStatement();
			stat.executeUpdate(strContent.toString());

			Statement verifstat = conn.createStatement();
			ResultSet rs = verifstat.executeQuery("select * from Avatar where pseudo = 'yoms';");
			if (!rs.next()) {
				System.out.println("Save base users ");
				PreparedStatement prep = conn.prepareStatement("insert into Avatar values (null, ?, ?, ?, ?, ?, '', ?, ?);");

				prep.setString(1, "yoms");
				prep.setString(2, "superyoms");
				prep.setString(3, "Geant");
				prep.setString(4, "Nouveau");
				prep.setString(5, "Masculin");
				prep.setInt(6, 1);
				prep.setInt(7, 0);
				prep.addBatch();

				prep.setString(1, "alex");
				prep.setString(2, "superalex");
				prep.setString(3, "Grand");
				prep.setString(4, "Nouveau");
				prep.setString(5, "Masculin");
				prep.setInt(6, 1);
				prep.setInt(7, 0);
				prep.addBatch();

				conn.setAutoCommit(false);
				prep.executeBatch();
				conn.setAutoCommit(true);
			}
			verifstat.close();
			stat.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	public boolean verifyUser(String nick, String password) {
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select rowid from Avatar where pseudo = '" + nick + "' and code_acces = '" + password + "';");
			boolean ret = rs.next();
			stat.close();
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String addUser(String[] values) {
		String code_acces = this.generateCodeAcces();
		try {
			PreparedStatement prep = conn.prepareStatement("insert into Avatar values (null, ?, ?, ?, ?, ?, '', ?, ?);");

			// pseudo
			prep.setString(1, values[0]);
			// code_acces
			prep.setString(2, code_acces);
			// taille
			prep.setString(3, values[1]);
			// humeur
			prep.setString(4, "Nouveau");
			// sexe
			prep.setString(5, values[2]);
			// est_admin
			prep.setInt(6, 0);
			// est_connecte
			prep.setInt(7, 0);
			prep.addBatch();

			conn.setAutoCommit(false);
			prep.executeBatch();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			System.out.println();
			e.printStackTrace();
		}
		return code_acces;
	}

	private String generateCodeAcces() {
		int length = 5;
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		int charLength = chars.length();
		StringBuilder pass = new StringBuilder(charLength);
		for (int x = 0; x < length; x++) {
			int i = (int) (Math.random() * charLength);
			pass.append(chars.charAt(i));
		}
		return pass.toString();
	}

	public boolean existsInAvatar(String column, String value) {
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select rowid from Avatar where " + column + " = '" + value + "';");
			boolean ret = rs.next();
			stat.close();
			return ret;
		} catch (Exception e) {
			System.out.println();
			e.printStackTrace();
		}
		return false;
	}

	public boolean isAdmin(String nick, String password) {
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select rowid from Avatar where pseudo = '" + nick + "' and code_acces = '" + password + "' and est_admin = 1;");
			boolean ret = rs.next();
			stat.close();
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Avatar[] selectAllAvatars() {
		ArrayList<Avatar> avatars = new ArrayList<Avatar>();
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select pseudo, taille, humeur, sexe, piece_courante from Avatar;");
			while (rs.next()) {
				Avatar avatar = new Avatar(0, rs.getString("pseudo"), "", rs.getString("taille"), rs.getString("humeur"), rs.getString("sexe"), rs.getString("piece_courante"), false, false);
				avatars.add(avatar);
			}
			stat.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Avatar ret[] = new Avatar[avatars.size()];
		return avatars.toArray(ret);
	}
}
