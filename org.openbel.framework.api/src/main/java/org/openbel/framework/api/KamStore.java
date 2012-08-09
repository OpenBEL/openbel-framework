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

import java.util.List;
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
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;

/**
 * Provides access to knowledge assembly models, or KAMs.
 */
public interface KamStore {

    /**
     * Closes the connection to a KAM and performs any necessary cleanup of
     * resources.
     * 
     * @param k An open KAM
     * @throws InvalidArgument Thrown if KAM argument is null
     */
    public void close(Kam k);

    /**
     * Performs any necessary cleanup of caches and closes any connections to
     * store.
     */
    public void teardown();

    /**
     * Reads the KAM catalog and returns a list of {@link KamInfo} indicating
     * which KAMs are available for use.
     * 
     * @return List of {@KamInfo}
     * @throws KamStoreException Thrown if the KAM catalog could not be
     * retrieved
     */
    public List<KamInfo> readCatalog() throws KamStoreException;

    /**
     * Returns the {@link KamInfo} found for the provided KAM name.
     * 
     * @param name Non-null KAM name
     * @return {@link KamInfo}
     * @throws KamStoreException Thrown if the {@link KamInfo} could not be
     * retrieved
     * @throws InvalidArgument Thrown if a null argument is provided
     */
    public KamInfo getKamInfo(String name) throws KamStoreException;

    /**
     * Loads and returns a KAM from the {@link KamStoreImpl}. The {@link KamFilter}
     * is used to filter the {@link KamEdge KAM edges} and {@link KamNode KAM
     * nodes} before loading so the resultant KAM is reduced.
     * 
     * @param info {@KamInfo}
     * @param fltr {@KamFilter}
     * @return {@link Kam}
     * @throws KamStoreException Thrown if the {@link Kam} could not be
     * retrieved
     */
    public Kam getKam(KamInfo info, KamFilter fltr) throws KamStoreException;

    /**
     * Returns a {@link Kam} based on the name of the KAM in the catalog.
     * 
     * @param name KAM name (case insensitive)
     * @return {@link Kam}
     * @throws KamStoreException Thrown if a KAM is not found for the specified
     * name
     */
    public Kam getKam(String name) throws KamStoreException;

    /**
     * Loads and returns a KAM from the KAM Store.
     * 
     * @param info KAM info
     * @return Kam
     * @throws InvalidArgument Thrown if {@code kamInfo} is null
     * @throws KamStoreException Thrown if a KAM is not found for the provided
     * {@link KamInfo}
     */
    public Kam getKam(KamInfo info) throws KamStoreException;

    /**
     * Get the list of annotation types for the provided KAM.
     * 
     * @param k {@link Kam}
     * @return List of {@link AnnotationType annotation types}; may be empty
     * @throws KamStoreException Thrown if a KAM is not found for the provided
     * {@link Kam}
     */
    public List<AnnotationType> getAnnotationTypes(Kam k)
            throws KamStoreException;

    /**
     * Get the list of annotation types for the provided KAM.
     * 
     * @param info {@link KamInfo}
     * @return List of {@link AnnotationType annotation types}; may be empty
     * @throws KamStoreException Thrown if the KAM or annotation types could not
     * be retrieved
     */
    public List<AnnotationType> getAnnotationTypes(KamInfo info)
            throws KamStoreException;

    /**
     * Get the list of domain string values for the annotation type in the KAM
     * identified by the supplied {@link KamInfo}.
     * 
     * @param info {@link KamInfo}
     * @param type The {@link AnnotationType annotation type}
     * @return {@link List} of string the annotation type domain value list
     * @throws KamStoreException Thrown if the KAM or annotation types could not
     * be retrieved
     */
    public List<String> getAnnotationTypeDomainValues(KamInfo info,
            AnnotationType type) throws KamStoreException;

    /**
     * Get the list of {@link BelDocumentInfo} for the provided KAM.
     * 
     * @param k {@link Kam}
     * @return List of {@link BelDocumentInfo}; may be empty
     * @throws KamStoreException Thrown if the list of {@link BelDocumentInfo}
     * could not be retrieved
     */
    public List<BelDocumentInfo> getBelDocumentInfos(Kam k)
            throws KamStoreException;

