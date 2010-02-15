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

    protected boolean[] _litValues;
    protected Random _rnd;

    //OK
    public SatSolution() {
        _rnd = new Random();
    }


    //OK
    public void initLiterals(int size){
        for(int i = 0; i < size; i++){
            _litValues[i] = true;
        }
    }

    public int getLiteralCount(){
        return _litValues.length;
    }

    //OK
    public boolean[] getLiteralValues() {
        return _litValues;
    }

    //OK
    public void setLiteralValues(boolean[] litValues) {
        _litValues = litValues;
    }

    //OK
    public Literal[] copyLiterals() {
        Literal[] newLiterals = new Literal[_literals.length];
        for(int i = 0; i < _literals.length; i++){
            newLiterals[i] = new Literal(_literals[i].getValue());
        }
        return newLiterals;
    }

    //OK
    public boolean  getLiteralValue(int index) {
        return _litValues[index];
    }

    //OK
    public void setLiteral(int literal) {
        _literals[Math.abs(literal) - 1].setValue(literal);
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