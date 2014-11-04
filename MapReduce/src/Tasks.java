
public abstract class Tasks {
	private int stat;
	private int slave;
	private int job;
	
	public int getStatus()
	{
		return stat;
	}
	public void setStatus(int status)
	{
		stat = status;
	}
	public Integer getSlaveID()
	{
		return slave;
	}
	public void setSlaveID(int slaveID)
	{
		slave=slaveID;
	}
	public Integer getJobID()
	{
		return job;
	}
	public void setJobID(int jobID)
	{
		job=jobID;
	}
}
