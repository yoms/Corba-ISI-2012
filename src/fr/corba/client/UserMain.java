package fr.corba.client;

import fr.corba.idl.Code.*;
import java.io.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.CosNaming.*;

public class UserMain implements Runnable {
	public void run() {
		try {
			// getting reference to POA
			org.omg.CORBA.Object obj = orb.resolve_initial_references("RootPOA");
			POA rootpoa = POAHelper.narrow(obj);
			// getting reference to POA manager
			POAManager manager = rootpoa.the_POAManager();
			// activating manager
			manager.activate();
			orb.run();
		} catch (Exception e) {
		}
	}

	protected static ORB orb;

	public static void main(String args[]) {
		try {
			// initializing ORB
			orb = ORB.init(args, null);
			// getting NameService
			org.omg.CORBA.Object obj = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = org.omg.CosNaming.NamingContextExtHelper.narrow(obj);

			// resolving servant name
			obj = ncRef.resolve_str("chatserver_yzioaw");
			Server server = ServerHelper.narrow(obj);

			// creating servant
			UserPOAImpl cc = new UserPOAImpl();
			// connecting servant to ORB
			User user = cc._this(orb);
			Thread t = new Thread(new UserMain());
			String id = server.subscribe("test", user);
			try {
				System.out.println("Connected with ID " + id);
				System.out.println("Type /quit to exit");
				t.start();
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				while (true) {
					String s = br.readLine();
					if (s.equals("/quit"))
						break;
					server.comment(id, s);
				}
			} finally {
				System.out.print("Unsubscribing...");
				server.unsubscribe(id);
				System.out.println(" done");
				orb.destroy();
				t.join();
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}