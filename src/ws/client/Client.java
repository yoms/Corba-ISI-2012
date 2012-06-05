package ws.client;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class Client {
	Service service;

	public void test(String[] args) {
		try { // Call Web Service Operation
			URL wsdlLocation = new URL("http://localhost:8080/ws/MyService?WSDL");
			QName serviceQName = new QName("http://server.ws/", "MyServiceService");

			service = Service.create(wsdlLocation, serviceQName);

			MyService myService = service.getPort(MyService.class);

			String nick = args[0];
			String password = args[1];
			List<Post> posts = myService.getMessages(nick, password);
			if (posts != null) {
				if (posts.size() != 0) {
					for (Iterator iterator = posts.iterator(); iterator.hasNext();) {
						Post post = (Post) iterator.next();
						System.out.println("Messages");
						System.out.println("id " + post.getId());
						System.out.println("pseudoEmetteur " + post.getPseudoEmetteur());
						System.out.println("contenu " + post.getContenu());
						System.out.println("date_heure " + post.getDateHeure());
						System.out.println("id_avatar " + post.getIdAvatar());
						System.out.println();
					}
				} else {
					System.out.println("Pas de nouveaux messages");
				}
			} else {
				System.out.println("Login Error");
			}
		} catch (Exception ex) {
			// TODO handle custom exceptions here
			ex.printStackTrace();
		}
		// TODO code application logic here
	}

	public static void main(String[] args) {

		Client client = new Client();

		client.test(args);
	}
}
