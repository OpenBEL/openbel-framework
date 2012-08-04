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
package org.openbel.framework.api.internal;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.CitationType;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.core.df.external.ExternalResourceException;
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
import org.openbel.framework.api.internal.KAMStoreDaoImpl.KamProtoEdge;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.KamProtoNode;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.Namespace;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.TermParameter;

/**
 * KAMStoreDao provides a JDBC-driven DAO for accessing objects in the KamStore.
 *
 * @author Julian Ray {@code jray@selventa.com}
 */
public interface KAMStoreDao extends KAMDao {

    public static final class KamProtoNodesAndEdges {
        private final Map<Integer, KamProtoNode> nodes;
        private final Map<Integer, KamProtoEdge> edges;

        public KamProtoNodesAndEdges(Map<Integer, KamProtoNode> nodes,
                Map<Integer, KamProtoEdge> edges) {
            this.nodes = nodes;
            this.edges = edges;
        }

        public Map<Integer, KamProtoNode> getKamProtoNodes() {
            return nodes;
        }

        public Map<Integer, KamProtoEdge> getKamProtoEdges() {
            return edges;
        }
    }

    /**
     * TODO Document
     *
     * @param kamInfo
     * @return
     * @throws SQLException
     */
    public KamProtoNodesAndEdges getKamProtoNodesAndEdges(KamInfo kamInfo)
            throws SQLException;

    /**
     * TODO Document
     *
     * @param kamInfo
     * @param kamFilter
     * @return
     * @throws SQLException
     */
    public KamProtoNodesAndEdges getKamProtoNodesAndEdges(KamInfo kamInfo,
            KamFilter kamFilter) throws SQLException;

    /**
     * TODO Document
     *
     * @return
     * @throws SQLException
     */
    public List<AnnotationType> getAnnotationTypes() throws SQLException;

    /**
     * TODO Document
     * @param annotationType
     * @return
     * @throws SQLException
     */
    public List<String> getAnnotationTypeDomainValues(
            AnnotationType annotationType) throws SQLException,
            ExternalResourceException;

    /**
     * TODO Document
     *
     * @param kamEdge
     * @return
     * @throws SQLException
     */
    public List<BelStatement> getSupportingEvidence(KamEdge kamEdge)
            throws SQLException;

    /**
     * TODO Document
     *
     * @param kamEdgeId
     * @return
     * @throws SQLException
     */
    public List<BelStatement> getSupportingEvidence(Integer kamEdgeId)
            throws SQLException;

    /**
     * Retrieve a filtered {@link List list} of
     * {@link BelStatement supporting evidence} for a {@link KamEdge kam edge}.
     *
     * @param kamEdge the {@link KamEdge kam edge} to retrieve
     * {@link BelStatement supporting evidence} for, which cannot be null
     * @param filter the {@link AnnotationFilter filter} to restrict the
     * returned {@link BelStatement supporting evidence}, which may be null
     * @return the {@link BelStatement supporting evidence} list
     * @throws InvalidArgument Thrown if <tt>kamEdge</tt> is <tt>null</tt>
     * @throws SQLException Thrown if a SQL error occurred while retrieving the
     * {@link KamEdge edge}'s {@link BelStatement supporting evidence}
     */
    public List<BelStatement> getSupportingEvidence(KamEdge kamEdge,
            AnnotationFilter filter) throws SQLException;

    /**
     * TODO Document
     *
     * @param kamNode
     * @return
     * @throws SQLException
     */
    public List<BelTerm> getSupportingTerms(KamNode kamNode)
            throws SQLException;

    public Integer getKamNodeId(String belTermString) throws SQLException;

    public Integer getKamNodeId(BelTerm belTerm) throws SQLException;

    /**
     * TODO Document
     *
     * @param kamNode
     * @param namespaceFilter
     * @return
     * @throws SQLException
     */
    public List<BelTerm> getSupportingTerms(KamNode kamNode,
            NamespaceFilter namespaceFilter) throws SQLException;

    /**
     * Retrieves the term parameters for a BEL Term in the KAM.
     *
     * @param belTermId {@link Integer} the BEL Term id
     * @return {@link List} of {@link TermParameter} for this BEL Term
     * @throws SQLException Thrown if a SQL error occurred retrieving the term
     * parameters
     */
    public List<TermParameter> getTermParameters(BelTerm belTerm)
            throws SQLException;

    /**
     * Retrieve {@link KamNode kam node} ids with the given {@link FunctionEnum
     * function} <em>that were compiled with</em> global parameters that are
     * represented by a {@link Namespace namespace} and {@link String
     * parameterValue} pair.
     * 
     * @param functionType {@link FunctionEnum}, the node function
     * @param namespace {@link Namespace}, the namespace, which may be null,
     *            indicating a parameter with an undefined namespace
     * @param parameterValue {@link String}, the parameter value
     * @return the {@link KamNode kam node} ids that contain this
     *         {@link Namespace namespace} and {@link String parameterValue}
     *         pair.
     * @throws SQLException Thrown if a SQL error occurred finding matching
     *             {@link KamNode kam nodes}
     */
    public List<Integer> getKamNodeCandidates(FunctionEnum functionType,
            Namespace namespace, String parameterValue) throws SQLException;

