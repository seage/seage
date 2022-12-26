package org.seage.hh.experimenter.singlealgorithm.feedback;

import org.junit.jupiter.api.Test;
import java.util.List;
import org.seage.hh.knowledgebase.db.DbManager;
import org.seage.hh.experimenter.configurator.FeedbackConfigurator;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;


public class NewFeedbackConfiguratorTest {

  @Test
  public void testDb() throws Exception {
    DbManager.init();

    FeedbackConfigurator nfc = new FeedbackConfigurator();

    List<ExperimentTaskRecord> output = nfc.getBestExperimentTasks("TSP", "TabuSearch", 10);
    System.out.println(output);
  }
}