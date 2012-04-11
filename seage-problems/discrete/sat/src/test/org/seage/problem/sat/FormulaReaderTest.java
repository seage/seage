/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.sat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author RMalek
 */
public class FormulaReaderTest {

    public FormulaReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of readClauses method, of class FormulaReader.
     */
    @Test
    public void testReadClauses() throws Exception 
    {
        Formula formula = FormulaReader.readFormula("data/uf20/uf20-01.cnf");

        assertNotNull(formula);
//        assertNotNull(formula.getLiterals());
//        assertNotNull(clauses[0].getLiterals()[0].getAbsValue());
    }

}