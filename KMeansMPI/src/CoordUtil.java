import java.util.List;


public class CoordUtil extends ClusterUtil {
	
	public double getDistance(Datapoint a, Datapoint b)
	{
		List<String> aVal = a.getValue();
		List<String> bVal = b.getValue();
		double xDis = Double.valueOf(aVal.get(0))- Double.valueOf(bVal.get(0));
		double yDis = Double.valueOf(aVal.get(1)) - Double.valueOf(bVal.get(0));
		
		return Math.sqrt(xDis*xDis + yDis*yDis);
	}
	
	public Coordinate getCentroid(List<Datapoint> cluster)
	{
		int count = 0;
		double xCen = 0.0;
		double yCen = 0.0;
	
		for (Datapoint i: cluster)
		{
			List<String> aVal = i.getValue();
			xCen += Double.valueOf(aVal.get(0));
			yCen += Double.valueOf(aVal.get(1));
			count++;
		}
		
		return new Coordinate(xCen/count,yCen/count);
	}

}
