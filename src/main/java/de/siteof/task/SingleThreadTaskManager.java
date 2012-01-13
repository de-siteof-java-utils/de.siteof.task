package de.siteof.task;


public class SingleThreadTaskManager extends ThreadPoolTaskManager {

	public SingleThreadTaskManager() {
		super(1);
	}

	/* (non-Javadoc)
	 * @see de.siteof.webpicturebrowser.task.ThreadPoolTaskManager#getThreadName(int)
	 */
	@Override
	protected String getThreadName(int threadNo) {
		return "SingleThread-" + threadNo;
	}

}
