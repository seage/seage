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
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.reasoning.algparams.evolution.grammar;

import org.seage.metaheuristic.genetics.Subject;
import org.seage.reasoning.algparams.Policy.Attribute;
import org.seage.aal.reporter.AlgorithmReportEvaluator;
import org.seage.reasoning.algparams.Policy;
import org.seage.reasoning.algparams.evolution.EvolutionReasoner;

/**
 * Evolution of grammar rules used for algorithm parameter settings.
 * @author rick
 */
public class GrammarEvolutionReasoner extends EvolutionReasoner
{

    public GrammarEvolutionReasoner(Attribute[] atts, AlgorithmReportEvaluator re)
    {
        super(atts, re);
    }

    @Override
    protected Policy createPolicy(Subject subject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
