import java.util.List;


public interface Jobs {
	public int getStatus();
	public void setStatus(int status);
	public List<Tasks> getTasks();
	public void setTasks(List<Tasks> taskList);
	public List<Tasks> getQueueTasks();
	public int updateTasks(Tasks task);
	public Integer getID();
	public void setID(int ID);
}
