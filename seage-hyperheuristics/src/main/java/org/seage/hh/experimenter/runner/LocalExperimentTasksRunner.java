package org.seage.hh.experimenter.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.ExperimentTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalExperimentTasksRunner implements IExperimentTasksRunner {
  private static Logger logger = LoggerFactory.getLogger(ExperimentTask.class.getName());

  @Override
  public List<DataNode> performExperimentTasks(
      List<ExperimentTask> tasks, Function<ExperimentTask, Void> reportFn) {

    // for (ExperimentTask t : tasks) {
    //   t.run();
    //   reportFn.apply(t);
    // }
    // return new ArrayList<>();
    return tasks.parallelStream().map(t -> {
      try {
        t.run();
        reportFn.apply(t);        
        return t.getExperimentTaskReport()
            .getDataNode("AlgorithmReport")
            .getDataNode("Statistics");
      } catch (Exception e) {
        logger.error("Experiment task execution failed!", e);
      }
      return null;
    }).filter(e -> e != null)
      .collect(Collectors.toList());

  }

}