    /**
     * Get the list of {@link BelDocumentInfo} for the provided {@link KamInfo}.
     * 
     * @param info KAM info
     * @return List of {@link BelDocumentInfo}; may be empty
     * @throws KamStoreException Thrown if the list of {@link BelDocumentInfo}
     * could not be retrieved
     */
    public List<BelDocumentInfo> getBelDocumentInfos(KamInfo info)
            throws KamStoreException;

    /**
     * Get the {@link Namespace} in the provided {@link Kam} with a resource
     * location defined by <tt>resourceLocation</tt>.
     * 
     * @param k {@link Kam} the KAM to retrieve the namespaces from
     * @param resourceLocation {@link String} the namespace resource location
     * that represents the namespace
     * @return {@link Namespace} in the KAM, or null if it one does not exist
     * for the <tt>resourceLocation</tt>
     * @throws KamStoreException Thrown if the namespaces could not be retrieved
     * for the {@link Kam}
     */
    public Namespace getNamespace(Kam k, String resourceLocation)
            throws KamStoreException;

    /**
     * Get the list of {@link Namespace namespaces} for a {@link Kam}.
     * 
     * @param k {@link Kam KAM}
     * @return List of {@link Namespace namespaces}; may be empty
     * @throws KamStoreException Thrown if the list of {@link Namespace
     * namespaces} could not be retrieved
     */
    public List<Namespace> getNamespaces(Kam k) throws KamStoreException;

    /**
     * Get the list of {@link Namespace namespaces} for a {@link KamInfo}.
     * 
     * @param info {@link KamInfo KAM info}
     * @return List of {@link Namespace namespaces}
     * @throws KamStoreException Thrown if the list of {@link Namespace
     * namespaces} could not be retrieved
     */
    public List<Namespace> getNamespaces(KamInfo info) throws KamStoreException;

    /**
     * Get the list of {@link BelStatement} supporting an {@link KamEdge edge}
     * with the supplied {@link AnnotationFilter annotation filter}.
     * 
     * @param edge {@link KamEdge KAM edge}
     * @param annotationFilter Annotation filter
     * @return List of {@link BelStatement BEL statements}; may be empty
     * @throws KamStoreException Thrown if the list of {@link BelStatement BEL
     * statements} could not be retrieved
     */
    public List<BelStatement> getSupportingEvidence(KamEdge edge,
            AnnotationFilter fltr) throws KamStoreException;

    /**
     * Get the list of {@link BelStatement} supporting an {@link KamEdge edge}.
     * 
     * @param edge {@link KamEdge KAM edge}
     * @return List of {@link BelStatement BEL statements}; may be empty
     * @throws KamStoreException Thrown if the list of {@link BelStatement BEL
     * statements} could not be retrieved
     */
    public List<BelStatement> getSupportingEvidence(KamEdge edge)
            throws KamStoreException;

    /**
     * Get the list of {@link BelTerm BEL terms} for a {@link KamNode node}.
     * 
     * @param node {@link KamNode KAM node}
     * @return List of {@link BelTerm BEL terms}; may be empty
     * @throws KamStoreException Thrown if the list of {@link BelTerm BEL terms}
     * could not be retrieved
     */
    public List<BelTerm> getSupportingTerms(KamNode node)
            throws KamStoreException;

    /**
     * Get the list of {@link BelTerm BEL terms} for a {@link KamNode node}.
     * 
     * @param node {@KamNode KAM node}
     * @param removeDups Flag to control removal of duplicate terms
     * @return List of {@link BelTerm BEL terms}
     * @throws KamStoreException Thrown if the list of {@link BelTerm BEL terms}
     * could not be retrieved
     */
    public List<BelTerm> getSupportingTerms(KamNode node, boolean removeDups)
            throws KamStoreException;

    /**
     * Returns a list of {@link BelTerm BEL term} objects which support the KAM
     * node. If {@code removeDuplicates} is true, the list of terms returned is
     * reduced to the list of unique terms based on namespace identifiers. If
     * {@code namespaceFilter} is set, the list of terms reported is initially
     * filtered with respect to the filter criteria.
     * 
     * @param node KAM node
     * @param fltr Namespace filter
     * @param removeDups Flag to control removal of duplicate terms
     * @return List of {@link BelTerm BEL terms}
     * @throws KamStoreException Thrown if the list of {@link BelTerm BEL terms}
     * could not be retrieved
     */
    public List<BelTerm> getSupportingTerms(KamNode node, boolean removeDups,
            NamespaceFilter fltr) throws KamStoreException;

