package org.seage.aal.algorithm;

import java.util.Random;

import junit.framework.Assert;

import org.seage.aal.data.AlgorithmParams;
import org.seage.data.DataNode;

public class AlgorithmAdapterTester extends AlgorithmAdapterTestBase
{

    public AlgorithmAdapterTester(IAlgorithmAdapter algorithm, /*Object[][] solutions,*/ AlgorithmParams algParams) throws Exception
    {
        _algorithm = algorithm;
        //_solutions = solutions;
        _algParams = algParams;
        
        
    	Random rnd = new Random(4);
    	_solutions = new Integer[NUM_SOLUTIONS][];
    	
    	for(int i=0;i<NUM_SOLUTIONS;i++)
    	{
    		_solutions[i] = new Integer[SOLUTION_LENGTH];
    		for(int j=0;j<SOLUTION_LENGTH;j++)
    		{
    			_solutions[i][j] = j+1;
    		}
    		for(int j=0;j<SOLUTION_LENGTH;j++)
    		{
    			int ix1 = rnd.nextInt(SOLUTION_LENGTH);
    			int ix2 = rnd.nextInt(SOLUTION_LENGTH);
    			Object a = _solutions[i][ix1]; 
    			_solutions[i][ix1] = _solutions[i][ix2];
    			_solutions[i][ix2] = a;
    		}
    	}
    }
    
    public void setAlgParameters(DataNode params) throws Exception
    {
        _algParams.removeDataNode(params.getName(), 0);
        _algParams.putDataNodeRef(params);
    }

    @Override
    public void testPhenotype() throws Exception
    {
        _algorithm.solutionsFromPhenotype(_solutions);
        Object[][] solutions = _algorithm.solutionsToPhenotype();        
        
        for(int k=0;k<solutions.length;k++)
        {
        	boolean match = false;
	        for(int i=0;i<_solutions.length;i++)
	            for(int j=0;j<_solutions[i].length;j++)
	                if(!solutions[k][j].equals(_solutions[i][j]))
	                	break;
	                else if((j+1)==_solutions[i].length)
	                	match = true;
	                    
	        if(!match){
	        	Assert.failNotSame("Phenotype transformation failed.", _solutions, solutions);
	        	return;
	        }
	        
	        
        }
        
        boolean flags[] = new boolean[solutions[0].length];        
        	
        for(int i=0;i<solutions.length;i++)
        {
        	for(int j=0;j<flags.length;j++)
            	flags[j] = false;
        	for(int j=0;j<solutions[i].length;j++)
        	{
        		if(!flags[(Integer)solutions[i][j]-1])
        			flags[(Integer)solutions[i][j]-1] = true;
        		else{
        			Assert.fail("Invalid phenotype solution");
        			return;
        		}
        	}
        	for(int j=0;j<solutions[i].length;j++)
        		if(!flags[(Integer)solutions[i][j]-1]){
        			Assert.fail("Invalid phenotype solution");
        			return;
        		}
        }
    }

    @Override
    public void testAlgorithm() throws Exception
    {
        _algorithm.solutionsFromPhenotype(_solutions);
        _algorithm.startSearching(_algParams);
        _algorithm.solutionsToPhenotype();
//        _algorithm.solutionsFromPhenotype(_solutions);
//        _algorithm.startSearching();

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
        testAlgorithm();
    }

    @Override
    public void testAsyncRunning() throws Exception
    {
        _algorithm.solutionsFromPhenotype(_solutions);
        _algorithm.startSearching(_algParams, true);
        Assert.assertTrue(_algorithm.isRunning());
        
        _algorithm.stopSearching();
        
        Assert.assertFalse(_algorithm.isRunning());
    }

	@Override
	public void testReport() throws Exception
	{
		testAlgorithm();
		_algReport = _algorithm.getReport();
		Assert.assertNotNull(_algReport);
		Assert.assertTrue(_algReport.containsNode("Parameters"));
		Assert.assertTrue(_algReport.containsNode("Minutes"));
		Assert.assertTrue(_algReport.containsNode("Statistics"));
		
		for(String attName : _algReport.getDataNode("Parameters").getValueNames())
			Assert.assertEquals(_algParams.getDataNode("Parameters").getValue(attName), _algReport.getDataNode("Parameters").getValue(attName));
		
		DataNode stats = _algReport.getDataNode("Statistics");
		Assert.assertTrue(stats.getValueInt("numberOfIter") > 0);
		Assert.assertTrue(stats.getValueDouble("initObjVal") > 0);
		Assert.assertTrue(stats.getValueInt("avgObjVal") > 0);
		Assert.assertTrue(stats.getValueInt("bestObjVal") > 0);
		Assert.assertTrue(stats.getValueInt("lastIterNumberNewSol") == 0 || stats.getValueInt("bestObjVal")< stats.getValueDouble("initObjVal") );
		Assert.assertTrue(stats.getValueInt("numberOfNewSolutions") > 0 || stats.getValueInt("lastIterNumberNewSol") == 0);
		Assert.assertTrue(stats.getValueInt("lastIterNumberNewSol") > 0 || stats.getValueInt("numberOfNewSolutions") == 0);
	}
}
