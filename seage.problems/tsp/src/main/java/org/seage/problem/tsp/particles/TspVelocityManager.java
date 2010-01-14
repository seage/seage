/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.problem.tsp.particles;

import java.util.Random;
import org.seage.metaheuristic.particles.IVelocityManager;
import org.seage.metaheuristic.particles.Solution;

/**
 *
 * @author Jan Zmatlik
 */
public class TspVelocityManager implements IVelocityManager
{

    public Solution getModifiedSolution(Solution solution)
    {
        return null;
//        TspSolution tspSolution = ((TspSolution)solution).clone();
//
//        Random rnd = new Random();
//        int tspSolutionLength = tspSolution.getTour().length;
//        int a = rnd.nextInt( tspSolutionLength );
//        int b = rnd.nextInt( tspSolutionLength );
//
//        // Swap values if indices are different
//        if(a != b)
//        {
//            tspSolution.getTour()[a] = tspSolution.getTour()[a] + tspSolution.getTour()[b];
//            tspSolution.getTour()[b] = tspSolution.getTour()[a] - tspSolution.getTour()[b];
//            tspSolution.getTour()[a] = tspSolution.getTour()[a] - tspSolution.getTour()[b];
//        }
//
//        return (Solution)tspSolution;
    }

}
