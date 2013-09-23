package org.seage.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;
import org.seage.logging.LogHelper;

public class TaskRunnerExTest
{
	private static Logger _logger = Logger.getLogger(TaskRunnerExTest.class.getName());
	@BeforeClass
	public static void aa()
	{
		LogHelper.setConsoleDefaultFormatter(_logger, Level.ALL);
	}
	
	@Test
	public void testTaskRunnerEx() throws InterruptedException
	{
		TaskRunnerEx _taskRunner = new TaskRunnerEx(4);
		
		List<Task> taskList= new ArrayList<Task>();
		taskList.add(new TestTask(1));
		taskList.add(new TestTask(10));
		taskList.add(new TestTask(3));
		taskList.add(new TestTask(5));
		taskList.add(new TestTask(1));
		taskList.add(new TestTask(5));
		taskList.add(new TestTask(1));
		taskList.add(new TestTask(1));
		taskList.add(new TestTask(1));
		taskList.add(new TestTask(10));
		taskList.add(new TestTask(1));
		taskList.add(new TestTask(1));
		taskList.add(new TestTask(1));
		taskList.add(new TestTask(1));
		taskList.add(new TestTask(1));
		
		_taskRunner.run(taskList.toArray(new Task[]{}));
	}
}

class TestTask extends Task
{
	private int _length;
	public TestTask(int coresNeeded)
	{
		super(coresNeeded);
		_length = 5+(int)(Math.random()*10);
		//System.out.println("creating " + getName() + " "+_length+"ms");
	}

	@Override
	public void run()
	{
		try
		{
			Thread.sleep(_length);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
}