package de.siteof.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThreadPoolTaskManager implements ITaskManager {

	private class ThreadPoolThread extends Thread {

		private boolean stopped;
		private String baseName;

		public ThreadPoolThread(ThreadGroup group, String name) {
			super(group, name + " [starting]");
			this.baseName = name;
		}

		public void setStopped(boolean stopped) {
			if (this.stopped != stopped) {
				this.stopped	= stopped;
				this.interrupt();
			}
		}

		public void run() {
			try {
				this.setName(baseName + " [waiting]");
				while (!stopped) {
					ITask task	= null;
					synchronized(pendingTaskList) {
						if (pendingTaskList.isEmpty()) {
							pendingTaskList.wait();
						}
						if (!pendingTaskList.isEmpty()) {
							task	= (ITask) pendingTaskList.get(0);
							pendingTaskList.remove(0);
						}
					}
					if (task != null) {
						availableThreadCount.decrementAndGet();
						try {
							this.setName(baseName + " [executing]");
							task.execute();
						} catch (Throwable e) {
							log.error("failed to run task - " + e, e);
						}
						availableThreadCount.incrementAndGet();
						this.setName(baseName + " [available]");
					}
				}
				this.setName(baseName + " [stopped]");
			} catch (InterruptedException e) {
				log.debug("waiting for task to run interrupted - " + e, e);
				this.setName(baseName + " [interrupted]");
			}
		}

	}


	private int maxThreadCount;
	private List<ITask> pendingTaskList	= new LinkedList<ITask>();
	private List<ThreadPoolThread> threadList = new ArrayList<ThreadPoolThread>();
	private ThreadGroup threadGroup;
	private AtomicInteger availableThreadCount = new AtomicInteger();
	private int threadCounter;

	private static final Log log	= LogFactory.getLog(ThreadPoolTaskManager.class);


	public ThreadPoolTaskManager(int maxThreadCount) {
		this.maxThreadCount	= maxThreadCount;
	}


	public void addTask(ITask task) {
		int pendingTaskCount;
		synchronized(pendingTaskList) {
			pendingTaskList.add(task);
			pendingTaskList.notifyAll();
			pendingTaskCount = pendingTaskList.size();
//			pendingTaskList.notify();
			if (pendingTaskCount > availableThreadCount.get()) {
				startThread();
			}
		}
//		if (pendingTaskCount > availableThreadCount.get()) {
//			startThread();
//		}
	}

	protected String getThreadName(int threadNo) {
		return "ThreadPool-" + threadNo;
	}

	private void startThread() {
		synchronized(this) {
			if (this.threadList.size() >= maxThreadCount) {
				log.warn("max thread count reached");
			} else {
				if (threadGroup == null) {
					threadGroup = new ThreadGroup("websitemanager.tasks");
				}
				int i = threadCounter++;
				ThreadPoolThread t	= new ThreadPoolThread(threadGroup, getThreadName(i));
				this.threadList.add(t);
				t.start();
				availableThreadCount.incrementAndGet();
			}
		}
	}


	public void start() {
		stop();
	}


	public void stop() {
		List<ThreadPoolThread> previousThreadList;
		synchronized(this) {
			previousThreadList	= this.threadList;
			this.threadList	= new ArrayList<ThreadPoolThread>();
		}
		for (Iterator<ThreadPoolThread> it = previousThreadList.iterator(); it.hasNext(); ) {
			ThreadPoolThread t	= it.next();
			t.setStopped(true);
		}
	}


	public boolean waitForTask(ITask task) {
		log.error(this.getClass().getName() + ".waitForTask(ITask) not implemented");
		return false;
	}

}
