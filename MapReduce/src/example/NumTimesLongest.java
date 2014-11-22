package example;

import java.util.ArrayList;
import java.util.List;

import util.Jobs;
import util.OutputCollector;

public class NumTimesLongest extends Jobs {

	@Override
	public int getRecordSize() {
		return 80;
	}

	@Override
	public void map(String key, String value,
			OutputCollector<String, String> output) {
		String[] words = key.split(" ");
		String maxWord = "";
		int max = 0;
		for (String w : words) {
			if(w.length()>max)
			{
				maxWord = w;
				max = w.length();
			}
		}
		output.collect(maxWord, String.valueOf(1));
	}

	@Override
	public void reduce(String key, List<String> values,
			OutputCollector<String, String> output) {
		output.collect(key, String.valueOf(values.size()));
	}

}
