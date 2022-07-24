<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : report.xsl
    Created on : 22. listopad 2009, 22:20
    Author     : rick
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="2.0">

    <xsl:output method="html"/>

    <xsl:template match="/report">
        <html>
            <head>
                <title>report.xsl</title>
            </head>
            <body>
                
                    <xsl:for-each-group select="run" group-by="@runID">
                        <xsl:sort select="current-grouping-key()"/>                        
                        <table border="1" style="border-collapse: collapse;" width="1000" onclick="document.getElementById('{current-grouping-key()}').style.display='block'">
                            <tr><td>instance</td><td>size</td><td>min</td><td>avg</td><td>max</td><td>hash</td></tr>
                            <xsl:for-each select="current-group()">
                                <xsl:sort select="@size" data-type="number"/>
                                    <tr>
                                        <td align="right"><xsl:value-of select="@instance"/></td>
                                        <td align="right"><xsl:value-of select="@size"/></td>
                                        <td align="right"><xsl:value-of select="format-number(stats/@minFitness, '#.00')"/></td>
                                        <td align="right"><xsl:value-of select="format-number(stats/@avgFitness, '#.00')"/></td>
                                        <td align="right"><xsl:value-of select="format-number(stats/@maxFitness, '#.00')"/></td>
                                        <td><code><xsl:value-of select="current-grouping-key()"/></code></td>
                                    </tr>
                            </xsl:for-each>                            
                        </table>

                        <div style="display:none" id="{current-grouping-key()}" onclick="document.getElementById('{current-grouping-key()}').style.display='none'">
                        <table border="1" style="border-collapse: collapse;" width="1000">                               
                            <xsl:for-each select="current-group()[1]/config/agent">
                                <tr><td><xsl:value-of select="@name"/></td><td/><td/></tr>
                                <xsl:for-each select=".//@*">
                                    <tr><td/><td><xsl:value-of select="name()"/></td><td><xsl:value-of select="."/></td></tr>
                                </xsl:for-each>
                            </xsl:for-each> 
                        </table>
                        </div>
                        <p/>
                    </xsl:for-each-group>
                    
                
            </body>
        </html>
    </xsl:template>


</xsl:stylesheet>
