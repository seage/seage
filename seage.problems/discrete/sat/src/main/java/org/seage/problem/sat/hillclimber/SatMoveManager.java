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
package org.seage.problem.sat.hillclimber;

import java.util.Random;
import org.seage.metaheuristic.hillclimber.IMove;
import org.seage.metaheuristic.hillclimber.IMoveManager;
import org.seage.metaheuristic.hillclimber.Solution;
import org.seage.problem.sat.Literal;

/**
 *
 * @author Martin Zaloga
 */
public class SatMoveManager implements IMoveManager {

    Random _rnd = new Random();
    SatMove[] _moves;

    /**
     * Function which generates the next steps algorithm
     * @param solution - Current solutions for which are generated further steps
     * @return - The next steps algorithm
     */
    public IMove[] getAllMoves(Solution solution) {
        SatSolution sol = (SatSolution) solution;
        SatMove[] moves = new SatMove[sol.getLiteralCount()];

        /*random selection of sequence the steps*/
        for (int i = 0; i < sol.getLiteralCount(); i++) {
            moves[i] = new SatMove(_rnd.nextInt(sol.getLiteralCount()));
        }
        _moves = moves;
        return (IMove[]) moves;
    }


    public void printMoves(){
        for(int i = 0; i < _moves.length; i++){
            System.out.println("move["+i+"] "+_moves[i].getLiteral().toString());
        }
    }
}
