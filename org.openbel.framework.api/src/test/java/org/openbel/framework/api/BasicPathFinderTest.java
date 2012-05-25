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
import static org.openbel.framework.common.enums.FunctionEnum.ABUNDANCE;
import static org.openbel.framework.common.enums.FunctionEnum.CATALYTIC_ACTIVITY;
import static org.openbel.framework.common.enums.FunctionEnum.COMPLEX_ABUNDANCE;
import static org.openbel.framework.common.enums.FunctionEnum.COMPOSITE_ABUNDANCE;
import static org.openbel.framework.common.enums.FunctionEnum.GENE_ABUNDANCE;
import static org.openbel.framework.common.enums.FunctionEnum.PROTEIN_ABUNDANCE;
import static org.openbel.framework.common.enums.FunctionEnum.RNA_ABUNDANCE;
import static org.openbel.framework.common.enums.RelationshipType.ACTS_IN;
import static org.openbel.framework.common.enums.RelationshipType.HAS_COMPONENT;
import static org.openbel.framework.common.enums.RelationshipType.INCLUDES;
import static org.openbel.framework.common.enums.RelationshipType.INCREASES;
import static org.openbel.framework.common.enums.RelationshipType.TRANSCRIBED_TO;
import static org.openbel.framework.common.enums.RelationshipType.TRANSLATED_TO;

import java.util.List;

import org.junit.Test;
import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.api.KamTestUtil.TestKamEdge;
import org.openbel.framework.api.KamTestUtil.TestKamNode;
import org.openbel.framework.internal.KamInfoUtil;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;

