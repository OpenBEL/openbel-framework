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

/**
 * KamCatalogObject represents a database object for tables in the
 * KAM catalog schema.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public abstract class KamCatalogDbObject {

    /**
     * Defines an id for the database object.
     */
    private Integer id;

    /**
     * Constructs the kam catalog object with the <tt>id</tt>.
     *
     * @param id {@link Integer}, the kam catalog object id
     */
    public KamCatalogDbObject(Integer id) {
        this.id = id;
    }

    /**
     * Returns the id of the database object.
     *
     * @param id {@link Integer}, the id of the database object
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the database object.
     *
     * @param id {@link Integer}, the id of the database object
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
