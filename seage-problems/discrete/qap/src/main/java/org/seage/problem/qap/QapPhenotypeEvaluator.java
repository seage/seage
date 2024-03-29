/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.problem.qap;

import org.seage.aal.algorithm.IPhenotypeEvaluator;

/**
 *
 * @author Karel Durkota
 * student
 */
public class QapPhenotypeEvaluator implements IPhenotypeEvaluator<QapPhenotype>
{
    private QapProblemInstance instance;

    public QapPhenotypeEvaluator(QapProblemInstance instance)
    {
        super();
        this.instance = instance;
    }

    @Override
    public int compare(double[] o1, double[] o2)
    {
        if (o1 == null)
            return -1;
        if (o2 == null)
            return 1;

        int length = o1.length <= o2.length ? o1.length : o2.length;

        for (int i = 0; i < length; i++)
        {
            if (o1[i] < o2[i])
                return 1;
            if (o1[i] > o2[i])
                return -1;
        }
        return 0;
    }

    @Override
    public double[] evaluate(QapPhenotype phenotypeSubject) throws Exception {
        Integer[] s = phenotypeSubject.getSolution();
        Double[][][] facilityLocation = instance.getFacilityLocation();
        double price = 0;
        for (int i = 0; i < facilityLocation[0][0].length; i++)
        {
            for (int j = 0; j < facilityLocation[0][0].length; j++)
            {
                price += facilityLocation[0][i][j] * facilityLocation[1][s[i]][s[j]];
            }
        }
        return new double[] { price };
    }
}
