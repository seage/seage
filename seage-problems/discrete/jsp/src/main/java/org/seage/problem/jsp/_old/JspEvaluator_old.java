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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */
package org.seage.problem.jsp._old;

import org.seage.data.ds.DataStore;
import org.seage.data.ds.DataTable;
import org.seage.metaheuristic.genetics.Chromosome;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.metaheuristic.genetics.SubjectEvaluator;
import org.seage.problem.jsp.ScheduleOperationInfo;

/**
 * Summary description for JSPEvaluator.
 */
@SuppressWarnings("all")
public class JspEvaluator_old extends SubjectEvaluator
{
    private int[] _firstActivityToDoIndex_InJob; // prvni nenakreslena operace v jobu
    private int[] _firstActivityToDoIndex_OnMachine; // prvni nenakreslena operace na pracovisti

    int[] _maxMachineTime;
    int[] _prevOperEndTime;

    private int _numJobs; // pocet jobu
    private int _numMachines; // pocet pracovist

    private MedicateTable _medicateTable;

    public JspEvaluator_old()
    {
        DataStore dataStore = DataStore.getInstance();
        DataTable dataTable = dataStore.getDataTable("jobs");
        _numJobs = dataTable.getRowCount();
        _numMachines = ((Integer[]) dataTable.getTableProperty())[2].intValue();

        _firstActivityToDoIndex_InJob = new int[_numJobs];
        _firstActivityToDoIndex_OnMachine = new int[_numMachines];

        _prevOperEndTime = new int[_numJobs];
        _maxMachineTime = new int[_numMachines];

        _medicateTable = new MedicateTable();

    }

