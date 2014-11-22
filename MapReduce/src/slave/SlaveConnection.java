package slave;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import dfs.DataNode;
import util.Configuration;
import util.Message;
import util.Tasks;
import example.*;


public class SlaveConnection implements Runnable {
	public String MasterAddr;
	public int port;
	public ServerSocket serverSoc;
	private boolean run;
	public SlaveCoordinator coord;
	
	public SlaveConnection(String MasterAddr, SlaveCoordinator coord)
	{
		this.MasterAddr = MasterAddr;
		this.coord = coord;
	}

	public void setPort(int masterListenPort)
	{
		port = masterListenPort;
		SlaveController.initSlave();
		
	}
	
	public void run()
	{
		serverSoc = null;
		try {
			serverSoc = new ServerSocket(Configuration.slaveListenPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		run = true;
		while (run)
		{
			Socket s;
			try {
				s = serverSoc.accept();
				ObjectInputStream in;
				ObjectOutputStream out;
				InputStream ins = s.getInputStream();
				out = new ObjectOutputStream(s.getOutputStream());
				in = new ObjectInputStream(ins);
				Message msg = (Message) in.readObject();
				if (msg.getType() == 's') {
					String f = msg.getFileName();
					int l = msg.getLines();
					System.out.println(f+l);
					System.out.println(coord.a);
					
					coord.dataNode.receiveFileFromStream(msg.getFileName(), ins, msg.getLines());
				} else {
					Message outMsg = receiveMessage(msg);
					out.writeObject(outMsg);
				}
				in.close();
				out.close();
				s.close();	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public Message receiveMessage(Message msg)
	{
		System.out.println("Message received");
		Message resp = new Message();
		resp.setType('a');
		if('a' == msg.getType())
		{
			//Received Acknowledgement Send acknowledgement
			return resp;
		}
		if('p' == msg.getType())
		{
			setPort(msg.getPort());
			return resp;
		}
		if('t'== msg.getType())
		{
			//Start the Task
			Tasks t = msg.getTask();
			coord.startTask(t);
			return resp;
		}
		if('k' == msg.getType())
		{
			//Start Killing the Task
			Tasks t = msg.getTask();
			coord.stopTask(t);
			return resp;
		}
		if('c'== msg.getType())
		{
			coord.stopServ();
			return resp;
		}
		if('s'== msg.getType())
		{
			
			return resp;
		}
		if('f'== msg.getType())
		{
			try {
				coord.dataNode.sendFileToAddr(msg.getFileName(), msg.getAddr());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resp;
		}
		else{
			return null;
		}
	}
	
	public void sendMessage(Message msg)
	{
		System.out.println("Message sent");
		try {
			Socket s = new Socket(MasterAddr, port);
			ObjectInputStream in;
			ObjectOutputStream out;
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			out.writeObject(msg);
			out.flush();
			System.out.println("serial?");
			Message inMsg = (Message)in.readObject();
			if('f' == inMsg.getType())
			{
				//Remove task from local coordinator and kill the thread
				Tasks t = msg.getTask();
				(coord.taskToThread.get(t)).finish();
				coord.taskToThread.remove(t);
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

	public void stop() {
		run = false;
	}
}
