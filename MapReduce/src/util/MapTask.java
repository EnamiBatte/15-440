package util;

import java.io.RandomAccessFile;
import java.util.List;


public abstract class MapTask extends Tasks {
	public List<RandomAccessFile> in;
	public String fileout;
	
	public abstract void map(String key, String value, OutputCollector<String,String> output);
}
