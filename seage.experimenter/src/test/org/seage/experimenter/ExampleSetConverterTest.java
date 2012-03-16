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

import com.rapidminer.example.ExampleSet;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.seage.data.DataNode;

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
        
        ExampleSet exampleSet = null;        
        DataNode expResult = null;
        
        DataNode result = ExampleSetConverter.convertToDataNode(exampleSet);
        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of convertToExampleSet method, of class ExampleSetConverter.
     */
    @Test
    public void testConvertToExampleSet() throws Exception
    {
        System.out.println("convertToExampleSet");
        
        DataNode dataNode = null;
        ExampleSet expResult = null;
        
        ExampleSet result = ExampleSetConverter.convertToExampleSet(dataNode);
        
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
