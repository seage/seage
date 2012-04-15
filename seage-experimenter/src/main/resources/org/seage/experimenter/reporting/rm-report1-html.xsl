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

        <xsl:import href="../seage-experimenter/src/main/resources/org/seage/experimenter/reporting/algorithm-parameters-names.xsl"/>
        <xsl:import href="../seage-experimenter/src/main/resources/org/seage/experimenter/reporting/algorithm-parameters-values.xsl"/>
        <xsl:import href="../seage-experimenter/src/main/resources/org/seage/experimenter/reporting/milis-to-sec.xsl"/>        
        
        <table border="1">
        <xsl:for-each select="ExampleSet[1]/Examples/Example">
            <tr><td><h2><xsl:value-of select="Result/@value" /></h2></td></tr>
            <xsl:variable name="Algorithm" select="Result[3]/@value" />
            <xsl:variable name="ExperimetnID" select="Result/@value" />
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
            
            <xsl:call-template name="GetParametersValues">
                <xsl:with-param name="Algorithm" select="$Algorithm" />
                <xsl:with-param name="FirstParameterStartPosition" select="6" />
            </xsl:call-template>
            
            <tr><td colspan="4"> </td></tr>
            <tr><td></td><td><h2><xsl:value-of select="$Algorithm" /></h2></td></tr>
            <xsl:for-each select="/Report/ExampleSet[2]/Examples/Example[Result[1]/@value=$ExperimetnID]">
                <tr><td></td><td></td>
                    <td align="center"><font size="5"><strong><xsl:value-of select="Result[3]/@value" /></strong></font></td>
                    <td>Average Solution Value: <strong><xsl:value-of select="Result[4]/@value" /></strong></td>
                    <xsl:call-template name="GetParametersValues">
                        <xsl:with-param name="Algorithm" select="$Algorithm" />
                        <xsl:with-param name="FirstParameterStartPosition" select="5" />
                    </xsl:call-template>
                </tr>
                <tr><td colspan="4"> </td></tr>
            </xsl:for-each> 
        </xsl:for-each>
        </table>
    </xsl:template>
</xsl:stylesheet>
