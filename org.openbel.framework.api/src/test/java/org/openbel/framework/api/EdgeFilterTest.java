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
package org.openbel.framework.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.openbel.framework.common.enums.FunctionEnum.BIOLOGICAL_PROCESS;
import static org.openbel.framework.common.enums.FunctionEnum.COMPLEX_ABUNDANCE;
import static org.openbel.framework.common.enums.FunctionEnum.KINASE_ACTIVITY;
import static org.openbel.framework.common.enums.FunctionEnum.PROTEIN_ABUNDANCE;
import static org.openbel.framework.common.enums.RelationshipType.ACTS_IN;
import static org.openbel.framework.common.enums.RelationshipType.DECREASES;
import static org.openbel.framework.common.enums.RelationshipType.HAS_COMPONENT;
import static org.openbel.framework.common.enums.RelationshipType.INCREASES;

import java.util.Collection;
import java.util.Iterator;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.KamTestUtil.TestKamEdge;
import org.openbel.framework.api.KamTestUtil.TestKamNode;
import org.openbel.framework.internal.KamInfoUtil;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;

public class EdgeFilterTest {
    private static Kam testKAM;

    @Test
    public void testSingleIncludeCriteria() {
        // Test include HAS_COMPONENT relationship type criteria yields 2 edges
        EdgeFilter edgeFilter = testKAM.createEdgeFilter();
        RelationshipTypeFilterCriteria rtcriteria =
                new RelationshipTypeFilterCriteria();
        rtcriteria.setInclude(true);
        rtcriteria.add(HAS_COMPONENT);
        edgeFilter.add(rtcriteria);
        Collection<KamEdge> edges = testKAM.getEdges(edgeFilter);
        assertNotNull(edges);
        assertEquals(2, edges.size());
        Iterator<KamEdge> edgeIt = edges.iterator();
        KamEdge e1 = edgeIt.next();
        assertEquals(HAS_COMPONENT, e1.getRelationshipType());
        KamEdge e2 = edgeIt.next();
        assertEquals(HAS_COMPONENT, e2.getRelationshipType());
    }

    @Test
    public void testSingleExcludeCriteria() {
        // Test exclude HAS_COMPONENT relationship type criteria yields 3 edges
        EdgeFilter edgeFilter = testKAM.createEdgeFilter();
        RelationshipTypeFilterCriteria rtcriteria =
                new RelationshipTypeFilterCriteria();
        rtcriteria.setInclude(false);
        rtcriteria.add(HAS_COMPONENT);
        edgeFilter.add(rtcriteria);
        Collection<KamEdge> edges = testKAM.getEdges(edgeFilter);
        assertNotNull(edges);
        assertEquals(3, edges.size());
        Iterator<KamEdge> edgeIt = edges.iterator();
        KamEdge e1 = edgeIt.next();
        assertEquals(INCREASES, e1.getRelationshipType());
        KamEdge e2 = edgeIt.next();
        assertEquals(ACTS_IN, e2.getRelationshipType());
        KamEdge e3 = edgeIt.next();
        assertEquals(DECREASES, e3.getRelationshipType());
    }

    @Test
    public void testMultipleIncludeCriteria() {
        // Test include INCREASES AND DECREASES relationship type criteria yields 2 edges
        EdgeFilter edgeFilter = testKAM.createEdgeFilter();
        RelationshipTypeFilterCriteria rtcriteria =
                new RelationshipTypeFilterCriteria();
        rtcriteria.setInclude(true);
        rtcriteria.add(INCREASES);
        rtcriteria.add(DECREASES);
        edgeFilter.add(rtcriteria);
        Collection<KamEdge> edges = testKAM.getEdges(edgeFilter);
        assertNotNull(edges);
        assertEquals(2, edges.size());
        Iterator<KamEdge> edgeIt = edges.iterator();
        KamEdge e1 = edgeIt.next();
        assertEquals(INCREASES, e1.getRelationshipType());
        KamEdge e2 = edgeIt.next();
        assertEquals(DECREASES, e2.getRelationshipType());
    }

    @Test
    public void testMultipleExcludeCriteria() {
        // Test exclude INCREASES AND DECREASES relationship type criteria yields 3 edges
        EdgeFilter edgeFilter = testKAM.createEdgeFilter();
        RelationshipTypeFilterCriteria rtcriteria =
                new RelationshipTypeFilterCriteria();
        rtcriteria.setInclude(false);
        rtcriteria.add(INCREASES);
        rtcriteria.add(DECREASES);
        edgeFilter.add(rtcriteria);
        Collection<KamEdge> edges = testKAM.getEdges(edgeFilter);
        assertNotNull(edges);
        assertEquals(3, edges.size());
        Iterator<KamEdge> edgeIt = edges.iterator();
        KamEdge e1 = edgeIt.next();
        assertEquals(HAS_COMPONENT, e1.getRelationshipType());
        KamEdge e2 = edgeIt.next();
        assertEquals(HAS_COMPONENT, e2.getRelationshipType());
        KamEdge e3 = edgeIt.next();
        assertEquals(ACTS_IN, e3.getRelationshipType());
    }

