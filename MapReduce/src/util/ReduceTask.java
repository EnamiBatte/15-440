package util;

import java.io.RandomAccessFile;
import java.util.List;


public abstract class ReduceTask extends Tasks {
	public List<RandomAccessFile> in;
	public String fileout;
	
	public abstract void reduce(String key, List<String> values, OutputCollector<String,String> output);
}
