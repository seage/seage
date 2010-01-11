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
package org.seage.problem.tsp;

import org.seage.aal.IProblemProvider;
import org.seage.aal.ProblemSolver;

/**
 *
 * @author Richard Malek
 */
public class TspProblemSolver extends ProblemSolver
{
    private TspProblemProvider _provider;

    public static void main(String[] args)
    {
        try
        {
            if(args.length == 0)
                throw new Exception("Usage: java -jar seage.problem.jar {config-xml-path}");
            new TspProblemSolver(args).run();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public TspProblemSolver(String[] args) throws Exception
    {
        super(args);       
    }

    @Override
    protected IProblemProvider getProblemProvider()
    {
         _provider = new TspProblemProvider();
        return _provider;
    }

    protected void visualize() throws Exception
    {
        Integer[] tour = (Integer[])_algorithm.solutionsToPhenotype()[0];

        String outPath = _problemParams.getDataNode("visualizer").getValueStr("outPath");
        int width = _problemParams.getDataNode("visualizer").getValueInt("width");
        int height = _problemParams.getDataNode("visualizer").getValueInt("height");

        Visualizer.instance().createGraph(_provider.getCities(), tour, outPath, width, height);
    }
    
}
