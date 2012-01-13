package de.siteof.task;

import java.awt.EventQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SwingTaskManager implements ITaskManager {

	private static final Log log	= LogFactory.getLog(SwingTaskManager.class);

	private static ITaskManager instance = new SwingTaskManager();

	public static ITaskManager getInstance() {
		return instance;
	}

	public void addTask(final ITask task) {
		if (!EventQueue.isDispatchThread()) {
			if (log.isDebugEnabled()) {
				log.debug("queueing runnable");
			}
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						task.execute();
					} catch (Exception e) {
						log.error("failed to run task - " + e, e);
					}
				}
			});
		} else {
			if (log.isDebugEnabled()) {
				log.debug("executing runnable");
			}
			try {
				task.execute();
			} catch (Exception e) {
				log.error("failed to run task - " + e, e);
			}
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
