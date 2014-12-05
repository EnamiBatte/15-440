import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import mpi.*;
public class Master {
	
	
	public static List<Integer> assign(List<Datapoint> input, List<Datapoint> centroids, char c, int chunks) throws Exception
	{
		int k = 0;
		Object[][] resps = new Object[chunks][1];
		List<Integer> results = new ArrayList<Integer>();
		for(int i = 0; i< chunks; i++)
		{
			List<Datapoint> chunkChoice = new ArrayList<Datapoint>();
			for(int j = 0; j<input.size()/chunks; j++)
			{
				chunkChoice.add(input.get(k+j));
			}
			k+= input.size()/chunks;
			Message msg = new Message();
			msg.setType(c);
			msg.setTask('a');
			msg.setCentroids(centroids);
			msg.setPoints(chunkChoice);
			Message[] buf = new Message[1];
			buf[0]=msg;
			MPI.COMM_WORLD.Isend(buf, 0, 1, MPI.OBJECT, i+1, chunks);
		}
		for(int i = 0; i< chunks; i++)
		{
			MPI.COMM_WORLD.Recv(resps[i], 0, 1, MPI.OBJECT, i+1, MPI.ANY_TAG);
			results.addAll(((Message)resps[i][0]).getAssignments());
		}
		return results;
	}
	
	public static List<Datapoint> newMeans(List<Datapoint> input, List<Integer> assignments, int centroidSize, char c, int chunks) throws Exception
	{
		int k = centroidSize/chunks;
		System.out.println("Size of " + k);
		Object[][] resps = new Object[chunks][1];
		List<List<List<Datapoint>>> allChunks = new ArrayList<List<List<Datapoint>>>();
		for(int j = 0; j<chunks; j++)
		{
			List<List<Datapoint>> chunkGroup = new ArrayList<List<Datapoint>>();
			for(int i = 0; i < k; i++)
			{
				List<Datapoint> group = new ArrayList<Datapoint>();
				chunkGroup.add(group);
			}
			if(j == chunks -1)
			{
				int l = 0;
				while(j*k+l < centroidSize)
				{
					chunkGroup.add(new ArrayList<Datapoint>());
					l++;
				}
			}
			allChunks.add(chunkGroup);
		}
		int j = 0;
		for(Integer i : assignments)
		{
			if(i/k == k)
			{
				allChunks.get(chunks-1).get((i%k)+k).add(input.get(j));
			}
			else{
				((allChunks.get(i/k)).get(i%k)).add(input.get(j));
			}
			j++;
		}
		for(int i = 0; i < chunks; i++)
		{
			Message msg = new Message();
			msg.setType(c);
			msg.setTask('k');
			msg.setGroup(allChunks.get(i));
			Message[] buf = new Message[1];
			buf[0] = msg;
			MPI.COMM_WORLD.Isend(buf, 0, 1, MPI.OBJECT, i+1, chunks);
		}
		List<Datapoint> results = new ArrayList<Datapoint>();
		for(int i = 0; i< chunks; i++)
		{
			MPI.COMM_WORLD.Recv(resps[i], 0, 1, MPI.OBJECT, i+1, MPI.ANY_TAG);
			results.addAll(((Message)resps[i][0]).getCentroids());
		}
		return results;
	}
	
	
	public static List<Datapoint> doKMeans(List<Datapoint> input, List<Datapoint> centriods, char c,int size) throws Exception
	{
		List<Datapoint> centroids = centriods;
		List<Integer> bs = new ArrayList<Integer>();
		List<Integer> as = assign(input,centroids,c,size);
		
		//Now need to merge the responses assignments to get as
		
		while(!endPoint(as,bs))
		{
			bs = as;
			centroids = newMeans(input,as,centroids.size(),c,size);
			as = assign(input,centroids,c,size);
		}
		
		return centroids;
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

	public static void run(String[] args,int size) throws Exception {
		System.out.println("run Master");
		if(args.length < 1)
		{
			System.out.println("Help");
			System.out.println("First argument should be c or d");
			System.out.println("First argument should be s or p");
			return;
		}
		else{
			char c = args[0].charAt(0);
			char type = args[1].charAt(0);
			if(c=='c' || c=='d')
			{
				if(type =='s' || type =='p'){
					FileInputStream inputStream = new FileInputStream(args[2]);
					BufferedReader read = new BufferedReader(new InputStreamReader(inputStream));
					int numClusters = Integer.parseInt(args[3]);
					//Assignments
					List<Datapoint> input = new ArrayList<Datapoint>();
					List<Datapoint> centriods = new ArrayList<Datapoint>();
		
					List<Datapoint> output;
					ClusterUtil cd;
					
					String line;
					int count = 0;
					
					if(c == 'c')
					{
						cd = new CoordUtil();

						while ((line = read.readLine()) != null) {
							if (line.length() == 0) {
								continue;
							}
							float xVal = Float.parseFloat(line.split(",")[0]);
							float yVal = Float.parseFloat(line.split(",")[1]);
							input.add(new Coordinate(xVal, yVal));
							if (count++ < numClusters) {
								centriods.add(new Coordinate(xVal, yVal));
							}
								
						}
					}
					else
					{
						cd = new DNAUtil();
						while ((line = read.readLine()) != null) {
							if (line.length() == 0) {
								continue;
							}
							
							input.add(new DNAStrand(line));
							if (count++ < numClusters) {
								centriods.add(new DNAStrand(line));
							}
								
						}
					}
					read.close();
					inputStream.close();
					if(type == 's')
					{
						output = KMeans.doKMeans(input, centriods, cd);
					}
					else{
						output = doKMeans(input, centriods, c,size-1);
					}
					for (Datapoint d : output) {
						System.out.println(d.getValue());
					}
				}
			}
		}	
		
		
	}

}
