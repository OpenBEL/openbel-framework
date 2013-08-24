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
