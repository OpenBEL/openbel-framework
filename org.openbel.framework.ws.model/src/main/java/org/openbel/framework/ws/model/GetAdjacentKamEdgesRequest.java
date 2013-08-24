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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="kamNode" type="{http://belframework.org/ws/schemas}KamNode"/>
 *         &lt;element name="direction" type="{http://belframework.org/ws/schemas}EdgeDirectionType" minOccurs="0"/>
 *         &lt;element name="filter" type="{http://belframework.org/ws/schemas}EdgeFilter" minOccurs="0"/>
 *         &lt;element name="dialect" type="{http://belframework.org/ws/schemas}DialectHandle" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "kamNode", "direction", "filter", "dialect" })
@XmlRootElement(name = "GetAdjacentKamEdgesRequest")
public class GetAdjacentKamEdgesRequest {

    @XmlElement(required = true)
    protected KamNode kamNode;
    protected EdgeDirectionType direction;
    protected EdgeFilter filter;
    protected DialectHandle dialect;

    /**
     * Gets the value of the kamNode property.
     *
     * @return possible object is {@link KamNode }
     *
     */
    public KamNode getKamNode() {
        return kamNode;
    }

    /**
     * Sets the value of the kamNode property.
     *
     * @param value
     *            allowed object is {@link KamNode }
     *
     */
    public void setKamNode(KamNode value) {
        this.kamNode = value;
    }

    /**
     * Gets the value of the direction property.
     *
     * @return possible object is {@link EdgeDirectionType }
     *
     */
    public EdgeDirectionType getDirection() {
        return direction;
    }

    /**
     * Sets the value of the direction property.
     *
     * @param value
     *            allowed object is {@link EdgeDirectionType }
     *
     */
    public void setDirection(EdgeDirectionType value) {
        this.direction = value;
    }

    /**
     * Gets the value of the filter property.
     *
     * @return possible object is {@link EdgeFilter }
     *
     */
    public EdgeFilter getFilter() {
        return filter;
    }

    /**
     * Sets the value of the filter property.
     *
     * @param value
     *            allowed object is {@link EdgeFilter }
     *
     */
    public void setFilter(EdgeFilter value) {
        this.filter = value;
    }

    /**
     * Gets the value of the dialect property.
     *
     * @return possible object is {@link DialectHandle }
     *
     */
    public DialectHandle getDialect() {
        return dialect;
    }

    /**
     * Sets the value of the dialect property.
     *
     * @param value
     *            allowed object is {@link DialectHandle }
     *
     */
    public void setDialect(DialectHandle value) {
        this.dialect = value;
    }

}
