package org.seage.thread;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class TaskRunnerExTest
{
    @BeforeClass
    public static void aa()
    {
        //LogHelper.setConsoleDefaultFormatter(_logger, Level.ALL);
    }

    @Test
    public void testTaskRunnerEx() throws InterruptedException
    {
        TaskRunnerEx _taskRunner = new TaskRunnerEx(4);

        List<Task> taskList = new ArrayList<Task>();
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

        _taskRunner.run(taskList.toArray(new Task[] {}));
    }
}

class TestTask extends Task
{
    private int _length;

    public TestTask(int coresNeeded)
    {
        super(coresNeeded);
        _length = 5 + (int) (Math.random() * 10);
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
            e.printStackTrace();
        }

    }
}
