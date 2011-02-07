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
package org.seage.problem.qap.fireflies;

import org.seage.problem.qap.fireflies.*;
import org.seage.problem.qap.AssignmentProvider;

/**
 *
 * @author Karel Durkota
 */

public class QapGreedyStartSolution extends QapSolution
{
    
    public QapGreedyStartSolution(){} // Appease clone()

    public QapGreedyStartSolution(Double[][][] customers) throws Exception
    {
        super( customers );
        _assign = AssignmentProvider.createGreedyAssignment( customers );
        
    }   // end constructor

}   // end class TspGreedyStartSolution
