package org.seage.launcher.commands;

import org.seage.experimenter.reporting.h2.ExperimentDataH2Importer;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Process experiment output data and add records to db")
public class ReportCommand extends Command 
{
    @Parameter(names = "--clean", description = "Drop all db data, create all tables and fill them by new data")
    private boolean clean;
    
    @Override
    public void performCommad() throws Exception 
    {
        new ExperimentDataH2Importer("output/experiment-logs", "database/seage", clean).processLogs();
    }
}

