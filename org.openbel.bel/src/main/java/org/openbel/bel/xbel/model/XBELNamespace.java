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
 *       &lt;attribute name="prefix" use="required" type="{http://belframework.org/schema/1.0/xbel}prefix" />
 *       &lt;attribute name="resourceLocation" use="required" type="{http://belframework.org/schema/1.0/xbel}resourceLocation" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "namespace")
public class XBELNamespace
        extends JAXBElement
{

    @XmlAttribute(namespace = "http://belframework.org/schema/1.0/xbel", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String prefix;
    @XmlAttribute(namespace = "http://belframework.org/schema/1.0/xbel", required = true)
    protected String resourceLocation;

    /**
     * Gets the value of the prefix property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the value of the prefix property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPrefix(String value) {
        this.prefix = value;
    }

    public boolean isSetPrefix() {
        return (this.prefix != null);
    }

    /**
     * Gets the value of the resourceLocation property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getResourceLocation() {
        return resourceLocation;
    }

    /**
     * Sets the value of the resourceLocation property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setResourceLocation(String value) {
        this.resourceLocation = value;
    }

    public boolean isSetResourceLocation() {
        return (this.resourceLocation != null);
    }

}
