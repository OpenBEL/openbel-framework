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
package org.openbel.framework.ws.utils;

import static java.util.Collections.emptyList;
import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;
import static org.apache.commons.lang.StringUtils.join;
import static org.apache.commons.lang.StringUtils.split;
import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.noLength;
import static org.openbel.framework.common.enums.CitationType.BOOK;
import static org.openbel.framework.common.enums.CitationType.JOURNAL;
import static org.openbel.framework.common.enums.CitationType.ONLINE_RESOURCE;
import static org.openbel.framework.common.enums.CitationType.OTHER;
import static org.openbel.framework.common.enums.CitationType.PUBMED;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.openbel.framework.api.KamDialect;
import org.openbel.framework.api.KamImpl;
import org.openbel.framework.api.KamStore;
import org.openbel.framework.api.KamStoreException;
import org.openbel.framework.api.KamStoreObject;
import org.openbel.framework.api.internal.KAMCatalogDao;
import org.openbel.framework.api.internal.KAMStoreDaoImpl;
import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.BelDocumentInfo;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.KamProtoEdge;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.KamProtoNode;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.TermParameter;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.ReturnType;
import org.openbel.framework.ws.dialect.BELSyntax;
import org.openbel.framework.ws.model.AnnotationDefinitionType;
import org.openbel.framework.ws.model.AnnotationFilterCriteria;
import org.openbel.framework.ws.model.AnnotationType;
import org.openbel.framework.ws.model.BelDocument;
import org.openbel.framework.ws.model.BelDocumentFilterCriteria;
import org.openbel.framework.ws.model.BelStatement;
import org.openbel.framework.ws.model.BelSyntax;
import org.openbel.framework.ws.model.BelTerm;
import org.openbel.framework.ws.model.Citation;
import org.openbel.framework.ws.model.CitationFilterCriteria;
import org.openbel.framework.ws.model.CitationType;
import org.openbel.framework.ws.model.EdgeDirectionType;
import org.openbel.framework.ws.model.EdgeFilter;
import org.openbel.framework.ws.model.FunctionReturnType;
import org.openbel.framework.ws.model.FunctionReturnTypeFilterCriteria;
import org.openbel.framework.ws.model.FunctionType;
import org.openbel.framework.ws.model.FunctionTypeFilterCriteria;
import org.openbel.framework.ws.model.Kam;
import org.openbel.framework.ws.model.KamEdge;
import org.openbel.framework.ws.model.KamFilter;
import org.openbel.framework.ws.model.KamNode;
import org.openbel.framework.ws.model.Namespace;
import org.openbel.framework.ws.model.NamespaceFilterCriteria;
import org.openbel.framework.ws.model.NodeFilter;
import org.openbel.framework.ws.model.ObjectFactory;
import org.openbel.framework.ws.model.RelationshipType;
import org.openbel.framework.ws.model.RelationshipTypeFilterCriteria;
import org.openbel.framework.ws.model.SimplePath;

/**
 * Provides static converter methods to go from KamStore objects to JAXB
 * wire-safe objects for return via the SOAP interface TODO Document
 *
 * @author julianjray
 */
public class Converter {

    public static final String FIELD_SEP = "|";
    private static final Charset ASCII = Charset.forName("US-ASCII");
    private static final GregorianCalendar calendar = new GregorianCalendar();
    private static final ObjectFactory OBJECT_FACTORY = ObjectFactorySingleton
            .getInstance();

    /**
     * Converts a {@link KamEdge ws kam edge} to a
     * {@link org.openbel.framework.api.Kam.KamEdge kam edge}
     * using the {@link org.openbel.framework.api.Kam kam}.
     *
     * @param kamEdge the {@link KamEdge ws kam edge} to convert from
     * @param kam the {@link org.openbel.framework.api.Kam kam}
     * to find the kam store kam edge
     * @return the {@link org.openbel.framework.api.Kam.KamEdge
     * kam edge} to convert to
     * @throws InvalidIdException Thrown if the kam edge id is invalid
     */
    public static org.openbel.framework.api.Kam.KamEdge
            convert(KamEdge kamEdge, org.openbel.framework.api.Kam kam)
                    throws InvalidIdException {
        if (kamEdge == null) {
            return null;
        }

        final KamStoreObjectRef ref = decodeEdge(kamEdge);
        int edgeId = ref.getKamStoreObjectId();

        return kam.findEdge(edgeId);
    }

    /**
     * @param edgeDirectionType
     * @return
     */
    public static org.openbel.framework.api.EdgeDirectionType
            convert(EdgeDirectionType edgeDirectionType) {
        if (null == edgeDirectionType) {
            return null;
        } else if (edgeDirectionType == EdgeDirectionType.FORWARD) {
            return org.openbel.framework.api.EdgeDirectionType.FORWARD;
        } else if (edgeDirectionType == EdgeDirectionType.REVERSE) {
            return org.openbel.framework.api.EdgeDirectionType.REVERSE;
        } else {
            return org.openbel.framework.api.EdgeDirectionType.BOTH;
        }
    }

