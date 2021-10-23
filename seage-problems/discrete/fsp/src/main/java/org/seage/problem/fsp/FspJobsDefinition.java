package org.seage.problem.fsp;

import java.io.InputStream;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.problem.jssp.JobsDefinition;

public class FspJobsDefinition extends JobsDefinition {

  public FspJobsDefinition(ProblemInstanceInfo instanceInfo, InputStream jobsDefinitionStream)
      throws Exception {
    super(instanceInfo, jobsDefinitionStream);
  }

  @Override
  protected void createJobInfos(InputStream jobsDefinitionStream) throws Exception {
    
  }

}
