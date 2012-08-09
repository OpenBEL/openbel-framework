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
package org.openbel.framework.api.internal;

import java.util.Date;

/**
 * KamDbObject represents the kam table in the KAM catalog schema.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class KamDbObject extends KamCatalogDbObject {
    /**
     * Defines the kam name.
     */
    private String name;

    /**
     * Defines the kam description.
     */
    private String description;

    /**
     * Defines the kam's last compiled time.
     */
    private Date lastCompiled;

    /**
     * Defines the kam schema name.
     */
    private String schemaName;

    public KamDbObject(Integer kamId, String name, String descripton,
            Date lastCompiled, String schemaName) {
        super(kamId);
        this.name = name;
        this.description = descripton;
        this.lastCompiled = lastCompiled;
        this.schemaName = schemaName;
    }

    /**
     * Returns the kam name.
     * 
     * @return {@link String}, the kam name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the kam name.
     * 
     * @param name {@link String}, the kam name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the kam description.
     * 
     * @return {@link String}, the kam description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the kam description.
     * 
     * @param description {@link String}, the kam description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the kam last compiled date.
     * 
     * @return {@link Date}, the kam last compiled date
     */
    public Date getLastCompiled() {
        return lastCompiled;
    }

    /**
     * Sets the kam last compiled time.
     * 
     * @param lastCompiled {@link Date}, the kam last compiled time
     */
    public void setLastCompiled(Date lastCompiled) {
        this.lastCompiled = lastCompiled;
    }

    /**
     * Returns the kam schema name.
     * 
     * @return {@link String}, the kam schema name
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * Sets the kam schema name.
     * 
     * @param schemaName {@link String}, the kam schema name
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result
                + ((lastCompiled == null) ? 0 : lastCompiled.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((schemaName == null) ? 0 : schemaName.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        KamDbObject other = (KamDbObject) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (lastCompiled == null) {
            if (other.lastCompiled != null)
                return false;
        } else if (!lastCompiled.equals(other.lastCompiled))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (schemaName == null) {
            if (other.schemaName != null)
                return false;
        } else if (!schemaName.equals(other.schemaName))
            return false;
        return true;
    }
}
