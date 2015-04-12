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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.experimenter.reporting.h2.ExperimentDataH2Importer;
import org.seage.experimenter.reporting.rapidminer.ExperimentDataRapidMinerImporter;
import org.seage.experimenter.singlealgorithm.evolution.SingleAlgorithmEvolutionExperimenter;
import org.seage.experimenter.singlealgorithm.feedback.SingleAlgorithmFeedbackExperimenter;
import org.seage.experimenter.singlealgorithm.random.SingleAlgorithmRandomExperimenter;
import org.seage.logging.LogHelper;

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
    	LogHelper.loadConfig("log.properties");
        _logger.info("SEAGE running ...");
        
    	try {            
            new Launcher().run(args);           
        } catch (Exception ex) {
            _logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    	
    	_logger.info("SEAGE finished ...");
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

        if (args[0].equals("-report0")) {
        	new ExperimentDataRapidMinerImporter("output/experiment-logs", "").processLogs();
        	return;
        }
        
        if (args[0].equals("-report")) 
        {
        	boolean clean = false;        	
        	if(args.length == 2 && args[1].equals("clean"))
        		clean = true;
        	
			new ExperimentDataH2Importer("output/experiment-logs", "database/seage", clean).processLogs();
        	
        	return;
        }

        if (args[0].equals("-experiment-single-random")) {             
        	if (args.length == 1+5)
            {
        		int numConfigs = Integer.parseInt(args[1]);
        		int timeoutS = Integer.parseInt(args[2]);
        		
                new SingleAlgorithmRandomExperimenter(numConfigs, timeoutS).runExperiment(args[3], args[4].split(","), args[5].split(","));

            } else {
                usage();
            }
            
            return;
        }
        
        if (args[0].equals("-experiment-single-feedback")) {             
        	if (args.length == 1+5)
            {
        		int numConfigs = Integer.parseInt(args[1]);
        		int timeoutS = Integer.parseInt(args[2]);
        		
                new SingleAlgorithmFeedbackExperimenter(numConfigs, timeoutS).runExperiment(args[3], args[4].split(","), args[5].split(","));

            } else {
                usage();
            }
            
            return;
        }
        // -experiment-single-evolution numSubjects numIterations algorithmTimeoutS problemID *|instanceID[,instanceID,...] *|algorithmID[,algorithmID,...]
        if (args[0].equals("-experiment-single-evolution")) {
        	if (args.length == 1+6)
            {
        		int numSubjects = Integer.parseInt(args[1]);
        		int numIterations = Integer.parseInt(args[2]);
        		int algorithmTimeoutS = Integer.parseInt(args[3]);
        		
                new SingleAlgorithmEvolutionExperimenter(numSubjects, numIterations, algorithmTimeoutS).runExperiment( args[4], args[5].split(","), args[6].split(","));

            /*} else if (args.length == 4) {
                new SingleAlgorithmEvolutionExperimenter().runExperiment(Integer.parseInt(args[1]), Long.parseLong(args[2]), args[3] );*/
            } else {
                usage();
            }
            
            return;
        }
                      
        
        
//        if (args[0].equals("-test")) {
//            if (args.length == 1) {
//                new ProblemProviderTester().test();
//            } else if (args.length == 2) {
//                new ProblemProviderTester().test(args[1]);
//            } else if (args.length == 3) {
//                new ProblemProviderTester().test(args[1], args[2]);
//            } else {
//                usage();
//            }
//            return;
//        }
//
//        if (args[0].equals("-config")) {
//            if (args.length == 2) {
//                new ProblemProviderTester().runFromConfigFile(args[1]);
//            } else {
//                usage();
//            }
//            return;
//        }
        
        if (args[0].equals("-agents")) {
            agents(args[1]);
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
        //_logger.info("\t-config path-to-config");
        _logger.info("\t-experiment-single-random    numOfConfigs algorithmTimeoutS problemID *|instanceID[,instanceID,...] *|algorithmID[,algorithmID,...]");
        _logger.info("\t-experiment-single-feedback  numOfConfigs algorithmTimeoutS problemID *|instanceID[,instanceID,...] *|algorithmID[,algorithmID,...]");
        _logger.info("\t-experiment-single-evolution numSubjects numIterations algorithmTimeoutS problemID *|instanceID[,instanceID,...] *|algorithmID[,algorithmID,...]");
        //_logger.info("\t-agents path-to-agent-config-xml");
        _logger.info("\t-report [clean]");
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
