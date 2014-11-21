package slave;
import util.*;


public abstract class RunTask implements Runnable{
	protected volatile Thread t;
	protected Tasks task;
	protected SlaveCoordinator coord;
	
	public void stop()
	{
		if(t!= null)
		{
			t = null;
		}
		Message msg = new Message();
		task.setStatus(0);
		msg.setTask(task);
		msg.setType('f');
		coord.sendMessage(msg);
	}
	public void finish()
	{
		if(t!= null)
		{
			t = null;
		}
	}
}
