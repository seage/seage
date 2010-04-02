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
package org.seage.problem.tsp.particles;

import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.seage.metaheuristic.particles.Particle;
import org.seage.problem.tsp.City;

/**
 *
 * @author Jan Zmatlik
 */
public abstract class TspParticle extends Particle
{
    public TspParticle(int dimension)
    {
        super( dimension );
    }

    public Integer[] getTour()
    {
        Integer[] tour = new Integer[getCoords().length];
        for(int i=0;i<tour.length;i++) tour[i] = i;

        java.util.Arrays.sort(tour, new CoordComparator(getCoords()));

        return tour;
    }

}
