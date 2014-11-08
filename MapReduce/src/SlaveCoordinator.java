import java.io.IOException;
import java.net.ServerSocket;


public class SlaveCoordinator {
	public String MasterAddr;
	public int port;
	public ServerSocket serverSoc;
	
	public SlaveCoordinator(int masterListenPort)
	{
		MasterAddr = Configuration.Master_Address;
		port = masterListenPort;
	}
	
	
	
	public void start()
	{
		serverSoc = null;
		try {
			serverSoc = new ServerSocket(Configuration.slaveListenPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		run();
	}
	
	public void run()
	{
		
	}
	//Handles Messages
	// Start/Timeout checking Message: acknowledge that connection is up
	// Also handle kill
	// New Task
	public void receiveMessage(Message msg)
	{
		
	}
	//Sends Messages
	// Sends Acknowledgement that connection is up
	// Sends acknowledgement of received task
	// Sends acknowledgement of finished task (or killed)
	public void sendMessage(Message msg)
	{
		
	}
	
	
}
