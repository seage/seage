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
package org.seage;

import java.util.Map;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.IProblemProvider;
import org.seage.aal.ProblemInstance;
import org.seage.aal.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.experimenter.DummyConfigurator;

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
    
    public void test(String problemId, String algorithmId) throws Exception
    {
        System.out.println("Testing algorithms:");
        System.out.println("-------------------");
        
        IProblemProvider provider = _providers.get(problemId);
        DataNode pi = provider.getProblemInfo();
        String problemName = pi.getValueStr("name");
        System.out.println(problemName);

        testProblem(provider, pi, pi.getDataNode("Algorithms").getDataNodeById(algorithmId));
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
                testProblem(provider, pi, alg);
                //System.out.println("\t"+alg.getValueStr("id")/*+" ("+alg.getValueStr("id")+")"*/);
            }
        }
        catch(Exception ex)
        {
            //System.err.println(problemId+": "+ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
    
    private void testProblem(IProblemProvider provider, DataNode pi, DataNode alg)
    {
        String algName = "";
        try {
            algName = alg.getValueStr("name");
            System.out.print("\t" + algName);
            
            DataNode config = new DummyConfigurator( alg.getValueStr("id")).prepareConfigs(pi)[0];
            
            IAlgorithmFactory factory = provider.getAlgorithmFactory(alg.getValueStr("id"));
            
            ProblemInstance instance = provider.initProblemInstance(config);
            IAlgorithmAdapter algorithm = factory.createAlgorithm(instance, config);
            DataNode algNode = config.getDataNode("Algorithm");
            Object[][] solutions = provider.generateInitialSolutions(algNode.getDataNode("Parameters").getValueInt("numSolutions"), instance);
            algorithm.solutionsFromPhenotype(solutions);
            algorithm.startSearching(algNode.getDataNode("Parameters"));
            solutions = algorithm.solutionsToPhenotype();
            algorithm.solutionsFromPhenotype(solutions);
            algorithm.startSearching(algNode.getDataNode("Parameters"));

            System.out.printf("%"+(50-algName.length())+"s","OK\n");

        } catch (Exception ex) {
            System.out.printf("%"+(52-algName.length())+"s","FAIL\n");
            //System.out.println("\t"+"FAIL");
            ex.printStackTrace();
            //System.err.println(problemId+"/"+alg.getValueStr("id")+": "+ex.toString());
        }
    }
}
