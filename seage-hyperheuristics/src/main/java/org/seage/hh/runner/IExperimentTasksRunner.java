package org.seage.hh.runner;

import java.util.List;
import java.util.function.Function;
import org.seage.hh.experiment.ExperimentTaskRequest;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;

public interface IExperimentTasksRunner {
  void performExperimentTasks(
      List<ExperimentTaskRequest> tasks, Function<ExperimentTaskRecord, Void> reportFn);
}
