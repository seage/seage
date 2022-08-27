package org.seage.hh.experimenter.multialgorithm;

import java.util.List;
import java.util.UUID;
import org.seage.aal.problem.ProblemInfo;
import org.seage.hh.experimenter.Experiment;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.configurator.Configurator;
import org.seage.hh.experimenter.runner.IExperimentTasksRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MultiAlgorithmRandomExperimenter implements Experiment {
  private static Logger logger =
      LoggerFactory.getLogger(MultiAlgorithmRandomExperimenter.class.getName());
  protected Configurator configurator;

  protected IExperimentTasksRunner experimentTasksRunner;
  protected ExperimentReporter experimentReporter;
  protected ProblemInfo problemInfo;
  
  protected String experimentName;
  protected UUID experimentID;
  protected String problemID;
  protected String instanceID;
  protected List<String> algorithmIDs;
  protected int numRuns;
  protected int timeoutS;
  private double bestScore;

  protected MultiAlgorithmRandomExperimenter(
      UUID experimentID, String problemID, List<String> algorithmIDs,
      String instanceID, int numRuns, int timeoutS,
      ExperimentReporter experimentReporter
  ) {
    this.experimentID = experimentID;
    this.problemID = problemID;
    this.algorithmIDs = algorithmIDs;
    this.instanceID = instanceID;
    this.numRuns = numRuns;
    this.timeoutS = timeoutS;
    this.experimentName = "SingleAlgorithm";
    this.experimentReporter = experimentReporter;
    this.bestScore = 0.0;
  }

  /**
   * Default method.
   */
  @Override
  public Double run() throws Exception {
    return bestScore;
  }
}
