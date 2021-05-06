package org.seage.hh.experimenter2;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import java.util.Map;

import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.aal.problem.ProblemScoreCalculator;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.experimenter.configurator.DefaultConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HyperHeuristic1Experimenter implements Experimenter {
  protected static Logger _logger =
      LoggerFactory.getLogger(MetaHeuristicExperimenter.class.getName());

  private UUID experimentTaskID;
  private UUID experimentID;
  private int jobID;
  private int stageID;
  private String experimentType;
  private String problemID;
  private String instanceID;
  private Map<String, Map<String, Object>> algorithmIDs;
  //private String configID;
  private Date startDate;
  private Date endDate;
  private Double score;
  private Double scoreDelta;
  private DefaultConfigurator defaultConfigurator;
  private ProblemInfo problemInfo;
  private ProblemInstanceInfo instanceInfo;

  //private AlgorithmParams algorithmParams;
  private long timeoutS;

  //private DataNode experimentTaskReport;
  private boolean taskFinished;
 

  /**
   * Constructor for DB mapper.
   */
  HyperHeuristic1Experimenter() {}

  /**
   * .
   * @param taskInfo .
   * @throws Exception .
   */
  public HyperHeuristic1Experimenter(
      ExperimentTaskRequest taskInfo) throws Exception {
    this(
        taskInfo.getExperimentTaskID(),
        taskInfo.getExperimentID(),
        taskInfo.getJobID(),
        taskInfo.getStageID(),
        taskInfo.getProblemID(),
        taskInfo.getInstanceID(),
        taskInfo.getTimeoutS()
    );
  }

  private HyperHeuristic1Experimenter(UUID experimentTaskID, 
      UUID experimentID, int jobID, int stageID, String problemID,
      String instanceID, long timeoutS)
      throws Exception {
    this.experimentTaskID = experimentTaskID;
    this.experimentID = experimentID;
    this.jobID = jobID;
    this.stageID = stageID;
    this.problemID = problemID;
    this.instanceID = instanceID;
    //this.configID = algorithmParams.hash();
    this.startDate = new Date();
    this.endDate = this.startDate;
    this.score = Double.MAX_VALUE;
    this.scoreDelta = 0.0;
    this.defaultConfigurator = new DefaultConfigurator(0.15);
    this.problemInfo = ProblemProvider.getProblemProviders().get(this.problemID).getProblemInfo();
    this.instanceInfo = problemInfo.getProblemInstanceInfo(instanceID);
    this.timeoutS = timeoutS;
    this.taskFinished = false;

    
    this.algorithmIDs = new HashMap<>() {{
        put("AntColony", new HashMap<>());
        put("GeneticAlgorithm", new HashMap<>());
        put("TabuSearch", new HashMap<>());
        put("SimulatedAnnealing", new HashMap<>());
      }
    };

    for (String algID: algorithmIDs.keySet()) {
      DataNode experimentTaskReport;
      experimentTaskReport = new DataNode("ExperimentTaskReport");
      experimentTaskReport.putValue("version", "0.7");
      experimentTaskReport.putValue("experimentType", experimentType);
      experimentTaskReport.putValue("experimentID", experimentID);
      experimentTaskReport.putValue("timeoutS", timeoutS);

      // parameters
      ProblemConfig config = defaultConfigurator.prepareConfigs(problemInfo,
          instanceInfo.getInstanceID(), algID, 2)[1];

      this.algorithmIDs.get(algID).put("algorithmParams", config.getAlgorithmParams());


      
      DataNode problemNode = new DataNode("Problem");
      problemNode.putValue("problemID", this.problemID);

      DataNode instanceNode = new DataNode("Instance");
      instanceNode.putValue("name", this.instanceID);

      DataNode algorithmNode = new DataNode("Algorithm");
      algorithmNode.putValue("algorithmID", algID);
      algorithmNode.putDataNode(
          (AlgorithmParams) this.algorithmIDs.get(algID).get("algorithmParams"));

      problemNode.putDataNode(instanceNode);
      
      DataNode configNode = new DataNode("Config");
      configNode.putDataNode(problemNode);
      configNode.putDataNode(algorithmNode);

      experimentTaskReport.putDataNode(configNode);
      this.algorithmIDs.get(algID).put("experimentTaskReport", experimentTaskReport);



      DataNode solutionsNode = new DataNode("Solutions");
      solutionsNode.putDataNode(new DataNode("Input"));
      solutionsNode.putDataNode(new DataNode("Output"));
      this.experimentTaskReport.putDataNode(solutionsNode);
    }
    //configNode.putValue("configID", this.configID);
    // configNode.putValue("runID", this.runID);
  }

 
  @Override
  public Double runExperiment() throws Exception {
    _logger.info("Running HyperHeuristic1Experimenter");
    // Prepare the initial pool
    // provider and factory
    IProblemProvider<Phenotype<?>> provider =
        ProblemProvider.getProblemProviders().get(this.problemID);
    IAlgorithmFactory<Phenotype<?>, ?> factory = provider.getAlgorithmFactory(this.algorithmID);

    // problem instance
    ProblemInstance instance = provider
        .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo(this.instanceID));

    IPhenotypeEvaluator<Phenotype<?>> evaluator = provider.initPhenotypeEvaluator(instance);

    // algorithm
    IAlgorithmAdapter<Phenotype<?>, ?> algorithm = factory.createAlgorithm(instance, evaluator);

    Phenotype<?>[] solutions = provider.generateInitialSolutions(instance,
        this.algorithmParams.getValueInt("numSolutions"), this.experimentID.hashCode());
    writeSolutions(evaluator,
        this.experimentTaskReport.getDataNode("Solutions").getDataNode("Input"), solutions);



    
    // Another part
    algorithm.solutionsFromPhenotype(solutions);
    algorithm.startSearching(this.algorithmParams, true);
    _logger.debug("Algorithm started");
    waitForTimeout(algorithm);
    algorithm.stopSearching();
    _logger.debug("Algorithm stopped");

    solutions = algorithm.solutionsToPhenotype();
    writeSolutions(evaluator,
        this.experimentTaskReport.getDataNode("Solutions").getDataNode("Output"), solutions);
    
    calculateExperimentScore();
    
    this.endDate = new Date();
    long durationS = (this.endDate.getTime() - this.startDate.getTime()) / 1000;

    this.experimentTaskReport.putDataNode(algorithm.getReport());
    this.experimentTaskReport.putValue("durationS", durationS);
    
    this.taskFinished = true;

    _logger.debug("Algorithm run duration: {}", durationS);    
    //_logger.debug("ExperimentTask finished ({})", this.configID);

    return null;
  }

  private void waitForTimeout(IAlgorithmAdapter<?, ?> alg) throws Exception {
    long time = System.currentTimeMillis();
    while (alg.isRunning() && ((System.currentTimeMillis() - time) < this.timeoutS * 1000)) {
      Thread.sleep(100);
    }
  }

  private void writeSolutions(IPhenotypeEvaluator<Phenotype<?>> evaluator, DataNode dataNode,
      Phenotype<?>[] solutions) {
    for (Phenotype<?> p : solutions) {
      try {
        DataNode solutionNode = new DataNode("Solution");
        solutionNode.putValue("objVal", p.getObjValue());
        solutionNode.putValue("score", p.getScore());
        solutionNode.putValue("solution", p.toText());
        solutionNode.putValue("hash", p.computeHash());
        dataNode.putDataNode(solutionNode);
      } catch (Exception ex) {
        _logger.error("Cannot write solution", ex);
      }
    }
  }

  private void calculateExperimentScore() throws Exception { 
    ProblemInfo problemInfo = ProblemProvider
        .getProblemProviders()
        .get(problemID)
        .getProblemInfo();
    ProblemScoreCalculator scoreCalculator = new ProblemScoreCalculator(problemInfo); 

    DataNode inputs = experimentTaskReport.getDataNode("Solutions").getDataNode("Input");
    DataNode outputs = experimentTaskReport.getDataNode("Solutions").getDataNode("Output");

    double bestObjValue = getBestObjectiveValue(outputs);
    double taskBestScore = 
        scoreCalculator.calculateInstanceScore(instanceID, bestObjValue);

    double initObjValue = getBestObjectiveValue(inputs);
    double taskInitScore = 
        scoreCalculator.calculateInstanceScore(instanceID, initObjValue);

    this.score = taskBestScore;
    this.scoreDelta = scoreCalculator.calculateScoreDelta(taskInitScore, taskBestScore);
  }

  private double getBestObjectiveValue(DataNode solutions) throws Exception {
    double bestObjVal = Double.MAX_VALUE;
    for (DataNode s : solutions.getDataNodes("Solution")) {
      double objVal = s.getValueDouble("objVal");
      if (objVal < bestObjVal) {
        bestObjVal = objVal;
      }
    }
    return bestObjVal;
  }

}
 