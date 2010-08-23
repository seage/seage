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

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.seage.classutil.ClassFinder;
import org.seage.classutil.ClassInfo;
import org.seage.data.DataNode;

/**
 * Implementation of IProblemProvider interface
 *
 * @author Richard Malek
 */
public class ProblemProvider implements IProblemProvider
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

        String pkg = this.getClass().getPackage().getName();
        alg.putValue("package", pkg);

        for(ClassInfo ci : ClassFinder.searchForClasses(".", "", pkg, IAlgorithmFactory.class))
        {
            DataNode algDn = new DataNode("Algorithm");
            algDn.putValue("name", ci.getClassObj().getCanonicalName());
            alg.putDataNode(algDn);
        }
        result.putDataNode(alg);

        return result;
    }

    @Override
    public IAlgorithmAdapter initAlgorithm(DataNode params)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IPhenotypeEvaluator initPhenotypeEvaluator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void initProblemInstance(DataNode params) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visualizeSolution(Object[] solution) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    ///////////////////////////////////////////////////////////////////////////

    

}
