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

import static java.lang.String.*;
import static java.util.Collections.*;
import static org.openbel.framework.common.BELUtilities.*;
import static org.openbel.framework.common.InvalidArgument.*;
import static org.openbel.framework.common.cfg.SystemConfiguration.*;

import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.api.internal.*;
import org.openbel.framework.api.internal.KAMCatalogDao.AnnotationFilter;
import org.openbel.framework.api.internal.KAMCatalogDao.KamFilter;
import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.api.internal.KAMCatalogDao.NamespaceFilter;
import org.openbel.framework.api.internal.KAMStoreDao.KamProtoNodesAndEdges;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.AnnotationType;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.BelDocumentInfo;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.BelStatement;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.BelTerm;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.Citation;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.KamProtoEdge;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.KamProtoNode;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.Namespace;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.TermParameter;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.SearchFunction;
import org.openbel.framework.common.enums.CitationType;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.core.df.external.ExternalResourceException;

/**
 * Provides access to a KamStore and the KAMs.
 *
 * @author julianjray
 */
public final class KAMStoreImpl implements KAMStore {
    private final DBConnection dbc;
    private final String catalog;
    private final String prefix;

    /* XXX: Don't use directly, always access via getKamCatalogDao() */
    private KAMCatalogDao _dao;

    /* Map populated as KAMs are requested. */
    private Map<KamInfo, KAMStoreDao> daomap;
    private Map<KamInfo, KAMUpdateDao> updatemap;
    private Map<KamInfo, EvidenceDao> evdaomap;

