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

import java.io.Serializable;
import java.util.Random;
import org.seage.metaheuristic.hillclimber.Solution;

/**
 *
 * @author Martin Zaloga
 */
public class SatSolution extends Solution implements java.lang.Cloneable, Serializable {

    protected boolean[] _litValues;
    protected Random _rnd;

    public SatSolution() {
        _rnd = new Random();
    }

    public void initLiterals(int size){
        _litValues = new boolean[size];
        for(int i = 0; i < size; i++){
            _litValues[i] = true;
        }
    }

    public int getLiteralCount(){
        return _litValues.length;
    }

    public boolean[] getLiteralValues() {
        return _litValues;
    }

    public void setLiteralValues(boolean[] litValues) {
        _litValues = litValues;
    }

    public boolean  getLiteralValue(int index) {
        return _litValues[index];
    }

    public void setLiteralValue(int index, boolean value) {
        _litValues[index] = value;
    }

    public void negLiteral(int index) {
        if(_litValues[index]){
            _litValues[index] = false;
        } else {
            _litValues[index] = true;
        }
    }

    public void printLiterals() {
        for(int i = 0; i < _litValues.length; i++){
            System.out.println("literal["+i+"] = "+_litValues[i]);
        }
    }
}