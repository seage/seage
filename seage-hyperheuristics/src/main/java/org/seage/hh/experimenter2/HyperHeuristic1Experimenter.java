package org.seage.hh.experimenter2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HyperHeuristic1Experimenter implements Experimenter {
  protected static Logger logger =
      LoggerFactory.getLogger(MetaHeuristicInstanceExperimenter.class.getName());

  @Override
  public void runExperiment() {
    logger.info("Running HyperHeuristic1Experimenter");
  }

}
