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
package org.openbel.framework.api;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.api.internal.KAMCatalogDao.AnnotationFilter;
import org.openbel.framework.api.internal.KAMCatalogDao.KamFilter;
import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.api.internal.KAMCatalogDao.NamespaceFilter;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.AnnotationType;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.BelDocumentInfo;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.BelStatement;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.BelTerm;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.Citation;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.Namespace;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.TermParameter;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.CitationType;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;

/**
 * Provides access to knowledge assembly models, or KAMs.
 *
 * @version 3.0.0 API improvements to mitigate a poor design
 */
public interface KAMStore {

    /**
     * Returns {@code true} if the KAM exists, {@code false} otherwise.
     *
     * @param k Non-null {@link Kam}
     * @return boolean
     * @throws InvalidArgument Thrown if a null argument is provided
     */
    public boolean exists(Kam k);

    /**
     * Returns {@code true} if the KAM identified by the {@link KamInfo} exists,
     * {@code false} otherwise.
     *
     * @param info Non-null {@link KamInfo}
     * @return boolean
     * @throws InvalidArgument Thrown if a null argument is provided
     */
    public boolean exists(KamInfo info);

    /**
     * Returns {@code true} if the KAM identified by name exists, {@code false}
     * otherwise.
     *
     * @param name Non-null KAM name
     * @return boolean
     * @throws InvalidArgument Thrown if a null argument is provided
     */
    public boolean exists(String name);

    /**
     * Closes the connection to a KAM and performs any necessary cleanup of
     * resources. If the KAM has not been retrieved, this operation is a no-op.
     *
     * @param k {@link Kam}
     * @throws InvalidArgument Thrown if KAM argument is null
     */
    public void close(Kam k);

    /**
     * Performs any necessary cleanup of caches and closes any connections to
     * store.
     */
    public void teardown();

    /**
     * Gets the KAM catalog, returning a {@link List list} of {@link KamInfo}
     * objects representing what is in the KAM catalog. This list will be empty
     * if the catalog has no KAMs.
     *
     * @return List of {@KamInfo}
     * @since 3.0.0
     */
    public List<KamInfo> getCatalog();

    /**
     * Gets the {@link KamInfo} for a KAM name.
     *
     * @param name Non-null KAM name
     * @return {@link KamInfo}; null if not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public KamInfo getKamInfo(String name);

    /**
     * Gets a KAM, optionally filtering the {@link KamEdge edges} and
     * {@link KamNode nodes} before loading.
     *
     * @param info Non-null {@KamInfo}
     * @param fltr {@KamFilter}; may be null
     * @return {@link Kam}; null if not found
     * @throws KAMStoreException Thrown if a database error occurred
     * @throws InvalidArgument Thrown if {@code info} is null
     */
    public Kam getKam(KamInfo info, KamFilter fltr);

