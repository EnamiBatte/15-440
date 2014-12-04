import java.io.Serializable;
import java.util.List;


public class Message implements Serializable {
	private char type;
	private char task;
	private List<Datapoint> centroids;
	private List<Integer> assignments;
	private List<Datapoint> points;
	private List<List<Datapoint>> groupPoints;
	
	
	public void setType(char t)
	{
		type = t;
	}
	public char getType()
	{
		return type;
	}
	public void setTask(char t)
	{
		task = t;
	}
	public char getTask()
	{
		return task;
	}

	public void setCentroids(List<Datapoint> centr)
	{
		centroids = centr;
	}
	public List<Datapoint> getCentroids()
	{
		return centroids;
	}
	
	public void setAssignments(List<Integer> assign)
	{
		assignments = assign;
	}
	public List<Integer> getAssignments()
	{
		return assignments;
	}

	public void setPoints(List<Datapoint> p)
	{
		points = p;
	}
	public List<Datapoint> getPoints()
	{
		return points;
	}
	
	public void setGroup(List<List<Datapoint>> p)
	{
		groupPoints = p;
	}
	public List<List<Datapoint>> getGroup()
	{
		return groupPoints;
	}
	
	
}
