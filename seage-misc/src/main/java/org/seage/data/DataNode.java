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
 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.data;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

/**
 * DataNode for storing dynamic data.
 * @author Richard Malek.
 */
public class DataNode {

  private String name;

  private HashMap<String, List<DataNode>> dataNodes;
  private HashMap<String, Object> values;
  private HashMap<String, DataNode> ids;

  private List<DataNodeListener> listeners;

  /**
   * DataNode constructor with name of the root element.
   */
  public DataNode(String name) {
    this.name = name;
    this.dataNodes = new HashMap<>();
    this.values = new HashMap<>();
    this.ids = new HashMap<>();

    this.listeners = new ArrayList<DataNodeListener>();
  }

  protected DataNode(DataNode node) {
    this(node.name);
    // plain copy
    this.values.putAll(node.values);    
    this.listeners.addAll(node.listeners);

    // copy child data nodes
    for (Entry<String, List<DataNode>> childEntry : node.dataNodes.entrySet()) {
      List<DataNode> children = new ArrayList<>();
      for (DataNode dn : childEntry.getValue()) {
        DataNode child = new DataNode(dn);
        children.add(child);
      }
      this.dataNodes.put(childEntry.getKey(), children);
    }
    // copy ids
    for (List<DataNode> children : node.dataNodes.values()) {
      for (DataNode dn : children) {
        if (dn.values.containsKey("id")) {
          this.ids.put(dn.values.get("id").toString(), dn);
        }
      }
    }
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void putDataNode(DataNode dataSet) throws Exception {
    DataNode clone = new DataNode(dataSet);
    putDataNodeRef(clone);
  }

  /**
   * Puts another datanode as is - i.e. the dataSet to be put is not cloned
   * @param dataSet The reference to the dataSet to be put.
   * @throws Exception .
   */
  public void putDataNodeRef(DataNode dataSet) throws Exception {
    dataSet.setListeners(this.listeners);

    if (!this.dataNodes.containsKey(dataSet.getName())) {
      this.dataNodes.put(dataSet.getName(), new ArrayList<>());
    }

    if (dataSet.containsValue("id")) {
      this.ids.put(dataSet.getValueStr("id"), dataSet);
    }

    List<DataNode> list = this.dataNodes.get(dataSet.getName());
    list.add(dataSet);
  }

  public void removeDataNode(String name, Object o) {
    this.dataNodes.get(name).remove(o);
  }

  public void removeDataNode(String name, int index) {
    this.dataNodes.get(name).remove(index);
  }

  public boolean containsNode(String nodeName) {
    return this.dataNodes.containsKey(nodeName);
  }

  public boolean containsValue(String valueName) {
    return this.values.containsKey(valueName);
  }

  public void putValue(String name, Object value) {
    this.values.put(name, value);
  }

  public Object getValue(String name) throws Exception {
    checkValueName(name);
    return this.values.get(name);
  }

  public String getValueStr(String name) throws Exception {
    checkValueName(name);
    return this.values.get(name).toString();
  }

  /**
   * Gets attribute value by name and converts it to integer.
   * @param name Attribute name.
   * @return Integer value.
   * @throws Exception .
   */
  public int getValueInt(String name) throws Exception {
    checkValueName(name);
    Object o = this.values.get(name);

    if (o instanceof Number) {
      return ((Number) o).intValue();
    } else if (o instanceof String) {
      return Integer.parseInt(o.toString());
    } else {
      throw new Exception("Not an integer number");
    }
  }

  /**
   * Gets attribute value by name and converts it to long.
   * @param name Attribute name.
   * @return Long value.
   * @throws Exception .
   */
  public long getValueLong(String name) throws Exception {
    checkValueName(name);
    Object o = this.values.get(name);

    if (o instanceof Number) {
      return ((Number) o).intValue();
    } else if (o instanceof String) {
      return Math.round(Double.parseDouble(o.toString()));
    } else {
      throw new Exception("Not a long number");
    }
  }

  /**
   * Gets attribute value by name and converts it to double.
   * @param name Attribute name.
   * @return Double value.
   * @throws Exception .
   */
  public double getValueDouble(String name) throws Exception {
    checkValueName(name);
    Object o = this.values.get(name);

    if (o instanceof Number) {
      return ((Number) o).doubleValue();
    } else if (o instanceof String) {
      return Double.parseDouble(o.toString());
    } else {
      throw new Exception("Not a double number");
    }
  }

  /**
   * Gets attribute value by name and converts it to boolean.
   * @param name Attribute name.
   * @return Boolean value.
   * @throws Exception .
   */
  public boolean getValueBool(String name) throws Exception {
    checkValueName(name);
    Object o = this.values.get(name);

    if (o instanceof Boolean) {
      return ((Boolean) o).booleanValue();
    } else if (o instanceof String) {
      return Boolean.parseBoolean(o.toString());
    } else {
      throw new Exception("Not a double number");
    }
  }

  private void checkValueName(String name) throws Exception {
    if (!this.values.containsKey(name)) {
      throw new Exception("DataNode does not contain value of name '" + name + "'");
    }
  }

  private void checkNodeName(String name) throws Exception {
    if (!this.dataNodes.containsKey(name)) {
      throw new Exception("DataNode does not contain node of name '" + name + "'");
    }
  }

  public Collection<Object> getValues() {
    return this.values.values();
  }

  public Collection<String> getValueNames() {
    return this.values.keySet();
  }

  public Collection<String> getNames() {
    return this.values.keySet();
  }

  public DataNode getDataNode(String name) throws Exception {
    checkNodeName(name);
    return this.dataNodes.get(name).get(0);
  }

  public DataNode getDataNode(String name, int index) throws Exception {
    checkNodeName(name);
    return this.dataNodes.get(name).get(index);
  }

  public DataNode getDataNodeById(String id) throws Exception {
    return this.ids.get(id);
  }

  /**
   * Gets child data nodes.
   * @return
   */
  public List<DataNode> getDataNodes() {
    ArrayList<DataNode> result = new ArrayList<>();

    for (List<DataNode> list : this.dataNodes.values()) {
      result.addAll(list);
    }
    return result;
  }

  /**
   * Gets child data nodes of the given name.
   * @param name The name of the child dataset to be taken.
   * @return
   */
  public List<DataNode> getDataNodes(String name) {
    List<DataNode> result = this.dataNodes.get(name);
    if (result == null) {
      return new ArrayList<>();
    } else {
      return result;
    }
  }

  public Collection<String> getDataNodeNames() {
    return this.dataNodes.keySet();
  }

  private void setListeners(List<DataNodeListener> listeners) {
    this.listeners = listeners;
  }

  /**. */
  public void addDataNodeListener(DataNodeListener listener) {
    if (!this.listeners.contains(listener)) {
      this.listeners.add(listener);
    }
  }

  /**
   * Converts the current DataNode to the xml Document.
   * @return Xml document.
   * @throws Exception .
   */
  public Document toXml() throws Exception {
    return toXml("");
  }

  /**
   * Converts the current DataNode to the xml Document.
   * @return Xml document.
   * @throws Exception .
   */
  public Document toXml(String xslPath) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    factory.setNamespaceAware(false);

    DocumentBuilder db = factory.newDocumentBuilder();
    DOMImplementation domImpl = db.getDOMImplementation();

    Document result = domImpl.createDocument(null, getName(), null);

    if (xslPath.length() > 0) {
      ProcessingInstruction pi = result.createProcessingInstruction("xml-stylesheet", "jkl;");
      pi.setData("type=\"text/xsl\" href=\"" + xslPath + "\"");
      result.appendChild(pi);
    }

    for (Entry<String, Object> e : this.values.entrySet()) {
      Attr a = result.createAttribute(e.getKey());
      a.setValue(e.getValue().toString());
      result.getDocumentElement().getAttributes().setNamedItem(a);
    }

    toXmlElement(result.getDocumentElement(), getDataNodes());
    return result;
  }

  private void toXmlElement(Element parent, List<DataNode> dataNodes) throws Exception {
    for (DataNode dn : dataNodes) {
      Element elem = parent.getOwnerDocument().createElement(dn.getName());
      for (String an : dn.getValueNames()) {
        Attr a = parent.getOwnerDocument().createAttribute(an);
        a.setValue(dn.getValueStr(an));
        elem.getAttributes().setNamedItem(a);
      }
      parent.appendChild(elem);

      toXmlElement(elem, dn.getDataNodes());
    }
  }

  public String hash() throws Exception {
    return HashHelper.hashFromString(toString());
  }

  @Override
  public String toString() {
    try {
      DOMSource domSource = new DOMSource(toXml());
      StringWriter writer = new StringWriter();
      StreamResult result = new StreamResult(writer);
      TransformerFactory tf = TransformerFactory.newInstance();
      tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
      tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, ""); // Compliant
      Transformer transformer = tf.newTransformer();
      transformer.transform(domSource, result);
      return writer.toString();
    } catch (Exception ex) {
      ex.printStackTrace();
      return "";
    }
  }
}
