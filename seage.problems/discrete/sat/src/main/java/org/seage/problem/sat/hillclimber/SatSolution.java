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
import org.seage.metaheuristic.hillclimber.Solution;
import org.seage.problem.sat.Literal;

/**
 *
 * @author Martin Zaloga
 */
public class SatSolution extends Solution {

    protected Literal[] _literals;
    protected Random _rnd;
    private int numLiterals;

    //OK
    public SatSolution() {
        _rnd = new Random();
    }

    public int numLiterals(){
        return numLiterals;
    }

    //OK
    public void initLiterals(int size){
        numLiterals = size;
        for(int i = 0; i < size; i++){
            _literals[i] = new Literal(i+1);
        }
    }

    //OK
    public Literal[] getLiterals() {
        return _literals;
    }

    //OK
    public void setLiterals(Literal[] literals) {
        _literals = literals;
        numLiterals = literals.length;
    }

    //OK
    public Literal[] copyLiterals() {
        Literal[] newLiterals = new Literal[_literals.length];
        for(int i = 0; i < _literals.length; i++){
            newLiterals[i] = new Literal(_literals[i].getLiteral());
        }
        return newLiterals;
    }

    //OK
    public Literal getLiteral(int index) {
        return _literals[index];
    }

    //OK
    public void setLiteral(int literal) {
        _literals[Math.abs(literal) - 1].setLiteral(literal);
    }

    //OK
    public void negLiteral(int literal) {
        _literals[Math.abs(literal) - 1].neg();
    }

    //OK
    public void printLiterals() {
        for(int i = 0; i < _literals.length; i++){
            System.out.println("literal["+i+"] = "+_literals[i]);
        }
    }
}