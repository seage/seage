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

package org.seage.grammar;

import java.util.Vector;

/**
 *
 * @author jenik
   F -> a
 */
public class IntGeneratorFunctor implements Functor
{

    public IntGeneratorFunctor(int startIndex)
    {
        this.startIndex = startIndex;
    }

    /** @brief retrieve value
      */
    public Object call()
    {
        if (pos < 0)
        {
            startIndex %= sourceVector.size();
            pos = sourceVector.get(startIndex) % sourceVector.size();
        }
        if (pos == sourceVector.size())
            pos = 0;
        int len = sourceVector.get(pos) + 1;
        Integer ret = new Integer(0);
        for (int i = 0; i < len; i++)
        {
            if (pos == sourceVector.size())
                pos = 0;
            ret <<= 8;
            ret += sourceVector.get(pos);
            pos++;
        }
        return ret;
    }

    public void setVector(Vector<Integer> source)
    {
        this.sourceVector = source;
        this.pos = -1;
    }

    private Vector<Integer> sourceVector;
    private int pos = -1;
    private int startIndex = -1;

}
