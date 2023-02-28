/**
 * 
 * @author David Omrai
 */

package org.seage.hh.experimenter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ExperimentScoreTable {
  Map<String, ExperimentScoreCard> scoreTable;

  public ExperimentScoreTable() {
    this.scoreTable = new HashMap<>();
  }

  public ExperimentScoreCard getExperiment(String algorithmID) {
    return scoreTable.get(algorithmID);
  }

  public void addExperiment(String algorithmID, ExperimentScoreCard experiment){
    this.scoreTable.put(algorithmID, experiment);
  }

  public Set<Entry<String, ExperimentScoreCard>> entrySet() {
    return this.scoreTable.entrySet();
  }
}
