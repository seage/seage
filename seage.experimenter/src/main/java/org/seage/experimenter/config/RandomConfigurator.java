/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.experimenter.config;

import java.util.ArrayList;
import java.util.List;
import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;

/**
 *
 * @author rick
 */
public class RandomConfigurator extends Configurator{

    private String _algID;
    private int _numConfigs;
    private DataNode _paramInfo;

    public RandomConfigurator(String algID, int numConfigs) {
        _algID = algID;
        _numConfigs = numConfigs;
    }

    @Override
    public ProblemConfig[] prepareConfigs(ProblemInfo problemInfo) throws Exception {

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
        algorithm.putValue("id", _algID);

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

            for(DataNode paramNode : problemInfo.getDataNode("Algorithms").getDataNodeById(_algID).getDataNodes("Parameter"))
            {
                String name = paramNode.getValueStr("name");
                DataNode p = new DataNode(name);
                p.putValue("min", paramNode.getValue("min"));
                p.putValue("max", paramNode.getValue("max"));
                _paramInfo.putDataNode(p);
            }

            for(int i=0;i<_numConfigs;i++)
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

        System.out.println("Saving ...");

        int i=0;
        for(ProblemConfig cfg : results)
            XmlHelper.writeXml(cfg, "tmp/"+System.currentTimeMillis()+"-"+(i++));

        int num = results.size();
        //int num = values.size();
        System.out.println("Total: " +num);
        System.out.println("Per core: " +num/8);
        return results.toArray(new ProblemConfig[0]);
    }


}
