package master;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dfs.*;
import util.*;

public class MasterCoordinator {

	public Map<Integer,String> slaveToAddress;
	public Map<Integer,List<Tasks>> slaveToTasks;
	public Map<Integer,Jobs> jobIDtoJobs;
	public List<Jobs> runningJobs;
	public List<Jobs> queueJobs;
	public List<Jobs> finishedJobs;
	public List<Tasks> queueTasks;
	public boolean SystemUp = false;
	public Map<Integer,MasterConnection> connections;
	public NameNode nameNode;

	public MasterCoordinator()
	{
		slaveToAddress = new HashMap<Integer,String>();
		slaveToTasks = new HashMap<Integer,List<Tasks>>();
		connections = new HashMap<Integer,MasterConnection>();
		jobIDtoJobs = new HashMap<Integer,Jobs>();
		runningJobs = new LinkedList<Jobs>();
		queueJobs = new LinkedList<Jobs>();
		finishedJobs = new LinkedList<Jobs>();
		queueTasks = new LinkedList<Tasks>();
		nameNode = new NameNode(Configuration.Slave_Addresses, Configuration.replication);
	}
	
	public void printQueue()
	{
		for(Jobs i : queueJobs)
		{
			System.out.println(i.toString());
		}
	}
	
	public void printFinished()
	{
		for(Jobs i : finishedJobs)
		{
			System.out.println(i.toString());
		}
	}
	
	public void printRunning()
	{
		for(Jobs i : runningJobs)
		{
			System.out.println(i.toString());
		}
	}
	
	public void startSystem()
	{
		if(SystemUp)
		{
			System.out.println("The system is already up.");
			return;
		}
		else
		{
			if(slaveToAddress.isEmpty())
			{
				int length = Configuration.Slave_Addresses.length;
				for(int i = 0; i < length; i++)
				{
					slaveToAddress.put(i, Configuration.Slave_Addresses[i]);
					startNode(i);
				}
			}
			//For each Slave Node startNode
			SystemUp = true;
		}
		
	}
	public void startNode(int nodeID)
	{
		if(connections.containsKey(nodeID))
		{
			MasterConnection con = connections.get(nodeID);
			con.start();
		}
		else{
			int listenPort = Configuration.masterListenPorts[nodeID];
			MasterConnection newCon = new MasterConnection(slaveToAddress.get(nodeID),listenPort,this);
			newCon.start();
			connections.put(nodeID, newCon);
		}
	}
	
	public void stopSystem()
	{
		//For each Job processKill
		queueJobs = null;
		for(Jobs j: runningJobs)
		{
			processKill(j.getID());
		}
		//Close each node
		Set<Integer> nodes = connections.keySet();
		for(Integer i : nodes)
		{
			closeNode(i);
		}
	}
	//Called by stopSystem
	public void closeNode(int nodeID)
	{
		MasterConnection con = connections.get(nodeID);
		Message msg = new Message();
		msg.setType('c');
		con.sendMessage(msg);
	}
	
	public void processKill(int id)
	{
		Jobs toKill = jobIDtoJobs.get(id);
		List<Tasks> removeTasks = toKill.getQueueTasks();
		queueTasks.removeAll(removeTasks);
		List<Tasks> toKillTasks = toKill.runningTasks();
		for(Tasks t: toKillTasks)
		{
			Integer i = t.getSlaveID();
			MasterConnection con = connections.get(i);
			Message msg = new Message();
			msg.setTask(t);
			msg.setType('k');
			con.sendMessage(msg);
		}
	}
	
	public void addJob(Jobs j)
	{
		
	}
	public void addTask()
	{
		
	}
	
	public void issueNextTask()
	{
		int minLoad = Configuration.maxTasksPerHost;
		int slaveNum = -1;
		for(int i = 0; i< slaveToAddress.size(); i++)
		{
			int num = slaveToTasks.size();
			if(num < minLoad)
			{
				slaveNum = i;
				minLoad = num;
			}
		}
		if(slaveNum < 0)
			return;
		//Check work load of slaves
		//If one is free, send next task in queue to it.
		//Should check that node has the necessary file if possible.
	}
	
	public void acknowledgeRunningTask(Tasks t)
	{
		int JobID = t.getJobID();
		Jobs job = jobIDtoJobs.get(JobID);
		int check = job.updateTasks(t);
		if(check == 2)
		{
			queueJobs.remove(job);
			runningJobs.add(job);		
		}
		int SlaveID = t.getSlaveID();
		slaveToTasks.get(SlaveID).add(t);
	}
	
	public void acknowledgeFinishedTask(Tasks t)
	{
		int SlaveID = t.getSlaveID();
		slaveToTasks.get(SlaveID).remove(t);
		
		int JobID = t.getJobID();
		Jobs job = jobIDtoJobs.get(JobID);
		int check = job.updateTasks(t);
		if(check == 0)
		{
			runningJobs.remove(job);
			finishedJobs.add(job);		
		}
		if(check == -1)
		{
			queueTasks.add(t);
		}
		if(check == -2)
		{
			processKill(JobID);
		}
		issueNextTask();
	}
	
}
