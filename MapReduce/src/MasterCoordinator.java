import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MasterCoordinator {

	public Map<Integer,String> slaveToAddress;
	public Map<Integer,List<Tasks>> slaveToTasks;
	public Map<Integer,Jobs> jobIDtoJobs;
	public List<Jobs> runningJobs;
	public List<Jobs> queueJobs;
	public List<Jobs> finishedJobs;
	public List<Tasks> queueTasks;
	public boolean SystemUp = false;
	public List<MasterConnection> connections;

	public MasterCoordinator()
	{
		slaveToAddress = new HashMap<Integer,String>();
		slaveToTasks = new HashMap<Integer,List<Tasks>>();
		jobIDtoJobs = new HashMap<Integer,Jobs>();
		runningJobs = new LinkedList<Jobs>();
		queueJobs = new LinkedList<Jobs>();
		finishedJobs = new LinkedList<Jobs>();
		queueTasks = new LinkedList<Tasks>();
		connections = new LinkedList<MasterConnection>();
		
		
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
		int listenPort = Configuration.masterListenPorts[nodeID];
		MasterConnection newCon = new MasterConnection(slaveToAddress.get(nodeID),listenPort);
		newCon.start();
		connections.add(newCon);
	}
	
	public void stopSystem()
	{
		
		//For each Job processKill
		//Close each node
	}
	//Called by stopSystem or in case of timeout
	public void closeNode(int nodeID)
	{
		
	}
	
	public void processKill(int id)
	{
		Jobs toKill = jobIDtoJobs.get(id);
		//Work
	}
	
	public void addJob()
	{
		
	}
	
	public void addTask()
	{
		
	}
	
	public void issueNextTask()
	{
		
	}
	
	public void acknowledgeRunningTask()
	{
		
	}
	
	public void acknowledgeFinishedTask()
	{
		
	}
	
}
