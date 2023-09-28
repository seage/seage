package org.seage.hh.experimenter.multialgorithm;

import java.util.List;
import org.seage.aal.problem.ProblemInfo;
import org.seage.hh.experimenter.Experiment;
import org.seage.hh.experimenter.ExperimentScoreCard;
import org.seage.hh.experimenter.configurator.Configurator;
import org.seage.hh.runner.IExperimentTasksRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MultiAlgorithmRandomExperimenter extends Experiment {
  private static Logger logger =
      LoggerFactory.getLogger(MultiAlgorithmRandomExperimenter.class.getName());
  protected Configurator configurator;

  protected IExperimentTasksRunner experimentTasksRunner;
  protected ProblemInfo problemInfo;
  
  protected List<String> algorithmIDs;
  protected int numRuns;
  private double bestScore;

  protected MultiAlgorithmRandomExperimenter(
      String algorithmID, String problemID,
      List<String> instanceIDs, List<String> algorithmIDs, int numRuns, int timeoutS,
      String tag
  ) throws Exception {
    super(algorithmID, problemID, instanceIDs, timeoutS, tag);
    this.algorithmIDs = algorithmIDs;
    this.numRuns = numRuns;
    this.timeoutS = timeoutS;
    this.experimentName = "SingleAlgorithm";
    this.bestScore = 0.0;
  }

  /**
   * Default method.
   */
  @Override
  public ExperimentScoreCard run() throws Exception {
    return null;//bestScore;
  }
}
