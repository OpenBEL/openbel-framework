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
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="handle" type="{http://belframework.org/ws/schemas}KamHandle" minOccurs="0"/>
 *         &lt;element name="loadStatus" type="{http://belframework.org/ws/schemas}KAMLoadStatus"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "handle",
        "loadStatus",
        "message"
})
@XmlRootElement(name = "LoadKamResponse")
public class LoadKamResponse {

    protected KamHandle handle;
    @XmlElement(required = true)
    protected KAMLoadStatus loadStatus;
    protected String message;

    /**
     * Gets the value of the handle property.
     *
     * @return
     *     possible object is
     *     {@link KamHandle }
     *
     */
    public KamHandle getHandle() {
        return handle;
    }

    /**
     * Sets the value of the handle property.
     *
     * @param value
     *     allowed object is
     *     {@link KamHandle }
     *
     */
    public void setHandle(KamHandle value) {
        this.handle = value;
    }

    /**
     * Gets the value of the loadStatus property.
     *
     * @return
     *     possible object is
     *     {@link KAMLoadStatus }
     *
     */
    public KAMLoadStatus getLoadStatus() {
        return loadStatus;
    }

    /**
     * Sets the value of the loadStatus property.
     *
     * @param value
     *     allowed object is
     *     {@link KAMLoadStatus }
     *
     */
    public void setLoadStatus(KAMLoadStatus value) {
        this.loadStatus = value;
    }

    /**
     * Gets the value of the message property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMessage(String value) {
        this.message = value;
    }

}
