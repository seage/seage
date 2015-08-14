package org.seage.problem.sat.tabusearch;

import java.util.ArrayList;

import org.seage.metaheuristic.tabusearch.LongTermMemory;
import org.seage.metaheuristic.tabusearch.Solution;

/**
 * Summary description for SatLongTermMemory.
 */
public class SatLongTermMemory implements LongTermMemory
{
    private int[] _literalCount;
    private int _iterNumber;
    private SatSolution _bestSolution;

    private ArrayList _report;
    private ArrayList _column;

    public SatLongTermMemory(int numLiterals)
    {
        _literalCount = new int[numLiterals];
        _iterNumber = 0;

        _report = new ArrayList();
        // _column = new ArrayList();
    }

    public void clearMemory()
    {
        for (int i = 0; i < _literalCount.length; i++)
        {
            _literalCount[i] = 0;
        }
    }

    public void memorizeSolution(Solution soln, boolean newBestSoln)
    {
        // if (_iterNumber == 0)
        // {
        // _column = new ArrayList();
        // _report.add(_column);
        // }

        _iterNumber++;
        SatSolution satSol = (SatSolution) soln;
        for (int i = 0; i < _literalCount.length; i++)
        {
            if (satSol.getLiteralValues()[i])
                _literalCount[i]++;
        }
        if (newBestSoln)
        {
            _bestSolution = (SatSolution) satSol.clone();
            // System.out.println(_iterNumber + "\t" + satSol);
        }
        // System.out.println(_iterNumber + "\t" + satSol);
        // System.out.println(_iterNumber + "\t" + satSol + "\t"+_bestSolution);
        // long[] val = bitsToNumbers(satSol.getLiteralValues());
        // System.out.println(val[0] +"\t"+val[1]);
        // _column.add(val);
    }

    public Solution diversifySolution()
    {
        SatSolution result = new SatSolution(new boolean[_literalCount.length]);
        int sum = 0;
        int avg = 0;
        for (int i = 0; i < _literalCount.length; i++)
        {
            sum += _literalCount[i];
        }
        avg += sum / _literalCount.length;

        for (int i = 0; i < _literalCount.length; i++)
        {
            result.getLiteralValues()[i] = _literalCount[i] > avg ? false : true;
        }

        return result;
    }

    public String toString()
    {
        String result = "";
        int sum = 0;
        for (int i = 0; i < _literalCount.length; i++)
        {
            result += (i + 1) + "\t" + _literalCount[i] + "\n";
            sum += _literalCount[i];
        }
        result += sum / _literalCount.length;
        return result;
    }

    public void resetIterNumber()
    {
        _iterNumber = 0;
    }

    private long[] bitsToNumbers(boolean bits[])
    {
        long val0 = 0;
        long val1 = 0;

        for (int i = 0; i < bits.length / 2; i++)
        {
            if (bits[i])
                val0 += Math.pow(2, i);
            if (bits[i + bits.length / 2])
                val1 += Math.pow(2, i);
        }
        if (bits.length % 2 == 1)
            if (bits[bits.length - 1])
                val1 += Math.pow(2, bits.length - 1);

        return new long[] { val0, val1 };
    }

    public void report()
    {
        int ix = 0;
        boolean next = true;

        while (next)
        {
            String line = "";
            next = false;
            for (int i = 0; i < _report.size(); i++)
            {
                ArrayList column = (ArrayList) _report.get(i);
                long[] val = null;
                if (ix < column.size())
                {
                    val = (long[]) column.get(ix);
                    line += val[0] + "\t" + val[1] + "\t";
                    next = true;
                }

            }
            ix++;
            System.out.println(line);
        }
    }
}
