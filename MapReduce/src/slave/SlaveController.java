package slave;

import master.MasterCoordinator;



public class SlaveController {

	//Initiate A MapReduce
	
	//Handle a Map
	//Compile Results
	//Receive commands from Master
	//Feed to a Reduce
	//Output
	
	public static void main(String[] args) {
		SlaveCoordinator mc = new SlaveCoordinator();
		mc.start();
		// TODO Auto-generated method stub
		String arg = args[0];
		
	}

}
