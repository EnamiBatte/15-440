package master;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import util.Message;
import master.MasterConnection;

public class MessageSender implements Runnable {

	private String addr;
	private int port;
	private Message msg;
	private MasterConnection mc;
	
	public MessageSender(String address,int port, Message message, MasterConnection mc)
	{
		addr = address;
		this.port = port;
		msg = message;
		this.mc = mc;
	}
	@Override
	public void run() {
		System.out.println("Message sent");
		try {
			Socket s = new Socket(addr, port);
			ObjectInputStream in;
			ObjectOutputStream out;
			in = new ObjectInputStream(s.getInputStream());
			out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(msg);
			out.flush();
			Message inMsg = (Message)in.readObject();
			System.out.println(inMsg.getType());
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
