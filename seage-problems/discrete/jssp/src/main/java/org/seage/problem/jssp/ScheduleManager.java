package jssp.data;
import ailibrary.data.*;

/**
 * Summary description for ScheduleManager.
 */
public class ScheduleManager
{
	DataTable _jobsTable;

	private int _numJobs;
	private int _numMachines;

	private int[] _lastActivityInJobIndex;
	private int[] _lastActivityOnMachineIndex;

	private int[] _endTimeInJob;
	private int[] _endTimeOnMachine;


	public ScheduleManager()
	{
		_jobsTable = DataStore.getInstance().getDataTable("jobs");
		_numJobs = _jobsTable.getRowCount();
		_numMachines = getNumMachines();

		_lastActivityInJobIndex = new int[_numJobs];
		_lastActivityOnMachineIndex = new int[_numMachines];

		_endTimeInJob = new int[_numJobs];
		_endTimeOnMachine = new int[_numMachines];

	}

	private int getNumMachines()
	{
		int val = 0;
		int numMachines = Integer.MIN_VALUE;

		for (int i = 0; i < _jobsTable.getRowCount(); i++)
		{
			for (int j = 0; j < _jobsTable.getRow(i).getCellCount(); j++)
			{
				val = ((OperationInfo)_jobsTable.getRow(i).getCell(j).getCellProperty()).MachineID;
				if (val > numMachines)
					numMachines = val;
			}
		}
		return numMachines;
	}

	public Object[] evaluateSchedule(int[] jobArray, boolean buildSchedule)
	{
		Schedule schedule = null;
		if (buildSchedule)
			schedule = new Schedule(_numJobs, _numMachines);

		JobInfo currentJob;
		OperationInfo currentOper;

		DataRow currentRow;
		DataCell currentCell;

		int indexCurrentMachine = 0;
		int indexCurrentJob = 0;
		int indexCurrentOper = 0;

		int maxMakeSpan = 0;

		for (int i = 0; i < _numJobs; i++)
		{
			_lastActivityInJobIndex[i] = 0;
			_endTimeInJob[i] = 0;
		}
		for (int i = 0; i < _numMachines; i++)
		{
			_lastActivityOnMachineIndex[i] = 0;
			_endTimeOnMachine[i] = 0;
		}

		for (int i = 0; i < jobArray.length; i++)
		{
			indexCurrentJob = jobArray[i] - 1;

			indexCurrentOper = _lastActivityInJobIndex[indexCurrentJob]++;

			currentRow = _jobsTable.getRow(indexCurrentJob);
			currentCell = currentRow.getCell(indexCurrentOper);

			currentJob = (JobInfo)currentRow.getRowProperty();
			currentOper = (OperationInfo)currentCell.getCellProperty();

			//indexCurrentMachine = _lastActivityOnMachineIndex[currentOper.MachineID - 1]++;
			indexCurrentMachine = currentOper.MachineID - 1;
			//System.out.print(indexCurrentMachine + ":" + indexCurrentJob + " ");
			//System.out.print(indexCurrentJob + ":" + indexCurrentOper + " ");

			
			if (_endTimeOnMachine[indexCurrentMachine] > _endTimeInJob[indexCurrentJob])
			{
				_endTimeOnMachine[indexCurrentMachine] += currentOper.Length;
				_endTimeInJob[indexCurrentJob] = _endTimeOnMachine[indexCurrentMachine];
			}
			else
			{
				_endTimeInJob[indexCurrentJob] += currentOper.Length;
				_endTimeOnMachine[indexCurrentMachine] = _endTimeInJob[indexCurrentJob];								
			}

			if (_endTimeOnMachine[indexCurrentMachine]  > maxMakeSpan)
			{
				maxMakeSpan = _endTimeOnMachine[indexCurrentMachine];
			}

			int a = 0;
			if (buildSchedule)
				schedule.addCell(indexCurrentJob,
								  indexCurrentMachine,
								  new ScheduleCell( i,
													_endTimeOnMachine[indexCurrentMachine] - currentOper.Length,
													currentOper.Length));

		}
		//System.out.println();

		Object[] res = { new Integer(maxMakeSpan), schedule/*, _numJobs, _numMachines */};
		return res;
	}
}
