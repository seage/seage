package org.seage.hh.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import org.seage.aal.Annotations.ProblemId;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.Experiment;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.Experimenter;
import org.seage.hh.experimenter.configurator.Configurator;
import org.seage.hh.experimenter.configurator.FeedbackConfigurator;
import org.seage.hh.experimenter.runner.IExperimentTasksRunner;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.genetics.ContinuousGeneticOperator;
import org.seage.metaheuristic.genetics.ContinuousGeneticOperator.Limit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.seage.metaheuristic.genetics.GeneticAlgorithm;
import org.seage.metaheuristic.genetics.GeneticAlgorithmEvent;

public class SingleAlgorithmEvolutionExperimenter
    implements IAlgorithmListener<GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject>>, 
    Experiment {
  private static Logger logger =
      LoggerFactory.getLogger(SingleAlgorithmEvolutionExperimenter.class.getName());
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

//    public SingleAlgorithmEvolutionExperimenter(int numSubjects, int numIterations, int algorithmTimeoutS)
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

  public SingleAlgorithmEvolutionExperimenter(
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
    this.feedbackConfigurator = new FeedbackConfigurator();
    this.problemInfo = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
  }

  @Override
  public Double run() throws Exception {
    try {
      logger.info("-------------------------------------");
      logger.info(String.format("%-15s", "Problem:", problemID));
      logger.info(String.format("%-15s", "Instance:",instanceID));
      ProblemInstanceInfo instanceInfo = problemInfo.getProblemInstanceInfo(instanceID);
      runExperimentTasksForProblemInstance();
    } catch (Exception ex) {
      logger.warn(ex.getMessage(), ex);
    }
    return bestScore;
  }

  protected void runExperimentTasksForProblemInstance() throws Exception {

    try {
      ProblemInstanceInfo instanceInfo = problemInfo.getProblemInstanceInfo(instanceID);
      if (problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID) == null) {
        throw new IllegalArgumentException("Unknown algorithm: " + algorithmID);
      }

      logger.info(String.format("%-15s Algorithm: ", algorithmID));

      ContinuousGeneticOperator.Limit[] limits = prepareAlgorithmParametersLimits(
        algorithmID, problemInfo);
      ContinuousGeneticOperator
          <SingleAlgorithmExperimentTaskSubject> realOperator = 
          new ContinuousGeneticOperator<SingleAlgorithmExperimentTaskSubject>(limits);

      SingleAlgorithmExperimentTaskEvaluator evaluator = 
          new SingleAlgorithmExperimentTaskEvaluator(
          this.experimentID,
          problemID, instanceID, algorithmID, algorithmTimeoutS);
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
    // logger.info(" Started");

  }

  @Override
  public void algorithmStopped(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e) {
    // logger.info(" Stopped");

  }

  @Override
  public void newBestSolutionFound(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e) {
    // update the best - or not
    System.out.println("Objective value" + e.getGeneticSearch().getBestSubject().getObjectiveValue());
  }

  @Override
  public void iterationPerformed(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e) {
    // logger.info(" Iteration " + e.getGeneticSearch().getCurrentIteration());
    logger.info(String.format("%-44s (%d/%d)", "   Iteration: ", e.getGeneticSearch().getCurrentIteration(),
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
    return (long)numIterations * numSubjects * instancesCount * algorithmsCount;
  }
}
