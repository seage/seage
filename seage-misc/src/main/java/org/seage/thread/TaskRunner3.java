package org.seage.thread;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskRunner3 implements ITaskRunner {
  private static Logger _logger = LoggerFactory.getLogger(TaskRunner3.class.getName());
  private int _cores;

  public TaskRunner3(int cores) {
    this._cores = cores;
  }

  @Override
  public void runTasks(List<Task> tasks) throws Exception {
    _logger.trace("started");
    List<Task> taskList = new ArrayList<Task>(tasks);
    CpuCoreCounter counter = new CpuCoreCounter(this._cores);

    while (!taskList.isEmpty()) {
      synchronized (counter) {
        if (counter.canUseCores(1)) {
          Task t = taskList.remove(0);
          counter.useCores(1);
          _logger.trace("starting " + t.getName());
          new Thread(new WorkerThread(counter, t)).start();

          if (!counter.isCpuBusy()) {
            _logger.trace("adding other tasks");
            List<Task> tasksToRemove = new ArrayList<Task>();
            for (int i = 0; i < taskList.size(); i++) {
              if (counter.canUseCores(1)) {
                tasksToRemove.add(taskList.get(i));
                Task t2 = taskList.get(i);
                counter.useCores(1);
                _logger.trace("starting " + t2.getName());
                new Thread(new WorkerThread(counter, t2)).start();
              }

            }
            for (int i = 0; i < tasksToRemove.size(); i++) {
              taskList.remove(tasksToRemove.get(i));
            }
          }
        }
        _logger.trace("waiting " + counter.CoresCurrentlyUsed);
        _logger.trace("------- ");
        counter.wait();
      }
    }
    synchronized (counter) {
      while (counter.CoresCurrentlyUsed > 0) {
        counter.wait();
      }
    }

    _logger.trace("finished");

  }

  public static void run(Runnable[] tasks, int cores) throws Exception {
    List<Task> taskList = new ArrayList<Task>();
    for (int i = 0; i < tasks.length; i++) {
      taskList.add(new SimpleTask("", tasks[i]));
    }
    run(taskList, cores);
  }

  public static void run(Task[] tasks, int cores) throws Exception {
    List<Task> taskList = new ArrayList<Task>();
    for (int i = 0; i < tasks.length; i++) {
      taskList.add(tasks[i]);
    }
    run(taskList, cores);
  }

  protected static void run(List<Task> tasks, int cores) throws Exception {
    new TaskRunner3(cores).runTasks(tasks);
  }

  static class WorkerThread implements Runnable {
    private CpuCoreCounter _counter;
    private Task _task;

    public WorkerThread(CpuCoreCounter counter, Task t) {
      _counter = counter;
      _task = t;
    }

    @Override
    public void run() {
      _logger.trace("running " + _task.getName() + ": " + " - " + _counter.CoresCurrentlyUsed);

      long t1 = System.currentTimeMillis();
      _task.run();
      long t2 = System.currentTimeMillis();

      synchronized (_counter) {
        _logger.trace("notifying " + _task.getName() + " after " + (t2 - t1) + "ms");
        _counter.releaseCores(1);
        _counter.notifyAll();
      }
    }
  }

  static class CpuCoreCounter {
    public int CoreCount;
    public int CoresCurrentlyUsed;

    public CpuCoreCounter(int cores) {
      CoreCount = cores;
      CoresCurrentlyUsed = 0;
    }

    public void useCores(int cores) {
      CoresCurrentlyUsed += cores;
    }

    public void releaseCores(int cores) {
      CoresCurrentlyUsed -= cores;
    }

    public boolean canUseCores(int cores) {
      return (CoresCurrentlyUsed + cores <= CoreCount) || (cores > CoreCount && CoresCurrentlyUsed == 0);
    }

    public boolean isCpuBusy() {
      return CoresCurrentlyUsed >= CoreCount;
    }
  }
}

class SimpleTask extends Task {
  private Runnable _task;

  public SimpleTask(String name, Runnable task) {
    super(name);
    _task = task;
  }

  @Override
  public void run() {
    _task.run();

  }

}
