/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.experimenter;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.DataRow;
import com.rapidminer.example.table.DataRowReader;
import com.rapidminer.example.table.ExampleTable;
import java.util.Iterator;
import org.seage.data.DataNode;

/**
 *
 * @author zmatlja1
 */
public class ExampleSetConverter {
    
    /**
     * Method performs a conversion from ExampleSet to DataNode.
     * @param ExampleSet exampleSet is a data structure from RapidMiner API
     * @return DataNode
     */
    public static DataNode convertToDataNode(ExampleSet exampleSet) throws Exception
    {
        DataNode exampleSetNode = new DataNode("ExampleSet");
        DataNode attributesNode = new DataNode("Attributes");        
        DataNode examplesNode = new DataNode("Examples");

        // Extracting all of attributes from ExampleSet
        Attributes attributes = exampleSet.getAttributes();
        Iterator<Attribute> attributeIterator = attributes.allAttributes();
        while( attributeIterator.hasNext() )
        {
            Attribute attribute = attributeIterator.next();
            
            DataNode attributeNode = new DataNode("Attribute");
            attributeNode.putValue("name", attribute.getName());
            attributesNode.putDataNode( attributeNode );
        }
               
        ExampleTable exampleTable = exampleSet.getExampleTable();
        DataRowReader dataRowReader = exampleTable.getDataRowReader();
        
        // Iteration over all examples and over all attributes
        while( dataRowReader.hasNext() )
        {
            DataRow dr = dataRowReader.next();
            
            DataNode exampleNode = new DataNode("Example");            
            
            attributeIterator = attributes.allAttributes(); 
            
            while( attributeIterator.hasNext() )
            {
                Attribute attribute = attributeIterator.next();
                
                DataNode resultNode = new DataNode("Result");
                
                // According to a javadoc of method get( attribute ), the method returns Double.NaN if a value is not a number (i.e. is null)
                // But this condition is not true, you can see on the line under this
                // if(dr.get( attribute ) == Double.NaN) System.out.println("Never reach");
                
                // If i retype a value to the String, its working fine
                if(String.valueOf( dr.get( attribute ) ).equals("NaN") ) continue;
                
                if( attribute.isNominal() )
                    resultNode.putValue("value", attribute.getMapping().mapIndex( (int)dr.get( attribute ) ) );
                else
                    resultNode.putValue("value", dr.get( attribute ) );
                
                exampleNode.putDataNode( resultNode );
            }
            
            examplesNode.putDataNode( exampleNode );
        }
        
        exampleSetNode.putDataNode( attributesNode );
        exampleSetNode.putDataNode( examplesNode );
        
        return exampleSetNode;
    }
    
    /**
     * Method performs a conversion from DataNode to ExampleSet.
     * @param DataNode dataNode is a internal data structure from SEAGE framework
     * @return ExampleSet
     */
    public static ExampleSet convertToExampleSet(DataNode dataNode)
    {
        return null;
    }
    
}
