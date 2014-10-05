import java.io.*;

// we test simple registry by binding a service to ROR.

public class testRebind
{

    public static void main(String args[])    
	throws IOException
    {
	// it takes seven arguments.
	// these are it wishes to connect to.
    String host = "localhost";
    int port = 15440;
    String ServiceName = "NameServerImpl";
    String IPAdr = "localhost";
    int PortNum = 15640;
    int ObjKey = 1;
    String InterfaceName = "NameServer";
	if(args.length>6)
	{
    host = args[0];
	port = Integer.parseInt(args[1]);

	// these are data.
	ServiceName = args[2];
	IPAdr = args[3];
	PortNum = Integer.parseInt(args[4]);
	ObjKey = Integer.parseInt(args[5]);
	InterfaceName = args[6];
	}

	// make ROR.
	RemoteObjectRef ror = new RemoteObjectRef(IPAdr, PortNum, ObjKey, InterfaceName);

	// this is the ROR content.
	System.out.println("IP address is "+ror.IP_adr);
	System.out.println("Port num is "+ror.Port);
	System.out.println("Object key is "+ror.Obj_Key);
	System.out.println("Interface name is "+ror.Remote_Interface_Name);

	// locate.
	SimpleRegistry sr = LocateSimpleRegistry.getRegistry(host, port);

	System.out.println("located."+sr+"/n");
	
	if (sr != null)
	    {
		// bind.
		sr.rebind(ServiceName, ror);

		// test the binding by looking up.
		RemoteObjectRef ror2 = sr.lookup(ServiceName);

		System.out.println("IP address is "+ror2.IP_adr);
		System.out.println("Port num is "+ror2.Port);
		System.out.println("Object key is "+ror2.Obj_Key);
		System.out.println("Interface name is "+ror2.Remote_Interface_Name);

	    }
	else		
	    {
		System.out.println("no registry found.");
	    }

    }
}