    /**
     * Creates a KAM store associated to the provided database connection.
     *
     * @param dbConnection Database connection
     */
    public KAMStoreImpl(DBConnection dbConnection) {
        this.dbc = dbConnection;
        this.catalog = getSystemConfiguration().getKamCatalogSchema();
        this.prefix = getSystemConfiguration().getKamSchemaPrefix();
        daomap = new HashMap<KamInfo, KAMStoreDao>();
        evdaomap = new HashMap<KamInfo, EvidenceDao>();
        updatemap = new HashMap<KAMCatalogDao.KamInfo, KAMUpdateDao>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(final Kam k) {
        if (k == null) throw new InvalidArgument(DEFAULT_MSG);
        return exists(k.getKamInfo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(final KamInfo info) {
        if (info == null) throw new InvalidArgument(DEFAULT_MSG);
        return exists(info.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(final String name) {
        if (name == null) throw new InvalidArgument(DEFAULT_MSG);
        KamInfo ki = search(getCatalog(), new SearchFunction<KamInfo>() {

            @Override
            public boolean match(KamInfo t) {
                return name.equals(t.getName());
            }
        });
        return ki != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close(Kam kam) {
        if (kam == null) throw new InvalidArgument(DEFAULT_MSG);
        final KamInfo ki = kam.getKamInfo();
        KAMStoreDao dao = daomap.get(ki);
        if (dao != null) {
            dao.terminate();
            daomap.remove(ki);
        }

        EvidenceDao evdao = evdaomap.get(ki);
        if (evdao != null) {
            evdao.terminate();
            evdaomap.remove(ki);
        }

        KAMUpdateDao update = updatemap.get(ki);
        if (update != null) {
            update.terminate();
            updatemap.remove(ki);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void teardown() {
        if (_dao != null) _dao.terminate();

        Set<Entry<KamInfo, KAMStoreDao>> entries = entries(daomap);
        Iterator<Entry<KamInfo, KAMStoreDao>> iter = entries.iterator();
        while (iter.hasNext()) {
            Entry<KamInfo, KAMStoreDao> next = iter.next();
            next.getValue().terminate();
            iter.remove();
        }

        Set<Entry<KamInfo, EvidenceDao>> evEntries = entries(evdaomap);
        Iterator<Entry<KamInfo, EvidenceDao>> evIter = evEntries.iterator();
        while (evIter.hasNext()) {
            Entry<KamInfo, EvidenceDao> next = evIter.next();
            next.getValue().terminate();
            evIter.remove();
        }

        Set<Entry<KamInfo, KAMUpdateDao>> e = entries(updatemap);
        Iterator<Entry<KamInfo, KAMUpdateDao>> ie = e.iterator();
        while (ie.hasNext()) {
            Entry<KamInfo, KAMUpdateDao> next = ie.next();
            next.getValue().terminate();
            ie.remove();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KamInfo> getCatalog() {
        try {
            return kamCatalogDao().getCatalog();
        } catch (SQLException e) {
            return emptyList();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamInfo getKamInfo(String kamName) {
        if (kamName == null) throw new InvalidArgument(DEFAULT_MSG);
        if (!exists(kamName)) return null;
        try {
            return kamCatalogDao().getKamInfoByName(kamName);
        } catch (SQLException e) {
            final String msg = "error getting KAM info by name";
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Kam getKam(KamInfo kamInfo, KamFilter kamFilter) {
        if (kamInfo == null) throw new InvalidArgument(DEFAULT_MSG);
        if (!exists(kamInfo)) return null;
        try {
            return getKam(kamStoreDao(kamInfo), kamInfo, kamFilter);
        } catch (SQLException e) {
            final String msg = "error getting KAM";
            throw new KAMStoreException(msg, e);
        }
    }

    private Kam getKam(KAMStoreDao kamStoreDao, KamInfo kamInfo,
            KamFilter kamFilter) {
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
            final String msg = "error loading nodes and edges";
            throw new KAMStoreException(msg, e);
        }
        return new KamImpl(kamInfo, nodes.values(), edges.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Kam getKam(String kamName) {
        if (kamName == null) throw new InvalidArgument(DEFAULT_MSG);
        if (!exists(kamName)) return null;
        final KamInfo ki = getKamInfo(kamName);
        return getKam(ki);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Kam getKam(KamInfo kamInfo) throws InvalidArgument,
            KAMStoreException {
        return getKam(kamInfo, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AnnotationType> getAnnotationTypes(Kam kam) {
        if (kam == null) throw new InvalidArgument(DEFAULT_MSG);
        if (!exists(kam)) return null;
        return getAnnotationTypes(kam.getKamInfo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AnnotationType> getAnnotationTypes(KamInfo ki) {
        if (ki == null) throw new InvalidArgument(DEFAULT_MSG);
        if (!exists(ki)) return null;
        try {
            return kamStoreDao(ki).getAnnotationTypes();
        } catch (SQLException e) {
            final String fmt = "error getting annotation types for %s";
            final String msg = format(fmt, ki.getName());
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAnnotationTypeDomainValues(KamInfo ki,
            AnnotationType annotationType) {
        if (ki == null) throw new InvalidArgument(DEFAULT_MSG);
        if (!exists(ki)) return null;
        try {
            KAMStoreDao dao = kamStoreDao(ki);
            return dao.getAnnotationTypeDomainValues(annotationType);
        } catch (SQLException e) {
            final String fmt = "error getting domain values for %s";
            final String msg = format(fmt, ki.getName());
            throw new KAMStoreException(msg, e);
        } catch (ExternalResourceException e) {
            final String msg = "resource exception";
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BelDocumentInfo> getBelDocumentInfos(Kam kam) {
        if (kam == null) throw new InvalidArgument(DEFAULT_MSG);
        if (!exists(kam)) return null;
        return getBelDocumentInfos(kam.getKamInfo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BelDocumentInfo> getBelDocumentInfos(KamInfo ki) {
        if (ki == null) throw new InvalidArgument(DEFAULT_MSG);
        if (!exists(ki)) return null;
        try {
            return kamStoreDao(ki).getBelDocumentInfos();
        } catch (SQLException e) {
            final String fmt = "error getting documents for %s";
            final String msg = format(fmt, ki.getName());
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Namespace getNamespace(final Kam kam, final String resloc) {
        if (kam == null) throw new InvalidArgument("kam", kam);
        if (noLength(resloc))
            throw new InvalidArgument("missing resource location");
        if (!exists(kam)) return null;
        List<Namespace> namespaces = getNamespaces(kam);
        for (final Namespace namespace : namespaces) {
            final String nrl = namespace.getResourceLocation();
            if (hasLength(nrl) && nrl.equals(resloc)) {
                return namespace;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Namespace> getNamespaces(Kam kam) {
        if (kam == null) throw new InvalidArgument("kam", kam);
        if (!exists(kam)) return null;
        return getNamespaces(kam.getKamInfo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Namespace> getNamespaces(KamInfo ki) {
        if (ki == null) throw new InvalidArgument("missing KAM info");
        if (!exists(ki)) return null;
        try {
            return kamStoreDao(ki).getNamespaces();
        } catch (SQLException e) {
            final String fmt = "error getting namespaces for %s";
            final String msg = format(fmt, ki.getName());
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BelStatement> getSupportingEvidence(KamEdge kamEdge,
            AnnotationFilter annotationFilter) {
        if (kamEdge == null) throw new InvalidArgument("kamEdge", kamEdge);
        Kam kam = kamEdge.getKam();
        if (!exists(kam)) return null;
        try {
            List<BelStatement> results = kamStoreDao(kam.getKamInfo())
                    .getSupportingEvidence(kamEdge, annotationFilter);
            if (results.isEmpty()) return emptyList();
            return results;
        } catch (SQLException e) {
            final String fmt = "error getting evidence for %s";
            final String msg = format(fmt, kam.getKamInfo().getName());
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BelStatement> getSupportingEvidence(KamEdge kamEdge) {
        if (kamEdge == null) throw new InvalidArgument("kamEdge", kamEdge);
        if (!exists(kamEdge.getKam())) return null;
        return getSupportingEvidence(kamEdge, null);
    }

    @Override
    public Map<KamEdge, List<Statement>> getSupportingEvidence(Collection<KamEdge> edges) {
        if (edges == null) throw new InvalidArgument("edges", edges);
        if (edges.isEmpty()) return emptyMap();

        KamEdge first = edges.iterator().next();
        Kam kam = first.getKam();
        if (!exists(kam)) return null;
        try {
            Map<KamEdge, List<Statement>> results = evidenceDao(kam.getKamInfo()).evidence(edges);
            if (results.isEmpty()) return emptyMap();
            return results;
        } catch (SQLException e) {
            final String fmt = "error getting evidence for %s";
            final String msg = format(fmt, kam.getKamInfo().getName());
            throw new KAMStoreException(msg, e);
        }
    }

    @Override
    public Map<KamEdge, List<Statement>> getSupportingEvidence(Collection<KamEdge> edges, AnnotationFilter filter) {
        if (edges == null) throw new InvalidArgument("edges", edges);
        if (edges.isEmpty()) return emptyMap();

        KamEdge first = edges.iterator().next();
        Kam kam = first.getKam();
        if (!exists(kam)) return null;
        try {
            Map<KamEdge, List<Statement>> results = evidenceDao(kam.getKamInfo()).evidence(edges, filter);
            if (results.isEmpty()) return emptyMap();
            return results;
        } catch (SQLException e) {
            final String fmt = "error getting evidence for %s";
            final String msg = format(fmt, kam.getKamInfo().getName());
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BelTerm> getSupportingTerms(KamNode kamNode) {
        if (kamNode == null) throw new InvalidArgument("kamNode", kamNode);
        if (!exists(kamNode.getKam())) return null;
        return getSupportingTerms(kamNode, false, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BelTerm> getSupportingTerms(KamNode kamNode, boolean rmvdups) {
        if (kamNode == null) throw new InvalidArgument("kamNode", kamNode);
        if (!exists(kamNode.getKam())) return null;
        return getSupportingTerms(kamNode, rmvdups, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BelTerm> getSupportingTerms(KamNode kamNode,
            boolean removeDuplicates, NamespaceFilter fltr) {
        if (kamNode == null) throw new InvalidArgument("kamNode", kamNode);
        KamInfo ki = kamNode.getKam().getKamInfo();
        if (!exists(ki)) return null;
        List<BelTerm> terms;
        try {
            terms = kamStoreDao(ki).getSupportingTerms(kamNode, fltr);
        } catch (SQLException e) {
            final String fmt = "error getting supporting terms for %s";
            final String msg = format(fmt, ki.getName());
            throw new KAMStoreException(msg, e);
        }

        // Check for duplicates
        if (removeDuplicates) {
            HashMap<String, BelTerm> map = new HashMap<String, BelTerm>();
            // FIXME: this is inefficient
            for (BelTerm belTerm : terms) {
                if (!map.containsKey(belTerm.getLabel())) {
                    map.put(belTerm.getLabel(), belTerm);
                }
            }
            terms.clear();
            terms.addAll(map.values());
        }
        return terms;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TermParameter> getTermParameters(final KamInfo ki,
            final BelTerm belTerm) {
        if (ki == null) throw new InvalidArgument("missing KAM info");
        if (belTerm == null) throw new InvalidArgument("missing BEL term");
        if (!exists(ki)) return null;
        try {
            return kamStoreDao(ki).getTermParameters(belTerm);
        } catch (SQLException e) {
            final String fmt = "error getting term parameters for %s";
            final String msg = format(fmt, ki.getName());
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode getKamNode(final Kam kam, String belTermString) {
        if (noLength(belTermString))
            throw new InvalidArgument("belTermString", belTermString);
        KamInfo ki = kam.getKamInfo();
        if (!exists(ki)) return null;
        KamNode kamNode;
        Integer nodeID;
        try {
            nodeID = kamStoreDao(ki).getKamNodeId(belTermString);
            kamNode = kam.findNode(nodeID);
        } catch (SQLException e) {
            final String fmt = "error getting KAM node ID for %s";
            final String msg = format(fmt, ki.getName());
            throw new KAMStoreException(msg, e);
        }
        return kamNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode getKamNode(final Kam kam, BelTerm belTerm) {
        if (kam == null) throw new InvalidArgument("missing KAM");
        if (belTerm == null) throw new InvalidArgument("missing BEL term");
        if (!exists(kam)) return null;
        return getKamNode(kam, belTerm.getLabel());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KamNode> getKamNodes(final Kam kam, FunctionEnum functionType,
            Namespace namespace, String parameterValue) {
        if (parameterValue == null) {
            throw new InvalidArgument("parameterValue", parameterValue);
        }
        if (!exists(kam)) return null;
        List<Integer> ids;
        try {
            ids = kamStoreDao(kam.getKamInfo()).getKamNodeCandidates(
                    functionType, namespace, parameterValue);
            List<KamNode> kamNodeList = new ArrayList<KamNode>(ids.size());
            for (Integer kamNodeId : ids) {
                kamNodeList.add(kam.findNode(kamNodeId));
            }
            return kamNodeList;
        } catch (SQLException e) {
            final String fmt = "error getting KAM nodes for %s";
            final String msg = format(fmt, kam.getKamInfo().getName());
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KamNode> getKamNodes(final Kam kam, Namespace namespace,
            String parameterValue) {
        if (parameterValue == null) {
            throw new InvalidArgument("parameterValue", parameterValue);
        }
        if (!exists(kam)) return null;
        List<Integer> ids;
        try {
            ids = kamStoreDao(kam.getKamInfo()).getKamNodeCandidates(
                    namespace, parameterValue);
            List<KamNode> kamNodeList = new ArrayList<KamNode>(ids.size());
            for (Integer kamNodeId : ids) {
                kamNodeList.add(kam.findNode(kamNodeId));
            }
            return kamNodeList;
        } catch (SQLException e) {
            final String fmt = "error getting KAM nodes for %s";
            final String msg = format(fmt, kam.getKamInfo().getName());
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KamNode> getKamNodes(final Kam k, final SkinnyUUID uuid) {
        if (uuid == null) throw new InvalidArgument("uuid", uuid);
        if (!exists(k)) return null;
        List<Integer> ids;
        try {
            ids = kamStoreDao(k.getKamInfo()).getKamNodeCandidates(uuid);
            List<KamNode> kamNodeList = new ArrayList<KamNode>();
            for (Integer kamNodeId : ids) {
                kamNodeList.add(k.findNode(kamNodeId));
            }
            return kamNodeList;
        } catch (SQLException e) {
            final String fmt = "error getting KAM nodes for %s";
            final String msg = format(fmt, k.getKamInfo().getName());
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KamNode> getKamNodes(final Kam k, final FunctionEnum function,
            final SkinnyUUID uuid) {
        if (uuid == null) {
            throw new InvalidArgument("uuid", uuid);
        }
        if (!exists(k)) return null;
        List<Integer> ids;
        try {
            ids = kamStoreDao(k.getKamInfo()).getKamNodeCandidates(
                    function, uuid);
            List<KamNode> kamNodeList = new ArrayList<KamNode>();
            for (Integer kamNodeId : ids) {
                KamNode kn = k.findNode(kamNodeId);
                if (kn != null)
                    kamNodeList.add(kn);
            }
            return kamNodeList;
        } catch (SQLException e) {
            final String fmt = "error getting KAM nodes for %s";
            final String msg = format(fmt, k.getKamInfo().getName());
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KamNode> getKamNodes(final Kam kam, final KamNode example) {
        if (kam == null) {
            throw new InvalidArgument("kam", kam);
        }

        if (example == null) {
            throw new InvalidArgument("example", example);
        }
        if (!exists(kam)) return null;
        List<KamNode> kamNodeList = new ArrayList<KamNode>();
        try {
            final KAMStoreDao ksdao = kamStoreDao(kam.getKamInfo());
            List<Integer> kamNodeIdMatches =
                    ksdao.getKamNodeCandidates(example);
            for (Integer kamNodeId : kamNodeIdMatches) {
                kamNodeList.add(kam.findNode(kamNodeId));
            }
        } catch (SQLException e) {
            final String fmt = "error getting KAM nodes for %s";
            final String msg = format(fmt, kam.getKamInfo().getName());
            throw new KAMStoreException(msg, e);
        }
        return kamNodeList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamNode getKamNodeForTerm(Kam k, String t, FunctionEnum fx,
            SkinnyUUID[] uuids) {
        if (k == null) throw new InvalidArgument("k is null");
        if (t == null) throw new InvalidArgument("t is null");
        if (fx == null) throw new InvalidArgument("fx is null");
        if (uuids == null || uuids.length == 0)
            throw new InvalidArgument("uuids", uuids);

        try {
            final KAMStoreDao dao = kamStoreDao(k.getKamInfo());
            Integer nid = dao.getKamNodeForTerm(t, fx, uuids);
            return nid == null ? null : k.findNode(nid);
        } catch (SQLException e) {
            final String fmt = "error retrieving KAM node for term %s, kam %s";
            final String msg = format(fmt, t, k.getKamInfo().getName());
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Citation> getCitations(KamInfo kamInfo,
            BelDocumentInfo belDocumentInfo) {
        if (null == kamInfo) {
            throw new InvalidArgument("kamInfo", kamInfo);
        }
        if (!exists(kamInfo)) return null;

        try {
            return kamStoreDao(kamInfo).getCitations(belDocumentInfo);
        } catch (SQLException e) {
            final String fmt = "error getting KAM nodes for %s";
            final String msg = format(fmt, kamInfo.getName());
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Citation> getCitations(KamInfo kamInfo,
            BelDocumentInfo docInfo, CitationType type)
            throws InvalidArgument, KAMStoreException {
        if (null == kamInfo) {
            throw new InvalidArgument("kamInfo", kamInfo);
        }
        if (!exists(kamInfo)) return null;
        try {
            return kamStoreDao(kamInfo).getCitations(docInfo, type);
        } catch (SQLException e) {
            final String fmt = "error getting citations for %s";
            final String msg = format(fmt, kamInfo.getName());
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Citation> getCitations(KamInfo kamInfo,
            CitationType citationType) {
        if (null == kamInfo) {
            throw new InvalidArgument("kamInfo", kamInfo);
        }
        if (!exists(kamInfo)) return null;
        try {
            return kamStoreDao(kamInfo).getCitations(citationType);
        } catch (SQLException e) {
            final String fmt = "error getting citations for %s";
            final String msg = format(fmt, kamInfo.getName());
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Citation> getCitations(KamInfo kamInfo,
            CitationType type, String... referenceIds) {
        if (null == kamInfo) {
            throw new InvalidArgument("kamInfo", kamInfo);
        }
        if (!exists(kamInfo)) return null;
        try {
            return kamStoreDao(kamInfo).getCitations(type, referenceIds);
        } catch (SQLException e) {
            final String fmt = "error getting citations for %s";
            final String msg = format(fmt, kamInfo.getName());
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean collapseKamNode(KamInfo info, KamNode collapsing, KamNode collapseTo) {
        try {
            KAMUpdateDao updateDao = kamUpdateDao(info);
            return updateDao.collapseKamNode(collapsing, collapseTo);
        } catch (SQLException e) {
            final String fmt = "error collapsing node for %s";
            final String msg = format(fmt, info.getName());
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int removeKamEdges(KamInfo info, int[] edgeIds) {
        try {
            KAMUpdateDao updateDao = kamUpdateDao(info);
            return updateDao.removeKamEdges(edgeIds);
        } catch (SQLException e) {
            final String msg = "error removing edges with edge ids";
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int removeKamEdges(KamInfo info, RelationshipType relationship) {
        try {
            KAMUpdateDao updateDao = kamUpdateDao(info);
            return updateDao.removeKamEdges(relationship);
        } catch (SQLException e) {
            final String fmt = "error removing edges with relationship %s for %s";
            final String msg = format(fmt, relationship.getDisplayValue(),
                    info.getName());
            throw new KAMStoreException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int coalesceKamEdges(KamInfo info) {
        try {
            KAMUpdateDao updateDao = kamUpdateDao(info);
            return updateDao.coalesceKamEdges();
        } catch (SQLException e) {
            final String msg = "error coalescing edges";
            throw new KAMStoreException(msg, e);
        }
    }

    private KAMStoreDao kamStoreDao(KamInfo ki) throws SQLException {
        KAMStoreDao dao = daomap.get(ki);
        if (dao != null) return dao;

        dao = new KAMStoreDaoImpl(ki.getKamDbObject().getSchemaName(), dbc);
        daomap.put(ki, dao);
        return dao;
    }

    private KAMUpdateDao kamUpdateDao(KamInfo ki) throws SQLException {
        KAMUpdateDao dao = updatemap.get(ki);
        if (dao != null) return dao;

        String schema = ki.getKamDbObject().getSchemaName();
        dao = new KAMUpdateDaoImpl(dbc, schema);
        updatemap.put(ki, dao);
        return dao;
    }

    private EvidenceDao evidenceDao(KamInfo ki) throws SQLException {
        EvidenceDao dao = evdaomap.get(ki);
        if (dao != null) return dao;

        String schema = ki.getKamDbObject().getSchemaName();
        dao = new EvidenceDaoImpl(dbc, schema);
        evdaomap.put(ki, dao);
        return dao;
    }

    private KAMCatalogDao kamCatalogDao() throws SQLException {
        if (_dao != null) return _dao;

        _dao = new KAMCatalogDao(dbc, catalog, prefix);
        return _dao;
    }
}
