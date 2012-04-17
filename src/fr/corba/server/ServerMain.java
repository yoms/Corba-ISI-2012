package fr.corba.server;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManager;

import fr.corba.idl.Code.Server;

public class ServerMain {
	public static void main(String args[]) {
		try {
			Properties p = new Properties();
			// UTF-8, UTF-16
			p.setProperty("com.sun.CORBA.codeset.charsets", "0x05010001, 0x00010109");
			// UTF-16, UTF-8
			p.setProperty("com.sun.CORBA.codeset.wcharsets", "0x00010109, 0x05010001");
			// initializing ORB
			ORB orb = ORB.init(args, p);

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