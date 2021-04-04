package org.seage.hh.experimenter2;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaHeuristicExperimenter implements AlgorithmExperimenter {
  protected static Logger logger =
      LoggerFactory.getLogger(MetaHeuristicExperimenter.class.getName());
  protected UUID experimentID;
  protected String instanceID;
  protected String algorithmID;
  protected int numConfigs;
  protected int timeoutS;

  /**
   * MetaHeuristicExperimenter constructor.
   */
  protected MetaHeuristicExperimenter(
      UUID experimentID, String instanceID, String algorithmID, int numConfigs, int timeoutS) {
    this.experimentID = experimentID;
    this.instanceID = instanceID;
    this.algorithmID = algorithmID;
    this.numConfigs = numConfigs;
    this.timeoutS = timeoutS;
  }

  @Override
  public void runExperiment() {
    logger.info("Running MetaheuristicExperimenter");

  }

}
