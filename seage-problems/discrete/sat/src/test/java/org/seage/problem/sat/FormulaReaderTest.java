/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.problem.sat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;

/**
 *
 * @author RMalek
 */
public class FormulaReaderTest
{

    public FormulaReaderTest()
    {
    }

    @BeforeAll
    public static void setUpClass() throws Exception
    {
    }

    @AfterAll
    public static void tearDownClass() throws Exception
    {
    }

    /**
     * Test of readClauses method, of class FormulaReader.
     */
    @Test
    public void testReadClauses() throws Exception
    {
        ProblemInstanceInfo pii = new ProblemInstanceInfo("uf20", ProblemInstanceOrigin.RESOURCE,
                "/org/seage/problem/sat/instances/uf20-01.cnf");
        SatProblemProvider spp = new SatProblemProvider();
        Formula formula = spp.initProblemInstance(pii);
        assertNotNull(formula);
        assertEquals(20, formula.getLiterals().size());
        assertEquals(91, formula.getClauses().size());

        // 1st clause
        assertEquals(3, formula.getClauses().get(0).getLiterals()[0].getIndex());
        assertEquals(false, formula.getClauses().get(0).getLiterals()[0].isNeg());
        assertEquals(17, formula.getClauses().get(0).getLiterals()[1].getIndex());
        assertEquals(true, formula.getClauses().get(0).getLiterals()[1].isNeg());
        assertEquals(18, formula.getClauses().get(0).getLiterals()[2].getIndex());
        assertEquals(false, formula.getClauses().get(0).getLiterals()[2].isNeg());

        // 45th clause
        assertEquals(13, formula.getClauses().get(45).getLiterals()[0].getIndex());
        assertEquals(false, formula.getClauses().get(45).getLiterals()[0].isNeg());
        assertEquals(6, formula.getClauses().get(45).getLiterals()[1].getIndex());
        assertEquals(false, formula.getClauses().get(45).getLiterals()[1].isNeg());
        assertEquals(9, formula.getClauses().get(45).getLiterals()[2].getIndex());
        assertEquals(false, formula.getClauses().get(45).getLiterals()[2].isNeg());

        // the last clause
        assertEquals(3, formula.getClauses().get(90).getLiterals()[0].getIndex());
        assertEquals(false, formula.getClauses().get(90).getLiterals()[0].isNeg());
        assertEquals(15, formula.getClauses().get(90).getLiterals()[1].getIndex());
        assertEquals(true, formula.getClauses().get(90).getLiterals()[1].isNeg());
        assertEquals(4, formula.getClauses().get(90).getLiterals()[2].getIndex());
        assertEquals(true, formula.getClauses().get(90).getLiterals()[2].isNeg());
    }

}
