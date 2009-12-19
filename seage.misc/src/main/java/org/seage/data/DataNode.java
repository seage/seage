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
package org.seage.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
    private String _name;
    private HashMap<String, List<DataNode>> _dataNodes;
    private HashMap<String, Object> _values;
    private List<DataNodeListener> _listeners;

    private String _xslPath;

    public DataNode(String name)
    {
        _name = name;
        _dataNodes = new HashMap<String, List<DataNode>>();
        _values = new HashMap<String, Object>();
        
        _listeners = new ArrayList<DataNodeListener>();
        _xslPath = "";
    }

    public String getName()
    {
        return _name;
    }
	
    public void putDataNode(DataNode dataSet0) throws Exception
    {
        DataNode dataSet = (DataNode)dataSet0.clone();
        dataSet.setListeners(_listeners);

        if(!_dataNodes.containsKey(dataSet.getName()))
            _dataNodes.put(dataSet.getName(), new ArrayList<DataNode>());

        List<DataNode> list = _dataNodes.get(dataSet.getName());
        list.add(dataSet);
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
        _values.put(name,  value);
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
        if (o instanceof Integer)
        {
            return ((Integer)o).intValue();
        }
        else
        if (o instanceof Double)
        {
            return ((Double)o).intValue();
        }
        else
        {
            return Integer.parseInt(o.toString());
        }
    }

    public long getValueLong(String name) throws Exception
    {
        checkValueName(name);
        Object o = _values.get(name);
        if (o instanceof Long)
        {
            return ((Long)o).longValue();
        }
        else
        {
            return Long.parseLong((String)o);
        }
    }
    
    public double getValueDouble(String name) throws Exception
    {
        checkValueName(name);
        Object o = _values.get(name);
        if (o instanceof Number)
        {
            return ((Number)o).doubleValue();
        }
        else
        {
            return Double.parseDouble((String)o);
        }
    }

    public boolean getValueBool(String name) throws Exception
    {
        checkValueName(name);
        Object o = _values.get(name);
        if (o instanceof Boolean)
        {
            return ((Boolean)o).booleanValue();
        }
        else
        {
            return Boolean.parseBoolean((String)o);
        }
    }
    
    private void checkValueName(String name) throws Exception
    {
        if(!_values.containsKey(name))
            throw new Exception("DataNode does not contain value of name '"+name+"'");
    }

    private void checkNodeName(String name) throws Exception
    {
        if(!_dataNodes.containsKey(name))
            throw new Exception("DataNode does not contain node of name '"+name+"'");
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
    
    public List<DataNode> getDataNodes()
    {
        ArrayList<DataNode> result = new ArrayList<DataNode>();
        
        for(List<DataNode> list : _dataNodes.values())
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
        if(result == null)
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
        if(!_listeners.contains(listener))
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
        
        if(_xslPath.length()>0)
        {
            ProcessingInstruction pi = result.createProcessingInstruction("xml-stylesheet", "jkl;");
            pi.setData("type=\"text/xsl\" href=\""+_xslPath+"\"");
            result.appendChild(pi);
        }

        for(Entry<String, Object> e : _values.entrySet())
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
//        Element elem = xmlDoc.createElement("");
//
//        for(Entry<String, Object> e : _values.entrySet())
//            elem.setAttribute(e.getKey(), e.getValue().toString());
        
        //for(Entry<String, List<DataNode>> e : dataNodes)
        //{
            for(DataNode dn : dataNodes)
            {
                Element elem = parent.getOwnerDocument().createElement(dn.getName());
                for(String an : dn.getValueNames())
                {
                    Attr a = parent.getOwnerDocument().createAttribute(an);
                    a.setValue(dn.getValueStr(an));
                    elem.getAttributes().setNamedItem(a);
                }
                parent.appendChild(elem);

                toXmlElement(elem, dn.getDataNodes());
            }
        //}
        //return elem;
    }

    @Override
    public Object clone()
    {
        Object result = null;
        try
        {
            result = ObjectCloner.deepCopy(this);
        }
        finally
        {
            return result;
        }
    }
}