    public static org.openbel.framework.api.EdgeFilter convert(
            final org.openbel.framework.api.Kam kam,
            final EdgeFilter filter) {
        if (filter == null) {
            return null;
        }
        org.openbel.framework.api.EdgeFilter kamEdgeFilter;
        kamEdgeFilter = kam.createEdgeFilter();

        final List<RelationshipTypeFilterCriteria> criterion;
        criterion = filter.getRelationshipCriteria();

        for (final RelationshipTypeFilterCriteria criteria : criterion) {
            kamEdgeFilter.add(convert(criteria));
        }

        return kamEdgeFilter;
    }

    public static org.openbel.framework.api.NodeFilter convert(
            final org.openbel.framework.api.Kam kam,
            NodeFilter nodeFilter) {
        if (null != nodeFilter) {
            final org.openbel.framework.api.NodeFilter kamNodeFilter =
                    kam
                            .createNodeFilter();

            // handle function type criteria
            List<FunctionTypeFilterCriteria> ftCriterion = nodeFilter
                    .getFunctionTypeCriteria();

            for (final FunctionTypeFilterCriteria criteria : ftCriterion) {
                final org.openbel.framework.api.FunctionTypeFilterCriteria kamNodeCriteria =
                        new org.openbel.framework.api.FunctionTypeFilterCriteria();

                kamNodeCriteria.setInclude(criteria.isIsInclude());

                final List<FunctionType> functionTypes = criteria.getValueSet();
                for (final FunctionType functionType : functionTypes) {
                    final org.openbel.framework.common.enums.FunctionEnum kamNodeFunctionType =
                            convert(functionType);
                    kamNodeCriteria.add(kamNodeFunctionType);
                }

                kamNodeFilter.add(kamNodeCriteria);
            }

            // handle function return type criteria
            final List<FunctionReturnTypeFilterCriteria> frtCriterion =
                    nodeFilter
                            .getFunctionReturnCriteria();

            for (final FunctionReturnTypeFilterCriteria criteria : frtCriterion) {
                final org.openbel.framework.api.FunctionReturnFilterCriteria kamNodeCriteria =
                        new org.openbel.framework.api.FunctionReturnFilterCriteria();

                kamNodeCriteria.setInclude(criteria.isIsInclude());

                final List<FunctionReturnType> functionReturnTypes =
                        criteria.getValueSet();
                for (final FunctionReturnType functionReturnType : functionReturnTypes) {
                    final ReturnType kamNodeReturnType =
                            convert(functionReturnType);
                    kamNodeCriteria.add(kamNodeReturnType);
                }

                kamNodeFilter.add(kamNodeCriteria);
            }

            return kamNodeFilter;
        }
        return null;
    }

    /**
     * Converts a {@link KamFilter ws KAM filter} into a {@link KAMCatalogDao.KamFilter KAM filter}.
     * @param wsFilter
     * @param kamInfo
     * @return
     * @throws InvalidIdException
     */
    public static KAMCatalogDao.KamFilter convert(KamFilter wsFilter,
            KamInfo kamInfo) throws InvalidIdException {
        if (null != wsFilter) {
            // create a new one and send it back
            KAMCatalogDao.KamFilter ret = kamInfo.createKamFilter();

            for (AnnotationFilterCriteria ac : wsFilter.getAnnotationCriteria()) {
                ret.add(convert(ac));
            }
            for (BelDocumentFilterCriteria bc : wsFilter.getDocumentCriteria()) {
                ret.add(convert(bc));
            }
            for (CitationFilterCriteria cc : wsFilter.getCitationCriteria()) {
                ret.add(convert(cc));
            }
            for (RelationshipTypeFilterCriteria rc : wsFilter
                    .getRelationshipCriteria()) {
                ret.add(convert(rc));
            }

            return ret;
        }
        return null;
    }

    /**
     * Converts a {@link AnnotationFilterCriteria ws annotation filter criteria} into
     * an {@link org.openbel.framework.api.AnnotationFilterCriteria
     * annotation filter criteria}.
     * @param wsFilterCriteria
     * @return
     * @throws InvalidIdException
     */
    public static
            org.openbel.framework.api.AnnotationFilterCriteria
            convert(AnnotationFilterCriteria wsFilterCriteria)
                    throws InvalidIdException {

        org.openbel.framework.api.AnnotationFilterCriteria filterCriteria =
                new org.openbel.framework.api.AnnotationFilterCriteria(
                        convert(wsFilterCriteria.getAnnotationType()));

        filterCriteria.setInclude(wsFilterCriteria.isIsInclude());

        for (String s : wsFilterCriteria.getValueSet()) {
            filterCriteria.add(s);
        }

        return filterCriteria;
    }

