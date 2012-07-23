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

/**
 * Summary description for Literal.
 */
public class Literal implements java.lang.Cloneable {

    private int _index;
    private boolean _neg;

    public Literal(int index, boolean neg) {
        _index = index;
        _neg = neg;
    }

    public boolean isNeg() {
        return _neg;
    }


    public int getIndex()
    {
        return _index;
    }

    @Override
    public String toString() {
        String result = "";
        result += _neg==true?"-"+_index:""+_index;
        return result;
    }
}
