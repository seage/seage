package org.seage.experimenter.reporting.rapidminer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.rapidminer.RapidMiner;
import com.rapidminer.repository.Repository;
import com.rapidminer.repository.RepositoryException;
import com.rapidminer.repository.RepositoryManager;
import com.rapidminer.repository.local.LocalRepository;
import com.rapidminer.tools.jdbc.JDBCProperties;
import com.rapidminer.tools.jdbc.connection.DatabaseConnectionService;
import com.rapidminer.tools.jdbc.connection.FieldConnectionEntry;

public class RapidMinerManager
{
    private static Logger _logger = Logger.getLogger(RapidMinerManager.class.getName());
    public static final String RAPIDMINER_LOCAL_REPOSITORY_NAME = "seage";
    public static final String RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH = "repository";

    private static boolean _rapidminerInitialized = false;
    private static boolean _repositoryInitialized = false;
    private static boolean _databaseConnectionInitialized = false;

    public static void init()
    {
        if (_rapidminerInitialized)
            return;
        System.setProperty("rapidminer.home", ".");
        RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
        RapidMiner.init();
        _rapidminerInitialized = true;
    }

    public static void initRepository() throws RepositoryException
    {
        if (_repositoryInitialized)
            return;

        _logger.fine("initRepository");
        RepositoryManager rm = RepositoryManager.getInstance(null);

        List<Repository> reposToRemove = new ArrayList<Repository>();

        for (Repository repo : rm.getRepositories())
        {
            if (!(repo instanceof LocalRepository))
                continue;
            LocalRepository r = (LocalRepository) repo;
            if (r.getName().equals(RAPIDMINER_LOCAL_REPOSITORY_NAME))
            {
                if (!r.getFile().getPath().equals(RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH))
                    reposToRemove.add(r);

            }
        }

        for (Repository repo : reposToRemove)
            rm.removeRepository(repo);

        try
        {
            rm.getRepository(RAPIDMINER_LOCAL_REPOSITORY_NAME);
        }
        catch (RepositoryException re)
        {
            rm.addRepository(new LocalRepository(RAPIDMINER_LOCAL_REPOSITORY_NAME,
                    new File(RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH)));
        }

        _logger.fine("initRepository: saving");
        rm.save();
        _logger.fine("initRepository: done");

        _repositoryInitialized = true;
    }

    public static void initDatabaseConnection() throws RepositoryException
    {
        if (_databaseConnectionInitialized)
            return;

        List<FieldConnectionEntry> entriesToDelete = new ArrayList<FieldConnectionEntry>();
        for (FieldConnectionEntry ce : DatabaseConnectionService.getConnectionEntries())
        {
            if (ce.getName().equals("SEAGE_DB"))
                entriesToDelete.add(ce);

        }
        for (FieldConnectionEntry ce : entriesToDelete)
            DatabaseConnectionService.deleteConnectionEntry(ce);

        JDBCProperties prop = new JDBCProperties(true);
        prop.setName("H2");
        prop.setUrlPrefix("jdbc:h2:");

        DatabaseConnectionService.addConnectionEntry(new FieldConnectionEntry("SEAGE_DB", prop,
                new File(".").getAbsolutePath() + "/database", "", "seage", "sa", new char[] { 's', 'a' }));

        _databaseConnectionInitialized = true;
    }

}
