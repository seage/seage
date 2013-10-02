package org.seage.experimenter.reporting.rapidminer.repository;

import org.seage.experimenter.reporting.IDocumentProcessor;
import org.w3c.dom.Document;

public class ExperimentInfoTableCreator extends RMDataTableCreator implements IDocumentProcessor
{

    public ExperimentInfoTableCreator(String repositoryPath, String tableName)
    {
        super(repositoryPath, tableName);

    }

    @Override
    public boolean isInvolved(Document doc)
    {
        return true;
    }

    @Override
    public void processDocument(Document doc) throws Exception
    {

        
    }

}
