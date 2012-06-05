package ws.client;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;


public class Client {
	Service service;

	public void test() {
		try { // Call Web Service Operation
			URL wsdlLocation = new URL("http://localhost:8080/ws/MyService?WSDL");
			QName serviceQName = new QName("http://server.ws/", "MyServiceService");

			service = Service.create(wsdlLocation, serviceQName);

			MyService myService = service.getPort(MyService.class);

			String arg0 = "Toulouse";
			int result = myService.getTemperature(arg0);
			System.out.println("Result = " + result);

			String id = "1";
			System.out.println("Messages");
//			WsMessage[] posts = myService.getMessages(id);
//			for (int i = 0; i < posts.length; i++) {
//				System.out.println("id " + posts[i].getId());
//				System.out.println("pseudoEmetteur " + posts[i].getPseudoEmetteur());
//				System.out.println("contenu " + posts[i].getContenu());
//				System.out.println("date_heure " + posts[i].getDateHeure());
//				System.out.println("id_avatar " + posts[i].getIdAvatar());
//			}
			List<Post> posts = myService.getMessages(id);
			for (Iterator iterator = posts.iterator(); iterator.hasNext();) {
				Post post = (Post) iterator.next();
				System.out.println("id " + post.getId());
				System.out.println("pseudoEmetteur " + post.getPseudoEmetteur());
				System.out.println("contenu " + post.getContenu());
				System.out.println("date_heure " + post.getDateHeure());
				System.out.println("id_avatar " + post.getIdAvatar());
				
			}

			arg0 = "Paris";
			result = myService.getTemperature(arg0);
			System.out.println("Result = " + result);
		} catch (Exception ex) {
			// TODO handle custom exceptions here
			ex.printStackTrace();
		}
		// TODO code application logic here
	}

	public static void main(String[] args) {

		Client client = new Client();

		client.test();
	}
}
