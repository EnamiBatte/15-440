package util;

import java.io.*;
import java.util.List;

public abstract class Tasks implements Serializable {
	public List<String> in;
	public List<String> fileout;
	private int stat;
	private int slave;
	private Jobs job;
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
	public Jobs getJob()
	{
		return job;
	}
	public void setJob(Jobs jobID)
	{
		job=jobID;
	}
}
