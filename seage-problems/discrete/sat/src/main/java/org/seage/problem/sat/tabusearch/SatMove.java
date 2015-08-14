package org.seage.problem.sat.tabusearch;

import org.seage.metaheuristic.tabusearch.Move;
import org.seage.metaheuristic.tabusearch.Solution;

/**
 * Summary description for KnapMove.
 */
public class SatMove implements Move
{
    private int[] _indexes;
    private int[] _values; // nastavi elem[_index+0] na 0/1
    private int _hash;

    public SatMove(int[] indexes, int[] values)
    {
        _indexes = indexes;
        _values = values;

        _hash = 0xaabbff;
        _hash ^= (_indexes[0] << 10) ^ (_indexes[0] << 5);
        _hash ^= (_indexes[1] << 10) ^ (_indexes[1] << 5);
        _hash ^= (_values[0] << 10) ^ (_values[0] << 5);
        _hash ^= (_values[1] << 10) ^ (_values[1] << 5);

        _hash = _hash % 0xffffff;
    }

    public void operateOn(Solution soln)
    {
        SatSolution satSol = (SatSolution) soln;

        satSol.getLiteralValues()[_indexes[0]] = _values[0] == 1 ? true : false;
        satSol.getLiteralValues()[_indexes[1]] = _values[1] == 1 ? true : false;

    }

    public int[] getIndexes()
    {
        return _indexes;
    }

    public int[] getValues()
    {
        return _values;
    }

    public int hashCode()
    {
        return _hash;
    }
}
