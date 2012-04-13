<?xml version="1.0" encoding="utf-8"?>

<!--
    Document   : report2html.xsl
    Created on : 12. duben 2012, 15:58
    Author     : zmatlja1
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
                xmlns:fn="http://www.w3.org/2005/xpath-functions">
    
<xsl:output method="html" indent="no"/>

    <xsl:template match="/">
        <xsl:apply-templates/>  
    </xsl:template>

    <xsl:template match="Statistics">
        <h1>Report 1</h1>
        
        <table border="1">
        <xsl:for-each select="Records/Record">
            <tr><td><h2><xsl:value-of select="Value/@value" /></h2></td></tr>
            <tr><td> </td><td><h3><xsl:value-of select="Value[2]/@value" />-</h3></td></tr>
            <tr><td> </td><td> </td><td><h3><xsl:value-of select="Value[3]/@value" />:</h3></td><td>Time <xsl:value-of select="Value[4]/@value" /> - <xsl:value-of select="Value[5]/@value" /></td></tr>
            
            cout <xsl:value-of select="count(/Statistics/Records/Record[1]/Value)" /> - <xsl:value-of select="count(/Statistics/Headers/Header)" />
            <xsl:for-each select="Value">
                <xsl:choose>
                    <xsl:when test="position()&lt;6"></xsl:when>
                    <xsl:when test="last()=position()"></xsl:when>
                    <xsl:otherwise>
                        <tr><td> </td><td> <xsl:value-of select="position()" /></td><td><h3><xsl:value-of select="/Statistics/Headers/Header[3]/@name" />:</h3></td><td><xsl:value-of select="@value" /> - <xsl:value-of select="following-sibling::Value/@value" /></td></tr>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
        </xsl:for-each>
        </table>
    </xsl:template>

</xsl:stylesheet>
