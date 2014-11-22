package master;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	public int numberOfOutgoingJobs = 0;

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
				int length = Configuration.Slave_Addresses.size();
				for(int i = 0; i < length; i++)
				{
					slaveToAddress.put(i, Configuration.Slave_Addresses.get(i));
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
			Thread t = new Thread(con);
			t.start();
		}
		else{
			int listenPort = Configuration.masterListenPorts[nodeID];
			MasterConnection newCon = new MasterConnection(slaveToAddress.get(nodeID),listenPort,this);
			Thread t = new Thread(newCon);
			t.start();
			connections.put(nodeID, newCon);
		}
	}
	
	public void stopSystem()
	{
		//For each Job processKill
		queueJobs = null;
		for(Jobs j: runningJobs)
		{
			processKill(j);
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
	
	public void kill(int jobID) {
		Jobs j = jobIDtoJobs.get(jobID);
		processKill(j);
	}
	public void processKill(Jobs j)
	{
		Jobs toKill = j;
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
	
	public void addJob(Jobs j, int Mappers)
	{
		int next = jobIDtoJobs.size();
		Jobs addedJob = j;
		String inputFile = j.getInputFile();
		List<Tasks> tasks = new LinkedList<Tasks>();
		List<String> outputFiles = j.getOutputFiles();
		for(int i=0; i < Mappers; i ++)
		{
			Tasks t = new MapTask();
			t.setJob(j);
			t.recordlength = j.getRecordSize();
			t.setStatus(1);
			//Needs inputFile for each mapper
			List<String> input = new LinkedList<String>();
			String partitionFile = String.valueOf(i)+"_"+j.getInputFile();
			String inPartitionFile = "m"+partitionFile;
			input.add(inPartitionFile);
			t.in = input;
			//Needs inputFile for each mapper
			List<String> output = new LinkedList<String>();
			String outPartitionFile = "M"+partitionFile;
			input.add(outPartitionFile);
			t.in = output;
			//Needs dfs output files
			tasks.add(t);
		}
		for(int k = 0; k< Configuration.numberOfReducers; k++)
		{
			Tasks t = new ReduceTask();
			t.setJob(j);
			t.recordlength = j.getRecordSize() + 2;
			t.setStatus(3);
			//Needs DFS files associated with Hash
			List<String> input = new LinkedList<String>();
			String correctHash = "_"+j.getInputFile()+"_"+String.valueOf(k);
			for(int i = 0; i< Mappers; i++)
			{
				input.add("RM"+String.valueOf(i)+correctHash);
			}
			t.in = input;
			List<String> out = new LinkedList<String>();
			out.add(outputFiles.get(k));
			t.fileout = out;
			tasks.add(t);
		}
		j.setTasks(tasks);
		j.setID(next);
		jobIDtoJobs.put(next,j);
		queueJobs.add(j);
		queueTasks.addAll(j.getQueueTasks());
		issueNextTask();
	}
	
	
	public void issueNextTask()
	{
		//Check work load of slaves
		//If one is free, send next task in queue to it.
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
		if(minLoad + numberOfOutgoingJobs > Configuration.maxTasksPerHost)
			return;
		Tasks assigned = null;
		//Should check that node has the necessary file if possible.
		for(Tasks t: queueTasks)
		{
			List<String> fileInputs = t.in;
			boolean hasIt = true;
			for(String file: fileInputs )
			{
				ArrayList<String> slaveNodes= nameNode.findFile(file);
				if(!slaveNodes.contains(slaveToAddress.get(slaveNum)))
				{
					hasIt = false;
					break;
				}
			}
			if(hasIt)
			{
				assigned = t;
				break;
			}	
		}
		numberOfOutgoingJobs++;
		if(assigned !=null)
		{
			
			Message msg = new Message();
			msg.setTask(assigned);
			System.out.println(assigned.in);
			msg.setType('t');
			MasterConnection mc = connections.get(slaveNum);
			mc.sendMessage(msg);
			issueNextTask();
		}
		else{
			String slave = slaveToAddress.get(slaveNum);
			assigned = queueTasks.get(0);
			for (String file: assigned.in)
			{
				int sourceNum = -1;
				String addr = nameNode.require(file,slave);
				Iterator<Entry<Integer,String>> it = slaveToAddress.entrySet().iterator();
				while(it.hasNext()) {
					Map.Entry<Integer,String> entry = (Map.Entry<Integer,String>)it.next();
					 if(entry.getValue().equals(addr)) {
						 sourceNum = entry.getKey();
					 }
				}
				Message msg = new Message();
				msg.setType('f');
				msg.setFileName(file);
				msg.setAddr(addr);
				MasterConnection mc = connections.get(sourceNum);
				mc.sendMessage(msg);
			}
			issueNextTask();
		}
	}
	
	public void acknowledgeRunningTask(Tasks t)
	{
		Jobs job = t.getJob();
		int check = job.updateTasks(t);
		if(check == 2)
		{
			queueJobs.remove(job);
			runningJobs.add(job);		
		}
		int SlaveID = t.getSlaveID();
		slaveToTasks.get(SlaveID).add(t);
		numberOfOutgoingJobs--;
	}
	
	public void acknowledgeFinishedTask(Tasks t)
	{
		int SlaveID = t.getSlaveID();
		slaveToTasks.get(SlaveID).remove(t);
		
		
		Jobs job = t.getJob();
		int check = job.updateTasks(t);
		if(check == 0)
		{
			runningJobs.remove(job);
			finishedJobs.add(job);		
		}
		if(check == 3)
		{
			queueTasks.addAll(job.getQueueTasks());
		}
		if(check == -1)
		{
			queueTasks.add(t);
		}
		if(check == -2)
		{
			processKill(job);
		}
		issueNextTask();
	}	
}
