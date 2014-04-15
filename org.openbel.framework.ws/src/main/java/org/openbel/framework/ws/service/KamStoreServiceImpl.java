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
package org.openbel.framework.ws.service;

import static java.lang.String.format;
import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;
import static org.openbel.framework.common.Strings.KAM_REQUEST_NO_KAM_FOR_HANDLE;
import static org.openbel.framework.ws.utils.Converter.convert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openbel.framework.api.*;
import org.openbel.framework.api.internal.KAMCatalogDao;
import org.openbel.framework.api.internal.KAMCatalogDao.AnnotationFilter;
import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.BelDocumentInfo;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.ws.model.*;
import org.openbel.framework.ws.model.AnnotationFilterCriteria;
import org.openbel.framework.ws.model.Citation;
import org.openbel.framework.ws.model.Kam;
import org.openbel.framework.ws.model.Namespace;
import org.openbel.framework.ws.utils.Converter;
import org.openbel.framework.ws.utils.Converter.KamStoreObjectRef;
import org.openbel.framework.ws.utils.InvalidIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO Provide documentation
 */
@Service
public class KamStoreServiceImpl implements KamStoreService {
    private static final Logger logger = LoggerFactory
            .getLogger(KamStoreServiceImpl.class);

    @Autowired
    private KAMStore kAMStore;
    @Autowired
    private KAMCatalogDao kamCatalogDao;
    @Autowired
    private KamCacheService kamCacheService;

