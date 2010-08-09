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
package org.seage.experimenter;

import java.io.File;
import java.io.InputStream;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IProblemProvider;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;

/**
 *
 * @author Richard Malek
 */
public class Experimenter
{
    protected IAlgorithmAdapter _algorithm;
    protected DataNode _problemParams;
    protected DataNode _algorithmParams;
    private IProblemProvider _provider;

    //protected abstract IProblemProvider getProblemProvider();

    public Experimenter(String[] args) throws Exception
    {
        InputStream schema = Experimenter.class.getResourceAsStream("config.xsd");
        if(schema == null)
            throw new Exception("No xsd schema found.");
        
        DataNode config = XmlHelper.readXml(new File(args[0]), schema);

        _provider = new org.seage.problem.tsp.TspProblemProvider();//getProblemProvider();
        
        _problemParams = config.getDataNode("problem");
        _algorithmParams = config.getDataNodeById(config.getDataNode("problem").getValueStr("runAlgorithmId")).getDataNodes().get(0);
        _provider.initProblemInstance(_problemParams);
        //_algorithm = _provider.createAlgorithmFactory(_algorithmParams).createAlgorithm(_algorithmParams);

        run();
    }

    public void run() throws Exception
    {
        _algorithm.solutionsFromPhenotype(_provider.generateInitialSolutions(_algorithmParams.getValueInt("numSolutions")));
        _algorithm.startSearching(_algorithmParams);        
        _provider.visualizeSolution(_algorithm.solutionsToPhenotype()[0]);

        reportBest();
    }

    protected void reportBest() throws Exception
    {
        System.out.println("Best: "+_algorithm.getReport().getDataNode("statistics").getValueStr("bestObjVal"));
    }

    public static void main(String[] args)
    {
        try
        {
            new Experimenter(args).run();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

