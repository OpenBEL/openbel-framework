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
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}name" minOccurs="0"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}comment" minOccurs="0"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}annotationGroup" minOccurs="0"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}statement" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}statementGroup" maxOccurs="unbounded" minOccurs="0"/>
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
        "name",
        "comment",
        "annotationGroup",
        "statement",
        "statementGroup"
})
@XmlRootElement(name = "statementGroup")
public class XBELStatementGroup
        extends JAXBElement
{

    protected String name;
    protected String comment;
    protected XBELAnnotationGroup annotationGroup;
    protected List<XBELStatement> statement;
    protected List<XBELStatementGroup> statementGroup;

    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    public boolean isSetName() {
        return (this.name != null);
    }

    /**
     * Gets the value of the comment property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setComment(String value) {
        this.comment = value;
    }

    public boolean isSetComment() {
        return (this.comment != null);
    }

    /**
     * Gets the value of the annotationGroup property.
     *
     * @return
     *     possible object is
     *     {@link XBELAnnotationGroup }
     *
     */
    public XBELAnnotationGroup getAnnotationGroup() {
        return annotationGroup;
    }

    /**
     * Sets the value of the annotationGroup property.
     *
     * @param value
     *     allowed object is
     *     {@link XBELAnnotationGroup }
     *
     */
    public void setAnnotationGroup(XBELAnnotationGroup value) {
        this.annotationGroup = value;
    }

    public boolean isSetAnnotationGroup() {
        return (this.annotationGroup != null);
    }

    /**
     * Gets the value of the statement property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statement property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatement().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XBELStatement }
     *
     *
     */
    public List<XBELStatement> getStatement() {
        if (statement == null) {
            statement = new ArrayList<XBELStatement>();
        }
        return this.statement;
    }

    public boolean isSetStatement() {
        return ((this.statement != null) && (!this.statement.isEmpty()));
    }

    public void unsetStatement() {
        this.statement = null;
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
