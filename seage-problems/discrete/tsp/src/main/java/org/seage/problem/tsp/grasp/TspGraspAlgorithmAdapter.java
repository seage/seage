package org.seage.problem.tsp.grasp;

import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.reporter.AlgorithmReport;

public class TspGraspAlgorithmAdapter implements IAlgorithmAdapter
{

    @Override
    public void startSearching(AlgorithmParams params) throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void startSearching(AlgorithmParams params, boolean async)
            throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void stopSearching() throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isRunning()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public AlgorithmReport getReport() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void solutionsFromPhenotype(Object[][] source) throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Object[][] solutionsToPhenotype() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public Object[] solutionToPhenotype(Object solution) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
