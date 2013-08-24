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
 *         &lt;element name="citationType" type="{http://belframework.org/ws/schemas}CitationType"/>
 *         &lt;element name="referenceIds" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="document" type="{http://belframework.org/ws/schemas}BelDocument" minOccurs="0"/>
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
        "citationType",
        "referenceIds",
        "document"
})
@XmlRootElement(name = "GetCitationsRequest")
public class GetCitationsRequest {

    @XmlElement(required = true)
    protected KamHandle handle;
    @XmlElement(required = true)
    protected CitationType citationType;
    @XmlElement(nillable = true)
    protected List<String> referenceIds;
    protected BelDocument document;

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
     * Gets the value of the citationType property.
     *
     * @return
     *     possible object is
     *     {@link CitationType }
     *
     */
    public CitationType getCitationType() {
        return citationType;
    }

    /**
     * Sets the value of the citationType property.
     *
     * @param value
     *     allowed object is
     *     {@link CitationType }
     *
     */
    public void setCitationType(CitationType value) {
        this.citationType = value;
    }

    /**
     * Gets the value of the referenceIds property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the referenceIds property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReferenceIds().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     *
     *
     */
    public List<String> getReferenceIds() {
        if (referenceIds == null) {
            referenceIds = new ArrayList<String>();
        }
        return this.referenceIds;
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
