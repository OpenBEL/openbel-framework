/**
 *  Copyright 2013 OpenBEL Consortium
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
