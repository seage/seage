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

import org.seage.problem.qap.AssignmentProvider;

/**
 *
 * @author Karel Durkota
 */
public class QapRandomSolution extends QapSolution {

    public QapRandomSolution(Double[][] facilityLocation)
    {
        super( facilityLocation );
        _assign = AssignmentProvider.createRandomAssignment( facilityLocation );
    }

}
