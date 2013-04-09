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
package org.openbel.framework.common.protonetwork.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import org.junit.Test;
import org.openbel.framework.common.protonetwork.model.ProtoEdgeTable;
import org.openbel.framework.common.protonetwork.model.ProtoEdgeTable.TableProtoEdge;

/**
 * {@link ProtoEdgeTableTest} tests the data representation of the
 * {@link ProtoEdgeTable proto edge table}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class ProtoEdgeTableTest {

    /**
     * Test adding distinct edges in a single call.  Based on BEL statement:
     * <p><tt>deg(p(1)) -> p(2)</tt></p>
     */
    @Test
    public void testDistinctEdgesSingleCall() {
        final ProtoEdgeTable tbl = new ProtoEdgeTable();

        final TableProtoEdge edge1 = new TableProtoEdge(1, "INCREASES", 2);
        final TableProtoEdge edge2 =
                new TableProtoEdge(1, "DIRECTLY_DECREASES", 3);

        // for statement 0 add both edges in one call
        tbl.addEdges(0, edge1, edge2);

        // we should have one statement (0)
        assertThat(tbl.getStatementEdges().size(), is(1));

        // we should have two edges (0 and 1)
        assertThat(tbl.getProtoEdges().size(), is(2));

        // edge 0 and 1 should associate with statement 0
        Iterator<Integer> stit = tbl.getStatementEdges().get(0).iterator();
        assertThat(stit.next(), is(0));
        assertThat(stit.next(), is(1));

        // both edges should not be equivalent
        assertThat(tbl.getEquivalences().get(0), is(0));
        assertThat(tbl.getEquivalences().get(1), is(1));
    }

    /**
     * Test adding distinct edges in a subsequent calls.  Based on BEL
     * statement:
     * <p><tt>deg(p(1)) -> p(2)</tt></p>
     */
    @Test
    public void testDistinctEdgesSubsequentCall() {
        final ProtoEdgeTable tbl = new ProtoEdgeTable();

        final TableProtoEdge edge1 = new TableProtoEdge(1, "INCREASES", 2);
        final TableProtoEdge edge2 =
                new TableProtoEdge(1, "DIRECTLY_DECREASES", 3);

        // for statement 0 add both edges in subsequent calls
        tbl.addEdges(0, edge1);
        tbl.addEdges(0, edge2);

        // we should have one statement (0)
        assertThat(tbl.getStatementEdges().size(), is(1));

        // we should have two edges (0 and 1)
        assertThat(tbl.getProtoEdges().size(), is(2));

        // edge 0 and 1 should associate with statement 0
        Iterator<Integer> stit = tbl.getStatementEdges().get(0).iterator();
        assertThat(stit.next(), is(0));
        assertThat(stit.next(), is(1));

        // both edges should not be equivalent
        assertThat(tbl.getEquivalences().get(0), is(0));
        assertThat(tbl.getEquivalences().get(1), is(1));
    }

    /**
     * Test adding duplicate edges from two separate statements.  Base on BEL
     * statements:
     * <p>
     * <tt>complex(p(1),p(2))</tt>
     * <br>
     * <tt>complex(p(1),p(2))</tt>
     * </p>
     */
    @Test
    public void testDuplicateEdges() {
        final ProtoEdgeTable tbl = new ProtoEdgeTable();

        // for statement 0, add edge triple "1 HAS_COMPONENT 2"
        final TableProtoEdge edge1 = new TableProtoEdge(1, "HAS_COMPONENT", 2);
        tbl.addEdges(0, edge1);

        // for statement 1, add the same edge
        final TableProtoEdge edge2 = new TableProtoEdge(1, "HAS_COMPONENT", 2);
        tbl.addEdges(1, edge2);

        // we should have two statements (0 and 1)
        assertThat(tbl.getStatementEdges().size(), is(2));

        // we should have two edges (0 and 1)
        assertThat(tbl.getProtoEdges().size(), is(2));

        // edge 0 should associate with statement 0
        assertThat(tbl.getStatementEdges().get(0).iterator().next(), is(0));

        // edge 1 should associate with statement 1
        assertThat(tbl.getStatementEdges().get(1).iterator().next(), is(1));

        // both edges should be equivalent
        assertThat(tbl.getEquivalences().get(0), is(0));
        assertThat(tbl.getEquivalences().get(1), is(0));
    }
}
