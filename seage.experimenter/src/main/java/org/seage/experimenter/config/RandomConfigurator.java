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
public class RandomConfigurator extends Configurator{

    private String _algID;
    public static int numberOfExperiments = 10;

    public RandomConfigurator(String algID) {
        _algID = algID;
    }

    @Override
    public ProblemConfig[] prepareConfigs(ProblemInfo problemInfo) throws Exception {
        ArrayList<ProblemConfig> results = new ArrayList<ProblemConfig>();

//        for(DataNode dn : problemInfo.getDataNode("Algorithms").getDataNodeById(_algID).getDataNodes("Parameter"))
//        {
//            
//        }
        ProblemConfig[] pattern = new ProblemConfig[numberOfExperiments];
//        ProblemConfig pattern = new ProblemConfig("Config");

//        DataNode[numberOfExperiments] algorithm = new DataNode[numberOfExperiments];
        DataNode[] algorithm = new DataNode[numberOfExperiments];
//        DataNode algorithm = new DataNode("Algorithm");
//        algorithm.putValue("id", _algID);

        DataNode[] params = new DataNode[numberOfExperiments];
//        DataNode params = new DataNode("Parameters");
        for(int i=0;i<params.length;i++){
            algorithm[i] = new DataNode("Algorithm");
            algorithm[i].putValue("id",_algID);
            pattern[i] = new ProblemConfig("Config");
            params[i]=new DataNode("Parameters");
            for(DataNode dn : problemInfo.getDataNode("Algorithms").getDataNodeById(_algID).getDataNodes("Parameter")){
                params[i].putValue(dn.getValueStr("name"), between(dn.getValue("min"),dn.getValue("max")));
            }
            algorithm[i].putDataNode(params[i]);
            pattern[i].putDataNode(algorithm[i]);
        }

//        algorithm.putDataNode(params);
//        pattern.putDataNode(algorithm);


        for(DataNode inst : problemInfo.getDataNode("Instances").getDataNodes())
        {
            for(int i=0;i<numberOfExperiments; i++){
                ProblemConfig config = (ProblemConfig)pattern[i].clone();

                DataNode problem = new DataNode("Problem");
                problem.putValue("id", problemInfo.getValue("id"));
                problem.putDataNode(inst);

                config.putDataNode(problem);

                results.add(config);
            }
        }

        return results.toArray(new ProblemConfig[0]);
    }

    private Object between(Object value1, Object value2) {
        Double min = (Double)value1;
        Double max = (Double)value2;
        Double generated = Math.random()*(max-min)+min;
        return (Object)generated;
    }

}
