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
 *     Richard Malek
 *     - Random algorithm
 */
package org.seage.problem.tsp.sannealing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.seage.problem.tsp.City;

/**
 *
 * @author Jan Zmatlik
 */
public class TspRandomSolution extends TspSolution {

    private Random _random = new Random();

    public TspRandomSolution(City[] cities)
    {
        super( cities );
        initRandomTour();
    }

    private void initRandomTour()
    {
        List<Integer> listTour = new ArrayList();
        for (int i = 0; i < _cities.length; i++) {
            listTour.add(i);
        }

        List<Integer> randTour = new ArrayList();
        Integer pom = null;
        for (int i = 0; i < _cities.length; i++) {
            pom = listTour.get(_random.nextInt(listTour.size()));
            randTour.add(pom);
            listTour.remove(pom);
        }

        _tour = new Integer[_cities.length];
        for (int i = 0; i < _cities.length; i++) {
            _tour[i] = randTour.get(i);
        }
    }
}
