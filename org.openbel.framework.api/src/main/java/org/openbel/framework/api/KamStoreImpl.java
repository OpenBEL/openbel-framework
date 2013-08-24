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

import static java.lang.String.format;
import static org.openbel.framework.common.BELUtilities.hasLength;
import static org.openbel.framework.common.BELUtilities.noLength;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.CitationType;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.core.df.external.ExternalResourceException;
import org.openbel.framework.internal.KAMCatalogDao;
import org.openbel.framework.internal.KAMStoreDao;
import org.openbel.framework.internal.KAMStoreDaoImpl;
import org.openbel.framework.internal.KAMCatalogDao.AnnotationFilter;
import org.openbel.framework.internal.KAMCatalogDao.KamFilter;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.internal.KAMCatalogDao.NamespaceFilter;
import org.openbel.framework.internal.KAMStoreDao.KamProtoNodesAndEdges;
import org.openbel.framework.internal.KAMStoreDaoImpl.AnnotationType;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelDocumentInfo;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelStatement;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelTerm;
import org.openbel.framework.internal.KAMStoreDaoImpl.Citation;
import org.openbel.framework.internal.KAMStoreDaoImpl.KamProtoEdge;
import org.openbel.framework.internal.KAMStoreDaoImpl.KamProtoNode;
import org.openbel.framework.internal.KAMStoreDaoImpl.Namespace;
import org.openbel.framework.internal.KAMStoreDaoImpl.TermParameter;

/**
 * Provides access to a KamStore and the KAMs.
 *
 * @author julianjray
 */
public final class KamStoreImpl implements KamStore {
    private final DBConnection dbConnection;

    /*
     * Don't use directly as it might not be configured. Always access via
     * getKamCatalogDao() method.
     */
    private KAMCatalogDao _kamCatalogDao;

    /*
     * Map of DAO's, one for each KAM. This map is populated in a lazy fashion,
     * as each KAM is requested.
     */
    private Map<KamInfo, KAMStoreDao> kamStoreDaoMap =
            new HashMap<KamInfo, KAMStoreDao>();

    /**
     * Defines the KAM catalog schema name pulled from the system configuration.
     * For further reference, see SystemConfiguration getKamCatalogSchema().
     */
    private final String kamCatalogSchemaName;

    /**
     * Defines the KAM schema prefix pulled from the system configuration. For
     * further reference, see SystemConfiguration getKamSchemaPrefix().
     */
    private final String kamSchemaPrefix;

