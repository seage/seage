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
import java.io.InputStream;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;

/**
 *
 * @author Richard Malek
 */
public abstract class ProblemSolver
{
    protected IAlgorithmAdapter _algorithm;
    protected DataNode _problemParams;
    protected DataNode _algorithmParams;


    protected abstract void initProblem(DataNode config) throws Exception;
    protected abstract IAlgorithmFactory createAlgorithmFactory(String algName) throws Exception;
    protected abstract void visualize() throws Exception;

    public ProblemSolver(String[] args) throws Exception
    {
        InputStream schema = ProblemSolver.class.getResourceAsStream("config.xsd");
        DataNode config = XmlHelper.readXml(new File(args[0]), schema);
        
        _problemParams = config.getDataNode("problem");
        _algorithmParams = config.getDataNodeById(config.getDataNode("problem").getValueStr("runAlgorithmId")).getDataNodes().get(0);
        initProblem(_problemParams);
        _algorithm = createAlgorithmFactory(_algorithmParams.getName()).createAlgorithm(_algorithmParams);
    }

    public void run() throws Exception
    { 
        _algorithm.startSearching(_algorithmParams);
        reportBest();
        visualize();
    }

    protected void reportBest() throws Exception
    {
        System.out.println("Best: "+_algorithm.getReport().getDataNode("statistics").getValueStr("bestObjVal"));
    }
}