    /**
     * Gets a KAM by KAM name.
     *
     * @param name Non-null KAM name
     * @return {@link Kam}; null if not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public Kam getKam(String name);

    /**
     * Gets a KAM by {@link KamInfo}.
     *
     * @param info Non-null {@link KamInfo}
     * @return {@link Kam}; null if not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public Kam getKam(KamInfo info);

    /**
     * Get the list of annotation types for the provided KAM.
     *
     * @param k Non-null {@link Kam}
     * @return {@link List} of {@link AnnotationType annotation types}; null if
     * the KAM is not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<AnnotationType> getAnnotationTypes(Kam k);

    /**
     * Get the list of annotation types for the provided KAM.
     *
     * @param info Non-null {@link KamInfo}
     * @return {@link List} of {@link AnnotationType annotation types}; null if
     * the KAM is not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<AnnotationType> getAnnotationTypes(KamInfo info);

    /**
     * Get the list of domain values for the annotation type in the KAM
     * identified by the {@link KamInfo}.
     *
     * @param info Non-null {@link KamInfo}
     * @param type Non-null {@link AnnotationType}
     * @return {@link List} of {@link String} annotation type domain values
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<String> getAnnotationTypeDomainValues(KamInfo info,
            AnnotationType type);

    /**
     * Get the list of {@link BelDocumentInfo} for the provided KAM.
     *
     * @param k Non-null {@link Kam}
     * @return {@link List} of {@link BelDocumentInfo}; null if the KAM is not
     * found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<BelDocumentInfo> getBelDocumentInfos(Kam k);

    /**
     * Get the list of {@link BelDocumentInfo} in the KAM identified by the
     * {@link KamInfo}.
     *
     * @param info Non-null {@link KamInfo}
     * @return {@link List} of {@link BelDocumentInfo}; null if the KAM is not
     * found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<BelDocumentInfo> getBelDocumentInfos(KamInfo info);

    /**
     * Get the {@link Namespace} for the provided KAM with a resource
     * location defined by {@code resourceLocation}.
     *
     * @param k Non-null {@link Kam}
     * @param resourceLocation Non-null {@link String} the namespace resource
     * location that represents the namespace
     * @return {@link Namespace} in the KAM, or null if the namespace or KAM
     * does not exist
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public Namespace getNamespace(Kam k, String resourceLocation);

    /**
     * Get the list of {@link Namespace namespaces} for the provided KAM.
     *
     * @param k Non-null {@link Kam}
     * @return {@link List} of {@link Namespace namespaces}; null if the KAM is
     * not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<Namespace> getNamespaces(Kam k);

    /**
     * Get the list of {@link Namespace namespaces} for the KAM identified by
     * the {@link KamInfo}.
     *
     * @param info Non-null {@link KamInfo}
     * @return {@link List} of {@link Namespace namespaces}; null if the KAM is
     * not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<Namespace> getNamespaces(KamInfo info);

    /**
     * Get the list of {@link BelStatement} supporting an {@link KamEdge edge}
     * with an optional {@link AnnotationFilter annotation filter}.
     *
     * @param edge {@link KamEdge}
     * @param annotationFilter {@link AnnotationFilter}; may be null
     * @return {@link List} of {@link BelStatement BEL statements}; null if the
     * KAM is not found
     * @throws InvalidArgument Thrown if {@code edge} is null
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<BelStatement> getSupportingEvidence(KamEdge edge,
            AnnotationFilter fltr);

    /**
     * Get the list of {@link BelStatement} supporting an {@link KamEdge edge}.
     *
     * @param edge {@link KamEdge}
     * @return {@link List} of {@link BelStatement BEL statements}; null if the
     * KAM is not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<BelStatement> getSupportingEvidence(KamEdge edge);

    public Map<KamEdge, List<Statement>> getSupportingEvidence(Collection<KamEdge> edges);

    public Map<KamEdge, List<Statement>> getSupportingEvidence(Collection<KamEdge> edges, AnnotationFilter filter);

    /**
     * Get the list of {@link BelTerm} for a {@link KamNode node}.
     *
     * @param node {@link KamNode}
     * @return {@link List} of {@link BelTerm}; null if the KAM is not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<BelTerm> getSupportingTerms(KamNode node);

    /**
     * Get the list of {@link BelTerm} for a {@link KamNode node}.
     *
     * @param node Non-null {@KamNode}
     * @param removeDups {@code true} if duplicate terms should be removed,
     * {@code false} otherwise
     * @return {@link List} of {@link BelTerm}; null if the KAM is not found
     * @throws InvalidArgument Thrown if {@code node} is null
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<BelTerm> getSupportingTerms(KamNode node, boolean removeDups);

    /**
     * Returns a list of {@link BelTerm} objects which support the
     * {@link KamNode}. If {@code removeDups} is true, the list of terms
     * returned is reduced to the list of unique terms based on namespace
     * identifiers. If {@code fltr} is set, the list of terms
     * reported is initially filtered with respect to the filter criteria.
     *
     * @param node {@link KamNode}
     * @param fltr Optional {@link NamespaceFilter}
     * @param removeDups {@code true} if duplicate terms should be removed,
     * {@code false} otherwise
     * @return {@link List} of {@link BelTerm}; null if the KAM is not found
     * @throws InvalidArgument Thrown if {@code node} is null
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<BelTerm> getSupportingTerms(KamNode node, boolean removeDups,
            NamespaceFilter fltr);

    /**
     * Retrieves the term parameters for a {@link BelTerm} in the KAM identified
     * by the {@link KamInfo}.
     *
     * @param info Non-null {@link KamInfo}
     * @param term Non-null The {@link BelTerm BEL term} to find parameters for
     * @return {@link List} of {@link TermParameter}; null if the KAM is not
     * found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<TermParameter> getTermParameters(KamInfo info, BelTerm term);

    /**
     * Retrieves a {@link KamNode} for a term string within the
     * specified KAM.
     *
     * @param k Non-null {@link Kam}
     * @param termString Non-null term string;
     * @return {@link KamNode KAM node}; null if the KAM or {@link KamNode} is
     * not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public KamNode getKamNode(Kam k, String termString);

    /**
     * Retrieves a {@link KamNode} for a {@link BelTerm} within the specified
     * KAM.
     *
     * @param k Non-null {@link Kam}
     * @param term Non-null {@link BelTerm}
     * @return {@link KamNode KAM node}; null if the KAM or {@link KamNode} is
     * not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public KamNode getKamNode(Kam k, BelTerm term);

    /**
     * Find all {@link KamNode nodes} that match the given {@link FunctionEnum
     * function} and <em>were compiled with</em> the
     * given {@link Namespace namespace}, and parameter value. <br>
     * This method will <em>not</em> return nodes that were compiled with
     * equivalents of the given namespace and parameter value. Use
     * {@link #getKamNodes(Kam, FunctionEnum, SkinnyUUID)} to search all
     * equivalents.
     *
     * @param k Non-null {@link Kam KAM}
     * @param function Non-null {@link FunctionEnum}
     * @param ns Non-null {@link Namespace}
     * @param paramValue Non-null Parameter value
     * @return {@link List} of {@link KamNode KAM node}; null if the KAM is not
     * found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<KamNode> getKamNodes(Kam k, FunctionEnum function,
            Namespace ns, String paramValue);

    /**
     * Find all {@link KamNode KAM nodes} <em>compiled with</em> the
     * given {@link Namespace namespace}, and parameter value. All
     * {@link FunctionEnum functions} will be returned.<br>
     * This method will <em>not</em> return nodes that were compiled with
     * equivalents of the given namespace and parameter value. Use
     * {@link #getKamNodes(Kam, SkinnyUUID)} to search all equivalents.
     *
     * @param k Non-null {@link Kam}
     * @param ns Non-null {@link Namespace}
     * @param paramValue Non-null parameter value
     * @return {@link List} of {@link KamNode KAM node}; null if the KAM is not
     * found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<KamNode> getKamNodes(Kam k, Namespace ns, String paramValue);

    /**
     * Find all {@link KamNode nodes} in the KAM that contain parameters
     * identified by the given {@link SkinnyUUID skinny UUID}. This method finds
     * nodes where the parameter occurs at any position. All
     * {@link FunctionEnum functions} will be returned.
     *
     * @param k Non-null {@link Kam kam}
     * @param function Non-null {@link FunctionEnum function}
     * @param uuid Non-null {@link SkinnyUUID}
     * @return {@link List} of {@link KamNode}; null if the KAM is not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<KamNode> getKamNodes(Kam k, FunctionEnum function,
            SkinnyUUID uuid);

    /**
     * Find all {@link KamNode nodes} in the KAM that contain parameters
     * identified by the given {@link SkinnyUUID skinny UUIDs}. This method
     * finds nodes where the parameter occurs at any position. All
     * {@link FunctionEnum functions} will be returned.
     *
     * @param k Non-null {@link Kam kam}
     * @param uuid Non-null {@link SkinnyUUID}
     * @return {@link List} of {@link KamNode}; null if the KAM is not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<KamNode> getKamNodes(Kam k, SkinnyUUID uuid);

    /**
     * Find all {@link KamNode nodes} that share node parameters with the
     * example {@link KamNode node}.
     * <p>
     * For instance, given a kam with the following nodes:
     * <ul>
     * <li><tt>complex(p(1), g(2))</tt></li>
     * <li><tt>p(1)</tt></li>
     * <li><tt>g(2)</tt></li>
     * <li><tt>r(2)</tt></li>
     * <li><tt>act(r(2))</tt></li>
     * </ul>
     * Providing the <tt>complex(p(1), g(2))</tt> {@link KamNode node} will
     * yield the following {@link KamNode nodes}:
     * <ul>
     * <li><tt>p(1)</tt></li>
     * <li><tt>g(2)</tt></li>
     * <li><tt>r(2)</tt></li>
     * <li><tt>act(r(2))</tt></li>
     * </ul>
     * </p>
     *
     * @param k Non-null {@link Kam}
     * @param node Non-null example {@link KamNode node}
     * @return {@link List} of {@link KamNode}; null if the KAM is not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<KamNode> getKamNodes(Kam k, KamNode node);

    /**
     * Returns the {@link KamNode KAM Node} given a BEL term signature and one
     * or more {@link SkinnyUUID uuids}.  Returns {@code null} if a
     * {@link KamNode KAM Node} could not be found.
     *
     * @param kam {@link Kam}; may not be {@code null}
     * @param term {@link String}, the term signature; may not be {@code null}
     * @param fx {@link FunctionEnum}; the term functionmay not be {@code null}
     * @param uuids {@code SkinnyUUID[]}; may not be {@code null} or empty
     * @return {@link Integer KAM node id} or {@code null} if one could not be
     * found
     * @throws InvalidArgument when {@code term} is {@code null}, {@code kam}
     * is {@code null}, {@code uuids} is {@code null}, or {@code uuids} is
     * empty
     * @throws KAMStoreException when a database error occurred
     */
    public KamNode getKamNodeForTerm(Kam kam, String term, FunctionEnum fx,
            SkinnyUUID[] uuids);

