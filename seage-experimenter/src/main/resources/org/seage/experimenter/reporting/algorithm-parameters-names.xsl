<?xml version="1.0" encoding="windows-1250"?>

<!--
    Document   : algorithm-parameters.xsl
    Created on : 15. duben 2012, 20:58
    Author     : zmatlja1
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>

    <xsl:template name="GetParametresByAlgorithmAndPosition">
        <xsl:param name="algorithm" />
        <xsl:param name="position" />
        
        <xsl:variable name="newPosition">        
            <xsl:choose>
                <xsl:when test="$position mod 2 != 0">                  
                <xsl:value-of select="$position+1" />
                </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$position" />
            </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:choose>
            <xsl:when test="$algorithm='SimulatedAnnealing'"><xsl:call-template name="SimulatedAnnealing"><xsl:with-param name="position" select="$newPosition" /></xsl:call-template></xsl:when>
            <xsl:when test="$algorithm='TabuSearch'"><xsl:call-template name="TabuSearch"><xsl:with-param name="position" select="$newPosition" /></xsl:call-template></xsl:when>
            <xsl:when test="$algorithm='GeneticAlgorithm'"><xsl:call-template name="GeneticAlgorithm"><xsl:with-param name="position" select="$newPosition" /></xsl:call-template></xsl:when>
<!--            <xsl:when test="$algorithm='FireflySearch'"><xsl:call-template name="FireflySearch"><xsl:with-param name="position" select="$newPosition" /></xsl:call-template></xsl:when>
            <xsl:when test="$algorithm='AntColony'"><xsl:call-template name="AntColony"><xsl:with-param name="position" select="$newPosition" /></xsl:call-template></xsl:when>
            <xsl:when test="$algorithm='HillClimber'"><xsl:call-template name="HillClimber"><xsl:with-param name="position" select="$newPosition" /></xsl:call-template></xsl:when>
            <xsl:when test="$algorithm='ParticleSwarm'"><xsl:call-template name="ParticleSwarm"><xsl:with-param name="position" select="$newPosition" /></xsl:call-template></xsl:when>-->
        </xsl:choose>            
    </xsl:template>
    
    <xsl:template name="SimulatedAnnealing">
        <xsl:param name="position" />

        <xsl:choose>
            <xsl:when test="$position=6">annealCoeficient</xsl:when>
            <xsl:when test="$position=8">maxInnerIterations</xsl:when>
            <xsl:when test="$position=10">maxTemperature</xsl:when>
            <xsl:when test="$position=12">minTemperature</xsl:when>
            <xsl:when test="$position=14">numInnerSuccesses</xsl:when>
            <xsl:when test="$position=16">numSolutions</xsl:when>
        </xsl:choose>            
    </xsl:template>
    
    <xsl:template name="TabuSearch">
        <xsl:param name="position" />
        <xsl:choose>
            <xsl:when test="$position=6">numIterDivers</xsl:when>
            <xsl:when test="$position=8">numIteration</xsl:when>
            <xsl:when test="$position=10">numSolutions</xsl:when>
            <xsl:when test="$position=12">tabuListLength</xsl:when>
        </xsl:choose>            
    </xsl:template>
    
    <xsl:template name="GeneticAlgorithm">
        <xsl:param name="position" />
        <xsl:choose>
            <xsl:when test="$position=6">crossLengthPct</xsl:when>
            <xsl:when test="$position=8">eliteSubjectPct</xsl:when>
            <xsl:when test="$position=10">iterationCount</xsl:when>
            <xsl:when test="$position=12">mutateLengthPct</xsl:when>
            <xsl:when test="$position=14">mutateSubjectPct</xsl:when>
            <xsl:when test="$position=16">numSolutions</xsl:when>
            <xsl:when test="$position=18">randomSubjectPct</xsl:when>
        </xsl:choose>            
    </xsl:template>

</xsl:stylesheet>
