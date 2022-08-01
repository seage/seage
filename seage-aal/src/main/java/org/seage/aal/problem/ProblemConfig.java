/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.aal.problem;

import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.data.DataNode;

/**
 * A configuration structure with information how to solve a given problem.
 * 
 * @author Richard Malek
 * 
 *         ProblemConfig |_ problemID |_ Instance |_ Algorithm |_ Parameter |_
 *         ... |_ Parameter
 */
public class ProblemConfig extends DataNode {
  private static final String PROBLEM_STRING = "Problem";
  private static final String INSTANCE_STRING = "Instance";

  public ProblemConfig(String name) {
    super(name);
  }

  public ProblemConfig(DataNode dn) {
    super(dn);
  }

  public String getConfigID() throws Exception {
    return this.getValueStr("configID");
  }

  public String getProblemID() throws Exception {
    return this.getDataNode(PROBLEM_STRING).getValueStr("id");
  }

  public ProblemInstanceInfo getProblemInstanceInfo() throws Exception {
    return new ProblemInstanceInfo(this.getDataNode(PROBLEM_STRING).getDataNode(INSTANCE_STRING));
  }

  public String getProblemInstanceName() throws Exception {
    return this.getDataNode(PROBLEM_STRING).getDataNode(INSTANCE_STRING).getValueStr("name");
  }

  public String getAlgorithmID() throws Exception {
    return this.getDataNode("Algorithm").getValueStr("id");
  }

  public AlgorithmParams getAlgorithmParams() throws Exception {
    AlgorithmParams result =
        new AlgorithmParams(this.getDataNode("Algorithm").getDataNode("Parameters"));
    // result.putValue("problemID", this.getDataNode(PROBLEM_STRING).getValue("id"));
    // result.putValue(INSTANCE_STRING,
    // this.getDataNode(PROBLEM_STRING).getDataNode(INSTANCE_STRING).getValue("name"));
    return result;
  }

  public String getProblemInstanceID() throws Exception {
    return this.getDataNode(PROBLEM_STRING).getDataNode(INSTANCE_STRING).getValueStr("id");
  }

}
