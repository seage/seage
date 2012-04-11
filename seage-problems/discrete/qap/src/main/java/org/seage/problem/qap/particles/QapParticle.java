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
package org.seage.problem.qap.particles;

import org.seage.metaheuristic.particles.Particle;

/**
 *
 * @author Karel Durkota
 */
public abstract class QapParticle extends Particle
{
    public QapParticle(int dimension)
    {
        super( dimension );
    }

    public Integer[] getAssign()
    {
        Integer[] assign = new Integer[getCoords().length];

        for(int i = 0; i < assign.length; i++) assign[i] = i;

        java.util.Arrays.sort(assign, new CoordComparator(getCoords()));

        return assign;
    }

}
