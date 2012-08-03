package org.seage.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskRunner
{
	private static Logger _logger = Logger.getLogger(TaskRunner.class.getName());

	List<Runnable> _taskQueue;

	public void runTasks(List<Runnable> taskQueue, int nrOfThreads)
	{
		_taskQueue = Collections.synchronizedList(new ArrayList<Runnable>(taskQueue));

		List<Thread> runnerThreads = new ArrayList<Thread>();

		for (int i = 0; i < nrOfThreads; i++)
		{
			Thread t = new Thread(new RunnerThread());
			runnerThreads.add(t);
			t.start();
		}

		// Wait for each thread
		for (int i = 0; i < nrOfThreads; i++)
		{
			try
			{
				runnerThreads.get(i).join();
			}
			catch (InterruptedException e)
			{
				_logger.log(Level.FINER, e.getMessage());
			}
		}
	}

	private class RunnerThread implements Runnable
	{
		@Override
		public void run()
		{
			while (!_taskQueue.isEmpty())
			{
				Thread t = new Thread(_taskQueue.remove(0));
				_logger.log(Level.FINER, t.toString());
				t.start();

				try
				{
					t.join();
				}
				catch (InterruptedException e)
				{
					_logger.log(Level.WARNING, e.getMessage());
				}
			}
		}

	}
}