    /**
     * Converts a {@link AnnotationType ws annotation type} to an
     * {@link KAMStoreDaoImpl.AnnotationType annotation type}.
     * @param wsAnnotationType
     * @return
     * @throws InvalidIdException
     */
    public static KAMStoreDaoImpl.AnnotationType convert(
            AnnotationType wsAnnotationType) throws InvalidIdException {

        String wsId = wsAnnotationType.getId();
        String wsName = wsAnnotationType.getName();
        String wsDescription = wsAnnotationType.getDescription();
        String wsUsage = wsAnnotationType.getUsage();
        AnnotationDefinitionType wsAdt =
                wsAnnotationType.getAnnotationDefinitionType();

        final int id =
                KamStoreObjectRef.decode(wsId,
                        KAMStoreDaoImpl.AnnotationType.class)
                        .getKamStoreObjectId();
        KAMStoreDaoImpl.AnnotationDefinitionType adt = convert(wsAdt);

        return new KAMStoreDaoImpl.AnnotationType(id, wsName, wsDescription,
                wsUsage, adt);
    }

    /**
     * Converts a {@link AnnotationDefinitionType ws annotation definition type} to a
     * {@link KAMStoreDaoImpl.AnnotationDefinitionType annotation definition type}.
     * @param wsAdt
     * @return
     */
    public static KAMStoreDaoImpl.AnnotationDefinitionType convert(
            AnnotationDefinitionType wsAdt) {

        switch (wsAdt) {
        case ENUMERATION:
            return KAMStoreDaoImpl.AnnotationDefinitionType.ENUMERATION;
        case REGULAR_EXPRESSION:
            return KAMStoreDaoImpl.AnnotationDefinitionType.REGULAR_EXPRESSION;
        case URL:
            return KAMStoreDaoImpl.AnnotationDefinitionType.URL;
        }
        return null;
    }

    /**
     * Converts a {@link CitationFilterCriteria ws citation filter criteria} into a
     * {@link org.openbel.framework.api.CitationFilterCriteria
     * citation filter criteria}.
     * @param wsFilterCriteria
     * @return
     * @throws InvalidIdException
     */
    public static
            org.openbel.framework.api.CitationFilterCriteria
            convert(CitationFilterCriteria wsFilterCriteria)
                    throws InvalidIdException {

        org.openbel.framework.api.CitationFilterCriteria objFilterCriteria =
                new org.openbel.framework.api.CitationFilterCriteria();
        objFilterCriteria.setInclude(wsFilterCriteria.isIsInclude());

        for (Citation citation : wsFilterCriteria.getValueSet()) {
            objFilterCriteria.add(convert(citation));
        }
        return objFilterCriteria;
    }

    /**
     * Converts a {@link Citation ws citation} to a {@link KAMStoreDaoImpl.Citation citation}.
     *
     * @param wsCitation
     * @return
     */
    public static KAMStoreDaoImpl.Citation convert(Citation wsCitation)
            throws InvalidIdException {

        String wsId = wsCitation.getId();
        String wsName = wsCitation.getName();
        CitationType wsCitationType = wsCitation.getCitationType();
        String wsComment = wsCitation.getComment();
        XMLGregorianCalendar wsPublicationDate =
                wsCitation.getPublicationDate();
        List<String> wsAuthors = wsCitation.getAuthors();

        Integer id = null;
        try {
            id = Integer.parseInt(wsId);
        } catch (NumberFormatException e) {
            throw new InvalidIdException(wsId);
        }
        List<String> authors =
                (wsAuthors == null ? new ArrayList<String>() : wsAuthors);
        Date publicationDate = (wsPublicationDate != null ?
                wsPublicationDate.toGregorianCalendar().getTime() : null);

        return new KAMStoreDaoImpl.Citation(
                wsName, id.toString(), wsComment, publicationDate, authors,
                convert(wsCitationType));
    }

    /**
     * @param filterCriteria
     * @return
     */
    public static
            org.openbel.framework.api.CitationFilterCriteria
            convert(NamespaceFilterCriteria filterCriteria) {

        org.openbel.framework.api.CitationFilterCriteria objFilterCriteria =
                new org.openbel.framework.api.CitationFilterCriteria();
        objFilterCriteria.setInclude(filterCriteria.isIsInclude());
        for (@SuppressWarnings("unused")
        Namespace namespace : filterCriteria.getValueSet()) {
            // objFilterCriteria.getValues().add(convert(namespace));
        }
        return objFilterCriteria;
    }

    /**
     * @param wsFilterCriteria
     * @return
     */
    public static
            org.openbel.framework.api.BelDocumentFilterCriteria
            convert(BelDocumentFilterCriteria wsFilterCriteria)
                    throws InvalidIdException {

        org.openbel.framework.api.BelDocumentFilterCriteria objFilterCriteria =
                new org.openbel.framework.api.BelDocumentFilterCriteria();
        objFilterCriteria.setInclude(wsFilterCriteria.isIsInclude());
        for (BelDocument belDocument : wsFilterCriteria.getValueSet()) {
            objFilterCriteria.add(convert(belDocument));
        }
        return objFilterCriteria;
    }

    /**
     * @param wsFilterCriteria
     * @return
     */
    public static
            org.openbel.framework.api.RelationshipTypeFilterCriteria
            convert(RelationshipTypeFilterCriteria wsFilterCriteria) {

        org.openbel.framework.api.RelationshipTypeFilterCriteria objFilterCriteria =
                new org.openbel.framework.api.RelationshipTypeFilterCriteria();

        objFilterCriteria.setInclude(wsFilterCriteria.isIsInclude());

        for (RelationshipType wsRelationshipType : wsFilterCriteria
                .getValueSet()) {
            objFilterCriteria.add(convert(wsRelationshipType));
        }

        return objFilterCriteria;
    }

