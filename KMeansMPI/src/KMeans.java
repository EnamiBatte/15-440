import java.util.*;



public class KMeans {
	
	public static List<Datapoint> doKMeans(List<Datapoint> input, List<Datapoint> centriods, ClusterUtil cd) {
		List<Integer> as = assign(input, centriods, cd);
		List<Integer> bs = new LinkedList<Integer>();
		int numOfCluster = centriods.size();
		
		while (!endPoint(as, bs)) {
			bs = as;
			centriods = new LinkedList<Datapoint>();
			for (int i = 0; i < numOfCluster; i++) {
				List<Datapoint> tmp = new LinkedList<Datapoint>();
				for (int j = 0; j < as.size(); j++) {
					if (i == as.get(j)) {
						tmp.add(input.get(j));
					}
				}
				centriods.add(cd.getCentroid(tmp));
			}
			
			as = assign(input, centriods, cd);	
		}
		
		return centriods;
		
	}
	public static boolean endPoint(List<Integer> as, List<Integer> bs) {
		int maxDifference = 0;
		int ret = 0;
		if (as.size() != bs.size()) {
			return false;
		}
		for (int i = 0; i < as.size(); i++) {
			ret += as.get(i) ^ bs.get(i);
		}
		return (ret <= maxDifference);
	}
	
	public static List<Integer> assign(List<Datapoint> points, List<Datapoint> centriods, ClusterUtil cd) {
		List<Integer> ret = new ArrayList<Integer>();
		for (Datapoint p : points) {
			double distance = cd.getDistance(p, centriods.get(0));
			int index = 0;
			for (int i = 1; i < centriods.size(); i++) {
				if (distance > cd.getDistance(p, centriods.get(i))) {					
					index = i;
					distance = cd.getDistance(p, centriods.get(i));
				}
			}
			ret.add(index);
		}
		return ret;
	}
	
}
