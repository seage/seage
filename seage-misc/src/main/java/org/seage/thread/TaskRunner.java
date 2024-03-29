package org.seage.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskRunner {
  private static Logger log = LoggerFactory.getLogger(TaskRunner.class.getName());

  ConcurrentLinkedQueue<Runnable> _taskQueue;

  public void runTasks(List<Runnable> taskQueue, int nrOfCores) {
    _taskQueue = new ConcurrentLinkedQueue<Runnable>(taskQueue);

    List<Thread> runnerThreads = new ArrayList<Thread>();

    for (int i = 0; i < nrOfCores; i++) {
      String threadName = "TaskRunner-" + (i + 1);
      Thread t = new Thread(new RunnerThread(threadName), threadName);
      runnerThreads.add(t);
      t.start();
    }

    // Wait for each thread
    for (int i = 0; i < nrOfCores; i++) {
      try {
        runnerThreads.get(i).join();
      } catch (InterruptedException e) {
        log.trace(e.getMessage());
        Thread.currentThread().interrupt();
      }
    }
  }

  private class RunnerThread implements Runnable {
    private String _threadName;

    public RunnerThread(String name) {
      _threadName = name;
    }

    @Override
    public void run() {
      int i = 1;
      while (!_taskQueue.isEmpty()) {
        Runnable task = _taskQueue.poll();
        if (task == null) {
          return;
        }
        Thread t = new Thread(task, _threadName + "-Job" + i++);
        log.trace(t.toString() + " started");
        t.start();

        try {
          t.join();
          log.trace(t.toString() + " finished");
        } catch (InterruptedException e) {
          log.warn(e.getMessage());
          Thread.currentThread().interrupt();
        }
      }
    }

  }
}
