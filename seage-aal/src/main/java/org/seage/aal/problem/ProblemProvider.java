/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */
package org.seage.aal.problem;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.classutil.ClassInfo;
import org.seage.classutil.ClassUtil;
import org.seage.data.DataNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of IProblemProvider interface
 *
 * @author Richard Malek
 */
public abstract class ProblemProvider<P extends Phenotype<?>> implements IProblemProvider<P> {
  public static Class<?>[] providerClasses = {};
  private static Logger logger = LoggerFactory.getLogger(ProblemProvider.class.getName());
  private static HashMap<String, IProblemProvider<Phenotype<?>>> providers;
  private ProblemInfo problemInfo;
  private HashMap<String, IAlgorithmFactory<P, ?>> algFactories;

  @Override
  public ProblemInfo getProblemInfo() throws Exception {
    if (problemInfo != null)
      return problemInfo;

    problemInfo = new ProblemInfo("ProblemInfo");

    Class<?> problemClass = this.getClass();
    Annotation an = null;

    an = problemClass.getAnnotation(Annotations.ProblemId.class);
    if (an == null)
      throw new Exception("Unable to get annotation ProblemId");
    String problemId = ((Annotations.ProblemId) an).value();

    an = problemClass.getAnnotation(Annotations.ProblemName.class);
    if (an == null)
      throw new Exception("Unable to get annotation ProblemName");
    String problemName = ((Annotations.ProblemName) an).value();

    problemInfo.putValue("id", problemId);
    problemInfo.putValue("name", problemName);
    problemInfo.putValue("class", getClass().getCanonicalName());

    // Instances
    DataNode instances = new DataNode("Instances");
    for (String in : ClassUtil.searchForInstances("instances", this.getClass().getPackage().getName())) {
      DataNode instance = new DataNode("Instance");
      instance.putValue("type", ProblemInstanceOrigin.RESOURCE);
      instance.putValue("path", in);
      String instanceFileName = in.substring(in.lastIndexOf('/') + 1);
      String instanceID = in.substring(in.lastIndexOf('/') + 1);
      if (instanceID.contains("."))
        instanceID = instanceID.substring(0, instanceID.lastIndexOf('.'));
      instance.putValue("id", instanceID);
      instance.putValue("name", instanceFileName);
      instances.putDataNode(instance);
    }
    problemInfo.putDataNode(instances);

    // Algorithms
    DataNode algorithms = new DataNode("Algorithms");
    algFactories = new HashMap<>();

    for (ClassInfo ci : ClassUtil.searchForClasses(IAlgorithmFactory.class, this.getClass().getPackage().getName())) {
      try {
        Class<?> algFactoryClass = Class.forName(ci.getClassName());
        Annotation an2 = null;

        // Algorithm adapters
        DataNode algorithm = new DataNode("Algorithm");

        an2 = algFactoryClass.getAnnotation(Annotations.AlgorithmId.class);
        if (an2 == null)
          throw new Exception(String.format("Unable to get annotation AlgorithmId: %s", algFactoryClass));
        String algId = ((Annotations.AlgorithmId) an2).value();

        an2 = algFactoryClass.getAnnotation(Annotations.AlgorithmName.class);
        if (an2 == null)
          throw new Exception("Unable to get annotation AlgorithmName");
        String algName = ((Annotations.AlgorithmName) an2).value();

        an2 = algFactoryClass.getAnnotation(Annotations.Broken.class);
        if (an2 != null) {
          logger.warn("!!! Algorithm '{}' is marked Broken", algName);
          continue;
        }

        algorithm.putValue("id", algId);
        algorithm.putValue("name", algName);
        algorithm.putValue("factoryClass", ci.getClassName());

        IAlgorithmFactory<P, ?> factory = (IAlgorithmFactory<P, ?>)algFactoryClass.getConstructor().newInstance();
        algFactories.put(algId, factory);

        // Algorithm parameters

        Class<?> algAdapterClass = factory.getAlgorithmClass();
        an2 = algAdapterClass.getAnnotation(Annotations.AlgorithmParameters.class);
        if (an2 == null)
          throw new Exception("Unable to get annotation AlgorithmParameters");
        Annotations.Parameter[] params = ((Annotations.AlgorithmParameters) an2).value();

        for (Annotations.Parameter p : params) {
          DataNode parameter = new DataNode("Parameter");
          parameter.putValue("name", p.name());
          parameter.putValue("min", p.min());
          parameter.putValue("max", p.max());
          parameter.putValue("init", p.init());
          algorithm.putDataNode(parameter);
        }
        // ---
        algorithms.putDataNode(algorithm);
      } catch (Exception ex) {
        logger.error(String.format("Getting information on %s failed", ci.getClassName()), ex);
      }
    }
    problemInfo.putDataNode(algorithms);

    return problemInfo;
  }

  @Override
  public IAlgorithmFactory<P, ?> getAlgorithmFactory(String algId) throws Exception {
    if (algFactories == null)
      throw new Exception("ProblemProvider not initialized, call getProblemInfo() first");
    if (!algFactories.containsKey(algId))
      throw new Exception("Unknown algorithm id: " + algId);
    return algFactories.get(algId);
  }

  @Override
  public HashMap<String, IAlgorithmFactory<P, ?>> getAlgorithmFactories() {
    return algFactories;
  }

  ///////////////////////////////////////////////////////////////////////////
  public static synchronized Map<String, IProblemProvider<Phenotype<?>>> getProblemProviders() throws Exception {
    HashMap<String, IProblemProvider<Phenotype<?>>> result = new HashMap<String, IProblemProvider<Phenotype<?>>>();

    for (Class<?> c : providerClasses) {
      result.put(c.getAnnotation(Annotations.ProblemId.class).value(),
          (IProblemProvider<Phenotype<?>>) c.getConstructor().newInstance());
    }

    return result;
  }

  public static synchronized Map<String, IProblemProvider<Phenotype<?>>> getProblemProviders0() throws Exception {
    if (providers != null)
      return providers;

    providers = new HashMap<>();
    logger.info("Searching for Providers");
    for (ClassInfo ci : ClassUtil.searchForClasses(IProblemProvider.class, "org.seage.problem")) {
      try {
        logger.info(ci.getClassName());
        IProblemProvider<Phenotype<?>> pp = 
          (IProblemProvider<Phenotype<?>>) Class.forName(ci.getClassName()).getConstructor().newInstance();

        providers.put(pp.getProblemInfo().getValueStr("id"), pp);
      } catch (Exception ex) {
        logger.error(ci.getClassName(), ex);
      }
    }
    logger.info("Providers count: {}", providers.keySet().size());
    return providers;
  }

}
