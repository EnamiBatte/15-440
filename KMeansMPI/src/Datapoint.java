import java.io.Serializable;
import java.util.List;


public interface Datapoint extends Serializable {
	public List<String> getValue();
}
