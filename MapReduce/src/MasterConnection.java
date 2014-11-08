import java.io.IOException;
import java.net.ServerSocket;


public class MasterConnection {
	public String slaveAddr;
	public int port;
	public ServerSocket serverSoc;
	
	public MasterConnection(String sladdr, int masterListenPort)
	{
		slaveAddr = sladdr;
		port = masterListenPort;
	}
	
	public void start()
	{
		serverSoc = null;
		try {
			serverSoc = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		
	}
	
	public void sendMessage(Message msg)
	{
		
	}
	
	public void processMessage(Message msg)
	{
		
	}
	
	
	
	
}
