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
import com.rapidminer.example.set.SimpleExampleSet;
import com.rapidminer.example.table.*;
import com.rapidminer.tools.Ontology;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;

/**
 *
 * @author zmatlja1
 */
public class ExampleSetConverter {
    
    private static final String ROOT_NODE = "ExampleSet";
    private static final String ATTRIBUTES_NODE = "Attributes";
    private static final String EXAMPLES_NODE = "Examples";
    
    /**
     * Method performs a conversion from ExampleSet to DataNode.
     * @param ExampleSet exampleSet is a data structure from RapidMiner API
     * @return DataNode
     */
    public static DataNode convertToDataNode(ExampleSet exampleSet, String file) throws Exception
    {
        DataNode exampleSetNode = new DataNode( ROOT_NODE );
        DataNode attributesNode = new DataNode( ATTRIBUTES_NODE );
        DataNode examplesNode   = new DataNode( EXAMPLES_NODE );

        // Extracting all of attributes from ExampleSet
        Attributes attributes = exampleSet.getAttributes();
        Iterator<Attribute> attributeIterator = attributes.allAttributes();
        while( attributeIterator.hasNext() )
        {
            Attribute attribute = attributeIterator.next();
            
            DataNode attributeNode = new DataNode("Attribute");
            attributeNode.putValue("name", attribute.getName());
            attributeNode.putValue("isnominal", attribute.isNominal());

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
        
        XmlHelper.writeXml(exampleSetNode, "C:\\wamp\\"+file);
        
        return exampleSetNode;
    }
    
    /**
     * Method performs a conversion from DataNode to ExampleSet.
     * @param DataNode dataNode is a internal data structure from SEAGE framework
     * @return ExampleSet
     */
    public static ExampleSet convertToExampleSet(DataNode dataNode) throws Exception
    {        
        DataNode attributesNode = dataNode.getDataNode( ATTRIBUTES_NODE );
        
        List<Attribute> attributes = new ArrayList<Attribute>();

        for(DataNode attribute : attributesNode.getDataNodes())
        {
            Attribute concreteAttribute; 
            
            if( attribute.getValueBool( "isnominal" ) )
                concreteAttribute = AttributeFactory.createAttribute(attribute.getValueStr("name"), Ontology.NOMINAL);
            else
                concreteAttribute = AttributeFactory.createAttribute(attribute.getValueStr("name"), Ontology.REAL);
            
            attributes.add( concreteAttribute );
        }
        
        DataNode examplesNode = dataNode.getDataNode( EXAMPLES_NODE );
        int numOfExamples = examplesNode.getDataNodes().size();
        
        MemoryExampleTable memoryExampleTable = new MemoryExampleTable( attributes );
        
        for(int i = 0; i < numOfExamples; i++)
        {
            DataNode exampleNode = examplesNode.getDataNode("Example", i);

            double data[] = new double[ exampleNode.getDataNodes().size() ];
            for (int j = 0; j < exampleNode.getDataNodes().size(); j++)
            {                
                DataNode resultNode = exampleNode.getDataNode("Result", j);
                if( attributes.get(j).isNominal() )
                    data[j] = attributes.get(j).getMapping().mapString( resultNode.getValueStr("value") );
                else
                    data[j] = resultNode.getValueDouble("value");
            }
            
            memoryExampleTable.addDataRow( new DoubleArrayDataRow( data ) );
        }
        
        return memoryExampleTable.createExampleSet();
    }
    
}
