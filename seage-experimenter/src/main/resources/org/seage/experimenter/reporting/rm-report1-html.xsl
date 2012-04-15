<?xml version="1.0" encoding="utf-8"?>

<!--
    Document   : rm-report1-html.xsl
    Created on : 12. duben 2012, 15:58
    Author     : zmatlja1
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    
<xsl:output method="html" doctype-system="http://www.w3.org/TR/html4/loose.dtd" doctype-public="-//W3C//DTD HTML 4.01//EN" indent="yes" />

    <xsl:template match="/">
        <html>
            <head>
                <title>Report 1</title>
            </head>
            <body>
                <xsl:apply-templates /> 
            </body>
        </html>         
    </xsl:template>

    <xsl:template match="Report">
        <h1>Report 1</h1>

        <xsl:import href="../seage-experimenter/src/main/resources/org/seage/experimenter/reporting/algorithm-parameters.xsl"/>
        <xsl:import href="../seage-experimenter/src/main/resources/org/seage/experimenter/reporting/milis-to-sec.xsl"/>
        
        <table border="1">
        <xsl:for-each select="ExampleSet[1]/Examples/Example">
            <tr><td><h2><xsl:value-of select="Result/@value" /></h2></td></tr>
            <xsl:variable name="Algorithm" select="Result[3]/@value" />
            <tr><td></td><td><h3><xsl:value-of select="Result[2]/@value" /></h3></td></tr>
            <tr>
                <td></td><td></td>
                <td>
                    <h3><xsl:value-of select="Result[3]/@value" />:</h3>
                </td>
                <td>
                    Time <xsl:call-template name="MillisecondsToSeconds"><xsl:with-param name="miliseconds" select="Result[4]/@value" /></xsl:call-template> s - 
                    <xsl:call-template name="MillisecondsToSeconds"><xsl:with-param name="miliseconds" select="Result[5]/@value" /></xsl:call-template> s
                </td>
            </tr>
            
            <xsl:for-each select="Result">
                <xsl:choose>
                    <xsl:when test="position()&lt;6"></xsl:when>
                    <xsl:when test="last()=position()"></xsl:when>
                    <xsl:when test="(position()-5) mod 2 = 0"></xsl:when>
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
            
            
        </xsl:for-each>
        </table>
    </xsl:template>

<!--    <xsl:variable name="SimulatedAnnealingParameters">
        <i ref="6">b</i>
        <i ref="8">d</i>
        <i ref="10">f</i>
        <i ref="g">h</i>
    </xsl:variable>-->
    
    

</xsl:stylesheet>
