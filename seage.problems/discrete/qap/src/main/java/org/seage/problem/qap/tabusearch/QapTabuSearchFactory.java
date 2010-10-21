/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Karel Durkota
 *     - Initial implementation
 *     Richard Malek
 *     - Added algorithm annotations
 */
package org.seage.problem.qap.tabusearch;

import org.seage.aal.Annotations;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.IProblemProvider;
import org.seage.aal.tabusearch.TabuSearchAdapter;
import org.seage.data.DataNode;
import org.seage.metaheuristic.tabusearch.Solution;
import org.seage.problem.qap.QapProblemProvider;

/**
 *
 * @author Karel Durkota
 */
@Annotations.AlgorithmId("TabuSearch")
@Annotations.AlgorithmName("Tabu Search")
public class QapTabuSearchFactory implements IAlgorithmFactory
{
//    private DataNode _algParams;
//    private TspProblemProvider _provider;
//
//    public TspTabuSearchFactory(DataNode algParams, TspProblemProvider provider)
//    {
//        _algParams = algParams;
//        _provider = provider;
//    }
    
    public void setProblemProvider(IProblemProvider provider) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public Class getAlgorithmClass() {
        return TabuSearchAdapter.class;
    }

    public IAlgorithmAdapter createAlgorithm() throws Exception
    {
        IAlgorithmAdapter algorithm;
        Double[][][] facilityLocation = QapProblemProvider.getFacilityLocation();

        algorithm = new TabuSearchAdapter(new QapMoveManager(), new QapObjectiveFunction(facilityLocation), new QapLongTermMemory(), "" ) {

            public void solutionsFromPhenotype(Object[][] source) throws Exception
            {
                _solutions = new Solution[source.length];
                for(int i=0;i<source.length;i++)
                {
                    QapSolution s = new QapSolution();
                    Integer[] assign = new Integer[source[i].length];
                    for(int j=0;j<assign.length;j++)
                        assign[j] = (Integer)source[i][j];
                    s.setAssign(assign);
                    _solutions[i] = s;
                }
            }

            public Object[][] solutionsToPhenotype() throws Exception
            {
                Object[][] result = new Object[_solutions.length][];

                for(int i=0;i<_solutions.length;i++)
                {
                    QapSolution s = (QapSolution)_solutions[i];
                    result[i] = new Integer[s.getAssign().length];
                    for(int j=0;j<s.getAssign().length;j++)
                    {
                        result[i][j] = s.getAssign()[j];
                    }
                }
                return result;
            }
        };

        //Object[][] solutions = _provider.generateInitialSolutions( _algParams.getValueInt("numSolution"));
        //algorithm.solutionsFromPhenotype(solutions);

        return algorithm;
    }

    public DataNode getAlgorithmParameters(DataNode params) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
