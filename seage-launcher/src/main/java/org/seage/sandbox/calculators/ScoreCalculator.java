package org.seage.sandbox.calculators;

import java.util.List;
import org.seage.data.DataNode;

public interface ScoreCalculator {
  public List<DataNode> calculateScore(DataNode results) throws Exception;
}
