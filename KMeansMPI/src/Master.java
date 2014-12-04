import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
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

	public static void run(String[] args) throws Exception {
		if(args.length < 1)
		{
			System.out.println("Help");
			return;
		}
		else{
			//args[0] = dna or coord
			//args[1] = seq or distr
			FileInputStream input = new FileInputStream(args[2]);
			BufferedReader read = new BufferedReader(new InputStreamReader(input));
			int numClusters = Integer.parseInt(args[3]);
			//Assignments
			
			
			
		}	
		
		
	}

}
