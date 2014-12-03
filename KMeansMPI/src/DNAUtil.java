import java.util.LinkedList;
import java.util.List;


public class DNAUtil extends ClusterUtil {

	@Override
	public double getDistance(Datapoint a, Datapoint b) {
		List<String> aVal = a.getValue();
		List<String> bVal = b.getValue();
		int i = 0;
		double result = 0;
		for(String aI: aVal)
		{
			String bI = bVal.get(i);
			if(aI.charAt(0)!=bI.charAt(0))
			{
				result+=1;
			}
			i++;
		}
		
		return result;
	}

	@Override
	public Datapoint getCentroid(List<Datapoint> cluster) {
		String res = "";
		List<String> resString = new LinkedList<String>();
		//Need Clean way to get Centroid
		return null;
	}

}
