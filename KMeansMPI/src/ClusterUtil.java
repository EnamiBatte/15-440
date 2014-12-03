import java.util.List;


public abstract class ClusterUtil {

	public abstract double getDistance(Datapoint a, Datapoint b);
	public abstract Datapoint getCentroid(List<Datapoint> cluster);
}
