package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MasterController {
	public static boolean running = true;
	public static BufferedReader reader;
	
	public static void stopMaster()
	{
		running = false;
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	
	public static void main(String[] args) {
		MasterCoordinator mc = new MasterCoordinator();
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		while (running) {
			try {
				String input = reader.readLine();
				String[] jobArgs = input.split(" ");
				String arg = jobArgs[0];
				if(arg.equals("print"))
				{
					String arg2= jobArgs[1];
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
					int arg2= Integer.parseInt(jobArgs[1]);
					mc.kill(arg2);
					}
					catch (NumberFormatException e)
					{
						System.out.println("Kill not called on a valid job.");
					}
				}
				else{
					
				}
			}catch(IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