    /**
     * Retrieves the term parameters for a {@link BelTerm BEL Term} in a KAM
     * pointed to by a {@link KamInfo KAM info}.
     * 
     * @param info The {@link KamInfo KAM info} to find term in
     * @param term The {@link BelTerm BEL term} to find parameters for
     * @return {@link List} of {@link TermParameter term parameters}
     * @throws KamStoreException Thrown if a KAM Store error occurred while
     * retrieving the term parameters
     */
    public List<TermParameter> getTermParameters(KamInfo info, BelTerm term)
            throws KamStoreException;

    /**
     * Retrieves a {@link KamNode KAM node} for a term string within the
     * specified KAM.
     * 
     * @param k {@link Kam KAM}
     * @param termString Non-null term string
     * @return {@link KamNode KAM node}
     * @throws KamStoreException Thrown if an error occurred while retrieving
     * the {@link KamNode KAM node}
     */
    public KamNode getKamNode(Kam k, String termString)
            throws KamStoreException;

    /**
     * Retrieves a {@link KamNode KAM node} for a {@link BelTerm BEL term}
     * within the specified KAM.
     * 
     * @param k {@link Kam KAM}
     * @param term {@link BelTerm BEL term}
     * @return {@link KamNode KAM node}
     * @throws KamStoreException Thrown if an error occurred while retrieving
     * the {@link KamNode KAM node}
     */
    public KamNode getKamNode(Kam k, BelTerm term) throws KamStoreException;

    /**
     * Find all {@link KamNode KAM nodes} that match the given
     * {@link FunctionEnum function} and <em>that were compiled with</em> the
     * given {@link Namespace namespace}, and parameter value. <br>
     * This method will <em>not</em> return nodes that were compiled with
     * equivalents of the given namespace and parameter value. Use
     * {@link #getKamNodes(Kam, FunctionEnum, SkinnyUUID)} to search all
     * equivalents.
     * 
     * @param k {@link Kam KAM}
     * @param function {@link FunctionEnum}
     * @param ns {@link Namespace}
     * @param paramValue Parameter value
     * @return {@link List} of {@link KamNode KAM node}
     * @throws KamStoreException Throw if an error occurred while retrieving the
     *             {@link KamNode KAM nodes}
     */
    public List<KamNode> getKamNodes(Kam k, FunctionEnum function,
            Namespace ns, String paramValue) throws KamStoreException;

    /**
     * Find all {@link KamNode KAM nodes} <em>that were compiled with</em> the
     * given {@link Namespace namespace}, and parameter value. All
     * {@link FunctionEnum functions} will be returned.<br>
     * This method will <em>not</em> return nodes that were compiled with
     * equivalents of the given namespace and parameter value. Use
     * {@link #getKamNodes(Kam, SkinnyUUID)} to search all equivalents.
     * 
     * @param k {@link Kam KAM}
     * @param ns {@link Namespace}
     * @param paramValue Parameter value
     * @return {@link List} of {@link KamNode KAM node} that were compiled with
     *         the given {@link Namespace} and value
     * @throws KamStoreException Thrown if an error occurred while retrieving
     *             the {@link KamNode KAM nodes}
     */
    public List<KamNode> getKamNodes(Kam k, Namespace ns, String paramValue)
            throws KamStoreException;

    /**
     * Find all {@link KamNode}s in the Kam that contain parameters identified
     * by the given {@link SkinnyUUID}s. This method finds nodes where the
     * parameter occurs at any position. All {@link FunctionEnum functions} will
     * be returned.
     * 
     * @param uuid {@link SkinnyUUID} identifying the parameter
     * 
     * @param k {@link Kam kam}
     * @param function {@link FunctionEnum function}
     * @param {@link SkinnyUUID uuid} identifying the parameter
     * @return {@link Set} of {@link KamNode}s containing the parameter
     *         identified by the {@link SkinnyUUID}. Note this identifier is
     *         namespace-agnostic; KamNodes that use any equivalent of the given
     *         UUID will be returned.
     * @throws KamStoreException Thrown if an error occurred while retrieving
     *             the {@link KamNode KAM nodes}
     */
    public List<KamNode> getKamNodes(Kam k, FunctionEnum function,
            SkinnyUUID uuid) throws KamStoreException;

