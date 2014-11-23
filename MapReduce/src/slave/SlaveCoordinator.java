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
import example.*;

public class SlaveCoordinator {
	public HashMap<Tasks, RunTask> taskToThread;
	public DataNode dataNode;
	public SlaveConnection conn;
	public String a;
	private volatile Thread th;

	
	public SlaveCoordinator()
	{
		dataNode = new DataNode();
		conn = new SlaveConnection(Configuration.Master_Address, this);
		taskToThread = new HashMap<Tasks,RunTask>();
	}
	
	public void startConnection()
	{
		th = new Thread(conn);
		th.start();
	}
	
	public void stopServ()
	{
		conn.stop();
		for(Tasks ti: taskToThread.keySet())
		{
			stopTask(ti);
		}
		SlaveController.stopSlave();
	}
	
	void stopTask(Tasks t) {
		taskToThread.get(t).stop();
		taskToThread.remove(t);
	}

	void startTask(Tasks t) {
		t.setStatus(2);
		Message msg = new Message();
		msg.setType('r');
		msg.setTask(t);
		conn.sendMessage(msg);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(t.getClass().equals(MapTask.class))
		{
			MapTask m = (MapTask) t;
			RunMap rm = new RunMap(m,this);
			taskToThread.put(m, rm);
			Thread th = new Thread(rm);
			th.start();
		}
		else{
			ReduceTask r = (ReduceTask) t;
			RunReduce rr = new RunReduce(r,this);
			taskToThread.put(r, rr);
			Thread th = new Thread(rr);
			th.start();
		}
		
	}

	public void addJobs(Jobs j)
	{
		System.out.println("Adding job");
		String input = j.getInputFile();
		int num = 0;
		try {
			System.out.println(input);
			num = dataNode.addFileToDFS(input, conn.port, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Apply DFS then send
		Message msg = new Message();
		msg.setJob(j);
		msg.setType('n');
		msg.setPartition(num);
		conn.sendMessage(msg);
	}
	
}
