package fr.corba.server;

import fr.corba.idl.Code.*;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManager;

public class ServerMain {
	public static void main(String args[]) {
		try {
			// initializing ORB
			ORB orb = ORB.init(args, null);

			// getting reference to POA
			org.omg.CORBA.Object obj = orb.resolve_initial_references("RootPOA");
			POA rootpoa = POAHelper.narrow(obj);
			// getting reference to POA manager
			POAManager manager = rootpoa.the_POAManager();
			// activating manager
			manager.activate();

			// getting NameService
			obj = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = org.omg.CosNaming.NamingContextExtHelper.narrow(obj);

			// creating servant
			ServerPOAImpl cs = new ServerPOAImpl();
			// connecting servant to ORB
			Server server = cs._this(orb);
			// binding servant reference to NameService
			ncRef.rebind(ncRef.to_name("chatserver_yzioaw"), server);

			System.out.println("Object activated");
			// starting orb
			orb.run();

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}