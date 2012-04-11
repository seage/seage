/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.seage.org/license/cpl-v10.html
 *
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
}
