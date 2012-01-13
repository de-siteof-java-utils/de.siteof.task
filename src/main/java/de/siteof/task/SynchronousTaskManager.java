package de.siteof.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SynchronousTaskManager implements ITaskManager {

	private static final Log log	= LogFactory.getLog(SynchronousTaskManager.class);

	private static ITaskManager instance = new SynchronousTaskManager();

	public static ITaskManager getInstance() {
		return instance;
	}

	public void addTask(ITask task) {
		try {
			task.execute();
		} catch (Exception e) {
			log.error("failed to run task - " + e, e);
		}
	}

	public void start() {
	}

	public void stop() {
	}

	public boolean waitForTask(ITask task) {
		log.error(this.getClass().getName() + ".waitForTask(ITask) not implemented");
		return false;
	}

}
