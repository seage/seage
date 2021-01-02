/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.problem.sat.genetics;

import java.util.ArrayList;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.genetics.GeneticAlgorithmAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.SatPhenotype;
import org.seage.problem.sat.SatPhenotypeEvaluator;

/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("GeneticAlgorithm")
@Annotations.AlgorithmName("GeneticAlgorithm")
public class SatGeneticAlgorithmFactory implements IAlgorithmFactory<SatPhenotype, Subject<Boolean>> {

    @Override
    public Class<?> getAlgorithmClass() {
        return GeneticAlgorithmAdapter.class;
    }

    @Override
    public IAlgorithmAdapter<SatPhenotype, Subject<Boolean>> createAlgorithm(ProblemInstance instance,
            IPhenotypeEvaluator<SatPhenotype> phenotypeEvaluator) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

//    @Override
//    public IAlgorithmAdapter<SatPhenotype, Subject<Boolean>> createAlgorithm(ProblemInstance instance,
//            IPhenotypeEvaluator<SatPhenotype> phenotypeEvaluator) throws Exception {
//        Formula formula = (Formula) instance;
//        IAlgorithmAdapter<SatPhenotype, Subject<Boolean>> algorithm = new GeneticAlgorithmAdapter<>(new SatGeneticOperator(),
//                new SatEvaluator(new SatPhenotypeEvaluator(formula)), phenotypeEvaluator, false) {
//            @Override
//            public void solutionsFromPhenotype(SatPhenotype[] source) throws Exception {
//                _solutions = new ArrayList<Subject<Boolean>>(source.length);
//                for (int i = 0; i < source.length; i++)
//                    _solutions.add(new Subject<Boolean>(source[i].getSolution()));
//            }
//
//            @Override
//            public SatPhenotype[] solutionsToPhenotype() throws Exception {
//                SatPhenotype[] result = new SatPhenotype[_solutions.size()];
//
//                for (int i = 0; i < _solutions.size(); i++) {                    
//                    result[i] = new SatPhenotype(_solutions.get(i).getChromosome().getGenes());
//                }
//                return result;
//            }
//
//            @Override
//            public SatPhenotype solutionToPhenotype(Subject<Boolean> solution) throws Exception {
//                // TODO Auto-generated method stub
//                return null;
//            }
//        };
//
//        return algorithm;
//    }

}
