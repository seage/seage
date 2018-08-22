package org.seage.hh.knowledgebase.rapidminer.repository;

import org.seage.hh.knowledgebase.importing.IDocumentProcessor;
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
    public void processDocument(Document doc, String containerFileName) throws Exception
    {

    }

}
