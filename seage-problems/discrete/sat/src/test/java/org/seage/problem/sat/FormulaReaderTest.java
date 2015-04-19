/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.problem.sat;

import java.io.FileInputStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;

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
    	ProblemInstanceInfo pii = new ProblemInstanceInfo("uf20", ProblemInstanceOrigin.RESOURCE, "/org/seage/problem/sat/instances/uf20-01.cnf");
    	SatProblemProvider spp = new SatProblemProvider();
        Formula formula = spp.initProblemInstance(pii);
        assertNotNull(formula);
        assertEquals(20, formula.getLiterals().size());
        assertEquals(91, formula.getClauses().length);
        
        // 1st clause
        assertEquals(3, formula.getClauses()[0].getLiterals()[0].getIndex());
        assertEquals(false, formula.getClauses()[0].getLiterals()[0].isNeg());
        assertEquals(17, formula.getClauses()[0].getLiterals()[1].getIndex());
        assertEquals(true, formula.getClauses()[0].getLiterals()[1].isNeg());
        assertEquals(18, formula.getClauses()[0].getLiterals()[2].getIndex());
        assertEquals(false, formula.getClauses()[0].getLiterals()[2].isNeg());
        
        // 45th clause
        assertEquals(13, formula.getClauses()[45].getLiterals()[0].getIndex());
        assertEquals(false, formula.getClauses()[45].getLiterals()[0].isNeg());
        assertEquals(6, formula.getClauses()[45].getLiterals()[1].getIndex());
        assertEquals(false, formula.getClauses()[45].getLiterals()[1].isNeg());
        assertEquals(9, formula.getClauses()[45].getLiterals()[2].getIndex());
        assertEquals(false, formula.getClauses()[45].getLiterals()[2].isNeg());
        
        // the last clause
        assertEquals(3, formula.getClauses()[90].getLiterals()[0].getIndex());
        assertEquals(false, formula.getClauses()[90].getLiterals()[0].isNeg());
        assertEquals(15, formula.getClauses()[90].getLiterals()[1].getIndex());
        assertEquals(true, formula.getClauses()[90].getLiterals()[1].isNeg());
        assertEquals(4, formula.getClauses()[90].getLiterals()[2].getIndex());
        assertEquals(true, formula.getClauses()[90].getLiterals()[2].isNeg());
    }

}
