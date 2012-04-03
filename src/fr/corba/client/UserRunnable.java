package fr.corba.client;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManager;

public class UserRunnable implements Runnable {
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

	public static ORB orb;
	public static String id;
}