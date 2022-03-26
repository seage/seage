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

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.classutil.ClassInfo;
import org.seage.classutil.ClassUtil;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of IProblemProvider interface.
 *
 * @author Richard Malek
 */
public abstract class ProblemProvider<P extends Phenotype<?>> implements IProblemProvider<P> {
  private static Class<?>[] providerClasses = {};
  private static Map<String, IProblemProvider<Phenotype<?>>> providers = null;
  private static Logger logger = LoggerFactory.getLogger(ProblemProvider.class.getName());
  private ProblemInfo problemInfo;
  private HashMap<String, IAlgorithmFactory<P, ?>> algFactories;

  public static void registerProblemProviders(Class<?>[] providerClasses) {
    ProblemProvider.providerClasses = providerClasses;
  }

  @Override
  public ProblemInfo getProblemInfo() throws Exception {
    if (problemInfo != null) {
      return problemInfo;
    }

    problemInfo = new ProblemInfo("ProblemInfo");

    Class<?> problemClass = this.getClass();
    Annotation an = null;

    an = problemClass.getAnnotation(Annotations.ProblemId.class);
    if (an == null) {
      throw new NullPointerException("Unable to get annotation ProblemId");
    }
    String problemId = ((Annotations.ProblemId) an).value();

    an = problemClass.getAnnotation(Annotations.ProblemName.class);
    if (an == null) {
      throw new NullPointerException("Unable to get annotation ProblemName");
    }
    String problemName = ((Annotations.ProblemName) an).value();

    problemInfo.putValue("id", problemId);
    problemInfo.putValue("name", problemName);
    problemInfo.putValue("class", getClass().getCanonicalName());

    String problemPackageName = this.getClass().getPackage().getName();
    // Instances metadata
    DataNode metadata = null;
    Path metadataPath = Paths.get("/",
        problemPackageName.replace('.', '/'),
        problemId.toLowerCase() + ".metadata.xml");
    try (InputStream stream = this.getClass().getResourceAsStream(metadataPath.toString())) {
      metadata = XmlHelper.readXml(stream).getDataNode("Instances");
    } catch (Exception ex) {
      logger.warn("No metadata for problem {}", problemId);
    }
    // Instances
    DataNode instances = new DataNode("Instances");
    for (String in : ClassUtil.searchForInstances("instances", problemPackageName)) {
      DataNode instance = new DataNode("Instance");
      instance.putValue("type", ProblemInstanceOrigin.RESOURCE);
      instance.putValue("path", in);
      String instanceFileName = in.substring(in.lastIndexOf('/') + 1);
      String instanceID = in.substring(in.lastIndexOf('/') + 1);
      if (instanceID.contains(".")) {
        instanceID = instanceID.substring(0, instanceID.lastIndexOf('.'));
      }
      instance.putValue("id", instanceID);
      instance.putValue("name", instanceFileName);
      // metadata ?
      if (metadata != null) {
        DataNode dn = metadata.getDataNodeById(instanceID);
        if(dn == null) {
          logger.warn("No metadata for instance '{}'", instanceID);
        } else {
          instance.putValue("size",dn.getValue("size"));
          instance.putValue("random",dn.getValue("random"));
          instance.putValue("greedy",dn.getValue("greedy"));
          instance.putValue("optimum",dn.getValue("optimum"));
        }
      }

      instances.putDataNode(instance);
    }
    problemInfo.putDataNode(instances);

    // Algorithms
    DataNode algorithms = new DataNode("Algorithms");
    algFactories = new HashMap<>();

    for (ClassInfo ci : ClassUtil.searchForClasses(IAlgorithmFactory.class, problemPackageName)) {
      try {
        Class<?> algFactoryClass = Class.forName(ci.getClassName());

        if(algFactoryClass.getAnnotation(Annotations.Broken.class) != null)
          continue;

        Annotation an2 = null;
        an2 = algFactoryClass.getAnnotation(Annotations.AlgorithmName.class);
        if (an2 == null) {
          throw new NullPointerException("Unable to get annotation AlgorithmName");
        }
        String algName = ((Annotations.AlgorithmName) an2).value();

        an2 = algFactoryClass.getAnnotation(Annotations.Broken.class);
        if (an2 != null) {
          logger.debug("!!! Algorithm '{}' is marked Broken", algName);
          continue;
        }

        an2 = algFactoryClass.getAnnotation(Annotations.AlgorithmId.class);        
        if (an2 == null) {
          throw new NullPointerException(
              String.format("Unable to get annotation AlgorithmId: %s", algFactoryClass));
        }      
        String algId = ((Annotations.AlgorithmId) an2).value();  

        // Algorithm adapters
        DataNode algorithm = new DataNode("Algorithm");        
        algorithm.putValue("id", algId);
        algorithm.putValue("name", algName);
        algorithm.putValue("factoryClass", ci.getClassName());

        IAlgorithmFactory<P, ?> factory = 
            (IAlgorithmFactory<P, ?>)algFactoryClass.getConstructor().newInstance();
        algFactories.put(algId, factory);

        // Algorithm parameters

        Class<?> algAdapterClass = factory.getAlgorithmClass();
        an2 = algAdapterClass.getAnnotation(Annotations.AlgorithmParameters.class);
        if (an2 == null) {
          throw new NullPointerException("Unable to get annotation AlgorithmParameters");
        }
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
    if (algFactories == null) {
      throw new NullPointerException(
          "ProblemProvider not initialized, call getProblemInfo() first");
    }
    if (!algFactories.containsKey(algId)) {
      throw new NullPointerException("Unknown algorithm id: " + algId);
    }
    return algFactories.get(algId);
  }

  @Override
  public HashMap<String, IAlgorithmFactory<P, ?>> getAlgorithmFactories() {
    return algFactories;
  }

  /**
   * Lists all registered problem providers.
   * @return Map of problem ids and problem providers.
   * @throws Exception .
   */
  @SuppressWarnings("unchecked")
  public static synchronized Map<String, IProblemProvider<Phenotype<?>>> 
      getProblemProviders() throws Exception {
    
    if (providers != null) {
      return providers;
    }
    
    providers = new HashMap<>();

    for (Class<?> c : providerClasses) {
      IProblemProvider<Phenotype<?>> pp = 
          (IProblemProvider<Phenotype<?>>) c.getConstructor().newInstance();
      pp.getProblemInfo();
      providers.put(c.getAnnotation(Annotations.ProblemId.class).value(), pp);       
    }

    return providers;
  }
}
