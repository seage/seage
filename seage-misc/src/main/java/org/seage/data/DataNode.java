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
package org.seage.data;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.seage.data.file.FileHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

/**
 *  @author Richard Malek.
 */
public class DataNode implements Serializable
{
    private static final long serialVersionUID = 2193543253630004569L;

    private String _name;

    private HashMap<String, List<DataNode>> _dataNodes;
    private HashMap<String, Object> _values;
    private HashMap<String, DataNode> _ids;

    private List<DataNodeListener> _listeners;

    private String _xslPath;

    public DataNode(String name)
    {
        _name = name;
        _dataNodes = new HashMap<String, List<DataNode>>();
        _values = new HashMap<String, Object>();
        _ids = new HashMap<String, DataNode>();

        _listeners = new ArrayList<DataNodeListener>();
        _xslPath = "";
    }

    protected DataNode(DataNode node)
    {
        DataNode dn = (DataNode) node.clone();
        _name = dn._name;
        _dataNodes = dn._dataNodes;
        _values = dn._values;
        _ids = dn._ids;

        _listeners = dn._listeners;
        _xslPath = dn._xslPath;
    }

    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public void putDataNode(DataNode dataSet0) throws Exception
    {
        DataNode dataSet = (DataNode) dataSet0.clone();
        putDataNodeRef(dataSet);
    }

    public void putDataNodeRef(DataNode dataSet) throws Exception
    {
        dataSet.setListeners(_listeners);

        if (!_dataNodes.containsKey(dataSet.getName()))
            _dataNodes.put(dataSet.getName(), new ArrayList<DataNode>());

        if (dataSet.containsValue("id"))
            _ids.put(dataSet.getValueStr("id"), dataSet);

        List<DataNode> list = _dataNodes.get(dataSet.getName());
        list.add(dataSet);
    }

    public void removeDataNode(String name, Object o)
    {
        _dataNodes.get(name).remove(o);
    }

    public void removeDataNode(String name, int index)
    {
        _dataNodes.get(name).remove(index);
    }

    public boolean containsNode(String nodeName)
    {
        return _dataNodes.containsKey(nodeName);
    }

    public boolean containsValue(String valueName)
    {
        return _values.containsKey(valueName);
    }

    public void putValue(String name, Object value)
    {
        _values.put(name, value);
    }

    public Object getValue(String name) throws Exception
    {
        checkValueName(name);
        return _values.get(name);
    }

    public String getValueStr(String name) throws Exception
    {
        checkValueName(name);
        return _values.get(name).toString();
    }

    public int getValueInt(String name) throws Exception
    {
        checkValueName(name);
        Object o = _values.get(name);

        if (o instanceof Number)
            return ((Number) o).intValue();
        else if (o instanceof String)
            return new Long(Math.round(Double.parseDouble(o.toString()))).intValue();
        else
            throw new Exception("Not an integer number");
    }

    public long getValueLong(String name) throws Exception
    {
        checkValueName(name);
        Object o = _values.get(name);

        if (o instanceof Number)
            return ((Number) o).intValue();
        else if (o instanceof String)
            return Math.round(Double.parseDouble(o.toString()));
        else
            throw new Exception("Not a long number");
    }

    public double getValueDouble(String name) throws Exception
    {
        checkValueName(name);
        Object o = _values.get(name);

        if (o instanceof Number)
            return ((Number) o).doubleValue();
        else if (o instanceof String)
            return Double.parseDouble(o.toString());
        else
            throw new Exception("Not a double number");
    }

    public boolean getValueBool(String name) throws Exception
    {
        checkValueName(name);
        Object o = _values.get(name);

        if (o instanceof Boolean)
            return ((Boolean) o).booleanValue();
        else if (o instanceof String)
            return Boolean.parseBoolean(o.toString());
        else
            throw new Exception("Not a double number");
    }

    private void checkValueName(String name) throws Exception
    {
        if (!_values.containsKey(name))
            throw new Exception("DataNode does not contain value of name '" + name + "'");
    }

    private void checkNodeName(String name) throws Exception
    {
        if (!_dataNodes.containsKey(name))
            throw new Exception("DataNode does not contain node of name '" + name + "'");
    }

    public Collection<Object> getValues()
    {
        return _values.values();
    }

    public Collection<String> getValueNames()
    {
        return _values.keySet();
    }

    public Collection<String> getNames()
    {
        return _values.keySet();
    }

    public DataNode getDataNode(String name) throws Exception
    {
        checkNodeName(name);
        return _dataNodes.get(name).get(0);
    }

    public DataNode getDataNode(String name, int index) throws Exception
    {
        checkNodeName(name);
        return _dataNodes.get(name).get(index);
    }

    public DataNode getDataNodeById(String id) throws Exception
    {
        return _ids.get(id);
    }

    public List<DataNode> getDataNodes()
    {
        ArrayList<DataNode> result = new ArrayList<DataNode>();

        for (List<DataNode> list : _dataNodes.values())
            result.addAll(list);
        return result;
    }

    /**
     * @param name
     * @return Returns all subnodes of given name
     */
    public List<DataNode> getDataNodes(String name)
    {
        List<DataNode> result = _dataNodes.get(name);
        if (result == null)
            return new ArrayList<DataNode>();
        else
            return result;
    }

    public Collection<String> getDataNodeNames()
    {
        return _dataNodes.keySet();
    }

    private void setListeners(List<DataNodeListener> listeners)
    {
        _listeners = listeners;
    }

    public void addDataNodeListener(DataNodeListener listener)
    {
        if (!_listeners.contains(listener))
            _listeners.add(listener);
    }

    public void setXslPath(String xslPath)
    {
        _xslPath = xslPath;
    }

    public Document toXml() throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);

        DocumentBuilder db = factory.newDocumentBuilder();
        DOMImplementation domImpl = db.getDOMImplementation();

        Document result = domImpl.createDocument(null, getName(), null);

        if (_xslPath.length() > 0)
        {
            ProcessingInstruction pi = result.createProcessingInstruction("xml-stylesheet", "jkl;");
            pi.setData("type=\"text/xsl\" href=\"" + _xslPath + "\"");
            result.appendChild(pi);
        }

        for (Entry<String, Object> e : _values.entrySet())
        {
            Attr a = result.createAttribute(e.getKey());
            a.setValue(e.getValue().toString());
            result.getDocumentElement().getAttributes().setNamedItem(a);
        }

        toXmlElement(result.getDocumentElement(), getDataNodes());
        return result;
    }

    private void toXmlElement(Element parent, List<DataNode> dataNodes) throws Exception
    {
        for (DataNode dn : dataNodes)
        {
            Element elem = parent.getOwnerDocument().createElement(dn.getName());
            for (String an : dn.getValueNames())
            {
                Attr a = parent.getOwnerDocument().createAttribute(an);
                a.setValue(dn.getValueStr(an));
                elem.getAttributes().setNamedItem(a);
            }
            parent.appendChild(elem);

            toXmlElement(elem, dn.getDataNodes());
        }
    }

    public String hash() throws Exception
    {
        return FileHelper.md5fromString(toString());
    }

    @Override
    public String toString()
    {
        try
        {
            DOMSource domSource = new DOMSource(toXml());
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return "";
        }
    }

    @Override
    public Object clone()
    {
        Object result = null;
        try
        {
            result = ObjectCloner.deepCopy(this);
        }
        catch (Exception e)
        {
        }

        return result;
    }
}
