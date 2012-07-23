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
package org.seage.experimenter.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.data.file.FileHelper;
import org.seage.data.xml.XmlHelper;

/**
 *
 * @author rick
 */
public class RandomConfigurator extends Configurator{

    //private String _algID;
    //private int _numConfigs;
    private DataNode _paramInfo;

//    public RandomConfigurator() {
//        _algID = algID;
//        _numConfigs = numConfigs;
//    }

    @Override
    public ProblemConfig[] prepareConfigs(ProblemInfo problemInfo, String algID, int numConfigs) throws Exception {

        List<ProblemConfig> results = new ArrayList<ProblemConfig>();
        List<List<Double>> values = new ArrayList<List<Double>>();

        // Config
        ProblemConfig config = new ProblemConfig("Config");
        //  |_ Problem
        DataNode problem = new DataNode("Problem");
        //  |   |_ Instance
        DataNode instance = new DataNode("Instance");
        //  |_ Algorithm
        DataNode algorithm = new DataNode("Algorithm");
        //      |_ Parameters
        DataNode params = new DataNode("Parameters");

        problem.putValue("id", problemInfo.getValue("id"));
        algorithm.putValue("id", algID);

        algorithm.putDataNode(params);
        problem.putDataNode(instance);
        config.putDataNode(algorithm);
        config.putDataNode(problem);


        for(DataNode inst : problemInfo.getDataNode("Instances").getDataNodes())
        {
            System.out.println(inst.getValue("path"));

            ProblemConfig instanceCfg = (ProblemConfig)config.clone();
            instanceCfg.getDataNode("Problem").getDataNode("Instance").putValue("name", inst.getValue("name"));
            instanceCfg.getDataNode("Problem").getDataNode("Instance").putValue("type", inst.getValue("type"));
            instanceCfg.getDataNode("Problem").getDataNode("Instance").putValue("path", inst.getValue("path"));

            _paramInfo = new DataNode("ParamInfo");

            for(DataNode paramNode : problemInfo.getDataNode("Algorithms").getDataNodeById(algID).getDataNodes("Parameter"))
            {
                String name = paramNode.getValueStr("name");
                DataNode p = new DataNode(name);
                p.putValue("min", paramNode.getValue("min"));
                p.putValue("max", paramNode.getValue("max"));
                _paramInfo.putDataNode(p);
            }

            for(int i=0;i<numConfigs;i++)
            {
                //ArrayList<Double> paramList = new ArrayList<Double>();
                ProblemConfig r = (ProblemConfig) instanceCfg.clone();
                for(int j=0;j<_paramInfo.getDataNodes().size();j++)
                {
                    String paramName = _paramInfo.getDataNodes().get(j).getName();
                    double min = _paramInfo.getDataNode(paramName).getValueDouble("min");
                    double max = _paramInfo.getDataNode(paramName).getValueDouble("max");
                    double val =  min + (max-min)*Math.random();
                    r.getDataNode("Algorithm").getDataNode("Parameters").putValue(paramName, val);
                    //paramList.add(val);
                }
                //values.add(paramList);
                r.putValue("configID", FileHelper.md5fromString(XmlHelper.getStringFromDocument(r.toXml())));
                results.add(r);
            }

//            for(int n=0;n<values.size();n++){
//
//                for(int i=0;i<_paramInfo.getDataNodes().size();i++){
//                    r.getDataNode("Algorithm").getDataNode("Parameters").putValue(_paramInfo.getDataNodes().get(i).getName(), values.get(n).get(i));
//                }
//                results.add(r);
//            }

            System.out.println("Mem: " +Runtime.getRuntime().totalMemory()/(1024*1024));
         }

//        System.out.println("Saving ...");
//        new File("tmp").mkdirs();
//        int i=0;
//        for(ProblemConfig cfg : results)
//            XmlHelper.writeXml(cfg, "tmp/"+System.currentTimeMillis()+"-"+(i++));

        int num = results.size();
        //int num = values.size();
        System.out.println("Total: " +5*num);
        System.out.println("Per core: " +5*num/Runtime.getRuntime().availableProcessors());
        return results.toArray(new ProblemConfig[0]);
    }


}
