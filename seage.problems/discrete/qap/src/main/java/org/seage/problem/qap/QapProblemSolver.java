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
package org.seage.problem.qap;

import org.seage.aal.IProblemProvider;
import org.seage.aal.ProblemSolver;

/**
 *
 * @author Karel Durkota
 */
public class QapProblemSolver extends ProblemSolver
{
    public static void main(String[] args)
    {
        try
        {
            if(args.length == 0)
                throw new Exception("Usage: java -jar seage.problem.jar {config-xml-path}");
            new QapProblemSolver(args);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public QapProblemSolver(String[] args) throws Exception
    {
        super(args);       
    }

    @Override
    protected IProblemProvider getProblemProvider()
    {
        return new QapProblemProvider();
    }
     
}
