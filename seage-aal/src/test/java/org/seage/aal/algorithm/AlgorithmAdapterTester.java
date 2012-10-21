package org.seage.aal.algorithm;

import junit.framework.Assert;

import org.seage.aal.data.AlgorithmParams;

public class AlgorithmAdapterTester extends AlgorithmAdapterTestBase
{

    public AlgorithmAdapterTester(IAlgorithmAdapter algorithm, Object[][] solutions, AlgorithmParams algParams) throws Exception
    {
        _algorithm = algorithm;
        _solutions = solutions;
        _algParams = algParams;
    }

    @Override
    public void testPhenotype() throws Exception
    {
        _algorithm.solutionsFromPhenotype(_solutions);
        Object[][] solutions = _algorithm.solutionsToPhenotype();
        
        for(int i=0;i<solutions.length;i++)
            for(int j=0;j<solutions[i].length;j++)
                if(!solutions[i][j].equals(_solutions[i][j]))
                    Assert.failNotSame("Phenotype transformation failed.", _solutions, solutions);

    }

    @Override
    public void testAlgorithm() throws Exception
    {
        _algorithm.solutionsFromPhenotype(_solutions);
        _algorithm.setParameters(_algParams);
        _algorithm.startSearching();
        _algorithm.solutionsToPhenotype();
        _algorithm.solutionsFromPhenotype(_solutions);
        _algorithm.startSearching();

    }

    @Override
    public void testAlgorithmWithZeroParams() throws Exception
    {
        testAlgorithm();
    }

}
