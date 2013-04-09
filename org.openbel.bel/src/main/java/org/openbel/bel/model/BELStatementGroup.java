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
package org.openbel.bel.model;

import java.util.ArrayList;
import java.util.List;

/**
 * BELStatementGroup represents a grouping {@link BELStatement} objects
 * captured in BEL Script.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class BELStatementGroup extends BELObject {
    private static final long serialVersionUID = 6692242897768495050L;
    private final String name;
    private final List<BELStatement> statements = new ArrayList<BELStatement>();
    private List<BELStatementGroup> childStatementGroups =
            new ArrayList<BELStatementGroup>();

    public BELStatementGroup() {
        this.name = null;
    }

    public BELStatementGroup(final String name) {
        this.name = name;
    }

    /**
     * Returns the statement group's name.
     *
     * @return {@link String} the statement group name, which may be null
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the statement group's statements.
     *
     * @return {@link List} of {@link BELStatement}s, which may be null
     */
    public List<BELStatement> getStatements() {
        return statements;
    }

    /**
     * Returns the statement group's child statement groups.
     *
     * @return {@link List} of {@link BELStatementGroup}, which may be null
     */
    public List<BELStatementGroup> getChildStatementGroups() {
        return childStatementGroups;
    }

    /**
     * Sets the statement group's child statement groups.
     *
     * @param childStatementGroups {@link List} of {@link BELStatementGroup} the
     * child statement groups, which can be null
     */
    public void setChildStatementGroups(
            List<BELStatementGroup> childStatementGroups) {
        this.childStatementGroups = childStatementGroups;
    }
}
