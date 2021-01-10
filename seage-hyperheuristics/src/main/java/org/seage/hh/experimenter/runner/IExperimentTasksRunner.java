package org.seage.hh.experimenter.runner;

import java.util.List;

import org.seage.hh.experimenter.ExperimentTask;

public interface IExperimentTasksRunner {
  void performExperimentTasks(List<ExperimentTask> tasks, String reportPath);
}