    /**
     * Find all {@link KamNode}s in the Kam that contain parameters identified
     * by the given {@link SkinnyUUID}s. This method finds nodes where the
     * parameter occurs at any position. All {@link FunctionEnum functions} will
     * be returned.
     * 
     * @param k {@link Kam kam}
     * @param {@link SkinnyUUID uuid} identifying the parameter
     * @return {@link Set} of {@link KamNode}s containing the parameter
     *         identified by the {@link SkinnyUUID}. Note this identifier is
     *         namespace-agnostic; KamNodes that use any equivalent of the given
     *         UUID will be returned.
     * @throws KamStoreException Thrown if an error occurred while retrieving
     *             the {@link KamNode KAM nodes}
     */
    public List<KamNode> getKamNodes(Kam k, SkinnyUUID uuid)
            throws KamStoreException;

    /**
     * Find all {@link KamNode KAM nodes} that share node parameters with the
     * example {@link KamNode KAM node}.
     * <p>
     * For instance, given a kam with the following nodes:
     * <ul>
     * <li><tt>complex(p(1), g(2))</tt></li>
     * <li><tt>p(1)</tt></li>
     * <li><tt>g(2)</tt></li>
     * <li><tt>r(2)</tt></li>
     * <li><tt>act(r(2))</tt></li>
     * </ul>
     * Providing the <tt>complex(p(1), g(2))</tt> {@link KamNode KAM node} will
     * yield the following {@link KamNode KAM nodes}:
     * <ul>
     * <li><tt>p(1)</tt></li>
     * <li><tt>g(2)</tt></li>
     * <li><tt>r(2)</tt></li>
     * <li><tt>act(r(2))</tt></li>
     * </ul>
     * </p>
     * 
     * @param k {@link Kam KAM}, the KAM that the nodes are contained within
     * @param node Example {@link KamNode KAM node}, the KAM node to match on
     * @return All {@link KamNode KAM nodes} that share node parameters with
     * <tt>node</tt>
     * @throws KamStoreException Thrown if an error occurred retrieving node
     * candidates
     */
    public List<KamNode> getKamNodes(Kam k, KamNode node)
            throws KamStoreException;

    /**
     * Returns all citations from associated with a {@link BelDocumentInfo BEL
     * document info} object.
     * 
     * @param kaminfo {@link KamInfo KAM info}
     * @param docinfo {@link BelDocumentInfo BEL document info}
     * @return {@link List} of {@link Citation}
     * @throws KamStoreException Thrown if an error occurred retrieving citation
     * candidates
     */
    public List<Citation>
            getCitations(KamInfo kaminfo, BelDocumentInfo docinfo)
                    throws KamStoreException;

    /**
     * Returns all citations from associated with a {@link BelDocumentInfo BEL
     * document info} object that are of the specified {@link CitationType
     * citations}.
     * 
     * @param kaminfo {@link KamInfo KAM info}
     * @param docinfo {@link BelDocumentInfo BEL document info}
     * @param citation {@link CitationType}
     * @return {@link List} of {@link Citation}
     * @throws KamStoreException Thrown if an error occurred retrieving citation
     * candidates
     */
    public List<Citation> getCitations(KamInfo kaminfo,
            BelDocumentInfo docinfo, CitationType citation)
            throws KamStoreException;

    /**
     * Returns all citations of the specified {@link CitationType citations} for
     * a KAM.
     * 
     * @param kaminfo {@link KamInfo KAM info}
     * @param citation {@link CitationType}
     * @return {@link List} of {@link Citation}
     * @throws KamStoreException Thrown if an error occurred retrieving citation
     * candidates
     */
    public List<Citation> getCitations(KamInfo info, CitationType citation)
            throws KamStoreException;

    /**
     * Returns all citation matching the specified {@link CitationType
     * citations} and reference IDs.
     * 
     * @param info {@link KamInfo KAM info}
     * @param citation {@link CitationType}
     * @param refIDs
     * @return {@link List} of {@link Citation}
     * @throws KamStoreException Thrown if an error occurred retrieving citation
     * candidates
     */
    public List<Citation> getCitations(KamInfo info, CitationType citation,
            String... refIDs) throws KamStoreException;
}
