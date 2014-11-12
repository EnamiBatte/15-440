import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


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
			serverSoc.setSoTimeout(Configuration.timeout);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		run();
	}
	
	public void run()
	{
		boolean run = true;
		while(run)
		{
			Socket s;
			try {
				s = serverSoc.accept();
				ObjectInputStream in;
				ObjectOutputStream out;
				out = new ObjectOutputStream(s.getOutputStream());
				in = new ObjectInputStream(s.getInputStream());
				Message msg = (Message) in.readObject();
				Message outMsg = receiveMessage(msg);
				out.writeObject(outMsg);
				in.close();
				out.close();
				s.close();
				
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
	}
	//Handles Messages
	// Start/Timeout checking Message: acknowledge that connection is up
	// Also handle kill
	// New Task
	public Message receiveMessage(Message msg)
	{
		if('a' == msg.getType())
		{
			//Received Acknowledgement Send acknowledgement
			Message resp = new Message();
			resp.setType('a');
			return resp;
		}
		else{
			return null;
		}
	}
	// Need a thread to do these things
	//Sends Messages
	//Sends a new job to Master to add to queue
	// Sends acknowledgement of finished task (or killed)
	public void sendMessage(Message msg)
	{
		try {
			Socket s = new Socket(MasterAddr, port);
			ObjectInputStream in;
			ObjectOutputStream out;
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			out.writeObject(msg);
			out.flush();
			Message inMsg = (Message)in.readObject();
			out.close();
			in.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
