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
 * <p>Java class for NodeFilter complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="NodeFilter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="functionTypeCriteria" type="{http://belframework.org/ws/schemas}FunctionTypeFilterCriteria" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="functionReturnCriteria" type="{http://belframework.org/ws/schemas}FunctionReturnTypeFilterCriteria" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodeFilter", propOrder = {
        "functionTypeCriteria",
        "functionReturnCriteria"
})
public class NodeFilter {

    protected List<FunctionTypeFilterCriteria> functionTypeCriteria;
    protected List<FunctionReturnTypeFilterCriteria> functionReturnCriteria;

    /**
     * Gets the value of the functionTypeCriteria property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the functionTypeCriteria property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFunctionTypeCriteria().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FunctionTypeFilterCriteria }
     *
     *
     */
    public List<FunctionTypeFilterCriteria> getFunctionTypeCriteria() {
        if (functionTypeCriteria == null) {
            functionTypeCriteria = new ArrayList<FunctionTypeFilterCriteria>();
        }
        return this.functionTypeCriteria;
    }

    /**
     * Gets the value of the functionReturnCriteria property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the functionReturnCriteria property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFunctionReturnCriteria().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FunctionReturnTypeFilterCriteria }
     *
     *
     */
    public List<FunctionReturnTypeFilterCriteria> getFunctionReturnCriteria() {
        if (functionReturnCriteria == null) {
            functionReturnCriteria =
                    new ArrayList<FunctionReturnTypeFilterCriteria>();
        }
        return this.functionReturnCriteria;
    }

}