    /**
     * @param objBelTerm
     * @return
     */
    public static BelTerm convert(KAMStoreDaoImpl.BelTerm objBelTerm,
            final KamInfo kamInfo) {
        BelTerm belTerm = OBJECT_FACTORY.createBelTerm();
        belTerm.setId(KamStoreObjectRef.encode(kamInfo, objBelTerm));
        belTerm.setLabel(objBelTerm.getLabel());

        return belTerm;
    }

    /**
     * @param objBelStatement
     * @return
     */
    public static BelStatement
            convert(KAMStoreDaoImpl.BelStatement objBelStatement,
                    final KamInfo kamInfo) {
        BelStatement belStatement = OBJECT_FACTORY.createBelStatement();
        belStatement.setDocument(convert(objBelStatement
                .getBelDocumentInfo(), kamInfo));
        belStatement.setId(KamStoreObjectRef.encode(kamInfo, objBelStatement));
        if (null != objBelStatement.getObject()) {
            if (objBelStatement.getObject() instanceof KAMStoreDaoImpl.BelTerm) {
                belStatement
                        .setObjectTerm(convert(
                                (KAMStoreDaoImpl.BelTerm) objBelStatement
                                        .getObject(), kamInfo));
            } else if (objBelStatement.getObject() instanceof KAMStoreDaoImpl.BelStatement) {
                belStatement
                        .setObjectStatement(convert(
                                (KAMStoreDaoImpl.BelStatement) objBelStatement
                                        .getObject(), kamInfo));
            }
        }
        belStatement.setRelationship(convert(objBelStatement
                .getRelationshipType()));
        belStatement.setSubjectTerm(convert(objBelStatement.getSubject(),
                kamInfo));

        belStatement.setCitation(convert(objBelStatement.getCitation()));

        List<KAMStoreDaoImpl.Annotation> objAnnotations =
                objBelStatement.getAnnotationList();
        for (KAMStoreDaoImpl.Annotation objAnnotation : objAnnotations) {
            belStatement.getAnnotations().add(convert(kamInfo, objAnnotation));
        }

        return belStatement;

    }

    /**
     * @param kam
     * @param objAnnotation
     * @return
     */
    public static org.openbel.framework.ws.model.Annotation convert(
            final KamInfo kamInfo,
            KAMStoreDaoImpl.Annotation objAnnotation) {
        AnnotationType convertedType = convert(
                objAnnotation.getAnnotationType(), kamInfo);

        org.openbel.framework.ws.model.Annotation annotation =
                new org.openbel.framework.ws.model.Annotation();
        annotation.setAnnotationType(convertedType);
        annotation.setId(KamStoreObjectRef.encode(kamInfo, objAnnotation));
        annotation.setValue(objAnnotation.getValue());

        return annotation;
    }

    public static
            RelationshipType
            convert(final org.openbel.framework.common.enums.RelationshipType relationshipTypeEnum) {
        if (relationshipTypeEnum == null) {
            return null;
        }

        return RelationshipType.fromValue(relationshipTypeEnum.name());
    }

    public static org.openbel.framework.common.enums.RelationshipType
            convert(final RelationshipType relationshipTypeEnum) {
        if (relationshipTypeEnum == null
                || relationshipTypeEnum == RelationshipType.UNKNOWN) {
            return null;
        }

        return org.openbel.framework.common.enums.RelationshipType
                .valueOf(relationshipTypeEnum.name());
    }

    public static
            FunctionType
            convert(
                    final org.openbel.framework.common.enums.FunctionEnum functionEnum) {
        if (functionEnum == null) {
            return null;
        }

        return FunctionType.fromValue(functionEnum.name());
    }

    public static FunctionEnum convert(final FunctionType functionType) {
        if (functionType == null) {
            return null;
        }

        return FunctionEnum.valueOf(functionType.name());
    }

    public static FunctionReturnType convert(final ReturnType returnType) {
        if (returnType == null) {
            return null;
        }

        return FunctionReturnType.fromValue(returnType.name());
    }

    public static ReturnType
            convert(final FunctionReturnType functionReturnType) {
        if (functionReturnType == null) {
            return null;
        }

        return ReturnType.valueOf(functionReturnType.name());
    }

    /**
     * Converts the KamStore kam node to a {@link KamNode ws kam node}.
     *
     * @param objKamNode the KamStore kam node to convert, which can be
     * {@code null}
     * @return the converted {@link KamNode ws kam node} or {@code null} if the
     * KamStore kam node was {@code null}
     */
    public static KamNode convert(final KamInfo kamInfo,
            org.openbel.framework.api.Kam.KamNode objKamNode) {
        if (objKamNode == null) {
            return null;
        }

        KamNode kamNode = OBJECT_FACTORY.createKamNode();
        kamNode.setFunction(convert(objKamNode.getFunctionType()));
        kamNode.setId(KamStoreObjectRef.encode(kamInfo, objKamNode));
        kamNode.setLabel(objKamNode.getLabel());
        return kamNode;
    }

