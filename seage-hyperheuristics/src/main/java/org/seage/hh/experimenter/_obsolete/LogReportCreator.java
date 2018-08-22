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
 *     Jan Zmatlik
 *     - Modified 
 */
package org.seage.hh.experimenter._obsolete;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Arrays;

import javax.xml.transform.stream.StreamResult;

import org.seage.data.file.FileHelper;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Richard Malek
 */
public class LogReportCreator implements ILogReport
{

    private static final String LOG_PATH = "output";
    private static final String REPORT_PATH = "report";
    private static final String XSL_TEMPLATE = "report2csv.xsl";
    private static final String OUTPUT_CSV_FILE = "report.csv";
    //private static final String CSV_HEADER          = "ExperimentID;ProblemID;AlgorithmID;InstanceID;ConfigID;InitSolutionValue;SolutionValue;Time;";

    private String _experimentID;

    public LogReportCreator()
    {
    }

    @Override
    public void report() throws Exception
    {
        createReport();
    }

    public void report(String experimentID) throws Exception
    {
        _experimentID = experimentID;
        createReport();
    }

    private void createReport() throws Exception
    {
        LoggerFactory.getLogger(ProcessPerformer.class.getName()).info( "Creating reports ...");
        LoggerFactory.getLogger(ProcessPerformer.class.getName()).debug("Creating reports ...");

        File logDir = new File(LOG_PATH);

        File reportDir = new File(REPORT_PATH);

        if (reportDir.exists())
            FileHelper.deleteDirectory(reportDir);

        reportDir.mkdirs();

        File output = new File(reportDir.getPath() + "/" + OUTPUT_CSV_FILE);
        output.createNewFile();

        StreamResult outputStream = new StreamResult(new FileOutputStream(output));

        FilenameFilter filter = new FilenameFilter()
        {
            @Override
            public boolean accept(File arg0, String arg1)
            {
                if (arg0.isDirectory())
                {
                    if (_experimentID == null)
                        return true;
                    else
                    {
                        if (!arg1.equals(_experimentID))
                            return false;
                        else
                            return true;
                    }
                }
                else
                    return false;
            }
        };

        // TODO: B - Number of names of parameters is fixed, for future will be good to create a floating number of names of parameters 

        int numberOfParameters = 10;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < numberOfParameters + 1; ++i)
        {
            sb.append("p").append(i).append(";");
        }

        //outputStream.getOutputStream().write( ( CSV_HEADER + sb.append("\n").toString() ).getBytes() );

        for (String dirName : logDir.list(filter))
        {
            System.err.println(dirName);

            File logDirDir = new File(logDir.getPath() + "/" + dirName);
            Arrays.sort(logDirDir.list());

            for (String xmlFileName : logDirDir.list())
            {
                String xmlPath = logDir.getPath() + "/" + dirName + "/" + xmlFileName;

                try
                {
                    Transformer.getInstance().transformByXSLT(xmlPath, XSL_TEMPLATE, outputStream);
                }
                catch (Exception e)
                {
                    LoggerFactory.getLogger(ProcessPerformer.class.getName()).debug(xmlPath);
                }
            }
        }
    }
}
