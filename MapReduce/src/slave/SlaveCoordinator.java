package slave;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import dfs.*;
import util.*;

public class SlaveCoordinator implements Runnable {
	public String MasterAddr;
	public int port;
	public ServerSocket serverSoc;
	public HashMap<Tasks, RunTask> taskToThread;
	public DataNode dataNode;
	protected volatile Thread t;

	
	public SlaveCoordinator()
	{
		taskToThread = new HashMap<Tasks, RunTask>();
		MasterAddr = Configuration.Master_Address;
		dataNode = new DataNode();
	}
	
	public void setPort(int masterListenPort)
	{
		port = masterListenPort;
		SlaveController.initSlave();
	}
	
	public void start()
	{
		serverSoc = null;
		try {
			serverSoc = new ServerSocket(Configuration.slaveListenPort);
			t = new Thread();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		run();
	}
	
	public void run()
	{
		while (this.t == Thread.currentThread ())
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
					
					dataNode.receiveFileFromStream(msg.getFileName(), ins, msg.getLines());
				}
				Message outMsg = receiveMessage(msg);
				out.writeObject(outMsg);
				in.close();
				out.close();
				s.close();	
			} catch (Exception e) {
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
		if('f'== msg.getType())
		{
			try {
				dataNode.sendFileToAddr(msg.getFileName(), msg.getAddr());
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
	
	
	public void stopServ()
	{
		t = null;
		for(Tasks t: taskToThread.keySet())
		{
			stopTask(t);
		}
		SlaveController.stopSlave();
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

	public void addJobs(Jobs j)
	{
		String input = j.getInputFile();
		int num = 0;
		try {
			num = dataNode.addFileToDFS(input, port, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Apply DFS then send
		Message msg = new Message();
		msg.setJob(j);
		msg.setType('n');
		msg.setPartition(num);
		sendMessage(msg);
	}
	//Sends Messages
	//Sends a new job to Master to add to queue
	// Sends acknowledgement of finished task (or killed)
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
