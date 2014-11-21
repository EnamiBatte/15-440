package slave;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import util.*;

public class SlaveCoordinator {
	public String MasterAddr;
	public int port;
	public ServerSocket serverSoc;
	public boolean run;
	public HashMap<Tasks, RunTask> taskToThread;
	public DataNode dataNode;

	
	public SlaveCoordinator()
	{
		taskToThread = new HashMap<Tasks, RunTask>();
		MasterAddr = Configuration.Master_Address;
		dataNode = new DataNode();
	}
	
	public void setPort(int masterListenPort)
	{
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
		run = true;
		while(run)
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
				if (msg.getType == 's') {
					dataNode.receiveFile(msg.getFileName(), ins, msg.getLines());
				}
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
		try {
			serverSoc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	//Handles Messages
	// Start/Timeout checking Message: acknowledge that connection is up
	// Also handle kill
	// New Task
	public Message receiveMessage(Message msg)
	{
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
			startTask(t);
			return resp;
		}
		if('k' == msg.getType())
		{
			//Start Killing the Task
			Tasks t = msg.getTask();
			stopTask(t);
			return resp;
		}
		if('c'== msg.getType())
		{
			stopServ();
			return resp;
		}
		if('s'== msg.getType())
		{
			return resp;
		}
		else{
			return null;
		}
	}
	
	
	public void stopServ()
	{
		run = false;
		for(Tasks t: taskToThread.keySet())
		{
			stopTask(t);
		}
	}
	
	private void stopTask(Tasks t) {
		taskToThread.get(t).stop();
		taskToThread.remove(t);
	}

	private void startTask(Tasks t) {
		t.setStatus(2);
		if(t.getClass().equals(MapTask.class))
		{
			MapTask m = (MapTask) t;
			RunMap rm = new RunMap(m,this);
			taskToThread.put(m, rm);
			rm.run();
		}
		else{
			ReduceTask r = (ReduceTask) t;
			RunReduce rr = new RunReduce(r,this);
			taskToThread.put(r, rr);
			rr.run();
		}
		
	}

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
			if('f' == inMsg.getType())
			{
				//Remove task from local coordinator and kill the thread
				Tasks t = msg.getTask();
				(taskToThread.get(t)).finish();
				taskToThread.remove(t);
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
