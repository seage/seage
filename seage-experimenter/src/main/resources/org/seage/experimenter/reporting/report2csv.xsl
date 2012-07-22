<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : report.xsl
    Created on : 22. listopad 2009, 22:20
    Author     : rick
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:exsl="http://exslt.org/common"
                extension-element-prefixes="exsl"
     			version="1.0">

    <xsl:output method="text" indent="no"/>
	
	<xsl:variable name="header">
		<item>ExperimentID</item>
		<item>ProblemID</item>
		<item>AlgorithmID</item>
		<item>InstanceID</item>
		<item>ConfigID</item>
		<item>InitSolutionValue2</item>
		<item>SolutionValue</item>
		<item>Duration</item>
	</xsl:variable>
	
    <xsl:template match="/">
    	<xsl:for-each select="exsl:node-set($header)/item">
    		<xsl:value-of select="."/><xsl:value-of select="';'"/>
    	</xsl:for-each>
        <xsl:apply-templates/>
    </xsl:template>
    

     <xsl:template match="ExperimentReport">
        <xsl:call-template name="ExperimentRun"/>
    </xsl:template>
    
     <xsl:template match="ExperimentTask">
        <xsl:call-template name="ExperimentRun"/>
    </xsl:template>

    <xsl:template name="ExperimentRun">
        <xsl:value-of select="@experimentID"/>;<xsl:value-of select="Config/Problem/@id"/>;<xsl:value-of select="Config/Algorithm/@id"/>;<xsl:value-of select="Config/Problem/Instance/@name"/>;<xsl:value-of select="Config/@configID"/>;<xsl:value-of select="AlgorithmReport/Statistics/@initObjVal"/>;<xsl:value-of select="AlgorithmReport/Statistics/@bestObjVal"/>;<xsl:value-of select="@duration"/>;<xsl:call-template name="parameters"/>;
    </xsl:template>
    
    <xsl:template name="parameters">
        <xsl:for-each select="Config/Algorithm/Parameters/@*"><xsl:value-of select="." />;</xsl:for-each>
    </xsl:template>


</xsl:stylesheet>
