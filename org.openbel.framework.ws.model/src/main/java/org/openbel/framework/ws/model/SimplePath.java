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
 * <p>Java class for SimplePath complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SimplePath">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="source" type="{http://belframework.org/ws/schemas}KamNode"/>
 *         &lt;element name="target" type="{http://belframework.org/ws/schemas}KamNode"/>
 *         &lt;element name="edges" type="{http://belframework.org/ws/schemas}KamEdge" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SimplePath", propOrder = {
        "source",
        "target",
        "edges"
})
public class SimplePath {

    @XmlElement(required = true)
    protected KamNode source;
    @XmlElement(required = true)
    protected KamNode target;
    protected List<KamEdge> edges;

    /**
     * Gets the value of the source property.
     *
     * @return
     *     possible object is
     *     {@link KamNode }
     *
     */
    public KamNode getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     *
     * @param value
     *     allowed object is
     *     {@link KamNode }
     *
     */
    public void setSource(KamNode value) {
        this.source = value;
    }

    /**
     * Gets the value of the target property.
     *
     * @return
     *     possible object is
     *     {@link KamNode }
     *
     */
    public KamNode getTarget() {
        return target;
    }

    /**
     * Sets the value of the target property.
     *
     * @param value
     *     allowed object is
     *     {@link KamNode }
     *
     */
    public void setTarget(KamNode value) {
        this.target = value;
    }

    /**
     * Gets the value of the edges property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the edges property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEdges().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KamEdge }
     *
     *
     */
    public List<KamEdge> getEdges() {
        if (edges == null) {
            edges = new ArrayList<KamEdge>();
        }
        return this.edges;
    }

}
