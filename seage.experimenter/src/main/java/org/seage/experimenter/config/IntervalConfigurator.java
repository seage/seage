/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.experimenter.config;

import java.util.ArrayList;
import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInfo;
import org.seage.data.DataNode;

/**
 *
 * @author rick
 */
public class IntervalConfigurator extends Configurator{

    private String _algID;

    public IntervalConfigurator(String algID) {
        _algID = algID;
    }

    @Override
    public ProblemConfig[] prepareConfigs(ProblemInfo problemInfo) throws Exception {
        ArrayList<ProblemConfig> results = new ArrayList<ProblemConfig>();

//        for(DataNode dn : problemInfo.getDataNode("Algorithms").getDataNodeById(_algID).getDataNodes("Parameter"))
//        {
//            
//        }
        
        ProblemConfig pattern = new ProblemConfig("Config");      
        
        DataNode algorithm = new DataNode("Algorithm");
        algorithm.putValue("id", _algID);

        DataNode params = new DataNode("Parameters");
        for(DataNode dn : problemInfo.getDataNode("Algorithms").getDataNodeById(_algID).getDataNodes("Parameter"))
            params.putValue(dn.getValueStr("name"), dn.getValue("init"));

        algorithm.putDataNode(params);
        pattern.putDataNode(algorithm);

        for(DataNode inst : problemInfo.getDataNode("Instances").getDataNodes())
        {
            ProblemConfig config = (ProblemConfig)pattern.clone();

            DataNode problem = new DataNode("Problem");
            problem.putValue("id", problemInfo.getValue("id"));
            problem.putDataNode(inst);

            config.putDataNode(problem);

            results.add(config);
        }

        return results.toArray(new ProblemConfig[0]);
    }

}
