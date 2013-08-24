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
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}comment" minOccurs="0"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}annotationGroup" minOccurs="0"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}subject"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}object" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="relationship" type="{http://belframework.org/schema/1.0/xbel}relationship" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "comment",
        "annotationGroup",
        "subject",
        "object"
})
@XmlRootElement(name = "statement")
public class XBELStatement
        extends JAXBElement
{

    protected String comment;
    protected XBELAnnotationGroup annotationGroup;
    @XmlElement(required = true)
    protected XBELSubject subject;
    protected XBELObject object;
    @XmlAttribute(namespace = "http://belframework.org/schema/1.0/xbel")
    protected Relationship relationship;

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
     * Gets the value of the subject property.
     *
     * @return
     *     possible object is
     *     {@link XBELSubject }
     *
     */
    public XBELSubject getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     *
     * @param value
     *     allowed object is
     *     {@link XBELSubject }
     *
     */
    public void setSubject(XBELSubject value) {
        this.subject = value;
    }

    public boolean isSetSubject() {
        return (this.subject != null);
    }

    /**
     * Gets the value of the object property.
     *
     * @return
     *     possible object is
     *     {@link XBELObject }
     *
     */
    public XBELObject getObject() {
        return object;
    }

    /**
     * Sets the value of the object property.
     *
     * @param value
     *     allowed object is
     *     {@link XBELObject }
     *
     */
    public void setObject(XBELObject value) {
        this.object = value;
    }

    public boolean isSetObject() {
        return (this.object != null);
    }

    /**
     * Gets the value of the relationship property.
     *
     * @return
     *     possible object is
     *     {@link Relationship }
     *
     */
    public Relationship getRelationship() {
        return relationship;
    }

    /**
     * Sets the value of the relationship property.
     *
     * @param value
     *     allowed object is
     *     {@link Relationship }
     *
     */
    public void setRelationship(Relationship value) {
        this.relationship = value;
    }

    public boolean isSetRelationship() {
        return (this.relationship != null);
    }

}
