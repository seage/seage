package org.seage.hh.runner;

import java.util.List;
import java.util.function.Function;
import org.seage.hh.experiment.ExperimentTaskRequest;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalExperimentTasksRunner implements IExperimentTasksRunner {
  private static Logger log = LoggerFactory.getLogger(
      LocalExperimentTasksRunner.class.getName());

  @Override
  public void performExperimentTasks(
      List<ExperimentTaskRequest> taskInfos, Function<ExperimentTaskRecord, Void> reportFn) {

    taskInfos.parallelStream().forEach(ti -> {
      try {
        ExperimentTaskRecord task = new ExperimentTaskRecord(ti);
        task.run();
        reportFn.apply(task);
      } catch (Exception e) {
        log.error("Experiment task execution failed!", e);
      }
    });

  }

}
