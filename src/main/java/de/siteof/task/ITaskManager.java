package de.siteof.task;

public interface ITaskManager {

	void addTask(ITask task);

	boolean waitForTask(ITask task);

	void start();

	void stop();

}
