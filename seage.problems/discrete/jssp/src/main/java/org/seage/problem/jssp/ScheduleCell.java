package jssp.data;

/**
 * Summary description for ScheduleCell.
 */
public class ScheduleCell
{
	private int _index;
	private int _startTime;
	private int _length;

	private ScheduleCell _prevCellOnMachine;
	private ScheduleCell _nextCellOnMachine;
	private ScheduleCell _previousCellInJob;

	public ScheduleCell(int index, int startTime, int length)
	{
		_index = index;
		_startTime = startTime;
		_length = length;
	}

	public ScheduleCell getPreviousCellOnMachine()
	{
		return _prevCellOnMachine;
	}
	public void setPreviousCellOnMachine(ScheduleCell prev)
	{
		_prevCellOnMachine = prev;
	}

	public ScheduleCell getNextCellOnMachine()
	{
		return _nextCellOnMachine;
	}
	public void setNextCellOnMachine(ScheduleCell next)
	{
		_nextCellOnMachine = next;
	}

	public ScheduleCell getPreviousCellInJob()
	{
		return _previousCellInJob;
	}
	public void setPreviousCellInJob(ScheduleCell prev)
	{
		_previousCellInJob = prev;
	}
		
		
	public int getStartTime()
	{
		return _startTime;
	}

	public int getEndTime()
	{
		return _startTime + _length;
	}

	public int getLength()
	{
		return _length;
	}

	public int getIndex()
	{
		return _index;
	}

	public int compareStartTo(ScheduleCell comp)
	{
		return _startTime - comp._startTime;
	}

	public boolean compareStart2EndTo(ScheduleCell comp)
	{
		if (this._startTime == comp._startTime + comp._length)
			return true;
		else
			return false;
	}
	
}
