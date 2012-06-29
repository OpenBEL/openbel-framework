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
package org.openbel.framework.compiler;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNetworkError;
import org.openbel.framework.core.protonetwork.ProtoNetworkDescriptor;

/**
 * BEL compiler phase three interface.
 * <p>
 * Phase three compilation consists of:
 * <ol>
 * <li><b>Stage 1 -- Gene Scaffolding Injection</b><br>
 * Injects gene scaffolding into the source proto-network.<br>
 * (TODO more info into how the source proto-network changes</li>
 * <li><b>Stage 2 -- Protein Family Injection</b><br>
 * Injects protein families into the source proto-network.<br>
 * (TODO more info into how the source proto-network changes)</li>
 * </ol>
 * </p>
 */
public interface DefaultPhaseThree {

    /**
     * Prunes the protein families document of statements that are not relevant
     * to the input proto-network.
     * <p>
     * Note: This method will modify the provided BEL document.
     * </p>
     *
     * @param expand When {@code true}, members of a protein family found within
     * the proto-network will result in protein family injection. Without
     * expansion (i.e., {@code false}, only the protein family itself is used
     * when examining the proto-network.
     * @param d BEL protein families document to be pruned
     * @param p Input {@link ProtoNetwork proto-network}
     * @return {@link DocumentModificationResult Prune result}
     */
    public DocumentModificationResult pruneFamilies(boolean expand, Document d,
            ProtoNetwork p);

    /**
     * Infer and add {@link Statement statements} for protein families and its
     * members when
     * {@link FunctionEnum#getActivities() molecular activity functions} are
     * defined in the {@link ProtoNetwork proto network}.
     *
     * @param d {@link Document}, the protein family document, which cannot be
     * null
     * @param p {@link ProtoNetwork}, the proto network being compiled, which
     * cannot be null
     * @return the {@link DocumentModificationResult result} that indicates
     * status and counts of statements added
     * @throws InvalidArgument Thrown if the {@link Document d} or
     * {@link ProtoNetwork p} is <tt>null</tt>
     */
    public DocumentModificationResult inferFamilies(final Document d,
            ProtoNetwork p);

    /**
     * Prunes the named complexes document of statements that are not relevant
     * to the input proto-network.
     * <p>
     * Note: This method will modify the provided BEL document.
     * </p>
     *
     * @param expand When {@code true}, components of a named complex found
     * within the proto-network will result in named complex injection. Without
     * expansion (i.e., {@code false}), only the named complex itself is used
     * when examining the proto-network.
     * @param d BEL named complexes document to be pruned
     * @param p Input {@link ProtoNetwork proto-network}
     * @return {@link DocumentModificationResult Prune result}
     */
    public DocumentModificationResult pruneComplexes(boolean expand,
            Document d,
            ProtoNetwork p);

    /**
     * Prunes the gene {@link Document document} of {@link Statement statements}
     * that are not relevant to the input {@link ProtoNetwork proto-network}.
     * <p>
     * Note: This method will modify the provided BEL document.
     * </p>
     *
     * @param d document BEL gene scaffolding document to be pruned
     * @param p Input {@link ProtoNetwork proto-network}
     * @return {@link DocumentModificationResult Prune result}
     */
    public DocumentModificationResult pruneGene(Document d, ProtoNetwork p);

    /**
     * Prunes orthology BEL {@link Document document} by:
     * <p>
     * Prune the {@link Document document}, relative to the
     * {@link ProtoNetwork proto network} being compiled, using equivalencing
     * to match up BEL terms.
     * </p>
     *
     * @param d {@link Document} the orthology BEL document, which cannot be
     * {@code null}
     * @param pn {@link ProtoNetwork} the compiled proto network, which cannot
     * be {@code null}
     * @return {@link DocumentModificationResult} containing the pruning
     * results
     * @throws InvalidArgument Thrown if {@code d} or {@code pn} is
     * {@code null}
     */
    public DocumentModificationResult pruneOrthologyDocument(final Document d,
            final ProtoNetwork pn);

    /**
     * Compiles {@link Document BEL common documents} to {@link ProtoNetwork
     * proto-networks}.
     *
     * @param document {@link Document} the document to compile
     * @return {@link ProtoNetwork} the compiled proto network
     */
    public ProtoNetwork compile(final Document d);

    /**
     * Deserialize and merge the proto network in <tt>protoNetworkSource</tt>
     * into the proto network in <tt>protoNetworkSource</tt>.
     *
     * @param destination {@link ProtoNetwork}, the proto-network containing the
     * merged results
     * @param source {@link ProtoNetwork}, the proto-network to merge into
     * {@code destination}
     * @throws ProtoNetworkError Thrown if an error occurred reading the proto-
     * network from the {@code source} descriptor.
     */
    public void merge(ProtoNetwork destination, ProtoNetwork source)
            throws ProtoNetworkError;

    /**
     * Writes the {@link ProtoNetwork proto-network}.
     *
     * @param protoNetworkRootPath {@link String}, the proto network root path
     * to write to
     * @param protoNetwork {@link ProtoNetwork}, the proto network to write
     */
    public ProtoNetworkDescriptor write(final String path,
            final ProtoNetwork network) throws ProtoNetworkError;

    /**
     * XXX: njb
     */
    public static class DocumentModificationResult {

        private int totalStatements;
        private int deltaStatements;
        private boolean success;
        private List<String> warnings;
        private List<String> errors;

        public boolean isSuccess() {
            return success;
        }

        public boolean isFailure() {
            return !success;
        }

        public int getRemainingStatements() {
            return totalStatements + deltaStatements;
        }

        public int getTotalStatements() {
            return totalStatements;
        }

        public void setTotalStatements(int i) {
            totalStatements = i;
        }

        public int getDeltaStatements() {
            return deltaStatements;
        }

        public void setDeltaStatements(int i) {
            deltaStatements = i;
        }

        public void setSuccess(boolean b) {
            success = b;
        }

        public void addWarning(String s) {
            if (warnings == null) {
                warnings = new ArrayList<String>();
            }
            warnings.add(s);
        }

        public List<String> getWarnings() {
            if (warnings == null) {
                return emptyList();
            }
            return warnings;
        }

        public void addError(String s) {
            if (errors == null) {
                errors = new ArrayList<String>();
            }
            errors.add(s);
        }

        public List<String> getErrors() {
            if (errors == null) {
                return emptyList();
            }
            return errors;
        }

    }
}
