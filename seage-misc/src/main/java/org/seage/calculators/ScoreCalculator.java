package org.seage.calculators;

import java.util.List;
import org.seage.data.DataNode;

public interface ScoreCalculator {
  public List<DataNode> calculateScore(String experimentId) throws Exception;
}
