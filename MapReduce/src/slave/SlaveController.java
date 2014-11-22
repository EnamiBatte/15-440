package slave;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import util.*;



public class SlaveController {
	public static boolean running = true;
	public static boolean init;
	public static BufferedReader reader;
	
	public static void initSlave()
	{
		init = true;
		System.out.println("System is started");
	}
	
	public static void stopSlave()
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
		SlaveCoordinator mc = new SlaveCoordinator();
		mc.start();
		reader = new BufferedReader(new InputStreamReader(System.in));
		while (running) {
			try {
				String input = reader.readLine();
				String[] jobArgs = input.split(" ");
				if(jobArgs[0].equals("start"))
				{
					int argsNeeded = 3 + Configuration.numberOfReducers;
					if(jobArgs.length<argsNeeded)
					{
						System.out.println("Format: start (exampleJob) (inputFile) (outputFiles...)");
						System.out.println("You should have an output file for each reducer");
						System.out.println("You have "+String.valueOf(Configuration.numberOfReducers) + " reducers set");
						continue;
					}
					String jobName = jobArgs[1];
					String inputFile = jobArgs[2];
					List<String> outFiles = new LinkedList<String>();
					for(int i = 3; i < jobArgs.length; i++)
					{
						outFiles.add(jobArgs[i]);
					}
					
					
					Jobs j = (Jobs)(Class.forName(jobName)).newInstance();
					j.setInputFile(inputFile);
					j.setString(jobName);
					j.setOutputFiles(outFiles);
					mc.addJobs(j);
					
				}
			} catch (IOException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
	}

}
