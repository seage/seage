<?xml version="1.0" encoding="windows-1250"?>

<!--
    Document   : milis-to-sec.xsl
    Created on : 15. duben 2012, 21:10
    Author     : zmatlja1
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>

    <xsl:template name="MillisecondsToSeconds">
        <xsl:param name="miliseconds" />        
        <xsl:value-of select="$miliseconds div 1000" />                  
    </xsl:template>

</xsl:stylesheet>
