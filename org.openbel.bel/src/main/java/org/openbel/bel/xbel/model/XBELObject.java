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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *       &lt;choice>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}term"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}statement"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "term",
        "statement"
})
@XmlRootElement(name = "object")
public class XBELObject
        extends JAXBElement
{

    protected XBELTerm term;
    protected XBELStatement statement;

    /**
     * Gets the value of the term property.
     *
     * @return
     *     possible object is
     *     {@link XBELTerm }
     *
     */
    public XBELTerm getTerm() {
        return term;
    }

    /**
     * Sets the value of the term property.
     *
     * @param value
     *     allowed object is
     *     {@link XBELTerm }
     *
     */
    public void setTerm(XBELTerm value) {
        this.term = value;
    }

    public boolean isSetTerm() {
        return (this.term != null);
    }

    /**
     * Gets the value of the statement property.
     *
     * @return
     *     possible object is
     *     {@link XBELStatement }
     *
     */
    public XBELStatement getStatement() {
        return statement;
    }

    /**
     * Sets the value of the statement property.
     *
     * @param value
     *     allowed object is
     *     {@link XBELStatement }
     *
     */
    public void setStatement(XBELStatement value) {
        this.statement = value;
    }

    public boolean isSetStatement() {
        return (this.statement != null);
    }

}
