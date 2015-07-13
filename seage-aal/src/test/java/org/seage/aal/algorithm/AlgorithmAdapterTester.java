package org.seage.aal.algorithm;

import java.util.Random;

import junit.framework.Assert;

import org.seage.aal.algorithm.algbase.AlgorithmAdapterTestBase;
import org.seage.data.DataNode;

public class AlgorithmAdapterTester extends AlgorithmAdapterTestBase
{
    public AlgorithmAdapterTester(IAlgorithmAdapter algorithm, AlgorithmParams algParams) throws Exception
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
        Assert.assertNull(_algAdapter.solutionsToPhenotype());
    }
    
    @Override
    public void testAlgorithmWithParamsNull() throws Exception
    {
    	try
    	{
    		_algParams = null;
    		testAlgorithm();
    		Assert.fail("Algorithm should throw an exception when parameters not set.");
    	}
    	catch(Exception ex)
    	{}
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
        Assert.assertTrue(_algAdapter.isRunning());
        
        _algAdapter.stopSearching();
        
        Assert.assertFalse(_algAdapter.isRunning());
    }

	@Override
	public void testReport() throws Exception
	{
		testAlgorithm();
		_algReport = _algAdapter.getReport();
		Assert.assertNotNull(_algReport);
		Assert.assertTrue(_algReport.containsNode("Parameters"));
		Assert.assertTrue(_algReport.containsNode("Minutes"));
		Assert.assertTrue(_algReport.containsNode("Statistics"));
		
		for(String attName : _algReport.getDataNode("Parameters").getValueNames())
			Assert.assertEquals(_algParams.getValue(attName), _algReport.getDataNode("Parameters").getValue(attName));
		
		DataNode stats = _algReport.getDataNode("Statistics");
		Assert.assertTrue(stats.getValueInt("numberOfIter") > 1);
		Assert.assertTrue(stats.getValueDouble("initObjVal") > 0);
		Assert.assertTrue(stats.getValueInt("avgObjVal") > 0);
		Assert.assertTrue(stats.getValueInt("bestObjVal") > 0);
		Assert.assertFalse(stats.getValueInt("initObjVal") < stats.getValueInt("bestObjVal"));
		Assert.assertTrue((stats.getValueInt("initObjVal") != stats.getValueInt("bestObjVal")) || (stats.getValueInt("numberOfNewSolutions") == 1));
		Assert.assertTrue(stats.getValueInt("bestObjVal") == stats.getValueDouble("initObjVal") || stats.getValueInt("lastIterNumberNewSol") > 1  );
		Assert.assertTrue(stats.getValueInt("numberOfNewSolutions") > 0 || stats.getValueInt("lastIterNumberNewSol") == 0);
		Assert.assertTrue(stats.getValueInt("lastIterNumberNewSol") > 1 || stats.getValueInt("numberOfNewSolutions") == 1);
	}
	
	private Object[][] createPhenotypeSolutions()
	{
		Random rnd = new Random(4);
		Object[][] solutions = new Integer[NUM_SOLUTIONS][];
    	
    	for(int i=0;i<NUM_SOLUTIONS;i++)
    	{
    		solutions[i] = new Integer[SOLUTION_LENGTH];
    		for(int j=0;j<SOLUTION_LENGTH;j++)
    		{
    			solutions[i][j] = j+1;
    		}
    		for(int j=0;j<SOLUTION_LENGTH;j++)
    		{
    			int ix1 = rnd.nextInt(SOLUTION_LENGTH);
    			int ix2 = rnd.nextInt(SOLUTION_LENGTH);
    			Object a = solutions[i][ix1]; 
    			solutions[i][ix1] = solutions[i][ix2];
    			solutions[i][ix2] = a;
    		}
    	}
    	
    	return solutions;
		
	}
}
