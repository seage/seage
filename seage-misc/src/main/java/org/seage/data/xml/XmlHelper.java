/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.data.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

import org.seage.data.DataNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Richard Malek
 */
public class XmlHelper {
  /**
   * Parse xml from string and create DataNode.
   */
  public static DataNode readXml(String xml) throws Exception {
    InputStream is = new ByteArrayInputStream(xml.getBytes());
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    DocumentBuilder builder = factory.newDocumentBuilder();

    Document doc = builder.parse(is);

    return readElement(doc.getDocumentElement());
  }

  public static DataNode readXml(File file) throws Exception {
    return readXml(new FileInputStream(file), null);
  }

  public static DataNode readXml(InputStream is) throws Exception {
    return readXml(is, null);
  }

  public static DataNode readXml(InputStream is, InputStream schema) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    if (schema != null) {
      factory.setNamespaceAware(true);
    } else {
      factory.setNamespaceAware(false);
    }

    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(is);

    if (schema != null) {
      // get validation driver:
      SchemaFactory factory2 = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
      factory2.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
      factory2.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
      // create schema by reading it from an XSD file:
      Schema schemaObj = factory2.newSchema(new StreamSource(schema));
      Validator validator = schemaObj.newValidator();
      // at last perform validation:
      validator.validate(new DOMSource(doc));
    }

    return readElement(doc.getDocumentElement());
  }

  private static DataNode readElement(Element xmlElem) throws Exception {

    DataNode result = new DataNode(xmlElem.getNodeName());

    NamedNodeMap atributes = xmlElem.getAttributes();
    for (int i = 0; i < atributes.getLength(); i++) {
      String attName = atributes.item(i).getNodeName();
      if (attName.startsWith("xmlns") || attName.contains(":")) {
        continue;
      }
      result.putValue(attName, atributes.item(i).getNodeValue());
    }

    NodeList nodeList = xmlElem.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
      if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
        Element elem = (Element) nodeList.item(i);
        result.putDataNodeRef(readElement(elem));
      }
    }

    return result;

  }

  public static synchronized void writeXml(DataNode dataSet, String path) throws Exception {
    writeXml(dataSet, new FileOutputStream(new File(path)));
  }

  public static void writeXml(DataNode dataSet, File f) throws Exception {
    writeXml(dataSet, new FileOutputStream(f));
  }

  public static synchronized void writeXml(
      DataNode dataSet, ZipOutputStream outputStream, String entryName) throws Exception {
    outputStream.putNextEntry(new ZipEntry(entryName));
    writeXml(dataSet, outputStream);
  }

  public static synchronized void writeXml(
      DataNode dataSet, OutputStream outputStream) throws Exception {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

    DOMSource source = new DOMSource(dataSet.toXml());
    StreamResult result = new StreamResult(outputStream);
    transformer.transform(source, result);
  }

  public static String getStringFromDocument(Document doc) throws TransformerException {
    DOMSource domSource = new DOMSource(doc);
    StringWriter writer = new StringWriter();
    StreamResult result = new StreamResult(writer);
    TransformerFactory tf = TransformerFactory.newInstance();
    tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
    Transformer transformer = tf.newTransformer();
    return writer.toString();
  }

  public static void validate(DataNode dataNode, String schemaPath) throws Exception {
    validate(dataNode.toXml(), schemaPath);
  }

  public static void validate(Document document, String schemaPath) throws Exception {

    // create a SchemaFactory capable of understanding WXS schemas
    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    // load a WXS schema, represented by a Schema instance
    Source schemaFile = new StreamSource(new File(schemaPath));
    Schema schema = factory.newSchema(schemaFile);

    // create a Validator instance, which can be used to validate an
    // instance document
    Validator validator = schema.newValidator();

    // validate the DOM tree

    validator.validate(new DOMSource(document));
  }

  public static class XPath // Java's XPath is too slow, this is a hack
  {
    public String XPathStr;
    public List<String> Nodes;

    public XPath(String xPath) {
      XPathStr = xPath;
      Nodes = new ArrayList<String>();
      String[] split = xPath.split("/");
      for (int i = 2; i < split.length; i++) {
        if (split[i].startsWith("@")) {
          Nodes.add(split[i].substring(1));
        } else {
          Nodes.add(split[i]);
        }

      }
    }

  }

  public static String getValueFromDocument(Element elem, XPath xPath) {
    return getValueFromDocument(elem, xPath, 0);
  }

  public static String getValueFromDocument(Element elem, XPath xPath, int level) {
    if (level + 1 == xPath.Nodes.size()) {
      return elem.getAttribute(xPath.Nodes.get(level));
    } else {
      String s = xPath.Nodes.get(level);
      return getValueFromDocument((Element) elem.getElementsByTagName(s).item(0), xPath, level + 1);
    }
  }
}
