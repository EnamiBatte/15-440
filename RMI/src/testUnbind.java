import java.io.*;

public class testUnbind {
	public static void main(String args[])    
		throws IOException
	    {
		String ServiceName;
		String host;
		int port;
		// it takes one argument.
		// these are it wishes to connect to
		// these are data.
		host = args[0];
		port = Integer.parseInt(args[1]);
		ServiceName = args[2];
		// locate.
		SimpleRegistry sr = LocateSimpleRegistry.getRegistry(host, port);

		System.out.println("located."+sr+"/n");
		
		if (sr != null)
		    {
			// bind.
			RemoteObjectRef ror = sr.lookup(ServiceName);
			//unbind
			sr.unbind(ServiceName);
			// test the binding by looking up.
			RemoteObjectRef ror2 = sr.lookup(ServiceName);
		    }
		else		
		    {
			System.out.println("no registry found.");
		    }

	    }
	}
