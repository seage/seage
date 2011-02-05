/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.experimenter.config;

import org.seage.experimenter.config.Configurator;
import org.seage.aal.ProblemConfig;
import org.seage.aal.ProblemInfo;
import org.seage.data.DataNode;

/**
 *
 * @author rick
 */
public class DefaultConfigurator extends Configurator{

    //private String _problemID;
    private String _algID;

    public DefaultConfigurator(String algID) {
        _algID = algID;
    }


    @Override
    public ProblemConfig[] prepareConfigs(ProblemInfo problemInfo) throws Exception {
        ProblemConfig result = new ProblemConfig("Config");
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

        return new ProblemConfig[]{result};
    }

}
