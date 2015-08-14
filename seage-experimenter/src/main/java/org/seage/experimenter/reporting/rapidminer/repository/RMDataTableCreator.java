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
package org.seage.experimenter.reporting.rapidminer.repository;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import org.seage.data.xml.XmlHelper;
import org.seage.experimenter.reporting.IDocumentProcessor;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.table.DataRow;

public abstract class RMDataTableCreator implements IDocumentProcessor
{
    protected static Logger _logger = Logger.getLogger(RMDataTableCreator.class.getName());

    protected String _repositoryPath;
    protected String _tableName;
    protected List<Attribute> _attributes;
    protected List<DataRow> _dataTable;

    protected Hashtable<String, Hashtable<String, XmlHelper.XPath>> _versionedXPaths;

    public RMDataTableCreator(String repositoryPath, String tableName)
    {
        _repositoryPath = repositoryPath;
        _tableName = tableName;
        _attributes = new ArrayList<Attribute>();
        _dataTable = new ArrayList<DataRow>();

        _versionedXPaths = new Hashtable<String, Hashtable<String, XmlHelper.XPath>>();
    }

    public String getRepositoryPath()
    {
        return _repositoryPath;
    }

    public String getTableName()
    {
        return _tableName;
    }

    public List<Attribute> getAttributes()
    {
        return _attributes;
    }

    public List<DataRow> getDataTable()
    {
        return _dataTable;
    }
}
