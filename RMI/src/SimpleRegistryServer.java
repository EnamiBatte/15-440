import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class SimpleRegistryServer {
	
	
	//TODO: Implement Table features and populate initial table
	
	public static void main(String args[])
	{
		int port = Integer.parseInt(args[0]);
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
					//lookup RemoteObject ref based on serviceName
					//Send found
					//Send line by line the IP_adr,port,Obj_Key,Remote_Interface_Name
					out.println("not found");
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
					//Actually bind it to table
					out.println("Not Bound");
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
