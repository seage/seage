package org.seage.aal.algorithm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;

import org.seage.aal.algorithm.algbase.AlgorithmAdapterTestBase;
import org.seage.data.DataNode;

public class AlgorithmAdapterTester<S> extends AlgorithmAdapterTestBase<S>
{
    public AlgorithmAdapterTester(IAlgorithmAdapter<TestPhenotype, S> algorithm, AlgorithmParams algParams) throws Exception
    {
        _algAdapter = algorithm;
        _algParams = algParams;
    }

    public void setAlgParameters(AlgorithmParams params) throws Exception
    {
        _algParams = params;
    }

    @Override
    public void testAlgorithm() throws Exception
    {
        _algAdapter.solutionsFromPhenotype(createPhenotypeSolutions());
        _algAdapter.startSearching(_algParams);
        assertNull(_algAdapter.solutionsToPhenotype());
        _algAdapter.solutionsToPhenotype();
    }

    @Override
    public void testAlgorithmWithParamsNull() throws Exception
    {
        try
        {
            _algParams = null;
            testAlgorithm();
            fail("Algorithm should throw an exception when parameters not set.");
        }
        catch (Exception ex)
        {
        }
    }

    @Override
    public void testAlgorithmWithParamsAtZero() throws Exception
    {
        _algAdapter.solutionsFromPhenotype(createPhenotypeSolutions());
        _algAdapter.startSearching(_algParams);
    }

    @Override
    public void testAsyncRunning() throws Exception
    {
        _algAdapter.solutionsFromPhenotype(createPhenotypeSolutions());
        _algAdapter.startSearching(_algParams, true);
        assertTrue(_algAdapter.isRunning());

        _algAdapter.stopSearching();

        assertFalse(_algAdapter.isRunning());
    }

    @Override
    public void testReport() throws Exception
    {
        testAlgorithm();
        _algReport = _algAdapter.getReport();
        assertNotNull(_algReport);
        assertTrue(_algReport.containsNode("Parameters"));
        assertTrue(_algReport.containsNode("Log"));
        assertTrue(_algReport.containsNode("Statistics"));

        for (String attName : _algReport.getDataNode("Parameters").getValueNames())
            assertEquals(_algParams.getValue(attName), _algReport.getDataNode("Parameters").getValue(attName));

        DataNode stats = _algReport.getDataNode("Statistics");
        assertTrue(stats.getValueInt("numberOfIter") > 1);
        assertTrue(stats.getValueDouble("initObjVal") > 0);
        assertTrue(stats.getValueInt("avgObjVal") > 0);
        assertTrue(stats.getValueInt("bestObjVal") > 0);
        assertFalse(stats.getValueInt("initObjVal") < stats.getValueInt("bestObjVal"));
        assertTrue((stats.getValueInt("initObjVal") != stats.getValueInt("bestObjVal"))
                || (stats.getValueInt("numberOfNewSolutions") == 1));
        assertTrue(stats.getValueInt("bestObjVal") == stats.getValueDouble("initObjVal")
                || stats.getValueInt("lastIterNumberNewSol") > 1);
        assertTrue(
                stats.getValueInt("numberOfNewSolutions") > 0 || stats.getValueInt("lastIterNumberNewSol") == 0);
        assertTrue(
                stats.getValueInt("lastIterNumberNewSol") > 1 || stats.getValueInt("numberOfNewSolutions") == 1);
    }

    private TestPhenotype[] createPhenotypeSolutions()
    {
        Random rnd = new Random(4);
        TestPhenotype[] solutions = new TestPhenotype[NUM_SOLUTIONS];

        for (int i = 0; i < NUM_SOLUTIONS; i++)
        {            
            Integer[] array = new Integer[SOLUTION_LENGTH]; 
            for (int j = 0; j < SOLUTION_LENGTH; j++)
            {
                array[j] = j + 1;
            }
            
            for (int j = 0; j < SOLUTION_LENGTH; j++)
            {
                int ix1 = rnd.nextInt(SOLUTION_LENGTH);
                int ix2 = rnd.nextInt(SOLUTION_LENGTH);
                Integer a = array[ix1];
                array[ix1] = array[ix2];
                array[ix2] = a;
            }
            solutions[i] = new TestPhenotype(array);
        }

        return solutions;

    }
}
