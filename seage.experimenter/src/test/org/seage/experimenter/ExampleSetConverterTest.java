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
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.*;
import org.junit.Test;
import static org.junit.Assert.*;
import org.seage.data.DataNode;
import com.rapidminer.tools.Ontology;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author zmatlja1
 */
public class ExampleSetConverterTest {
    
    public ExampleSetConverterTest() { }

    /**
     * Test of convertToDataNode method, of class ExampleSetConverter.
     */
    @Test
    public void testConvertToDataNode() throws Exception
    {
        System.out.println("convertToDataNode");
        
        /**
         * strom1 -> DataNode -> ExampleSet -> strom2, strom1==strom2
         * 
         * Modification of the testflow:
         * 
         * Create ExampleSet (Create Attributes and DataTable) -> DataNode -> ExampleSet -> Equal Attributes and DataTables between first and last ExampleSet
         * 
         */
        
        List<Attribute> attributes = new ArrayList<Attribute>();
        
        attributes.add( AttributeFactory.createAttribute("ProblemID", Ontology.NOMINAL) );
        attributes.add( AttributeFactory.createAttribute("AlgorithmID", Ontology.NOMINAL) );
        attributes.add( AttributeFactory.createAttribute("ConfigID", Ontology.NOMINAL) );
        attributes.add( AttributeFactory.createAttribute("SolutionValue", Ontology.REAL) );
        attributes.add( AttributeFactory.createAttribute("p1", Ontology.REAL) );
        
        MemoryExampleTable memoryExampleTable = new MemoryExampleTable( attributes );
        
        int numOfExamples = 5;
        
        String[][] dataAsString = 
        {
            {"TSP","GeneticAlgorithm","97731cc3ee4419c15f323f992ba1c016","9123.651348024126","6.847248066794909"},
            {"TSP","GeneticAlgorithm","295d7610c2f557f75cf1714398aec8c2","8321.468203306158","3.8606775449647635"},
            {"TSP","TabuSearch","2bf9ae7a96b22b5919533fcbd1f5e98a","24896.684531658222","1.0"},
            {"TSP","SimulatedAnnealing","fea1c75ba3571e2e1b10e52c951a733a","659.904033340102","0.10190304207985498"},
            {"TSP","SimulatedAnnealing","e9a8f9a8164bb7bf8894c22660c751b2","32878.673827639104","0.19098335600058403"},
        };        
        
        for(int i = 0; i < numOfExamples; i++)
        {
            double data[] = new double[ attributes.size() ];
            for(int j = 0; j < attributes.size(); j++)
            {
                if( attributes.get(j).isNominal() )
                    data[j] = attributes.get(j).getMapping().mapString( dataAsString[i][j] );
                else
                    data[j] = Double.valueOf( dataAsString[i][j] );
            }
            memoryExampleTable.addDataRow( new DoubleArrayDataRow( data ) );
        }

        ExampleSet exampleSet = memoryExampleTable.createExampleSet();        
        
        // ExampleSet -> DataNode
        DataNode dataNode = ExampleSetConverter.convertToDataNode( exampleSet );
        
        // DataNode -> ExampleSet
        ExampleSet convertedSet = ExampleSetConverter.convertToExampleSet( dataNode );
        
        assertEquals(convertedSet.getAttributes().size(), exampleSet.getAttributes().size());

        MemoryExampleTable convertedMemoryExampleTable = (MemoryExampleTable)convertedSet.getExampleTable();
        
        for(int i = 0; i < convertedMemoryExampleTable.getAttributeCount(); i++)
        {
            // Comparison of attributes names and data types
            assertEquals(convertedMemoryExampleTable.getAttribute(i).getName(), exampleSet.getExampleTable().getAttribute(i).getName());            
            assertEquals(convertedMemoryExampleTable.getAttribute(i).isNominal(), exampleSet.getExampleTable().getAttribute(i).isNominal());
        }
        
        DataRowReader dataRowReader = convertedMemoryExampleTable.getDataRowReader();
        Iterator<Attribute> attributeIterator = convertedSet.getAttributes().allAttributes();
        
        String[][] convertedDataAsString = new String[numOfExamples][attributes.size()];
        
        
        // Collecting data from converted ExampleSet
        int i = 0, j = 0;
        while( dataRowReader.hasNext() )
        {
            DataRow dr = dataRowReader.next();

            while( attributeIterator.hasNext() )
            {
                Attribute attribute = attributeIterator.next();
                
                if( attribute.isNominal() )
                    convertedDataAsString[i][j] = attribute.getMapping().mapIndex( (int)dr.get( attribute ) );
                else
                    convertedDataAsString[i][j] = String.valueOf( dr.get( attribute ) );
                ++j;
            }
            ++i;
        }
        
        // Data comparison of ExampleSets
        for(int k = 0; k < dataAsString.length; k++)
        {
            for(int l = 0; l < dataAsString[k].length; l++)
            {
                assertEquals(dataAsString[k][l], convertedDataAsString[k][l]);
            }
        } 
    }

    /**
     * Test of convertToExampleSet method, of class ExampleSetConverter.
     */
    @Test
    public void testConvertToExampleSet() throws Exception
    {
        System.out.println("convertToExampleSet");
        
        /**
         * strom1 -> ExampleSet -> DataNode -> strom2, strom1==strom2
         */
        
        DataNode dataNode = null;
        ExampleSet expResult = null;
        
        ExampleSet result = ExampleSetConverter.convertToExampleSet(dataNode);
        
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