    @Override
    public double[] evaluate(Subject solution) throws Exception
    {
        try
        {
            return new double[] {};
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    public void evaluateSubject(Subject subject) //throws Exception
    {
        int geneIndex = 0;
        int jobValue = 0;

        ScheduleOperationInfo ai;
        Chromosome ch;
        //2015		Genome genome = subject.getGenome();
        // vezmu tabulku jobu		
        DataTable dt = DataStore.getInstance().getDataTable("jobs");

        int maxOper = dt.getCellCount();
        int numOper = 0;
        int operAdded = 0;

        for (int i = 0; i < _numMachines; i++)
        {
            _firstActivityToDoIndex_OnMachine[i] = 0;
            _maxMachineTime[i] = 0;
        }
        for (int i = 0; i < _numJobs; i++)
        {
            _firstActivityToDoIndex_InJob[i] = 0;
            _prevOperEndTime[i] = 0;
        }

        // dokud nenakreslim(do rozvrhu) vsechny operace, iteruju		
        while (true)
        {
            // na pracovisti zacnu od prvni nenakreslene operace			
            for (int i = 0; i < _numMachines; i++)
            {
                //2015				ch = genome.getChromosome(i);
                // pokud uz mam na pracovisti vse nakreslene, jdu na dalsi				
                //2015				if(ch.getLength() <= _firstActivityToDoIndex_OnMachine[i]) continue;

                // hodnota genu	
                //2015				jobValue = ch.getGene(_firstActivityToDoIndex_OnMachine[i]).getValue()-1;

                // aktualni prochazena operace	
                // prvni nenakreslena operace v jobu
                //2015				ai = (OperationInfo)dt.getRow(jobValue).getCell(_firstActivityToDoIndex_InJob[jobValue]).getCellProperty();

                // pokud mam nenakreslenou operaci na spravnem pracovisti, muzu ji nakreslit
                //2015				if(ai.MachineID-1 == i)
                {
                    _firstActivityToDoIndex_InJob[jobValue]++;
                    _firstActivityToDoIndex_OnMachine[i]++;
                    numOper++;
                    operAdded++;

                    if (_prevOperEndTime[jobValue] > _maxMachineTime[i])
                    {
                        //2015						_maxMachineTime[i] = _prevOperEndTime[jobValue] + ai.Length;
                    }
                    else
                    {
                        //2015						_maxMachineTime[i] += ai.Length;
                    }
                    _prevOperEndTime[jobValue] = _maxMachineTime[i];

                    //					System.out.println(numOper + "> " + ai.WorkstationID+  ": "+(jobValue+1));
                }
                geneIndex++;
            }
            if (operAdded == 0)
            {
                medicateSubject(subject);

                ////				int jobValue2 = 0,i=0;
                ////				System.out.println();
                ////				for(i=0;i<_numMachines;i++)
                ////				{
                ////					
                ////					jobValue2 = genome.getChromosome(i).getGene(_firstActivityToDoIndex_OnMachine[i]).getValue(0)-1;
                ////					 //ai = (ActivityInfo)dt.getRow(jobValue).getCell(_firstActivityToDoIndex_InJob[jobValue]).getCellProperty();
                ////					System.out.print((jobValue2+1));
                ////				}
                ////				for(i=0;i<_numMachines;i++)
                ////				{
                ////					jobValue2 = genome.getChromosome(i).getGene(_firstActivityToDoIndex_OnMachine[i]).getValue(0)-1;
                ////					if(jobValue != jobValue2) break;
                ////					/*jobValue = genome.getChromosome(i).getGene(_firstActivityToDoIndex_OnMachine[i]).getValue(0)-1;
                ////					ai = (ActivityInfo)dt.getRow(jobValue).getCell(_firstActivityToDoIndex_InJob[jobValue]).getCellProperty();
                ////					System.out.print((jobValue+1));*/
                ////				}
                ////				System.out.println();
                ////				System.out.println((jobValue+1));
                ////				System.out.println((jobValue2+1));
                ////
                ////				/*System.out.println();
                ////				for(i=0;i<_numJobs;i++)
                ////				{
                ////					jobValue2 = genome.getChromosome(i).getGene(_firstActivityToDoIndex_OnMachine[i]).getValue(0)-1;
                ////					 if(jobValue != jobValue2) break;
                ////					System.out.print(_firstActivityToDoIndex_InJob[i]);
                ////				}*/
                ////				System.out.println();
                ////				/*int index = 0,j=0;
                ////				index = i;
                ////				Gene tmpGene = subject.getGenome().getChromosome(index).getGene(_firstActivityToDoIndex_OnMachine[i]);
                ////				Gene tmpGene2=null;
                ////				for(j=0;j<subject.getGenome().getChromosome(index).getLength();j++)
                ////				{
                ////					tmpGene2 = subject.getGenome().getChromosome(index).getGene(j);
                ////					if(tmpGene2.getValue(0)-1==jobValue)
                ////                        break;						 
                ////				}
                ////				Gene tmpTmp = tmpGene;
                ////				subject.getGenome().getChromosome(index).setGene(_firstActivityToDoIndex_OnMachine[index], tmpGene2);
                ////				subject.getGenome().getChromosome(index).setGene(j, tmpTmp);
                ////				
                ////				/*_firstActivityToDoIndex_InJob[jobValue2]=_firstActivityToDoIndex_OnMachine[index];
                ////				_firstActivityToDoIndex_InJob[jobValue]=index;*/
                ////				/*_firstActivityToDoIndex_OnMachine[index]=jobValue2;*/
                //throw new Exception("Mame problem, Houstone");
            }

            operAdded = 0;
            //System.out.println();
            //geneIndex++ ;		
            if (maxOper == numOper)
                break;
        }

        //System.out.println(geneIndex);

        // ve maxMachineTime[i] mam maximalni casy na pracovistich
        // staci vybrat nejvetsi a mam MakeSpan
        int makeSpan = Integer.MIN_VALUE;
        for (int i = 0; i < _numMachines; i++)
        {
            if (_maxMachineTime[i] > makeSpan)
                makeSpan = _maxMachineTime[i];
        }

        double[] objectiveVals = { makeSpan };
        subject.setObjectiveValue(objectiveVals);
    }

    private class MedicateTable
    {
        class MedicateRow
        {
            public MedicateElement Left;
            public MedicateElement Right;
            public int Hits;

            public MedicateRow()
            {
                Left = new MedicateElement();
                Right = new MedicateElement();
                Hits = 0;
            }

            public void clear()
            {
                Left.clear();
                Right.clear();
                Hits = 0;
            }

        }

        class MedicateElement
        {
            public int NextMachineIndex;
            public int JobIndex;
            public int GeneIndex;

            public MedicateElement()
            {
                clear();
            }

            public void clear()
            {
                NextMachineIndex = -1;
                JobIndex = -1;
                GeneIndex = -1;
            }
        }

        public MedicateRow[] Rows;

        public MedicateTable()
        {
            Rows = new MedicateRow[_numMachines];
            for (int i = 0; i < _numMachines; i++)
                Rows[i] = new MedicateRow();
        }

        public void clear()
        {
            for (int i = 0; i < _numMachines; i++)
                Rows[i].clear();
        }
    }

    private void medicateSubject(Subject subject)
    {
        ScheduleOperationInfo firstActivityToDo;
        DataTable dataTable = DataStore.getInstance().getDataTable("jobs");
        //2015		Genome genome = subject.getGenome();

        int jobIndex = 0, jobIndex2 = 0;

        _medicateTable.clear();

        /*		#region Debug print
        			subject.print();
        			for(int i=0;i<_numMachines;i++)
        			{	
        				if(genome.getChromosome(i).getLength() <= _firstActivityToDoIndex_OnMachine[i])
        				{
        					System.out.print("- ");
        					continue;
        				}
        				jobIndex = subject.getGenome().getChromosome(i).getGene(_firstActivityToDoIndex_OnMachine[i]).getValue(0)-1;
        				System.out.print((jobIndex+1)+" ");
        			
        			}
        			System.out.println();
        		#endregion*/

        // najdi prvni pracoviste, na kterem jsou operace k nakresleni 
        int chromIndex = -1, entryRowIndex = -1;

        for (int i = 0; i < _numMachines; i++)
        {
            //2015			if(genome.getChromosome(i).getLength() <= _firstActivityToDoIndex_OnMachine[i]) continue;
            //2015			else 
            {
                chromIndex = i;
                break;
            }
        }

        // index jobu, ktery zpusobuje problem
        //2015		jobIndex = genome.getChromosome(chromIndex).getGene(_firstActivityToDoIndex_OnMachine[chromIndex]).getValue()-1;
        // prvni nenakreslena operace jobu "jobIndex" v tabulce "jobs"

        _medicateTable.Rows[chromIndex].Left.JobIndex = jobIndex;
        _medicateTable.Rows[chromIndex].Left.GeneIndex = _firstActivityToDoIndex_OnMachine[chromIndex];
        _medicateTable.Rows[chromIndex].Hits++;

        //2015		firstActivityToDo = (OperationInfo)dataTable.getRow(jobIndex).getCell(_firstActivityToDoIndex_InJob[jobIndex]).getCellProperty();
        //2015		_medicateTable.Rows[chromIndex].Left.NextMachineIndex = firstActivityToDo.MachineID-1;
        //2015		chromIndex = firstActivityToDo.MachineID-1;

        // index pracoviste, kam se ma skocit - pouze Left !!!

        int delkaCyklu = 1;
        while (true)
        {
            //System.out.println("jobValue: "+(jobIndex+1)+"\t chromIndex: "+chromIndex);

            /* Right*/
            _medicateTable.Rows[chromIndex].Right.JobIndex = jobIndex;
            //2015			for(int i=_firstActivityToDoIndex_OnMachine[chromIndex];i<genome.getChromosome(chromIndex).getLength();i++)
            {
                //2015				if(genome.getChromosome(chromIndex).getGene(i).getValue()==jobIndex+1)
                {
                    //2015					_medicateTable.Rows[chromIndex].Right.GeneIndex = i;

                    break;
                }
            }
            /*******/

            //2015			jobIndex = genome.getChromosome(chromIndex).getGene(_firstActivityToDoIndex_OnMachine[chromIndex]).getValue()-1;

            /* Left*/
            //2015			_medicateTable.Rows[chromIndex].Left.JobIndex = jobIndex;
            //2015			_medicateTable.Rows[chromIndex].Left.GeneIndex = _firstActivityToDoIndex_OnMachine[chromIndex];			
            /*******/

            //2015			_medicateTable.Rows[chromIndex].Hits++;

            //2015			if(_medicateTable.Rows[chromIndex].Hits > 1)
            //			{
            //				entryRowIndex = chromIndex;
            //				break;
            //			}

            //2015			delkaCyklu++;

            //2015			firstActivityToDo = (OperationInfo)dataTable.getRow(jobIndex).getCell(_firstActivityToDoIndex_InJob[jobIndex]).getCellProperty();
            //2015			_medicateTable.Rows[chromIndex].Left.NextMachineIndex = firstActivityToDo.MachineID-1;
            //2015			chromIndex = firstActivityToDo.MachineID-1;

            ////			subject.getGenome().getChromosome(chromIndex).swapGenes(table.Rows[chromIndex].Left.GeneIndex, table.Rows[chromIndex].Right.GeneIndex);
            ////			break;
        }

        //		System.out.println("OUT> delka cyklu: " + delkaCyklu);
        //		System.out.println("OUT> entryRowIndex: " + entryRowIndex);
        //		//System.out.println(table.Rows[entryRowIndex].Left.JobIndex);
        //
        int nextIndex = 0;// 2016 _medicateTable.Rows[entryRowIndex].Left.NextMachineIndex;

        //System.out.print(entryRowIndex + ", ");
        while (entryRowIndex != nextIndex)
        {
            //			System.out.print(nextIndex + ", ");
            //2015			subject.getGenome().getChromosome(nextIndex).swapGenes(_medicateTable.Rows[nextIndex].Left.GeneIndex, _medicateTable.Rows[nextIndex].Right.GeneIndex);
            nextIndex = _medicateTable.Rows[nextIndex].Left.NextMachineIndex;

        }
        //		System.out.println();
    }

}
