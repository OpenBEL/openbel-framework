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

import java.util.HashMap;
import java.util.Map;

import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.api.internal.KamInfoUtil;
import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;

/**
 * KamTestUtil is a test utility to create a {@link Kam} by providing
 * {@link TestKamNode} and {@link TestKamEdge} objects.  These objects serve
 * as container objects so that the utility can construct true {@link KamNode}
 * and {@link KamEdge} objects.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class KamTestUtil {

    /**
     * Creates a test {@link Kam} by using an array of {@link TestKamNode}
     * objects and an array of {@link TestKamEdge} objects.
     *
     * @param testKamInfo {@link KamInfo}, the test kam info object created
     * using {@link KamInfoUtil}
     * @param testKamNodes {@link TestKamNode TestKamNode[]}, array of test
     * kam nodes
     * @param testKamEdges {@link TestKamEdge TestKamEdge[]}, array of test
     * kam edges
     * @return the created test {@link Kam}
     */
    public static Kam createKam(final KamInfo testKamInfo,
            final TestKamNode[] testKamNodes,
            final TestKamEdge[] testKamEdges) {
        if (testKamNodes == null) {
            throw new InvalidArgument("testKamNodes", testKamNodes);
        }

        if (testKamEdges == null) {
            throw new InvalidArgument("testKamEdges", testKamEdges);
        }

        KamImpl testKAM = new KamImpl(testKamInfo);

        Map<TestKamNode, KamNode> newNodes =
                new HashMap<TestKamNode, Kam.KamNode>();
        for (final TestKamNode testKamNode : testKamNodes) {
            newNodes.put(testKamNode, testKAM.createNode(testKamNode.id,
                    testKamNode.functionType,
                    testKamNode.label));
        }

        for (final TestKamEdge testKamEdge : testKamEdges) {
            final KamNode newSource = newNodes.get(testKamEdge.sourceNode);
            final KamNode newTarget = newNodes.get(testKamEdge.targetNode);

            if (newSource == null || newTarget == null) {
                throw new IllegalStateException(
                        "Test KAM nodes could not be found.");
            }

            testKAM.createEdge(testKamEdge.id, newSource, testKamEdge.relType,
                    newTarget);
        }

        return testKAM;
    }

    /**
     * TestKamNode is a container for kam node fields.  This object serves
     * as a convence to the utility user.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     */
    public static final class TestKamNode {
        private final Integer id;
        private final FunctionEnum functionType;
        private final String label;

        public TestKamNode(final Integer id,
                final FunctionEnum functionType,
                final String label) {
            this.id = id;
            this.functionType = functionType;
            this.label = label;
        }
    }

    /**
     * TestKamEdge is a container for kam edge fields.  This object serves
     * as a convence to the utility user.
     *
     * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
     */
    public static final class TestKamEdge {
        private final Integer id;
        private final TestKamNode sourceNode;
        private final RelationshipType relType;
        private final TestKamNode targetNode;

        public TestKamEdge(final Integer id,
                final TestKamNode sourceNode,
                final RelationshipType relType,
                final TestKamNode targetNode) {
            this.id = id;
            this.sourceNode = sourceNode;
            this.relType = relType;
            this.targetNode = targetNode;
        }
    }
}
