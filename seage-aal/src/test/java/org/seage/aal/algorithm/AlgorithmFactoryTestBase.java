package org.seage.aal.algorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.data.DataNode;

public abstract class AlgorithmFactoryTestBase
{
    protected IAlgorithmFactory _algorithmFactory;
    protected IProblemProvider _problemProvider;
    protected String _algorithmID;

    public AlgorithmFactoryTestBase(IProblemProvider problemProvider, String algorithmID)
    {
        _problemProvider = problemProvider;
        _algorithmID = algorithmID;
    }

    @Before
    public void setUp() throws Exception
    {
        assertNotNull(_problemProvider);
        assertNotNull(_problemProvider.getProblemInfo());
        _algorithmFactory = _problemProvider.getAlgorithmFactory(_algorithmID);
        assertNotNull(_algorithmFactory);
    }

    @Test
    public void testGetAlgorithmClass()
    {
        assertNotNull(_algorithmFactory.getAlgorithmClass());
    }

    @Test
    public void testCreateAlgorithm() throws Exception
    {
        assertNotNull(_problemProvider);
        List<ProblemInstanceInfo> infos = _problemProvider.getProblemInfo().getProblemInstanceInfos();
        assertTrue(infos.size() > 0);
        ProblemInstanceInfo pii = infos.get(0);

        IAlgorithmAdapter aa = _algorithmFactory.createAlgorithm(_problemProvider.initProblemInstance(pii));
        assertNotNull(aa);
    }

    @Test
    public void testAlgorithmAdapter() throws Exception
    {
        assertNotNull(_problemProvider);
        List<ProblemInstanceInfo> infos = _problemProvider.getProblemInfo().getProblemInstanceInfos();
        assertTrue(infos.size() > 0);
        ProblemInstanceInfo pii = infos.get(0);

        IAlgorithmAdapter aa = _algorithmFactory.createAlgorithm(_problemProvider.initProblemInstance(pii));
        assertNotNull(aa);

        Object[][] solutions = _problemProvider.generateInitialSolutions(_problemProvider.initProblemInstance(pii), 10,
                1);
        assertNotNull(solutions);
        aa.solutionsFromPhenotype(solutions);
        Object[][] solutions2 = aa.solutionsToPhenotype();
        assertNotNull(solutions2);
        assertEquals(solutions.length, solutions2.length);

        for (int i = 0; i < solutions.length; i++)
        {
            assertNotNull(solutions[i]);
            assertNotNull(solutions2[i]);
            for (int j = 0; j < solutions[i].length; j++)
            {
                assertEquals(solutions[i][j], solutions2[i][j]);
            }
        }

        AlgorithmParams params = createAlgorithmParams(_problemProvider.getProblemInfo());
        solutions = _problemProvider.generateInitialSolutions(_problemProvider.initProblemInstance(pii),
                params.getValueInt("numSolutions"), 1);
        aa.solutionsFromPhenotype(solutions);
        aa.startSearching(params);

        solutions2 = aa.solutionsToPhenotype();
        assertNotNull(solutions2);
        assertEquals(solutions.length, solutions2.length);

        for (int i = 0; i < solutions.length; i++)
        {
            assertNotNull(solutions[i]);
            assertNotNull(solutions2[i]);
            //			boolean theSame=true;
            //			for(int j=0;j<solutions[i].length;j++)
            //			{
            //				if(!solutions[i][j].equals( solutions2[i][j]))
            //				{
            //					theSame=false;
            //					break;
            //				}
            //			}
            //			assertFalse(theSame);
        }
    }

    private AlgorithmParams createAlgorithmParams(ProblemInfo problemInfo) throws Exception
    {
        AlgorithmParams result = new AlgorithmParams();
        DataNode algParamsNode = problemInfo.getDataNode("Algorithms").getDataNodeById(_algorithmID);
        for (DataNode param : algParamsNode.getDataNodes("Parameter"))
        {
            result.putValue(param.getValueStr("name"), param.getValue("init"));
        }
        result.putValue("iterationCount", 1);
        result.putValue("numSolutions", 1);
        return result;
    }
}
