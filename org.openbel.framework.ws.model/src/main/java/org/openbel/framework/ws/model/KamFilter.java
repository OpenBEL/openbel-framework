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
import javax.xml.bind.annotation.XmlType;

/**
 * TODO - Document
 *
 * <p>Java class for KamFilter complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="KamFilter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="annotationCriteria" type="{http://belframework.org/ws/schemas}AnnotationFilterCriteria" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="documentCriteria" type="{http://belframework.org/ws/schemas}BelDocumentFilterCriteria" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="citationCriteria" type="{http://belframework.org/ws/schemas}CitationFilterCriteria" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="relationshipCriteria" type="{http://belframework.org/ws/schemas}RelationshipTypeFilterCriteria" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KamFilter", propOrder = {
        "annotationCriteria",
        "documentCriteria",
        "citationCriteria",
        "relationshipCriteria"
})
public class KamFilter {

    protected List<AnnotationFilterCriteria> annotationCriteria;
    protected List<BelDocumentFilterCriteria> documentCriteria;
    protected List<CitationFilterCriteria> citationCriteria;
    protected List<RelationshipTypeFilterCriteria> relationshipCriteria;

    /**
     * Gets the value of the annotationCriteria property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the annotationCriteria property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnnotationCriteria().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AnnotationFilterCriteria }
     *
     *
     */
    public List<AnnotationFilterCriteria> getAnnotationCriteria() {
        if (annotationCriteria == null) {
            annotationCriteria = new ArrayList<AnnotationFilterCriteria>();
        }
        return this.annotationCriteria;
    }

    /**
     * Gets the value of the documentCriteria property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentCriteria property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumentCriteria().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BelDocumentFilterCriteria }
     *
     *
     */
    public List<BelDocumentFilterCriteria> getDocumentCriteria() {
        if (documentCriteria == null) {
            documentCriteria = new ArrayList<BelDocumentFilterCriteria>();
        }
        return this.documentCriteria;
    }

    /**
     * Gets the value of the citationCriteria property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the citationCriteria property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCitationCriteria().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CitationFilterCriteria }
     *
     *
     */
    public List<CitationFilterCriteria> getCitationCriteria() {
        if (citationCriteria == null) {
            citationCriteria = new ArrayList<CitationFilterCriteria>();
        }
        return this.citationCriteria;
    }

    /**
     * Gets the value of the relationshipCriteria property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the relationshipCriteria property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelationshipCriteria().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RelationshipTypeFilterCriteria }
     *
     *
     */
    public List<RelationshipTypeFilterCriteria> getRelationshipCriteria() {
        if (relationshipCriteria == null) {
            relationshipCriteria =
                    new ArrayList<RelationshipTypeFilterCriteria>();
        }
        return this.relationshipCriteria;
    }

}
