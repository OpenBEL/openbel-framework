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
 * <p>Java class for BelDocumentFilterCriteria complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="BelDocumentFilterCriteria">
 *   &lt;complexContent>
 *     &lt;extension base="{http://belframework.org/ws/schemas}FilterCriteria">
 *       &lt;sequence>
 *         &lt;element name="valueSet" type="{http://belframework.org/ws/schemas}BelDocument" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BelDocumentFilterCriteria", propOrder = {
        "valueSet"
})
public class BelDocumentFilterCriteria
        extends FilterCriteria
{

    @XmlElement(required = true)
    protected List<BelDocument> valueSet;

    /**
     * Gets the value of the valueSet property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the valueSet property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValueSet().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BelDocument }
     *
     *
     */
    public List<BelDocument> getValueSet() {
        if (valueSet == null) {
            valueSet = new ArrayList<BelDocument>();
        }
        return this.valueSet;
    }

}