    /**
     * Converts the KamStore kam edge to a {@link KamEdge ws kam edge}.
     *
     * @param objKamEdge the KamStore kam edge to convert, which can be
     * {@code null}
     * @return the converted {@link KamEdge ws kam edge} or {@code null} if the
     * KamStore kam edge was {@code null}
     */
    public static KamEdge convert(final KamInfo kamInfo,
            org.openbel.framework.api.Kam.KamEdge objKamEdge) {
        if (objKamEdge == null) {
            return null;
        }

        KamEdge kamEdge = OBJECT_FACTORY.createKamEdge();
        kamEdge.setId(KamStoreObjectRef.encode(kamInfo, objKamEdge));
        kamEdge.setRelationship(convert(objKamEdge.getRelationshipType()));
        kamEdge.setSource(convert(kamInfo, objKamEdge.getSourceNode()));
        kamEdge.setTarget(convert(kamInfo, objKamEdge.getTargetNode()));
        return kamEdge;
    }

    /**
     * Converts {@link org.openbel.framework.common.enums.CitationType} to
     * {@link CitationType}.
     *
     * @param objCitationType Citation type
     * @return {@link CitationType}; may be null
     */
    public static
            CitationType
            convert(
                    org.openbel.framework.common.enums.CitationType objCitationType) {
        if (objCitationType == null) {
            return null;
        }
        switch (objCitationType) {
        case BOOK:
            return CitationType.BOOK;
        case JOURNAL:
            return CitationType.JOURNAL;
        case ONLINE_RESOURCE:
            return CitationType.ONLINE_RESOURCE;
        case OTHER:
            return CitationType.OTHER;
        case PUBMED:
            return CitationType.PUBMED;
        default:
            return null;
        }
    }

    /**
     * @param objCitation
     * @return
     */
    public static Citation
            convert(KAMStoreDaoImpl.Citation objCitation) {

        Citation citation = OBJECT_FACTORY.createCitation();
        citation.setCitationType(convert(objCitation.getCitationType()));
        citation.setComment(objCitation.getComment());
        // citation id should not be encrypted, as it has a specific meaning
        citation.setId(objCitation.getId());
        citation.setName(objCitation.getName());

        Date pubdate = objCitation.getPublicationDate();
        if (pubdate != null) {
            try {
                calendar.setTime(pubdate);
                citation.setPublicationDate(DatatypeFactory.newInstance()
                        .newXMLGregorianCalendar(calendar));
            } catch (DatatypeConfigurationException e) {
                // Swallowed
            }
        }

        List<String> authors = objCitation.getAuthors();
        if (authors != null) {
            citation.getAuthors().addAll(authors);
        }

        return citation;
    }

    /**
     * @param kamInfo
     * @return
     */
    public static Kam
            convert(KAMCatalogDao.KamInfo kamInfo) {

        Kam kam = OBJECT_FACTORY.createKam();
        try {
            kam.setId(KamStoreObjectRef.encode(kamInfo, kamInfo));
            kam.setDescription(kamInfo.getKamDbObject().getDescription());
            calendar.setTime(kamInfo.getKamDbObject().getLastCompiled());
            kam.setLastCompiled(DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(calendar));
            kam.setName(kamInfo.getKamDbObject().getName());
        } catch (DatatypeConfigurationException e) {
            // Swallowed
        }
        return kam;
    }

    /**
     * @param objAnnotationType
     * @return
     */
    public static AnnotationType
            convert(KAMStoreDaoImpl.AnnotationType objAnnotationType,
                    final KamInfo kamInfo) {

        AnnotationType annotationType =
                OBJECT_FACTORY.createAnnotationType();
        annotationType.setDescription(objAnnotationType.getDescription());
        annotationType.setId(KamStoreObjectRef.encode(kamInfo,
                objAnnotationType));
        annotationType.setName(objAnnotationType.getName());
        annotationType.setUsage(objAnnotationType.getUsage());
        annotationType.setAnnotationDefinitionType(
                AnnotationDefinitionType.valueOf(objAnnotationType
                        .getAnnotationDefinitionType().toString())
                );
        return annotationType;
    }

    /**
     * @param objBelDocument
     * @return
     */
    public static BelDocument convert(KAMStoreDaoImpl.BelDocumentInfo docinfo,
            final KamInfo kamInfo) {

        BelDocument belDocument = OBJECT_FACTORY.createBelDocument();
        String authors = docinfo.getAuthors();
        if (authors != null) {
            for (final String author : split(authors, FIELD_SEP)) {
                belDocument.getAuthors().add(author);
            }
        }
        belDocument.setContactInfo(docinfo.getContactInfo());
        belDocument.setCopyright(docinfo.getCopyright());
        belDocument.setDescription(docinfo.getDescription());
        belDocument.setDisclaimer(docinfo.getDisclaimer());
        belDocument.setId(KamStoreObjectRef.encode(kamInfo, docinfo));
        belDocument.setLicenseInfo(docinfo.getLicenseInfo());
        belDocument.setName(docinfo.getName());
        belDocument.setVersion(docinfo.getVersion());

        for (KAMStoreDaoImpl.AnnotationType objAnnotationType : docinfo
                .getAnnotationTypes()) {
            belDocument.getAnnotationTypes().add(
                    convert(objAnnotationType, kamInfo));
        }

        for (KAMStoreDaoImpl.Namespace namespace : docinfo
                .getNamespaces()) {
            belDocument.getNamespaces().add(convert(namespace, kamInfo));
        }

        return belDocument;
    }