    public KamStoreServiceImpl() {
        logger.debug("Instantiating");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Citation> getCitations(KamHandle kamHandle,
            CitationType citationType,
            List<String> valueList, BelDocument belDocument)
            throws KamStoreServiceException {
        List<Citation> list = new ArrayList<Citation>();
        final String handle = kamHandle.getHandle();

        try {
            org.openbel.framework.api.Kam objKam;
            objKam = kamCacheService.getKam(handle);
            if (objKam == null) {
                throw new KamStoreServiceException(format(
                        KAM_REQUEST_NO_KAM_FOR_HANDLE, kamHandle.getHandle()));
            }

            org.openbel.framework.common.enums.CitationType citation;
            citation = convert(citationType);

            KamInfo ki = objKam.getKamInfo();
            List<org.openbel.framework.api.internal.KAMStoreDaoImpl.Citation> citations;
            if (belDocument != null) {
                BelDocumentInfo info;
                try {
                    info = convert(belDocument);
                } catch (InvalidIdException e) {
                    throw new KamStoreServiceException(
                            "Error processing Bel document", e);
                }

                citations = kAMStore.getCitations(ki, info, citation);
            } else if (hasItems(valueList)) {
                String[] valueArr = valueList.toArray(new String[0]);
                citations = kAMStore.getCitations(ki, citation, valueArr);
            } else {
                citations = kAMStore.getCitations(ki, citation);
            }

            for (org.openbel.framework.api.internal.KAMStoreDaoImpl.Citation c : citations) {
                Citation c2 = convert(c);
                list.add(c2);
            }
        } catch (KAMStoreException e) {
            logger.warn(e.getMessage());
            throw new KamStoreServiceException(e);
        }

        return list;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BelTerm> getSupportingTerms(KamNode kamNode)
            throws KamStoreServiceException {
        List<BelTerm> list = new ArrayList<BelTerm>();

        try {
            // Get the real Kam from the KamCache
            KamStoreObjectRef kamElementRef;
            try {
                kamElementRef = Converter.decodeNode(kamNode);
            } catch (InvalidIdException e) {
                logger.warn(e.getMessage());
                throw new KamStoreServiceException("Error processing KAM node",
                        e);
            }

            final KamInfo kamInfo =
                    getKamInfo(kamElementRef, "Error processing KAM node");
            final org.openbel.framework.api.Kam objKam =
                    kamCacheService.getKam(kamInfo.getName());
            if (objKam == null) {
                throw new KamStoreServiceException(
                        new InvalidIdException(kamElementRef.getEncodedString()));
            }

            // Get the real KamNode from the Kam
            org.openbel.framework.api.Kam.KamNode objKamNode =
                    objKam.findNode(kamElementRef.getKamStoreObjectId());
            // Get the supporting terms for the node
            for (org.openbel.framework.api.internal.KAMStoreDaoImpl.BelTerm objBelTerm : kAMStore
                    .getSupportingTerms(objKamNode)) {
                list.add(convert(objBelTerm, kamInfo));
            }
        } catch (KAMStoreException e) {
            logger.warn(e.getMessage());
            throw new KamStoreServiceException(e);
        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BelStatement> getSupportingEvidence(KamEdge kamEdge,
            KamFilter kamFilter) throws KamStoreServiceException {
        List<BelStatement> list = new ArrayList<BelStatement>();

        try {
            // Get the real Kam from the KamCache
            KamStoreObjectRef kamElementRef;
            try {
                kamElementRef = Converter.decodeEdge(kamEdge);
            } catch (InvalidIdException e) {
                throw new KamStoreServiceException("Error processing KAM edge",
                        e);
            }

            final KamInfo kamInfo =
                    getKamInfo(kamElementRef, "Error processing KAM edge");
            final org.openbel.framework.api.Kam objKam =
                    kamCacheService.getKam(kamInfo.getName());
            if (objKam == null) {
                throw new KamStoreServiceException(
                        new InvalidIdException(kamElementRef.getEncodedString()));
            }

            // Get the real KamEdge from the Kam
            org.openbel.framework.api.Kam.KamEdge objKamEdge =
                    objKam.findEdge(kamElementRef.getKamStoreObjectId());

            // Get the supporting evidence for the edge
            final List<org.openbel.framework.api.internal.KAMStoreDaoImpl.BelStatement> evidence;
            if (kamFilter != null) {
                // including edge and filter
                final List<AnnotationFilterCriteria> criteria =
                        kamFilter.getAnnotationCriteria();

                final List<org.openbel.framework.api.internal.KAMStoreDaoImpl.AnnotationType> al =
                        new ArrayList<org.openbel.framework.api.internal.KAMStoreDaoImpl.AnnotationType>();
                for (org.openbel.framework.api.internal.KAMStoreDaoImpl.AnnotationType a : kAMStore
                        .getAnnotationTypes(objKam)) {
                    al.add(a);
                }

                final AnnotationFilter annotationFilter =
                        objKam.getKamInfo().createAnnotationFilter();

                for (AnnotationFilterCriteria c : criteria) {
                    final AnnotationType type = c.getAnnotationType();

                    for (org.openbel.framework.api.internal.KAMStoreDaoImpl.AnnotationType a : al) {
                        if (type.getName().equals(a.getName())) {
                            org.openbel.framework.api.AnnotationFilterCriteria afc =
                                    new org.openbel.framework.api.AnnotationFilterCriteria(
                                            a);
                            afc.getValues().addAll(c.getValueSet());
                            afc.setInclude(c.isIsInclude());

                            annotationFilter.add(afc);
                            break;
                        }
                    }
                }

                evidence =
                        kAMStore.getSupportingEvidence(objKamEdge,
                                annotationFilter);
            } else {
                // including only edge
                evidence = kAMStore.getSupportingEvidence(objKamEdge);
            }

            // Convert and return
            for (org.openbel.framework.api.internal.KAMStoreDaoImpl.BelStatement statement : evidence) {
                list.add(convert(statement, kamInfo));
            }

        } catch (KAMStoreException e) {
            logger.warn(e.getMessage());
            throw new KamStoreServiceException(e);
        }

        return list;
    }

    @Override
    public List<EdgeStatement> getSupportingEvidenceMultiple(List<KamEdge> kamEdges, KamFilter kamFilter) throws KamStoreServiceException {
        List<EdgeStatement> list = new ArrayList<EdgeStatement>();
        if (kamEdges == null || kamEdges.isEmpty()) return null;

        try {
            // get KAM from first edge
            final List<org.openbel.framework.api.Kam.KamEdge> edges = new ArrayList<org.openbel.framework.api.Kam.KamEdge>(kamEdges.size());
            KamEdge first = kamEdges.iterator().next();

            // Get the real Kam from the KamCache
            org.openbel.framework.api.Kam kam;
            try {
                KamStoreObjectRef ref = Converter.decodeEdge(first);
                final KamInfo info = getKamInfo(ref, "Error processing KAM edge");
                kam = kamCacheService.getKam(info.getName());
                if (kam == null) {
                    throw new KamStoreServiceException(new InvalidIdException(ref.getEncodedString()));
                }
            } catch (InvalidIdException e) {
                throw new KamStoreServiceException("Error processing KAM edge", e);
            }

            // Convert kam edges
            for (KamEdge kamEdge : kamEdges) {
                // Get the real KamEdge from the Kam
                try {
                    KamStoreObjectRef ref = Converter.decodeEdge(kamEdge);
                    edges.add(kam.findEdge(ref.getKamStoreObjectId()));
                } catch (InvalidIdException e) {
                    throw new KamStoreServiceException("Error processing KAM edge", e);
                }
            }

            // Get the supporting evidence for the edge
            AnnotationFilter annotationFilter = null;
            if (kamFilter != null) {
                // including edge and filter
                final List<AnnotationFilterCriteria> criteria =
                        kamFilter.getAnnotationCriteria();

                final List<org.openbel.framework.api.internal.KAMStoreDaoImpl.AnnotationType> al =
                        new ArrayList<org.openbel.framework.api.internal.KAMStoreDaoImpl.AnnotationType>();
                for (org.openbel.framework.api.internal.KAMStoreDaoImpl.AnnotationType a : kAMStore
                        .getAnnotationTypes(kam)) {
                    al.add(a);
                }

                annotationFilter = kam.getKamInfo().createAnnotationFilter();

                for (AnnotationFilterCriteria c : criteria) {
                    final AnnotationType type = c.getAnnotationType();

                    for (org.openbel.framework.api.internal.KAMStoreDaoImpl.AnnotationType a : al) {
                        if (type.getName().equals(a.getName())) {
                            org.openbel.framework.api.AnnotationFilterCriteria afc =
                                    new org.openbel.framework.api.AnnotationFilterCriteria(
                                            a);
                            afc.getValues().addAll(c.getValueSet());
                            afc.setInclude(c.isIsInclude());

                            annotationFilter.add(afc);
                            break;
                        }
                    }
                }
            }

            // retrieve evidence (optionally use annotation filter)
            Map<org.openbel.framework.api.Kam.KamEdge, List<org.openbel.framework.common.model.Statement>> evidence;
            evidence = (annotationFilter != null) ?
                    kAMStore.getSupportingEvidence(edges, annotationFilter) :
                    kAMStore.getSupportingEvidence(edges);


            // convert to ws
            for (Map.Entry<org.openbel.framework.api.Kam.KamEdge, List<Statement>> entry : evidence.entrySet()) {
                String id = KamStoreObjectRef.encode(kam.getKamInfo(), entry.getKey());

                for (Statement stmt : entry.getValue()) {
                    org.openbel.framework.ws.model.Statement wsStmt = convert(stmt);
                    EdgeStatement edgeStatement = new EdgeStatement();
                    edgeStatement.setId(id);
                    edgeStatement.setStatement(wsStmt);
                    list.add(edgeStatement);
                }
            }

            return list;
        } catch (Throwable e) {
            logger.warn(e.getMessage());
            throw new KamStoreServiceException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Namespace> getNamespaces(KamHandle kamHandle)
            throws KamStoreServiceException {
        List<Namespace> list = new ArrayList<Namespace>();
        final String handle = kamHandle.getHandle();

        try {
            // Get the real Kam from the KamCache
            org.openbel.framework.api.Kam objKam =
                    kamCacheService.getKam(handle);
            if (objKam == null) {
                throw new KamStoreServiceException(format(
                        KAM_REQUEST_NO_KAM_FOR_HANDLE, handle));
            }
            // Get all the namespaces for this Kam
            for (org.openbel.framework.api.internal.KAMStoreDaoImpl.Namespace objNamespace : kAMStore
                    .getNamespaces(objKam)) {
                list.add(convert(objNamespace, objKam.getKamInfo()));
            }
        } catch (KAMStoreException e) {
            logger.warn(e.getMessage());
            throw new KamStoreServiceException(e);
        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AnnotationType> getAnnotationTypes(KamHandle kamHandle)
            throws KamStoreServiceException {
        List<AnnotationType> list = new ArrayList<AnnotationType>();
        final String handle = kamHandle.getHandle();

        try {
            // Get the real Kam from the KamCache
            org.openbel.framework.api.Kam objKam =
                    kamCacheService.getKam(handle);
            if (objKam == null) {
                throw new KamStoreServiceException(format(
                        KAM_REQUEST_NO_KAM_FOR_HANDLE, handle));
            }
            // Get all the annotationTypes for this Kam
            for (org.openbel.framework.api.internal.KAMStoreDaoImpl.AnnotationType objAnnotationType : kAMStore
                    .getAnnotationTypes(objKam)) {
                list.add(convert(objAnnotationType, objKam.getKamInfo()));
            }
        } catch (KAMStoreException e) {
            logger.warn(e.getMessage());
            throw new KamStoreServiceException(e);
        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BelDocument> getBelDocuments(KamHandle kamHandle)
            throws KamStoreServiceException {
        List<BelDocument> list = new ArrayList<BelDocument>();
        final String handle = kamHandle.getHandle();

        try {
            // Get the real Kam from the KamCache
            org.openbel.framework.api.Kam objKam =
                    kamCacheService.getKam(handle);
            if (objKam == null) {
                throw new KamStoreServiceException(format(
                        KAM_REQUEST_NO_KAM_FOR_HANDLE, handle));
            }
            // Get all the belDocuments for this Kam
            for (org.openbel.framework.api.internal.KAMStoreDaoImpl.BelDocumentInfo objBelDocument : kAMStore
                    .getBelDocumentInfos(objKam)) {
                list.add(convert(objBelDocument, objKam.getKamInfo()));
            }
        } catch (KAMStoreException e) {
            logger.warn(e.getMessage());
            throw new KamStoreServiceException(e);
        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Kam> getCatalog() throws KamStoreServiceException {
        List<Kam> list = new ArrayList<Kam>();
        try {
            for (KamInfo kamInfo : kamCatalogDao.getCatalog()) {
                list.add(convert(kamInfo));
            }
        } catch (SQLException e) {
            logger.warn(e.getMessage());
            throw new KamStoreServiceException(e.getMessage());
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<KamNode>
            getKamNodes(KamHandle kamHandle, final List<Node> nodes)
                    throws KamStoreServiceException {

        final String handle = kamHandle.getHandle();
        try {
            final org.openbel.framework.api.Kam kam =
                    kamCacheService
                            .getKam(handle);

            if (kam == null) {
                throw new KamStoreServiceException(format(
                        KAM_REQUEST_NO_KAM_FOR_HANDLE, handle));
            }

            final List<KamNode> resolvedKamNodes = sizedArrayList(nodes.size());
            for (final Node node : nodes) {
                resolvedKamNodes.add(convert(kam.getKamInfo(),
                        kAMStore.getKamNode(kam, node.getLabel())));
            }

            return resolvedKamNodes;
        } catch (KAMStoreException e) {
            throw new KamStoreServiceException(e);
        }
    }

    public void setKamStore(KAMStore kAMStore) {
        this.kAMStore = kAMStore;
    }

    public void setKamCatalogDao(KAMCatalogDao kamCatalogDao) {
        this.kamCatalogDao = kamCatalogDao;
    }

    public void setKamCacheService(KamCacheService kamCacheService) {
        this.kamCacheService = kamCacheService;
    }

    private KamInfo getKamInfo(final KamStoreObjectRef kamElementRef,
            String errorMsg)
            throws KamStoreServiceException {

        final int kamInfoId = kamElementRef.getKamInfoId();
        KamInfo kamInfo = null;
        try {
            kamInfo = kamCatalogDao.getKamInfoById(kamInfoId);
        } catch (SQLException e) {
            throw new KamStoreServiceException(errorMsg, e);
        }
        if (kamInfo == null) {
            throw new KamStoreServiceException(
                    new InvalidIdException(kamElementRef.getEncodedString()));
        }
        return kamInfo;
    }
}
