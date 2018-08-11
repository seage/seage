package org.seage.knowledgebase.importing.db;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.seage.knowledgebase.importing.IDocumentProcessor;
import org.seage.knowledgebase.importing.ProcessExperimentZipFileTask;
import org.seage.knowledgebase.importing.db.tablecreator.AlgorithmParamsTableCreator;
import org.seage.knowledgebase.importing.db.tablecreator.ExperimentTasksTableCreator;
import org.seage.knowledgebase.importing.db.tablecreator.ExperimentsTableCreator;
import org.seage.knowledgebase.importing.db.tablecreator.DataTableCreator;
import org.seage.thread.TaskRunner3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExperimentDataDBImporter
{
    private static Logger _logger = LoggerFactory.getLogger(ExperimentDataDBImporter.class.getName());
    private String _logPath;
    private String _dbPath;
    private ExperimentsTableCreator _expperimentsTableCreator;
    private List<DataTableCreator> _dataTableCreators;

    public ExperimentDataDBImporter(String logPath, String dbPath, boolean clean) throws Exception
    {
        _logPath = logPath;
        _dbPath = dbPath;

        _expperimentsTableCreator = new ExperimentsTableCreator(_logPath, _dbPath, clean);

        _dataTableCreators = new ArrayList<DataTableCreator>();
        _dataTableCreators.add(_expperimentsTableCreator);
        _dataTableCreators.add(new ExperimentTasksTableCreator(dbPath));
        _dataTableCreators.add(new AlgorithmParamsTableCreator.GeneticAlgorithm(dbPath));
        _dataTableCreators.add(new AlgorithmParamsTableCreator.TabuSearch(dbPath));
        _dataTableCreators.add(new AlgorithmParamsTableCreator.SimulatedAnnealing(dbPath));
        _dataTableCreators.add(new AlgorithmParamsTableCreator.AntColony(dbPath));
    }

    public void processLogs() throws Exception
    {
        _logger.info("Processing experiment logs ...");
        long t0 = System.currentTimeMillis();

        try
        {
            HashSet<String> ids = _expperimentsTableCreator.getExperimentIDs();
            createAndRunProcessExperimentZipFileTasks(ids);
            //expIDsTableCreator.insertNewExperiments(ids);
        }
        finally
        {
            for (DataTableCreator tc : _dataTableCreators)
                tc.close();
        }
        long t1 = (System.currentTimeMillis() - t0) / 1000;
        _logger.info("Time:  " + t1 + "s");

    }

    @SuppressWarnings("unchecked")
    private void createAndRunProcessExperimentZipFileTasks(HashSet<String> ids) throws Exception
    {
        File logDir = new File(_logPath);
        List<Runnable> tasks = new ArrayList<Runnable>();
        File[] files = logDir.listFiles(new ZipFileFilter(ids));
        int fileCount = files == null ? 0 : files.length;
        try
        {
            for (int i=0;i<fileCount;i++)
            {                
                tasks.add(new ProcessExperimentZipFileTask((List<IDocumentProcessor>) (List<?>) _dataTableCreators, files[i]));                
            }
            TaskRunner3.run(tasks.toArray(new Runnable[] {}), /*2*/Runtime.getRuntime().availableProcessors());
        }
        catch (Exception ex)
        {
            _logger.error( ex.getMessage(), ex);
        }
        if(fileCount == 0)
            _logger.info("No zip files found to process");
        else
            _logger.info("Files processed: " + fileCount);
    }

    private class ZipFileFilter implements FilenameFilter
    {
        private HashSet<String> _ids;

        public ZipFileFilter(HashSet<String> ids)
        {
            _ids = ids;
        }

        @Override
        public boolean accept(File arg0, String filename)
        {
            if (!filename.endsWith(".zip"))
                return false;
            String id = filename.split("-")[0];
            if (id == null)
                return false;
            if (_ids.contains(id))
                return true;
            return false;
        }

    }

}
