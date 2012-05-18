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
package org.openbel.framework.core.kamstore;

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