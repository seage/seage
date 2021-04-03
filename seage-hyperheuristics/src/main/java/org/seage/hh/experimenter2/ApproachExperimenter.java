package org.seage.hh.experimenter2;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApproachExperimenter {
  protected static Logger logger = LoggerFactory.getLogger(ApproachExperimenter.class.getName());

  /**
   * ApproachExperimenter.
   * @param approachName .
   * @param problemInstanceIDs .
   */
  public ApproachExperimenter(
      String approachName, HashMap<String, List<String>> problemInstanceIDs) {
    
    logger.info("Approach '{}'", approachName);
    
    for (Entry<String, List<String>> entry: problemInstanceIDs.entrySet()) {
      logger.info("Problem '{}'", entry.getKey());

      for (String instanceID : entry.getValue()) {
        logger.info(" - Instance '{}'", instanceID);
      }
    }
  }
}
