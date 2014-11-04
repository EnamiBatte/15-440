
public class Configuration {
	public static final String Master_Address = "unix1.andrew.cmu.edu";
	public static final String[] Slave_Addresses = {"unix2.andrew.cmu.edu","unix3.andrew.cmu.edu"};
	public static final int port = 4451;
	public static final int timeout = 500;
	//Need size of each map
	//Unix3 has 6 cores
	public static final int maxMapsPerHost = 6;
	public static final int mapPartitionSize = 1;
	//public static final int reducePartitionSize = 2000;
	//Number of Reduces/Per Host
	//Other constants as needed
}