    public static KAMStoreDaoImpl.BelDocumentInfo convert(BelDocument source)
            throws InvalidIdException {
        String id = source.getId();
        String name = source.getName();
        String description = source.getDescription();
        String version = source.getVersion();
        String copyright = source.getCopyright();
        String disclaimer = source.getDisclaimer();
        String contactInfo = source.getContactInfo();
        String licenseInfo = source.getLicenseInfo();
        List<String> authors = source.getAuthors();

        int intId =
                KamStoreObjectRef.decode(id,
                        KAMStoreDaoImpl.BelDocumentInfo.class)
                        .getKamStoreObjectId();

        String authorString = null;
        if (hasItems(authors)) {
            authorString = join(authors, FIELD_SEP);
        }

        List<KAMStoreDaoImpl.AnnotationType> emptyAnnos = emptyList();
        List<KAMStoreDaoImpl.Namespace> emptyNSs = emptyList();

        final KAMStoreDaoImpl.BelDocumentInfo ret =
                new KAMStoreDaoImpl.BelDocumentInfo(
                        intId, name, description,
                        version, copyright, disclaimer, contactInfo,
                        licenseInfo,
                        authorString, emptyAnnos, emptyNSs);

        return ret;
    }

    /**
     * @param objNamespace
     * @return
     */
    public static Namespace
            convert(KAMStoreDaoImpl.Namespace objNamespace,
                    final KamInfo kamInfo) {

        Namespace namespace = OBJECT_FACTORY.createNamespace();
        namespace.setId(KamStoreObjectRef.encode(kamInfo, objNamespace));
        namespace.setPrefix(objNamespace.getPrefix());
        namespace.setResourceLocation(objNamespace.getResourceLocation());

        return namespace;
    }

    public static KAMStoreDaoImpl.Namespace convert(final Namespace src,
            final org.openbel.framework.api.Kam kam,
            final KamStore kamStore) throws InvalidArgument, KamStoreException {
        if (src == null || src.getResourceLocation() == null) {
            return null;
        }

        return kamStore.getNamespace(kam, src.getResourceLocation());
    }

    /**
     * Convert a common namespace to a WS namespace. WS namespace will not have
     * its ID populated as it is not a {@link KamStoreObject}
     *
     * @param ns
     * @return
     */
    public static Namespace convert(
            final org.openbel.framework.common.model.Namespace ns) {
        Namespace ws = OBJECT_FACTORY.createNamespace();
        ws.setPrefix(ns.getPrefix());
        ws.setResourceLocation(ns.getResourceLocation());
        return ws;
    }

    /**
     * Convert a WS namespace to a common namespace
     *
     * @param ns
     * @return
     */
    public static org.openbel.framework.common.model.Namespace convert(
            final Namespace ws) {
        return new org.openbel.framework.common.model.Namespace(
                ws.getPrefix(), ws.getResourceLocation());
    }

    public static org.openbel.framework.common.enums.CitationType convert(
            CitationType c) {
        if (c == null) {
            return null;
        }
        switch (c) {
        case BOOK:
            return BOOK;
        case JOURNAL:
            return JOURNAL;
        case ONLINE_RESOURCE:
            return ONLINE_RESOURCE;
        case OTHER:
            return OTHER;
        case PUBMED:
            return PUBMED;
        default:
            return null;
        }
    }

    public static SimplePath convert(
            final org.openbel.framework.api.SimplePath simplePath,
            final KamInfo kamInfo) {
        final SimplePath wsSimplePath = new SimplePath();

        wsSimplePath.setSource(convert(kamInfo, simplePath.getSource()));
        wsSimplePath.setTarget(convert(kamInfo, simplePath.getTarget()));

        for (org.openbel.framework.api.Kam.KamEdge kamEdge : simplePath
                .getEdges()) {
            wsSimplePath.getEdges().add(convert(kamInfo, kamEdge));
        }

        return wsSimplePath;
    }

    public static BELSyntax convert(BelSyntax ws) {
        switch (ws) {
        case LONG_FORM:
            return BELSyntax.LONG_FORM;
        case SHORT_FORM:
            return BELSyntax.SHORT_FORM;
        default:
            throw new UnsupportedOperationException("Unsupported syntax: " + ws);
        }
    }

    public static KamStoreObjectRef decodeNode(final KamNode kamNode)
            throws InvalidIdException {
        return KamStoreObjectRef.decode(kamNode.getId());
    }

