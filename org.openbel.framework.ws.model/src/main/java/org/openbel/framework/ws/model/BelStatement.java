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
import javax.xml.bind.annotation.XmlType;

/**
 * TODO - Document
 *
 * <p>Java class for BelStatement complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="BelStatement">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="subjectTerm" type="{http://belframework.org/ws/schemas}BelTerm"/>
 *         &lt;element name="relationship" type="{http://belframework.org/ws/schemas}RelationshipType" minOccurs="0"/>
 *         &lt;element name="objectTerm" type="{http://belframework.org/ws/schemas}BelTerm" minOccurs="0"/>
 *         &lt;element name="objectStatement" type="{http://belframework.org/ws/schemas}BelStatement" minOccurs="0"/>
 *         &lt;element name="annotations" type="{http://belframework.org/ws/schemas}Annotation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="citation" type="{http://belframework.org/ws/schemas}Citation" minOccurs="0"/>
 *         &lt;element name="document" type="{http://belframework.org/ws/schemas}BelDocument"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BelStatement", propOrder = {
        "id",
        "subjectTerm",
        "relationship",
        "objectTerm",
        "objectStatement",
        "annotations",
        "citation",
        "document"
})
public class BelStatement {

    @XmlElement(required = true)
    protected String id;
    @XmlElement(required = true)
    protected BelTerm subjectTerm;
    protected RelationshipType relationship;
    protected BelTerm objectTerm;
    protected BelStatement objectStatement;
    protected List<Annotation> annotations;
    protected Citation citation;
    @XmlElement(required = true)
    protected BelDocument document;

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

    /**
     * Gets the value of the subjectTerm property.
     *
     * @return
     *     possible object is
     *     {@link BelTerm }
     *
     */
    public BelTerm getSubjectTerm() {
        return subjectTerm;
    }

    /**
     * Sets the value of the subjectTerm property.
     *
     * @param value
     *     allowed object is
     *     {@link BelTerm }
     *
     */
    public void setSubjectTerm(BelTerm value) {
        this.subjectTerm = value;
    }

    /**
     * Gets the value of the relationship property.
     *
     * @return
     *     possible object is
     *     {@link RelationshipType }
     *
     */
    public RelationshipType getRelationship() {
        return relationship;
    }

    /**
     * Sets the value of the relationship property.
     *
     * @param value
     *     allowed object is
     *     {@link RelationshipType }
     *
     */
    public void setRelationship(RelationshipType value) {
        this.relationship = value;
    }

    /**
     * Gets the value of the objectTerm property.
     *
     * @return
     *     possible object is
     *     {@link BelTerm }
     *
     */
    public BelTerm getObjectTerm() {
        return objectTerm;
    }

    /**
     * Sets the value of the objectTerm property.
     *
     * @param value
     *     allowed object is
     *     {@link BelTerm }
     *
     */
    public void setObjectTerm(BelTerm value) {
        this.objectTerm = value;
    }

    /**
     * Gets the value of the objectStatement property.
     *
     * @return
     *     possible object is
     *     {@link BelStatement }
     *
     */
    public BelStatement getObjectStatement() {
        return objectStatement;
    }

    /**
     * Sets the value of the objectStatement property.
     *
     * @param value
     *     allowed object is
     *     {@link BelStatement }
     *
     */
    public void setObjectStatement(BelStatement value) {
        this.objectStatement = value;
    }

    /**
     * Gets the value of the annotations property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the annotations property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnnotations().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Annotation }
     *
     *
     */
    public List<Annotation> getAnnotations() {
        if (annotations == null) {
            annotations = new ArrayList<Annotation>();
        }
        return this.annotations;
    }

    /**
     * Gets the value of the citation property.
     *
     * @return
     *     possible object is
     *     {@link Citation }
     *
     */
    public Citation getCitation() {
        return citation;
    }

    /**
     * Sets the value of the citation property.
     *
     * @param value
     *     allowed object is
     *     {@link Citation }
     *
     */
    public void setCitation(Citation value) {
        this.citation = value;
    }

    /**
     * Gets the value of the document property.
     *
     * @return
     *     possible object is
     *     {@link BelDocument }
     *
     */
    public BelDocument getDocument() {
        return document;
    }

    /**
     * Sets the value of the document property.
     *
     * @param value
     *     allowed object is
     *     {@link BelDocument }
     *
     */
    public void setDocument(BelDocument value) {
        this.document = value;
    }

}
