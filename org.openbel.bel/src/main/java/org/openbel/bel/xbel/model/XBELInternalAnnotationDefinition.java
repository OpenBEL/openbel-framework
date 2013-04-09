/**
 * Copyright (C) 2012-2013 Selventa, Inc.
 *
 * This file is part of the OpenBEL Framework.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The OpenBEL Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the OpenBEL Framework. If not, see <http://www.gnu.org/licenses/>.
 *
 * Additional Terms under LGPL v3:
 *
 * This license does not authorize you and you are prohibited from using the
 * name, trademarks, service marks, logos or similar indicia of Selventa, Inc.,
 * or, in the discretion of other licensors or authors of the program, the
 * name, trademarks, service marks, logos or similar indicia of such authors or
 * licensors, in any marketing or advertising materials relating to your
 * distribution of the program or any covered product. This restriction does
 * not waive or limit your obligation to keep intact all copyright notices set
 * forth in the program as delivered to you.
 *
 * If you distribute the program in whole or in part, or any modified version
 * of the program, and you assume contractual liability to the recipient with
 * respect to the program or modified version, then you will indemnify the
 * authors and licensors of the program for any liabilities that these
 * contractual assumptions directly impose on those licensors and authors.
 */
package org.openbel.bel.xbel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}description"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}usage"/>
 *         &lt;choice>
 *           &lt;element ref="{http://belframework.org/schema/1.0/xbel}listAnnotation"/>
 *           &lt;element ref="{http://belframework.org/schema/1.0/xbel}patternAnnotation"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://belframework.org/schema/1.0/xbel}annotationDefinitionIdentifier" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "description",
        "usage",
        "listAnnotation",
        "patternAnnotation"
})
@XmlRootElement(name = "internalAnnotationDefinition")
public class XBELInternalAnnotationDefinition
        extends JAXBElement
{

    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    protected String usage;
    protected XBELListAnnotation listAnnotation;
    protected String patternAnnotation;
    @XmlAttribute(namespace = "http://belframework.org/schema/1.0/xbel", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String id;

    /**
     * Gets the value of the description property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDescription(String value) {
        this.description = value;
    }

    public boolean isSetDescription() {
        return (this.description != null);
    }

    /**
     * Gets the value of the usage property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Sets the value of the usage property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUsage(String value) {
        this.usage = value;
    }

    public boolean isSetUsage() {
        return (this.usage != null);
    }

    /**
     * Gets the value of the listAnnotation property.
     *
     * @return
     *     possible object is
     *     {@link XBELListAnnotation }
     *
     */
    public XBELListAnnotation getListAnnotation() {
        return listAnnotation;
    }

    /**
     * Sets the value of the listAnnotation property.
     *
     * @param value
     *     allowed object is
     *     {@link XBELListAnnotation }
     *
     */
    public void setListAnnotation(XBELListAnnotation value) {
        this.listAnnotation = value;
    }

    public boolean isSetListAnnotation() {
        return (this.listAnnotation != null);
    }

    /**
     * Gets the value of the patternAnnotation property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPatternAnnotation() {
        return patternAnnotation;
    }

    /**
     * Sets the value of the patternAnnotation property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPatternAnnotation(String value) {
        this.patternAnnotation = value;
    }

    public boolean isSetPatternAnnotation() {
        return (this.patternAnnotation != null);
    }

    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setId(String value) {
        this.id = value;
    }

    public boolean isSetId() {
        return (this.id != null);
    }

}
