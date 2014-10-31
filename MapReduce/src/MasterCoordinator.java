import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class MasterCoordinator {

	public Map<Integer,InetAddress> slaveToAddress;
	public Map<Integer,List<Tasks>> slaveToTasks;
	public Map<Integer,List<Jobs>> jobIDtoJobs;
	public List<Jobs> runningJobs;
	public List<Jobs> queueJobs;
	public List<Jobs> finishedJobs;
	public List<Tasks> queueTasks;

	public MasterCoordinator()
	{
		slaveToAddress = new HashMap<Integer,InetAddress>();
		slaveToTasks = new HashMap<Integer,List<Tasks>>();
		jobIDtoJobs = new HashMap<Integer,List<Jobs>>();
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
	
}
