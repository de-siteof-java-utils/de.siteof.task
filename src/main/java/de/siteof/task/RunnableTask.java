package de.siteof.task;

public class RunnableTask extends AbstractTask {
	
	private final Runnable runnable;
	
	public RunnableTask(Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public void execute() throws Exception {
		runnable.run();
	}

}
