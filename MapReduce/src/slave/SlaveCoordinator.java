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

public class SlaveCoordinator {
	public HashMap<Tasks, RunTask> taskToThread;
	public DataNode dataNode;
	public SlaveConnection conn;

	
	public SlaveCoordinator()
	{
		dataNode = new DataNode();
		conn = new SlaveConnection();
	}
	
	public void startConnection()
	{
		conn.run();
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
