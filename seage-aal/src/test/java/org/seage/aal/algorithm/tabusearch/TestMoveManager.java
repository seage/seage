package org.seage.aal.algorithm.tabusearch;

import org.seage.metaheuristic.tabusearch.Move;
import org.seage.metaheuristic.tabusearch.MoveManager;
import org.seage.metaheuristic.tabusearch.Solution;

public class TestMoveManager implements MoveManager
{

    @Override
    public Move[] getAllMoves(Solution solution) throws Exception
    {
        Move m = new Move()
        {
            
            @Override
            public void operateOn(Solution soln)
            {
                // TODO Auto-generated method stub
                
            }
        };
        return new Move[]{m, m};
    }

}
