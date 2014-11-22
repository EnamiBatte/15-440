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
		if(Character.isLetter(key.charAt(0)))
			output.collect(key, String.valueOf(1));
	}

	@Override
	public void reduce(String key, List<String> values,
			OutputCollector<String, String> output) {
		output.collect(key, String.valueOf(values.size()));
		
	}
	
}
