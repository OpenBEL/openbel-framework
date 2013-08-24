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
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}header"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}namespaceGroup" minOccurs="0"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}annotationDefinitionGroup" minOccurs="0"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}statementGroup" maxOccurs="unbounded"/>
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
        "header",
        "namespaceGroup",
        "annotationDefinitionGroup",
        "statementGroup"
})
@XmlRootElement(name = "document")
public class XBELDocument
        extends JAXBElement
{

    @XmlElement(required = true)
    protected XBELHeader header;
    protected XBELNamespaceGroup namespaceGroup;
    protected XBELAnnotationDefinitionGroup annotationDefinitionGroup;
    @XmlElement(required = true)
    protected List<XBELStatementGroup> statementGroup;

    /**
     * Gets the value of the header property.
     *
     * @return
     *     possible object is
     *     {@link XBELHeader }
     *
     */
    public XBELHeader getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     *
     * @param value
     *     allowed object is
     *     {@link XBELHeader }
     *
     */
    public void setHeader(XBELHeader value) {
        this.header = value;
    }

    public boolean isSetHeader() {
        return (this.header != null);
    }

    /**
     * Gets the value of the namespaceGroup property.
     *
     * @return
     *     possible object is
     *     {@link XBELNamespaceGroup }
     *
     */
    public XBELNamespaceGroup getNamespaceGroup() {
        return namespaceGroup;
    }

    /**
     * Sets the value of the namespaceGroup property.
     *
     * @param value
     *     allowed object is
     *     {@link XBELNamespaceGroup }
     *
     */
    public void setNamespaceGroup(XBELNamespaceGroup value) {
        this.namespaceGroup = value;
    }

    public boolean isSetNamespaceGroup() {
        return (this.namespaceGroup != null);
    }

    /**
     * Gets the value of the annotationDefinitionGroup property.
     *
     * @return
     *     possible object is
     *     {@link XBELAnnotationDefinitionGroup }
     *
     */
    public XBELAnnotationDefinitionGroup getAnnotationDefinitionGroup() {
        return annotationDefinitionGroup;
    }

    /**
     * Sets the value of the annotationDefinitionGroup property.
     *
     * @param value
     *     allowed object is
     *     {@link XBELAnnotationDefinitionGroup }
     *
     */
    public void
            setAnnotationDefinitionGroup(XBELAnnotationDefinitionGroup value) {
        this.annotationDefinitionGroup = value;
    }

    public boolean isSetAnnotationDefinitionGroup() {
        return (this.annotationDefinitionGroup != null);
    }

    /**
     * Gets the value of the statementGroup property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statementGroup property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatementGroup().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XBELStatementGroup }
     *
     *
     */
    public List<XBELStatementGroup> getStatementGroup() {
        if (statementGroup == null) {
            statementGroup = new ArrayList<XBELStatementGroup>();
        }
        return this.statementGroup;
    }

    public boolean isSetStatementGroup() {
        return ((this.statementGroup != null) && (!this.statementGroup
                .isEmpty()));
    }

    public void unsetStatementGroup() {
        this.statementGroup = null;
    }

}
