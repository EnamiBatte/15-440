package slave;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import master.MasterConnection;
import util.Message;
import util.Tasks;

public class SlaveMessageSender implements Runnable {
	private String addr;
	private int port;
	private Message msg;
	private SlaveConnection sc;
	
	public SlaveMessageSender(String address,int port, Message message, SlaveConnection sc)
	{
		addr = address;
		this.port = port;
		msg = message;
		this.sc = sc;
	}
	@Override
	public void run() {
		System.out.println("Message sent");
		try {
			Socket s = new Socket(addr, port);
			ObjectInputStream in;
			ObjectOutputStream out;
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			out.writeObject(msg);
			out.flush();
			System.out.println("serial?");
			Message inMsg = (Message)in.readObject();
			System.out.println(inMsg.getType());
			if('f' == inMsg.getType())
			{
				//Remove task from local coordinator and kill the thread
				Tasks t = msg.getTask();
				(sc.coord.taskToThread.get(t)).finish();
				sc.coord.taskToThread.remove(t);
			}
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
