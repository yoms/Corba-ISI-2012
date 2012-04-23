package fr.corba.server;

import java.io.FileInputStream;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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
			ResultSet rs = verifstat.executeQuery("select * from Avatar where identifiant = 'yoms';");
			if (!rs.next()) {
				System.out.println("Save base users ");
				PreparedStatement prep = conn.prepareStatement("insert into Avatar values (null, ?, ?, ?, ?, ?, '', ?, ?);");

				prep.setString(1, "yoms");
				prep.setString(2, "superyoms");
				prep.setString(3, "yoms");
				prep.setString(4, "geant");
				prep.setString(5, "nouveau");
				prep.setInt(6, 1);
				prep.setInt(7, 0);
				prep.addBatch();

				prep.setString(1, "alex");
				prep.setString(2, "superalex");
				prep.setString(3, "alex");
				prep.setString(4, "grand");
				prep.setString(5, "nouveau");
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
			ResultSet rs = stat.executeQuery("select rowid from Avatar where identifiant = '" + nick + "' and code_acces = '" + password + "';");
			boolean ret = rs.next();
			stat.close();
			return ret;
		} catch (Exception e) {
			System.out.println();
			e.printStackTrace();
		}
		return false;
	}

	public String addUser(String[] values) {
		String code_acces = this.generateCodeAcces();
		try {
			PreparedStatement prep = conn.prepareStatement("insert into Avatar values (null, ?, ?, ?, ?, ?, '', ?, ?);");

			// identifiant
			prep.setString(1, values[0]);
			// code_acces
			prep.setString(2, code_acces);
			// pseudo
			prep.setString(3, values[1]);
			// taille
			prep.setString(4, values[2]);
			// humeur
			prep.setString(5, values[3]);
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

	public boolean existsOnAvatar(String column, String value) {
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
}
