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

package org.seage.problem.tsp.sannealing;

import org.seage.problem.tsp.City;

/**
 *
 * @author Jan Zmatlik
 */
public class TspSortedSolution extends TspSolution{
    
    public TspSortedSolution(City[] cities)
    {
        super( cities );
        initSortedTour();
    }

    /**
     * Sort and fill the tour
     */
    private void initSortedTour()
    {
        for(int i = 0; i < _tour.length; i++) _tour[i] = i;
    }

}
