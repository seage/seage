package org.seage.knowledgebase.rapidminer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.seage.knowledgebase.importing.IDocumentProcessor;
import org.seage.knowledgebase.importing.ProcessExperimentZipFileTask;
import org.seage.knowledgebase.rapidminer.repository.AlgorithmParamsTableCreator;
import org.seage.knowledgebase.rapidminer.repository.RMDataTableCreator;
import org.seage.knowledgebase.rapidminer.repository.SingleAlgorithmTableCreator;
import org.seage.thread.TaskRunner3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rapidminer.Process;
import com.rapidminer.example.table.ListDataRowReader;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorCreationException;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.io.RepositoryStorer;
import com.rapidminer.repository.RepositoryException;
import com.rapidminer.tools.OperatorService;

public class ExperimentDataRapidMinerImporter
{
    private static Logger _logger = LoggerFactory.getLogger(ExperimentDataRapidMinerImporter.class.getName());
    private String _logPath;

    private List<RMDataTableCreator> _rmDataTableCreators;

    public ExperimentDataRapidMinerImporter(String logPath, String repoName) throws RepositoryException
    {
        _logPath = logPath;
        _rmDataTableCreators = new ArrayList<RMDataTableCreator>();
        _rmDataTableCreators.add(new SingleAlgorithmTableCreator("/databases/experiments", "ExperimentValues"));
        _rmDataTableCreators.add(new AlgorithmParamsTableCreator.GeneticAlgorithm("/databases/experiments/parameters"));
        _rmDataTableCreators.add(new AlgorithmParamsTableCreator.TabuSearch("/databases/experiments/parameters"));
        _rmDataTableCreators.add(new AlgorithmParamsTableCreator.AntColony("/databases/experiments/parameters"));
        _rmDataTableCreators
                .add(new AlgorithmParamsTableCreator.SimulatedAnnealing("/databases/experiments/parameters"));
    }

    @SuppressWarnings("unchecked")
    public void processLogs() throws OperatorException, OperatorCreationException, RepositoryException
    {
        _logger.info("Processing experiment logs ...");

        long t0 = System.currentTimeMillis();

        File logDir = new File(_logPath);
        List<Runnable> tasks = new ArrayList<Runnable>();

        try
        {
            for (File f : logDir.listFiles())
            {
                if (!f.getName().endsWith(".zip"))
                    continue;

                tasks.add(
                        new ProcessExperimentZipFileTask((List<IDocumentProcessor>) (List<?>) _rmDataTableCreators, f));
            }
            
            TaskRunner3.run(tasks.toArray(new Runnable[] {}), Runtime.getRuntime().availableProcessors());
        }
        catch (Exception ex)
        {
            _logger.error( ex.getMessage());
        }

        writeDataTablesToRepository();

        long t1 = (System.currentTimeMillis() - t0) / 1000;

        _logger.info("Processing experiment logs DONE - " + t1 + "s");
    }

    private void writeDataTablesToRepository() throws OperatorException, OperatorCreationException, RepositoryException
    {
        RapidMinerManager.init();
        RapidMinerManager.initRepository();

        for (RMDataTableCreator table : _rmDataTableCreators)
        {
            MemoryExampleTable memoryTable = new MemoryExampleTable(table.getAttributes());

            /* Save model */
            Operator modelWriter = OperatorService
                    .createOperator(RepositoryStorer.class);
            modelWriter.setParameter(
                    RepositoryStorer.PARAMETER_REPOSITORY_ENTRY,
                    "//" + RapidMinerManager.RAPIDMINER_LOCAL_REPOSITORY_NAME + table.getRepositoryPath() + "/"
                            + table.getTableName());

            Process process = new Process();

            process.getRootOperator().getSubprocess(0).addOperator(modelWriter);
            process.getRootOperator()
                    .getSubprocess(0)
                    .getInnerSources()
                    .getPortByIndex(0)
                    .connectTo(
                            modelWriter.getInputPorts().getPortByName("input"));

            memoryTable.readExamples(new ListDataRowReader(table.getDataTable().iterator()));

            process.run(new IOContainer(memoryTable.createExampleSet()));
        }
    }
}
