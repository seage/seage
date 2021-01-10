package org.seage.thread;

import java.util.List;

public interface ITaskRunner {
  void runTasks(List<Task> tasks) throws Exception;
}
