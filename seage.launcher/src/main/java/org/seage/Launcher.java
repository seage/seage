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

import aglobe.platform.Platform;
import java.util.Map;
import org.seage.aal.IProblemProvider;
import org.seage.aal.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;
import org.seage.experimenter.AlgorithmTester;

/**
 *
 * @author rick
 */
public class Launcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            new Launcher().run(args);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void run(String[] args) throws Exception
    {
        if(args.length == 0)
        {
            usage();
            return;
        }
        if(args[0].equals("-list"))
        {
            list();
            return;
        }
        if(args[0].equals("-test"))
        {
            if(args.length == 1)
            {
                new AlgorithmTester().test();
                return;
            }
            if(args.length==3 && args[1].equals("-problem"))
            {
                new AlgorithmTester().test(args[2]);
                return;
            }
            usage();
        }
        if(args[0].equals("-agents"))
        {
            agents(args[1]);
            return;
        }
    }

    private void usage()
    {
        System.out.println("Usage:");
        System.out.println("------");
        System.out.println("java -jar seage.launcher.jar {params}\n");
        System.out.println("params:");
        System.out.println("\t-list");
        System.out.println("\t-test [-problem problem-id]");
        System.out.println("\t-agents path-to-agent-config-xml");
    }

    private void list() throws Exception
    {
        System.out.println("List of implemented problems and algorithms:");
        System.out.println("--------------------------------------------");

        DataNode problems = new DataNode("Problems");
        Map<String, IProblemProvider> providers = ProblemProvider.getProblemProviders();

        for(String problemId : providers.keySet())
        {
            try
            {
                IProblemProvider pp = providers.get(problemId);
                DataNode pi = pp.getProblemInfo();
                problems.putDataNode(pi);
                
                String name = pi.getValueStr("name");
                System.out.println(name);

                System.out.println("\talgorithms:");
                for(DataNode alg : pi.getDataNode("Algorithms").getDataNodes())
                {
                    System.out.println("\t\t"+alg.getValueStr("id")/*+" ("+alg.getValueStr("id")+")"*/);

                    //System.out.println("\t\t\tparameters:");
                    for(DataNode param : alg.getDataNodes("Parameter"))
                        System.out.println("\t\t\t"+
                            param.getValueStr("name")+"  ("+
                            param.getValueStr("min")+", "+
                            param.getValueStr("max")+", "+
                            param.getValueStr("init")+")");
                }
                System.out.println("\tinstances:");
                for(DataNode inst : pi.getDataNode("Instances").getDataNodes())
                    System.out.println("\t\t"+inst.getValueStr("type")+"="+inst.getValueStr("path")/*+" ("+alg.getValueStr("id")+")"*/);

                System.out.println();
            }
            catch(Exception ex)
            {
                System.err.println(problemId+": "+ex.getMessage());
            }
            XmlHelper.writeXml(problems, "problems.xml");
        }
    }


    private void agents(String config)
    {
        Platform.main(new String[]{"-name", "SEAGE", "-topics", "-gui", "-p", "config="+config, "ProblemAgent:org.seage.ael.agent.ProblemAgent" });
    }
}
