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
package org.openbel.framework.core.compiler.expansion;

import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.core.protonetwork.ProtoNetworkBuilder;

/**
 * {@link ExpansionService} defines the mechanism which expands terms and
 * statements to {@link ProtoNetwork proto network} proto nodes and proto
 * edges.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface ExpansionService {

    /**
     * Expand terms in the {@link ProtoNetwork proto network} based on a set
     * of {@link TermExpansionRule term expansion rules}.
     *
     * @param doc the original {@link Document document} needed as a dependency
     * when using {@link ProtoNetworkBuilder proto network builder}
     * @param pn the {@link ProtoNetwork proto network}
     * @see TermExpansionRule
     */
    public void expandTerms(final Document doc, final ProtoNetwork pn);

    /**
     * Expand statements in the {@link ProtoNetwork proto network} based on a
     * set of {@link StatementExpansionRule statement expansion rules}.
     *
     * @param doc the original {@link Document document} needed as a dependency
     * when using {@link ProtoNetworkBuilder proto network builder}
     * @param pn the {@link ProtoNetwork proto network}
     * @param stmtExpand determines if {@link ProtoNetwork proto network}
     * statements should be expanded to proto edges by inferring relationships
     * using the {@link DistributeStatementExpansionRule distributed rule}
     * @see StatementExpansionRule
     */
    public void expandStatements(final Document doc, final ProtoNetwork pn,
            final boolean stmtExpand);
}
