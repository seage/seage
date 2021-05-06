package org.seage.hh.experimenter2;

import java.util.Date;
import java.util.UUID;

import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HyperHeuristic1Experimenter implements Experimenter {
  protected static Logger logger =
      LoggerFactory.getLogger(MetaHeuristicExperimenter.class.getName());

  private UUID experimentTaskID;
  private UUID experimentID;
  private int jobID;
  private int stageID;
  private String experimentType;
  private String problemID;
  private String instanceID;
  private String algorithmID;
  private String configID;
  private Date startDate;
  private Date endDate;
  private Double score;
  private Double scoreDelta;

  private AlgorithmParams algorithmParams;
  private long timeoutS;

  private DataNode experimentTaskReport;
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
        taskInfo.getAlgorithmID(),
        taskInfo.getAlgorithmParams(),
        taskInfo.getTimeoutS()
    );
  }

  private HyperHeuristic1Experimenter(UUID experimentTaskID, 
      UUID experimentID, int jobID, int stageID, String problemID,
      String instanceID, String algorithmID, AlgorithmParams algorithmParams, long timeoutS)
      throws Exception {
    this.experimentTaskID = experimentTaskID;
    this.experimentID = experimentID;
    this.jobID = jobID;
    this.stageID = stageID;
    this.problemID = problemID;
    this.instanceID = instanceID;
    this.algorithmID = algorithmID;
    this.configID = algorithmParams.hash();
    this.startDate = new Date();
    this.endDate = this.startDate;
    this.score = Double.MAX_VALUE;
    this.scoreDelta = 0.0;
  }


  @Override
  public Double runExperiment() throws Exception {
    logger.info("Running HyperHeuristic1Experimenter");
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


    return null;
  }

}
 