/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.jssp.tabusearch;

import java.io.FileInputStream;

import org.seage.metaheuristic.tabusearch.BestEverAspirationCriteria;
import org.seage.metaheuristic.tabusearch.SimpleTabuList;
import org.seage.metaheuristic.tabusearch.TabuSearch;
import org.seage.metaheuristic.tabusearch.TabuSearchEvent;
import org.seage.metaheuristic.tabusearch.TabuSearchListener;
import org.seage.problem.jssp.JobsDefinition;
import org.seage.problem.jssp.JsspPhenotypeEvaluator;

/**
 *
 * @author Richard Malek
 */
public class JsspTabuSearchTest implements TabuSearchListener
{
    public static void main(String[] args)
    {
        try
        {
            String path = "data/jssp/abz6.xml";

            new JsspTabuSearchTest().run(path);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void run(String path) throws Exception
    {
        JobsDefinition jobsDef = new JobsDefinition(null, new FileInputStream(path));
        System.out.println("Loading jobs from path: " + path);
        System.out.println("Number of jobs: " + jobsDef.getJobsCount());

        JsspPhenotypeEvaluator evaluator = new JsspPhenotypeEvaluator(jobsDef);
        
        TabuSearch ts = new TabuSearch(
                new JsspSolution(jobsDef.getJobsCount(), jobsDef.getJobInfos()[0].getOperationInfos().length),
                new JsspMoveManager(evaluator),
                new JsspObjectiveFunction(evaluator),
                new SimpleTabuList(7),
                new BestEverAspirationCriteria(),
                false);

        ts.addTabuSearchListener(this);
        ts.setIterationsToGo(1500000);
        ts.startSolving();
    }

    @Override
    public void newBestSolutionFound(TabuSearchEvent e)
    {
        System.out.println(
                e.getTabuSearch().getBestSolution().toString() + " - " + e.getTabuSearch().getIterationsCompleted());
    }

    @Override
    public void improvingMoveMade(TabuSearchEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void newCurrentSolutionFound(TabuSearchEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void noChangeInValueMoveMade(TabuSearchEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void tabuSearchStarted(TabuSearchEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void tabuSearchStopped(TabuSearchEvent e)
    {
        System.out.println("finished");
    }

    @Override
    public void unimprovingMoveMade(TabuSearchEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

}
