package org.seage.hh.experimenter2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaheuristicExperimenter implements AlgorithmExperimenter {
  protected static Logger logger =
      LoggerFactory.getLogger(MetaheuristicExperimenter.class.getName());

  @Override
  public void runExperiment() {
    logger.info("Running MetaheuristicExperimenter");

  }

}
