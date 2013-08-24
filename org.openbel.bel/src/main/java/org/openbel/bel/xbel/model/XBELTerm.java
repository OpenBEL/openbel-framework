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
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}parameter"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}term"/>
 *       &lt;/choice>
 *       &lt;attribute name="function" use="required" type="{http://belframework.org/schema/1.0/xbel}function" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "parameterOrTerm"
})
@XmlRootElement(name = "term")
public class XBELTerm
        extends JAXBElement
{

    @XmlElements({
            @XmlElement(name = "parameter", type = XBELParameter.class),
            @XmlElement(name = "term", type = XBELTerm.class)
    })
    protected List<JAXBElement> parameterOrTerm;
    @XmlAttribute(namespace = "http://belframework.org/schema/1.0/xbel", required = true)
    protected Function function;

    /**
     * Gets the value of the parameterOrTerm property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameterOrTerm property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameterOrTerm().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XBELParameter }
     * {@link XBELTerm }
     *
     *
     */
    public List<JAXBElement> getParameterOrTerm() {
        if (parameterOrTerm == null) {
            parameterOrTerm = new ArrayList<JAXBElement>();
        }
        return this.parameterOrTerm;
    }

    public boolean isSetParameterOrTerm() {
        return ((this.parameterOrTerm != null) && (!this.parameterOrTerm
                .isEmpty()));
    }

    public void unsetParameterOrTerm() {
        this.parameterOrTerm = null;
    }

    /**
     * Gets the value of the function property.
     *
     * @return
     *     possible object is
     *     {@link Function }
     *
     */
    public Function getFunction() {
        return function;
    }

    /**
     * Sets the value of the function property.
     *
     * @param value
     *     allowed object is
     *     {@link Function }
     *
     */
    public void setFunction(Function value) {
        this.function = value;
    }

    public boolean isSetFunction() {
        return (this.function != null);
    }

}
