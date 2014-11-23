package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class RecordReader {
	
	private List<RandomAccessFile> fileReader;
	private int recordLength;
	private boolean ismap;
	
	public RecordReader(List<RandomAccessFile> f, int length, boolean t)
	{
		fileReader = f;
		recordLength = length;
		ismap = t;
	}
	
	public Map<String,List<String>> getKeyValuePairs () {
		System.out.println("Getting Key Value Pairs");
		Map<String,List<String>> pairs = new HashMap<String,List<String>>();
		int numLines;
		if(ismap){
			numLines = Configuration.numberOfLines;
		}
		else{
			numLines = 999;
		}
		try {
			for(RandomAccessFile reader: fileReader)
			{
				System.out.print("number of lines is" + numLines);
				if(ismap){
					numLines = Configuration.numberOfLines;
					for(int i = 0; i < numLines; i++)
					{	
						String record = reader.readLine();
						List<String> values = pairs.get(record);
						if(values == null)
						{
							values = new LinkedList<String>();
						}
						values.add(record);
						pairs.put(record,values);
					}
				}
				else{
					String record = reader.readLine();
					while(record != null){
						int index = record.indexOf("|");
						String key = record.substring(0,index);
						String value = record.substring(index+1,record.length());
						List<String> values = pairs.get(key);
						if(values == null)
						{
							values = new LinkedList<String>();
						}
						values.add(value);
						pairs.put(key,values);
					}
				}
			}
			System.out.println("Got Key Value Pairs");
			return pairs;
			
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
