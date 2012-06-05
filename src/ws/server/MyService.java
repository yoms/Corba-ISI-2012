package ws.server;

import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import fr.corba.idl.Code.Post;
import fr.corba.server.DataBase;

@WebService
public class MyService {
	private DataBase db = new DataBase();

	public List<Post> getMessages(String id) {
		return db.getMessagesStored(id);
	}

	public int getTemperature(String city) {
		if (city.equals("Toulouse")) {
			return 20;
		} else if (city.equals("Paris")) {
			return 15;
		}
		return Integer.MIN_VALUE;
	}

	public static void main(String[] args) {
		Endpoint.publish("http://localhost:8080/ws/MyService", new MyService());
	}
}
