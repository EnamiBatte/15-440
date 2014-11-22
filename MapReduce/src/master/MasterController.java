package master;


public class MasterController {
	//Start System
	//Stop System
	//Kill Job
	
	//Output Following
	//Queue Jobs
	//Running Jobs
	//Where Job
	
	
	//Check for Timeout
	//Attempt to Restart Communication
	//Send to Other Participant
	
	
	
	public static void main(String[] args) {
		MasterCoordinator mc = new MasterCoordinator();
		
		// TODO Auto-generated method stub
		String arg = args[0];
		if(arg.equals("print"))
		{
			String arg2= args[1];
			if(arg2.equals("queue"))
			{
				mc.printQueue();
			}
			else if(arg2.equals("finished"))
			{
				mc.printFinished();
			}
			else if(arg2.equals("running"))
			{
				mc.printRunning();
			}
			else{
				System.out.println("Unrecognized Print Command");
			}
		}
		else if(arg.equals("kill"))
		{
			//Error processing
			try{
			int arg2= Integer.parseInt(args[1]);
			mc.kill(arg2);
			}
			catch (NumberFormatException e)
			{
				System.out.println("Kill not called on a valid job.");
			}
		}
		else{
			
		}
		
	}

}
