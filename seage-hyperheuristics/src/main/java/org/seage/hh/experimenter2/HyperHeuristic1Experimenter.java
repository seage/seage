package org.seage.hh.experimenter2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HyperHeuristic1Experimenter implements AlgorithmExperimenter {
  protected static Logger logger =
      LoggerFactory.getLogger(MetaHeuristicExperimenter.class.getName());

  @Override
  public void runExperiment() {
    logger.info("Running HyperHeuristic1Experimenter");
  }

}