    /**
     * Returns all citations associated with the {@link BelDocumentInfo} in the
     * KAM identified by the {@link KamInfo}.
     *
     * @param info Non-null {@link KamInfo}
     * @param docInfo Non-null {@link BelDocumentInfo}
     * @return {@link List} of {@link Citation}; null if the KAM is not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<Citation> getCitations(KamInfo info, BelDocumentInfo docInfo);

    /**
     * Returns all {@link Citation citations} associated with the
     * {@link BelDocumentInfo} object in the KAM identified by the
     * {@link KamInfo}.
     *
     * @param info Non-null {@link KamInfo}
     * @param docInfo Non-null {@link BelDocumentInfo}
     * @param citation Non-null {@link CitationType}
     * @return {@link List} of {@link Citation}; null if the KAM is not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<Citation> getCitations(KamInfo info, BelDocumentInfo docinfo,
            CitationType citation);

    /**
     * Returns all {@link Citation citations} associated with the KAM identified
     * by the {@link KamInfo}.
     *
     * @param info Non-null {@link KamInfo}
     * @param citation Non-null {@link CitationType}
     * @return {@link List} of {@link Citation}; null if the KAM is not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<Citation> getCitations(KamInfo info, CitationType citation);

    /**
     * Returns all {@link Citation citations} matching the specified
     * {@link CitationType citation types} and reference identifiers in the KAM
     * identified by the {@link KamInfo}.
     *
     * @param info Non-null {@link KamInfo}
     * @param types Non-null {@link CitationType}
     * @param refIDs Non-null reference identifiers
     * @return {@link List} of {@link Citation}; null if the KAM is not found
     * @throws InvalidArgument Thrown if a null argument is provided
     * @throws KAMStoreException Thrown if a database error occurred
     */
    public List<Citation> getCitations(KamInfo info, CitationType types,
            String... refIDs);

