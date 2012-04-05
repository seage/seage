/**
 * *****************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Common Public License v1.0 which accompanies this distribution,
 * and is available at http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors: Richard Malek - Initial implementation
 */
package org.seage;


import aglobe.platform.Platform;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.seage.aal.algorithm.IProblemProvider;
import org.seage.aal.algorithm.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.experimenter.Experimenter;
import org.seage.logging.LogHelper;
import org.seage.experimenter.reporting.ILogReport;
import org.seage.experimenter.reporting.LogReportCreator;
import org.seage.temp.AlgorithmTester;
import org.seage.temp.ClasspathTest;
/**
 *
 * @author rick
 */
public class Launcher {

    private static final Logger log = Logger.getLogger(Launcher.class.getPackage().getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            LogHelper.loadConfig("log.properties");
            log.info("Running");
            new Launcher().run(args);
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private void run(String[] args) throws Exception {
        if (args.length == 0) {
            usage();
            return;
        }

        if (args[0].equals("-list")) {
            list();
            return;
        }

        if (args[0].equals("-test")) {
            if (args.length == 1) {
                new AlgorithmTester().test();
            } else if (args.length == 2) {
                new AlgorithmTester().test(args[1]);
            } else if (args.length == 3) {
                new AlgorithmTester().test(args[1], args[2]);
            } else {
                usage();
            }
            return;
        }

        if (args[0].equals("-config")) {
            if (args.length == 2) {
                new AlgorithmTester().runFromConfigFile(args[1]);
            } else {
                usage();
            }
            return;
        }

        if (args[0].equals("-experiment")) {
//            if(args.length==2 )
//                new Experimenter().runExperiments(args[1]);
//            else
            if (args.length >= 5) {
//                List<String> algIDs = new ArrayList<String>();
//                for(int i=5;i<)
                new Experimenter().runExperiments(args[1], Integer.parseInt(args[2]), Long.parseLong(args[3]), Arrays.copyOfRange(args, 4, args.length));

            } else if (args.length == 4) {
                new Experimenter().runExperiments(args[1], Integer.parseInt(args[2]), Long.parseLong(args[3]));
            } else {
                usage();
            }
            return;
        }

        if (args[0].equals("-agents")) {
            agents(args[1]);
            return;
        }
        
        if (args[0].equals("-report")) {
            ILogReport reporter = new LogReportCreator();
            reporter.report();
            return;
        }
        
        if (args[0].equals("-cp")) {
            new ClasspathTest().main(args);
            return;
        }

        usage();
    }

    private void usage() {
        System.out.println("Usage:");
        System.out.println("------");
        System.out.println("java -jar seage.launcher.jar {params}\n");
        System.out.println("params:");
        System.out.println("\t-list");
        System.out.println("\t-test [problem-id [algorithm-id]]");
        System.out.println("\t-config path-to-config");
        System.out.println("\t-experiment problem-id num-runs timeoutS [algorithm-id [algorithm-id]*] ");
        System.out.println("\t-agents path-to-agent-config-xml");
        System.out.println("\t-report");
    }

    private void list() throws Exception {
        System.out.println("List of implemented problems and algorithms:");
        System.out.println("--------------------------------------------");

        DataNode problems = new DataNode("Problems");
        Map<String, IProblemProvider> providers = ProblemProvider.getProblemProviders();

        for (String problemId : providers.keySet()) {
            try {
                IProblemProvider pp = providers.get(problemId);
                DataNode pi = pp.getProblemInfo();
                problems.putDataNode(pi);

                String name = pi.getValueStr("name");
                System.out.println(name);

                System.out.println("\talgorithms:");
                for (DataNode alg : pi.getDataNode("Algorithms").getDataNodes()) {
                    System.out.println("\t\t" + alg.getValueStr("id")/*
                             * +" ("+alg.getValueStr("id")+")"
                             */);

                    //System.out.println("\t\t\tparameters:");
                    for (DataNode param : alg.getDataNodes("Parameter")) {
                        System.out.println("\t\t\t"
                                + param.getValueStr("name") + "  ("
                                + param.getValueStr("min") + ", "
                                + param.getValueStr("max") + ", "
                                + param.getValueStr("init") + ")");
                    }
                }
                System.out.println("\tinstances:");
                for (DataNode inst : pi.getDataNode("Instances").getDataNodes()) {
                    System.out.println("\t\t" + inst.getValueStr("type") + "=" + inst.getValueStr("path")/*
                             * +" ("+alg.getValueStr("id")+")"
                             */);
                }

                System.out.println();
            } catch (Exception ex) {
                log.log(Level.SEVERE, problemId + ": " + ex.getMessage(), ex);
            }
            //XmlHelper.writeXml(problems, "problems.xml");
        }
    }

    private void agents(String config) {
        Platform.main(new String[]{"-name", "SEAGE", "-topics", "-gui", "-p", "config=" + config, "ProblemAgent:org.seage.ael.agent.ProblemAgent"});
    }
}
