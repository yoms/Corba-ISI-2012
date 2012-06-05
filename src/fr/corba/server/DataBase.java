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
import fr.corba.idl.Code.Piece;
import fr.corba.idl.Code.Post;

public class DataBase {
	Connection conn;

	public DataBase() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:corba.db");
			if (createDataBase())
				System.out.println("DataBase created");
			if (createUsers())
				System.out.println("Users added");
			if (createRooms())
				System.out.println("Rooms added");
		} catch (Exception e) {
			e.printStackTrace();
		}

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

	public boolean userExist(String nick) {
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select rowid from Avatar where pseudo = '" + nick + "';");
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
			PreparedStatement prep = conn.prepareStatement("insert into Avatar values (null, ?, ?, ?, ?, ?, '1', ?, ?);");

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
			prep.close();
		} catch (Exception e) {
			System.out.println();
			e.printStackTrace();
		}
		return code_acces;
	}

	public Piece getPiece(int idPiece) {
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select id, posX, posY, nom, id_nord, id_sud, id_est, id_ouest from Piece where id = '" + idPiece + "';");
			rs.next();
			int rowid = rs.getInt("id");
			String nom = rs.getString("nom");
			int id_nord = rs.getInt("id_nord");
			int id_sud = rs.getInt("id_sud");
			int id_est = rs.getInt("id_est");
			int id_ouest = rs.getInt("id_ouest");
			int posX = rs.getInt("posX");
			int posY = rs.getInt("posY");

			rs = stat.executeQuery("select id, pseudo, taille, humeur, sexe, id_piece, est_connecte from Avatar where id_piece = '" + rowid + "';");
			ArrayList<Avatar> avatars = new ArrayList<Avatar>();
			while (rs.next()) {
				Avatar avatar = new Avatar(rs.getInt("id"), rs.getString("pseudo"), "", rs.getString("taille"), rs.getString("humeur"), rs.getString("sexe"), rs.getInt("id_piece"), false, rs.getBoolean("est_connecte"));
				avatars.add(avatar);
			}
			Avatar ret[] = new Avatar[avatars.size()];
			Piece piece = new Piece(rowid, posX, posY, nom, id_nord, id_sud, id_est, id_ouest, avatars.toArray(ret));
			stat.close();
			return piece;
		} catch (Exception e) {
			System.out.println();
			e.printStackTrace();
		}
		return null;
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

	public Avatar getAvatar(String nick) {
		Avatar avatar = null;
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select id, pseudo, code_acces, taille, humeur, sexe, id_piece, est_admin, est_connecte from Avatar where pseudo ='" + nick + "';");
			if (rs.next())
				avatar = new Avatar(rs.getInt("id"), rs.getString("pseudo"), rs.getString("code_acces"), rs.getString("taille"), rs.getString("humeur"), rs.getString("sexe"), rs.getInt("id_piece"), rs.getBoolean("est_admin"), rs.getBoolean("est_connecte"));
			stat.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return avatar;
	}

	public Avatar[] selectAllAvatars() {
		ArrayList<Avatar> avatars = new ArrayList<Avatar>();
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select pseudo, taille, humeur, sexe, id_piece from Avatar;");
			while (rs.next()) {
				Avatar avatar = new Avatar(0, rs.getString("pseudo"), "", rs.getString("taille"), rs.getString("humeur"), rs.getString("sexe"), rs.getInt("id_piece"), false, false);
				avatars.add(avatar);
			}
			stat.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Avatar ret[] = new Avatar[avatars.size()];
		return avatars.toArray(ret);
	}

	public boolean setConnected(String nick, boolean isConnected) {
		int ret = 0;
		try {
			Statement stat = conn.createStatement();
			ret = stat.executeUpdate("update Avatar set est_connecte = " + fromBooleanToInt(isConnected) + " where pseudo = '" + nick + "'");
			stat.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret == 1;
	}

	public boolean changePiece(String nick, int idPiece) {
		int ret = 0;
		try {
			Statement stat = conn.createStatement();
			ret = stat.executeUpdate("update Avatar set id_piece = " + idPiece + " where pseudo = '" + nick + "'");
			stat.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret == 1;
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

	private int fromBooleanToInt(boolean b) {
		return (b) ? 1 : 0;
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

	private boolean createDataBase() {
		try {
			Statement stat = conn.createStatement();
			int ch;
			StringBuffer strContent = new StringBuffer();
			URI uri = DataBase.class.getResource("../../../ressource/schema.sql").toURI();
			FileInputStream fin = new FileInputStream(uri.getPath());

			while ((ch = fin.read()) != -1) {
				strContent.append((char) ch);
				if ((char) ch == ';') {
					stat.executeUpdate(strContent.toString());
					strContent = new StringBuffer();
				}
			}

			stat.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	private boolean createUsers() {
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select * from Avatar where pseudo = 'yoms';");
			if (!rs.next()) {
				PreparedStatement prep = conn.prepareStatement("insert into Avatar values (null, ?, ?, ?, ?, ?, ?, ?, ?);");

				prep.setString(1, "yoms");
				prep.setString(2, "superyoms");
				prep.setString(3, "Geant");
				prep.setString(4, "Nouveau");
				prep.setString(5, "Masculin");
				prep.setInt(6, 1);
				prep.setInt(7, 1);
				prep.setInt(8, 0);
				prep.addBatch();

				prep.setString(1, "alex");
				prep.setString(2, "superalex");
				prep.setString(3, "Grand");
				prep.setString(4, "Nouveau");
				prep.setString(5, "Masculin");
				prep.setInt(6, 1);
				prep.setInt(7, 1);
				prep.setInt(8, 0);
				prep.addBatch();

				prep.setString(1, "bob");
				prep.setString(2, "bob");
				prep.setString(3, "Grand");
				prep.setString(4, "Nouveau");
				prep.setString(5, "Masculin");
				prep.setInt(6, 1);
				prep.setInt(7, 0);
				prep.setInt(8, 0);
				prep.addBatch();

				conn.setAutoCommit(false);
				prep.executeBatch();
				conn.setAutoCommit(true);
			}
			stat.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private boolean createRooms() {
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select * from Piece");
			if (!rs.next()) {
				PreparedStatement prep = conn.prepareStatement("insert into Piece values (null, ?, ?, ?, ?, ?, ?, ?);");
				int x = 3, y = 3, roomNumber = 1;
				for (int i = 0; i < x; i++) {
					for (int j = 0; j < y; j++) {
						prep.setInt(1, j);
						prep.setInt(2, i);
						prep.setString(3, "Room" + roomNumber);

						// Nord
						if (i == 0)
							prep.setNull(4, i);
						else
							prep.setInt(4, roomNumber - x);
						// Sud
						if (i == x - 1)
							prep.setNull(5, i);
						else
							prep.setInt(5, roomNumber + x);
						// Est
						if (j == y - 1)
							prep.setNull(6, j);
						else
							prep.setInt(6, roomNumber + 1);
						// Ouest
						if (j == 0)
							prep.setNull(7, j);
						else
							prep.setInt(7, roomNumber - 1);

						prep.addBatch();
						roomNumber++;
					}
				}

				conn.setAutoCommit(false);
				prep.executeBatch();
				conn.setAutoCommit(true);
			}
			stat.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void saveMessage(String from, String to, String content) {
		try {
			Avatar avatar = getAvatar(to);
			PreparedStatement prep = conn.prepareStatement("insert into post values (null, ?, ?, DATETIME('NOW'), ?);");

			prep.setString(1, from);
			prep.setString(2, content);
			prep.setInt(3, avatar.id);
			prep.addBatch();

			conn.setAutoCommit(false);
			prep.executeBatch();
			conn.setAutoCommit(true);
			prep.close();
		} catch (Exception e) {
			System.out.println();
			e.printStackTrace();
		}
	}

	public ArrayList<Post> getMessagesStored(String id) {
		System.out.println("getMessagesStored");
		ArrayList<Post> posts = new ArrayList<Post>();
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select id, pseudoEmetteur, contenu, date_heure, id_avatar from post where id_avatar = '" + id + "';");
			while (rs.next()) {
				int idPost = rs.getInt("id");
				System.out.println("id " + idPost);
				int avatarId = rs.getInt("id_avatar");
				String nom = rs.getString("pseudoEmetteur");
				String contenu = rs.getString("contenu");
				String date = rs.getString("date_heure");
				posts.add(new Post(idPost, nom, contenu, date, avatarId));
			}
			stat.close();
			if (posts.size() > 0) {
				Statement st = conn.createStatement();
				String sql = "DELETE FROM post WHERE id_avatar = '" + posts.get(0).id_avatar + "'";
				st.executeUpdate(sql);
				st.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return posts;
	}

	public ArrayList<Post> getStored(String nick, String password) {
		System.out.println("getStored");
		ArrayList<Post> posts = null;
		try {
			if (verifyUser(nick, password)) {
				posts = new ArrayList<Post>();
				Avatar avatar = getAvatar(nick);
				Statement stat = conn.createStatement();
				ResultSet rs = stat.executeQuery("select id, pseudoEmetteur, contenu, date_heure, id_avatar from post where id_avatar = '" + avatar.id + "';");
				while (rs.next()) {
					int idPost = rs.getInt("id");
					System.out.println("id " + idPost);
					int avatarId = rs.getInt("id_avatar");
					String nom = rs.getString("pseudoEmetteur");
					String contenu = rs.getString("contenu");
					String date = rs.getString("date_heure");
					posts.add(new Post(idPost, nom, contenu, date, avatarId));
				}
				stat.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return posts;
	}
}
