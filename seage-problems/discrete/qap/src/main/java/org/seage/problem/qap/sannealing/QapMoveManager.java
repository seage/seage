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
package org.seage.problem.qap.sannealing;

import java.util.Random;
import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Karel Durkota
 */
public class QapMoveManager implements IMoveManager
{

    public Solution getModifiedSolution(Solution solution)
    {
        QapSolution qapSolution = ((QapSolution)solution).clone();

        Random rnd = new Random();
        int qapSolutionLength = qapSolution.getAssign().length;
        int a = rnd.nextInt( qapSolutionLength );
        int b = rnd.nextInt( qapSolutionLength );

        // Swap values if indices are different
        if(a != b)
        {

            qapSolution.getAssign()[a] = qapSolution.getAssign()[a] + qapSolution.getAssign()[b];
            qapSolution.getAssign()[b] = qapSolution.getAssign()[a] - qapSolution.getAssign()[b];
            qapSolution.getAssign()[a] = qapSolution.getAssign()[a] - qapSolution.getAssign()[b];
        }

        return (Solution)qapSolution;
    }

}