    public static KamStoreObjectRef decodeEdge(final KamEdge kamEdge)
            throws InvalidIdException {
        return KamStoreObjectRef.decode(kamEdge.getId());
    }

    public static final class KamStoreObjectRef {

        private final int kamInfoId;
        private final int kamStoreObjectId;
        private final Class<? extends KamStoreObject> kamStoreObjectClass;
        private final String encodedString;

        private KamStoreObjectRef(final int kamInfoId,
                final int kamStoreObjectId,
                final Class<? extends KamStoreObject> kamStoreObjectClass,
                final String encodedString) {
            if (kamStoreObjectId < 1) {
                throw new InvalidArgument("kamStoreObjectId", kamStoreObjectId);
            }

            if (kamStoreObjectClass == null) {
                throw new InvalidArgument("kamStoreObjectClass",
                        kamStoreObjectClass);
            }

            this.kamInfoId = kamInfoId;
            this.kamStoreObjectId = kamStoreObjectId;
            this.kamStoreObjectClass = kamStoreObjectClass;
            this.encodedString = encodedString;
        }

        public int getKamInfoId() {
            return kamInfoId;
        }

        public int getKamStoreObjectId() {
            return kamStoreObjectId;
        }

        public Class<? extends KamStoreObject> getKamStoreObjectClass() {
            return kamStoreObjectClass;
        }

        public String getEncodedString() {
            return encodedString;
        }

        /**
         * Encodes the KamInfo and a KamStoreObject instance as a fixed-length, identifying string.
         * @param kamInfo
         * @param obj
         * @return
         */
        public static String encode(final KamInfo kamInfo,
                final KamStoreObject obj) {
            if (obj == null) {
                return null;
            }
            final Integer id = obj.getId();
            if (id == null) {
                return null;
            }
            byte prefix =
                    KamStoreObjectType.fromClass(obj.getClass())
                            .getRepresentation();
            return encode(kamInfo.getId().intValue(), id.intValue(), prefix);
        }

        /**
         * Decodes a string that was encoded with {@link encode} back to the id of
         * some KamStoreObject.
         * @param encoded
         * @param expectedClass The class of a subclass of KamStoreObject that will be
         * used to verify the type encoded in the {@code encode()}ed string.  If expectedClass
         * is null then no verification check is performed.
         * @return
         * @throws InvalidIdException Thrown if {@code encoded} is {@code null} or empty, or
         * if there was an error decoding the {@link encode()}ed string.
         */
        public static KamStoreObjectRef decode(String encoded,
                Class<? extends KamStoreObject> expectedClass)
                throws InvalidIdException {
            return decode(encoded, KamStoreObjectType.fromClass(expectedClass));
        }

        /**
         * Identical to {@link decode} except that
         * @param encoded
         * @return
         * @throws InvalidIdException
         */
        public static KamStoreObjectRef decode(String encoded)
                throws InvalidIdException {
            return decode(encoded, (KamStoreObjectType) null);
        }

        /*
         * Private encoding and decoding methods:
         */

        private static String encode(final int kamInfoId, int id, byte prefix) {

            final byte[] unencoded = new byte[8];
            Arrays.fill(unencoded, (byte) 0);
            ByteBuffer buffer =
                    ByteBuffer.allocate(8).putInt(kamInfoId).putInt(id);
            buffer.flip();
            buffer.get(unencoded);

            final byte[] encoded = encodeBase64(unencoded);

            // Because padding is added for the Base64 encoding, the last byte of encoded
            // is actually encoded padding and is always '='.  This is of no use so
            // truncate that last byte and add the prefix to the beginning of encoded.
            shiftRight(encoded);
            encoded[0] = prefix;

            return new String(encoded, ASCII);
        }

        private static KamStoreObjectRef decode(String encodedStr,
                KamStoreObjectType expected) throws InvalidIdException {
            try {
                if (noLength(encodedStr)) {
                    throw new InvalidIdException();
                }
                final byte[] encoded = encodedStr.getBytes(ASCII);
                if (encoded.length != 12) {
                    throw new InvalidIdException(encodedStr);
                }

                // The first byte should contain an ASCII character
                // representing the type of object whose ID is encoded.
                final byte prefix = encoded[0];
                final KamStoreObjectType represented =
                        KamStoreObjectType.fromRepresentation(prefix);
                if (represented == null) {
                    throw new InvalidIdException(encodedStr);
                } else if (expected != null && represented != expected) {
                    throw new InvalidIdException(encodedStr);
                }

                // Remove the prefix byte and add back the byte of Base64-encoded padding.
                shiftLeft(encoded);
                encoded[11] = (byte) 0x3d; // '=' in ASCII

                final byte[] decoded = decodeBase64(encoded);
                if (decoded.length != 8) {
                    throw new InvalidIdException(encodedStr);
                }

                ByteBuffer buffer = ByteBuffer.allocate(8).put(decoded);
                buffer.flip();
                final int kamInfoId = buffer.getInt();
                final int kamStoreObjId = buffer.getInt();

                return new KamStoreObjectRef(kamInfoId, kamStoreObjId,
                        represented.getRepresentedClass(), encodedStr);

            } catch (IndexOutOfBoundsException e) {
                throw new InvalidIdException(encodedStr);
            }
        }

