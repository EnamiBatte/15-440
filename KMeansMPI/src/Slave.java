import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mpi.*;


public class Slave {
	
	public static List<Integer> assign(List<Datapoint> points, List<Datapoint> centriods, ClusterUtil cd) {
		List<Integer> ret = new ArrayList<Integer>();
		for (Datapoint p : points) {
			double distance = cd.getDistance(p, centriods.get(0));
			int index = 0;
			for (int i = 1; i < centriods.size(); i++) {
				if (distance > cd.getDistance(p, centriods.get(i))) {					
					index = i;
				}
			}
			ret.add(index);
		}
		return ret;
	}
	
	public static List<Datapoint> doKMeans(List<List <Datapoint>> input, ClusterUtil cd) {
		List<Datapoint> centriods = new LinkedList<Datapoint>();
		for (List<Datapoint> cluster : input)
		{
			centriods.add(cd.getCentroid(cluster));
		}
		return centriods;
	}
	
	public static void run() throws Exception
	{
		while(true)
		{
			Object[] message = new Object[1];
			MPI.COMM_WORLD.Recv(message,0,1,MPI.OBJECT,0,MPI.ANY_TAG);
			Message msg = (Message)message[0];
			ClusterUtil cUtil;
			Message[] buf = new Message[1];
			Message retMsg = new Message();
			if(msg.getType()=='c')
			{
				cUtil = new CoordUtil();
				retMsg.setType('c');
			}
			else if(msg.getType()=='d')
			{
				cUtil = new DNAUtil();
				retMsg.setType('d');
			}
			else{
				return;
			}
			if(msg.getTask()=='a')
			{
				retMsg.setTask('a');
				List<Integer> assignments = assign(msg.getPoints(), msg.getCentroids(), cUtil);
				retMsg.setAssignments(assignments);
			}
			else if(msg.getTask()=='k')
			{
				retMsg.setTask('k');
				List<Datapoint> centroids = doKMeans(msg.getGroup(),cUtil);
				retMsg.setCentroids(centroids);
			}
			buf[0] = retMsg;
			MPI.COMM_WORLD.Send(buf,0,1,MPI.OBJECT,0,MPI.COMM_WORLD.Rank());
		}
	}
	
	

}
