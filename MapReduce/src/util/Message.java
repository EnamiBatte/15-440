package util;

import java.io.Serializable;
import java.util.ArrayList;


public class Message implements Serializable {
	
	private char type;
	private Tasks task;
	private Jobs job;
	
	private String filename;
	private int lines;
	private String addr;
	private int partition;
	private ArrayList<String> addrList;
	private int port;
	
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
	public void setFileName(String f)
	{
		filename = f;
	}
	public String getFileName()
	{
		return filename;
	}
	public void setAddr(String a)
	{
		addr = a;
	}
	public String getAddr()
	{
		return addr;
	}
	public void setLines(int l)
	{
		lines = l;
	}
	public int getLines()
	{
		return lines;
	}
	public void setPartition(int p) 
	{
		partition = p;
	}
	public int getPartition() 
	{
		return partition;
	}	
	public void setAddrList(ArrayList<String> s)
	{
		addrList = s;
	}
	public ArrayList<String> getAddrList()
	{
		return addrList;
	}
	public void setPort(int p)
	{
		port = p;
	}
	public int getPort()
	{
		return port;
	}

}
