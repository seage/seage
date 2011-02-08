/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.aal.data;

import org.seage.data.DataNode;

/**
 *
 * @author rick
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
