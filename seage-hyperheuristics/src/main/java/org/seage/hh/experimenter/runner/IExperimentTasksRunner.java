package org.seage.hh.experimenter.runner;

import java.util.List;
import java.util.function.Function;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.ExperimentTask;
import org.seage.hh.experimenter.ExperimentTaskInfo;

public interface IExperimentTasksRunner {
  List<DataNode> performExperimentTasks(
      List<ExperimentTaskInfo> tasks, Function<ExperimentTask, Void> reportFn);
}
