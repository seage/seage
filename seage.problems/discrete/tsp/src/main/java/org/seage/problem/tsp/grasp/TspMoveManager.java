/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Martin Zaloga
 *     - Initial implementation
 */
package org.seage.problem.tsp.grasp;

import java.util.Random;
import org.seage.metaheuristic.grasp.IMove;
import org.seage.metaheuristic.grasp.IMoveManager;
import org.seage.metaheuristic.grasp.Solution;

/**
 *
 * @author Martin Zaloga
 */
public class TspMoveManager implements IMoveManager {

    /**
     * Function which generates the next steps algorithm
     * @param solution - Current solutions for which are generated further steps
     * @return - The next steps algorithm
     */
    public IMove[] getAllMoves(Solution solution) {
        TspSolution sol = (TspSolution) solution;
        TspMove[] moves = new TspMove[sol.getTour().length];
        Random rnd = new Random();

        /*random selection of sequence the steps*/
        for (int i = 0; i < sol.getTour().length; i++) {
            moves[i] = new TspMove(rnd.nextInt(sol.getTour().length), rnd.nextInt(sol.getTour().length));
        }
        
        return (IMove[]) moves;
    }
}
