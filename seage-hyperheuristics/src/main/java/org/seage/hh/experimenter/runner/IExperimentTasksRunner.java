package org.seage.hh.experimenter.runner;

import java.util.List;
import java.util.function.Function;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.ExperimentTask;

public interface IExperimentTasksRunner {
  List<DataNode> performExperimentTasks(
      List<ExperimentTask> tasks, Function<ExperimentTask, Void> reportFn);
}
