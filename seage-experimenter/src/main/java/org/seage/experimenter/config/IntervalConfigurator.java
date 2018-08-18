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

import java.util.ArrayList;
import java.util.List;

import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.data.DataNode;

/**
 *
 * @author Richard Malek
 */
public class IntervalConfigurator extends Configurator
{

    // private String _algID;
    private int _granularity;
    private DataNode _paramInfo;

    public IntervalConfigurator(int granularity)
    {
        //_algID = algID;
        _granularity = granularity;
    }

    @Override
    public ProblemConfig[] prepareConfigs(ProblemInfo problemInfo, String instanceID, String algorithmID,
            int numConfigs) throws Exception
    {

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
        algorithm.putValue("id", algorithmID);

        algorithm.putDataNode(params);
        problem.putDataNode(instance);
        config.putDataNode(algorithm);
        config.putDataNode(problem);

        for (DataNode inst : problemInfo.getDataNode("Instances").getDataNodes())
        {
            System.out.println(inst.getValue("path"));

            ProblemConfig instanceCfg = (ProblemConfig) config.clone();
            instanceCfg.getDataNode("Problem").getDataNode("Instance").putValue("name", inst.getValue("name"));
            instanceCfg.getDataNode("Problem").getDataNode("Instance").putValue("type", inst.getValue("type"));
            instanceCfg.getDataNode("Problem").getDataNode("Instance").putValue("path", inst.getValue("path"));

            List<List<Double>> returnValues = new ArrayList<List<Double>>();
            ArrayList<Double> parentValues = new ArrayList<Double>();
            ArrayList<String> paramsToAdd = new ArrayList<String>();
            _paramInfo = new DataNode("ParamInfo");

            for (DataNode paramNode : problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID)
                    .getDataNodes("Parameter"))
            {
                String name = paramNode.getValueStr("name");
                paramsToAdd.add(name);
                DataNode p = new DataNode(name);
                p.putValue("min", paramNode.getValue("min"));
                p.putValue("max", paramNode.getValue("max"));
                _paramInfo.putDataNode(p);
            }

            expand(parentValues, paramsToAdd, returnValues);

            //            for(int n=0;n<returnValues.size();n++){
            //                ProblemConfig r = (ProblemConfig) instanceCfg.clone();
            //                for(int i=0;i<_paramInfo.getDataNodes().size();i++){
            //                    r.getDataNode("Algorithm").getDataNode("Parameters").putValue(_paramInfo.getDataNodes().get(i).getName(), returnValues.get(n).get(i));
            //                }
            //                results.add(r);
            //            }
            values.addAll(returnValues);

            System.out.println("Mem: " + Runtime.getRuntime().totalMemory() / (1024 * 1024));
        }

        System.out.println("Saving ...");

        //        for(ProblemConfig cfg : results)
        //            XmlHelper.writeXml(cfg, "tmp/"+System.currentTimeMillis()+"-"+(i++));

        //        int num = results.size();
        int num = values.size();
        System.out.println("Total: " + num);
        System.out.println("Per core: " + num / 8);
        return results.toArray(new ProblemConfig[0]);
    }

    private void expand(List<Double> parentValues, List<String> paramsToAdd, List<List<Double>> results)
            throws Exception
    {
        List<String> paramsToAddNew = new ArrayList<String>();
        paramsToAddNew.addAll(paramsToAdd);
        String paramName = paramsToAddNew.remove(0);

        //        ProblemConfig n =  (ProblemConfig)parent.clone();

        for (int i = 0; i < _granularity; i++)
        {
            double min = _paramInfo.getDataNode(paramName).getValueDouble("min");
            double max = _paramInfo.getDataNode(paramName).getValueDouble("max");
            double val = min + (max - min) / (_granularity - 1) * i;
            //System.out.print(val+" ");
            List<Double> valueCfg = new ArrayList<Double>();
            valueCfg.addAll(parentValues);
            valueCfg.add(val);
            //ProblemConfig valueCfg = (ProblemConfig)parent.clone();
            //valueCfg.getDataNode("Algorithm").getDataNode("Parameters").putValue(_paramInfo.getDataNode(paramName).getName(), val);

            if (paramsToAddNew.size() > 0)
                expand(valueCfg, paramsToAddNew, results);
            else
                results.add(valueCfg);
        }

    }
}
