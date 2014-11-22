package slave;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dfs.*;
import util.*;
import example.*;


public class RunMap extends RunTask {
	private MapTask map;
	private RecordReader reader;
	
	
	public RunMap(MapTask m, SlaveCoordinator coordinator)
	{
		map=m;
		coord = coordinator;
		RandomAccessFile read;
		try {
			read = new RandomAccessFile((m.getInput()).get(0),"r");
			List<RandomAccessFile> readers = new LinkedList<RandomAccessFile>();
			readers.add(read);
			reader = new RecordReader(readers,m.recordlength,true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Running a map");
		while (run)
	    {
			//Figure out how to read input file
			//Evaluate it
			Map<String,List<String>> input = reader.getKeyValuePairs();
			if(input == null)
			{
				map.setStatus(-1);
				Message msg = new Message();
				msg.setTask(map);
				msg.setType('f');
				coord.conn.sendMessage(msg);
				return;
			}
			OutputCollector<String,String> collect = new OutputCollector<String,String>();
			Set<String> keySet = input.keySet();
			for(String key: keySet)
			{
				List<String> values = input.get(key);
				for(String value : values)
				{
					map.getJob().map(key, value, collect);
				}
			}
			List<Pair> results = collect.getResults();
			int length = results.size();
			FileOutputStream out;
			try {
				System.out.println("Trying to Write to File");
				out = new FileOutputStream(new File(map.getOutput().get(0)));
				BufferedWriter dw=new BufferedWriter(new OutputStreamWriter(out));
				for(Pair p: results)
				{
					dw.append(p.toString());
				}
				dw.flush();
				dw.close();
				out.close();
			}  catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				map.setStatus(-1);
				Message msg = new Message();
				msg.setTask(map);
				msg.setType('f');
				coord.conn.sendMessage(msg);
				return;
			}
			map.setStatus(1);
			Message msg = new Message();
			msg.setTask(map);
			msg.setType('f');
			coord.conn.sendMessage(msg);
			break;
	    }
	}
}
