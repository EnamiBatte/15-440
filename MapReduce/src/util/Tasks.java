package util;

public abstract class Tasks {
	private int stat;
	private int slave;
	private int job;
	public int recordlength;
	//Status = -1 means it failed due to error
	//Status = 2 means it is running
	//Status = 1 means it finished successfully
	//Status = 0 means it finished due to a kill
	
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