    @Test
    public void testCollidingIncludeExcludeCriteria() {
        // Test include
        //   HAS_COMPONENT
        // and exclude
        //   HAS_COMPONENT
        // relationship type criterion yields 2 edges
        EdgeFilter edgeFilter = testKAM.createEdgeFilter();
        RelationshipTypeFilterCriteria incriteria =
                new RelationshipTypeFilterCriteria();
        incriteria.setInclude(true);
        incriteria.add(HAS_COMPONENT);
        RelationshipTypeFilterCriteria excriteria =
                new RelationshipTypeFilterCriteria();
        excriteria.setInclude(false);
        excriteria.add(HAS_COMPONENT);
        edgeFilter.add(incriteria);
        edgeFilter.add(excriteria);
        Collection<KamEdge> edges = testKAM.getEdges(edgeFilter);
        assertNotNull(edges);
        assertEquals(5, edges.size());
        Iterator<KamEdge> edgeIt = edges.iterator();
        KamEdge e1 = edgeIt.next();
        assertEquals(HAS_COMPONENT, e1.getRelationshipType());
        KamEdge e2 = edgeIt.next();
        assertEquals(HAS_COMPONENT, e2.getRelationshipType());
        KamEdge e3 = edgeIt.next();
        assertEquals(INCREASES, e3.getRelationshipType());
        KamEdge e4 = edgeIt.next();
        assertEquals(ACTS_IN, e4.getRelationshipType());
        KamEdge e5 = edgeIt.next();
        assertEquals(DECREASES, e5.getRelationshipType());
    }

    @Test
    public void testCollidingExcludeIncludeCriteria() {
        // Test exclude
        //   HAS_COMPONENT
        // and include
        //   HAS_COMPONENT
        // relationship type criterion yields 2 edges
        EdgeFilter edgeFilter = testKAM.createEdgeFilter();
        RelationshipTypeFilterCriteria excriteria =
                new RelationshipTypeFilterCriteria();
        excriteria.setInclude(false);
        excriteria.add(HAS_COMPONENT);
        RelationshipTypeFilterCriteria incriteria =
                new RelationshipTypeFilterCriteria();
        incriteria.setInclude(true);
        incriteria.add(HAS_COMPONENT);
        edgeFilter.add(excriteria);
        edgeFilter.add(incriteria);
        Collection<KamEdge> edges = testKAM.getEdges(edgeFilter);
        assertNotNull(edges);
        assertEquals(3, edges.size());
        Iterator<KamEdge> edgeIt = edges.iterator();
        KamEdge e1 = edgeIt.next();
        assertEquals(INCREASES, e1.getRelationshipType());
        KamEdge e2 = edgeIt.next();
        assertEquals(ACTS_IN, e2.getRelationshipType());
        KamEdge e3 = edgeIt.next();
        assertEquals(DECREASES, e3.getRelationshipType());
    }

    @BeforeClass
    public static void setup() {
        testKAM = createTestKAM();
        assertNotNull(testKAM);
    }

    private static Kam createTestKAM() {
        KamInfo testKAMInfo = null;
        try {
            testKAMInfo = KamInfoUtil.createKamInfo();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        final TestKamNode[] testKamNodes = new TestKamNode[] {
                new TestKamNode(1, COMPLEX_ABUNDANCE, "complex(p(1), p(2))"),
                new TestKamNode(2, PROTEIN_ABUNDANCE, "p(1)"),
                new TestKamNode(3, PROTEIN_ABUNDANCE, "p(2)"),
                new TestKamNode(4, KINASE_ACTIVITY, "kin(p(2))"),
                new TestKamNode(5, BIOLOGICAL_PROCESS, "bp(3)")
        };

        final TestKamEdge[] testKamEdges =
                new TestKamEdge[] {
                        new TestKamEdge(1, testKamNodes[0], HAS_COMPONENT,
                                testKamNodes[1]),
                        new TestKamEdge(2, testKamNodes[0], HAS_COMPONENT,
                                testKamNodes[2]),
                        new TestKamEdge(3, testKamNodes[1], INCREASES,
                                testKamNodes[2]),
                        new TestKamEdge(4, testKamNodes[2], ACTS_IN,
                                testKamNodes[3]),
                        new TestKamEdge(5, testKamNodes[3], DECREASES,
                                testKamNodes[4])
                };

        return KamTestUtil.createKam(testKAMInfo, testKamNodes, testKamEdges);
    }
}
