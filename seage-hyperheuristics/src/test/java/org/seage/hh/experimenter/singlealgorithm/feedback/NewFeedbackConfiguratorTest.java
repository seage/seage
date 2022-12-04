package org.seage.hh.experimenter.singlealgorithm.feedback;

import org.junit.jupiter.api.Test;
import java.util.List;
import org.seage.hh.experimenter.configurator.NewFeedbackConfigurator;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;


public class NewFeedbackConfiguratorTest {

  @Test
  public void testDb() throws Exception {
    NewFeedbackConfigurator nfc = new NewFeedbackConfigurator();

    List<ExperimentTaskRecord> output = nfc.getBestExperimentTasks("TSP", "TabuSearch", 10);
    System.out.println(output);
  }
}