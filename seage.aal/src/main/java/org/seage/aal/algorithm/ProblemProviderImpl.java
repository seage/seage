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
package org.seage.aal.algorithm;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import org.seage.aal.Annotations;
import org.seage.aal.data.ProblemInfo;
import org.seage.classutil.ClassUtil;
import org.seage.classutil.ClassInfo;
import org.seage.data.DataNode;


/**
 * Implementation of IProblemProvider interface
 *
 * @author Richard Malek
 */
public abstract class ProblemProviderImpl implements IProblemProvider, Serializable
{
    private static HashMap<String, IProblemProvider> _providers;
    private ProblemInfo _problemInfo;
    private HashMap<String, IAlgorithmFactory> _algFactories;

    @Override
    public ProblemInfo getProblemInfo() throws Exception
    {
        if(_problemInfo != null)
            return _problemInfo;

        _problemInfo = new ProblemInfo("ProblemInfo");
        
        Class problemClass = this.getClass();
        Annotation an = null;

        an = problemClass.getAnnotation(Annotations.ProblemId.class);
        if(an == null) throw new Exception("Unable to get annotation ProblemId");
        String problemId = ((Annotations.ProblemId)an).value();

        an = problemClass.getAnnotation(Annotations.ProblemName.class);
        if(an == null) throw new Exception("Unable to get annotation ProblemName");
        String problemName = ((Annotations.ProblemName)an).value();

        _problemInfo.putValue("id", problemId);
        _problemInfo.putValue("name", problemName);
        _problemInfo.putValue("class", getClass().getCanonicalName());

        // Instances
        DataNode instances = new DataNode("Instances");
        for(String in : ClassUtil.searchForInstancesInJar("instances", this.getClass()))
        {
            DataNode instance = new DataNode("Instance");
            instance.putValue("type", "resource");
            instance.putValue("path", in);
            instance.putValue("name", in.substring(in.lastIndexOf('/')+1));
            instances.putDataNode(instance);
        }
        _problemInfo.putDataNode(instances);

        // Algorithms
        DataNode algorithms = new DataNode("Algorithms");
        _algFactories = new HashMap<String, IAlgorithmFactory>();

        for(ClassInfo ci : ClassUtil.searchForClassesInJar(IAlgorithmFactory.class, this.getClass()))
        {
            try
            {
                Class algFactoryClass = Class.forName(ci.getClassName());
                Annotation an2 = null;

                // Algorithm adapters
                DataNode algorithm = new DataNode("Algorithm");

                an2 = algFactoryClass.getAnnotation(Annotations.AlgorithmId.class);
                if(an2 == null) throw new Exception("Unable to get annotation AlgorithmId");
                String algId = ((Annotations.AlgorithmId)an2).value();

                an2 = algFactoryClass.getAnnotation(Annotations.AlgorithmName.class);
                if(an2 == null) throw new Exception("Unable to get annotation AlgorithmName");
                String algName = ((Annotations.AlgorithmName)an2).value();

                algorithm.putValue("id", algId);
                algorithm.putValue("name", algName);
                algorithm.putValue("factoryClass", ci.getClassName());

                IAlgorithmFactory factory = (IAlgorithmFactory)algFactoryClass.newInstance();
                //factory.setProblemProvider((IProblemProvider)ObjectCloner.deepCopy(this));
                _algFactories.put(algId, factory);                

                // Algorithm parameters                

                Class algAdapterClass = ((IAlgorithmFactory)algFactoryClass.newInstance()).getAlgorithmClass();
                an2 = algAdapterClass.getAnnotation(Annotations.AlgorithmParameters.class);
                if(an2 == null) throw new Exception("Unable to get annotation AlgorithmParameters");
                Annotations.Parameter[] params = ((Annotations.AlgorithmParameters)an2).value();

                for(Annotations.Parameter p : params)
                {
                    DataNode parameter = new DataNode("Parameter");
                    parameter.putValue("name", p.name());
                    parameter.putValue("min", p.min());
                    parameter.putValue("max", p.max());
                    parameter.putValue("init", p.init());
                    algorithm.putDataNode(parameter);
                }
                // ---
                algorithms.putDataNode(algorithm);
            }
            catch(Exception ex)
            {
                //System.err.println(ci.getClassName()+": "+ex.getMessage());
                ex.printStackTrace();
            }
        }
        _problemInfo.putDataNode(algorithms);

        return _problemInfo;
    }

    public IAlgorithmFactory getAlgorithmFactory(String algId) throws Exception
    {
        if(!_algFactories.containsKey(algId))
            throw new Exception("Unknown algorithm id: " + algId);
        return _algFactories.get(algId);
    }



    ///////////////////////////////////////////////////////////////////////////

    public static Map<String, IProblemProvider> getProblemProviders() throws Exception
    {
        if(_providers != null)
            return _providers;
        
        _providers = new HashMap<String, IProblemProvider>();

        for(ClassInfo ci : ClassUtil.searchForClasses(IProblemProvider.class, "seage.problem"))
        {
            try
            {
                IProblemProvider pp = (IProblemProvider)Class.forName(ci.getClassName()).newInstance();

                _providers.put(pp.getProblemInfo().getValueStr("id"), pp);
            }
            catch(Exception ex)
            {
                System.err.println(ci.getClassName()+": "+ex.getMessage());
            }
        }

        return _providers;
    }

}
