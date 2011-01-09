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
        result.putDataNode(problemInfo.getDataNode("Algorithms").getDataNodeById(_algID));
        result.putDataNode(problemInfo.getDataNode("Instances").getDataNode("Instance", 0));
        return new DataNode[]{result};
    }

}
