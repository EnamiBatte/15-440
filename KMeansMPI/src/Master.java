import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;


public class Master {
	
	
	public static List<Integer> assign(List<Datapoint> input, List<Datapoint> centroids, char c)
	{
		int chunks = 1;
		int k = 0;
		Object[][] resps = new Object[chunks][1];
		List<Integer> results = new LinkedList<Integer>();
		for(int i = 0; i< chunks; i++)
		{
			List<Datapoint> chunkChoice = new LinkedList<Datapoint>();
			for(int j = 0; j<input.size()/i; j++)
			{
				chunkChoice.add(input.get(k+j));
			}
			k+= input.size()/i;
			Message msg = new Message();
			msg.setType(c);
			msg.setTask('a');
			msg.setCentroids(centroids);
			msg.setPoints(chunkChoice);
			Message[] buf = new Message[1];
			//MPI.COMM_WORLD.Isend(buf, 0, 1, MPI.OBJECT, i, chunks);
		}
		for(int i = 0; i< chunks; i++)
		{
			//MPI.COMM_WORLD.Recv(resps[i], 0, 1, MPI.OBJECT, i, MPI.ANY_TAG);
			results.addAll(((Message)resps[i][0]).getAssignments());
		}
		return results;
	}
	
	public static List<Datapoint> newMeans(List<Datapoint> input, List<Integer> assignments, char c)
	{
		return null;
	}
	
	
	public static List<Datapoint> doKMeans(List<Datapoint> input, List<Datapoint> centriods, char c)
	{
		List<Datapoint> centroids = centriods;
		List<Integer> bs = new LinkedList<Integer>();
		List<Integer> as = assign(input,centroids,c);
		
		//Now need to merge the responses assignments to get as
		
		while(!endPoint(as,bs))
		{
			bs = as;
			centroids = newMeans(input,as,c);
			as = assign(input,centroids,c);
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

	public static void run(String[] args) throws Exception {
		if(args.length < 1)
		{
			System.out.println("Help");
			return;
		}
		else{
			//args[0] = dna or coord
			//args[1] = seq or distr
			FileInputStream inputStream = new FileInputStream(args[2]);
			BufferedReader read = new BufferedReader(new InputStreamReader(inputStream));
			int numClusters = Integer.parseInt(args[3]);
			//Assignments
			List<Datapoint> input = new LinkedList<Datapoint>();
			List<Datapoint> centriods = new LinkedList<Datapoint>();
			ClusterUtil cd;

			
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
			
			List<Datapoint> output = doKMeans(input, centriods, 'c');
			for (Datapoint d : output) {
				System.out.println(d.getValue());
			}
		}	
		
		
	}

}
