import java.util.LinkedList;
import java.util.List;


public class DNAUtil extends ClusterUtil {

	@Override
	public double getDistance(Datapoint a, Datapoint b) {
		List<String> aVal = a.getValue();
		List<String> bVal = b.getValue();
		int i = 0;
		double result = 0;
		for(String aI: aVal)
		{
			String bI = bVal.get(i);
			if(aI.charAt(0)!=bI.charAt(0))
			{
				result+=1;
			}
			i++;
		}
		
		return result;
	}

	@Override
	public Datapoint getCentroid(List<Datapoint> cluster) {
		int length = cluster.get(0).size();
		int[][] dna = new int[4][length];
		String[] ret = new String[length];
		int[] max = new int[length];
		for (Datapoint dp : cluster) {
			List<String> str = dp.getValue();
			for (int i = 0; i < str.size(); i++) {
				switch (str.get(i).charAt(0)) {
				case 'A':
					dna[0][i]++;
					break;
				case 'T':
					dna[1][i]++;
					break;
				case 'G':
					dna[2][i]++;
					break;
				case 'C':
					dna[3][i]++;
				}
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < length; j++) {
				if (max[j] < dna[i][j]) {
					max[j] = dna[i][j];
					switch (i) {
					case '0':
						ret[j] = "A";
						break;
					case '1':
						ret[j] = "T";
						break;
					case '2':
						ret[j] = "G";
						break;
					case '3':
						ret[j] = "C";
					}
				}
			}
		}
		return Arrays.asList(ret);
	}

}
