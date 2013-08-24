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
 *         &lt;element name="kam" type="{http://belframework.org/ws/schemas}KamHandle"/>
 *         &lt;element name="geneNamespaces" type="{http://belframework.org/ws/schemas}Namespace" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="bpNamespaces" type="{http://belframework.org/ws/schemas}Namespace" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="chemNamespaces" type="{http://belframework.org/ws/schemas}Namespace" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="syntax" type="{http://belframework.org/ws/schemas}BelSyntax"/>
 *         &lt;element name="hideNamespacePrefixes" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "kam",
        "geneNamespaces",
        "bpNamespaces",
        "chemNamespaces",
        "syntax",
        "hideNamespacePrefixes"
})
@XmlRootElement(name = "GetCustomDialectRequest")
public class GetCustomDialectRequest {

    @XmlElement(required = true)
    protected KamHandle kam;
    protected List<Namespace> geneNamespaces;
    protected List<Namespace> bpNamespaces;
    protected List<Namespace> chemNamespaces;
    @XmlElement(required = true, defaultValue = "SHORT_FORM")
    protected BelSyntax syntax;
    @XmlElement(defaultValue = "true")
    protected boolean hideNamespacePrefixes;

    /**
     * Gets the value of the kam property.
     *
     * @return
     *     possible object is
     *     {@link KamHandle }
     *
     */
    public KamHandle getKam() {
        return kam;
    }

    /**
     * Sets the value of the kam property.
     *
     * @param value
     *     allowed object is
     *     {@link KamHandle }
     *
     */
    public void setKam(KamHandle value) {
        this.kam = value;
    }

    /**
     * Gets the value of the geneNamespaces property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the geneNamespaces property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGeneNamespaces().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Namespace }
     *
     *
     */
    public List<Namespace> getGeneNamespaces() {
        if (geneNamespaces == null) {
            geneNamespaces = new ArrayList<Namespace>();
        }
        return this.geneNamespaces;
    }

    /**
     * Gets the value of the bpNamespaces property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bpNamespaces property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBpNamespaces().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Namespace }
     *
     *
     */
    public List<Namespace> getBpNamespaces() {
        if (bpNamespaces == null) {
            bpNamespaces = new ArrayList<Namespace>();
        }
        return this.bpNamespaces;
    }

    /**
     * Gets the value of the chemNamespaces property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the chemNamespaces property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChemNamespaces().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Namespace }
     *
     *
     */
    public List<Namespace> getChemNamespaces() {
        if (chemNamespaces == null) {
            chemNamespaces = new ArrayList<Namespace>();
        }
        return this.chemNamespaces;
    }

    /**
     * Gets the value of the syntax property.
     *
     * @return
     *     possible object is
     *     {@link BelSyntax }
     *
     */
    public BelSyntax getSyntax() {
        return syntax;
    }

    /**
     * Sets the value of the syntax property.
     *
     * @param value
     *     allowed object is
     *     {@link BelSyntax }
     *
     */
    public void setSyntax(BelSyntax value) {
        this.syntax = value;
    }

    /**
     * Gets the value of the hideNamespacePrefixes property.
     *
     */
    public boolean isHideNamespacePrefixes() {
        return hideNamespacePrefixes;
    }

    /**
     * Sets the value of the hideNamespacePrefixes property.
     *
     */
    public void setHideNamespacePrefixes(boolean value) {
        this.hideNamespacePrefixes = value;
    }

}
