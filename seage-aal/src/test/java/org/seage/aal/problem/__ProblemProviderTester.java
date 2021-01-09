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
package org.seage.aal.problem;

import java.io.File;
import java.util.Map;

import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.Phenotype;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;
//import org.seage.experimenter.config.DefaultConfigurator;

/**
 *
 * @author Richard Malek
 */
@SuppressWarnings("all")
public class __ProblemProviderTester {

  private Map<String, IProblemProvider<Phenotype<?>>> _providers;

  public __ProblemProviderTester() throws Exception {
    _providers = ProblemProvider.getProblemProviders();
  }

  public void test() throws Exception {
    System.out.println("Testing algorithms:");
    System.out.println("-------------------");

    for (String problemId : _providers.keySet()) {
      testProblem(_providers.get(problemId));
    }
  }

  public void test(String problemId) {
    System.out.println("Testing algorithms:");
    System.out.println("-------------------");

    testProblem(_providers.get(problemId));
  }

  public void test(String problemID, String algorithmID) throws Exception {
    System.out.println("Testing algorithms:");
    System.out.println("-------------------");

    IProblemProvider provider = _providers.get(problemID);
    ProblemInfo pi = provider.getProblemInfo();
    String problemName = pi.getValueStr("name");
    System.out.println(problemName);

    // testProblem(provider, pi,
    // pi.getDataNode("Algorithms").getDataNodeById(algorithmID));
  }

  private void testProblem(IProblemProvider provider) {
    try {
      ProblemInfo pi = provider.getProblemInfo();
      String problemName = pi.getValueStr("name");
      System.out.println(problemName);

      for (DataNode alg : pi.getDataNode("Algorithms").getDataNodes()) {
        // testProblem(provider, pi.getInstanceInfo(instanceID), alg.getValueStr("id"));
        // System.out.println("\t"+alg.getValueStr("id")/*+"
        // ("+alg.getValueStr("id")+")"*/);
      }
    } catch (Exception ex) {
      // System.err.println(problemId+": "+ex.getLocalizedMessage());
      ex.printStackTrace();
    }
  }

  @SuppressWarnings("unused")
  private void testProblem(IProblemProvider provider, String instanceID, String algorithmID) {
    try {
      System.out.print("\t" + algorithmID);

      ProblemConfig config = null;// new DefaultConfigurator().prepareConfigs(provider.getProblemInfo(),
                                  // instanceID, algorithmID, 1)[0];

      IAlgorithmFactory factory = provider.getAlgorithmFactory(algorithmID);

      ProblemInstance instance = provider
          .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo(instanceID));
//            IAlgorithmAdapter algorithm = factory.createAlgorithm(instance);
//            @SuppressWarnings("null")
//            AlgorithmParams algNode = config.getAlgorithmParams();
//            Object[][] solutions = provider.generateInitialSolutions(instance,
//                    algNode.getDataNode("Parameters").getValueInt("numSolutions"), 1);
//            algorithm.solutionsFromPhenotype(solutions);
//            algorithm.startSearching(algNode);
//            solutions = algorithm.solutionsToPhenotype();
//            algorithm.solutionsFromPhenotype(solutions);
//            algorithm.startSearching(algNode);
//
//            System.out.printf("%" + (50 - algorithmID.length()) + "s", "OK\n");

    } catch (Exception ex) {
      System.out.printf("%" + (52 - algorithmID.length()) + "s", "FAIL\n");
      // System.out.println("\t"+"FAIL");
      ex.printStackTrace();
      // System.err.println(problemId+"/"+alg.getValueStr("id")+": "+ex.toString());
    }
  }

  public void runFromConfigFile(String configPath) throws Exception {
    ProblemConfig config = new ProblemConfig(XmlHelper.readXml(new File(configPath)));
    String problemID = config.getDataNode("Problem").getValueStr("id");
    String algorithmID = config.getDataNode("Algorithm").getValueStr("id");
    String instanceID = config.getDataNode("Problem").getDataNode("Instance").getValueStr("id");
    // provider and factory
    IProblemProvider provider = ProblemProvider.getProblemProviders().get(problemID);
    IAlgorithmFactory factory = provider.getAlgorithmFactory(algorithmID);

    // problem instance
    ProblemInstance instance = provider
        .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo(instanceID));

    // algorithm
//        IAlgorithmAdapter algorithm = factory.createAlgorithm(instance);
//
//        AlgorithmParams algNode = config.getAlgorithmParams();
//        //Object[][] solutions = (Object[][])
//        provider.generateInitialSolutions(instance, algNode.getDataNode("Parameters").getValueInt("numSolutions"), 1);
    //
    //
    // System.out.printf("%s: %4s %s\n", "Problem","", problemID);
    // System.out.printf("%s: %2s %s\n", "Algorithm","", algorithmID);
    // System.out.printf("%s: %3s %s\n", "Instance","", instance);
    // System.out.println("Running ...");
    // algorithm.solutionsFromPhenotype(solutions);
    // algorithm.startSearching(algNode);
    // solutions = algorithm.solutionsToPhenotype();
    //
    // // phenotype evaluator
    // IPhenotypeEvaluator evaluator = provider.initPhenotypeEvaluator(instance);
    // double[] result = evaluator.evaluate(solutions[0]);
    //
    // System.out.printf("%s: %5s %s\n", "Result","", result[0]);
    // //System.out.println(": " + result[0]);
    //
    // System.out.printf("%s: %3s ", "Solution","");
    // for(int i=0;i<solutions[0].length;i++)
    // System.out.print(solutions[0][i]+" ");
    // System.out.println();
    //
    // AlgorithmReport report = algorithm.getReport();
    // report.save("output/"+System.currentTimeMillis()+".xml");
  }
}
