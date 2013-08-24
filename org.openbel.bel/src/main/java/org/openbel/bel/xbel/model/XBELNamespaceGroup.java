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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}namespace" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="defaultResourceLocation" type="{http://belframework.org/schema/1.0/xbel}resourceLocation" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "namespace"
})
@XmlRootElement(name = "namespaceGroup")
public class XBELNamespaceGroup
        extends JAXBElement
{

    protected List<XBELNamespace> namespace;
    @XmlAttribute(namespace = "http://belframework.org/schema/1.0/xbel")
    protected String defaultResourceLocation;

    /**
     * Gets the value of the namespace property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the namespace property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNamespace().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XBELNamespace }
     *
     *
     */
    public List<XBELNamespace> getNamespace() {
        if (namespace == null) {
            namespace = new ArrayList<XBELNamespace>();
        }
        return this.namespace;
    }

    public boolean isSetNamespace() {
        return ((this.namespace != null) && (!this.namespace.isEmpty()));
    }

    public void unsetNamespace() {
        this.namespace = null;
    }

    /**
     * Gets the value of the defaultResourceLocation property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDefaultResourceLocation() {
        return defaultResourceLocation;
    }

    /**
     * Sets the value of the defaultResourceLocation property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDefaultResourceLocation(String value) {
        this.defaultResourceLocation = value;
    }

    public boolean isSetDefaultResourceLocation() {
        return (this.defaultResourceLocation != null);
    }

}
