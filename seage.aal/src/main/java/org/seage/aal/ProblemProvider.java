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

    @Override
    public Object[][] generateInitialSolutions(int numSolutions) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataNode getProblemInfo() throws Exception
    {
        DataNode result = new DataNode("ProblemInfo");
        result.putDataNode(new DataNode("Instances"));
        
        DataNode alg = new DataNode("Algorithms");


        for(ClassInfo ci : ClassFinder.searchForClassesInJar(IAlgorithmFactory.class, this.getClass()))
        {
            //if(ClassFinderOld.checkForParent(c, IAlgorithmFactory.class))
            {
                try
                {
                    Class cls = Class.forName(ci.getClassName());
                    Annotation an = null;

                    an = cls.getAnnotation(Annotations.AlgorithmId.class);
                    if(an == null) throw new Exception("Unable to get annotation AlgorithmId");
                    String id = ((Annotations.AlgorithmId)an).value();

                    an = cls.getAnnotation(Annotations.AlgorithmName.class);
                    if(an == null) throw new Exception("Unable to get annotation AlgorithmName");
                    String name = ((Annotations.AlgorithmName)an).value();

                    
                    DataNode algDn = new DataNode("Algorithm");
                    //algDn.putValue("name", ci.getClassName());
                    algDn.putValue("id", id);
                    algDn.putValue("name", name);
                    alg.putDataNode(algDn);
                }
                catch(Exception ex)
                {
                    System.err.println(ci.getClassName()+": "+ex.getMessage());
                }
            }
        }
        result.putDataNode(alg);

        return result;
    }

    ///////////////////////////////////////////////////////////////////////////

    

}
