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
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element name="sources" type="{http://belframework.org/ws/schemas}KamNode" maxOccurs="unbounded" minOccurs="2"/>
 *         &lt;element name="dialect" type="{http://belframework.org/ws/schemas}DialectHandle" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="maxDepth" type="{http://www.w3.org/2001/XMLSchema}int" default="4" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "sources", "dialect" })
@XmlRootElement(name = "InterconnectRequest")
public class InterconnectRequest {

    @XmlElement(required = true)
    protected List<KamNode> sources;
    protected DialectHandle dialect;
    @XmlAttribute
    protected Integer maxDepth;

    /**
     * Gets the value of the sources property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the sources property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getSources().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link KamNode }
     *
     *
     */
    public List<KamNode> getSources() {
        if (sources == null) {
            sources = new ArrayList<KamNode>();
        }
        return this.sources;
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

    /**
     * Gets the value of the maxDepth property.
     *
     * @return possible object is {@link Integer }
     *
     */
    public int getMaxDepth() {
        if (maxDepth == null) {
            return 4;
        }
        return maxDepth;
    }

    /**
     * Sets the value of the maxDepth property.
     *
     * @param value
     *            allowed object is {@link Integer }
     *
     */
    public void setMaxDepth(Integer value) {
        this.maxDepth = value;
    }

}
