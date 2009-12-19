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
package org.seage.problem.tsp.hillclimber;

import java.util.Random;
import org.seage.metaheuristic.hillclimber.IMove;
import org.seage.metaheuristic.hillclimber.IMoveManager;
import org.seage.metaheuristic.hillclimber.Solution;

/**
 *
 * @author Richard Malek
 */
public class TspMoveManager implements IMoveManager {

    public IMove[] getAllMoves(Solution solution) {
        TspSolution sol = (TspSolution) solution;
        TspMove[] moves = new TspMove[sol._tour.length];

        Random rnd = new Random();

        for (int i = 0; i < sol._tour.length; i++) {
            moves[i] = new TspMove(rnd.nextInt(sol._tour.length), rnd.nextInt(sol._tour.length));
        }

        return (IMove[]) moves;
    }
}
