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
 * SEAGE. If not, see <http://www.gnu.org/licenses/>.
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
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.problem.jsp.JspProblemProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
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
    if (origin == ProblemInstanceOrigin.RESOURCE)
      stream0 = getClass().getResourceAsStream(path);
    else
      stream0 = new FileInputStream(path);
    
    FspJobsDefinition jobsDefinition = null;
    
    try(InputStream stream=stream0)
    {
        jobsDefinition = new FspJobsDefinition(instanceInfo, stream);   
    }
    catch (Exception ex)
    {
      logger.error("FspProblemProvider.initProblemInstance - creating FspJobsDefinition failed, path: {}", path);
      throw ex;
    }

    return jobsDefinition;
  }

  // TODO: Remove after testing
  public static void main(String[] args) {    
    try {
      FspProblemProvider provider = new FspProblemProvider();
      provider.initProblemInstance(new ProblemInstanceInfo(
        "100x20_01", ProblemInstanceOrigin.RESOURCE, "/org/seage/problem/fsp/instances/100x20_01.txt"
      ));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
