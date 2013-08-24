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
package org.openbel.framework.internal;

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
