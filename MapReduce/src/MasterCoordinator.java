import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class MasterCoordinator {

	public Map<Integer,InetAddress> slaveToAddress;
	public Map<Integer,List<Tasks>> slaveToTasks;
	public Map<Integer,Jobs> jobIDtoJobs;
	public List<Jobs> runningJobs;
	public List<Jobs> queueJobs;
	public List<Jobs> finishedJobs;
	public List<Tasks> queueTasks;
	public boolean SystemUp = false;

	public MasterCoordinator()
	{
		slaveToAddress = new HashMap<Integer,InetAddress>();
		slaveToTasks = new HashMap<Integer,List<Tasks>>();
		jobIDtoJobs = new HashMap<Integer,Jobs>();
		runningJobs = new LinkedList<Jobs>();
		queueJobs = new LinkedList<Jobs>();
		finishedJobs = new LinkedList<Jobs>();
		queueTasks = new LinkedList<Tasks>();
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
			//For each Slave Node startNode
			SystemUp = true;
		}
		
	}
	public void startNode(int nodeID)
	{
		
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
