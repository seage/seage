<?xml version="1.0" encoding="utf-8"?>

<!--
    Document   : rm-report1-html.xsl
    Created on : 12. duben 2012, 15:58
    Author     : zmatlja1
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    
<xsl:output method="html" indent="no"/>

    <xsl:template match="/">
        <xsl:apply-templates/>  
    </xsl:template>

    <xsl:template match="ExampleSet">
        <h1>Report 1</h1>
        
        <table border="1">
        <xsl:for-each select="Examples/Example">
            <tr><td><h2><xsl:value-of select="Result/@value" /></h2></td></tr>
            <tr><td> </td><td><h3><xsl:value-of select="Result[2]/@value" /></h3></td></tr>
            <tr><td> </td><td> </td><td><h3><xsl:value-of select="Result[3]/@value" />:</h3></td><td>Time <xsl:value-of select="Result[4]/@value" /> - <xsl:value-of select="Result[5]/@value" /></td></tr>
            
            <xsl:for-each select="Result">
                <xsl:choose>
                    <xsl:when test="position()&lt;6"></xsl:when>
                    <xsl:when test="last()=position()"></xsl:when>
                    <xsl:when test="(position()-5) mod 2 = 0"></xsl:when>
                    <xsl:when test="@value=-1.0 and following-sibling::Result/@value=-1.0"></xsl:when>
                    <xsl:otherwise>
                        <tr><td> </td><td> <xsl:value-of select="/ExampleSet/Attributes/Attribute[6]/@name" /></td><td><h3><xsl:value-of select="/ExampleSet/Attributes/Attribute[3]/@name" />:</h3></td><td><xsl:value-of select="@value" /> - <xsl:value-of select="following-sibling::Result/@value" /></td></tr>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
        </xsl:for-each>
        </table>
    </xsl:template>

</xsl:stylesheet>
