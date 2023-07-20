package org.seage.hh.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.Experiment;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.configurator.Configurator;
import org.seage.hh.experimenter.configurator.FeedbackConfigurator;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;
import org.seage.hh.runner.IExperimentTasksRunner;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.genetics.ContinuousGeneticOperator;
import org.seage.metaheuristic.genetics.ContinuousGeneticOperator.Limit;
import org.seage.metaheuristic.genetics.GeneticAlgorithm;
import org.seage.metaheuristic.genetics.GeneticAlgorithmEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Single algorithm evolution experiment class.
 */
public class SingleAlgorithmEvolutionExperiment
    implements IAlgorithmListener<GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject>>, 
    Experiment {
  private static Logger logger =
      LoggerFactory.getLogger(SingleAlgorithmEvolutionExperiment.class.getName());
  private FeedbackConfigurator feedbackConfigurator;
  private int numSubjects;
  private int numIterations;
  private int algorithmTimeoutS;

  protected Configurator configurator;

  protected IExperimentTasksRunner experimentTasksRunner;
  protected ExperimentReporter experimentReporter;
  protected ProblemInfo problemInfo;

  protected String experimentName;
  protected UUID experimentID;
  protected String problemID;
  protected String instanceID;
  protected String algorithmID;
  protected int numRuns;
  protected int timeoutS;
  private double bestScore; 

  //    public SingleAlgorithmEvolutionExperiment(
  //      int numSubjects, int numIterations, int algorithmTimeoutS)
  //            throws Exception
  //    {
  //        super("SingleAlgorithmEvolution");
  //
  //        _numSubjects = numSubjects;
  //        _numIterations = numIterations;
  //        _algorithmTimeoutS = algorithmTimeoutS;
  //
  //        _feedbackConfigurator = new FeedbackConfigurator();
  //        ;
  //    }

  /**
   * Constructor.
   *
   * @param experimentID Experiment ID.
   * @param problemID Problem ID.
   * @param algorithmID Algorithm ID.
   * @param instanceID Instance ID. 
   * @param numSubjects Number of subjects.
   * @param numIterations Number of iterations.
   * @param algorithmTimeoutS Algorithm's timetout.
   * @param experimentReporter Experiment reporter.
   * @throws Exception .
   */
  public SingleAlgorithmEvolutionExperiment(
      UUID experimentID,
      String problemID,
      String algorithmID,
      String instanceID,
      int numSubjects, 
      int numIterations, 
      int algorithmTimeoutS,
      ExperimentReporter experimentReporter
  ) throws Exception {
    this.experimentID = experimentID;
    this.problemID = problemID;
    this.algorithmID = algorithmID;
    this.instanceID = instanceID;
    this.numSubjects = numSubjects;
    this.numIterations = numIterations;
    this.algorithmTimeoutS = algorithmTimeoutS;

    this.experimentName = "SingleAlgorithmEvolution";
    this.experimentReporter = experimentReporter;
    this.bestScore = 0.0;
    
    // Initialize
    this.feedbackConfigurator = new FeedbackConfigurator(0.0);
    this.problemInfo = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
  }

  @Override
  public Double run() throws Exception {
    try {
      logger.info("-------------------------------------");
      logger.info("Problem: {}", problemID);
      logger.info("Instance: {}",instanceID);
      ProblemInstanceInfo instanceInfo = problemInfo.getProblemInstanceInfo(instanceID);
      
      runExperimentTasksForProblemInstance();
    } catch (Exception ex) {
      logger.warn(ex.getMessage(), ex);
    }
    return bestScore;
  }

  protected void runExperimentTasksForProblemInstance() throws Exception {

    try {
      // ProblemInstanceInfo instanceInfo = problemInfo.getProblemInstanceInfo(instanceID);
      if (problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID) == null) {
        throw new IllegalArgumentException("Unknown algorithm: " + algorithmID);
      }

      logger.info("Algorithm: {}", algorithmID);

      ContinuousGeneticOperator.Limit[] limits = prepareAlgorithmParametersLimits(
        algorithmID, problemInfo);
      ContinuousGeneticOperator
          <SingleAlgorithmExperimentTaskSubject> realOperator = 
          new ContinuousGeneticOperator<SingleAlgorithmExperimentTaskSubject>(limits);

      SingleAlgorithmExperimentTaskEvaluator evaluator = 
          new SingleAlgorithmExperimentTaskEvaluator(
          this.experimentID,
          problemID, instanceID, algorithmID, algorithmTimeoutS, this::reportExperimentTask);
      GeneticAlgorithm<SingleAlgorithmExperimentTaskSubject> ga = 
          new GeneticAlgorithm<SingleAlgorithmExperimentTaskSubject>(
          realOperator, evaluator);
      ga.addGeneticSearchListener(this);
      ga.setCrossLengthPct(30);
      ga.setEliteSubjectsPct(0);
      ga.setIterationToGo(numIterations);
      ga.setMutateChromosomeLengthPct(10);
      ga.setMutatePopulationPct(50);
      ga.setPopulationCount(numSubjects);
      ga.setRandomSubjectsPct(20);

      List<SingleAlgorithmExperimentTaskSubject> subjects = 
          initializeSubjects(problemInfo, instanceID, algorithmID,
          numSubjects);

      ga.startSearching(subjects);

      
    } catch (Exception ex) {
      logger.warn(ex.getMessage(), ex);
    }
    
  }

  protected Void reportExperimentTask(ExperimentTaskRecord experimentTask) {
    try {
      logger.debug("Report for config id: {}", experimentTask.getConfigID());
      experimentReporter.reportExperimentTask(experimentTask);
      double taskScore = experimentTask.getScore();
      if (taskScore > this.bestScore) {
        this.bestScore = taskScore;
      }
    } catch (Exception e) {
      logger.error(String.format("Failed to report the experiment task: %s", 
          experimentTask.getExperimentTaskID().toString()), e);
    }
    return null;
  }

  private List<SingleAlgorithmExperimentTaskSubject> initializeSubjects(
      ProblemInfo problemInfo, String instanceID,
      String algorithmID, int count) throws Exception {
    List<SingleAlgorithmExperimentTaskSubject> result = 
        new ArrayList<SingleAlgorithmExperimentTaskSubject>();

    ProblemConfig[] pc = feedbackConfigurator
      .prepareConfigs(problemInfo, instanceID, algorithmID, count);

    List<DataNode> params = problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID)
        .getDataNodes("Parameter");

    for (int i = 0; i < count; i++) {
      String[] names = new String[params.size()];
      Double[] values = new Double[params.size()];
      for (int j = 0; j < params.size(); j++) {
        names[j] = params.get(j).getValueStr("name");
        values[j] = pc[i].getDataNode(
          "Algorithm").getDataNode("Parameters").getValueDouble(names[j]);
      }
      result.add(new SingleAlgorithmExperimentTaskSubject(names, values));
    }

    return result;
  }

  protected Limit[] prepareAlgorithmParametersLimits(
      String algorithmID, ProblemInfo pi
  ) throws Exception {
    List<DataNode> params = pi.getDataNode(
        "Algorithms").getDataNodeById(algorithmID).getDataNodes("Parameter");
    Limit[] result = new Limit[params.size()];

    int i = 0;
    for (DataNode paramNode : params) {
      double min = paramNode.getValueDouble("min");
      double max = paramNode.getValueDouble("max");

      result[i++] = new ContinuousGeneticOperator.Limit(min, max);
    }

    return result;
  }

  @Override
  public void algorithmStarted(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e) {
    logger.debug(" Started");
  }

  @Override
  public void algorithmStopped(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e) {
    logger.debug(" Stopped");
  }

  @Override
  public void newBestSolutionFound(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e) {
    // update the best - or not
    logger.debug("Objective value {}", e.getGeneticSearch().getBestSubject().getObjectiveValue());
  }

  @Override
  public void iterationPerformed(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e) {
    // logger.info(" Iteration " + e.getGeneticSearch().getCurrentIteration());
    logger.info(String.format(
        "%-44s (%d/%d)", "   Iteration: ", e.getGeneticSearch().getCurrentIteration(),
        e.getGeneticSearch().getIterationToGo()));
  }

  @Override
  public void noChangeInValueIterationMade(
      GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e) {

  }

  protected long getEstimatedTime(int instancesCount, int algorithmsCount) {
    return getNumberOfConfigs(instancesCount, algorithmsCount) * algorithmTimeoutS * 1000;
  }


  protected long getNumberOfConfigs(int instancesCount, int algorithmsCount) {
    return (long) numIterations * numSubjects * instancesCount * algorithmsCount;
  }
}
