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

import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TourProvider;

/**
 *
 * @author Jan Zmatlik
 */
public class TspGreedyParticle extends TspParticle {

    public TspGreedyParticle(City[] cities, int dimension) throws Exception
    {
        super( cities , dimension  );
        _tour = TourProvider.createGreedyTour( cities );
    }

}
