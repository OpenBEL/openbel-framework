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
package org.openbel.framework.api;

import java.util.HashMap;
import java.util.Map;

import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.internal.KamInfoUtil;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;

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
