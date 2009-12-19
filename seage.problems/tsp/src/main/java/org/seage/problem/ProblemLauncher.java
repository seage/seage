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
package org.seage.problem;

import java.io.File;
import java.io.InputStream;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;
import org.seage.problem.tsp.TspProblemSolver;

/**
 *
 * @author Richard Malek
 */
public class ProblemLauncher
{
    public static void main(String[] args)
    {
        try
        {
            new ProblemLauncher().run(args);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void run(String[] args) throws Exception
    {
        InputStream schema = ProblemLauncher.class.getResourceAsStream("config.xsd");
        DataNode config = XmlHelper.readXml(new File(args[0]), schema);

        IProblemSolver problem = createProblemProvider(config);

        problem.runAlgorithm();
        problem.reportBest();
        problem.visualize();
    }

    private IProblemSolver createProblemProvider(DataNode problem) throws Exception
    {
        String problemName = problem.getDataNode("problem").getValueStr("problemName");

        if(problemName.toLowerCase().equals("tsp"))
            return new TspProblemSolver(problem);

        throw new Exception("Unknown problem to solve: " + problemName);
    }

}
