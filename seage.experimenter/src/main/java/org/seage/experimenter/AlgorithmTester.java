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
package org.seage.experimenter;

import java.util.Map;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.IProblemProvider;
import org.seage.aal.ProblemProvider;
import org.seage.data.DataNode;

/**
 *
 * @author RMalek
 */
public class AlgorithmTester {

    private Map<String, IProblemProvider> _providers ;
    
    public AlgorithmTester() throws Exception
    {
        _providers = ProblemProvider.getProblemProviders();
    }

    public void test() throws Exception
    {
        System.out.println("Testing algorithms:");
        System.out.println("-------------------");        

        for(String problemId : _providers.keySet())
        {          
            testProblem(_providers.get(problemId));
        }
    }

    public void test(String problemId)
    {
        System.out.println("Testing algorithms:");
        System.out.println("-------------------");

        testProblem(_providers.get(problemId));
    }

    private void testProblem( IProblemProvider provider)
    {
        try
        {            
            DataNode pi = provider.getProblemInfo();
            String problemName = pi.getValueStr("name");
            System.out.println(problemName);

            for(DataNode alg : pi.getDataNode("Algorithms").getDataNodes())
            {
                try {
                    System.out.print("\t" + alg.getValueStr("name"));
                    IAlgorithmFactory factory = provider.getAlgorithmFactory(alg.getValueStr("id"));
                    
                    DataNode config = new DummyConfigurator( alg.getValueStr("id")).prepareConfigs(pi)[0];
                    IAlgorithmAdapter algorithm = factory.createAlgorithm(config);
                    algorithm.solutionsFromPhenotype(provider.generateInitialSolutions(config.getDataNode("Parameters").getValueInt("numSolutions")));
                    algorithm.startSearching(config.getDataNode("Parameters"));

                    System.out.println("\t"+"OK");

                } catch (Exception ex) {
                    System.out.println("\t"+"FAIL");
                    ex.printStackTrace();
                    //System.err.println(problemId+"/"+alg.getValueStr("id")+": "+ex.toString());
                }
                //System.out.println("\t"+alg.getValueStr("id")/*+" ("+alg.getValueStr("id")+")"*/);
            }
        }
        catch(Exception ex)
        {
            //System.err.println(problemId+": "+ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}
