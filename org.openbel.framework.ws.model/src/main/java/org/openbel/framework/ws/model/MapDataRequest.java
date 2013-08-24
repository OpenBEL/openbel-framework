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
package org.openbel.framework.ws.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="handle" type="{http://belframework.org/ws/schemas}KamHandle"/>
 *         &lt;element name="namespace" type="{http://belframework.org/ws/schemas}Namespace"/>
 *         &lt;element name="values" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *         &lt;element name="nodeFilter" type="{http://belframework.org/ws/schemas}NodeFilter" minOccurs="0"/>
 *         &lt;element name="dialect" type="{http://belframework.org/ws/schemas}DialectHandle" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "handle",
        "namespace",
        "values",
        "nodeFilter",
        "dialect"
})
@XmlRootElement(name = "MapDataRequest")
public class MapDataRequest {

    @XmlElement(required = true)
    protected KamHandle handle;
    @XmlElement(required = true)
    protected Namespace namespace;
    @XmlElement(required = true)
    protected List<String> values;
    protected NodeFilter nodeFilter;
    protected DialectHandle dialect;

    /**
     * Gets the value of the handle property.
     *
     * @return
     *     possible object is
     *     {@link KamHandle }
     *
     */
    public KamHandle getHandle() {
        return handle;
    }

    /**
     * Sets the value of the handle property.
     *
     * @param value
     *     allowed object is
     *     {@link KamHandle }
     *
     */
    public void setHandle(KamHandle value) {
        this.handle = value;
    }

    /**
     * Gets the value of the namespace property.
     *
     * @return
     *     possible object is
     *     {@link Namespace }
     *
     */
    public Namespace getNamespace() {
        return namespace;
    }

    /**
     * Sets the value of the namespace property.
     *
     * @param value
     *     allowed object is
     *     {@link Namespace }
     *
     */
    public void setNamespace(Namespace value) {
        this.namespace = value;
    }

    /**
     * Gets the value of the values property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the values property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValues().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     *
     *
     */
    public List<String> getValues() {
        if (values == null) {
            values = new ArrayList<String>();
        }
        return this.values;
    }

    /**
     * Gets the value of the nodeFilter property.
     *
     * @return
     *     possible object is
     *     {@link NodeFilter }
     *
     */
    public NodeFilter getNodeFilter() {
        return nodeFilter;
    }

    /**
     * Sets the value of the nodeFilter property.
     *
     * @param value
     *     allowed object is
     *     {@link NodeFilter }
     *
     */
    public void setNodeFilter(NodeFilter value) {
        this.nodeFilter = value;
    }

    /**
     * Gets the value of the dialect property.
     *
     * @return
     *     possible object is
     *     {@link DialectHandle }
     *
     */
    public DialectHandle getDialect() {
        return dialect;
    }

    /**
     * Sets the value of the dialect property.
     *
     * @param value
     *     allowed object is
     *     {@link DialectHandle }
     *
     */
    public void setDialect(DialectHandle value) {
        this.dialect = value;
    }

}
