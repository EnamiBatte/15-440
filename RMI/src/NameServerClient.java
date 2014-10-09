import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class NameServerClient {
	public static void main(String[] args) throws IOException
	{
	if(args.length<3)
	{
		args = new String[]{ "localhost","15440","service"};
	}
	String host = args[0];
	int port = Integer.parseInt(args[1]);
	String serviceName = args[2];
	SimpleRegistry sr =  LocateSimpleRegistry.getRegistry(host, port);
	RemoteObjectRef ror = sr.lookup(serviceName);
	
	RORtbl tbl = new RORtbl();
	
	
	NameServer ns = (NameServer) ror.localise();
	Object o;
	try {
		o = Class.forName("NameServer").newInstance();
		ns.add("service2", null, null);
		
		System.out.println("Checking service");
		RemoteObjectRef temp = ns.match(serviceName);
		System.out.println(temp.getIP());
		System.out.println(temp.getRemoteInterfaceName());
		
		System.out.println("checking next");
		ns = ns.next();
		System.out.println(ns.match("service"));
	} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}		
    }
}

