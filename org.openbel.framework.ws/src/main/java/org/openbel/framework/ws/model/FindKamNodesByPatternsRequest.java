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
package org.openbel.framework.ws.model;

import java.util.ArrayList;
import java.util.List;

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
 *         &lt;element name="handle" type="{http://belframework.org/ws/schemas}KamHandle"/>
 *         &lt;element name="patterns" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *         &lt;element name="filter" type="{http://belframework.org/ws/schemas}NodeFilter" minOccurs="0"/>
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
@XmlType(name = "", propOrder = { "handle", "patterns", "filter", "dialect" })
@XmlRootElement(name = "FindKamNodesByPatternsRequest")
public class FindKamNodesByPatternsRequest {

    @XmlElement(required = true)
    protected KamHandle handle;
    @XmlElement(required = true)
    protected List<String> patterns;
    protected NodeFilter filter;
    protected DialectHandle dialect;

    /**
     * Gets the value of the handle property.
     * 
     * @return possible object is {@link KamHandle }
     * 
     */
    public KamHandle getHandle() {
        return handle;
    }

    /**
     * Sets the value of the handle property.
     * 
     * @param value
     *            allowed object is {@link KamHandle }
     * 
     */
    public void setHandle(KamHandle value) {
        this.handle = value;
    }

    /**
     * Gets the value of the patterns property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the patterns property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getPatterns().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     * 
     * 
     */
    public List<String> getPatterns() {
        if (patterns == null) {
            patterns = new ArrayList<String>();
        }
        return this.patterns;
    }

    /**
     * Gets the value of the filter property.
     * 
     * @return possible object is {@link NodeFilter }
     * 
     */
    public NodeFilter getFilter() {
        return filter;
    }

    /**
     * Sets the value of the filter property.
     * 
     * @param value
     *            allowed object is {@link NodeFilter }
     * 
     */
    public void setFilter(NodeFilter value) {
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
