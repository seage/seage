package org.seage.experimenter.runner;

import java.util.List;

import org.seage.experimenter.ExperimentTask;

public interface IExperimentTasksRunner {
  void performExperimentTasks(List<ExperimentTask> tasks, String reportPath);
}
