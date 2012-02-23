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

    <xsl:output method="text" indent="no"/>
    <!--xsl:key name="problems" match="/xml/ExperimentReport/Config/Problem" use="@id"/>
    <xsl:key name="instances" match="/xml/ExperimentReport/Config/Problem/Instance" use="@name"/--> 

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="/kkk">
        <![CDATA[experimentID;problemID;algorithmID;instanceID;configID;value]]> 
                <xsl:apply-templates select="ExperimentReport">
                    <!--xsl:sort select="@id" order="descending"/-->
                </xsl:apply-templates>
    </xsl:template>

     <xsl:template match="ExperimentReport">
        <xsl:call-template name="asdf"/>
    </xsl:template>
    
     <xsl:template match="ExperimentTask">
        <xsl:call-template name="asdf"/>
    </xsl:template>

    <xsl:template name="asdf">
        <xsl:value-of select="@experimentID"/>;<xsl:value-of select="Config/Problem/@id"/>;<xsl:value-of select="Config/Algorithm/@id"/>;<xsl:value-of select="Config/Problem/Instance/@name"/>;<xsl:value-of select="Config/@configID"/>;<xsl:value-of select="AlgorithmReport/Statistics/@bestObjVal"/>;<xsl:call-template name="parameters"/>;
    </xsl:template>
    
    <xsl:template name="parameters">
        <xsl:for-each select="Config/Algorithm/Parameters/@*"><xsl:value-of select="." />;</xsl:for-each>        
    </xsl:template>
    
    
    
    
    <!--xsl:template match="batch">

    </xsl:template>

    <xsl:template match="run">

    </xsl:template-->

</xsl:stylesheet>