/**
 * BasicPathFinderTest tests the {@link BasicPathFinder} findPaths, scan, and
 * interconnect APIs.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class BasicPathFinderTest {

    /**
     * Test path finding from geneAbundance(23) to
     * catalyticActivity(proteinAbundance(23)) with a default max search depth.
     */
    @Test
    public void testFindPathDefault() {
        final Kam kam = createTestKAM();
        final PathFinder pathFinder = new BasicPathFinder(kam);

        SimplePath[] pathsFound = pathFinder.findPaths(kam.findNode(1),
                kam.findNode(5));

        assertNotNull(pathsFound);
        assertEquals(1, pathsFound.length);

        final List<KamEdge> edges = pathsFound[0].getEdges();
        assertEquals(3, edges.size());
        final KamEdge[] edgeArray = edges.toArray(new KamEdge[edges.size()]);
        assertEquals(TRANSCRIBED_TO, edgeArray[0].getRelationshipType());
        assertEquals(TRANSLATED_TO, edgeArray[1].getRelationshipType());
        assertEquals(ACTS_IN, edgeArray[2].getRelationshipType());
    }

    /**
     * Test path finding from geneAbundance(23) to
     * catalyticActivity(proteinAbundance(23)) with a max search depth of 6.
     */
    @Test
    public void testFindPathLargerDepth() {
        final Kam kam = createTestKAM();
        final PathFinder pathFinder = new BasicPathFinder(kam, 6);

        SimplePath[] pathsFound = pathFinder.findPaths(kam.findNode(1),
                kam.findNode(5));

        assertNotNull(pathsFound);
        assertEquals(2, pathsFound.length);

        List<KamEdge> edges = pathsFound[0].getEdges();
        assertEquals(3, edges.size());
        KamEdge[] edgeArray = edges.toArray(new KamEdge[edges.size()]);
        assertEquals(TRANSCRIBED_TO, edgeArray[0].getRelationshipType());
        assertEquals(TRANSLATED_TO, edgeArray[1].getRelationshipType());
        assertEquals(ACTS_IN, edgeArray[2].getRelationshipType());

        edges = pathsFound[1].getEdges();
        assertEquals(5, edges.size());
        edgeArray = edges.toArray(new KamEdge[edges.size()]);
        assertEquals(TRANSCRIBED_TO, edgeArray[0].getRelationshipType());
        assertEquals(TRANSLATED_TO, edgeArray[1].getRelationshipType());
        assertEquals(HAS_COMPONENT, edgeArray[2].getRelationshipType());
        assertEquals(HAS_COMPONENT, edgeArray[3].getRelationshipType());
        assertEquals(INCREASES, edgeArray[4].getRelationshipType());
    }

    /**
     * Test path scan out from abundance(22) with a default max search depth.
     */
    @Test
    public void testScanDefault() {
        final Kam kam = createTestKAM();
        final PathFinder pathFinder = new BasicPathFinder(kam);

        SimplePath[] pathsFound =
                pathFinder.scan(kam.findNode("abundance(22)"));

        assertNotNull(pathsFound);
        assertEquals(7, pathsFound.length);

        // test returned paths scanned and their order
        assertEquals(kam.findNode("abundance(22)"), pathsFound[0].getSource());
        assertEquals(kam.findNode("geneAbundance(23)"),
                pathsFound[0].getTarget());
        assertEquals(kam.findNode("abundance(22)"), pathsFound[1].getSource());
        assertEquals(kam.findNode("geneAbundance(23)"),
                pathsFound[1].getTarget());
        assertEquals(kam.findNode("abundance(22)"), pathsFound[2].getSource());
        assertEquals(
                kam.findNode("compositeAbundance(abundance(22),abundance(29))"),
                pathsFound[2].getTarget());
        assertEquals(kam.findNode("abundance(22)"), pathsFound[3].getSource());
        assertEquals(kam.findNode("geneAbundance(28)"),
                pathsFound[3].getTarget());
        assertEquals(kam.findNode("abundance(22)"), pathsFound[4].getSource());
        assertEquals(kam.findNode("abundance(29)"), pathsFound[4].getTarget());
        assertEquals(kam.findNode("abundance(22)"), pathsFound[5].getSource());
        assertEquals(
                kam.findNode("complexAbundance(abundance(22),proteinAbundance(28))"),
                pathsFound[5].getTarget());
        assertEquals(kam.findNode("abundance(22)"), pathsFound[6].getSource());
        assertEquals(kam.findNode("rnaAbundance(28)"),
                pathsFound[6].getTarget());
    }

    /**
     * Test path scan out from abundance(22) with a default max search depth.
     */
    @Test
    public void testScanSmallDepth() {
        final Kam kam = createTestKAM();
        final PathFinder pathFinder = new BasicPathFinder(kam, 2);

        SimplePath[] pathsFound =
                pathFinder.scan(kam.findNode("abundance(22)"));

        assertNotNull(pathsFound);
        assertEquals(5, pathsFound.length);

        // test returned paths scanned and their order
        assertEquals(kam.findNode("abundance(22)"), pathsFound[0].getSource());
        assertEquals(kam.findNode("proteinAbundance(23)"),
                pathsFound[0].getTarget());
        assertEquals(kam.findNode("abundance(22)"), pathsFound[1].getSource());
        assertEquals(kam.findNode("proteinAbundance(23)"),
                pathsFound[1].getTarget());
        assertEquals(kam.findNode("abundance(22)"), pathsFound[2].getSource());
        assertEquals(kam.findNode("proteinAbundance(28)"),
                pathsFound[2].getTarget());
        assertEquals(kam.findNode("abundance(22)"), pathsFound[3].getSource());
        assertEquals(kam.findNode("abundance(29)"), pathsFound[3].getTarget());
        assertEquals(kam.findNode("abundance(22)"), pathsFound[4].getSource());
        assertEquals(kam.findNode("catalyticActivity(proteinAbundance(28))"),
                pathsFound[4].getTarget());
    }

    /**
     * Test interconnect sources of proteinAbundance(23) and
     * proteinAbundance(28).
     */
    @Test
    public void testInterconnectDefaultDepth() {
        final Kam kam = createTestKAM();
        final PathFinder pathFinder = new BasicPathFinder(kam);

        SimplePath[] pathsFound = pathFinder.interconnect(new KamNode[] {
                kam.findNode("proteinAbundance(23)"),
                kam.findNode("proteinAbundance(28)") });

        assertNotNull(pathsFound);
        assertEquals(2, pathsFound.length);

        List<KamEdge> edges = pathsFound[0].getEdges();
        assertEquals(4, edges.size());
        KamEdge[] edgeArray = edges.toArray(new KamEdge[edges.size()]);
        assertEquals(ACTS_IN, edgeArray[0].getRelationshipType());
        assertEquals(INCREASES, edgeArray[1].getRelationshipType());
        assertEquals(HAS_COMPONENT, edgeArray[2].getRelationshipType());
        assertEquals(HAS_COMPONENT, edgeArray[3].getRelationshipType());

        edges = pathsFound[1].getEdges();
        assertEquals(4, edges.size());
        edgeArray = edges.toArray(new KamEdge[edges.size()]);
        assertEquals(HAS_COMPONENT, edgeArray[0].getRelationshipType());
        assertEquals(HAS_COMPONENT, edgeArray[1].getRelationshipType());
        assertEquals(HAS_COMPONENT, edgeArray[2].getRelationshipType());
        assertEquals(HAS_COMPONENT, edgeArray[3].getRelationshipType());
    }

    /**
     * Test interconnect sources of proteinAbundance(23) and
     * proteinAbundance(28) with a max search depth of 3.
     */
    @Test
    public void testInterconnectSmallDepth() {
        final Kam kam = createTestKAM();
        final PathFinder pathFinder = new BasicPathFinder(kam, 3);

        SimplePath[] pathsFound = pathFinder.interconnect(new KamNode[] {
                kam.findNode("proteinAbundance(23)"),
                kam.findNode("proteinAbundance(28)") });

        assertNotNull(pathsFound);
        assertEquals(0, pathsFound.length);
    }

    /**
     * Create a test KAM for testing path finds.
     *
     * @return {@link Kam} the test KAM
     */
    private Kam createTestKAM() {
        KamInfo testKAMInfo = null;
        try {
            testKAMInfo = KamInfoUtil.createKamInfo();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        final TestKamNode[] testKamNodes =
                new TestKamNode[] {
                        new TestKamNode(1, GENE_ABUNDANCE, "geneAbundance(23)"),
                        new TestKamNode(2, RNA_ABUNDANCE, "rnaAbundance(23)"),
                        new TestKamNode(3, PROTEIN_ABUNDANCE,
                                "proteinAbundance(23)"),
                        new TestKamNode(
                                4,
                                COMPLEX_ABUNDANCE,
                                "complexAbundance(abundance(22),proteinAbundance(23))"),
                        new TestKamNode(5, CATALYTIC_ACTIVITY,
                                "catalyticActivity(proteinAbundance(23))"),
                        new TestKamNode(6, ABUNDANCE, "abundance(29)"),
                        new TestKamNode(7, ABUNDANCE, "abundance(22)"),
                        new TestKamNode(
                                8,
                                COMPOSITE_ABUNDANCE,
                                "compositeAbundance(abundance(22),abundance(29))"),
                        new TestKamNode(
                                9,
                                COMPLEX_ABUNDANCE,
                                "complexAbundance(abundance(22),proteinAbundance(28))"),
                        new TestKamNode(10, CATALYTIC_ACTIVITY,
                                "catalyticActivity(proteinAbundance(28))"),
                        new TestKamNode(11, PROTEIN_ABUNDANCE,
                                "proteinAbundance(28)"),
                        new TestKamNode(12, RNA_ABUNDANCE, "rnaAbundance(28)"),
                        new TestKamNode(13, GENE_ABUNDANCE, "geneAbundance(28)"),
                };

        final TestKamEdge[] testKamEdges =
                new TestKamEdge[] {
                        new TestKamEdge(1, testKamNodes[0], TRANSCRIBED_TO,
                                testKamNodes[1]),
                        new TestKamEdge(2, testKamNodes[1], TRANSLATED_TO,
                                testKamNodes[2]),
                        new TestKamEdge(3, testKamNodes[2], ACTS_IN,
                                testKamNodes[4]),
                        new TestKamEdge(4, testKamNodes[3], HAS_COMPONENT,
                                testKamNodes[2]),
                        new TestKamEdge(5, testKamNodes[3], HAS_COMPONENT,
                                testKamNodes[6]),
                        new TestKamEdge(6, testKamNodes[6], INCREASES,
                                testKamNodes[4]),
                        new TestKamEdge(7, testKamNodes[8], HAS_COMPONENT,
                                testKamNodes[6]),
                        new TestKamEdge(8, testKamNodes[7], INCLUDES,
                                testKamNodes[6]),
                        new TestKamEdge(9, testKamNodes[7], INCLUDES,
                                testKamNodes[5]),
                        new TestKamEdge(10, testKamNodes[7], INCREASES,
                                testKamNodes[9]),
                        new TestKamEdge(11, testKamNodes[8], HAS_COMPONENT,
                                testKamNodes[10]),
                        new TestKamEdge(12, testKamNodes[10], ACTS_IN,
                                testKamNodes[9]),
                        new TestKamEdge(13, testKamNodes[11], TRANSLATED_TO,
                                testKamNodes[10]),
                        new TestKamEdge(14, testKamNodes[12], TRANSCRIBED_TO,
                                testKamNodes[11]),
                };

        return KamTestUtil.createKam(testKAMInfo, testKamNodes, testKamEdges);
    }
}
