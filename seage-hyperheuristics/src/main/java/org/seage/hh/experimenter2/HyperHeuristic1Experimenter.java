package org.seage.hh.experimenter2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.ExperimentTask;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.experimenter.configurator.DefaultConfigurator;
import org.seage.hh.experimenter.runner.IExperimentTasksRunner;
import org.seage.hh.experimenter.runner.LocalExperimentTasksRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HyperHeuristic1Experimenter implements Experimenter {
  private static Logger logger =
      LoggerFactory.getLogger(MetaHeuristicExperimenter.class.getName());
  private DefaultConfigurator configurator;
  private ProblemInfo problemInfo;
  private IExperimentTasksRunner experimentTasksRunner;
  private ExperimentReporter experimentReporter;
  
  private UUID experimentID;
  private String problemID;
  private String instanceID;
  //private String algorithmID;
  private int numRuns;
  private int timeoutS;
  private double bestScore;
  private List<Phenotype<?>[]> pool;
  private String[] algorithmIDs = {
    "AntColony", "GeneticAlgorithm", "TabuSearch", "SimulatedAnnealing"};

  /**
   * HyperHeuristic1Experimenter constructor.
   */
  protected HyperHeuristic1Experimenter(
      UUID experimentID, String problemID, String instanceID, 
      String algorithmID, int numRuns, int timeoutS,
      ExperimentReporter experimentReporter) 
      throws Exception {
    this.experimentID = experimentID;
    this.problemID = problemID;
    this.instanceID = instanceID;
    //this.algorithmID = algorithmID;
    this.numRuns = numRuns;
    this.timeoutS = timeoutS;
    this.experimentReporter = experimentReporter;
    this.bestScore = Double.MIN_VALUE;
    this.pool = new ArrayList<>();

    // Initialize all
    this.problemInfo = ProblemProvider.getProblemProviders().get(this.problemID).getProblemInfo();
    this.experimentTasksRunner = new LocalExperimentTasksRunner();
    this.configurator = new DefaultConfigurator(0.15);
  }

  /**
   * Method runs experiment.
   */
  public Double runExperiment() throws Exception {
    ProblemInstanceInfo instanceInfo = problemInfo.getProblemInstanceInfo(instanceID);

    // The taskQueue size must be limited since the results are stored in the task's reports
    // Queue -> Tasks -> Reports -> Solutions ==> OutOfMemoryError
    List<ExperimentTaskRequest> taskQueue = new ArrayList<>();





    // provider and factory
    IProblemProvider<Phenotype<?>> provider =
        ProblemProvider.getProblemProviders().get(this.problemID);

    // problem instance
    ProblemInstance instance = provider
        .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo(this.instanceID));
    
    int numSolutions = 1;
    long randomSeed = this.experimentID.hashCode();
    
    for (int i = 0; i < this.pool.size(); i++) {
      this.pool
        .add(generateInitialSolutions(provider, instance, numSolutions, randomSeed));
    }
    
    for (int i = 0; i < numRuns; i++) {
      for (String algorithmID: algorithmIDs) {
        // Prepare experiment task configs
        ProblemConfig config = configurator.prepareConfigs(problemInfo,
            instanceInfo.getInstanceID(), algorithmID, 2)[1]; // the second with a bit of randomness
  
        Phenotype<?>[] solutions =  this.pool.size() > 0 
          ? this.pool.remove(new Random().nextInt(this.pool.size())) : null;
        
        // Enqueue experiment tasks
        for (int runID = 1; runID <= numRuns; runID++) {
          taskQueue.add(new ExperimentTaskRequest(
              UUID.randomUUID(), experimentID, runID, 1, problemID, instanceID,
              algorithmID, config.getAlgorithmParams(), 
              solutions, timeoutS));
        }
      }
    } 

    // RUN EXPERIMENT TASKS
    experimentTasksRunner.performExperimentTasks(taskQueue, this::reportExperimentTask);

    return bestScore;
  }
  
  private Void reportExperimentTask(ExperimentTask experimentTask) {
    try {
      experimentReporter.reportExperimentTask(experimentTask);
      // Phenotype<?>[] solution = {
      //  ;
      // }
      // this.pool.add(new Phenotype<?>()
      //   .fromText();
      
      Object solution =  experimentTask
          .getExperimentTaskReport().getDataNode("Solutions").getValue("phenotype");
      
      Phenotype<?> phenotype = (Phenotype<?>)solution;

      Phenotype<?>[] solutionEntry = {
        phenotype
      };

      this.pool.add(solutionEntry);

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
