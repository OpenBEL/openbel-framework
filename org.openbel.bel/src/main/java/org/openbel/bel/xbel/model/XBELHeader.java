/**
 * Copyright (C) 2012 Selventa, Inc.
 *
 * This file is part of the OpenBEL Framework.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The OpenBEL Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the OpenBEL Framework. If not, see <http://www.gnu.org/licenses/>.
 *
 * Additional Terms under LGPL v3:
 *
 * This license does not authorize you and you are prohibited from using the
 * name, trademarks, service marks, logos or similar indicia of Selventa, Inc.,
 * or, in the discretion of other licensors or authors of the program, the
 * name, trademarks, service marks, logos or similar indicia of such authors or
 * licensors, in any marketing or advertising materials relating to your
 * distribution of the program or any covered product. This restriction does
 * not waive or limit your obligation to keep intact all copyright notices set
 * forth in the program as delivered to you.
 *
 * If you distribute the program in whole or in part, or any modified version
 * of the program, and you assume contractual liability to the recipient with
 * respect to the program or modified version, then you will indemnify the
 * authors and licensors of the program for any liabilities that these
 * contractual assumptions directly impose on those licensors and authors.
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
