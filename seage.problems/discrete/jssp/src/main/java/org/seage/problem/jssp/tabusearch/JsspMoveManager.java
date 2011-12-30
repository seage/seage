package jssp.algorithm.tabusearch;

import ailibrary.algorithm.tabusearch.*;
import jssp.data.*;
/**
 * Summary description for JsspMoveManager.
 */
public class JsspMoveManager implements MoveManager
{
	private ScheduleManager _scheduleManager;
	private int _maxMoves;

	public JsspMoveManager(ScheduleManager scheduleManager)
	{
		_scheduleManager = scheduleManager;
		_maxMoves = 100;
	}

	public Move[] getAllMoves(Solution solution) throws Exception
	{
		try
		{			
			JsspSolution sol = (JsspSolution)solution;

			Object[] val = _scheduleManager.evaluateSchedule(sol.getJobArray(), true);

			Schedule schedule = (Schedule)val[1];
			ScheduleCell[] criticalPath = schedule.findCriticalPath();

			JsspMove[] moves = new JsspMove[Math.min(criticalPath.length/2, _maxMoves)];
			for (int i = 0; i < moves.length; i++)
			{
				moves[i] = new JsspMove(criticalPath[2*i].getIndex(), criticalPath[2*i+1].getIndex());
			}

			return moves;
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}
}
