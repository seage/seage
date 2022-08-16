/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation - Added problem
 * annotations
 */

package org.seage.problem.fsp;

import java.io.FileInputStream;
import java.io.InputStream;
import org.seage.aal.Annotations;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.aal.problem.ProblemMetadataGenerator;
import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.JspProblemProvider;
import org.seage.problem.jsp.JspScheduleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * @author Richard Malek
 */
@Annotations.ProblemId("FSP")
@Annotations.ProblemName("Flow Shop Scheduling Problem")
public class FspProblemProvider extends JspProblemProvider {
  private static Logger logger = LoggerFactory.getLogger(FspProblemProvider.class.getName());

  @Override
  public FspJobsDefinition initProblemInstance(ProblemInstanceInfo instanceInfo) throws Exception
  {
    ProblemInstanceOrigin origin = instanceInfo.getOrigin();
    String path = instanceInfo.getPath();

    InputStream stream0;
    if (origin == ProblemInstanceOrigin.RESOURCE) {
      stream0 = getClass().getResourceAsStream(path);
    } else {
      stream0 = new FileInputStream(path);
    }
    
    FspJobsDefinition jobsDefinition = null;
    
    try (InputStream stream = stream0) {
      jobsDefinition = new FspJobsDefinition(instanceInfo, stream);
    } catch (Exception ex) {
      logger.error(
          "FspProblemProvider.initProblemInstance - creating FspJobsDefinition failed, path: {}",
          path);
      throw ex;
    }
    return jobsDefinition;
  }


  
  @Override
  public ProblemMetadataGenerator<JspPhenotype> initProblemMetadataGenerator() {
    return new FspProblemsMetadataGenerator(this);
  }

  // TODO: Remove after testing
  /**
   * .
   * @param args .
   */
  public static void main(String[] args) {
    try {
      // String iid = "rm3_3_01";
      // String iid = "tai20_05_01";
      String iid = "tai500_20_01";
      FspProblemProvider provider = new FspProblemProvider();
      ProblemInfo pi = provider.getProblemInfo();
      ProblemInstanceInfo pii = pi.getProblemInstanceInfo(iid);
      JspJobsDefinition instance = provider.initProblemInstance(pii);
      JspPhenotypeEvaluator jspEval = new JspPhenotypeEvaluator(pi, instance);
      JspPhenotype schedule1 = JspScheduleProvider.createGreedySchedule(jspEval, instance);
      JspPhenotype schedule2 = JspScheduleProvider.createRandomSchedule(jspEval, instance, 1);

      System.out.println("greedy: " + schedule1.getObjValue());
      System.out.println("random: " + schedule2.getObjValue());
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
