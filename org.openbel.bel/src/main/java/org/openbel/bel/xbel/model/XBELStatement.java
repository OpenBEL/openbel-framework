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
