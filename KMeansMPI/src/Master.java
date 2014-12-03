import java.util.List;


public class Master {
	
	public static List<Datapoint> doKMeans(List<Datapoint> input, List<Datapoint> centriods, ClusterUtil cd)
	{
		
		
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

}
