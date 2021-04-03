package org.seage.calculators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.seage.data.DataNode;

public class UnitMetricScoreCalculator implements ScoreCalculator {
  /**
   * Map represents weights for each problem domain.
   */
  @SuppressWarnings("serial")
  private final Map<String, Double> problemsWeightsMap = new HashMap<>() {
    {
      put("SAT", 1.0);
      put("TSP", 1.0);
    }
  };

  @Override
  public List<DataNode> calculateScore(List<DataNode> card) throws Exception {
    //todo
    return null;
  }
}
