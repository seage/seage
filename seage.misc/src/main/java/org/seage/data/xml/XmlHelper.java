/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.data.xml;

import org.seage.data.DataNode;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.*;


/**
*   @author Richard Malek
*/
public class XmlHelper
{
    public static DataNode readXml(String xml) throws Exception
    {
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setXIncludeAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(is);
        
        return readElement(doc.getDocumentElement());
    }
    
    public static DataNode readXml(File file) throws Exception
    {
        return readXml(file, null);
    }

    public static DataNode readXml(File file, InputStream schema) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        if(schema != null)
            factory.setNamespaceAware(true);
        else
            factory.setNamespaceAware(false);
        
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);

        if(doc.getDocumentElement().hasAttribute("redirect"))
        {
            String path = doc.getDocumentElement().getAttribute("redirect");
            if(!path.startsWith("\\") && file.getParent() != null)
                path = file.getParent()+"\\"+path;
            return readXml(new File(path), schema);
        }
        else if(schema != null)
        {
            // get validation driver:
            SchemaFactory factory2 = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            // create schema by reading it from an XSD file:
            Schema schemaObj = factory2.newSchema(new StreamSource(schema));
            Validator validator = schemaObj.newValidator();
            // at last perform validation:
            validator.validate(new DOMSource(doc));
        }

        return readElement(doc.getDocumentElement());
    }
	
    private static DataNode readElement(Element xmlElem) throws Exception
    {
        try
        {
            DataNode result = new DataNode(xmlElem.getNodeName());

            NamedNodeMap atributes = xmlElem.getAttributes();
            for (int i = 0; i < atributes.getLength(); i++)
            {
                String attName = atributes.item(i).getNodeName();
                if(attName.startsWith("xmlns") || attName.contains(":"))
                    continue;
                result.putValue(attName, atributes.item(i).getNodeValue());
            }

            NodeList nodeList = xmlElem.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++)
            {
                if (nodeList.item(i).getNodeType() == Element.ELEMENT_NODE)
                {
                    Element elem = (Element)nodeList.item(i);
                    result.putDataNode(readElement(elem));
                }
            }

            return result;
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }
    
    public static void writeXml(DataNode dataSet, String path)
    {
        try
        {
            TransformerFactory tFactory =
                TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(dataSet.toXml());
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(source, result);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static String getStringFromDocument(Document doc)
    {
        try
        {
           DOMSource domSource = new DOMSource(doc);
           StringWriter writer = new StringWriter();
           StreamResult result = new StreamResult(writer);
           TransformerFactory tf = TransformerFactory.newInstance();
           Transformer transformer = tf.newTransformer();
           transformer.transform(domSource, result);
           return writer.toString();
        }
        catch(TransformerException ex)
        {
           ex.printStackTrace();
           return null;
        }
    }
    
    public static void validate(DataNode dataNode, String schemaPath) throws Exception
    {
        validate(dataNode.toXml(), schemaPath);
    }
            
    public static void validate(Document document, String schemaPath) throws Exception    
    {

        // create a SchemaFactory capable of understanding WXS schemas
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // load a WXS schema, represented by a Schema instance
        Source schemaFile = new StreamSource(new File(schemaPath));
        Schema schema = factory.newSchema(schemaFile);

        // create a Validator instance, which can be used to validate an instance document
        Validator validator = schema.newValidator();

        // validate the DOM tree

        validator.validate(new DOMSource(document));
    }
}
