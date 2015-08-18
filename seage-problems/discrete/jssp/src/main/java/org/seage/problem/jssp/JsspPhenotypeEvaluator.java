package org.seage.problem.jssp;

import org.seage.aal.algorithm.IPhenotypeEvaluator;

public class JsspPhenotypeEvaluator implements IPhenotypeEvaluator
{
    private JobsDefinition _jobsDefinition;
    private int _numJobs;
    private int _numMachines;

    private int[] _lastActivityInJobIndex;
    private int[] _lastActivityOnMachineIndex;

    private int[] _endTimeInJob;
    private int[] _endTimeOnMachine;
    
    public JsspPhenotypeEvaluator(JobsDefinition jobsDefinition)
    {
        _jobsDefinition = jobsDefinition;
        
        _numJobs = _jobsDefinition.getJobsCount();
        _numMachines = _jobsDefinition.getMachinesCount();

        _lastActivityInJobIndex = new int[_numJobs];
        _lastActivityOnMachineIndex = new int[_numMachines];

        _endTimeInJob = new int[_numJobs];
        _endTimeOnMachine = new int[_numMachines];
    }
    
    @Override
    public double[] evaluate(Object[] phenotypeSubject) throws Exception
    {
        Integer[] jobArray = (Integer[])phenotypeSubject;
        
        JobInfo currentJob;
        OperationInfo currentOper;

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

            currentJob = _jobsDefinition.getJobInfos()[indexCurrentJob];
            currentOper = currentJob.getOperationInfos()[indexCurrentOper];

            indexCurrentMachine = currentOper.MachineID - 1;

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

            if (_endTimeOnMachine[indexCurrentMachine] > maxMakeSpan)
            {
                maxMakeSpan = _endTimeOnMachine[indexCurrentMachine];
            } 
        }
                
        return new double[] { maxMakeSpan };
    }
    
    @Override
    public int compare(double[] arg0, double[] arg1)
    {
        return 0;
    }

}
