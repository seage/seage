package org.seage.hh.experimenter2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.ExperimentTask;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.experimenter.configurator.DefaultConfigurator;
import org.seage.hh.experimenter.runner.IExperimentTasksRunner;
import org.seage.hh.experimenter.runner.LocalExperimentTasksRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * @author David Omrai
 */
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
  private int numSteps = 10;
  private int timeoutS;
  private double bestScore;
  private List<Phenotype<?>> solutionPool;
  private int solutionPoolSize = 100;
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
    this.solutionPool = new ArrayList<>();

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

    

    // provider and factory
    IProblemProvider<Phenotype<?>> provider =
        ProblemProvider.getProblemProviders().get(this.problemID);

    // problem instance
    ProblemInstance instance = provider
        .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo(this.instanceID));
    
    long randomSeed = this.experimentID.hashCode();
    Comparator<Phenotype<?>> compareByScore = 
        (Phenotype<?> p1, Phenotype<?> p2) -> p1.getScore().compareTo(p2.getScore());
    
    for (int jobID = 1; jobID <= numRuns; jobID++) {
      this.solutionPool = new ArrayList<>();
      Phenotype<?>[] pool =  
        generateInitialSolutions(provider, instance, this.solutionPoolSize, randomSeed);
      this.solutionPool.addAll(Arrays.asList(pool));
      //System.out.println(pool);
      //System.out.println(this.solutionPool);
        
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
              instanceInfo
              .getInstanceID(), algorithmID, 2)[1]; // the second with a bit of randomness
    

          int numSolutions = config
              .getDataNode("Algorithm").getDataNode("Parameters").getValueInt("numSolutions");
          numSolutions = Math.min(numSolutions, this.solutionPoolSize);

          Class problemPhenotypeClass = this.solutionPool.get(0).getClass();

          Phenotype<?>[] solutions = (Phenotype<?>[])
            java.lang.reflect.Array.newInstance(problemPhenotypeClass, numSolutions);
          //List<Phenotype<?>> algSolutions = new ArrayList<>();
          for (int i = 0; i < numSolutions; i++) {
            solutions[i] = this.solutionPool
                .get(new Random().nextInt(this.solutionPool.size()));
          }
          
          
          //System.out.println(algSolutions);
          
          // Enqueue experiment tasks
          taskQueue.add(new ExperimentTaskRequest(
              UUID.randomUUID(), experimentID, jobID, stageID, problemID, instanceID,
              algorithmID, config.getAlgorithmParams(), 
              solutions, timeoutS / numSteps));
        }
  
        // RUN EXPERIMENT TASKS
        experimentTasksRunner.performExperimentTasks(taskQueue, this::reportExperimentTask);
      }
    } 
    

    return bestScore;
  }
  
  private Void reportExperimentTask(ExperimentTask experimentTask) {
    try {
      experimentReporter.reportExperimentTask(experimentTask);
      
      // Object solution =  experimentTask
      //     .getExperimentTaskReport().getDataNode("Solutions").getValue("phenotype");

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
