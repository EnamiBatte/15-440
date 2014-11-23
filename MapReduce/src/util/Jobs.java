package util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.io.*;


public abstract class Jobs implements Serializable {
	private Integer stat;
	private int id;
	private List<Tasks> allTasks;
	private List<Tasks> queueTasks;
	private int numFailures;
	private String job;
	private String inputFile;
	private List<String> outFiles;
	
	
	//Status = 3 means it started
	//Status = 2 means it is running
	//Status = 1 means it finished successfully
	//Status = 0 means it finished due to a kill
	public Jobs()
	{
		allTasks = new LinkedList<Tasks>();
		queueTasks =  new LinkedList<Tasks>();
		numFailures = 0;
	}
	public int getStatus()
	{
		return stat;
	}
	public void setStatus(int status)
	{
		stat = status;
	}
	public List<Tasks> getTasks()
	{
		return allTasks;
	}
	public void setTasks(List<Tasks> taskList)
	{
		if(taskList!=null){
		for(Tasks t: taskList)
		{
			if(t.getStatus() < 3)
			{
				t.setStatus(3);
				queueTasks.add(t);
			}
			else{
				allTasks.add(t);
			}
		}
		System.out.println("This Job has " + queueTasks.size() +" in queue");
		System.out.println("This Job has " + allTasks.size() + " more tasks");
		}
		else{
			allTasks = null;
		}
	}
	public List<Tasks> getQueueTasks()
	{
		return queueTasks;
	}
	
	public Integer getID(){
		return id;
	}
	public void setID(int ID){
		id = ID;
	}
	public void setString(String j)
	{
		job = j;
	}
	public String toString()
	{
		return job;
	}
	public void setInputFile(String in)
	{
		inputFile = in;
	}
	public String getInputFile()
	{
		return inputFile;
	}
	public void setOutputFiles(List<String> out)
	{
		outFiles = out;
	}
	public List<String> getOutputFiles()
	{
		return outFiles;
	}
	
	
	public abstract int getRecordSize();
	public abstract void map(String key, String value, OutputCollector<String,String> output);
	public abstract void reduce(String key, List<String> values, OutputCollector<String,String> output);
}
