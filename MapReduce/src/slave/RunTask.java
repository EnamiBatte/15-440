package slave;
import util.*;
import example.*;


public abstract class RunTask implements Runnable{
	protected volatile boolean run;
	protected Tasks task;
	protected SlaveCoordinator coord;
	
	public void stop()
	{
		run = false;
		Message msg = new Message();
		task.setStatus(0);
		msg.setTask(task);
		msg.setType('f');
		coord.conn.sendMessage(msg);
	}
	public void finish()
	{
		run = false;
	}
}