    /**
     * Retrieve {@link KamNode kam node} ids <em>that were compiled with</em>
     * global parameters that are represented by a {@link Namespace namespace}
     * and {@link String parameterValue} pair.
     * 
     * @param namespace {@link Namespace}, the namespace, which may be null,
     *            indicating a parameter with an undefined namespace
     * @param parameterValue {@link String}, the parameter value
     * @return the {@link KamNode kam node} ids that contain this
     *         {@link Namespace namespace} and {@link String parameterValue}
     *         pair.
     * @throws SQLException Thrown if a SQL error occurred finding matching
     *             {@link KamNode kam nodes}
     */
    public List<Integer> getKamNodeCandidates(Namespace namespace,
            String parameterValue) throws SQLException;

    /**
     * Retrieve {@link KamNode kam node} ids that contain global parameters that
     * are represented by the given {@link SkinnyUUID uuid}.
     * 
     * @param uuid {@link SkinnyUUID uuid} identifying the namespace value which
     *            must not be null.
     * @return the {@link KamNode kam node} ids that contain this
     *         {@link SkinnyUUID uuid}.
     * @throws SQLException Thrown if a SQL error occurred finding matching
     *             {@link KamNode kam nodes}
     */
    public List<Integer> getKamNodeCandidates(SkinnyUUID uuid)
            throws SQLException;

    /**
     * Retrieve {@link KamNode kam node} ids with the given {@link FunctionEnum
     * function} that contain global parameters that are represented by the
     * given {@link SkinnyUUID uuid}.
     * 
     * @param functionType {@link FunctionEnum}, the node function
     * @param uuid {@link SkinnyUUID uuid} identifying the namespace value which
     *            must not be null.
     * @return the {@link KamNode kam node} ids that contain this
     *         {@link SkinnyUUID uuid}.
     * @throws SQLException Thrown if a SQL error occurred finding matching
     *             {@link KamNode kam nodes}
     */
    public List<Integer> getKamNodeCandidates(FunctionEnum functionType,
            SkinnyUUID uuid) throws SQLException;

    /**
     * Retrieves candidate {@link KamNode kam node} ids that contain global
     * parameters found in the <tt>example</tt> {@link KamNode kam node}.
     *
     * <p>
     * For instance, given a kam with the following nodes:<ul>
     * <li><tt>complex(p(1), g(2))</tt></li>
     * <li><tt>p(1)</tt></li>
     * <li><tt>g(2)</tt></li>
     * <li><tt>r(2)</tt></li>
     * <li><tt>act(r(2))</tt></li></ul>
     *
     * Providing the <tt>complex(p(1), g(2))</tt> {@link KamNode kam node} will
     * yield ids for the following similar {@link KamNode kam nodes}:<ul>
     * <li><tt>p(1)</tt></li>
     * <li><tt>g(2)</tt></li>
     * <li><tt>r(2)</tt></li>
     * <li><tt>act(r(2))</tt></li></ul>
     * </p>
     *
     * @param example {@link KamNode}, the kam node to retrieve similar matches
     * on, which cannot be null or have a null id
     * @return the ids of {@link KamNode kam nodes} containing similar global
     * parameters
     * @throws SQLException Thrown if a SQL error occurred retrieving similar
     * node matches
     */
    public List<Integer> getKamNodeCandidates(KamNode example)
            throws SQLException;

    /**
    * TODO Document
    *
    * @return
    * @throws SQLException
    */
    public List<BelDocumentInfo> getBelDocumentInfos() throws SQLException;

    /**
     * TODO Document
     *
     * @return
     * @throws SQLException
     */
    public List<Namespace> getNamespaces() throws SQLException;

    /**
     * TODO Document
     *
     * @param belStatementId
     * @return
     * @throws SQLException
     */
    public BelStatement getBelStatement(Integer belStatementId)
            throws SQLException;

    /**
     * Returns all citations from associated with a {@link BelDocumentInfo} object.
     * @param belDocumentInfo
     * @return
     * @throws SQLException
     */
    public List<Citation> getCitations(BelDocumentInfo belDocumentInfo)
            throws SQLException;

    /**
     * Returns all citations from associated with a {@link BelDocumentInfo} object that are of the specified {@link CitationType}
     * @param belDocumentInfo
     * @param citationType
     * @return
     * @throws SQLException
     */
    public List<Citation> getCitations(BelDocumentInfo belDocumentInfo,
            CitationType citationType) throws SQLException;

    /**
     * Returns all citations of the specified {@link CitationType} for a Kam.
     * @param citationType
     * @return
     * @throws SQLException
     */
    public List<Citation> getCitations(CitationType citationType)
            throws SQLException;

    /**
     * Returns all citation matching the specified {@link CitationType} and reference IDs.
     * @param citationType
     * @param refereceIds
     * @return
     * @throws SQLException
     */
    public List<Citation> getCitations(CitationType citationType,
            String... referenceIds) throws SQLException;
}
