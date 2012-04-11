package jssp.algorithm.tabusearch;

import jssp.data.*;
import ailibrary.algorithm.tabusearch.*;
/**
 * Summary description for JsspObjectiveFunction.
 */
public class JsspObjectiveFunction implements ObjectiveFunction
{
	private ScheduleManager _scheduleManager;

	public JsspObjectiveFunction(ScheduleManager scheduleManager)
	{
		_scheduleManager = scheduleManager;
	}

	public double[] evaluate(Solution soln, Move move) throws Exception
	{
		//JsspSolution solution = (JsspSolution)soln;		
		//int[] jobArray = (int[])solution.getJobArray().clone();

		//if (move!=null)
		//{
		//    JsspMove move2 = (JsspMove)move;
		//    int tmp = jobArray[move2.getIndex1()];
		//    jobArray[move2.getIndex1()] = jobArray[move2.getIndex2()];
		//    jobArray[move2.getIndex2()] = tmp;
		//}
		if (move != null)
			move.operateOn(soln);
		int[] jobArray = (int[])((JsspSolution)soln).getJobArray();
		Object[] values = _scheduleManager.evaluateSchedule(jobArray, false);
		if (move != null)
			move.operateOn(soln);


		return new double[] { ((Integer)values[0]).intValue() };
	}
}
