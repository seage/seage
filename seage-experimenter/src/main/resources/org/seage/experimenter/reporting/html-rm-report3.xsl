<?xml version="1.0" encoding="utf-8"?>

<!--
    Document   : html-rm-report3.xsl
    Created on : 17. duben 2012,12:07
    Author     : zmatlja1
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    
<xsl:output method="html" doctype-system="http://www.w3.org/TR/html4/loose.dtd" doctype-public="-//W3C//DTD HTML 4.01//EN" indent="yes" />

    <xsl:template match="/">
        <html>
            <head>
                <title>Report 3</title>
            </head>
            <body>
                <xsl:apply-templates /> 
            </body>
        </html>         
    </xsl:template>

    <xsl:template match="Report">
        <h1>Report 3</h1>
        
        <xsl:import href="../seage-experimenter/src/main/resources/org/seage/experimenter/reporting/algorithm-parameters-names.xsl"/>
        <xsl:import href="../seage-experimenter/src/main/resources/org/seage/experimenter/reporting/algorithm-parameters-values.xsl"/>
        <xsl:import href="../seage-experimenter/src/main/resources/org/seage/experimenter/reporting/milis-to-sec.xsl"/>        
        
        <table border="1">
        <xsl:for-each select="ExampleSet[1]/Examples/Example">
            <xsl:variable name="Algorithm" select="Result[2]/@value" />
            <xsl:variable name="ProblemID" select="Result[1]/@value" />
            
            <xsl:choose>
                <xsl:when test="preceding-sibling::Example/Result[2]/@value=$Algorithm">
                    <tr><td></td></tr>
                </xsl:when>
                <xsl:otherwise>
                    <tr><td><h2><xsl:value-of select="Result[2]/@value" /></h2></td></tr>
                </xsl:otherwise>
            </xsl:choose>     

            <tr>
                <td></td><td></td>
                <td>
                    <h3><xsl:value-of select="Result[1]/@value" />:</h3>
                </td>
                <td>
                    Time <xsl:call-template name="MillisecondsToSeconds"><xsl:with-param name="miliseconds" select="Result[4]/@value" /></xsl:call-template> s - 
                    <xsl:call-template name="MillisecondsToSeconds"><xsl:with-param name="miliseconds" select="Result[5]/@value" /></xsl:call-template> s
                </td>
            </tr>
            
            <xsl:call-template name="GetParametersValues">
                <xsl:with-param name="Algorithm" select="$Algorithm" />
                <xsl:with-param name="FirstParameterStartPosition" select="6" />
            </xsl:call-template>
            
        </xsl:for-each>
        </table>
    </xsl:template>
</xsl:stylesheet>
