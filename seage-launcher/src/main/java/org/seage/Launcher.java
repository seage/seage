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
 * Contributors: Richard Malek - Initial implementation
 */
package org.seage;


//import aglobe.platform.Platform;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.seage.aal.algorithm.IProblemProvider;
import org.seage.aal.algorithm.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.experimenter.AdaptiveExperimenter;
import org.seage.logging.LogHelper;
import org.seage.experimenter.reporting.LogReportCreator;
import org.seage.experimenter.reporting.ReportManager;
import org.seage.experimenter.reporting.StatisticalReportCreator;
import org.seage.experimenter.reporting.rapidminer.ExperimentLogImporter;
import org.seage.experimenter.reporting.rapidminer.RMTest;
import org.seage.experimenter.singlealgorithm.SingleAlgorithmExperimenter;
import org.seage.temp.AlgorithmTester;
import org.seage.temp.ClasspathTest;
/**
 *
 * @author rick
 */
public class Launcher {

    private static final Logger _logger = Logger.getLogger(Launcher.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            LogHelper.loadConfig("log.properties");
            _logger.info("SEAGE running ...");
            new Launcher().run(args);
            _logger.info("SEAGE finished ...");
        } catch (Exception ex) {
            _logger.log(Level.SEVERE, ex.getMessage(), ex);
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
            
            int indexCounter = 0;
            if(args[1].equals("-a"))
            {
                indexCounter++;
                if (args.length >= (5+indexCounter))
                {
                    new AdaptiveExperimenter().runExperiment(Integer.parseInt(args[2+indexCounter]), Long.parseLong(args[3+indexCounter]), args[1+indexCounter], Arrays.copyOfRange(args, 4+indexCounter, args.length));

                } else if (args.length == (4+indexCounter))
                {
                    new AdaptiveExperimenter().runExperiment(Integer.parseInt(args[2+indexCounter]), Long.parseLong(args[3+indexCounter]), args[1+indexCounter]);
                } 
                else
                {
                    usage();
                }
            }
            else
            {            
                if (args.length >= (5+indexCounter))
                {
    //                List<String> algIDs = new ArrayList<String>();
    //                for(int i=5;i<)
                    new SingleAlgorithmExperimenter().runExperiment(Integer.parseInt(args[2]), Long.parseLong(args[3]), args[1], Arrays.copyOfRange(args, 4, args.length));

                } else if (args.length == (4+indexCounter)) {
                    new SingleAlgorithmExperimenter().runExperiment(Integer.parseInt(args[2]), Long.parseLong(args[3]), args[1] );
                } else {
                    usage();
                }
            }
            return;
        }
        if (args[0].equals("-experiment1")) {
            if (args.length >= 6)
            {
                new SingleAlgorithmExperimenter().runExperiment(Integer.parseInt(args[1]), Long.parseLong(args[2]), args[3], args[4], Arrays.copyOfRange(args, 5, args.length));

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
            
            ReportManager reportManager = new ReportManager();
            reportManager.addReporter(new LogReportCreator());
            reportManager.addReporter(new StatisticalReportCreator());
            
            reportManager.executeAllReporters();
            
            return;
        }
        
        if (args[0].equals("-t")) {
        	new RMTest().testCustomExampleSetOnInput();
        	return;
        }
        
        if (args[0].equals("-repo")) {
        	new ExperimentLogImporter("output/experiment-logs", "").processLogs();
        	return;
        }
        
        if (args[0].equals("-cp")) {
            new ClasspathTest().main(args);
            return;
        }

        usage();
    }

    private void usage() {
        _logger.info("Usage:");
        _logger.info("------");
        _logger.info("java -jar seage.launcher.jar {params}\n");
        _logger.info("params:");
        _logger.info("\t-list");
        _logger.info("\t-test [problem-id [algorithm-id]]");
        _logger.info("\t-config path-to-config");
        _logger.info("\t-experiment [-a]? num-runs timeoutS problem-id [algorithm-id [algorithm-id]*] ");
        _logger.info("\t-experiment1 num-runs timeoutS problem-id algorithm-id instance-id [instance-id]*");
        _logger.info("\t-agents path-to-agent-config-xml");
        _logger.info("\t-report");
    }

    private void list() throws Exception {
        _logger.info("List of implemented problems and algorithms:");
        _logger.info("--------------------------------------------");

        DataNode problems = new DataNode("Problems");
        Map<String, IProblemProvider> providers = ProblemProvider.getProblemProviders();

        for (String problemId : providers.keySet()) {
            try {
                IProblemProvider pp = providers.get(problemId);
                DataNode pi = pp.getProblemInfo();
                problems.putDataNode(pi);

                String name = pi.getValueStr("name");
                _logger.info(name);

                _logger.info("\talgorithms:");
                for (DataNode alg : pi.getDataNode("Algorithms").getDataNodes()) {
                    _logger.info("\t\t" + alg.getValueStr("id")/*
                             * +" ("+alg.getValueStr("id")+")"
                             */);

                    //_logger.info("\t\t\tparameters:");
                    for (DataNode param : alg.getDataNodes("Parameter")) {
                        _logger.info("\t\t\t"
                                + param.getValueStr("name") + "  ("
                                + param.getValueStr("min") + ", "
                                + param.getValueStr("max") + ", "
                                + param.getValueStr("init") + ")");
                    }
                }
                _logger.info("\tinstances:");
                for (DataNode inst : pi.getDataNode("Instances").getDataNodes()) {
                    _logger.info("\t\t" + inst.getValueStr("type") + "=" + inst.getValueStr("path")/*
                             * +" ("+alg.getValueStr("id")+")"
                             */);
                }

                _logger.info("");
            } catch (Exception ex) {
                _logger.log(Level.SEVERE, problemId + ": " + ex.getMessage(), ex);
            }
            //XmlHelper.writeXml(problems, "problems.xml");
        }
    }

    private void agents(String config) {
        //Platform.main(new String[]{"-name", "SEAGE", "-topics", "-gui", "-p", "config=" + config, "ProblemAgent:org.seage.ael.agent.ProblemAgent"});
    }
}
