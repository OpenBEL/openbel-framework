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
package org.openbel.framework.core.equivalence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.protonetwork.model.ProtoEdgeTable.TableProtoEdge;

/**
 * Tests around {@link StatementEquivalencer}
 *
 * @author James McMahon {@code <jmcmahon@selventa.com>}
 */
public class StatementEquivalencerTest {

    // TODO create a more generalized test that makes sure equivalent edges
    // have the same statements
    /**
     * Test for loss of statement support backing equal edges
     *
     * see https://github.com/OpenBEL/openbel-framework/issues/10
     */
    @Test
    public void testIssue10() {
        // test data from nick's test, this won't make sense in isolation
        // see https://gist.github.com/2919455 for documentation
        List<TableProtoEdge> edges = new ArrayList<TableProtoEdge>();
        edges.add(new TableProtoEdge(0, RelationshipType.DIRECTLY_INCREASES.getDisplayValue(), 1));
        edges.add(new TableProtoEdge(0, RelationshipType.DIRECTLY_INCREASES.getDisplayValue(), 1));
        edges.add(new TableProtoEdge(2, RelationshipType.ACTS_IN.getDisplayValue(), 0));
        edges.add(new TableProtoEdge(2, RelationshipType.ACTS_IN.getDisplayValue(), 0));
        edges.add(new TableProtoEdge(3, RelationshipType.TRANSCRIBED_TO.getDisplayValue(), 4));
        edges.add(new TableProtoEdge(4, RelationshipType.TRANSLATED_TO.getDisplayValue(), 5));
        edges.add(new TableProtoEdge(6, RelationshipType.TRANSCRIBED_TO.getDisplayValue(), 7));
        edges.add(new TableProtoEdge(7, RelationshipType.TRANSLATED_TO.getDisplayValue(), 8));

        Map<Integer, Set<Integer>> edgeStmts = new HashMap<Integer, Set<Integer>>();
        edgeStmts.put(0, arrayToSet(0));
        edgeStmts.put(1, arrayToSet(1));
        edgeStmts.put(2, arrayToSet(0));
        edgeStmts.put(3, arrayToSet(1));
        edgeStmts.put(4, arrayToSet(2));
        edgeStmts.put(5, arrayToSet(3));
        edgeStmts.put(6, arrayToSet(4));
        edgeStmts.put(7, arrayToSet(5));

        Map<Integer, Integer> eqn = new HashMap<Integer, Integer>();
        eqn.put(0, 0);
        eqn.put(1, 1);
        eqn.put(2, 2);
        eqn.put(3, 3);
        eqn.put(4, 4);
        eqn.put(5, 2);
        eqn.put(6, 5);
        eqn.put(7, 1);
        eqn.put(8, 6);

        // this will be cleared, but just to be superstitious
        Map<Integer, Integer> eqe = new HashMap<Integer, Integer>();
        eqn.put(0, 0);
        eqn.put(1, 0);
        eqn.put(2, 2);
        eqn.put(3, 2);
        eqn.put(4, 4);
        eqn.put(5, 5);
        eqn.put(6, 6);
        eqn.put(7, 7);

        StatementEquivalencer.equivalenceInternal(edges, edgeStmts, eqn, eqe);

        Assert.assertEquals(2, edgeStmts.get(1).size());
    }

    private static Set<Integer> arrayToSet(int... array) {
        // damn you java
        Set<Integer> set = new HashSet<Integer>();
        for (int v : array) {
           set.add(v);
        }
        return set;
    }
}
