package util;

import java.util.ArrayList;
import java.util.Arrays;

public class Configuration {
	public static final String Master_Address = "unix1.andrew.cmu.edu";
	public static final String[] slaveAddress = {"unix2.andrew.cmu.edu","unix3.andrew.cmu.edu"};
	public static final ArrayList<String> Slave_Addresses = new ArrayList(Arrays.asList(slaveAddress));
	public static final int slaveListenPort = 4451;
	public static final int[] masterListenPorts = {4651,6451};
	public static final int timeout = 600;
	public static final int length = 1000;
	//Need size of each map
	//Unix3 has 6 cores
	public static final int replication = 2;
	public static final int numberOfLines = 5;
	public static final int numberOfReducers = 2;
	public static final int maxTasksPerHost = 6;
	public static final int mapPartitionSize = 1;
	//public static final int mapRetries = 2;
	//public static final int reducePartitionSize = 2000;
	//Number of Reduces/Per Host
	//Other constants as needed
}
