package org.seage.hh.experimenter2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HyperHeuristic1Experimenter implements Experimenter {
  protected static Logger logger =
      LoggerFactory.getLogger(MetaHeuristicExperimenter.class.getName());

  @Override
  public Double runExperiment() throws Exception {
    logger.info("Running HyperHeuristic1Experimenter");
    return null;
  }

}
