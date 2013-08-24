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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * TODO - Document
 * <p>
 * Java class for FilterCriteria complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="FilterCriteria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="isInclude" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FilterCriteria")
@XmlSeeAlso({
        AnnotationFilterCriteria.class,
        FunctionReturnTypeFilterCriteria.class,
        FunctionTypeFilterCriteria.class,
        RelationshipTypeFilterCriteria.class,
        BelDocumentFilterCriteria.class,
        CitationFilterCriteria.class,
        NamespaceFilterCriteria.class
})
public class FilterCriteria {

    @XmlAttribute
    protected Boolean isInclude;

    /**
     * Gets the value of the isInclude property.
     *
     * @return possible object is {@link Boolean }
     */
    public boolean isIsInclude() {
        if (isInclude == null) {
            return true;
        }
        return isInclude;
    }

    /**
     * Sets the value of the isInclude property.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setIsInclude(Boolean value) {
        this.isInclude = value;
    }

}
