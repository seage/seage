package org.seage.aal.algorithm;

import junit.framework.Assert;

import org.seage.aal.data.AlgorithmParams;
import org.seage.data.DataNode;

public class AlgorithmAdapterTester extends AlgorithmAdapterTestBase
{

    public AlgorithmAdapterTester(IAlgorithmAdapter algorithm, Object[][] solutions, AlgorithmParams algParams) throws Exception
    {
        _algorithm = algorithm;
        _solutions = solutions;
        _algParams = algParams;
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
        
        for(int i=0;i<_solutions.length;i++)
            for(int j=0;j<_solutions[i].length;j++)
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
//        _algorithm.solutionsFromPhenotype(_solutions);
//        _algorithm.startSearching();

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
        _algorithm.setParameters(_algParams);
        _algorithm.startSearching(true);
        
		Thread.sleep(100);
        Assert.assertTrue(_algorithm.isRunning());
        
        _algorithm.stopSearching();
        
		Thread.sleep(100);        
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
	}
}
