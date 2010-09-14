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

import aglobe.platform.Platform;
import java.util.Map;
import org.seage.aal.IProblemProvider;
import org.seage.aal.ProblemProvider;
import org.seage.data.DataNode;

/**
 *
 * @author rick
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            new Main().run(args);
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
            new AlgorithmTester().test();
            return;
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
        System.out.println("java -jar seage.experimenter.jar {params}\n");
        System.out.println("params:");
        System.out.println("\t-list");
        System.out.println("\t-test");
        System.out.println("\t-agents path-to-agent-config-xml");
    }

    private void list() throws Exception
    {
        System.out.println("List of implemented problems and algorithms:");
        System.out.println("--------------------------------------------");

        Map<String, IProblemProvider> providers = ProblemProvider.getProblemProviders();

        for(String problemId : providers.keySet())
        {
            try
            {
                IProblemProvider pp = providers.get(problemId);
                DataNode pi = pp.getProblemInfo();
                String name = pi.getValueStr("name");
                System.out.println(name);

                for(DataNode alg : pi.getDataNode("Algorithms").getDataNodes())
                    System.out.println("\t"+alg.getValueStr("id")/*+" ("+alg.getValueStr("id")+")"*/);
            }
            catch(Exception ex)
            {
                System.err.println(problemId+": "+ex.getMessage());
            }
        }
    }


    private void agents(String config)
    {
        Platform.main(new String[]{"-name", "SEAGE", "-topics", "-gui", "-p", "config="+config, "ProblemAgent:org.seage.ael.agent.ProblemAgent" });
    }
}
