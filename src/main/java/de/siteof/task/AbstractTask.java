package de.siteof.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractTask implements ITask, Runnable {

	private static final Log log	= LogFactory.getLog(AbstractTask.class);


	public void run() {
		try {
			this.execute();
		} catch (Exception e) {
			log.error("failed to run task - " + e, e);
		}
	}

}
