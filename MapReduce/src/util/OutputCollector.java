package util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class OutputCollector<T1, T2> {
	private List<Pair> results;
	
	public OutputCollector()
	{
		results = new LinkedList<Pair>();
	}
	
	public void collect(T1 k, T2 v)
	{
		Pair next = new Pair(k,v);
		results.add(next);
	}
	
	public List<Pair> getResults()
	{
		return results;
	}
}
