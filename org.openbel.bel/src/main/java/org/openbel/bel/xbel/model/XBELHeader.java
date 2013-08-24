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
 *       &lt;all>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}name"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}description"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}version"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}copyright" minOccurs="0"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}disclaimer" minOccurs="0"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}contactInfo" minOccurs="0"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}authorGroup" minOccurs="0"/>
 *         &lt;element ref="{http://belframework.org/schema/1.0/xbel}licenseGroup" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

        })
@XmlRootElement(name = "header")
public class XBELHeader
        extends JAXBElement
{

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    protected String version;
    protected String copyright;
    protected String disclaimer;
    protected String contactInfo;
    protected XBELAuthorGroup authorGroup;
    protected XBELLicenseGroup licenseGroup;

    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    public boolean isSetName() {
        return (this.name != null);
    }

    /**
     * Gets the value of the description property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDescription(String value) {
        this.description = value;
    }

    public boolean isSetDescription() {
        return (this.description != null);
    }

    /**
     * Gets the value of the version property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setVersion(String value) {
        this.version = value;
    }

    public boolean isSetVersion() {
        return (this.version != null);
    }

    /**
     * Gets the value of the copyright property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * Sets the value of the copyright property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCopyright(String value) {
        this.copyright = value;
    }

    public boolean isSetCopyright() {
        return (this.copyright != null);
    }

    /**
     * Gets the value of the disclaimer property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDisclaimer() {
        return disclaimer;
    }

    /**
     * Sets the value of the disclaimer property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDisclaimer(String value) {
        this.disclaimer = value;
    }

    public boolean isSetDisclaimer() {
        return (this.disclaimer != null);
    }

    /**
     * Gets the value of the contactInfo property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Sets the value of the contactInfo property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setContactInfo(String value) {
        this.contactInfo = value;
    }

    public boolean isSetContactInfo() {
        return (this.contactInfo != null);
    }

    /**
     * Gets the value of the authorGroup property.
     *
     * @return
     *     possible object is
     *     {@link XBELAuthorGroup }
     *
     */
    public XBELAuthorGroup getAuthorGroup() {
        return authorGroup;
    }

    /**
     * Sets the value of the authorGroup property.
     *
     * @param value
     *     allowed object is
     *     {@link XBELAuthorGroup }
     *
     */
    public void setAuthorGroup(XBELAuthorGroup value) {
        this.authorGroup = value;
    }

    public boolean isSetAuthorGroup() {
        return (this.authorGroup != null);
    }

    /**
     * Gets the value of the licenseGroup property.
     *
     * @return
     *     possible object is
     *     {@link XBELLicenseGroup }
     *
     */
    public XBELLicenseGroup getLicenseGroup() {
        return licenseGroup;
    }

    /**
     * Sets the value of the licenseGroup property.
     *
     * @param value
     *     allowed object is
     *     {@link XBELLicenseGroup }
     *
     */
    public void setLicenseGroup(XBELLicenseGroup value) {
        this.licenseGroup = value;
    }

    public boolean isSetLicenseGroup() {
        return (this.licenseGroup != null);
    }

}
