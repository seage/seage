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

package org.seage.aal.data;

import org.seage.data.DataNode;

/**
 * A configuration structure with information how to solve a given  problem
 * @author Richard Malek
 * 
 * ProblemConfig
 *  |_ problemID
 *  |_ instance
 *  |_Algorithm
 *     |_ Parameter
 *     |_ ...
 *     |_ Parameter
 */
public class ProblemConfig extends DataNode{

    /**
	 * 
	 */
	private static final long serialVersionUID = -5927850750546609837L;

	public ProblemConfig(String name) {
        super(name);
    }

    public ProblemConfig(DataNode dn) {
        super(dn);
    }
    
    public AlgorithmParams getAlgorithmParams() throws Exception
    {
        AlgorithmParams result = new AlgorithmParams(this.getDataNode("Algorithm"));
        result.putValue("problemID", this.getDataNode("Problem").getValue("id"));
        result.putValue("instance", this.getDataNode("Problem").getDataNode("Instance").getValue("name"));
        return result;
    }
    
    public String getConfigID() throws Exception
    {
    	return this.getValueStr("configID");
    }
    
    public String getProblemID() throws Exception
    {
    	return this.getDataNode("Problem").getValueStr("id");
    }
    
    public String getInstanceName() throws Exception
    {
    	return this.getDataNode("Algorithm").getValueStr("id");
    }
    
    public String getAlgorithmID() throws Exception
    {
    	return this.getDataNode("Problem").getDataNode("Instance").getValueStr("name");
    }
    
}
