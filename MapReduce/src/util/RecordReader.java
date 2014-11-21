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
		Map<String,List<String>> pairs = new HashMap<String,List<String>>();
		byte[] b = new byte[recordLength]; 
		int numLines = Configuration.mapPartitionSize;
		try {
			for(RandomAccessFile reader: fileReader)
			{
				for(int i = 0; i < numLines; i++)
				{
					int bytesread = reader.read(b);
					if(bytesread < recordLength)
						return pairs;
					String record = new String(b);
					if(ismap)
					{
						List<String> values = pairs.get(record);
						if(values == null)
						{
							values = new LinkedList<String>();
						}
						values.add(record);
						pairs.put(record,values);
					}
					else
					{
						int index = record.indexOf("|");
						String key = record.substring(0,index).trim();
						String value = record.substring(index+1,recordLength-1).trim();
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
			return pairs;
			
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
