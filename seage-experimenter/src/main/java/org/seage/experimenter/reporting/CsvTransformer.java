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


import com.csvreader.CsvReader;
import java.nio.charset.Charset;
import org.seage.data.DataNode;

/**
 *
 * @author zmatlja1
 */
public class CsvTransformer {
    
    private static CsvTransformer transformer;
    
    private CsvTransformer() {}
    
    public static CsvTransformer getInstance()
    {
        if(transformer == null)
            transformer = new CsvTransformer();
        
        return transformer;
    }
    
    public DataNode csvToDataNode(String path) throws Exception
    {
        CsvReader csvReader = new CsvReader( path );
        
        System.out.println( csvReader.readHeaders() );
        
        System.out.println(path);
        System.out.println(csvReader.getHeaderCount());
        
        System.out.println("clmn " +   csvReader.getColumnCount());
        
        while(csvReader.readRecord())
        {
            System.out.println(csvReader.get("ExperimentID"));
        }
        
        return null;
    }
    
}
