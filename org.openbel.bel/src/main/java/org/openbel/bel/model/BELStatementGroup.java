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
