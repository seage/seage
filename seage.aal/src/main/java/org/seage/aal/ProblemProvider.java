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
package org.seage.aal;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import org.seage.classutil.ClassFinder;
import org.seage.classutil.ClassInfo;
import org.seage.data.DataNode;

/**
 * Implementation of IProblemProvider interface
 *
 * @author Richard Malek
 */
public abstract class ProblemProvider implements IProblemProvider
{
    private HashMap<String, IAlgorithmFactory> _algFactories;

    @Override
    public Object[][] generateInitialSolutions(int numSolutions) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataNode getProblemInfo() throws Exception
    {
        DataNode result = new DataNode("ProblemInfo");
        
        Class problemClass = this.getClass();
        Annotation an = null;

        an = problemClass.getAnnotation(Annotations.ProblemId.class);
        if(an == null) throw new Exception("Unable to get annotation ProblemId");
        String problemId = ((Annotations.ProblemId)an).value();

        an = problemClass.getAnnotation(Annotations.ProblemName.class);
        if(an == null) throw new Exception("Unable to get annotation ProblemName");
        String problemName = ((Annotations.ProblemName)an).value();

        result.putValue("id", problemId);
        result.putValue("name", problemName);
        result.putValue("class", getClass().getCanonicalName());

        DataNode instances = new DataNode("Instances");
        result.putDataNode(instances);

        // Add instance info here
        //-----------------------

        // Algorithms
        DataNode algorithms = new DataNode("Algorithms");
        _algFactories = new HashMap<String, IAlgorithmFactory>();

        for(ClassInfo ci : ClassFinder.searchForClassesInJar(IAlgorithmFactory.class, this.getClass()))
        {
            DataNode algorithm = new DataNode("Algorithm");            
            
            try
            {
                Class algClass = Class.forName(ci.getClassName());

                Annotation an2 = null;

                an2 = algClass.getAnnotation(Annotations.AlgorithmId.class);
                if(an == null) throw new Exception("Unable to get annotation AlgorithmId");
                String algId = ((Annotations.AlgorithmId)an2).value();

                an2 = algClass.getAnnotation(Annotations.AlgorithmName.class);
                if(an == null) throw new Exception("Unable to get annotation AlgorithmName");
                String algName = ((Annotations.AlgorithmName)an2).value();

                algorithm.putValue("id", algId);
                algorithm.putValue("name", algName);
                algorithm.putValue("factoryClass", ci.getClassName());

                _algFactories.put(algId, (IAlgorithmFactory)algClass.newInstance());

                algorithms.putDataNode(algorithm);
            }
            catch(Exception ex)
            {
                System.err.println(ci.getClassName()+": "+ex.getMessage());
                ex.printStackTrace();
            }
        }
        result.putDataNode(algorithms);

        return result;
    }

    public IAlgorithmFactory getAlgorithmFactory(String algId) throws Exception
    {
        return _algFactories.get(algId);
    }



    ///////////////////////////////////////////////////////////////////////////

    public static Map<String, IProblemProvider> getProblemProviders() throws Exception
    {
        HashMap<String, IProblemProvider> result = new HashMap<String, IProblemProvider>();

        for(ClassInfo ci : ClassFinder.searchForClasses(IProblemProvider.class, "seage.problem"))
        {
            try
            {
                IProblemProvider pp = (IProblemProvider)Class.forName(ci.getClassName()).newInstance();

                result.put(pp.getProblemInfo().getValueStr("id"), pp);
            }
            catch(Exception ex)
            {
                System.err.println(ci.getClassName()+": "+ex.getMessage());
            }
        }

        return result;
    }

}
