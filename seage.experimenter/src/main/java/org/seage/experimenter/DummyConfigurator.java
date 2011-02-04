/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.experimenter;

import org.seage.data.DataNode;

/**
 *
 * @author rick
 */
public class DummyConfigurator extends Configurator{

    //private String _problemID;
    private String _algID;

    public DummyConfigurator(/*String problemID,*/ String algID) {
        //_problemID = problemID;
        _algID = algID;
    }


    @Override
    public DataNode[] prepareConfigs(DataNode problemInfo) throws Exception {
        DataNode result = new DataNode("Config");
        //result.putDataNode(problemInfo.getDataNode("Algorithms").getDataNodeById(_algID));
        DataNode problem = new DataNode("Problem");
        problem.putValue("id", problemInfo.getValue("id"));
        problem.putDataNode(problemInfo.getDataNode("Instances").getDataNode("Instance", 0));
        
        DataNode algorithm = new DataNode("Algorithm");
        algorithm.putValue("id", _algID);

        DataNode params = new DataNode("Parameters");
        for(DataNode dn : problemInfo.getDataNode("Algorithms").getDataNodeById(_algID).getDataNodes("Parameter"))
            params.putValue(dn.getValueStr("name"), dn.getValue("init"));

        algorithm.putDataNode(params);

        result.putDataNode(problem);
        result.putDataNode(algorithm);

        return new DataNode[]{result};
    }

}
