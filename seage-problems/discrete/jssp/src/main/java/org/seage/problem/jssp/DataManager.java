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
package org.seage.problem.jssp;

import java.util.Random;

import org.seage.data.ds.DataTable;

/**
 * Summary description for DataReader.
 */
public class DataManager
{
    private int _numOper;
    private Random _random;

    public DataManager()
    {
        _random = new Random(321123);
    }

    public void initDataStore(String path) throws Exception
    {
        try
        {
            //			DataStore dataStore = DataStore.getInstance();
            //			dataStore.insertDataTable("jobs", getJobDataTable(path));
            //			dataStore.insertDataTable("solutions", getSolutionDataTable());

        }
        catch (Exception ex)
        {
            throw ex;
        }

    }

    private DataTable getJobDataTable(String path) throws Exception
    {
        try
        {
            //			XmlReader reader = new XmlReaderImpl();
            //			XmlElement rootElem = reader.readXml(path);
            //			ArrayList jobList = rootElem.getElementList("job");
            //			HashMap machines = new HashMap();					// potrebuju zjistit pocet stroju
            //			int operCount = 0;
            //
            DataTable result = new DataTable();
            //			result.initDataRows(jobList.size());
            //
            //			for (int i = 0; i < jobList.size(); i++)
            //			{
            //				XmlElement jobElem = (XmlElement)jobList.get(i);
            //				ArrayList operList = jobElem.getElementList("operation");
            //				result.getRow(i).initDataCells(operList.size());
            //
            //				JobInfo jobInfo = new JobInfo(
            //							Integer.parseInt(jobElem.getAttribute("id").getValue()),
            //							Integer.parseInt(jobElem.getAttribute("priority").getValue())
            //							);
            //
            //				result.getRow(i).setRowProperty(jobInfo);
            //				
            //				for (int j = 0; j < operList.size(); j++)
            //				{
            //					XmlElement operElem = (XmlElement)operList.get(j);
            //
            //					OperationInfo operInfo = new OperationInfo();
            //					operInfo.JobID = jobInfo.getID();
            //					operInfo.OperationID = Integer.parseInt(operElem.getAttribute("number").getValue());
            //					operInfo.MachineID = Integer.parseInt(operElem.getAttribute("machineID").getValue());
            //					operInfo.Length = Integer.parseInt(operElem.getAttribute("length").getValue());
            //
            //					result.getRow(i).getCell(j).setCellProperty(operInfo);
            //
            //					Integer machineID = new Integer(operInfo.MachineID);
            //					if(!machines.containsKey(machineID))
            //						machines.put(machineID, machineID);
            //					
            //					operCount++;
            //				}				
            //			}
            //
            //			/////
            //			result.setTableProperty(
            //				new Integer[] { 
            //					new Integer(jobList.size()), 
            //					new Integer(operCount), 
            //					new Integer(machines.size())});
            //
            return result;
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    public DataTable getSolutionDataTable() throws Exception
    {
        try
        {
            //			DataTable jobTable = DataStore.getInstance().getDataTable("jobs");
            //			ArrayList operations = new ArrayList();
            //			
            //			for(int i=0;i<jobTable.getRowCount();i++)
            //			{					
            //				for(int j=0;j<jobTable.getRow(i).getCellCount();j++)
            //				{
            //					OperationInfo operInfo = (OperationInfo)jobTable.getRow(i).getCell(j).getCellProperty();
            //					operations.add(new Integer(operInfo.JobID));
            //				}
            //			}
            //
            DataTable result = new DataTable();
            //			result.initDataRows(1);
            //			result.getRow(0).initDataCells(operations.size());
            //
            //			for (int i = 0; i < operations.size(); i++)
            //			{
            //				result.getRow(0).getCell(i).setCellProperty(operations.get(i));
            //			}
            //			
            //			int[] initSol = new int[operations.size()];
            //			for (int i = 0; i < 10*operations.size(); i++)
            //			{
            //				int ix1 = _random.nextInt(operations.size());
            //				int ix2 = _random.nextInt(operations.size());
            //				Object tmp1 = result.getRow(0).getCell(ix1).getCellProperty();
            //				Object tmp2 = result.getRow(0).getCell(ix2).getCellProperty();
            //				result.getRow(0).getCell(ix1).setCellProperty(tmp2);
            //				result.getRow(0).getCell(ix2).setCellProperty(tmp1);
            //			}
            //			for (int i = 0; i < operations.size(); i++)
            //			{
            //				initSol[i] = ((Integer)result.getRow(0).getCell(i).getCellProperty()).intValue();
            //			}
            //			System.out.print(hashCode(initSol)+"\t");
            //			_numOper = operations.size();

            return result;
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    private int hashCode(int[] values)
    {
        int hash = 0x1000;
        for (int i = 0; i < values.length; i++)
        {
            int key = values[i];
            hash = ((hash << 5) ^ (hash >> 27)) ^ (key + 2) << 1;
        }
        return hash;
    }

    public int getNumOper()
    {
        return _numOper;
    }
}
