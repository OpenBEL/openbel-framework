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
import javax.xml.bind.annotation.XmlElements;
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
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element ref="{http://belframework.org/schema/1.0/xbel}annotation" maxOccurs="unbounded"/>
 *           &lt;element ref="{http://belframework.org/schema/1.0/xbel}evidence" minOccurs="0"/>
 *           &lt;element ref="{http://belframework.org/schema/1.0/xbel}citation" minOccurs="0"/>
 *         &lt;/choice>
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
        "annotationOrEvidenceOrCitation"
})
@XmlRootElement(name = "annotationGroup")
public class XBELAnnotationGroup
        extends JAXBElement
{

    @XmlElements({
            @XmlElement(name = "annotation", type = XBELAnnotation.class),
            @XmlElement(name = "citation", type = XBELCitation.class),
            @XmlElement(name = "evidence", type = String.class)
    })
    protected List<Object> annotationOrEvidenceOrCitation;

    /**
     * Gets the value of the annotationOrEvidenceOrCitation property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the annotationOrEvidenceOrCitation property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnnotationOrEvidenceOrCitation().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XBELAnnotation }
     * {@link XBELCitation }
     * {@link String }
     *
     *
     */
    public List<Object> getAnnotationOrEvidenceOrCitation() {
        if (annotationOrEvidenceOrCitation == null) {
            annotationOrEvidenceOrCitation = new ArrayList<Object>();
        }
        return this.annotationOrEvidenceOrCitation;
    }

    public boolean isSetAnnotationOrEvidenceOrCitation() {
        return ((this.annotationOrEvidenceOrCitation != null) && (!this.annotationOrEvidenceOrCitation
                .isEmpty()));
    }

    public void unsetAnnotationOrEvidenceOrCitation() {
        this.annotationOrEvidenceOrCitation = null;
    }

}
