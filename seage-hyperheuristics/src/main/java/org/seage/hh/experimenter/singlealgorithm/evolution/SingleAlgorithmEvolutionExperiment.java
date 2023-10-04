package org.seage.hh.experimenter.singlealgorithm.evolution;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
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
import org.seage.hh.experimenter.configurator.RandomConfigurator;
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
  private RandomConfigurator randomConfigurator;
  private int numSubjects;
  private int numIterations;
  private int algorithmTimeoutS;

  protected Configurator configurator;

  protected IExperimentTasksRunner experimentTasksRunner;
  protected ExperimentReporter experimentReporter;
  protected ProblemInfo problemInfo;
  protected HashMap<String, ProblemInstanceInfo> instancesInfo;

  protected String experimentName;
  protected UUID experimentID;
  protected String problemID;
  protected List<String> instanceIDs;
  protected String algorithmID;
  protected int numRuns;
  protected int timeoutS;
  private double bestScore; 
  private Random random;

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
      List<String> instanceIDs,
      int numSubjects, 
      int numIterations, 
      int algorithmTimeoutS,
      ExperimentReporter experimentReporter
  ) throws Exception {
    this.experimentID = experimentID;
    this.problemID = problemID;
    this.algorithmID = algorithmID;
    this.instanceIDs = instanceIDs;
    this.numSubjects = numSubjects;
    this.numIterations = numIterations;
    this.algorithmTimeoutS = algorithmTimeoutS;

    this.experimentName = "SingleAlgorithmEvolution";
    this.experimentReporter = experimentReporter;
    this.bestScore = 0.0;
    
    // Initialize
    this.feedbackConfigurator = new FeedbackConfigurator(0.0);
    this.randomConfigurator = new RandomConfigurator();
    this.problemInfo = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
    this.instancesInfo = new HashMap<>();
    this.random = new Random();
    
    for (String instanceID : instanceIDs) {
      instancesInfo.put(instanceID, problemInfo.getProblemInstanceInfo(instanceID));
    }
  }

  @Override
  public Double run() throws Exception {
    try {
      logger.info("-------------------------------------");      
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
          new ContinuousGeneticOperator<>(limits);

      SingleAlgorithmExperimentTaskEvaluator evaluator = 
          new SingleAlgorithmExperimentTaskEvaluator(
          this.experimentID,
          problemID, 
          instanceIDs, 
          algorithmID, 
          algorithmTimeoutS,  
          this.problemInfo,
          this.instancesInfo, 
          this::reportExperimentTask);
      GeneticAlgorithm<SingleAlgorithmExperimentTaskSubject> ga = 
          new GeneticAlgorithm<>(realOperator, evaluator);
      ga.addGeneticSearchListener(this);
      ga.setCrossLengthPct(50);
      ga.setEliteSubjectsPct(10);
      ga.setIterationToGo(numIterations);
      ga.setMutateChromosomeLengthPct(5);
      ga.setMutatePopulationPct(20);
      ga.setPopulationCount(numSubjects);
      ga.setRandomSubjectsPct(1);

      
      List<SingleAlgorithmExperimentTaskSubject> subjects = 
          initializeSubjects(problemInfo, instanceIDs, algorithmID, numSubjects);
      logger.info("Prepared {} initial configs", subjects.size());

      ga.startSearching(subjects);
      
    } catch (Exception ex) {
      logger.warn(ex.getMessage(), ex);
    }
    
  }

  protected Void reportExperimentTask(ExperimentTaskRecord experimentTask) {
    try {
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
      ProblemInfo problemInfo, List<String> instanceIDs,
      String algorithmID, int numOfSubjects
  ) throws Exception {
    List<SingleAlgorithmExperimentTaskSubject> result = new ArrayList<>();
    Set<String> newConfigIds = new HashSet<>();
    int numPerInstance = Math.max(
          1,  (int) Math.floor(numOfSubjects / (double) instanceIDs.size()));
    int numOfRestOfSubject = Math.max(
          0, numOfSubjects - this.instanceIDs.size() * numPerInstance);

    for (String instanceID : instanceIDs) { // NOSONAR
      int subjectsForInstance = numPerInstance;
      if (numOfRestOfSubject != 0) {
        subjectsForInstance += 1;
        numOfRestOfSubject -= 1;
      }

      List<ProblemConfig> configs = new ArrayList<>(Arrays.asList(
          feedbackConfigurator.prepareConfigs(problemInfo, instanceID, 
          algorithmID, subjectsForInstance)));

      List<DataNode> params = problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID)
          .getDataNodes("Parameter");

      int numAddedInstanceSubjects = 0;
      int i = 0;
      while (numAddedInstanceSubjects != subjectsForInstance) {
        String[] names = new String[params.size()];
        Double[] values = new Double[params.size()];
        for (int j = 0; j < params.size(); j++) {
          names[j] = params.get(j).getValueStr("name");
          values[j] = configs.get(i).getDataNode(
            "Algorithm").getDataNode("Parameters").getValueDouble(names[j]);
        }
        var subject = new SingleAlgorithmExperimentTaskSubject(names, values);
        String newConfigId = subject.getAlgorithmParams().hash();
        // Add only the new ones
        if (!newConfigIds.contains(newConfigId)) {
          newConfigIds.add(newConfigId);
          result.add(subject);
          numAddedInstanceSubjects += 1;
        } else {
          ProblemConfig randomConfig = randomConfigurator.prepareConfigs(
              problemInfo, instanceID, algorithmID, 1)[0];
          configs.add(randomConfig);
        }
        // Next config
        i++;
      }
    }

    // Return the right size of results
    return result.subList(0, numOfSubjects);
  }

  protected Limit[] prepareAlgorithmParametersLimits(
      String algorithmID, ProblemInfo pi) throws Exception {
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
    logger.info("Started configs evolution");
  }

  @Override
  public void algorithmStopped(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e) {
    logger.info("Finished config evolution");
  }

  @Override
  public void newBestSolutionFound(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e) {
    // update the best - or not
    logger.debug("Objective value {}", e.getGeneticSearch().getBestSubject().getObjectiveValue());
  }

  @Override
  public void iterationPerformed(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e) {
    // logger.info(" Iteration " + e.getGeneticSearch().getCurrentIteration());
    logger.info("Config generation done: \t ({}/{})", e.getGeneticSearch().getCurrentIteration(),
        e.getGeneticSearch().getIterationToGo());
  }

  @Override
  public void noChangeInValueIterationMade(
      GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e) {
        // empty
  }

  protected long getEstimatedTime(int instancesCount, int algorithmsCount) {
    return getNumberOfConfigs(instancesCount, algorithmsCount) * algorithmTimeoutS * 1000;
  }


  protected long getNumberOfConfigs(int instancesCount, int algorithmsCount) {
    return (long) numIterations * numSubjects * instancesCount * algorithmsCount;
  }
}