    /**
     * Collapse a {@link KamNode kam node} to another {@link KamNode kam node}.
     *
     * @param info {@link KamInfo}; may not be {@code null}
     * @param collapsing {@link KamNode} node to collapse; may not be
     * {@code null}
     * @param collapseTo {@link KamNode} the collapse target; may not be
     * {@code null}
     * @return {@code true} if the collapse occurred; {@code false} if the
     * collapse did not occur
     */
    public boolean collapseKamNode(KamInfo info, KamNode collapsing,
            KamNode collapseTo);

    /**
     * Remove all {@link KamEdge kam edges} from the {@link Set}.
     *
     * @param edgeIds {@code int[]} array of edge ids
     * @return {@code int} kam edges deleted
     * @throws SQLException when a SQL error occurred deleting records
     * @throws InvalidArgument when {@code edges} is {@code null}
     */
    public int removeKamEdges(KamInfo info, int[] edgeIds);

    /**
     * Remove {@link KamEdge kam edges} for a specific
     * {@link RelationshipType relationship}.
     *
     * @param info {@link KamInfo}; may not be {@code null}
     * @param relationship {@link RelationshipType}; may not be {@code null}
     * @return {@code int} records deleted (kam edges + statements)
     */
    public int removeKamEdges(KamInfo info, RelationshipType relationship);

    /**
     * Coalesce duplicate {@link KamEdge kam edges} to one
     * {@link KamEdge kam edge}.  The statements for each duplicate
     * {@link KamEdge kam edge} will be remapped and then that
     * {@link KamEdge kam edge} will be removed.
     *
     * @param info {@link KamInfo}; may not be {@code null}
     * @return {@code int} kam edges coalesced/removed
     */
    public int coalesceKamEdges(KamInfo info);
}
