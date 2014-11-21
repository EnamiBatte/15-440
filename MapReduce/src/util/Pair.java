package util;


public class Pair<T1,T2> {
	public T1 key;
	public T2 value;
	
	public Pair(T1 k,T2 v)
	{
		key = k;
		value = v;
	}
	
	public String toString()
	{
		return key.toString()+"|"+value.toString();
	}

}
