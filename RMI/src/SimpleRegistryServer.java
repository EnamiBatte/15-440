import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class SimpleRegistryServer {
	
	
	
	public static void main(String args[])
	{
		int port;
		HashMap<String,RemoteObjectRef> map = new HashMap<String,RemoteObjectRef>();
		if(args.length > 0)
		{
			port = Integer.parseInt(args[0]);
		}
		else
		{
			port = 15440;
		}
		try {
			ServerSocket servSock = new ServerSocket(port);
			while(true)
			{
				Socket sock = servSock.accept();
				BufferedReader in = 
					    new BufferedReader(new InputStreamReader (sock.getInputStream()));
				PrintWriter out = 
						new PrintWriter(sock.getOutputStream(), true);
				
				String arg = in.readLine();
				if(arg.equals("lookup"))
				{
					String serviceName = in.readLine();
					System.out.println("lookup of "+ serviceName);
					if(map.containsKey(serviceName))
					{
						RemoteObjectRef value = map.get(serviceName);
						out.println("found");
						out.println(value.getIP());
						out.println(Integer.toString(value.getPort()));
						out.println(Integer.toString(value.getObjKey()));
						out.println(value.getRemoteInterfaceName());
					}
					else{
						out.println("not found");
					}
				}
				else if(arg.equals("rebind"))
				{
					String serviceName = in.readLine();
					System.out.println("rebind of "+ serviceName);
					String ROR_IP_adr = in.readLine();
					int ROR_port = Integer.parseInt(in.readLine());
					int Obj_Key = Integer.parseInt(in.readLine());
					String Remote_Interface_Name = in.readLine();
					RemoteObjectRef ror = new RemoteObjectRef(ROR_IP_adr,ROR_port,Obj_Key,Remote_Interface_Name);
					map.put(serviceName, ror);
					//Actually bind it to table
					out.println("Bound");
				}
				else if(arg.equals("who are you?"))
				{
					out.println("I am a simple registry.");
				}
				else{
					System.out.println("Not valid request");
				}
				sock.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
