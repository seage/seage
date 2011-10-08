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

import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Karel Durkota
 */
public class QapObjectiveFunction implements IObjectiveFunction
{
    private QapSolution _currrentQapSolution;
    
    public void setObjectiveValue(Solution solution)
    {
        _currrentQapSolution = (QapSolution)solution;

        double price = 0.0;
        int assignLength = _currrentQapSolution.getAssign().length;

        for (int i = 1; i < assignLength; i++){
                price += _currrentQapSolution.getFacilityLocation()[i][_currrentQapSolution.getAssign()[i]];
        }
        solution.setObjectiveValue( price );
    }
}
