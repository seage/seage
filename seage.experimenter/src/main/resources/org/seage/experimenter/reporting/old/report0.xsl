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
        <xsl:for-each-group select="ExperimentTask/Config/Problem" group-by="@id">
            <tr><td><h1><xsl:value-of select="@id"/></h1></td></tr>
            <xsl:for-each-group select="current-group()/../.." group-by="@experimentID">
                <tr><td/><td><h2><xsl:value-of select="@experimentID"/></h2></td></tr>
                <xsl:for-each-group select="current-group()/Config/Algorithm" group-by="@id">
                    <tr><td/><td/><td><h3><xsl:value-of select="@id"/></h3></td></tr>
                    <xsl:for-each-group select="current-group()/../Problem/Instance" group-by="@name">
                        <tr><td/><td/><td/><td><h4><xsl:value-of select="@name"/></h4></td></tr>
                        
                        <xsl:variable name="results">
                            <xsl:for-each-group select="current-group()/../.." group-by="@configID">
                                <xsl:sort select="sum(current-group()/../AlgorithmReport/Statistics/@bestObjVal)" order="ascending"/> 
                                <xsl:variable name="stats" select="current-group()/../AlgorithmReport/Statistics"/>
                                <xsl:element name="r">
                                    <xsl:attribute name="configID">
                                        <xsl:value-of select="@configID"/>
                                    </xsl:attribute>
                                    <xsl:attribute name="val">
                                        <xsl:value-of select="sum($stats/@bestObjVal) div count($stats)"/>
                                    </xsl:attribute>
                                    <xsl:copy-of select="Algorithm"/>
                                </xsl:element>
                            </xsl:for-each-group>
                        </xsl:variable>
                        
                        <tr><td/><td/><td/><td/><td><h5><xsl:value-of select="$results/r[1]/@configID"/></h5></td><td><xsl:value-of select="$results/r[1]/@val"/></td></tr>
                        <tr><td/><td/><td/><td/><td/>
                            <td>
                                <xsl:for-each select="$results/r[1]/Algorithm/Parameters/@*">
                                    <xsl:value-of select="name(.)"/> : <xsl:value-of select="."/>,
                                </xsl:for-each>
                            </td>
                        </tr>
                    </xsl:for-each-group>
                </xsl:for-each-group>
            </xsl:for-each-group>
            <tr/>
        </xsl:for-each-group>                  
                
                <!--xsl:apply-templates select="Experiment">
                    <xsl:sort select="@id" order="descending"/>
                </xsl:apply-templates-->
            
        
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
