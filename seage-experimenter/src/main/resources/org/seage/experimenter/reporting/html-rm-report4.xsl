<?xml version="1.0" encoding="utf-8"?>

<!--
    Document   : html-rm-report4.xsl
    Created on : 17. duben 2012,17:07
    Author     : zmatlja1
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    
<xsl:output method="html" doctype-system="http://www.w3.org/TR/html4/loose.dtd" doctype-public="-//W3C//DTD HTML 4.01//EN" indent="yes" />

    <xsl:template match="/">
        <html>
            <head>
                <title>Report 4</title>
            </head>
            <body>
                <xsl:apply-templates /> 
            </body>
        </html>         
    </xsl:template>

    <xsl:template match="Report">
        <h1>Report 4</h1>
        
        <xsl:import href="../seage-experimenter/src/main/resources/org/seage/experimenter/reporting/algorithm-parameters-names.xsl"/>
        <xsl:import href="../seage-experimenter/src/main/resources/org/seage/experimenter/reporting/algorithm-parameters-values.xsl"/>
        <xsl:import href="../seage-experimenter/src/main/resources/org/seage/experimenter/reporting/milis-to-sec.xsl"/>        
        
        <table border="1">
        <xsl:for-each select="ExampleSet[1]/Examples/Example">
            <xsl:variable name="Algorithm" select="Result[1]/@value" />

            <tr>
                 <td>
                    <h3><xsl:value-of select="Result[1]/@value" />:</h3>
                </td>
                <td>
                    Time <xsl:call-template name="MillisecondsToSeconds"><xsl:with-param name="miliseconds" select="Result[3]/@value" /></xsl:call-template> s - 
                    <xsl:call-template name="MillisecondsToSeconds"><xsl:with-param name="miliseconds" select="Result[4]/@value" /></xsl:call-template> s
                </td>
            </tr>
            
            <xsl:call-template name="GetParametersValues">
                <xsl:with-param name="Algorithm" select="$Algorithm" />
                <xsl:with-param name="FirstParameterStartPosition" select="5" />
            </xsl:call-template>
            
        </xsl:for-each>
        </table>
    </xsl:template>
</xsl:stylesheet>
