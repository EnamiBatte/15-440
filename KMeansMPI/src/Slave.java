import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


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
	
	
	
	

}
