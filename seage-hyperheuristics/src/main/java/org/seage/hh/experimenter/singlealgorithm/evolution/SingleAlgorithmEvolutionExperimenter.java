package org.seage.hh.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.Experimenter;
import org.seage.hh.experimenter.configurator.FeedbackConfigurator;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.genetics.ContinuousGeneticOperator;
import org.seage.metaheuristic.genetics.ContinuousGeneticOperator.Limit;
import org.seage.metaheuristic.genetics.GeneticAlgorithm;
import org.seage.metaheuristic.genetics.GeneticAlgorithmEvent;

public class SingleAlgorithmEvolutionExperimenter extends Experimenter
    implements IAlgorithmListener<GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject>> {
  private FeedbackConfigurator _feedbackConfigurator;
  private int _numSubjects;
  private int _numIterations;
  private int _algorithmTimeoutS;
  private List<String> algorithmIDs;

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

  /**
   * .
   * @param experimentName .
   * @param problemID .
   * @param instanceIDs .
   * @param algorithmIDs .
   * @param numSubjects .
   * @param numIterations .
   * @param algorithmTimeoutS .
   * @throws Exception .
   */
  public SingleAlgorithmEvolutionExperimenter(
      String experimentName,
      String problemID, 
      String[] instanceIDs,
      String[] algorithmIDs, 
      int numSubjects, 
      int numIterations, 
      int algorithmTimeoutS
  ) throws Exception {
    super(
        experimentName, 
        "", 
        Map.ofEntries(
          Map.entry(problemID, Arrays.asList(instanceIDs))
        ), 
        numIterations,
        algorithmTimeoutS
    );
    this.algorithmIDs = Arrays.asList(algorithmIDs);
    _numSubjects = numSubjects;
    _numIterations = numIterations;
    _algorithmTimeoutS = algorithmTimeoutS;

    _feedbackConfigurator = new FeedbackConfigurator();
  }

  protected void experimentMain() throws Exception {
    // Run experiment tasks for each instance
    for (Entry<String, List<String>> entry : instanceIDsPerProblems.entrySet()) {
      String problemID = entry.getKey();
    
      ProblemInfo problemInfo = ProblemProvider
          .getProblemProviders()
          .get(problemID)
          .getProblemInfo();

      int instanceIndex = 0;
      for (String instanceID : entry.getValue()) {
        try {
          logger.info("-------------------------------------");
          logger.info(String.format("%-15s %s", "Problem:", problemID));
          logger.info(String.format("%-15s %-16s    (%d/%d)", "Instance:", 
              instanceID, instanceIndex + 1, entry.getValue().size()));
          ProblemInstanceInfo instanceInfo = problemInfo.getProblemInstanceInfo(
              instanceID);
          runExperimentTasksForProblemInstance(instanceInfo);
        } catch (Exception ex) {
          logger.warn(ex.getMessage(), ex);
        }
      }
    }
  }

  protected void runExperimentTasksForProblemInstance(ProblemInstanceInfo instanceInfo) throws Exception {

    for (Entry<String, List<String>> entry : instanceIDsPerProblems.entrySet()) {
      String problemID = entry.getKey();
      String instanceID = instanceInfo.getInstanceID();

      ProblemInfo problemInfo = ProblemProvider
          .getProblemProviders()
          .get(problemID)
          .getProblemInfo();


      int algorithmIndex = 0;
      for (String algorithmID : this.algorithmIDs){
        try {
          if (problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID) == null) {
            throw new IllegalArgumentException("Unknown algorithm: " + algorithmID);
          }

          logger.info(
              String.format(
                "%-15s %-24s (%d/%d)", 
                "Algorithm: ", algorithmID, algorithmIndex + 1, this.algorithmIDs.size()));

          ContinuousGeneticOperator.Limit[] limits = prepareAlgorithmParametersLimits(
            algorithmID, problemInfo);
          ContinuousGeneticOperator
              <SingleAlgorithmExperimentTaskSubject> realOperator = 
              new ContinuousGeneticOperator<SingleAlgorithmExperimentTaskSubject>(limits);

          SingleAlgorithmExperimentTaskEvaluator evaluator = 
              new SingleAlgorithmExperimentTaskEvaluator(
              this.experimentID,
              problemID, instanceID, algorithmID, _algorithmTimeoutS);
          GeneticAlgorithm<SingleAlgorithmExperimentTaskSubject> ga = 
              new GeneticAlgorithm<SingleAlgorithmExperimentTaskSubject>(
              realOperator, evaluator);
          ga.addGeneticSearchListener(this);
          ga.setCrossLengthPct(30);
          ga.setEliteSubjectsPct(0);
          ga.setIterationToGo(_numIterations);
          ga.setMutateChromosomeLengthPct(10);
          ga.setMutatePopulationPct(50);
          ga.setPopulationCount(_numSubjects);
          ga.setRandomSubjectsPct(20);

          List<SingleAlgorithmExperimentTaskSubject> subjects = 
              initializeSubjects(problemInfo, instanceID, algorithmID,
              _numSubjects);

          ga.startSearching(subjects);
        } catch (Exception ex) {
          logger.warn(ex.getMessage(), ex);
        }
        algorithmIndex++;
      }
    }
  }

  private List<SingleAlgorithmExperimentTaskSubject> initializeSubjects(
      ProblemInfo problemInfo, String instanceID,
      String algorithmID, int count) throws Exception {
    List<SingleAlgorithmExperimentTaskSubject> result = 
        new ArrayList<SingleAlgorithmExperimentTaskSubject>();

    ProblemConfig[] pc = _feedbackConfigurator
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

  protected Limit[] prepareAlgorithmParametersLimits(String algorithmID, ProblemInfo pi) throws Exception {
    List<DataNode> params = pi.getDataNode("Algorithms").getDataNodeById(algorithmID).getDataNodes("Parameter");
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

  }

  @Override
  public void iterationPerformed(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e) {
    // logger.info(" Iteration " + e.getGeneticSearch().getCurrentIteration());
    logger.info(String.format("%-44s (%d/%d)", "   Iteration: ", e.getGeneticSearch().getCurrentIteration(),
        e.getGeneticSearch().getIterationToGo()));
  }

  @Override
  public void noChangeInValueIterationMade(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e) {

  }


  protected long getEstimatedTime(int instancesCount, int algorithmsCount) {
    return getNumberOfConfigs(instancesCount, algorithmsCount) * _algorithmTimeoutS * 1000;
  }


  protected long getNumberOfConfigs(int instancesCount, int algorithmsCount) {
    return (long)_numIterations * _numSubjects * instancesCount * algorithmsCount;
  }

  @Override
  protected String getExperimentConfig() {
    // TODO Auto-generated method stub
    return null;
  }

}
