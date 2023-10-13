package org.seage.hh.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
public class SingleAlgorithmConfigsEvolutionExperiment extends Experiment implements
      IAlgorithmListener<GeneticAlgorithmEvent<SingleAlgorithmConfigsEvolutionSubject>> {

  private static Logger logger =
      LoggerFactory.getLogger(SingleAlgorithmConfigsEvolutionExperiment.class.getName());

  private FeedbackConfigurator feedbackConfigurator;
  private RandomConfigurator randomConfigurator;
  private int numSubjects;
  private int numGenerations;
  private int algorithmTimeoutS;

  protected Configurator configurator;

  protected ExperimentReporter experimentReporter;
  protected ProblemInfo problemInfo;
  protected HashMap<String, ProblemInstanceInfo> instancesInfo;
  protected HashMap<String, HashMap<String, Double>> instanceScores;
  private ProblemScoreCalculator problemScoreCalculator;



  /**
   * Constructor.
   *
   * @param algorithmID Algorithm ID.
   * @param problemID Problem ID.
   * @param instanceIDs Instance IDs. 
   * @param numSubjects Number of configs.
   * @param numGenerations Number of iterations.
   * @param algorithmTimeoutS Algorithm's timetout.
   * @param tag Experiment tag.
   * @throws Exception .
   */
  public SingleAlgorithmConfigsEvolutionExperiment(
      String algorithmID,
      String problemID,
      List<String> instanceIDs,
      int numSubjects, 
      int numGenerations, 
      int algorithmTimeoutS,
      String tag
  ) throws Exception {
    super(algorithmID, problemID, instanceIDs, algorithmTimeoutS, tag);
    
    this.numSubjects = numSubjects;
    this.numGenerations = numGenerations;
    this.algorithmTimeoutS = algorithmTimeoutS;
    this.instanceScores = new HashMap<>();

    this.experimentName = "SingleAlgorithmEvolution";
    
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
  public void run() throws Exception {
    logStart();

    instanceScores = new HashMap<>();

    // Main run
    List<SingleAlgorithmConfigsEvolutionSubject> bestConfigs = findBestAlgorithmConfigs();
    // Evaluation
    runAlgorithmConfigsValidation(bestConfigs);
    // Done

    Map<String, Double> configScores = calculateConfigScores();

    List<String> configIDs = new ArrayList<>();
    configIDs.addAll(configScores.keySet());
    configIDs.sort((c1, c2) -> Double.compare(
        configScores.get(c2), configScores.get(c1)));
    for (String configID : configIDs) {
      logger.info(" - {}  {}", 
          configID, String.format("%.4f", configScores.get(configID)));
    }
    // Print the best score
    logEnd(configScores.get(configIDs.get(0)));
  }

  protected List<SingleAlgorithmConfigsEvolutionSubject> findBestAlgorithmConfigs() 
      throws Exception {
    try {
      if (problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID) == null) {
        throw new IllegalArgumentException("Unknown algorithm: " + algorithmID);
      }

      logger.info("Algorithm: {}", algorithmID);

      ContinuousGeneticOperator.Limit[] limits = 
          prepareAlgorithmParametersLimits(algorithmID, problemInfo);
      ContinuousGeneticOperator<SingleAlgorithmConfigsEvolutionSubject> realOperator = 
          new ContinuousGeneticOperator<>(limits);

      SingleAlgorithmConfigsEvolutionSubjectEvaluator evaluator = 
          new SingleAlgorithmConfigsEvolutionSubjectEvaluator(
              this.experimentID,
              problemID, 
              instanceIDs, 
              algorithmID, 
              algorithmTimeoutS,  
              this.problemInfo,
              this.instancesInfo);
      GeneticAlgorithm<SingleAlgorithmConfigsEvolutionSubject> ga = 
          new GeneticAlgorithm<>(realOperator, evaluator);
      ga.addGeneticSearchListener(this);
      ga.setCrossLengthPct(40);
      ga.setEliteSubjectsPct(1);
      ga.setIterationToGo(numGenerations);
      ga.setMutateChromosomeLengthPct(40);
      ga.setMutatePopulationPct(40);
      ga.setPopulationCount(numSubjects);
      ga.setRandomSubjectsPct(1);

      
      List<SingleAlgorithmConfigsEvolutionSubject> configs = 
          initializeConfigs(problemInfo, instanceIDs, algorithmID, numSubjects);
      // Fill the rest of configs by random configs
      if (configs.size() < numSubjects) {
        configs.addAll(initializeRandomConfigs(
            problemInfo, instanceIDs, algorithmID, numSubjects - configs.size()));
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
    return List.of();
  }

  private void runAlgorithmConfigsValidation(List<SingleAlgorithmConfigsEvolutionSubject> configs)
      throws Exception {
    // RUN EXPERIMENT VALIDATION
    logger.info("Started config validation");

    for (String instanceID : instanceIDs) { 
      List<ExperimentTaskRequest> taskQueue = new ArrayList<>();
      for (SingleAlgorithmConfigsEvolutionSubject config : configs) {
        String configID = config.getAlgorithmParams().hash();

        taskQueue.add(new ExperimentTaskRequest(
            UUID.randomUUID(), 
            experimentID, 
            1, 
            1, 
            problemID, 
            instanceID,
            algorithmID, 
            configID, 
            config.getAlgorithmParams(), 
            null, 
            numGenerations * algorithmTimeoutS));
      }
      logger.info("Evaluating {} configs for instance {}", taskQueue.size(), instanceID);

      new LocalExperimentTasksRunner().performExperimentTasks(
          taskQueue, this::reportValidationExperimentTask);
    }
  }

  protected Void reportValidationExperimentTask(ExperimentTaskRecord experimentTask) {
    try {
      String configID = experimentTask.getConfigID();
      String instanceID = experimentTask.getInstanceID();
      Double score = experimentTask.getScore();

      //logger.info("-- {} - {} - {}", configID, instanceID, score);
      experimentReporter.reportExperimentTask(experimentTask);

      // Log the instance score
      instanceScores.computeIfAbsent(configID, t -> new HashMap<>());
      instanceScores.get(configID).computeIfAbsent(instanceID, t -> score);
      instanceScores.get(configID).put(instanceID, 
          Math.max(score, instanceScores.get(configID).get(instanceID)));
    } catch (Exception e) {
      logger.error(String.format("Failed to report the experiment task: %s", 
          experimentTask.getExperimentTaskID().toString()), e);
    }
    return null;
  }

  private List<SingleAlgorithmConfigsEvolutionSubject> initializeConfigs(ProblemInfo problemInfo,
      List<String> instanceIDs, String algorithmID, int numOfConfigs) throws Exception {

    List<SingleAlgorithmConfigsEvolutionSubject> result = new ArrayList<>();
    Set<String> newConfigIds = new HashSet<>();
    int numPerInstance = Math.max(1, numOfConfigs / instanceIDs.size());
    
    int curNumOfConfigs = 0;

    for (String instanceID : instanceIDs) {
      if (curNumOfConfigs >= numOfConfigs) {
        break;
      }

      ProblemConfig[] pc =
          feedbackConfigurator.prepareConfigs(problemInfo, instanceID, algorithmID, numPerInstance);

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
        var config = new SingleAlgorithmConfigsEvolutionSubject(names, values);
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

  private List<SingleAlgorithmConfigsEvolutionSubject> initializeRandomConfigs(
      ProblemInfo problemInfo, List<String> instanceIDs, String algorithmID, int numOfConfigs)
      throws Exception {
    List<SingleAlgorithmConfigsEvolutionSubject> result = new ArrayList<>();
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
        var config = new SingleAlgorithmConfigsEvolutionSubject(names, values);
        result.add(config);
      }
    }
    return result.subList(0, numOfConfigs);
  }

  protected Map<String, Double> calculateConfigScores() throws Exception {
    Map<String, Double> result = new HashMap<>();

    for (String configID : instanceScores.keySet()) {
      List<String> insIDs = new ArrayList<>();
      List<Double> insScores = new ArrayList<>();
      for (String instanceID : instanceScores.get(configID).keySet()) {
        insIDs.add(instanceID);
        insScores.add(instanceScores.get(configID).get(instanceID));
      }

      result.put(configID, problemScoreCalculator.calculateProblemScore(
          insIDs.toArray(new String[]{}), 
          insScores.stream().mapToDouble(a -> a).toArray())); 
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
  public void algorithmStarted(GeneticAlgorithmEvent<SingleAlgorithmConfigsEvolutionSubject> e) {
    logger.info("Started configs evolution");
  }

  @Override
  public void algorithmStopped(GeneticAlgorithmEvent<SingleAlgorithmConfigsEvolutionSubject> e) {
    logger.info("Finished config evolution");
  }

  @Override
  public void newBestSolutionFound(GeneticAlgorithmEvent<SingleAlgorithmConfigsEvolutionSubject> e) {
    // update the best - or not
    logger.debug("Objective value {}", e.getGeneticSearch().getBestSubject().getObjectiveValue());
  }

  @Override
  public void iterationPerformed(GeneticAlgorithmEvent<SingleAlgorithmConfigsEvolutionSubject> e) {
    logger.info("Config generation done: \t ({}/{})", e.getGeneticSearch().getCurrentIteration(),
        e.getGeneticSearch().getIterationToGo());
  }

  @Override
  public void noChangeInValueIterationMade(
      GeneticAlgorithmEvent<SingleAlgorithmConfigsEvolutionSubject> e) {
    // empty
  }

  protected long getEstimatedTime(int instancesCount, int algorithmsCount) {
    return getNumberOfConfigs(instancesCount, algorithmsCount) * algorithmTimeoutS * 1000;
  }


  protected long getNumberOfConfigs(int instancesCount, int algorithmsCount) {
    return (long) numGenerations * instancesCount * algorithmsCount;
  }
}
