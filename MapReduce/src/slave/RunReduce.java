package slave;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.*;
import example.*;


public class RunReduce extends RunTask {
	private ReduceTask reduce;
	private RecordReader reader;
	
	
	public RunReduce(ReduceTask r, SlaveCoordinator coordinator)
	{
		reduce = r;
		task = r;
		coord = coordinator;
		List<RandomAccessFile> readers = new LinkedList<RandomAccessFile>();
		for(String read: reduce.getInput())
			try {
				readers.add(new RandomAccessFile(read,"r"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		reader = new RecordReader(readers,reduce.recordlength,false);
	}
	@Override
	public void run() {
		
		while (run)
	    {
			//Figure out how to read input file
			//Evaluate it
			Map<String,List<String>> input = reader.getKeyValuePairs();
			OutputCollector<String,String> collect = new OutputCollector<String,String>();
			Set<String> keySet = input.keySet();
			for(String key: keySet)
			{
				List<String> values = input.get(key);
				reduce.getJob().reduce(key, values, collect);
			}
			List<Pair> results = collect.getResults();
			List<String> stringResults = new LinkedList<String>();
			for(Pair p : results)
			{
				stringResults.add(p.toString());
			}
			FileOutputStream out;
			
			try {
				Collections.sort(stringResults);
				
				out = new FileOutputStream(new File(reduce.getOutput().get(0)));
				BufferedWriter dw=new BufferedWriter(new OutputStreamWriter(out));
				for(String res: stringResults)
				{
					dw.append(res);
				}
				dw.flush();
				dw.close();
				out.close();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				reduce.setStatus(-1);
				Message msg = new Message();
				msg.setTask(reduce);
				msg.setType('f');
				coord.conn.sendMessage(msg);
				return;
			}
			reduce.setStatus(1);
			Message msg = new Message();
			msg.setTask(reduce);
			msg.setType('f');
			coord.conn.sendMessage(msg);
			
	    }
	}
}
