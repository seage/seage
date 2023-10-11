package org.seage.hh.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.aal.problem.ProblemScoreCalculator;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.Experiment;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.experimenter.configurator.Configurator;
import org.seage.hh.experimenter.configurator.FeedbackConfigurator;
import org.seage.hh.experimenter.configurator.RandomConfigurator;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;
import org.seage.hh.runner.IExperimentTasksRunner;
import org.seage.hh.runner.LocalExperimentTasksRunner;
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
    implements IAlgorithmListener<GeneticAlgorithmEvent<SingleAlgorithmConfigExperimentSubject>>, 
    Experiment {
  private static Logger logger =
      LoggerFactory.getLogger(SingleAlgorithmEvolutionExperiment.class.getName());
  private FeedbackConfigurator feedbackConfigurator;
  private RandomConfigurator randomConfigurator;
  private int numConfigs;
  private int numIterations;
  private int algorithmTimeoutS;

  protected Configurator configurator;

  protected IExperimentTasksRunner experimentValidationTasksRunner;
  protected ExperimentReporter experimentReporter;
  protected ProblemInfo problemInfo;
  protected HashMap<String, ProblemInstanceInfo> instancesInfo;
  protected HashMap<String, HashMap<String, Double>> confValInstancesScore;
  private ProblemScoreCalculator problemScoreCalculator;

  protected String experimentName;
  protected UUID experimentID;
  protected String problemID;
  protected List<String> instanceIDs;
  protected String algorithmID;
  protected int numRuns;
  protected int timeoutS;
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
   * @param instanceIDs Instance IDs. 
   * @param numConfigs Number of subjects.
   * @param numIterations Number of iterations.
   * @param algorithmTimeoutS Algorithm's timetout.
   * @param experimentReporter Experiment reporter.
   * @throws Exception .
   */
  public SingleAlgorithmEvolutionExperiment(
      UUID experimentID,
      String problemID,
      String algorithmID,
      List<String> instanceIDs,
      int numConfigs, 
      int numIterations, 
      int algorithmTimeoutS,
      ExperimentReporter experimentReporter
  ) throws Exception {
    this.experimentID = experimentID;
    this.problemID = problemID;
    this.algorithmID = algorithmID;
    this.instanceIDs = instanceIDs;
    this.numConfigs = numConfigs;
    this.numIterations = numIterations;
    this.algorithmTimeoutS = algorithmTimeoutS;
    this.experimentValidationTasksRunner = new LocalExperimentTasksRunner();
    this.confValInstancesScore = new HashMap<>();

    this.experimentName = "SingleAlgorithmEvolution";
    this.experimentReporter = experimentReporter;
    
    // Initialize
    this.feedbackConfigurator = new FeedbackConfigurator(0.0);
    this.randomConfigurator = new RandomConfigurator();
    this.problemInfo = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
    this.instancesInfo = new HashMap<>();
    this.problemScoreCalculator = new ProblemScoreCalculator(problemInfo);

    // Set the number of validation instances to use
    for (String instanceID : instanceIDs) {
      instancesInfo.put(instanceID, problemInfo.getProblemInstanceInfo(instanceID));
    }
  }

  @Override
  public Double run() throws Exception {
    confValInstancesScore = new HashMap<>();
    try {
      logger.info("-------------------------------------");

      runSubjectsValidation(getBestSubjectsForAlgorithm());

        // reportBestExperimentSubject(bestSubject, startDate, endDate);
    } catch (Exception ex) {
      logger.warn(ex.getMessage(), ex);
    }
    return getBestExperimentConfigScore();
  }

  private void runSubjectsValidation(List<SingleAlgorithmConfigExperimentSubject> taskSubjects) throws Exception {
    // RUN EXPERIMENT VALIDATION
    logger.info("Started config validation");
    // TODO - how many intances to use (all of them for now)
    for (String instanceID : instanceIDs) { 
      List<ExperimentTaskRequest> taskQueue = new ArrayList<>();
      for (SingleAlgorithmConfigExperimentSubject subject : taskSubjects) {
        String configID = subject.getAlgorithmParams().hash();

        taskQueue.add(new ExperimentTaskRequest(
            UUID.randomUUID(), 
            experimentID, 
            1, 
            1, 
            problemID, 
            instanceID,
            algorithmID, 
            configID, 
            subject.getAlgorithmParams(), 
            null, 
            10)); // TODO - unified time for each validation task
      }
      logger.info("Evaluating {} configs for instance {}", taskQueue.size(), instanceID);

      experimentValidationTasksRunner.performExperimentTasks(
          taskQueue, this::reportValidationExperimentTask);
    }
  }

  protected Void reportValidationExperimentTask(ExperimentTaskRecord experimentTask) {
    try {
      String configID = experimentTask.getConfigID();
      String instanceID = experimentTask.getInstanceID();
      Double score = experimentTask.getScore();

      // logger.info("Instance {} score: {}", instanceID, score);
      experimentReporter.reportExperimentTask(experimentTask);

      // Log the instance score
      confValInstancesScore.computeIfAbsent(
          configID, t -> new HashMap<>());
      confValInstancesScore.get(configID).computeIfAbsent(instanceID, t -> score);
      confValInstancesScore.get(configID).put(instanceID, Math.max(
          confValInstancesScore.get(configID).get(instanceID),
          score
      ));
    } catch (Exception e) {
      logger.error(String.format("Failed to report the experiment task: %s", 
          experimentTask.getExperimentTaskID().toString()), e);
    }
    return null;
  }

  protected List<SingleAlgorithmConfigExperimentSubject> 
      getBestSubjectsForAlgorithm() throws Exception {

    try {
      if (problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID) == null) {
        throw new IllegalArgumentException("Unknown algorithm: " + algorithmID);
      }

      logger.info("Algorithm: {}", algorithmID);

      ContinuousGeneticOperator.Limit[] limits = prepareAlgorithmParametersLimits(
        algorithmID, problemInfo);
      ContinuousGeneticOperator
          <SingleAlgorithmConfigExperimentSubject> realOperator = 
          new ContinuousGeneticOperator<>(limits);

      SingleAlgorithmExperimentTaskEvaluator evaluator = 
          new SingleAlgorithmExperimentTaskEvaluator(
          this.experimentID,
          problemID, 
          instanceIDs, 
          algorithmID, 
          algorithmTimeoutS,  
          this.problemInfo,
          this.instancesInfo);
      GeneticAlgorithm<SingleAlgorithmConfigExperimentSubject> ga = 
          new GeneticAlgorithm<>(realOperator, evaluator);
      ga.addGeneticSearchListener(this);
      ga.setCrossLengthPct(40);
      ga.setEliteSubjectsPct(1);
      ga.setIterationToGo(numIterations);
      ga.setMutateChromosomeLengthPct(40);
      ga.setMutatePopulationPct(40);
      ga.setPopulationCount(numConfigs);
      ga.setRandomSubjectsPct(1);

      
      List<SingleAlgorithmConfigExperimentSubject> configs = 
          initializeConfigs(problemInfo, instanceIDs, algorithmID, numConfigs);
      // Fill the rest of configs by random subjects
      if (configs.size() < numConfigs) {
        configs.addAll(initializeRandomConfigs(
            problemInfo, instanceIDs, algorithmID, numConfigs - configs.size()));
      }
      logger.info("Prepared {} initial configs", configs.size());
      for (var s : configs) {
        logger.info(" - {}", s.getAlgorithmParams().hash());
      }
      ga.startSearching(configs);

      // Store the last iteration population
      return ga.getSubjects();

    } catch (Exception ex) {
      logger.warn(ex.getMessage(), ex);
    }
    return null;
  }

  protected double getBestExperimentConfigScore() throws Exception {
    // this.bestScore = -bestSubject.getObjectiveValue()[0];
    Double bestScore = 0.0;
    List<String> insIDs = new ArrayList<>();
    List<Double> insScores = new ArrayList<>();
    for (String configID : confValInstancesScore.keySet()) {
      for (String instanceID : confValInstancesScore.get(configID).keySet()) {
        insIDs.add(instanceID);
        insScores.add(confValInstancesScore.get(configID).get(instanceID));
      }

      Double curScore = problemScoreCalculator.calculateProblemScore(
          insIDs.toArray(new String[]{}), 
          insScores.stream().mapToDouble(a -> a).toArray()); 
      
      logger.info(" - {} {}", configID, curScore);

      if (bestScore <= curScore) {
        bestScore = curScore;
      }
    }
      
    return bestScore;
  }

  private List<SingleAlgorithmConfigExperimentSubject> initializeConfigs(
      ProblemInfo problemInfo, List<String> instanceIDs, String algorithmID, int numOfConfigs
  ) throws Exception {

    List<SingleAlgorithmConfigExperimentSubject> result = new ArrayList<>();
    Set<String> newConfigIds = new HashSet<>();
    int numPerInstance = Math.max(1, numOfConfigs / instanceIDs.size());
    
    int curNumOfConfigs = 0;

    for (String instanceID : instanceIDs) {
      if (curNumOfConfigs >= numOfConfigs) {
        break;
      }

      ProblemConfig[] pc = feedbackConfigurator.prepareConfigs(
        problemInfo, instanceID, algorithmID, numPerInstance);

      List<DataNode> params = problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID)
          .getDataNodes("Parameter");

      for (int i = 0; i < pc.length; i++) {
        if (curNumOfConfigs >= numOfConfigs) {
          break;
        }
        
        String[] names = new String[params.size()];
        Double[] values = new Double[params.size()];
        for (int j = 0; j < params.size(); j++) {
          names[j] = params.get(j).getValueStr("name");
          values[j] = pc[i].getDataNode(
            "Algorithm").getDataNode("Parameters").getValueDouble(names[j]);
        }
        var config = new SingleAlgorithmConfigExperimentSubject(names, values);
        String newConfigId = config.getAlgorithmParams().hash();
        // Add only the new ones
        if (!newConfigIds.contains(newConfigId)) {
          newConfigIds.add(newConfigId);
          result.add(config);
          curNumOfConfigs += 1;
        }
      }
    }
    return result;
  }

  private List<SingleAlgorithmConfigExperimentSubject> initializeRandomConfigs(
      ProblemInfo problemInfo, List<String> instanceIDs,
      String algorithmID, int numOfConfigs) throws Exception {
    List<SingleAlgorithmConfigExperimentSubject> result = new ArrayList<>();
    int numRuns = (int) Math.ceil(numOfConfigs / (double)instanceIDs.size());
    for (int i = 0; i < numRuns; i++) {
      for (String instanceID : instanceIDs) {
        ProblemConfig[] pc = randomConfigurator.prepareConfigs(
            problemInfo, instanceID, algorithmID, 1);

        List<DataNode> params = problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID)
            .getDataNodes("Parameter");

        String[] names = new String[params.size()];
        Double[] values = new Double[params.size()];
        for (int j = 0; j < params.size(); j++) {
          names[j] = params.get(j).getValueStr("name");
          values[j] = pc[0].getDataNode(
            "Algorithm").getDataNode("Parameters").getValueDouble(names[j]);
        }
        var config = new SingleAlgorithmConfigExperimentSubject(names, values);
        result.add(config);
      }
    }
    return result.subList(0, numOfConfigs);
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
  public void algorithmStarted(GeneticAlgorithmEvent<SingleAlgorithmConfigExperimentSubject> e) {
    logger.info("Started configs evolution");
  }

  @Override
  public void algorithmStopped(GeneticAlgorithmEvent<SingleAlgorithmConfigExperimentSubject> e) {
    logger.info("Finished config evolution");
  }

  @Override
  public void newBestSolutionFound(GeneticAlgorithmEvent<SingleAlgorithmConfigExperimentSubject> e) {
    // update the best - or not
    logger.debug("Objective value {}", e.getGeneticSearch().getBestSubject().getObjectiveValue());
  }

  @Override
  public void iterationPerformed(GeneticAlgorithmEvent<SingleAlgorithmConfigExperimentSubject> e) {
    // logger.info(" Iteration " + e.getGeneticSearch().getCurrentIteration());
    logger.info("Config generation done: \t ({}/{})", e.getGeneticSearch().getCurrentIteration(),
        e.getGeneticSearch().getIterationToGo());
  }

  @Override
  public void noChangeInValueIterationMade(
      GeneticAlgorithmEvent<SingleAlgorithmConfigExperimentSubject> e) {
        // empty
  }

  protected long getEstimatedTime(int instancesCount, int algorithmsCount) {
    return getNumberOfConfigs(instancesCount, algorithmsCount) * algorithmTimeoutS * 1000;
  }


  protected long getNumberOfConfigs(int instancesCount, int algorithmsCount) {
    return (long) numIterations * numConfigs * instancesCount * algorithmsCount;
  }
}
