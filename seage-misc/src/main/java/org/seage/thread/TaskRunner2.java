package org.seage.thread;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskRunner2
{
    private static Logger _logger = LoggerFactory.getLogger(TaskRunner2.class.getName());

    public static void run(Runnable[] tasks, int processors) throws InterruptedException
    {
        List<Task2> taskList = new ArrayList<Task2>();
        for (int i = 0; i < tasks.length; i++)
            taskList.add(new SimpleTask2(tasks[i]));
        run(taskList, processors);
    }

    public static void run(Task2[] tasks, int processors) throws InterruptedException
    {
        List<Task2> taskList = new ArrayList<Task2>();
        for (int i = 0; i < tasks.length; i++)
            taskList.add(tasks[i]);
        run(taskList, processors);
    }

    protected static void run(List<Task2> tasks, int processors) throws InterruptedException
    {
        _logger.trace("started");
        List<Task2> taskList = new ArrayList<Task2>(tasks);
        CpuCoreCounter counter = new CpuCoreCounter(processors);

        while (!taskList.isEmpty())
        {
            synchronized (counter)
            {
                if (counter.canUseCores(taskList.get(0).getCoresNeeded()))
                {
                    Task2 t = taskList.remove(0);
                    counter.useCores(t.getCoresNeeded());
                    _logger.trace("starting " + t.getName());
                    new Thread(new WorkerThread(counter, t)).start();

                    if (!counter.isCpuBusy())
                    {
                        _logger.trace("adding other tasks");
                        List<Task2> tasksToRemove = new ArrayList<Task2>();
                        for (int i = 0; i < taskList.size(); i++)
                        {
                            if (counter.canUseCores(taskList.get(i).getCoresNeeded()))
                            {
                                tasksToRemove.add(taskList.get(i));
                                Task2 t2 = taskList.get(i);
                                counter.useCores(t2.getCoresNeeded());
                                _logger.trace("starting " + t2.getName());
                                new Thread(new WorkerThread(counter, t2)).start();
                            }

                        }
                        for (int i = 0; i < tasksToRemove.size(); i++)
                        {
                            taskList.remove(tasksToRemove.get(i));
                        }
                    }
                }
                _logger.trace("waiting " + counter.CoresCurrentlyUsed);
                _logger.trace("------- ");
                counter.wait();
            }
        }
        synchronized (counter)
        {
            while (counter.CoresCurrentlyUsed > 0)
                counter.wait();
        }

        _logger.trace("finished");
    }

    static class WorkerThread implements Runnable
    {
        private CpuCoreCounter _counter;
        private Task2 _task;

        public WorkerThread(CpuCoreCounter counter, Task2 t)
        {
            _counter = counter;
            _task = t;
        }

        @Override
        public void run()
        {
            _logger.trace(
                    "running " + _task.getName() + ": " + _task.getCoresNeeded() + " - " + _counter.CoresCurrentlyUsed);

            long t1 = System.currentTimeMillis();
            _task.run();
            long t2 = System.currentTimeMillis();

            synchronized (_counter)
            {
                _logger.trace("notifying " + _task.getName() + " after " + (t2 - t1) + "ms");
                _counter.releaseCores(_task.getCoresNeeded());
                _counter.notify();
            }
        }
    }

    static class CpuCoreCounter
    {
        public int CoreCount;
        public int CoresCurrentlyUsed;

        public CpuCoreCounter(int cores)
        {
            CoreCount = cores;
            CoresCurrentlyUsed = 0;
        }

        public void useCores(int cores)
        {
            CoresCurrentlyUsed += cores;
        }

        public void releaseCores(int cores)
        {
            CoresCurrentlyUsed -= cores;
        }

        public boolean canUseCores(int cores)
        {
            return (CoresCurrentlyUsed + cores <= CoreCount) || (cores > CoreCount && CoresCurrentlyUsed == 0);
        }

        public boolean isCpuBusy()
        {
            return CoresCurrentlyUsed >= CoreCount;
        }
    }
}

abstract class Task2 implements Runnable
{
    private static int _id = 1;
    private int _coresNeeded;
    private String _name;

    public Task2(int coresNeeded)
    {
        _coresNeeded = coresNeeded;
        _name = "T" + _id++ + "(" + getCoresNeeded() + ")";
    }

    public int getCoresNeeded()
    {
        return _coresNeeded;
    }

    public String getName()
    {
        return _name;
    }
}

class SimpleTask2 extends Task2
{
    private Runnable _task;

    public SimpleTask2(Runnable task)
    {
        super(1);
        _task = task;
    }

    @Override
    public void run()
    {
        _task.run();

    }

}
