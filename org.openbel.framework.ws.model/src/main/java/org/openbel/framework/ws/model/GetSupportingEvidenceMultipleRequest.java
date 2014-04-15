/**
 * Copyright (C) 2012-2013 Selventa, Inc.
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

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

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
 *         &lt;element name="kamEdges" type="{http://belframework.org/ws/schemas}KamEdge" maxOccurs="unbounded"/>
 *         &lt;element name="filter" type="{http://belframework.org/ws/schemas}KamFilter" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "kamEdges",
        "filter"
})
@XmlRootElement(name = "GetSupportingEvidenceMultipleRequest")
public class GetSupportingEvidenceMultipleRequest {

    @XmlElement(required = true)
    protected List<KamEdge> kamEdges;
    protected KamFilter filter;

    /**
     * Gets the value of the kamEdge property.
     *
     * @return possible object is
     * {@link org.openbel.framework.ws.model.KamEdge}
     */
    public List<KamEdge> getKamEdges() {
        if (kamEdges == null) {
            kamEdges = new ArrayList<KamEdge>();
        }
        return kamEdges;
    }

    /**
     * Gets the value of the filter property.
     *
     * @return possible object is
     * {@link org.openbel.framework.ws.model.KamFilter}
     */
    public KamFilter getFilter() {
        return filter;
    }

    /**
     * Sets the value of the filter property.
     *
     * @param value allowed object is
     * {@link org.openbel.framework.ws.model.KamFilter}
     */
    public void setFilter(KamFilter value) {
        this.filter = value;
    }
}
