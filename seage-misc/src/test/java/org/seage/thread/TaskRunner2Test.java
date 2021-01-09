package org.seage.thread;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TaskRunner2Test {
  @Test
  public void testTaskRunnerEx() throws InterruptedException {
    List<Task2> taskList = new ArrayList<Task2>();
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

    TaskRunner2.run(taskList.toArray(new Task2[] {}), 4);
  }
}

class TestTask extends Task2 {
  private int _length;

  public TestTask(int coresNeeded) {
    super(coresNeeded);
    _length = 5 + (int) (Math.random() * 10);
    // System.out.println("creating " + getName() + " "+_length+"ms");
  }

  @Override
  public void run() {
    try {
      Thread.sleep(_length);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }
}
