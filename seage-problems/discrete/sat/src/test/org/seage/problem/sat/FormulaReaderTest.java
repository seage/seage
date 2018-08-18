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

package org.seage.problem.sat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.*;

/**
 *
 * @author Richard Malek
 */
public class FormulaReaderTest {

    public FormulaReaderTest() {
    }

    @BeforeAll
    public static void setUpClass() throws Exception {
    }

    @AfterAll
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
