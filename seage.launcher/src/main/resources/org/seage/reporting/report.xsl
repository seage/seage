<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : report.xsl
    Created on : 22. listopad 2009, 22:20
    Author     : rick
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
     version="1.0">

    <xsl:output method="xml"/>
    <xsl:key name="problems" match="/xml/ExperimentReport/Config/Problem" use="@id"/>
    <xsl:key name="instances" match="/xml/ExperimentReport/Config/Problem/Instance" use="@name"/>

    <xsl:template match="/xml">                
        <xml>
        <xsl:for-each select="/xml/ExperimentReport/Config/Problem[generate-id(.) = generate-id(key('problems', @id)[1])]">
        <!--a> <xsl:value-of select="key('problems', 'TSP')/@id"/></a-->
            <problem>
                <xsl:attribute name="id">
                    <xsl:value-of select="@id" />
                </xsl:attribute>            
                
                <xsl:variable name="inst" select="key('problems', @id)/Instance"/>
                
                <xsl:for-each select="key('problems', @id)/Instance"> <!-- [count(. | key('instances', @name)[1]) = 1] -  [generate-id(.) = generate-id(key('instances', @name))]-->
                        <i c="{count(. | key('instances', @name)[1])}" a1="{generate-id(.)}" b="{generate-id(key('instances', @name))}" aa="{./@name}" bb="{key('instances', @name)/@name}">
                        <i1><xsl:copy-of select="."/></i1>
                        <i2><xsl:copy-of select="key('instances', @name)[1]"/></i2>
                        </i>
                </xsl:for-each>
                
                <i2><xsl:value-of select="key('problems', 'TSP2')/Instance[generate-id(.) = generate-id(key('instances', 'eil101.tsp'))]/@name"/></i2>
                
            </problem>
        </xsl:for-each>
            
        </xml>
    </xsl:template>

    <!--xsl:template match="batch">

    </xsl:template>

    <xsl:template match="run">

    </xsl:template-->

</xsl:stylesheet>
