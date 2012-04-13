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
package org.seage.experimenter.reporting;

import java.io.FileInputStream;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author zmatlja1
 */
public class Transformer {
    
    private static Transformer _transformer;
 
    private Transformer(){}
    
    public static Transformer getInstance()
    {
        if(_transformer == null)
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
        javax.xml.transform.Transformer xmlTransformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(getClass().getResourceAsStream(xsltName)));      
        
        xmlTransformer.transform(new StreamSource(new FileInputStream(xmlPath)), outputStream);
    }
    
}
