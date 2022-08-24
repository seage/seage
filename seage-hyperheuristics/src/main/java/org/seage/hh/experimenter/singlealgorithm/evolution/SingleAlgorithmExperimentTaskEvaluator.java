package org.seage.hh.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;
import org.seage.metaheuristic.genetics.SubjectEvaluator;
import org.seage.thread.TaskRunner3;

public class SingleAlgorithmExperimentTaskEvaluator extends SubjectEvaluator<SingleAlgorithmExperimentTaskSubject> {
  private UUID experimentID;
  private String problemID;
  private String instanceID;
  private String algorithmID;
  private long timeoutS;

  private HashMap<String, Double> configCache;

  public SingleAlgorithmExperimentTaskEvaluator(UUID experimentID, String problemID, String instanceID,
      String algorithmID, long timeoutS) {
    super();
    this.experimentID = experimentID;
    this.problemID = problemID;
    this.instanceID = instanceID;
    this.algorithmID = algorithmID;
    this.timeoutS = timeoutS;
    this.configCache = new HashMap<String, Double>();
  }

  @Override
  public void evaluateSubjects(List<SingleAlgorithmExperimentTaskSubject> subjects) throws Exception {
    List<ExperimentTaskRequest> taskQueue = new ArrayList<>();
    HashMap<ExperimentTaskRequest, SingleAlgorithmExperimentTaskSubject> taskMap = new HashMap<>();

    for (SingleAlgorithmExperimentTaskSubject s : subjects) {
      AlgorithmParams algorithmParams = new AlgorithmParams(); // subject
      for (int i = 0; i < s.getChromosome().getLength(); i++) {
        algorithmParams.putValue(s.getParamNames()[i], s.getChromosome().getGene(i));
      }

      String configID = algorithmParams.hash();
      if (this.configCache.containsKey(configID)) {
        s.setObjectiveValue(new double[] { this.configCache.get(configID) });
      } else {
        ExperimentTaskRequest task = new ExperimentTaskRequest(
            UUID.randomUUID(),
            this.experimentID,
            1, 1,
            this.problemID,
            this.instanceID,
            this.algorithmID,
            algorithmParams,
            null,
            this.timeoutS
        );

        taskMap.put(task, s);
        taskQueue.add(task);
      }
    }

    TaskRunner3.run(taskQueue.toArray(new Runnable[] {}), Runtime.getRuntime().availableProcessors());

    for (ExperimentTaskRequest task : taskQueue) {
      SingleAlgorithmExperimentTaskSubject s = taskMap.get(task);
      double value = Double.MAX_VALUE;
      try {
        // value = task.getExperimentTaskReport().getDataNode("AlgorithmReport").getDataNode("Statistics")
        //     .getValueDouble("bestObjVal");
      } catch (Exception ex) {
        _logger.warn("Unable to set value");
      }
      s.setObjectiveValue(new double[] { value });

      this.configCache.put(task.getConfigID(), value);
    }

  }

  @Override
  protected double[] evaluate(SingleAlgorithmExperimentTaskSubject solution) throws Exception {
    throw new UnsupportedOperationException("Should be unimplemented");
  }

}
