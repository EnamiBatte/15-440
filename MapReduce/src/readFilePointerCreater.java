import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class readFilePointerCreater {
	private List<RandomAccessFile> mReader;
	private int splitNumber;
	public readFilePointerCreater(String filePath, Configuration config) {
		File file = new File(filePath);
		int length = config.length;
		this.mReader = new ArrayList<RandomAccessFile>();
		this.splitNumber = (int)Math.ceil(file.length() / length);
		for (int i = 0; i < this.splitNumber; i++) {
			RandomAccessFile tmp;
			try {
				tmp = new RandomAccessFile(filePath, "r");
				tmp.seek(i * length);
				mReader.add(tmp);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public int getSplitNumber() {
		return this.splitNumber;
	}
	public RandomAccessFile getReader(int index) {
		//index start from 0 to splitNumber - 1
		return this.mReader.get(index); 
	}
}
