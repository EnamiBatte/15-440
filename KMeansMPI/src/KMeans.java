import java.util.*;

public class KMeans {
	public static void main(String[] args) {
		List<Datapoint> input = new LinkedList<Datapoint>();
		List<Datapoint> centriods = new LinkedList<Datapoint>();
		int numOfInput = 10;
		int numOfCluster = 2;
		CoordUtil cd = new CoordUtil();
		
		input.add(new Coordinate(1.1, 2.1));
		input.add(new Coordinate(1.2, 2.2));
		input.add(new Coordinate(1.3, 2.3));
		input.add(new Coordinate(0.8, 2.4));
		input.add(new Coordinate(1, 1.9));
		input.add(new Coordinate(11.1, -2.1));
		input.add(new Coordinate(11.2, -2.2));
		input.add(new Coordinate(11.3, -2.3));
		input.add(new Coordinate(10.8, -2.4));
		input.add(new Coordinate(11.0, -1.9));
		centriods.add(new Coordinate(0, 0));
		centriods.add(new Coordinate(5, 5));
		
		List<Integer> as = assign(input, centriods);
		List<Integer> bs = new LinkedList<Integer>();

		while (!as.equals(bs)) {
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
			as = assign(input, centriods);	
		}
		
		for (int i = 0; i < numOfInput; i++) {
			System.out.println(bs.get(i));
		}
	}
	
	public static List<Integer> assign(List<Datapoint> points, List<Datapoint> centriods) {
		List<Integer> ret = new ArrayList<Integer>();
		CoordUtil cd = new CoordUtil();
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
	
}
