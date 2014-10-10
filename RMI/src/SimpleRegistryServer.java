import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;


public class SimpleRegistryServer {
	
	
	//TODO: Implement Table features and populate initial table
	private static Hashtable<String, RemoteObjectRef> nameTable;
	
	public static void nameTable() {
		nameTable = new Hashtable<String, RemoteObjectRef>();
	}
	
	public static void main(String args[]) {
		int port = 15440;
		if(args.length > 0)
			port = Integer.parseInt(args[0]);
		
    	nameTable();
		try {
			
			ServerSocket servSock = new ServerSocket(port);
			while(true) {
				
				Socket sock = servSock.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader (sock.getInputStream()));
				PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
				String arg = in.readLine();
				int argi = 0;
				if (arg.equals("bind")) {
					argi = 1;
				} else if (arg.equals("lookup")) {
					argi = 2;
				} else if (arg.equals("rebind")) {
					argi = 3;
				} else if (arg.equals("unbind")) {
					argi = 4;
				} else if (arg.equals("who are you?")) {
					argi = 5;
				}
				switch (argi) {
					case 1: { // bind
						String serviceName = in.readLine();
						System.out.println("bind of "+ serviceName);
						String ROR_IP_adr = in.readLine();
						int ROR_port = Integer.parseInt(in.readLine());
						int Obj_Key = Integer.parseInt(in.readLine());
						String Remote_Interface_Name = in.readLine();
						RemoteObjectRef ror = new RemoteObjectRef(ROR_IP_adr, ROR_port, Obj_Key, 
								Remote_Interface_Name);
						//bind the service name and ror
						nameTable.put(serviceName, ror);
						break;
					}
					case 2:  { // lookup
						String serviceName = in.readLine();
						System.out.println("lookup of "+ serviceName);
						//lookup RemoteObject ref based on serviceName
						System.out.println("serviceName = " + serviceName);
						if (nameTable.containsKey(serviceName)) {
							RemoteObjectRef ror = nameTable.get(serviceName);
							//Send found
							out.println("found");
							//Send line by line the IP_adr,port,Obj_Key,Remote_Interface_Name
							out.println(ror.getIP());
							out.println(ror.getPort());
							out.println(ror.getObjKey());
							out.println(ror.getRemoteInterfaceName());
						} else {
							out.println("not found");
						}
						break;
					}
					case 3: { // rebind
						String serviceName = in.readLine();
						System.out.println("rebind of "+ serviceName);
						String ROR_IP_adr = in.readLine();
						int ROR_port = Integer.parseInt(in.readLine());
						int Obj_Key = Integer.parseInt(in.readLine());
						String Remote_Interface_Name = in.readLine();
						RemoteObjectRef ror = new RemoteObjectRef(ROR_IP_adr, ROR_port, Obj_Key, 
								Remote_Interface_Name);
						//Actually bind it to table
						if (nameTable.containsKey(serviceName)) {
							nameTable.remove(serviceName);
						}
						nameTable.put(serviceName, ror);
						out.println("Not Bound");
						break;
					}
					case 4: { // unbind
						String serviceName = in.readLine();
						System.out.println("unbind of "+ serviceName);
						if (nameTable.containsKey(serviceName)) {
							nameTable.remove(serviceName);
						}
						break;
					}
					case 5: { // who are you?
						System.out.println("I am a simple registry.");
						out.println("I am a simple registry.");
						break;
					}
					default: {
						System.out.println("Not valid request");
					}
				} //end switch
				in.close();
				out.flush();
				out.close();
				sock.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}