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
  version="1.0">

    <xsl:output method="html"/>

    <xsl:template match="/report">
        <html>
            <head>
                <title>report</title>
            </head>
            <body>
                
                    <xsl:for-each select="batch">
                        <xsl:sort select="@date"/>
                        <table border="1" style="border-collapse: collapse;" width="1000" onclick="document.getElementById('{@id}').style.display='block'">
                            <tr><td>date</td><td><xsl:value-of select="@date"/></td><td colspan="3"><tt><xsl:value-of select="@id"/></tt></td></tr>
                            <tr><td>instance</td><td>size</td><td>min</td><td>avg</td><td>max</td></tr>
                            <xsl:for-each select="run">
                                <xsl:sort select="@size" data-type="number"/>
                                    <tr>
                                        <td align="right"><xsl:value-of select="@instance"/></td>
                                        <td align="right"><xsl:value-of select="@size"/></td>
                                        <td align="right"><xsl:value-of select="format-number(stats/@minFitness, '#.00')"/></td>
                                        <td align="right"><xsl:value-of select="format-number(stats/@avgFitness, '#.00')"/></td>
                                        <td align="right"><xsl:value-of select="format-number(stats/@maxFitness, '#.00')"/></td>
                                    </tr>
                            </xsl:for-each>                            
                        </table>

                        <div style="display:none" id="{@id}" onclick="document.getElementById('{@id}').style.display='none'">
                        <table border="1" style="border-collapse: collapse;" width="1000">                               
                            <xsl:for-each select="config/agent">
                                <tr><td><xsl:value-of select="@name"/></td><td/><td/></tr>
                                <xsl:for-each select=".//@*">
                                    <tr><td/><td><xsl:value-of select="name()"/></td><td><xsl:value-of select="."/></td></tr>
                                </xsl:for-each>
                            </xsl:for-each> 
                        </table>
                        </div>
                        <p/>
                    </xsl:for-each>
                    
                
            </body>
        </html>
    </xsl:template>

    <!--xsl:template match="batch">

    </xsl:template>

    <xsl:template match="run">

    </xsl:template-->

</xsl:stylesheet>
