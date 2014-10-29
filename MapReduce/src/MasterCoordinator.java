import java.net.InetAddress;
import java.util.List;
import java.util.Map;


public class MasterCoordinator {

	public Map<Integer,InetAddress> slaveToAddress;
	public Map<Integer,List<Jobs>> slaveToJobs;
	public List<Jobs> queueJobs;
	public List<Jobs> finishedJobs;

	public MasterCoordinator()
	{
		
	}
}
