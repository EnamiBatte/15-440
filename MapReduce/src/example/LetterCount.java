package example;

import java.util.List;

import util.Jobs;
import util.OutputCollector;

public class LetterCount extends Jobs {

	@Override
	public int getRecordSize() {
		return 1;
	}

	@Override
	public void map(String key, String value,
			OutputCollector<String, String> output) {
		for(Character c: key.toCharArray())
		{
			if(Character.isLetter(c))
				output.collect(String.valueOf(c), String.valueOf(1));
		}
	}

	@Override
	public void reduce(String key, List<String> values,
			OutputCollector<String, String> output) {
		output.collect(key, String.valueOf(values.size()));
		
	}
	
}
