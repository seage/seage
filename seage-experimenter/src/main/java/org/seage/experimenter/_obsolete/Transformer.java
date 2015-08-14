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
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.experimenter._obsolete;

import java.io.FileInputStream;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;

/**
 *
 * @author zmatlja1
 */
public class Transformer
{

    private static Transformer _transformer;

    private Transformer()
    {
    }

    public static Transformer getInstance()
    {
        if (_transformer == null)
            _transformer = new Transformer();

        return _transformer;
    }

    /**
     * Method transforms a XML file into CSV format by using XSL Template
     * 
     * @param String xmlPath - Path to a XML file
     * @param String xsltName - Path to a XSLT
     * @param StreamResult outputStream - Stream from CSV file
     * 
     * @return DataNode - SEAGE data structure
     */
    public void transformByXSLT(String xmlPath, String xsltName, StreamResult outputStream) throws Exception
    {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        javax.xml.transform.Transformer xmlTransformer = tFactory
                .newTransformer(new javax.xml.transform.stream.StreamSource(getClass().getResourceAsStream(xsltName)));

        xmlTransformer.transform(new StreamSource(new FileInputStream(xmlPath)), outputStream);
    }

    public void transformByXSLTFromDOMSource(Document xmlDocument, String xsltName, StreamResult outputStream)
            throws Exception
    {
        javax.xml.transform.Transformer xmlTransformer = TransformerFactory.newInstance()
                .newTransformer(new javax.xml.transform.stream.StreamSource(getClass().getResourceAsStream(xsltName)));

        xmlTransformer.transform(new DOMSource(xmlDocument), outputStream);
    }

}
