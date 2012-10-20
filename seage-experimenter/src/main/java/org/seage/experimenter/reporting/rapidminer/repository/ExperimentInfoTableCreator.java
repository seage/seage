package org.seage.experimenter.reporting.rapidminer.repository;

import java.util.ArrayList;
import java.util.Hashtable;

import org.seage.data.xml.XmlHelper;
import org.w3c.dom.Document;

import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DataRow;
import com.rapidminer.tools.Ontology;

public class ExperimentInfoTableCreator extends RMDataTableCreator
{

    public ExperimentInfoTableCreator(String repositoryPath, String tableName)
    {
        super(repositoryPath, tableName);

    }

    @Override
    public Boolean isInvolved(Document doc)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void processDocument(Document doc)
    {

        
    }

}
