package org.seage.hh.experimenter.runner;

import java.util.List;
import java.util.function.Function;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalExperimentTasksRunner implements IExperimentTasksRunner {
  private static Logger logger = LoggerFactory.getLogger(
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
        logger.error("Experiment task execution failed!", e);
      }
    });

  }

}
