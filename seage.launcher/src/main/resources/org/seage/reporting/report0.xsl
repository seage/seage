<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : report.xsl
    Created on : 22. listopad 2009, 22:20
    Author     : rick
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
     version="2.0">

    <!--xsl:output method="html"/>
    <xsl:key name="problems" match="/xml/ExperimentReport/Config/Problem" use="@id"/>
    <xsl:key name="instances" match="/xml/ExperimentReport/Config/Problem/Instance" use="@name"/-->

    <xsl:template match="/xml">
        <html><body><table>
        <!--xsl:for-each select="Experiment"></xsl:for-each-->
        <!--a> <xsl:value-of select="key('problems', 'TSP')/@id"/></a-->
                          
                <xsl:apply-templates select="Experiment">
                    <xsl:sort select="@id" order="descending"/>
                </xsl:apply-templates>
            
        
        </table></body></html>    
    </xsl:template>

    <xsl:template match="Experiment">    
        
        <tr><td><h1><xsl:value-of select="@id"/></h1></td></tr>
        <xsl:apply-templates/>        
    </xsl:template>
    
    <xsl:template match="Problem/Instance">                
        <tr><td/><td><h2><xsl:value-of select="@id"/></h2></td></tr>
        <xsl:apply-templates/>        
    </xsl:template>
    
    <xsl:template match="Config">
        <xsl:variable name="stats" select="Run/AlgorithmReport/Statistics"/>
        <tr><td/><td/><td><xsl:value-of select="Algorithm/@id"/></td><td><xsl:value-of select="sum($stats/@bestObjVal) div count($stats)"/></td></tr>
        <xsl:apply-templates/>
    </xsl:template>

    
    
    <!--xsl:template match="batch">

    </xsl:template>

    <xsl:template match="run">

    </xsl:template-->

</xsl:stylesheet>
