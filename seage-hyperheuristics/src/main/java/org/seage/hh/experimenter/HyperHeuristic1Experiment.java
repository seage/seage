package org.seage.hh.experimenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.configurator.ExtendedDefaultConfigurator;
import org.seage.hh.experimenter.runner.IExperimentTasksRunner;
import org.seage.hh.experimenter.runner.LocalExperimentTasksRunner;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * @author David Omrai
 */
public class HyperHeuristic1Experiment implements Experiment {
  private static Logger logger =
      LoggerFactory.getLogger(MetaHeuristicExperiment.class.getName());
  private ExtendedDefaultConfigurator configurator;
  private ProblemInfo problemInfo;
  private IExperimentTasksRunner experimentTasksRunner;
  private ExperimentReporter experimentReporter;
  
  private UUID experimentID;
  private String problemID;
  private String instanceID;
  private int numSteps;
  private int timeoutS;
  private double bestScore;
  private List<Phenotype<?>> solutionPool;
  private int solutionPoolSize = 100;
  private String[] algorithmIDs = {
    "GeneticAlgorithm", "TabuSearch", "SimulatedAnnealing", "AntColony"};
  private Random random = new Random(); // NOSONAR

  /**
   * HyperHeuristic1Experimenter constructor.
   */
  protected HyperHeuristic1Experiment(
      UUID experimentID, String problemID, String instanceID, 
      String algorithmID, int numSteps, int timeoutS,
      ExperimentReporter experimentReporter) 
      throws Exception {
    this.experimentID = experimentID;
    this.problemID = problemID;
    this.instanceID = instanceID;
    this.numSteps = numSteps;
    this.timeoutS = timeoutS;
    this.experimentReporter = experimentReporter;
    this.bestScore = Double.MIN_VALUE;
    this.solutionPool = new ArrayList<>();

    // Initialize all
    this.problemInfo = ProblemProvider.getProblemProviders().get(this.problemID).getProblemInfo();
    this.experimentTasksRunner = new LocalExperimentTasksRunner();
    this.configurator = new ExtendedDefaultConfigurator();
  }

  /**
   * Method runs experiment.
   */
  public Double run() throws Exception {
    ProblemInstanceInfo instanceInfo = problemInfo.getProblemInstanceInfo(instanceID);

    // provider and factory
    IProblemProvider<Phenotype<?>> provider =
        ProblemProvider.getProblemProviders().get(this.problemID);

    // problem instance
    ProblemInstance instance = provider
        .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo(this.instanceID));
    
    long randomSeed = this.experimentID.hashCode();
    Comparator<Phenotype<?>> compareByScore = 
        (Phenotype<?> p1, Phenotype<?> p2) -> p1.getScore().compareTo(p2.getScore());
    
    this.solutionPool = new ArrayList<>();
    Phenotype<?>[] pool =  
      generateInitialSolutions(provider, instance, this.solutionPoolSize, randomSeed);
    this.solutionPool.addAll(Arrays.asList(pool));
      
    for (int stageID = 1; stageID <= numSteps; stageID++) {
      // prepare solution pool 
      Collections.sort(this.solutionPool, compareByScore.reversed());
      this.solutionPool = this.solutionPool.subList(0, this.solutionPoolSize);
      
      // The taskQueue size must be limited since the results are stored in the task's reports
      // Queue -> Tasks -> Reports -> Solutions ==> OutOfMemoryError
      List<ExperimentTaskRequest> taskQueue = new ArrayList<>();
      for (String algorithmID: algorithmIDs) {
        // Prepare experiment task configs
        ProblemConfig config = configurator.prepareConfigs(problemInfo,
            instanceInfo.getInstanceID(), algorithmID, 2)[1]; // the second with a bit of randomness
  
        int numSolutions = config
            .getDataNode("Algorithm").getDataNode("Parameters").getValueInt("numSolutions");
        
        numSolutions = Math.min(numSolutions, this.solutionPoolSize);

        Class<?> problemPhenotypeClass = this.solutionPool.get(0).getClass();

        Phenotype<?>[] solutions = (Phenotype<?>[])
          java.lang.reflect.Array.newInstance(problemPhenotypeClass, numSolutions);
        for (int i = 0; i < numSolutions; i++) {
          solutions[i] = this.solutionPool
              .get(random.nextInt(this.solutionPool.size()));
        }
                
        // Enqueue experiment tasks
        taskQueue.add(new ExperimentTaskRequest(
            UUID.randomUUID(), experimentID, 1, stageID, problemID, instanceID,
            algorithmID, config.getAlgorithmParams(), 
            solutions, timeoutS / numSteps));
      }

      // RUN EXPERIMENT TASKS
      experimentTasksRunner.performExperimentTasks(taskQueue, this::reportExperimentTask);
    }  

    return bestScore;
  }
  
  private Void reportExperimentTask(ExperimentTaskRecord experimentTask) {
    try {
      experimentReporter.reportExperimentTask(experimentTask);

      DataNode solutionNodes = experimentTask
          .getExperimentTaskReport().getDataNode("Solutions").getDataNode("Output");

      for (DataNode solutionNode: solutionNodes.getDataNodes("Solution")) {
        Phenotype<?> phenotype = (Phenotype<?>)solutionNode.getValue("phenotype");
        this.solutionPool.add(phenotype);
      }


      double taskScore = experimentTask.getScore();
      if (taskScore > bestScore) {
        this.bestScore = taskScore;
      }
    } catch (Exception e) {
      logger.error(String.format("Failed to report the experiment task: %s", 
          experimentTask.getExperimentTaskID().toString()), e);
    }
    return null;
  }


  private static Phenotype<?>[] generateInitialSolutions(
      IProblemProvider<Phenotype<?>> provider, ProblemInstance instance,
      int numSolutions, long randomSeed) throws Exception {
    return provider.generateInitialSolutions(instance, numSolutions, randomSeed);
  }
}
