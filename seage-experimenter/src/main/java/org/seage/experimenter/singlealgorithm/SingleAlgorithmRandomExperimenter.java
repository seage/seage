package org.seage.experimenter.singlealgorithm;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.data.xml.XmlHelper;
import org.seage.experimenter.ExperimentTask;
import org.seage.experimenter.Experimenter;
import org.seage.experimenter.config.Configurator;
import org.seage.experimenter.config.RandomConfigurator;

public class SingleAlgorithmRandomExperimenter extends Experimenter {
  protected Configurator _configurator;
  private int _numConfigs;
  private int _timeoutS;

  private final int NUM_RUNS = 3;

  public SingleAlgorithmRandomExperimenter(String problemID, String[] instanceIDs, String[] algorithmIDs,
      int numConfigs, int timeoutS) throws Exception {
    super("SingleAlgorithmRandom", problemID, instanceIDs, algorithmIDs);

    _numConfigs = numConfigs;
    _timeoutS = timeoutS;

    _configurator = new RandomConfigurator();
  }

//    public SingleAlgorithmRandomExperimenter(String problemID, String[] instanceIDs, String[] algorithmIDs, int numConfigs, int timeoutS)
//    {
//        this(numConfigs, timeoutS);
//        _experimentName = experimenterName;
//    }

  @Override
  protected void runExperiment(ProblemInstanceInfo instanceInfo) throws Exception {
    for (int i = 0; i < _algorithmIDs.length; i++) {
      String algorithmID = _algorithmIDs[i];
      String instanceID = instanceInfo.getInstanceID();

      _logger.info(String.format("%-15s %-24s (%d/%d)", "Algorithm: ", algorithmID, i + 1, _algorithmIDs.length));
      _logger.info(String.format("%-44s", "   Running... "));

      List<ExperimentTask> taskQueue = new ArrayList<ExperimentTask>();
      ProblemConfig[] configs = _configurator.prepareConfigs(_problemInfo, instanceInfo.getInstanceID(), algorithmID,
          _numConfigs);
      for (ProblemConfig config : configs) {
        for (int runID = 1; runID <= NUM_RUNS; runID++) {
          // String reportName = problemInfo.getProblemID() + "-" + algorithmID + "-" +
          // instanceInfo.getInstanceID() + "-" + configID + "-" + runID + ".xml";
          taskQueue.add(new ExperimentTask(_experimentName, _experimentID, _problemID, instanceID, algorithmID,
              config.getAlgorithmParams(), runID, _timeoutS));
        }
      }
      String reportPath = String.format("output/experiment-logs/%s-%s-%s-%s.zip", _experimentID, _problemID, instanceID,
          algorithmID);

      _experimentTasksRunner.performExperimentTasks(taskQueue, reportPath);
//            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(reportPath)))) {                
//                for(ExperimentTask et : taskQueue) {
//                    XmlHelper.writeXml(et.getExperimentTaskReport(), zos, et.getReportName());
//                }
//            }
      /*
       * try(ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new
       * File(reportPath)))) { Optional<Integer> n = taskQueue.parallelStream().map(t
       * -> { _logger.info("t.run()"); t.run(); return t; }).map(t -> { DataNode r =
       * null; try { _logger.info("XmlHelper.writeXml()"); r =
       * t.getExperimentTaskReport(); XmlHelper.writeXml(r, zos, t.getReportName()); }
       * catch (Exception e) { // TODO Auto-generated catch block e.printStackTrace();
       * } return 1; }).reduce((a, b) -> a+1); }
       */
    }
  }

  @Override
  protected long getEstimatedTime(int instancesCount, int algorithmsCount) {
    return getNumberOfConfigs(instancesCount, algorithmsCount) * _timeoutS * 1000;
  }

  @Override
  protected long getNumberOfConfigs(int instancesCount, int algorithmsCount) {
    return _numConfigs * NUM_RUNS * instancesCount * algorithmsCount;
  }
}
