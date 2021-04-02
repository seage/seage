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
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
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
 * A configuration structure with information how to solve a given problem
 * 
 * @author Richard Malek
 * 
 *         ProblemConfig |_ problemID |_ Instance |_ Algorithm |_ Parameter |_
 *         ... |_ Parameter
 */
public class ProblemConfig extends DataNode {

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
    return this.getDataNode("Problem").getValueStr("id");
  }

  public ProblemInstanceInfo getProblemInstanceInfo() throws Exception {
    return new ProblemInstanceInfo(this.getDataNode("Problem").getDataNode("Instance"));
  }

  public String getProblemInstanceName() throws Exception {
    return this.getDataNode("Problem").getDataNode("Instance").getValueStr("name");
  }

  public String getAlgorithmID() throws Exception {
    return this.getDataNode("Algorithm").getValueStr("id");
  }

  public AlgorithmParams getAlgorithmParams() throws Exception {
    AlgorithmParams result = new AlgorithmParams(this.getDataNode("Algorithm").getDataNode("Parameters"));
    // result.putValue("problemID", this.getDataNode("Problem").getValue("id"));
    // result.putValue("instance",
    // this.getDataNode("Problem").getDataNode("Instance").getValue("name"));
    return result;
  }

  public String getProblemInstanceID() throws Exception {
    return this.getDataNode("Problem").getDataNode("Instance").getValueStr("id");
  }

}
