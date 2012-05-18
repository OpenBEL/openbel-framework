<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:exslt="http://exslt.org/common"
                xmlns:bel="http://belframework.org/schema/1.0/xbel"
                version="1.0">
  <xsl:output method="xml" indent="no" />

  <!-- Copy elements -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" />
    </xsl:copy>
  </xsl:template>

  <!-- Normalize the whitespace in all text nodes -->
  <xsl:template match="text()" priority="2">
    <xsl:value-of select="normalize-space()" />
  </xsl:template>

  <!-- Remove blank citation comments -->
  <xsl:template match="bel:citation/bel:comment[not(normalize-space())]" priority="1" />

  <!-- Remove blank statement groups -->
  <xsl:template match="bel:document//bel:statementGroup[not(@* or node() or normalize-space())]"
                priority="1" />

  <!-- Remove bel:description and bel:usage elements -->
  <xsl:template match="bel:document/bel:annotationDefinitionGroup/bel:internalAnnotationDefinition/node()[self::bel:description or self::bel:usage]"
                priority="1" />

  <!-- Collapse statement groups into the top-level bel:statementGroup elements -->
  <xsl:template match="bel:document/bel:statementGroup//bel:statementGroup"
                priority="3">
    <xsl:apply-templates select="@*|node()" />
  </xsl:template>

  <!-- Remove bel:name elements -->
  <xsl:template match="/bel:document/bel:statementGroup/bel:name" priority="1" />

  <!-- Remove bel:annotationGroup elements of all bel:statementGroup elements -->
  <xsl:template match="/bel:document//bel:statementGroup/bel:annotationGroup"
                priority="3" />
</xsl:stylesheet>
