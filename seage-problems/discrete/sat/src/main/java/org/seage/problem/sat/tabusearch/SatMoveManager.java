package org.seage.problem.sat.tabusearch;

import java.util.ArrayList;
import java.util.Random;

import org.seage.metaheuristic.tabusearch.Move;
import org.seage.metaheuristic.tabusearch.MoveManager;
import org.seage.metaheuristic.tabusearch.Solution;

/**
 * Summary description for SatMoveManager.
 */
public class SatMoveManager implements MoveManager
{
    // public DataStore _dataStore;
    private SatMove[] _null;
    private ArrayList<SatMove> _moves;
    private Random _random;

    public SatMoveManager(/* DataStore dataStore */)
    {
        // _dataStore = dataStore;
        _moves = new ArrayList<SatMove>();
        _random = new Random(97789);
        _null = new SatMove[0];
    }

    private int[] getTableValue2(int val0, int val1)
    {
        if (val0 == 1 && val1 == 0)
            return new int[] { 0, 1 };
        if (val0 == 0 && val1 == 1)
            return new int[] { 1, 1 };
        if (val0 == 0 && val1 == 0)
            return new int[] { 1, 0 };
        // if (val0 == 1 && val1 == 1)
        return new int[] { 0, 0 };
    }

    private int[] getTableValue(int val0, int val1)
    {
        if (val0 == 1)
            return new int[] { 0, val1 };
        if (val0 == 0)
            return new int[] { 1, val1 };

        // if (val0 == 1 && val1 == 1)
        return new int[] { 0, 0 };
    }

    @Override
    public Move[] getAllMoves(Solution solution)
    {
        SatSolution satSol = (SatSolution) solution;
        _moves.clear();
        int numMoves = Math.min(satSol.getLiteralValues().length, 100);
        for (int i = 0; i < numMoves; i++)
        {
            int ix0 = _random.nextInt(satSol.getLiteralValues().length - 1);
            int ix1 = _random.nextInt(satSol.getLiteralValues().length - 1);
            // int ix0 = i;
            // int ix1 = (i + 1) % satSol.getLiteralValues().length;

            int val0 = satSol.getLiteralValues()[ix0] == true ? 1 : 0;
            int val1 = satSol.getLiteralValues()[ix1] == true ? 1 : 0;
            int[] diff = getTableValue(val0, val1);

            _moves.add(new SatMove(new int[] { ix0, ix1 }, diff));
        }
        // int ix0 = 0;
        // int ix1 = satSol.getLiteralValues().length - 1;
        // int val0 = satSol.getLiteralValues()[ix0] == true ? 1 : 0;
        // int val1 = satSol.getLiteralValues()[ix1] == true ? 1 : 0;
        // int[] diff = getTableValue(val0, val1);

        // result.add(new SatMove(new int[] { ix0, ix1}, diff));

        return _moves.toArray(_null);
    }
}
