import java.io.Serializable;


public class Message implements Serializable {
	
	private char type;
	private Tasks task;
	private Jobs job;
	
	public char getType()
	{
		return type;
	}
	public void setType(char t)
	{
		type = t;
	}
	public Tasks getTask()
	{
		return task;
	}
	public void setTask(Tasks ta)
	{
		task = ta;
	}
	public Jobs getJob()
	{
		return job;
	}
	public void setJob(Jobs j)
	{
		job = j;
	}

}