        private static void shiftRight(final byte[] bytes) {
            for (int i = bytes.length - 1; i > 0; --i) {
                bytes[i] = bytes[i - 1];
            }
        }

        private static void shiftLeft(final byte[] bytes) {
            for (int i = 0; i < bytes.length - 1; ++i) {
                bytes[i] = bytes[i + 1];
            }
        }
    }

    private static enum KamStoreObjectType {
        ANNOTATION('M', KAMStoreDaoImpl.Annotation.class),
        ANNOTATION_TYPE('A', KAMStoreDaoImpl.AnnotationType.class),
        BEL_DOCUMENT_INFO('D', BelDocumentInfo.class),
        BEL_STATEMENT('S', KAMStoreDaoImpl.BelStatement.class),
        BEL_TERM('T', KAMStoreDaoImpl.BelTerm.class),
        KAM_EDGE(
                'E',
                org.openbel.framework.api.Kam.KamEdge.class),
        KAM_NODE(
                'N',
                org.openbel.framework.api.Kam.KamNode.class),
        KAM_PROTO_EDGE('R', KamProtoEdge.class),
        KAM_PROTO_NODE('V', KamProtoNode.class),
        TERM_PARAMETER('P', TermParameter.class),
        KAM_IMPL('K', KamImpl.class),
        KAM_INFO('I', KamInfo.class),
        NAMESPACE('Q', KAMStoreDaoImpl.Namespace.class),
        KAM_DIALECT('X', KamDialect.class);

        private static Map<Class<? extends KamStoreObject>, KamStoreObjectType> classesIndex;
        private static byte[] representations;
        private static int[] repIndex;
        static {
            final CharsetEncoder asciiEncoder = ASCII.newEncoder();

            final KamStoreObjectType[] types = values();
            final int n = types.length;

            classesIndex =
                    new HashMap<Class<? extends KamStoreObject>, KamStoreObjectType>(
                            n);
            representations = new byte[n];

            for (int i = 0; i < n; ++i) {
                final KamStoreObjectType type = types[i];

                classesIndex.put(type.clazz, type);

                type.repByte = representations[i] =
                        Converter.encode(asciiEncoder, type.rep).byteValue();
            }

            repIndex = sortAndCreateIndex(representations);
        }

        private final Class<? extends KamStoreObject> clazz;
        private final char rep;
        private byte repByte;

        private KamStoreObjectType(final char rep,
                final Class<? extends KamStoreObject> clazz) {
            this.clazz = clazz;
            this.rep = rep;
            this.repByte = 0;
        }

        public static KamStoreObjectType fromClass(
                Class<? extends KamStoreObject> clazz) {
            KamStoreObjectType ret = classesIndex.get(clazz);

            if (ret == null) {
                // check interfaces
                for (Class<?> i : clazz.getInterfaces()) {
                    if (KamStoreObject.class.isAssignableFrom(i)) {
                        ret = classesIndex.get(i);
                        break;
                    }
                }
            }

            if (ret == null) {
                throw new UnsupportedOperationException(String.format(
                        "Class '%s' does not have a corresponding %s!",
                        clazz.getName(),
                        KamStoreObjectType.class.getSimpleName()));
            }
            return ret;
        }

        public static KamStoreObjectType fromRepresentation(final byte rep) {
            int j = Arrays.binarySearch(representations, rep);
            return (j >= 0 ? values()[repIndex[j]] : null);
        }

        public byte getRepresentation() {
            return repByte;
        }

        public Class<? extends KamStoreObject> getRepresentedClass() {
            return clazz;
        }

        private static int[] sortAndCreateIndex(final byte[] bytes) {
            int n = bytes.length;
            int[] index = new int[n];
            for (int i = 0; i < n; ++i) {
                index[i] = i;
            }

            for (int i = 0; i < n - 1; ++i) {
                for (int j = i + 1; j < n; ++j) {
                    if (bytes[i] > bytes[j]) {
                        final byte swap = bytes[i];
                        final int k = index[i];
                        bytes[i] = bytes[j];
                        index[i] = index[j];
                        bytes[j] = swap;
                        index[j] = k;
                    }
                }
            }

            return index;
        }
    }

    private static Byte encode(final CharsetEncoder encoder, final char c) {
        encoder.reset();
        if (!encoder.canEncode(c)) {
            return null;
        }
        encoder.reset();
        ByteBuffer buffer = ByteBuffer.allocate(1);
        CharBuffer charBuffer = CharBuffer.allocate(1).put(c);
        charBuffer.flip();
        CoderResult result = null;

        result = encoder.encode(charBuffer, buffer, false);
        if (!result.isUnderflow()) {
            return null;
        }
        result = encoder.encode(charBuffer, buffer, true);
        if (result.isMalformed() || result.isUnmappable()) {
            return null;
        }
        result = encoder.flush(buffer);
        if (!result.isUnderflow()) {
            return null;
        }
        buffer.flip();
        return buffer.get(0);
    }
}
