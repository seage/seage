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

import org.seage.data.DataNode;

/**
 * A structure describing a problem (name, id, available algorithms and instances)
 * @author rick
 * 
 * ProblemInfo
 *  |_ id
 *  |_ name
 *  |_ class
 *  |_ Algorithms
 *  |   |_ Algorithm
 *  |   |   |_ id
 *  |   |   |_ name
 *  |   |   |_ factoryClass
 *  |   |   |_ Parameter
 *  |   |   |   |_ name
 *  |   |   |   |_ max
 *  |   |   |   |_ min
 *  |   |   |   |_ init
 *  |   |   |_ ...
 *  |   |   |_ Parameter
 *  |   |_ Algorithm
 *  |       |_ ...
 *  |_ Instances
 *      |_ Instance
 *      |	|_ id
 *      |	|_ name
 *      |   |_ type ("file" | "resource")
 *      |   |_ path
 *      |_ ...
 *      |_ Instance
 */
public class ProblemInfo extends DataNode
{       
    
	private static final long serialVersionUID = 7658277873739208135L;

	public ProblemInfo(String name) {
        super(name);
    }
	
	public String getProblemID() throws Exception
	{
		return getValueStr("id");
	}
	
	public ProblemInstanceInfo getProblemInstanceInfo(String instanceID) throws Exception
	{
		DataNode dn = getDataNode("Instances").getDataNodeById(instanceID);
		if(dn == null)
			throw new Exception("ProblemInfo does not contain any information on instanceID: "+instanceID);
		return new ProblemInstanceInfo(dn);
	}

}
