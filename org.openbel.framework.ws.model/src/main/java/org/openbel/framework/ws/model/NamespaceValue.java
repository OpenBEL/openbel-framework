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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * TODO - Document
 *
 * <p>Java class for NamespaceValue complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="NamespaceValue">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="namespace" type="{http://belframework.org/ws/schemas}Namespace"/>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="equivalence" type="{http://belframework.org/ws/schemas}EquivalenceId" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NamespaceValue", propOrder = {
        "namespace",
        "value",
        "equivalence"
})
public class NamespaceValue {

    @XmlElement(required = true)
    protected Namespace namespace;
    @XmlElement(required = true)
    protected String value;
    protected EquivalenceId equivalence;

    /**
     * Gets the value of the namespace property.
     *
     * @return
     *     possible object is
     *     {@link Namespace }
     *
     */
    public Namespace getNamespace() {
        return namespace;
    }

    /**
     * Sets the value of the namespace property.
     *
     * @param value
     *     allowed object is
     *     {@link Namespace }
     *
     */
    public void setNamespace(Namespace value) {
        this.namespace = value;
    }

    /**
     * Gets the value of the value property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the equivalence property.
     *
     * @return
     *     possible object is
     *     {@link EquivalenceId }
     *
     */
    public EquivalenceId getEquivalence() {
        return equivalence;
    }

    /**
     * Sets the value of the equivalence property.
     *
     * @param value
     *     allowed object is
     *     {@link EquivalenceId }
     *
     */
    public void setEquivalence(EquivalenceId value) {
        this.equivalence = value;
    }

}
