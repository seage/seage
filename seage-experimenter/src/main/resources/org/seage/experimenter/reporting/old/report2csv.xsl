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

    <xsl:output method="text"/>
    <!--xsl:key name="problems" match="/xml/ExperimentReport/Config/Problem" use="@id"/>
    <xsl:key name="instances" match="/xml/ExperimentReport/Config/Problem/Instance" use="@name"/--> 

    <xsl:template match="/xml">
      experimentID;problemID;algorithmID;instanceID;configID;value               
                <xsl:apply-templates select="ExperimentTask">
                    <!--xsl:sort select="@id" order="descending"/-->
                </xsl:apply-templates>
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


    <xsl:template match="ExperimentTask">    
        <xsl:value-of select="@experimentID"/>;<xsl:value-of select="Config/Problem/@id"/>;<xsl:value-of select="Config/Algorithm/@id"/>;<xsl:value-of select="Config/Problem/Instance/@name"/>;<xsl:value-of select="Config/@configID"/>;<xsl:value-of select="AlgorithmReport/Statistics/@bestObjVal"/>;
    </xsl:template>
    
    
    <!--xsl:template match="batch">

    </xsl:template>

    <xsl:template match="run">

    </xsl:template-->

</xsl:stylesheet>
