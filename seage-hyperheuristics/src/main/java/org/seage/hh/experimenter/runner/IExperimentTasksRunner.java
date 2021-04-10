package org.seage.hh.experimenter.runner;

import java.util.List;
import java.util.function.Function;
import org.seage.hh.experimenter.ExperimentTask;
import org.seage.hh.experimenter.ExperimentTaskRequest;

public interface IExperimentTasksRunner {
  void performExperimentTasks(
      List<ExperimentTaskRequest> tasks, Function<ExperimentTask, Void> reportFn);
}
