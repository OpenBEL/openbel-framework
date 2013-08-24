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
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}internalAnnotationDefinition" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}externalAnnotationDefinition" maxOccurs="unbounded" minOccurs="0"/>
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
        "internalAnnotationDefinition",
        "externalAnnotationDefinition"
})
@XmlRootElement(name = "annotationDefinitionGroup")
public class XBELAnnotationDefinitionGroup
        extends JAXBElement
{

    protected List<XBELInternalAnnotationDefinition> internalAnnotationDefinition;
    protected List<XBELExternalAnnotationDefinition> externalAnnotationDefinition;

    /**
     * Gets the value of the internalAnnotationDefinition property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the internalAnnotationDefinition property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInternalAnnotationDefinition().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XBELInternalAnnotationDefinition }
     *
     *
     */
    public List<XBELInternalAnnotationDefinition>
            getInternalAnnotationDefinition() {
        if (internalAnnotationDefinition == null) {
            internalAnnotationDefinition =
                    new ArrayList<XBELInternalAnnotationDefinition>();
        }
        return this.internalAnnotationDefinition;
    }

    public boolean isSetInternalAnnotationDefinition() {
        return ((this.internalAnnotationDefinition != null) && (!this.internalAnnotationDefinition
                .isEmpty()));
    }

    public void unsetInternalAnnotationDefinition() {
        this.internalAnnotationDefinition = null;
    }

    /**
     * Gets the value of the externalAnnotationDefinition property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the externalAnnotationDefinition property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExternalAnnotationDefinition().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XBELExternalAnnotationDefinition }
     *
     *
     */
    public List<XBELExternalAnnotationDefinition>
            getExternalAnnotationDefinition() {
        if (externalAnnotationDefinition == null) {
            externalAnnotationDefinition =
                    new ArrayList<XBELExternalAnnotationDefinition>();
        }
        return this.externalAnnotationDefinition;
    }

    public boolean isSetExternalAnnotationDefinition() {
        return ((this.externalAnnotationDefinition != null) && (!this.externalAnnotationDefinition
                .isEmpty()));
    }

    public void unsetExternalAnnotationDefinition() {
        this.externalAnnotationDefinition = null;
    }

}
