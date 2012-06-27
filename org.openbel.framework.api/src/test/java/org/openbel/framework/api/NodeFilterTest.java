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
import static org.openbel.framework.common.enums.ReturnType.ABUNDANCE;

import java.util.Collection;
import java.util.Iterator;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.api.KamTestUtil.TestKamEdge;
import org.openbel.framework.api.KamTestUtil.TestKamNode;
import org.openbel.framework.common.enums.ReturnType;
import org.openbel.framework.internal.KamInfoUtil;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;

/**
 * Unit test to cover filtering of {@link KamNode} objects in the {@link Kam}
 * using a {@link NodeFilter}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class NodeFilterTest {
    private static Kam testKAM;

    @Test
    public void testSingleIncludeCriteria() {
        // Test include of ABUNDANCE return type criteria yields 1 node
        NodeFilter nf = testKAM.createNodeFilter();
        FunctionReturnFilterCriteria rtcriteria =
                new FunctionReturnFilterCriteria();
        rtcriteria.setInclude(true);
        rtcriteria.add(ABUNDANCE);
        nf.add(rtcriteria);
        Collection<KamNode> nodes = testKAM.getNodes(nf);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertEquals(KINASE_ACTIVITY, nodes.iterator().next().getFunctionType());
        assertEquals(ABUNDANCE, nodes.iterator().next().getFunctionType()
                .getReturnType());

        // Test include of PROTEIN_ABUNDANCE function type criteria yields 2 nodes
        nf = testKAM.createNodeFilter();
        FunctionTypeFilterCriteria ftcriteria =
                new FunctionTypeFilterCriteria();
        ftcriteria.setInclude(true);
        ftcriteria.add(PROTEIN_ABUNDANCE);
        nf.add(ftcriteria);
        nodes = testKAM.getNodes(nf);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        Iterator<KamNode> nodeIt = nodes.iterator();
        KamNode n1 = nodeIt.next();
        assertEquals(PROTEIN_ABUNDANCE, n1.getFunctionType());
        KamNode n2 = nodeIt.next();
        assertEquals(PROTEIN_ABUNDANCE, n2.getFunctionType());
    }

    @Test
    public void testSingleExcludeCriteria() {
        // Test exclude of BIOLOGICAL_PROCESS return type criteria yields 4 nodes
        NodeFilter nf = testKAM.createNodeFilter();
        FunctionReturnFilterCriteria rtcriteria =
                new FunctionReturnFilterCriteria();
        rtcriteria.setInclude(false);
        rtcriteria.add(ReturnType.BIOLOGICAL_PROCESS);
        nf.add(rtcriteria);
        Collection<KamNode> nodes = testKAM.getNodes(nf);
        assertNotNull(nodes);
        assertEquals(4, nodes.size());
        Iterator<KamNode> nodeIt = nodes.iterator();
        KamNode n1 = nodeIt.next();
        assertEquals(COMPLEX_ABUNDANCE, n1.getFunctionType());
        KamNode n2 = nodeIt.next();
        assertEquals(PROTEIN_ABUNDANCE, n2.getFunctionType());
        KamNode n3 = nodeIt.next();
        assertEquals(PROTEIN_ABUNDANCE, n3.getFunctionType());
        KamNode n4 = nodeIt.next();
        assertEquals(KINASE_ACTIVITY, n4.getFunctionType());

        // Test exclude of PROTEIN_ABUNDANCE function type criteria yields 3 nodes
        nf = testKAM.createNodeFilter();
        FunctionTypeFilterCriteria ftcriteria =
                new FunctionTypeFilterCriteria();
        ftcriteria.setInclude(false);
        ftcriteria.add(PROTEIN_ABUNDANCE);
        nf.add(ftcriteria);
        nodes = testKAM.getNodes(nf);
        assertNotNull(nodes);
        assertEquals(3, nodes.size());
        nodeIt = nodes.iterator();
        n1 = nodeIt.next();
        assertEquals(COMPLEX_ABUNDANCE, n1.getFunctionType());
        n2 = nodeIt.next();
        assertEquals(KINASE_ACTIVITY, n2.getFunctionType());
        n3 = nodeIt.next();
        assertEquals(BIOLOGICAL_PROCESS, n3.getFunctionType());
    }

    @Test
    public void testMultipleIncludeCriterion() {
        // Test include of ABUNDANCE AND BIOLOGICAL_PROCESS return type criterion
        // yields 2 nodes
        NodeFilter nf = testKAM.createNodeFilter();
        FunctionReturnFilterCriteria rtcriteria =
                new FunctionReturnFilterCriteria();
        rtcriteria.setInclude(true);
        rtcriteria.add(ABUNDANCE);
        rtcriteria.add(ReturnType.BIOLOGICAL_PROCESS);
        nf.add(rtcriteria);
        Collection<KamNode> nodes = testKAM.getNodes(nf);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        Iterator<KamNode> nodeIt = nodes.iterator();
        KamNode n1 = nodeIt.next();
        assertEquals(KINASE_ACTIVITY, n1.getFunctionType());
        assertEquals(ABUNDANCE, n1.getFunctionType().getReturnType());
        KamNode n2 = nodeIt.next();
        assertEquals(BIOLOGICAL_PROCESS, n2.getFunctionType());
        assertEquals(ReturnType.BIOLOGICAL_PROCESS, n2.getFunctionType()
                .getReturnType());

        // Test include of PROTEIN_ABUNDANCE AND COMPLEX_ABUNDANCE function type
        // criterion yields 3 nodes
        nf = testKAM.createNodeFilter();
        FunctionTypeFilterCriteria ftcriteria =
                new FunctionTypeFilterCriteria();
        ftcriteria.setInclude(true);
        ftcriteria.add(PROTEIN_ABUNDANCE);
        ftcriteria.add(COMPLEX_ABUNDANCE);
        nf.add(ftcriteria);
        nodes = testKAM.getNodes(nf);
        assertNotNull(nodes);
        assertEquals(3, nodes.size());
        nodeIt = nodes.iterator();
        n1 = nodeIt.next();
        assertEquals(COMPLEX_ABUNDANCE, n1.getFunctionType());
        n2 = nodeIt.next();
        assertEquals(PROTEIN_ABUNDANCE, n2.getFunctionType());
        KamNode n3 = nodeIt.next();
        assertEquals(PROTEIN_ABUNDANCE, n3.getFunctionType());
    }

    @Test
    public void testMultipleExcludeCriterion() {
        // Test exclude of BIOLOGICAL_PROCESS AND COMPLEX_ABUNDANCE return type
        // criterion yields 3 nodes
        NodeFilter nf = testKAM.createNodeFilter();
        FunctionReturnFilterCriteria rtcriteria =
                new FunctionReturnFilterCriteria();
        rtcriteria.setInclude(false);
        rtcriteria.add(ReturnType.BIOLOGICAL_PROCESS);
        rtcriteria.add(ReturnType.COMPLEX_ABUNDANCE);
        nf.add(rtcriteria);
        Collection<KamNode> nodes = testKAM.getNodes(nf);
        assertNotNull(nodes);
        assertEquals(3, nodes.size());
        Iterator<KamNode> nodeIt = nodes.iterator();
        KamNode n1 = nodeIt.next();
        assertEquals(PROTEIN_ABUNDANCE, n1.getFunctionType());
        assertEquals(ReturnType.PROTEIN_ABUNDANCE, n1.getFunctionType()
                .getReturnType());
        KamNode n2 = nodeIt.next();
        assertEquals(PROTEIN_ABUNDANCE, n2.getFunctionType());
        assertEquals(ReturnType.PROTEIN_ABUNDANCE, n2.getFunctionType()
                .getReturnType());
        KamNode n3 = nodeIt.next();
        assertEquals(KINASE_ACTIVITY, n3.getFunctionType());
        assertEquals(ABUNDANCE, n3.getFunctionType().getReturnType());

        // Test exclude of PROTEIN_ABUNDANCE AND BIOLOGICAL_PROCESS function
        // type criterion yields 2 nodes
        nf = testKAM.createNodeFilter();
        FunctionTypeFilterCriteria ftcriteria =
                new FunctionTypeFilterCriteria();
        ftcriteria.setInclude(false);
        ftcriteria.add(PROTEIN_ABUNDANCE);
        ftcriteria.add(BIOLOGICAL_PROCESS);
        nf.add(ftcriteria);
        nodes = testKAM.getNodes(nf);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        nodeIt = nodes.iterator();
        n1 = nodeIt.next();
        assertEquals(COMPLEX_ABUNDANCE, n1.getFunctionType());
        n2 = nodeIt.next();
        assertEquals(KINASE_ACTIVITY, n2.getFunctionType());
    }

    @Test
    public void testMultipleIncludeExcludeCriteria() {
        // Test exclude of function types:
        //   COMPLEX_ABUNDANCE
        //   PROTEIN_ABUNDANCE
        // and include of return type:
        //   BIOLOGICAL_PROCESS
        // yields 2 nodes:
        //   KINASE_ACTIVITY - because it is not excluded, and default it to include
        //   BIOLOGICAL_PROCESS - because it matches include and does not match exclude
        NodeFilter nf = testKAM.createNodeFilter();
        FunctionTypeFilterCriteria ftcriteria =
                new FunctionTypeFilterCriteria();
        ftcriteria.setInclude(false);
        ftcriteria.add(COMPLEX_ABUNDANCE);
        ftcriteria.add(PROTEIN_ABUNDANCE);
        nf.add(ftcriteria);
        FunctionReturnFilterCriteria rtcriteria =
                new FunctionReturnFilterCriteria();
        rtcriteria.setInclude(true);
        rtcriteria.add(ReturnType.BIOLOGICAL_PROCESS);
        nf.add(rtcriteria);
        Collection<KamNode> nodes = testKAM.getNodes(nf);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        Iterator<KamNode> nodeIt = nodes.iterator();
        KamNode n1 = nodeIt.next();
        assertEquals(KINASE_ACTIVITY, n1.getFunctionType());
        KamNode n2 = nodeIt.next();
        assertEquals(BIOLOGICAL_PROCESS, n2.getFunctionType());
    }

    @Test
    public void testCollidingIncludeExcludeCriteria() {
        // Test include of function type PROTEIN_ABUNDANCE and exclude
        // of return type PROTEIN_ABUNDANCE will return 5 nodes:
        //   COMPLEX_ABUNDANCE - because it was not excluded
        //   PROTEIN_ABUNDANCE x 2 - because the first criteria said to include
        //   KINASE_ACTIVITY - because it was not excluded
        //   BIOLOGICAL_PROCESS - because it was not excluded
        NodeFilter nf = testKAM.createNodeFilter();
        FunctionTypeFilterCriteria ftcriteria =
                new FunctionTypeFilterCriteria();
        ftcriteria.setInclude(true);
        ftcriteria.add(PROTEIN_ABUNDANCE);
        nf.add(ftcriteria);
        FunctionReturnFilterCriteria rtcriteria =
                new FunctionReturnFilterCriteria();
        rtcriteria.setInclude(false);
        rtcriteria.add(ReturnType.PROTEIN_ABUNDANCE);
        nf.add(rtcriteria);
        Collection<KamNode> nodes = testKAM.getNodes(nf);
        assertNotNull(nodes);
        assertEquals(5, nodes.size());
        Iterator<KamNode> nodeIt = nodes.iterator();
        KamNode n1 = nodeIt.next();
        assertEquals(COMPLEX_ABUNDANCE, n1.getFunctionType());
        KamNode n2 = nodeIt.next();
        assertEquals(PROTEIN_ABUNDANCE, n2.getFunctionType());
        KamNode n3 = nodeIt.next();
        assertEquals(PROTEIN_ABUNDANCE, n3.getFunctionType());
        KamNode n4 = nodeIt.next();
        assertEquals(KINASE_ACTIVITY, n4.getFunctionType());
        KamNode n5 = nodeIt.next();
        assertEquals(BIOLOGICAL_PROCESS, n5.getFunctionType());
    }

    @Test
    public void testCollidingExcludeIncludeCriteria() {
        // Test exclude of function type PROTEIN_ABUNDANCE and include
        // of return type PROTEIN_ABUNDANCE will return 3 nodes:
        //   COMPLEX_ABUNDANCE - because it was not excluded/included, so default it to include
        //   KINASE_ACTIVITY - because it was not excluded/included, so default it to include
        //   BIOLOGICAL_PROCESS - because it was not excluded/included, so default it to include
        NodeFilter nf = testKAM.createNodeFilter();
        FunctionTypeFilterCriteria ftcriteria =
                new FunctionTypeFilterCriteria();
        ftcriteria.setInclude(false);
        ftcriteria.add(PROTEIN_ABUNDANCE);
        nf.add(ftcriteria);
        FunctionReturnFilterCriteria rtcriteria =
                new FunctionReturnFilterCriteria();
        rtcriteria.setInclude(true);
        rtcriteria.add(ReturnType.PROTEIN_ABUNDANCE);
        nf.add(rtcriteria);
        Collection<KamNode> nodes = testKAM.getNodes(nf);
        assertNotNull(nodes);
        assertEquals(3, nodes.size());
        Iterator<KamNode> nodeIt = nodes.iterator();
        KamNode n1 = nodeIt.next();
        assertEquals(COMPLEX_ABUNDANCE, n1.getFunctionType());
        KamNode n2 = nodeIt.next();
        assertEquals(KINASE_ACTIVITY, n2.getFunctionType());
        KamNode n3 = nodeIt.next();
        assertEquals(BIOLOGICAL_PROCESS, n3.getFunctionType());
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