    /**
     * Creates a KAM store associated to the provided database connection.
     *
     * @param dbConnection Database connection
     */
    public KamStoreImpl(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
        this.kamCatalogSchemaName =
                getSystemConfiguration().getKamCatalogSchema();
        this.kamSchemaPrefix = getSystemConfiguration().getKamSchemaPrefix();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close(Kam kam) {
        if (null == kam) {
            throw new InvalidArgument("kam", kam);
        }
        KAMStoreDao kamStoreDao = kamStoreDaoMap.get(kam.getKamInfo());
        if (null != kamStoreDao) {
            kamStoreDao.terminate();
            kamStoreDaoMap.remove(kam.getKamInfo());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void teardown() {
        if (null != _kamCatalogDao) {
            _kamCatalogDao.terminate();
        }

        Set<Entry<KamInfo, KAMStoreDao>> entries = kamStoreDaoMap.entrySet();
        Iterator<Entry<KamInfo, KAMStoreDao>> iter = entries.iterator();
        while (iter.hasNext()) {
            Entry<KamInfo, KAMStoreDao> next = iter.next();
            next.getValue().terminate();
            iter.remove();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KamInfo> readCatalog() throws KamStoreException {
        try {
            return getKamCatalogDao().getCatalog();
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamInfo getKamInfo(String kamName) throws KamStoreException {
        if (kamName == null) {
            throw new InvalidArgument("kamName", kamName);
        }

        try {
            return getKamCatalogDao().getKamInfoByName(kamName);
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Kam getKam(KamInfo kamInfo, KamFilter kamFilter)
            throws InvalidArgument, KamStoreException {
        if (null == kamInfo) {
            throw new InvalidArgument("kamInfo", kamInfo);
        }

        try {
            return getKam(getKamStoreDao(kamInfo), kamInfo, kamFilter);
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
    }

    /**
     * Internal method to load a {@link Kam} with a given {@link KAMStoreDao}.<br>
     * Allows bypassing of the {@link KAMStoreDao} cache
     * @param kamStoreDao
     * @param kamInfo
     * @param kamFilter
     * @return
     * @throws KamStoreException
     */
    private Kam getKam(KAMStoreDao kamStoreDao, KamInfo kamInfo,
            KamFilter kamFilter) throws KamStoreException {
        Map<Integer, KamProtoNode> nodes;
        Map<Integer, KamProtoEdge> edges;
        try {

            KamProtoNodesAndEdges all;
            if (kamFilter == null || kamFilter.getFilterCriteria().isEmpty()) {
                all = kamStoreDao.getKamProtoNodesAndEdges(kamInfo);
            } else {
                all = kamStoreDao.getKamProtoNodesAndEdges(kamInfo, kamFilter);
            }
            edges = all.getKamProtoEdges();
            nodes = all.getKamProtoNodes();
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
        return new KamImpl(kamInfo, nodes.values(), edges.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Kam getKam(String kamName) throws InvalidArgument, KamStoreException {
        // Retrieve the KamInfo by the specified name
        final KamInfo kamInfo = getKamInfo(kamName);

        // Load KAM if the entry exists in the KAM catalog.
        if (kamInfo != null) {
            return getKam(kamInfo);
        }

        final String err = format("No KAM found with name '%s'", kamName);
        throw new KamStoreException(err);
    }

    /**
     * Loads and returns a KAM from the KAM Store.
     *
     * @param kamInfo KAM info
     * @return Kam
     * @throws InvalidArgument Thrown if {@code kamInfo} is null
     * @throws KamStoreException Thrown if a KAM is not found for the provided
     * {@link KamInfo}
     */
    @Override
    public Kam getKam(KamInfo kamInfo) throws InvalidArgument,
            KamStoreException {
        return getKam(kamInfo, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AnnotationType> getAnnotationTypes(Kam kam)
            throws InvalidArgument, KamStoreException {
        if (null == kam) {
            throw new InvalidArgument("kam", kam);
        }
        return getAnnotationTypes(kam.getKamInfo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AnnotationType> getAnnotationTypes(KamInfo kamInfo)
            throws InvalidArgument, KamStoreException {

        if (null == kamInfo) {
            throw new InvalidArgument("kamInfo", kamInfo);
        }

        // Lazily return the list of annotation types store in the Kam
        try {
            return getKamStoreDao(kamInfo).getAnnotationTypes();
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAnnotationTypeDomainValues(
            KamInfo kamInfo, AnnotationType annotationType)
            throws KamStoreException {
        if (null == kamInfo) {
            throw new InvalidArgument("kamInfo", kamInfo);
        }

        // Lazily return the list of annotation types store in the Kam
        try {
            return getKamStoreDao(kamInfo).getAnnotationTypeDomainValues(
                    annotationType);
        } catch (SQLException e) {
            throw new KamStoreException(e);
        } catch (ExternalResourceException e) {
            throw new KamStoreException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BelDocumentInfo> getBelDocumentInfos(Kam kam)
            throws InvalidArgument, KamStoreException {
        if (null == kam) {
            throw new InvalidArgument("kam", kam);
        }
        return getBelDocumentInfos(kam.getKamInfo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BelDocumentInfo> getBelDocumentInfos(KamInfo kamInfo)
            throws InvalidArgument, KamStoreException {
        if (null == kamInfo) {
            throw new InvalidArgument("kamInfo", kamInfo);
        }
        try {
            return getKamStoreDao(kamInfo).getBelDocumentInfos();
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Namespace getNamespace(final Kam kam,
            final String resourceLocation)
            throws InvalidArgument,
            KamStoreException {
        if (null == kam) {
            throw new InvalidArgument("kam", kam);
        }

        if (noLength(resourceLocation)) {
            throw new InvalidArgument("resourceLocation", resourceLocation);
        }

        List<Namespace> namespaces = getNamespaces(kam.getKamInfo());
        for (final Namespace namespace : namespaces) {
            final String nrl = namespace.getResourceLocation();
            if (hasLength(nrl) && nrl.equals(resourceLocation)) {
                return namespace;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Namespace> getNamespaces(Kam kam) throws InvalidArgument,
            KamStoreException {
        if (null == kam) {
            throw new InvalidArgument("kam", kam);
        }
        return getNamespaces(kam.getKamInfo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Namespace> getNamespaces(KamInfo kamInfo)
            throws InvalidArgument, KamStoreException {

        if (null == kamInfo) {
            throw new InvalidArgument("kamInfo", kamInfo);
        }
        try {
            return getKamStoreDao(kamInfo).getNamespaces();
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BelStatement> getSupportingEvidence(KamEdge kamEdge,
            AnnotationFilter annotationFilter) throws InvalidArgument,
            KamStoreException {
        if (null == kamEdge) {
            throw new InvalidArgument("kamEdge", kamEdge);
        }
        try {
            return getKamStoreDao(kamEdge.getKam().getKamInfo())
                    .getSupportingEvidence(kamEdge, annotationFilter);
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BelStatement> getSupportingEvidence(KamEdge kamEdge)
            throws InvalidArgument, KamStoreException {
        return getSupportingEvidence(kamEdge, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BelTerm> getSupportingTerms(KamNode kamNode)
            throws InvalidArgument, KamStoreException {
        return getSupportingTerms(kamNode, false, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BelTerm> getSupportingTerms(KamNode kamNode,
            boolean removeDuplicates) throws InvalidArgument, KamStoreException {
        return getSupportingTerms(kamNode, removeDuplicates, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BelTerm> getSupportingTerms(KamNode kamNode,
            boolean removeDuplicates, NamespaceFilter namespaceFilter)
            throws InvalidArgument, KamStoreException {
        if (null == kamNode) {
            throw new InvalidArgument("kamNode", kamNode);
        }
        try {
            List<BelTerm> list =
                    getKamStoreDao(kamNode.getKam().getKamInfo())
                            .getSupportingTerms(kamNode, namespaceFilter);

            // Check for duplicates
            if (removeDuplicates == true) {
                HashMap<String, BelTerm> map = new HashMap<String, BelTerm>();
                for (BelTerm belTerm : list) {
                    if (!map.containsKey(belTerm.getLabel())) {
                        map.put(belTerm.getLabel(), belTerm);
                    }
                }
                list.clear();
                list.addAll(map.values());
            }
            return list;
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TermParameter> getTermParameters(final KamInfo kamInfo,
            final BelTerm belTerm) throws KamStoreException {
        if (null == kamInfo) {
            throw new InvalidArgument("kamInfo", kamInfo);
        }

        if (null == belTerm) {
            throw new InvalidArgument("belTerm", belTerm);
        }
        try {
            return getKamStoreDao(kamInfo).getTermParameters(belTerm);
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode getKamNode(final Kam kam, String belTermString)
            throws KamStoreException {
        if (StringUtils.isEmpty(belTermString)) {
            throw new InvalidArgument("belTermString", belTermString);
        }
        KamNode kamNode = null;
        try {
            Integer kamNodeId =
                    getKamStoreDao(kam.getKamInfo())
                            .getKamNodeId(belTermString);
            kamNode = kam.findNode(kamNodeId);
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
        return kamNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode getKamNode(final Kam kam, BelTerm belTerm)
            throws KamStoreException {
        return getKamNode(kam, belTerm.getLabel());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KamNode> getKamNodes(final Kam kam, FunctionEnum functionType,
            Namespace namespace, String parameterValue)
            throws KamStoreException {
        if (parameterValue == null) {
            throw new InvalidArgument("parameterValue", parameterValue);
        }
        try {
            List<Integer> ids =
                    getKamStoreDao(kam.getKamInfo()).getKamNodeCandidates(
                            functionType, namespace, parameterValue);
            List<KamNode> kamNodeList = new ArrayList<KamNode>(ids.size());
            for (Integer kamNodeId : ids) {
                kamNodeList.add(kam.findNode(kamNodeId));
            }
            return kamNodeList;
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KamNode> getKamNodes(final Kam kam, Namespace namespace,
            String parameterValue) throws KamStoreException {
        if (parameterValue == null) {
            throw new InvalidArgument("parameterValue", parameterValue);
        }
        try {
            List<Integer> ids =
                    getKamStoreDao(kam.getKamInfo()).getKamNodeCandidates(
                            namespace, parameterValue);
            List<KamNode> kamNodeList = new ArrayList<KamNode>(ids.size());
            for (Integer kamNodeId : ids) {
                kamNodeList.add(kam.findNode(kamNodeId));
            }
            return kamNodeList;
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KamNode> getKamNodes(final Kam k, final SkinnyUUID uuid)
            throws KamStoreException {
        if (uuid == null) {
            throw new InvalidArgument("uuid", uuid);
        }
        try {
            List<Integer> ids =
                    getKamStoreDao(k.getKamInfo()).getKamNodeCandidates(uuid);
            List<KamNode> kamNodeList = new ArrayList<KamNode>();
            for (Integer kamNodeId : ids) {
                kamNodeList.add(k.findNode(kamNodeId));
            }
            return kamNodeList;
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KamNode> getKamNodes(final Kam k, final FunctionEnum function,
            final SkinnyUUID uuid) throws KamStoreException {
        if (uuid == null) {
            throw new InvalidArgument("uuid", uuid);
        }
        try {
            List<Integer> ids =
                    getKamStoreDao(k.getKamInfo()).getKamNodeCandidates(
                            function, uuid);
            List<KamNode> kamNodeList = new ArrayList<KamNode>();
            for (Integer kamNodeId : ids) {
                kamNodeList.add(k.findNode(kamNodeId));
            }
            return kamNodeList;
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KamNode> getKamNodes(final Kam kam, final KamNode example)
            throws KamStoreException {
        if (kam == null) {
            throw new InvalidArgument("kam", kam);
        }

        if (example == null) {
            throw new InvalidArgument("example", example);
        }

        List<KamNode> kamNodeList = new ArrayList<KamNode>();
        try {
            final KAMStoreDao ksdao = getKamStoreDao(kam.getKamInfo());
            List<Integer> kamNodeIdMatches =
                    ksdao.getKamNodeCandidates(example);
            for (Integer kamNodeId : kamNodeIdMatches) {
                kamNodeList.add(kam.findNode(kamNodeId));
            }
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
        return kamNodeList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Citation> getCitations(KamInfo kamInfo,
            BelDocumentInfo belDocumentInfo) throws InvalidArgument,
            KamStoreException {
        if (null == kamInfo) {
            throw new InvalidArgument("kamInfo", kamInfo);
        }

        try {
            return getKamStoreDao(kamInfo).getCitations(belDocumentInfo);
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Citation> getCitations(KamInfo kamInfo,
            BelDocumentInfo belDocumentInfo, CitationType citationType)
            throws InvalidArgument, KamStoreException {
        if (null == kamInfo) {
            throw new InvalidArgument("kamInfo", kamInfo);
        }

        try {
            return getKamStoreDao(kamInfo).getCitations(belDocumentInfo,
                    citationType);
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Citation> getCitations(KamInfo kamInfo,
            CitationType citationType) throws InvalidArgument,
            KamStoreException {
        if (null == kamInfo) {
            throw new InvalidArgument("kamInfo", kamInfo);
        }

        try {
            return getKamStoreDao(kamInfo).getCitations(citationType);
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Citation> getCitations(KamInfo kamInfo,
            CitationType citationType, String... referenceIds)
            throws InvalidArgument, KamStoreException {
        if (null == kamInfo) {
            throw new InvalidArgument("kamInfo", kamInfo);
        }

        try {
            return getKamStoreDao(kamInfo).getCitations(citationType,
                    referenceIds);
        } catch (SQLException e) {
            throw new KamStoreException(e);
        }
    }

    private KAMStoreDao getKamStoreDao(KamInfo kamInfo) throws SQLException {
        KAMStoreDao dao = kamStoreDaoMap.get(kamInfo);
        if (null == dao) {
            dao = new KAMStoreDaoImpl(kamInfo.getKamDbObject()
                    .getSchemaName(), dbConnection);
            kamStoreDaoMap.put(kamInfo, dao);
        }
        return dao;
    }

    private KAMCatalogDao getKamCatalogDao() throws SQLException {
        if (null == _kamCatalogDao) {
            _kamCatalogDao = new KAMCatalogDao(dbConnection,
                    kamCatalogSchemaName, kamSchemaPrefix);
        }
        return _kamCatalogDao;
    }
}
