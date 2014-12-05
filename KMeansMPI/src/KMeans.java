import java.util.*;



public class KMeans {
	public static void main(String[] args) {
		List<Datapoint> input = new LinkedList<Datapoint>();
		List<Datapoint> centriods = new LinkedList<Datapoint>();
		ClusterUtil cd;

		
		cd = new CoordUtil();
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
		/*
		cd = new DNAUtil();
		input.add(new DNAStrand("AAAAAA"));
		input.add(new DNAStrand("ATTAAA"));
		input.add(new DNAStrand("AAAGAA"));
		input.add(new DNAStrand("CAAAAA"));
		input.add(new DNAStrand("GAGCGG"));
		input.add(new DNAStrand("GGTCGG"));
		input.add(new DNAStrand("GATCGG"));
		input.add(new DNAStrand("GATAGG"));
		input.add(new DNAStrand("GATCCG"));
		input.add(new DNAStrand("CCAATA"));
		input.add(new DNAStrand("CCAATT"));
		input.add(new DNAStrand("CCAATG"));
		centriods.add(new DNAStrand("AAAAAA"));
		centriods.add(new DNAStrand("CCCATC"));
		centriods.add(new DNAStrand("GGGGGG"));
		*/
		
		
		List<Datapoint> output = doKMeans(input, centriods, cd);
		for (Datapoint d : output) {
			System.out.println(d.getValue());
		}
	}
	
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
