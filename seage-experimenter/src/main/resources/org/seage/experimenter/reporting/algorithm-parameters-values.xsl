<?xml version="1.0" encoding="windows-1250"?>

<!--
    Document   : algorithm-parameters-values.xsl
    Created on : 15. duben 2012, 22:00
    Author     : zmatlja1
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>

    <xsl:template name="GetParametersValues">
        <xsl:param name="Algorithm" />
        <xsl:param name="FirstParameterStartPosition" />
        
        <xsl:for-each select="Result">
                <xsl:choose>
                    <xsl:when test="position()&lt;$FirstParameterStartPosition"></xsl:when>
                    <xsl:when test="last()=position()"></xsl:when>
                    <xsl:when test="(position()-($FirstParameterStartPosition)-1) mod 2 = 0"></xsl:when>
                    <xsl:when test="@value=-1.0 and following-sibling::Result/@value=-1.0"></xsl:when>
                    <xsl:otherwise>
                         <tr>
                             <td></td><td></td>
                             <th>
                                 <xsl:call-template name="GetParametresByAlgorithmAndPosition">
                                     <xsl:with-param name="algorithm" select="$Algorithm" />
                                     <xsl:with-param name="position" select="position()" />
                                 </xsl:call-template>
                             </th>
                             <td>
                                 <xsl:value-of select="@value" /> - <xsl:value-of select="following-sibling::Result/@value" />
                             </td>
                        </tr>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
