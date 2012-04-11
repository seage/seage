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
import java.util.List;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.seage.data.DataNode;

/**
 *
 * @author zmatlja1
 */
public class XSLTransformer {
    
    private static XSLTransformer performer;
    
    private XSLTransformer(){}
    
    public static XSLTransformer getInstance()
    {
        if(performer == null)
            performer = new XSLTransformer();
        
        return performer;
    }
    
    public void transformFromXML(String xmlPath, String xsltName, StreamResult outputStream) throws Exception
    {         
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(getClass().getResourceAsStream(xsltName)));      
        
        transformer.transform (new StreamSource(new FileInputStream(xmlPath)), outputStream);
    }
    
    
    
}
