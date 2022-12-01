package org.seage.hh.experimenter.singlealgorithm.feedback;

import org.junit.jupiter.api.Test;
import org.seage.hh.experimenter.configurator.NewFeedbackConfigurator;

public class NewFeedbackConfiguratorTest {

  @Test
  public void testDb() throws Exception {
    NewFeedbackConfigurator nfc = new NewFeedbackConfigurator();

    nfc.getBestExperimentTasks("TSP", "TabuSearch", 1);
  }
}