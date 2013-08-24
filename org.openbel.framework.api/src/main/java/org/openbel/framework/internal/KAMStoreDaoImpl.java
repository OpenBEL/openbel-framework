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
package org.openbel.framework.internal;

import static java.util.Collections.emptyList;
import static java.sql.ResultSet.*;
import static org.openbel.framework.common.BELUtilities.noLength;
import static org.openbel.framework.common.BELUtilities.sizedHashMap;
import static org.openbel.framework.common.BELUtilities.sizedHashSet;
import static org.openbel.framework.common.BELUtilities.substringEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.openbel.framework.common.AnnotationDefinitionResolutionException;
import org.openbel.framework.common.BELUtilities;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.bel.parser.BELParser;
import org.openbel.framework.common.enums.CitationType;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.model.AnnotationDefinition;
import org.openbel.framework.common.model.CitationAuthorsAnnotationDefinition;
import org.openbel.framework.common.model.CitationCommentAnnotationDefinition;
import org.openbel.framework.common.model.CitationDateAnnotationDefinition;
import org.openbel.framework.common.model.CitationNameAnnotationDefinition;
import org.openbel.framework.common.model.CitationReferenceAnnotationDefinition;
import org.openbel.framework.common.model.CitationTypeAnnotationDefinition;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.common.util.PackUtils;
import org.openbel.framework.common.util.Pair;
import org.openbel.framework.core.df.AbstractJdbcDAO;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.core.df.encryption.EncryptionServiceException;
import org.openbel.framework.core.df.encryption.KamStoreEncryptionServiceImpl;
import org.openbel.framework.core.df.encryption.SymmetricEncryptionService;
import org.openbel.framework.core.df.external.CacheableAnnotationDefinitionService;
import org.openbel.framework.core.df.external.CacheableAnnotationDefinitionServiceImpl;
import org.openbel.framework.core.df.external.ExternalResourceException;
import org.openbel.framework.internal.KAMCatalogDao.AnnotationFilter;
import org.openbel.framework.internal.KAMCatalogDao.KamFilter;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.internal.KAMCatalogDao.NamespaceFilter;
import org.openbel.framework.api.AnnotationFilterCriteria;
import org.openbel.framework.api.BelDocumentFilterCriteria;
import org.openbel.framework.api.CitationFilterCriteria;
import org.openbel.framework.api.FilterCriteria;
import org.openbel.framework.api.Kam;
import org.openbel.framework.api.KamElementImpl;
import org.openbel.framework.api.KamStoreObjectImpl;
import org.openbel.framework.api.NamespaceFilterCriteria;
import org.openbel.framework.api.RelationshipTypeFilterCriteria;
import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;

/**
 *
 * @author Julian Ray {@code jray@selventa.com}
 *
 */
public final class KAMStoreDaoImpl extends AbstractJdbcDAO implements
        KAMStoreDao {

    private static final String SELECT_PROTO_NODES_SQL =
            "SELECT kn.kam_node_id, kn.function_type_id, kn.node_label_oid FROM @.kam_node kn";
    private static final String SELECT_PROTO_EDGES_SQL =
            "SELECT ke.kam_edge_id, ke.kam_source_node_id, ke.relationship_type_id, ke.kam_target_node_id FROM @.kam_edge ke";
    private static final String SELECT_OBJECTS_VALUE_SQL =
            "SELECT type_id, varchar_value, objects_text_id FROM @.objects WHERE objects_id = ?";
    private static final String SELECT_OBJECTS_TEXT_SQL =
            "SELECT text_value FROM @.objects_text WHERE objects_text_id = ?";
    private static final String SELECT_OBJECTS_ID_SQL =
            "SELECT objects_id from @.objects WHERE varchar_value = ?";
    private static final String SELECT_STATEMENTS_BY_EDGE_SQL =
            "select "
                    +
                    "s.statement_id, document_id, subject_term_id, relationship_type_id, object_term_id, nested_subject_id, nested_relationship_type_id, nested_object_id "
                    +
                    "FROM @.statement s, @.kam_edge_statement_map kesm WHERE s.statement_id = kesm.statement_id and kesm.kam_edge_id = ?";
    private static final String SELECT_TERM_BY_ID_SQL =
            "SELECT term_label_oid FROM @.term WHERE term_id = ?";
    private static final String SELECT_KAM_NODE_ID_BY_TERM_ID_SQL =
            "SELECT kam_node_id FROM @.term WHERE term_id = ?";
    private static final String SELECT_DOCUMENT_BY_ID_SQL =
            "SELECT document_id, name, description, version, copyright, disclaimer, contact_information, license_information, authors FROM @.document_header_information WHERE document_id = ?";
    private static final String SELECT_ANNOTATION_BY_ID_SQL =
            "SELECT value_oid, annotation_definition_id FROM @.annotation WHERE annotation_id = ?";
    private static final String SELECT_ANNOTATIONS_BY_STATEMENT_ID_SQL =
            "SELECT b.annotation_id FROM @.statement_annotation_map a LEFT JOIN @.annotation b ON a.annotation_id = b.annotation_id WHERE a.statement_id = ?";
    private static final String SELECT_ANNOTATION_TYPE_BY_ID_SQL =
            "SELECT annotation_definition_id, name, description, annotation_usage, annotation_definition_type_id FROM @.annotation_definition WHERE annotation_definition_id = ?";
    private static final String SELECT_ANNOTATION_TYPES_SQL =
            "SELECT annotation_definition_id, name, description, annotation_usage, annotation_definition_type_id FROM @.annotation_definition";
    private static final String SELECT_ANNOTATION_TYPES_BY_DOCUMENT_ID_SQL =
            "SELECT annotation_definition_id FROM @.document_annotation_def_map WHERE document_id = ?";
    private static final String SELECT_ANNOTATION_TYPE_DOMAIN_VALUE_SQL =
            "SELECT domain_value_oid, annotation_definition_type_id FROM @.annotation_definition WHERE annotation_definition_id = ?";
    private static final String SELECT_KAM_NODE_PARAMETERS_PREFIX_SQL =
            "SELECT knp.kam_node_id, knp.kam_global_parameter_id FROM @.kam_node_parameter knp";
    private static final String SELECT_KAM_NODE_PARAMETERS_ORDER_SQL =
            " ORDER BY knp.kam_node_id, knp.ordinal";

    private static final String SELECT_NAMESPACES_BY_DOCUMENT_ID_SQL =
            "SELECT namespace_id FROM @.document_namespace_map WHERE document_id = ?";
    private static final String SELECT_NAMESPACE_BY_PREFIX_SQL =
            "SELECT namespace_id, prefix, resource_location_oid FROM @.namespace WHERE prefix = ?";
    private static final String SELECT_DOCUMENTS_SQL =
            "SELECT document_id, name, description, version, copyright, disclaimer, contact_information, license_information, authors FROM @.document_header_information";
    private static final String SELECT_NAMESPACES_SQL =
            "SELECT namespace_id, prefix, resource_location_oid FROM @.namespace";
    private static final String SELECT_NAMESPACE_BY_ID_SQL =
            "SELECT namespace_id, prefix, resource_location_oid FROM @.namespace WHERE namespace_id = ?";
    private static final String SELECT_TERM_PARAMETERS_BY_TERM_ID_SQL =
            "SELECT term_parameter_id, namespace_id, parameter_value_oid FROM @.term_parameter WHERE term_id = ? ORDER BY ordinal";
    private static final String SELECT_KAM_NODE_IDS_FOR_PARAMETER_FUNCTION_SQL =
            "SELECT DISTINCT(k.kam_node_id) FROM @.kam_node k LEFT JOIN @.term t ON k.kam_node_id = t.kam_node_id LEFT JOIN @.term_parameter tp ON t.term_id = tp.term_id WHERE k.function_type_id = ? AND (tp.namespace_id = ? OR (tp.namespace_id IS NULL AND ? IS NULL)) AND tp.parameter_value_oid = ?";
    private static final String SELECT_KAM_NODE_IDS_FOR_PARAMETER_SQL =
            "SELECT DISTINCT(k.kam_node_id) FROM @.kam_node k LEFT JOIN @.term t ON k.kam_node_id = t.kam_node_id LEFT JOIN @.term_parameter tp ON t.term_id = tp.term_id WHERE (tp.namespace_id = ? OR (tp.namespace_id IS NULL AND ? IS NULL)) AND tp.parameter_value_oid = ?";
    private static final String SELECT_KAM_NODE_IDS_FOR_UUID_FUNCTION_SQL =
            "SELECT k.kam_node_id FROM @.kam_node k INNER JOIN @.kam_node_parameter p ON k.kam_node_id = p.kam_node_id INNER JOIN @.kam_parameter_uuid u ON p.kam_global_parameter_id = u.kam_global_parameter_id WHERE k.function_type_id = ? AND u.most_significant_bits = ? and u.least_significant_bits = ?";
    private static final String SELECT_KAM_NODE_IDS_FOR_UUID_SQL =
            "SELECT DISTINCT(p.kam_node_id) FROM @.kam_node_parameter p LEFT JOIN @.kam_parameter_uuid u ON p.kam_global_parameter_id = u.kam_global_parameter_id WHERE u.most_significant_bits = ? and u.least_significant_bits = ?";
    private static final String SELECT_STATEMENT_BY_ID_SQL =
            "SELECT statement_id, document_id, subject_term_id, relationship_type_id, object_term_id, nested_subject_id, nested_relationship_type_id, nested_object_id FROM @.statement WHERE statement_id = ?";
    private static final String SELECT_TERMS_IDS_BY_NODE_ID_SQL =
            "SELECT term_id FROM @.term WHERE kam_node_id = ?";
    private static final String SELECT_KAM_NODES_CONTAINING_KAM_NODE_PARAMETER_SQL =
            "SELECT DISTINCT(knp2.kam_node_id) FROM @.kam_node_parameter knp1, @.kam_node_parameter knp2 where knp1.kam_node_id = ? and knp1.kam_global_parameter_id = knp2.kam_global_parameter_id and knp2.kam_node_id != ?";
    private static final String SELECT_CITATION_ANNOTATIONS_SQL =
            "SELECT sam.annotation_id, sam.statement_id, s.document_id FROM @.statement s LEFT JOIN @.statement_annotation_map sam ON s.statement_id = sam.statement_id LEFT JOIN @.annotation a ON a.annotation_id = sam.annotation_id LEFT JOIN @.annotation_definition ad ON ad.annotation_definition_id = a.annotation_definition_id WHERE ad.name IN ('"
                    + StringUtils
                            .join(KAMStoreConstants.CITATION_ANNOTATION_DEFINITION_IDS,
                                    "', '") + "') order by sam.statement_id";
    private static final String SELECT_TERM_ID_BY_PARAMETERS_SQL =
            "SELECT term_id FROM @.term_parameter WHERE (namespace_id = ? OR (namespace_id IS NULL AND ? IS NULL)) AND parameter_value_oid = ? AND ordinal = ?";

    private static final Pattern NON_WORD_PATTERN = Pattern.compile("[\\W_]");
    private static final String ANY_NUMBER_PLACEHOLDER = "#";
    private static final int ANY_NUMBER_PLACEHOLDER_LENGTH =
            ANY_NUMBER_PLACEHOLDER.length();
    private static final Pattern NUMBER_REGEX_PATTERN = Pattern.compile("\\d+");
    private static final Pattern ANY_NUMBER_REGEX_PATTERN = Pattern
            .compile("\\d+|" + Pattern.quote(ANY_NUMBER_PLACEHOLDER));
    private static final String CITATION = "Citation";
    private static final Set<String> citationTypes = getCitationDefinitions();

    // Caches
    private Map<Integer, BelTerm> termCache =
            new ConcurrentHashMap<Integer, BelTerm>();
    private Map<Integer, String> objectValueCache =
            new ConcurrentHashMap<Integer, String>();
    private Map<String, Integer> objectValueReverseCache =
            new ConcurrentHashMap<String, Integer>();
    private Map<Integer, Namespace> namespaceCache =
            new ConcurrentHashMap<Integer, Namespace>();
    private Map<String, Namespace> namespacePrefixCache =
            new ConcurrentHashMap<String, Namespace>();
    private Map<Integer, AnnotationType> annotationTypeCache =
            new ConcurrentHashMap<Integer, AnnotationType>();
    private Map<Integer, List<BelStatement>> supportingEvidenceCache =
            new ConcurrentHashMap<Integer, List<BelStatement>>();
    private Map<Integer, List<BelTerm>> supportingTermCache =
            new ConcurrentHashMap<Integer, List<BelTerm>>();
    private Map<String, Integer> supportingTermLabelReverseCache =
            new ConcurrentHashMap<String, Integer>();
    private Map<Integer, Integer> kamNodeTermCache =
            new ConcurrentHashMap<Integer, Integer>();

    private Map<Integer, BelStatement> statementCache =
            new ConcurrentHashMap<Integer, BelStatement>();
    private Map<Integer, BelDocumentInfo> documentCache =
            new ConcurrentHashMap<Integer, BelDocumentInfo>();
    private Map<Integer, List<TermParameter>> termParameterCache =
            new ConcurrentHashMap<Integer, List<TermParameter>>();
    private Map<Integer, Annotation> annotationCache =
            new ConcurrentHashMap<Integer, KAMStoreDaoImpl.Annotation>();
    private Map<Integer, List<Annotation>> statementAnnotationCache =
            new ConcurrentHashMap<Integer, List<Annotation>>();
    private Map<Integer, List<String>> annotationTypeValueCache =
            new ConcurrentHashMap<Integer, List<String>>();
    private Map<Integer, List<Integer>> nodeExampleMatchCache =
            new ConcurrentHashMap<Integer, List<Integer>>();

    //citation caches
    private Map<String, Citation> citationMap = null;
    private Map<Integer, List<Citation>> belDocumentCitationsMap = null;

    private SymmetricEncryptionService encryptionService;

    private CacheableAnnotationDefinitionService cacheableAnnotationDefinitionService;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat(
            KAMStoreConstants.DATE_FORMAT);

    /**
     * Creates a KAMStoreDaoImpl from the Jdbc {@link Connection} that will
     * be used to load the KAM.
     *
     * @param dbc {@link Connection}, the database connection which should be
     * non-null and already open for sql execution.
     * @throws InvalidArgument - Thrown if {@code dbc} is null or the sql
     * connection is already closed.
     * @throws SQLException - Thrown if a sql error occurred while loading
     * the KAM.
     */
    public KAMStoreDaoImpl(String schemaName, DBConnection dbc)
            throws SQLException {
        super(dbc, schemaName);

        if (dbc == null) {
            throw new InvalidArgument("dbc is null");
        }

        if (dbc.getConnection().isClosed()) {
            throw new InvalidArgument("dbc is closed and cannot be used");
        }
        this.encryptionService = new KamStoreEncryptionServiceImpl();
        this.cacheableAnnotationDefinitionService =
                new CacheableAnnotationDefinitionServiceImpl();
    }

    @Override
    public List<BelDocumentInfo> getBelDocumentInfos() throws SQLException {
        List<BelDocumentInfo> list = new ArrayList<BelDocumentInfo>();

        ResultSet rset = null;

        try {
            PreparedStatement ps = getPreparedStatement(SELECT_DOCUMENTS_SQL);

            rset = ps.executeQuery();

            while (rset.next()) {
                BelDocumentInfo belDocumentInfo = getBelDocumentInfo(rset);
                list.add(belDocumentInfo);

                // This might save us time later on
                if (!documentCache.containsKey(belDocumentInfo.getId())) {
                    documentCache.put(belDocumentInfo.getId(), belDocumentInfo);
                }
            }
        } finally {
            close(rset);
        }

        return list;
    }

    @Override
    public List<Namespace> getNamespaces() throws SQLException {
        List<Namespace> list = new ArrayList<Namespace>();

        ResultSet rset = null;

        try {
            PreparedStatement ps = getPreparedStatement(SELECT_NAMESPACES_SQL);

            rset = ps.executeQuery();

            while (rset.next()) {
                Namespace namespace = getNamespace(rset);
                list.add(namespace);
                // This might save us time later on
                if (!namespaceCache.containsKey(namespace.getId())) {
                    namespaceCache.put(namespace.getId(), namespace);
                }
                if (!namespacePrefixCache.containsKey(namespace.getPrefix())) {
                    namespacePrefixCache.put(namespace.getPrefix(), namespace);
                }
            }
        } finally {
            close(rset);
        }

        return list;
    }

    @Override
    public BelStatement getBelStatement(Integer belStatementId)
            throws SQLException {

        // See if this statement is already cached
        if (statementCache.containsKey(belStatementId)) {
            return statementCache.get(belStatementId);
        }

        BelStatement belStatement = null;
        ResultSet rset = null;
        PreparedStatement ps = null;
        try {
            ps = getPreparedStatement(SELECT_STATEMENT_BY_ID_SQL);
            ps.setInt(1, belStatementId);

            rset = ps.executeQuery();

            if (rset.next()) {
                belStatement = getStatement(rset);
            }
        } finally {
            close(rset);
            close(ps);
        }

        // put this list into the evidence cache
        statementCache.put(belStatementId, belStatement);
        return belStatement;
    }

    @Override
    public List<BelStatement> getSupportingEvidence(KamEdge kamEdge)
            throws SQLException {
        return getSupportingEvidence(kamEdge.getId());
    }

    @Override
    public List<BelStatement> getSupportingEvidence(KamEdge kamEdge,
            AnnotationFilter filter) throws SQLException {
        final List<BelStatement> stmts = getSupportingEvidence(kamEdge);

        if (filter == null) {
            return stmts;
        }

        final List<FilterCriteria> criteria = filter.getFilterCriteria();
        final Map<AnnotationType, AnnotationFilterCriteria> amap =
                sizedHashMap(criteria.size());
        for (final FilterCriteria c : criteria) {
            final AnnotationFilterCriteria afc = (AnnotationFilterCriteria) c;
            amap.put(afc.getAnnotationType(), afc);
        }

        final Iterator<BelStatement> stmtIt = stmts.iterator();
        while (stmtIt.hasNext()) {
            final BelStatement stmt = stmtIt.next();

            final List<Annotation> annotations = stmt.getAnnotationList();

            for (final FilterCriteria c : criteria) {
                // criteria is invalid, continue
                if (c == null) {
                    continue;
                }

                final AnnotationFilterCriteria afc =
                        (AnnotationFilterCriteria) c;

                // criteria's annotation type is invalid, continue
                if (afc.getAnnotationType() == null) {
                    continue;
                }

                Annotation matchedAnnotation = null;
                for (final Annotation annotation : annotations) {
                    if (annotation.getAnnotationType() == afc
                            .getAnnotationType()) {
                        matchedAnnotation = annotation;
                    }
                }

                if (matchedAnnotation == null) {
                    if (c.isInclude()) {
                        stmtIt.remove();
                    }
                } else {
                    boolean valueMatch = afc.getValues().contains(
                            matchedAnnotation.getValue());
                    if (valueMatch && !c.isInclude()) {
                        stmtIt.remove();
                    }
                }
            }
        }

        return stmts;
    }

    /**
     *
     * @param kamEdgeId
     * @return
     * @throws SQLException
     */
    @Override
    public List<BelStatement> getSupportingEvidence(Integer kamEdgeId)
            throws SQLException {

        if (kamEdgeId == null) {
            throw new IllegalArgumentException("KAM edge ID cannot be null.");
        }
        // See if this evidence is already cached
        if (supportingEvidenceCache.containsKey(kamEdgeId)) {
            return supportingEvidenceCache.get(kamEdgeId);
        }

        List<BelStatement> list = new ArrayList<BelStatement>();

        ResultSet rset = null;

        try {
            PreparedStatement ps =
                    getPreparedStatement(SELECT_STATEMENTS_BY_EDGE_SQL);
            ps.setInt(1, kamEdgeId);

            rset = ps.executeQuery();

            while (rset.next()) {
                list.add(getStatement(rset));
            }
        } finally {
            close(rset);
        }

        // put this list into the evidence cache
        supportingEvidenceCache.put(kamEdgeId, list);
        return list;
    }

    @Override
    public List<BelTerm> getSupportingTerms(KamNode kamNode,
            NamespaceFilter namespaceFilter) throws SQLException {
        List<BelTerm> termList = getSupportingTerms(kamNode);

        if (namespaceFilter != null) {
            for (FilterCriteria criterion : namespaceFilter.getFilterCriteria()) {
                NamespaceFilterCriteria nfc =
                        (NamespaceFilterCriteria) criterion;
                Set<Integer> targetNamespaceIds = new HashSet<Integer>();
                for (Namespace ns : nfc.getValues()) {
                    targetNamespaceIds.add(ns.getId());
                }
                List<BelTerm> matchedTerms = new ArrayList<BelTerm>();
                for (BelTerm term : termList) {
                    List<TermParameter> params = getTermParameters(term);
                    for (TermParameter param : params) {
                        if (param.namespace != null
                                && targetNamespaceIds.contains(param.namespace
                                        .getId())) {
                            matchedTerms.add(term);
                            break;
                        }
                    }
                }
                if (criterion.isInclude()) {
                    termList.retainAll(matchedTerms);
                } else {
                    // must be exclude
                    termList.removeAll(matchedTerms);
                }

            }
        }
        return termList;
    }

    @Override
    public List<BelTerm> getSupportingTerms(KamNode kamNode)
            throws SQLException {
        return getSupportingTerms(kamNode.getId());
    }

    private List<BelTerm> getSupportingTerms(Integer kamNodeId)
            throws SQLException {

        // See if this evidence is already cached
        if (supportingTermCache.containsKey(kamNodeId)) {
            return supportingTermCache.get(kamNodeId);
        }

        List<BelTerm> list = new ArrayList<BelTerm>();

        ResultSet rset = null;

        try {
            PreparedStatement ps =
                    getPreparedStatement(SELECT_TERMS_IDS_BY_NODE_ID_SQL);
            ps.setInt(1, kamNodeId);

            rset = ps.executeQuery();

            while (rset.next()) {
                list.add(getBelTermById(rset.getInt(1)));
            }
        } finally {
            close(rset);
        }

        // put this list into the evidence cache
        supportingTermCache.put(kamNodeId, list);
        for (BelTerm belTerm : list) {
            supportingTermLabelReverseCache.put(belTerm.getLabel(), kamNodeId);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Integer getKamNodeId(String belTermString) throws SQLException {
        // See if the bel term is already mapped
        if (supportingTermLabelReverseCache.containsKey(belTermString)) {
            return supportingTermLabelReverseCache.get(belTermString);
        }

        // parse the BelTerm
        Term term;
        try {
            term = BELParser.parseTerm(belTermString);
        } catch (Exception e) {
            // invalid BEL
            return null;
        }

        Collection<Integer> possibleTermIds = null;
        int ordinal = 0;
        for (Parameter param : term.getAllParametersLeftToRight()) {
            Integer namespaceId = null;
            if (param.getNamespace() != null
                    && StringUtils.isNotBlank(param.getNamespace().getPrefix())) {
                Namespace ns =
                        getNamespaceByPrefix(param.getNamespace().getPrefix());
                if (ns != null) {
                    namespaceId = ns.getId();
                }
            }
            String paramValue = param.getValue();
            if (paramValue.startsWith("\"") && paramValue.endsWith("\"")) {
                paramValue = paramValue.substring(1, paramValue.length() - 1);
            }

            Integer valueOid = getObjectIdByValue(paramValue);
            if (valueOid == null) {
                // could not find label for param
                if (possibleTermIds != null) {
                    possibleTermIds.clear();
                }
                break;
            }

            ResultSet rset = null;
            try {
                PreparedStatement ps =
                        getPreparedStatement(SELECT_TERM_ID_BY_PARAMETERS_SQL);
                // set twice to handle is null, see http://stackoverflow.com/questions/4215135/
                if (namespaceId == null) {
                    ps.setNull(1, Types.INTEGER);
                    ps.setNull(2, Types.INTEGER);
                } else {
                    ps.setInt(1, namespaceId);
                    ps.setInt(2, namespaceId);
                }

                ps.setInt(3, valueOid);
                ps.setInt(4, ordinal);

                rset = ps.executeQuery();

                Set<Integer> termIdsWithParam = new HashSet<Integer>();
                while (rset.next()) {
                    termIdsWithParam.add(rset.getInt(1));
                }

                if (possibleTermIds == null) {
                    // first param
                    possibleTermIds = termIdsWithParam;
                } else {
                    possibleTermIds =
                            CollectionUtils.intersection(possibleTermIds,
                                    termIdsWithParam);
                }
            } finally {
                close(rset);
            }

            // no need to continue to next param if possibleTermIds is empty
            if (possibleTermIds.isEmpty()) {
                break;
            }
            ordinal++;
        }

        Integer kamNodeId = null;
        // iterate over all possible terms and check for label matches
        if (possibleTermIds != null) {
            for (Integer termId : possibleTermIds) {
                BelTerm belTerm = getBelTermById(termId);
                if (belTerm.getLabel().equals(belTermString)) {
                    kamNodeId = getKamNodeId(belTerm);
                    break;
                }
            }
        }

        if (kamNodeId != null) {
            supportingTermLabelReverseCache.put(belTermString, kamNodeId);
        }
        return kamNodeId;
    }

    /**
     *
     * @param belTerm
     * @return
     * @throws SQLException
     */
    @Override
    public Integer getKamNodeId(BelTerm belTerm) throws SQLException {
        // See if the bel term is already mapped
        if (kamNodeTermCache.containsKey(belTerm.getId())) {
            return kamNodeTermCache.get(belTerm.getId());
        }

        ResultSet rset = null;
        Integer kamNodeId = null;
        try {
            PreparedStatement ps =
                    getPreparedStatement(SELECT_KAM_NODE_ID_BY_TERM_ID_SQL);
            ps.setInt(1, belTerm.getId());
            rset = ps.executeQuery();

            if (rset.next()) {
                kamNodeId = rset.getInt(1);
            }
        } finally {
            close(rset);
        }
        // Add to the cache
        kamNodeTermCache.put(belTerm.getId(), kamNodeId);

        return kamNodeId;
    }

    /**
     *
     */
    @Override
    public List<TermParameter> getTermParameters(BelTerm belTerm)
            throws SQLException {
        return getTermParameters(belTerm.getId());
    }

    /**
     *
     * @param belTermId
     * @return
     * @throws SQLException
     */
    private List<TermParameter> getTermParameters(Integer belTermId)
            throws SQLException {
        // See if this evidence is already cached
        if (termParameterCache.containsKey(belTermId)) {
            return termParameterCache.get(belTermId);
        }

        List<TermParameter> list = new ArrayList<TermParameter>();
        ResultSet rset = null;

        try {
            PreparedStatement ps =
                    getPreparedStatement(SELECT_TERM_PARAMETERS_BY_TERM_ID_SQL);
            ps.setInt(1, belTermId);
            rset = ps.executeQuery();

            while (rset.next()) {
                Integer termParameterId = rset.getInt(1);
                Namespace namespace = getNamespaceById(rset.getInt(2));
                String parameterValue = getObjectValueById(rset.getInt(3));
                list.add(new TermParameter(termParameterId, namespace,
                        parameterValue));
            }
        } finally {
            close(rset);
        }

        // put this list into the evidence cache
        termParameterCache.put(belTermId, list);

        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getKamNodeCandidates(FunctionEnum functionType,
            Namespace namespace, String parameterValue) throws SQLException {
        // if function is null then delegate to overloaded sibling
        if (functionType == null) {
            return getKamNodeCandidates(namespace, parameterValue);
        }

        // guard against blank parameter value
        if (noLength(parameterValue)) {
            throw new InvalidArgument("parameterValue is blank");
        }

        final Integer objectId = getParameterObjectValueId(parameterValue);
        if (objectId == null) {
            // we couldn't find a string for parameterValue, so return no matches
            return new ArrayList<Integer>();
        }

        PreparedStatement ps =
                getPreparedStatement(SELECT_KAM_NODE_IDS_FOR_PARAMETER_FUNCTION_SQL);
        ps.setInt(1, functionType.getValue());

        // set namespace to search on, which may be null
        if (namespace == null || namespace.getId() == null) {
            ps.setNull(2, Types.INTEGER);
            ps.setNull(3, Types.INTEGER);
        } else {
            final int nid = namespace.getId();
            ps.setInt(2, nid);
            ps.setInt(3, nid);
        }

        ps.setInt(4, objectId);

        return queryForKamNodeCandidates(ps);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getKamNodeCandidates(Namespace namespace,
            String parameterValue) throws SQLException {
        // guard against blank parameter value
        if (noLength(parameterValue)) {
            throw new InvalidArgument("parameterValue is blank");
        }

        final Integer objectId = getParameterObjectValueId(parameterValue);
        if (objectId == null) {
            // we couldn't find a string for parameterValue, so return no matches
            return new ArrayList<Integer>();
        }

        PreparedStatement ps =
                getPreparedStatement(SELECT_KAM_NODE_IDS_FOR_PARAMETER_SQL);

        // set namespace to search on, which may be null
        if (namespace == null || namespace.getId() == null) {
            ps.setNull(1, Types.INTEGER);
            ps.setNull(2, Types.INTEGER);
        } else {
            final int nid = namespace.getId();
            ps.setInt(1, nid);
            ps.setInt(2, nid);
        }

        ps.setInt(3, objectId);

        return queryForKamNodeCandidates(ps);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getKamNodeCandidates(SkinnyUUID uuid)
            throws SQLException {
        if (uuid == null) {
            throw new InvalidArgument("uuid", uuid);
        }
        PreparedStatement ps =
                getPreparedStatement(SELECT_KAM_NODE_IDS_FOR_UUID_SQL);
        ps.setLong(1, uuid.getMostSignificantBits());
        ps.setLong(2, uuid.getLeastSignificantBits());

        return queryForKamNodeCandidates(ps);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getKamNodeCandidates(FunctionEnum functionType,
            SkinnyUUID uuid) throws SQLException {
        if (functionType == null) {
            return getKamNodeCandidates(uuid);
        }
        if (uuid == null) {
            throw new InvalidArgument("uuid", null);
        }
        PreparedStatement ps =
                getPreparedStatement(SELECT_KAM_NODE_IDS_FOR_UUID_FUNCTION_SQL);
        ps.setInt(1, functionType.getValue());
        ps.setLong(2, uuid.getMostSignificantBits());
        ps.setLong(3, uuid.getLeastSignificantBits());

        return queryForKamNodeCandidates(ps);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getKamNodeCandidates(KamNode example)
            throws SQLException {
        if (example == null || example.getId() == null) {
            throw new InvalidArgument("example", example);
        }

        List<Integer> kamNodeIds = nodeExampleMatchCache.get(example.getId());
        if (kamNodeIds != null) {
            return kamNodeIds;
        }

        PreparedStatement ps =
                getPreparedStatement(SELECT_KAM_NODES_CONTAINING_KAM_NODE_PARAMETER_SQL);
        ps.setInt(1, example.getId());
        ps.setInt(2, example.getId());

        kamNodeIds = queryForKamNodeCandidates(ps);
        nodeExampleMatchCache.put(example.getId(), kamNodeIds);

        return kamNodeIds;
    }

    private List<Integer> queryForKamNodeCandidates(final PreparedStatement ps)
            throws SQLException {
        List<Integer> kamNodeIdList = new ArrayList<Integer>();

        ResultSet rset = null;
        try {
            rset = ps.executeQuery();

            while (rset.next()) {
                Integer kamNodeId = rset.getInt(1);
                kamNodeIdList.add(kamNodeId);
            }
        } finally {
            close(rset);
        }

        return kamNodeIdList;
    }

    private Integer getParameterObjectValueId(String parameterValue)
            throws SQLException {

        // remove quoted parameter values, set to null if only ""
        if (parameterValue.startsWith("\"") && parameterValue.endsWith("\"")) {
            parameterValue = parameterValue.length() == 2 ? null :
                    parameterValue.substring(1, parameterValue.length() - 1);
        }

        final Integer objectId = getObjectIdByValue(parameterValue);
        return objectId;
    }

    /**
     *
     * @param belTermId
     * @return
     * @throws SQLException
     */
    private BelTerm getBelTermById(Integer belTermId) throws SQLException {

        // See if the term is cached
        if (termCache.containsKey(belTermId)) {
            return termCache.get(belTermId);
        }

        BelTerm belTerm = null;
        ResultSet rset = null;

        try {
            PreparedStatement ps = getPreparedStatement(SELECT_TERM_BY_ID_SQL);
            ps.setInt(1, belTermId);

            rset = ps.executeQuery();

            if (rset.next()) {
                String label = getObjectValueById(rset.getInt(1));
                // Get the term parameters for this term and reconstruct the
                // label based on its original encoding
                for (TermParameter termParameter : getTermParameters(belTermId)) {
                    String nsprefix =
                            termParameter.namespace != null ? termParameter.namespace.prefix
                                    : null;
                    String paramValue = termParameter.parameterValue;
                    Matcher m = NON_WORD_PATTERN.matcher(paramValue);
                    if (m.find()
                            && (!paramValue.startsWith("\"") && !paramValue
                                    .endsWith("\""))) {
                        // values must be quoted if there is a non-word character
                        paramValue = "\"" + paramValue + "\"";
                    }

                    final String termParam;
                    if (nsprefix != null) {
                        termParam = nsprefix + ":" + paramValue;
                    } else {
                        termParam = paramValue;
                    }
                    label = label.replaceFirst("#", termParam);
                }
                belTerm = new BelTerm(belTermId, label);
            }
        } finally {
            close(rset);
        }

        // Insert this term into the cache
        termCache.put(belTermId, belTerm);

        return belTerm;
    }

    /**
     *
     * @param namespaceId
     * @return
     * @throws SQLException
     */
    private Namespace getNamespaceById(Integer namespaceId) throws SQLException {

        // See if the term is cached
        if (namespaceCache.containsKey(namespaceId)) {
            return namespaceCache.get(namespaceId);
        }

        Namespace namespace = null;
        ResultSet rset = null;

        try {
            PreparedStatement ps =
                    getPreparedStatement(SELECT_NAMESPACE_BY_ID_SQL);
            ps.setInt(1, namespaceId);

            rset = ps.executeQuery();

            if (rset.next()) {
                namespace = getNamespace(rset);
            }
        } finally {
            close(rset);
        }

        // Insert this term into the cache
        if (namespace != null) {
            namespaceCache.put(namespaceId, namespace);
            namespacePrefixCache.put(namespace.getPrefix(), namespace);
        }

        return namespace;

    }

    /**
     *
     * @param belTermId
     * @return
     * @throws SQLException
     */
    private BelDocumentInfo getBelDocumentInfoById(Integer belDocumentId)
            throws SQLException {

        // See if the document is cached
        if (documentCache.containsKey(belDocumentId)) {
            return documentCache.get(belDocumentId);
        }

        ResultSet rset = null;
        BelDocumentInfo belDocumentInfo = null;

        try {
            PreparedStatement ps =
                    getPreparedStatement(SELECT_DOCUMENT_BY_ID_SQL);
            ps.setInt(1, belDocumentId);

            rset = ps.executeQuery();

            if (rset.next()) {
                belDocumentInfo = getBelDocumentInfo(rset);
            }
        } finally {
            close(rset);
        }

        // Insert this document into the cache
        documentCache.put(belDocumentId, belDocumentInfo);

        return belDocumentInfo;
    }

    /**
     *
     * @param belDocumentId
     * @return
     * @throws SQLException
     */
    private List<Namespace> getNamespacesByDocumentId(Integer belDocumentId)
            throws SQLException {

        ResultSet rset = null;
        List<Namespace> list = new ArrayList<Namespace>();

        try {
            PreparedStatement ps =
                    getPreparedStatement(SELECT_NAMESPACES_BY_DOCUMENT_ID_SQL);
            ps.setInt(1, belDocumentId);

            rset = ps.executeQuery();

            while (rset.next()) {
                Integer namespaceId = rset.getInt(1);
                list.add(getNamespaceById(namespaceId));
            }
        } finally {
            close(rset);
        }

        return list;
    }

    /**
     * Obtain a namespace by prefix
     * @param prefix
     * @return
     * @throws SQLException
     */
    private Namespace getNamespaceByPrefix(String prefix) throws SQLException {
        if (namespacePrefixCache.containsKey(prefix)) {
            return namespacePrefixCache.get(prefix);
        }

        ResultSet rset = null;
        Namespace ns = null;

        try {
            PreparedStatement ps =
                    getPreparedStatement(SELECT_NAMESPACE_BY_PREFIX_SQL);
            ps.setString(1, prefix);

            rset = ps.executeQuery();

            if (rset.next()) {
                ns = getNamespace(rset);
            }
        } finally {
            close(rset);
        }

        if (ns != null) {
            namespacePrefixCache.put(prefix, ns);
            namespaceCache.put(ns.getId(), ns);
        }

        return ns;
    }

    /**
     *
     * @param belTermId
     * @return
     * @throws SQLException
     */
    private List<AnnotationType> getAnnotationTypesByDocumentId(
            Integer belDocumentId) throws SQLException {
        ResultSet rset = null;
        List<AnnotationType> list = new ArrayList<AnnotationType>();

        try {
            PreparedStatement ps =
                    getPreparedStatement(SELECT_ANNOTATION_TYPES_BY_DOCUMENT_ID_SQL);
            ps.setInt(1, belDocumentId);

            rset = ps.executeQuery();

            while (rset.next()) {
                Integer annotationTypeId = rset.getInt(1);
                AnnotationType type = getAnnotationTypeById(annotationTypeId);

                addAnnotationType(list, type);
            }
        } finally {
            close(rset);
        }

        return list;
    }

    /**
     * Adds the {@link AnnotationType type} to the {@link List list} based on:
     * <ul>
     * <li>if {@link CitationNameAnnotationDefinition#ANNOTATION_DEFINITION_ID}
     * is the annotation then add a Citation {@link AnnotationType annotation type}</li>
     * <li>if another Citation field is the annotation then ignore it</li>
     * <li>any other annotation is added</li>
     * </ul>
     *
     * @param list the {@link List annotation type list}
     * @param type the {@link AnnotationType annotation type} to add
     */
    private void addAnnotationType(List<AnnotationType> list,
            AnnotationType type) {
        boolean isCitation = citationTypes.contains(type.getName());
        if (isCitation) {
            // add Citation is we have seen a CitationName annotation, throw away the others
            boolean isCitationName =
                    CitationNameAnnotationDefinition.ANNOTATION_DEFINITION_ID
                            .equals(type.getName());
            if (isCitationName) {
                list.add(createCitationType(type));
            }
        } else {
            list.add(type);
        }
    }

    /**
     *
     * @param statementId
     * @return
     * @throws SQLException
     */
    private List<Annotation> getAnnotationsByStatementId(Integer statementId)
            throws SQLException {

        // See if the annotation set for this statement is already cached
        if (statementAnnotationCache.containsKey(statementId)) {
            return statementAnnotationCache.get(statementId);
        }

        ResultSet rset = null;
        List<Annotation> list = new ArrayList<Annotation>();

        try {
            PreparedStatement ps =
                    getPreparedStatement(SELECT_ANNOTATIONS_BY_STATEMENT_ID_SQL);
            ps.setInt(1, statementId);

            rset = ps.executeQuery();

            while (rset.next()) {
                Integer annotationId = rset.getInt(1);
                list.add(getAnnotation(annotationId));
            }
        } finally {
            close(rset);
        }

        // Add this to the cache
        statementAnnotationCache.put(statementId, list);

        return list;
    }

    /**
     * Builds an SQL snippet to help select KAM edges that match <code>kamFilter</code>.
     *
     * The returned SQL snippet is a SELECT statement that queries the {@code kam_edge_id}'s of
     * the KAM edges that satisfy the provided KAM filter.  Callers may INNER JOIN with the snippet
     * to select more fields of the {@code kam_edge} table.
     *
     * @param kamFilter A selection of KAM filter criteria to apply in including or excluding edges.
     * <code>kamFilter</code> can not be null.
     * @return A pair of a SQL snippet and a list of all parameters to bind when using the
     * generated SQL in a PreparedStatement.
     */
    private Pair<String, List<String>> getFilteredSelectProtoEdgesSql(
            KamFilter kamFilter) {

        List<FilterCriteria> criteria = kamFilter.getFilterCriteria();

        // Create a StringBuilder for each part of the SQL query.
        StringBuilder baseQuery =
                new StringBuilder("SELECT ke.kam_edge_id FROM @.kam_edge ke");
        StringBuilder citationJoins = new StringBuilder();
        StringBuilder whereClause = new StringBuilder(" WHERE TRUE");
        StringBuilder havingClause = new StringBuilder();

        boolean joinedStatements = false, joinedAnnotations = false, groupedByEdge =
                false;

        // Create lists to contain the Strings that will need to
        // be bound in the PreparedStatement
        ArrayList<String> annotationParameters = new ArrayList<String>(), citationParameters =
                new ArrayList<String>();

        int uniqueSubselectId = 0;

        // Matching some of the types of FilterCriteria require an SQL aggregate function
        // that computes boolean AND or OR over a group.
        // Use MAX(CASE WHEN some_boolean THEN 1 ELSE 0 END)=1 for a boolean OR aggregate function.
        // Use MIN(CASE WHEN some_boolean THEN 1 ELSE 0 END)=1 for a boolean AND aggregate function.

        for (FilterCriteria criterion : criteria) {
            boolean include = criterion.isInclude();

            // The filter criteria are ANDed together.

            if (criterion instanceof RelationshipTypeFilterCriteria) {
                Set<RelationshipType> relationships =
                        ((RelationshipTypeFilterCriteria) criterion)
                                .getValues();
                int size = relationships.size();
                if (size == 0) {
                    // There is nothing on which to match.
                    continue;
                }

                whereClause.append(" AND ");

                // An include filter matches the edges that have at least one of the provided
                // relationship types (i.e. the tests of equality of the edge relationship type to each
                // of the provided relationship types are ORed).
                // An exclude filter matches the edges that have none of the provided relationship types
                // (i.e. the tests of equality of the edge relationship type to each of the provided
                // relationship types are ORed, then finally complemented (NOT)).
                if (!include) {
                    whereClause.append("NOT ");
                }

                whereClause.append("(");
                int count = 0;
                for (RelationshipType relationship : relationships) {
                    whereClause.append("ke.relationship_type_id=");
                    whereClause.append(relationship.getValue());
                    if (++count < size) {
                        whereClause.append(" OR ");
                    }
                }
                whereClause.append(")");

            } else if (criterion instanceof BelDocumentFilterCriteria) {
                Set<BelDocumentInfo> documents =
                        ((BelDocumentFilterCriteria) criterion).getValues();
                int size = documents.size();
                if (size == 0) {
                    // There is nothing on which to match.
                    continue;
                }

                // The provided documents are matched against the documents associated with the
                // supporting evidence for the KAM edges, that is the statements that are associated
                // with the edge.   This filter requires joining with the <code>statement</code> table.
                if (!joinedStatements) {
                    baseQuery
                            .append(" LEFT OUTER JOIN @.kam_edge_statement_map kesm ON ke.kam_edge_id=kesm.kam_edge_id");
                    baseQuery
                            .append(" LEFT OUTER JOIN @.statement s ON kesm.statement_id=s.statement_id");
                    joinedStatements = true;
                }

                // An include filter matches only the edges that have supporting evidence statements that
                // have a document that is one of the provided documents.  A predicate expression
                // for "being one of the provided documents" must be evaluated for all documents
                // of supporting statements.  If that predicate is true for any document then the edge
                // passes the filter (i.e. a boolean OR).
                // An exclude filter matches only the edges all of whose supporting evidence statements do
                // not have documents that are one of the provided documents.  The predicate expression
                // of "being one of the provided documents" must be false for all documents of all
                // supporting statements for an edge to pass an "exclude" filter (i.e. NOT OR).

                // The query will need to group by edge and use a HAVING clause to match the predicate.
                if (!groupedByEdge) {
                    havingClause = new StringBuilder(" HAVING TRUE");
                    groupedByEdge = true;
                }

                havingClause.append(" AND");
                if (!include) {
                    havingClause.append(" NOT");
                }
                havingClause.append(" MAX(CASE WHEN (");
                int count = 0;
                for (BelDocumentInfo doc : documents) {
                    havingClause.append("s.document_id=");
                    havingClause.append(doc.getId());
                    if (++count < size) {
                        havingClause.append(" OR ");
                    }
                }
                havingClause.append(") THEN 1 ELSE 0 END)=1");

            } else if (criterion instanceof AnnotationFilterCriteria) {
                AnnotationFilterCriteria ac =
                        (AnnotationFilterCriteria) criterion;
                Integer type = ac.getAnnotationType().getId();
                Set<String> annotations = ac.getValues();
                int size = annotations.size();
                if (size == 0) {
                    continue;
                }

                // This case is very similar to the BelDocumentFilterCriteria case, except
                // that the match is performed on non-citation annotations.

                // The query will need to join on the <code>statement</code> table.
                if (!joinedStatements) {
                    baseQuery
                            .append(" LEFT OUTER JOIN @.kam_edge_statement_map kesm ON ke.kam_edge_id=kesm.kam_edge_id");
                    baseQuery
                            .append(" LEFT OUTER JOIN @.statement s ON kesm.statement_id=s.statement_id");
                    joinedStatements = true;
                }

                // The query will need to group by edge.
                if (!groupedByEdge) {
                    havingClause = new StringBuilder(" HAVING TRUE");
                    groupedByEdge = true;
                }

                // The query will also need to join the <code>annotations</code> table.
                if (!joinedAnnotations) {
                    baseQuery
                            .append(" LEFT OUTER JOIN @.statement_annotation_map sam ON s.statement_id=sam.statement_id");
                    baseQuery
                            .append(" LEFT OUTER JOIN @.annotation a ON sam.annotation_id=a.annotation_id");
                    baseQuery
                            .append(" LEFT OUTER JOIN @.annotation_definition ad ON a.annotation_definition_id=ad.annotation_definition_id");
                    baseQuery
                            .append(" LEFT OUTER JOIN @.objects o ON a.value_oid=o.objects_id");
                    baseQuery
                            .append(" LEFT OUTER JOIN @.objects_text ot ON o.objects_text_id=ot.objects_text_id");
                    joinedAnnotations = true;
                }

                havingClause.append(" AND");
                if (!include) {
                    havingClause.append(" NOT");
                }
                havingClause.append(" MAX(CASE WHEN (");
                int count = 0;
                for (String annotation : annotations) {
                    String encryptedValue = null;
                    try {
                        // The annotation values are store encrypted in the database, so the match
                        // must be done on the encrypted values.
                        encryptedValue = encryptionService.encrypt(annotation);
                    } catch (EncryptionServiceException ex) {
                        continue; // TODO
                    }

                    annotationParameters.add(encryptedValue);
                    havingClause.append("(a.annotation_definition_id=");
                    havingClause.append(type);
                    havingClause
                            .append(" AND ((o.varchar_value IS NOT NULL AND o.varchar_value=?)");
                    havingClause.append(" OR (o.varchar_value IS NULL AND ");
                    if (dbConnection.isDerby()) {
                        // Apache Derby does not support comparing CLOBs so cast
                        // to the largest VARCHAR type for the comparison.
                        havingClause
                                .append("CAST(ot.text_value AS VARCHAR(32672))");
                    } else {
                        havingClause.append("ot.text_value");
                    }
                    havingClause.append("=?)))");

                    if (++count < size) {
                        havingClause.append(" OR ");
                    }
                }
                havingClause.append(") THEN 1 ELSE 0 END)=1");

            } else if (criterion instanceof CitationFilterCriteria) {
                Set<Citation> citations =
                        ((CitationFilterCriteria) criterion).getValues();
                int size = citations.size();
                if (size == 0) {
                    continue;
                }

                // This case is similar to the AnnotationFilterCriteria, except that the annotations
                // used in the match are the predefined citation annotations
                // (authors, comment, date, name, reference, and type) and the predicate
                // to determine whether the citation of a supporting evidence statement
                // matches a provided citation is computed in a subselect.

                // The query will need to join on the <code>statement</code> table.
                if (!joinedStatements) {
                    baseQuery
                            .append(" LEFT OUTER JOIN @.kam_edge_statement_map kesm ON ke.kam_edge_id=kesm.kam_edge_id");
                    baseQuery
                            .append(" LEFT OUTER JOIN @.statement s ON kesm.statement_id=s.statement_id");
                    joinedStatements = true;
                }

                whereClause.append(" AND");

                // An include filter matches an edge only if one of the supporting statements has one
                // of the provided citations (i.e. boolean OR).
                // An exclude filter matches an edge only if none of the supporting statements has one
                // of the provided citations (i.e. NOT OR).
                if (!include) {
                    whereClause.append(" NOT");
                }
                whereClause.append(" (FALSE");

                for (Citation citation : citations) {
                    String encryptedReference = null, encryptedDate = null, encryptedName =
                            null, encryptedComment = null, encryptedType = null, encryptedAuthors =
                            null;
                    try {
                        // The citation annotation values are store encrypted in the database, so the match
                        // must be done on the encrypted values.
                        final String id = citation.getId();
                        encryptedReference =
                                (id != null ? encryptionService.encrypt(id)
                                        : null);
                        final String name = citation.getName();
                        encryptedName =
                                (name != null ? encryptionService.encrypt(name)
                                        : null);
                        final String comment = citation.getComment();
                        encryptedComment =
                                (comment != null ? encryptionService
                                        .encrypt(comment) : null);
                        final CitationType citationType =
                                citation.getCitationType();
                        encryptedType =
                                (citationType != null ?
                                        encryptionService.encrypt(citationType
                                                .getDisplayValue()) : null);

                        // Pack the authors string exactly as done in
                        // CitationDataConverter.convert(Citation, Map<String, BELAnnotationDefinition>).
                        final List<String> authors = citation.getAuthors();
                        if (BELUtilities.hasItems(authors)) {
                            encryptedAuthors =
                                    encryptionService
                                            .encrypt(
                                            StringUtils.left(PackUtils
                                                    .packValues(authors), 4000));
                        }

                        final Date date = citation.getPublicationDate();
                        encryptedDate =
                                (date != null ? encryptionService
                                        .encrypt(dateFormat.format(date))
                                        : null);
                    } catch (EncryptionServiceException ex) {
                        continue; // TODO
                    }

                    citationJoins.append(" LEFT OUTER JOIN (");
                    citationJoins
                            .append("SELECT s.statement_id statement_id, SUM(CASE WHEN (FALSE");

                    int countNulls = 0;
                    for (String type : KAMStoreConstants.CITATION_ANNOTATION_DEFINITION_IDS) {
                        String value = null;
                        if (type == CitationAuthorsAnnotationDefinition.ANNOTATION_DEFINITION_ID) {
                            value = encryptedAuthors;
                        } else if (type == CitationDateAnnotationDefinition.ANNOTATION_DEFINITION_ID) {
                            value = encryptedDate;
                        } else if (type == CitationNameAnnotationDefinition.ANNOTATION_DEFINITION_ID) {
                            value = encryptedName;
                        } else if (type == CitationTypeAnnotationDefinition.ANNOTATION_DEFINITION_ID) {
                            value = encryptedType;
                        } else if (type == CitationCommentAnnotationDefinition.ANNOTATION_DEFINITION_ID) {
                            value = encryptedComment;
                        } else if (type == CitationReferenceAnnotationDefinition.ANNOTATION_DEFINITION_ID) {
                            value = encryptedReference;
                        }

                        if (noLength(value)) {
                            // For example the citation date can be null, in which case the
                            // supporting statements are not checked for the citation date
                            // annotation.
                            ++countNulls;
                            continue;
                        }

                        citationParameters.add(value);

                        citationJoins.append(" OR (ad.name='");
                        citationJoins.append(type);
                        citationJoins
                                .append("' AND ((o.varchar_value IS NOT NULL AND o.varchar_value=?)");
                        citationJoins
                                .append(" OR (o.varchar_value IS NULL AND ");
                        if (dbConnection.isDerby()) {
                            // Apache Derby does not support comparing CLOBs so cast
                            // to the largest VARCHAR type for the comparison.
                            // https://db.apache.org/derby/docs/10.7/ref/rrefjdbc96386.html
                            citationJoins
                                    .append("CAST(ot.text_value AS VARCHAR(32672))");
                        } else {
                            citationJoins.append("ot.text_value");
                        }
                        citationJoins.append("=?)))");
                    }

                    citationJoins.append(") THEN 1 ELSE 0 END) citations");
                    citationJoins.append(" FROM @.statement s");
                    citationJoins
                            .append(" LEFT OUTER JOIN @.statement_annotation_map sam ON s.statement_id=sam.statement_id");
                    citationJoins
                            .append(" LEFT OUTER JOIN @.annotation a ON sam.annotation_id=a.annotation_id");
                    citationJoins
                            .append(" LEFT OUTER JOIN @.annotation_definition ad ON a.annotation_definition_id=ad.annotation_definition_id");
                    citationJoins
                            .append(" LEFT OUTER JOIN @.objects o ON a.value_oid=o.objects_id");
                    citationJoins
                            .append(" LEFT OUTER JOIN @.objects_text ot ON o.objects_text_id=ot.objects_text_id");
                    citationJoins.append(" GROUP BY s.statement_id) t");
                    citationJoins.append(uniqueSubselectId);
                    citationJoins.append(" ON s.statement_id=t");
                    citationJoins.append(uniqueSubselectId);
                    citationJoins.append(".statement_id");

                    // The subselect above stores whether the supporting statements of each edge have the
                    // provided citation in the "has_citation" field.
                    whereClause.append(" OR (t");
                    whereClause.append(uniqueSubselectId);
                    whereClause.append(".citations<>0 AND MOD(t");
                    whereClause.append(uniqueSubselectId);
                    whereClause.append(".citations,");
                    whereClause
                            .append(KAMStoreConstants.CITATION_ANNOTATION_DEFINITION_IDS.length
                                    - countNulls);
                    whereClause.append(")=0)");

                    ++uniqueSubselectId;
                }

                whereClause.append(")");
            }
        }

        // Prepare the final SQL query.
        baseQuery.append(citationJoins);
        baseQuery.append(whereClause);
        if (groupedByEdge) {
            baseQuery.append(" GROUP BY ke.kam_edge_id");
            baseQuery.append(havingClause);
        }

        // Create a pair of the SQL query string and a list of the string parameters that
        // need to be bound, in order, in any PreparedStatement that uses the query string.
        final String sql = baseQuery.toString();
        final int size =
                2 * (citationParameters.size() + annotationParameters.size());
        final List<String> bindings = new ArrayList<String>(size);
        for (String param : citationParameters) {
            bindings.add(param);
            bindings.add(param);
        }
        for (String param : annotationParameters) {
            bindings.add(param);
            bindings.add(param);
        }

        return new Pair<String, List<String>>(sql, bindings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamProtoNodesAndEdges getKamProtoNodesAndEdges(KamInfo kamInfo)
            throws SQLException {

        Map<Integer, KamProtoNode> nodes = new HashMap<Integer, KamProtoNode>();
        Map<Integer, KamProtoEdge> edges = new HashMap<Integer, KamProtoEdge>();
        ResultSet nodesRs = null;
        ResultSet paramsRs = null;

        try {
            PreparedStatement nps =
                    getPreparedStatement(SELECT_PROTO_NODES_SQL);
            nodesRs = nps.executeQuery();

            PreparedStatement pps = getPreparedStatement(
                    SELECT_KAM_NODE_PARAMETERS_PREFIX_SQL
                            + SELECT_KAM_NODE_PARAMETERS_ORDER_SQL,
                    TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY);
            paramsRs = pps.executeQuery();

            while (nodesRs.next()) {
                KamProtoNode kamProtoNode = getKamProtoNode(nodesRs, paramsRs);
                nodes.put(kamProtoNode.getId(), kamProtoNode);
            }
        } finally {
            close(nodesRs);
            close(paramsRs);
        }

        try {
            PreparedStatement ps = getPreparedStatement(SELECT_PROTO_EDGES_SQL);
            nodesRs = ps.executeQuery();

            while (nodesRs.next()) {
                Integer kamEdgeId = nodesRs.getInt(1);
                KamProtoNode sourceKamProtoNode = nodes.get(nodesRs.getInt(2));
                Integer relationshipTypeId = nodesRs.getInt(3);
                KamProtoNode targetKamProtoNode = nodes.get(nodesRs.getInt(4));

                // Sanity checks
                if (null == sourceKamProtoNode) {
                    throw new SQLException(String.format(
                            "Source node for edge %d is missing.", kamEdgeId));
                }
                if (null == targetKamProtoNode) {
                    throw new SQLException(String.format(
                            "Target node for edge %d is missing.", kamEdgeId));
                }
                edges.put(kamEdgeId,
                        new KamProtoEdge(kamEdgeId, sourceKamProtoNode,
                                RelationshipType.fromValue(relationshipTypeId),
                                targetKamProtoNode));
            }
        } finally {
            close(nodesRs);
            close(paramsRs);
        }

        return new KamProtoNodesAndEdges(nodes, edges);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KamProtoNodesAndEdges getKamProtoNodesAndEdges(KamInfo kamInfo,
            KamFilter kamFilter) throws SQLException {

        if (kamFilter == null) {
            // The result will be the same as the result of continuing in this method,
            // but getKamProtoNodesAndEdges(KamInfo) can better handle the query.
            return getKamProtoNodesAndEdges(kamInfo);
        }

        Map<Integer, KamProtoNode> nodes = new HashMap<Integer, KamProtoNode>();
        Map<Integer, KamProtoEdge> edges = new HashMap<Integer, KamProtoEdge>();
        ResultSet nodesRs = null;
        ResultSet paramsRs = null;

        // Get an SQL snippet to help query the KAM nodes and edges.
        final Pair<String, List<String>> pair =
                getFilteredSelectProtoEdgesSql(kamFilter);
        final String snippet = pair.getFirst();
        final List<String> bindings = pair.getSecond();

        // Query the KAM nodes that are either a source or target node of
        // a KAM edge that satisfies the KAM filter.
        StringBuilder nodesSql = new StringBuilder(SELECT_PROTO_NODES_SQL);
        nodesSql.append(
                " INNER JOIN (SELECT ke.kam_source_node_id, ke.kam_target_node_id FROM @.kam_edge ke INNER JOIN (")
                .append(snippet)
                .append(") fke ON ke.kam_edge_id=fke.kam_edge_id")
                .append(") ke ON kn.kam_node_id=ke.kam_source_node_id OR kn.kam_node_id=ke.kam_target_node_id");

        // Same for global parameters
        StringBuilder paramSql =
                new StringBuilder(SELECT_KAM_NODE_PARAMETERS_PREFIX_SQL)
                        .append(" INNER JOIN (SELECT ke.kam_source_node_id, ke.kam_target_node_id FROM @.kam_edge ke INNER JOIN (")
                        .append(snippet)
                        .append(") fke ON ke.kam_edge_id=fke.kam_edge_id")
                        .append(") ke ON knp.kam_node_id=ke.kam_source_node_id OR knp.kam_node_id=ke.kam_target_node_id")
                        .append(SELECT_KAM_NODE_PARAMETERS_ORDER_SQL);

        try {
            PreparedStatement nps = getPreparedStatement(nodesSql.toString());
            int count = 0;
            for (String param : bindings) {
                nps.setString(++count, param);
            }
            nodesRs = nps.executeQuery();

            PreparedStatement pps = getPreparedStatement(paramSql.toString(),
                    TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY);
            count = 0;
            for (String param : bindings) {
                pps.setString(++count, param);
            }
            paramsRs = pps.executeQuery();

            while (nodesRs.next()) {
                KamProtoNode kamProtoNode = getKamProtoNode(nodesRs, paramsRs);
                nodes.put(kamProtoNode.getId(), kamProtoNode);
            }
        } finally {
            close(nodesRs);
        }

        // Then query the KAM edges.
        StringBuilder sql2 = new StringBuilder(SELECT_PROTO_EDGES_SQL);
        sql2.append(" INNER JOIN (")
                .append(snippet)
                .append(") fke ON ke.kam_edge_id=fke.kam_edge_id");
        try {
            PreparedStatement ps = getPreparedStatement(sql2.toString());
            int count = 0;
            for (String param : bindings) {
                ps.setString(++count, param);
            }
            nodesRs = ps.executeQuery();

            while (nodesRs.next()) {
                Integer kamEdgeId = nodesRs.getInt(1);
                KamProtoNode sourceKamProtoNode = nodes.get(nodesRs.getInt(2));
                Integer relationshipTypeId = nodesRs.getInt(3);
                KamProtoNode targetKamProtoNode = nodes.get(nodesRs.getInt(4));

                // Sanity checks
                if (null == sourceKamProtoNode) {
                    throw new SQLException(String.format(
                            "Source node for edge %d is missing.", kamEdgeId));
                }
                if (null == targetKamProtoNode) {
                    throw new SQLException(String.format(
                            "Target node for edge %d is missing.", kamEdgeId));
                }
                edges.put(kamEdgeId,
                        new KamProtoEdge(kamEdgeId, sourceKamProtoNode,
                                RelationshipType.fromValue(relationshipTypeId),
                                targetKamProtoNode));
            }
        } finally {
            close(nodesRs);
        }

        return new KamProtoNodesAndEdges(nodes, edges);
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    protected Collection<Citation> getAllCitations() throws SQLException {

        ResultSet rset = null;
        Map<String, Citation> citations = new HashMap<String, Citation>();

        try {
            PreparedStatement ps =
                    getPreparedStatement(SELECT_CITATION_ANNOTATIONS_SQL);
            rset = ps.executeQuery();
            List<Annotation> annotations = new ArrayList<Annotation>();
            Integer lastStatementId = null;
            //annotations ordered by statement ID
            while (rset.next()) {
                Integer statementId = rset.getInt(4);
                //Integer documentId = rset.getInt(5);
                if (lastStatementId == null) {
                    lastStatementId = statementId;
                }
                if (!lastStatementId.equals(statementId)) {
                    //new citation
                    Citation citation = new Citation(annotations);
                    String key = makeCitationKey(citation);
                    if (!citations.containsKey(key)) {
                        citations.put(key, citation);
                    }
                    annotations.clear();
                }
                annotations.add(getAnnotation(rset));
                lastStatementId = statementId;
            }
            Citation citation = new Citation(annotations);
            String key = makeCitationKey(citation);
            citations.put(key, citation);
        } finally {
            close(rset);
        }

        return citations.values();
    }

    //temporary key for determining citation equivalence
    private String makeCitationKey(Citation citation) {
        return citation.getName() + "," + citation.getId() + ","
                + citation.getCitationType();
    }

    private synchronized void initializeCitationMaps() throws SQLException {
        if (belDocumentCitationsMap == null || citationMap == null) {
            belDocumentCitationsMap = new HashMap<Integer, List<Citation>>();
            citationMap = new HashMap<String, Citation>();

            ResultSet rset = null;
            Map<String, Citation> citations = new HashMap<String, Citation>();

            try {
                PreparedStatement ps =
                        getPreparedStatement(SELECT_CITATION_ANNOTATIONS_SQL);
                rset = ps.executeQuery();
                List<Annotation> annotations = new ArrayList<Annotation>();
                Integer lastStatementId = 1;
                Integer lastDocumentId = null;
                //annotations ordered by statement ID
                while (rset.next()) {
                    Integer annotationId = rset.getInt(1);
                    Integer statementId = rset.getInt(2);
                    Integer documentId = rset.getInt(3);

                    if (!lastStatementId.equals(statementId)) {
                        //new citation
                        Citation citation = new Citation(annotations);
                        String key = makeCitationKey(citation);
                        if (!citations.containsKey(key)) {
                            citations.put(key, citation);
                            processCitationMapEntry(citation, documentId);
                        }
                        annotations.clear();
                    }
                    annotations.add(getAnnotation(annotationId));
                    lastStatementId = statementId;
                    lastDocumentId = documentId;
                }
                Citation citation = new Citation(annotations);
                String key = makeCitationKey(citation);
                if (!citations.containsKey(key)) {
                    citations.put(key, citation);
                    processCitationMapEntry(citation, lastDocumentId);
                }

            } finally {
                close(rset);
            }
        }
    }

    private void processCitationMapEntry(Citation citation, Integer documentId) {
        citationMap.put(citation.getId(), citation);
        List<Citation> documentCitations =
                belDocumentCitationsMap.get(documentId);
        if (documentCitations == null) {
            documentCitations = new ArrayList<Citation>();
            belDocumentCitationsMap.put(documentId, documentCitations);
        }
        documentCitations.add(citation);
    }

    @Override
    public List<Citation> getCitations(BelDocumentInfo belDocumentInfo)
            throws SQLException {
        List<Citation> citations = new ArrayList<Citation>();
        if (belDocumentCitationsMap == null) {
            initializeCitationMaps();
        }
        List<Citation> matches =
                belDocumentCitationsMap.get(belDocumentInfo.getId());
        if (matches != null) {
            citations.addAll(matches);
        }
        return citations;
    }

    @Override
    public List<Citation> getCitations(BelDocumentInfo belDocumentInfo,
            CitationType citationType) throws SQLException {
        List<Citation> citations = new ArrayList<Citation>();
        for (Citation citation : getCitations(belDocumentInfo)) {
            if (citation.getCitationType() == citationType) {
                citations.add(citation);
            }
        }
        return citations;
    }

    @Override
    public List<Citation> getCitations(CitationType citationType)
            throws SQLException {
        if (citationMap == null) {
            initializeCitationMaps();
        }
        List<Citation> citations = new ArrayList<Citation>();
        for (Citation citation : citationMap.values()) {
            if (citation.getCitationType() == citationType) {
                citations.add(citation);
            }
        }
        return citations;
    }

    @Override
    public List<Citation> getCitations(CitationType citationType,
            String... referenceIds) throws SQLException {
        if (referenceIds == null) {
            return emptyList();
        }
        if (citationMap == null) {
            initializeCitationMaps();
        }
        List<Citation> citations = new ArrayList<Citation>();
        for (int i = 0; i < referenceIds.length; i++) {
            Citation citation = citationMap.get(referenceIds[i]);
            if (citation != null && citation.getCitationType() == citationType) {
                citations.add(citation);
            }
        }
        return citations;
    }

    /**
     *
     * @param annotationTypeId
     * @return
     * @throws SQLException
     */
    private AnnotationType getAnnotationTypeById(Integer annotationTypeId)
            throws SQLException {
        // See if the term is cached
        if (annotationTypeCache.containsKey(annotationTypeId)) {
            return annotationTypeCache.get(annotationTypeId);
        }

        AnnotationType annotationType = null;
        ResultSet rset = null;

        try {
            PreparedStatement ps =
                    getPreparedStatement(SELECT_ANNOTATION_TYPE_BY_ID_SQL);
            ps.setInt(1, annotationTypeId);
            rset = ps.executeQuery();

            if (rset.next()) {
                annotationType = getAnnotationType(rset);
            }
        } finally {
            close(rset);
        }

        annotationTypeCache.put(annotationTypeId, annotationType);

        return annotationType;
    }

    @Override
    public List<String> getAnnotationTypeDomainValues(
            AnnotationType annotationType) throws SQLException,
            ExternalResourceException {
        if (annotationType == null) {
            throw new InvalidArgument("annotationType", annotationType);
        }

        if (CITATION.equals(annotationType.getName())) {
            return Arrays.asList(".*");
        }

        if (annotationTypeValueCache.containsKey(annotationType.getId())) {
            return annotationTypeValueCache.get(annotationType.getId());
        }

        List<String> domainValues = new ArrayList<String>();
        ResultSet rset = null;

        try {
            PreparedStatement ps =
                    getPreparedStatement(SELECT_ANNOTATION_TYPE_DOMAIN_VALUE_SQL);
            ps.setInt(1, annotationType.getId());
            rset = ps.executeQuery();

            if (rset.next()) {
                String value = getObjectValueById(rset.getInt(1));
                switch (annotationType.getAnnotationDefinitionType()) {
                case ENUMERATION:
                    for (String domainValue : StringUtils.split(value, "|")) {
                        domainValues.add(domainValue);
                    }
                    break;
                case REGULAR_EXPRESSION:
                    domainValues.add(value);
                    break;
                case URL:
                    //retrieve external resource
                    try {
                        AnnotationDefinition ad =
                                cacheableAnnotationDefinitionService
                                        .resolveAnnotationDefinition(value);
                        if (BELUtilities.hasItems(ad.getEnums())) {
                            //external enumerations
                            domainValues.addAll(ad.getEnums());
                        } else {
                            //pattern
                            domainValues.add(ad.getValue());
                        }
                    } catch (AnnotationDefinitionResolutionException e) {
                        throw new ExternalResourceException(e.getName(),
                                e.getMessage(), e);
                    }
                    break;
                }
            }
        } finally {
            close(rset);
        }

        // Add this list to the cache
        annotationTypeValueCache.put(annotationType.getId(), domainValues);

        return domainValues;
    }

    @Override
    public List<AnnotationType> getAnnotationTypes() throws SQLException {
        List<AnnotationType> list = new ArrayList<AnnotationType>();

        ResultSet rset = null;

        try {
            PreparedStatement ps =
                    getPreparedStatement(SELECT_ANNOTATION_TYPES_SQL);
            rset = ps.executeQuery();

            while (rset.next()) {
                AnnotationType annotationType = getAnnotationType(rset);
                addAnnotationType(list, annotationType);

                // This might save us time later on
                if (!annotationTypeCache.containsKey(annotationType.getId())) {
                    annotationTypeCache.put(annotationType.getId(),
                            annotationType);
                }
            }
        } finally {
            close(rset);
        }

        return list;
    }

    private AnnotationType createCitationType(AnnotationType annotationType) {
        return new AnnotationType(
                annotationType.getId(),
                CITATION,
                "The citation that supports one or more statements.",
                "Use this annotation to link statements to a citation.",
                AnnotationDefinitionType.REGULAR_EXPRESSION);
    }

    /**
     *
     * @param rset
     * @return
     * @throws SQLException
     */
    private AnnotationType getAnnotationType(ResultSet rset)
            throws SQLException {
        Integer annotationTypeId = rset.getInt(1);

        if (annotationTypeCache.containsKey(annotationTypeId)) {
            return annotationTypeCache.get(annotationTypeId);
        }

        String name = rset.getString(2);
        String description = rset.getString(3);
        String usage = rset.getString(4);
        Integer annotationDefinitionTypeId = rset.getInt(5);
        AnnotationDefinitionType annotationDefinitionType =
                AnnotationDefinitionType.fromValue(annotationDefinitionTypeId);

        final AnnotationType annotationType;
        if (annotationDefinitionType.equals(AnnotationDefinitionType.URL)) {
            ResultSet dvrset = null;
            try {
                PreparedStatement dvps =
                        getPreparedStatement(SELECT_ANNOTATION_TYPE_DOMAIN_VALUE_SQL);
                dvps.setInt(1, annotationTypeId);
                dvrset = dvps.executeQuery();

                String url = null;
                if (dvrset.next()) {
                    url = getObjectValueById(dvrset.getInt(1));
                }

                annotationType =
                        new AnnotationType(annotationTypeId, name, description,
                                usage, annotationDefinitionType, url);
            } finally {
                close(dvrset);
            }
        } else {
            annotationType =
                    new AnnotationType(annotationTypeId, name, description,
                            usage, annotationDefinitionType);
        }

        return annotationType;
    }

    /**
     *
     * @param nodes
     * @param params A {@link ResultSet} from a SELECT on the {@code kam_node_parameter}
     * table.
     * @return
     * @throws SQLException
     */
    private KamProtoNode getKamProtoNode(ResultSet nodes, ResultSet params)
            throws SQLException {
        Integer kamNodeId = nodes.getInt(1);
        Integer functionTypeId = nodes.getInt(2);
        String label = getObjectValueById(nodes.getInt(3));

        List<Integer> nodeParameterIds = new ArrayList<Integer>();

        while (params.next()) {
            if (params.getInt(1) != kamNodeId.intValue()) {
                params.previous(); // cursor poised for proper next() call
                break;
            }
            nodeParameterIds.add(params.getInt(2));
        }

        for (Integer nodeParameterId : nodeParameterIds) {
            label =
                    label.replaceFirst(ANY_NUMBER_PLACEHOLDER,
                            nodeParameterId.toString());
        }

        return new KamProtoNode(kamNodeId,
                FunctionEnum.fromValue(functionTypeId), label);
    }

    /**
     *
     * @param rset
     * @return
     * @throws SQLException
     */
    private Annotation getAnnotation(ResultSet rset) throws SQLException {
        Integer annotationId = rset.getInt(1);
        String label = getObjectValueById(rset.getInt(2));
        Integer annotationTypeId = rset.getInt(3);

        return new Annotation(annotationId,
                getAnnotationTypeById(annotationTypeId), label);
    }

    private Annotation getAnnotation(final Integer annotationId)
            throws SQLException {
        Annotation annotation = annotationCache.get(annotationId);
        if (annotation != null) {
            return annotation;
        }

        ResultSet rset = null;
        try {
            PreparedStatement ps =
                    getPreparedStatement(SELECT_ANNOTATION_BY_ID_SQL);
            ps.setInt(1, annotationId);
            rset = ps.executeQuery();

            if (rset.next()) {
                String label = getObjectValueById(rset.getInt(1));
                Integer annotationTypeId = rset.getInt(2);
                AnnotationType annotationType =
                        getAnnotationTypeById(annotationTypeId);

                annotation =
                        new Annotation(annotationId, annotationType, label);
                annotationCache.put(annotationId, annotation);

                return annotation;
            }
        } finally {
            close(rset);
        }

        return null;
    }

    /**
     * @param rset
     * @return
     * @throws SQLException
     */
    private Namespace getNamespace(ResultSet rset) throws SQLException {
        Integer namespaceId = rset.getInt(1);
        String prefix = rset.getString(2);
        String resourceLocation = getObjectValueById(rset.getInt(3));

        return new Namespace(namespaceId, prefix, resourceLocation);
    }

    /**
     *
     * @param rset
     * @return
     * @throws SQLException
     */
    private BelStatement getStatement(ResultSet rset) throws SQLException {
        Integer statementId = rset.getInt(1);
        Integer documentId = rset.getInt(2);
        Integer subjectTermId = rset.getInt(3);
        Integer relationshipTypeId = rset.getInt(4);
        boolean definitional = rset.wasNull();
        Integer objectTermId = rset.getInt(5);
        Integer nestedSubjectId = rset.getInt(6);
        Integer nestedRelationship = rset.getInt(7);
        Integer nestedObjectId = rset.getInt(8);

        // Get the document info for this statement
        BelDocumentInfo belDocumentInfo = getBelDocumentInfoById(documentId);
        // Lastly, get the annotations
        List<Annotation> annotations = getAnnotationsByStatementId(statementId);

        BelTerm subject = getBelTermById(subjectTermId);
        BelElement object = null;
        if (definitional) {
            return new BelStatement(statementId, subject, belDocumentInfo,
                    annotations);
        } else if (null != objectTermId && !objectTermId.equals(0)) {
            object = getBelTermById(objectTermId);
            return new BelStatement(statementId, subject,
                    RelationshipType.fromValue(relationshipTypeId), object,
                    belDocumentInfo, annotations);
        } else {
            BelTerm nestedSubject = getBelTermById(nestedSubjectId);
            BelTerm nestedObject = getBelTermById(nestedObjectId);
            BelStatement nested =
                    new BelStatement(statementId, nestedSubject,
                            RelationshipType.fromValue(nestedRelationship),
                            nestedObject,
                            belDocumentInfo, annotations);
            return new BelStatement(statementId, subject,
                    RelationshipType.fromValue(relationshipTypeId), nested,
                    belDocumentInfo, annotations);
        }
    }

    /**
     *
     * @param belTermId
     * @return
     * @throws SQLException
     */
    private BelDocumentInfo getBelDocumentInfo(ResultSet rset)
            throws SQLException {
        Integer belDocumentId = rset.getInt(1);
        String name = rset.getString(2);
        String description = rset.getString(3);
        String version = rset.getString(4);
        String copyright = rset.getString(5);
        String disclaimer = rset.getString(6);
        String contactInfo = rset.getString(7);
        String licenseInfo = rset.getString(8);
        String authors = rset.getString(9);

        List<AnnotationType> annotationTypes =
                getAnnotationTypesByDocumentId(belDocumentId);
        List<Namespace> namespaces = getNamespacesByDocumentId(belDocumentId);

        return new BelDocumentInfo(belDocumentId, name, description, version,
                copyright, disclaimer, contactInfo, licenseInfo, authors,
                annotationTypes, namespaces);
    }

    /**
     * @param objectsTextId
     * @return
     * @throws SQLException
     */
    private String getObjectsTextById(Integer objectsTextId)
            throws SQLException {
        PreparedStatement ps = null;
        ResultSet rset = null;
        String value = null;
        try {
            ps = getPreparedStatement(SELECT_OBJECTS_TEXT_SQL);
            ps.setInt(1, objectsTextId);
            rset = ps.executeQuery();
            if (rset.next()) {
                value = rset.getString(1);
            }
        } finally {
            close(rset);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    private String getObjectValueById(Integer objectId) throws SQLException {

        // See if the object is cached
        if (objectValueCache.containsKey(objectId)) {
            return objectValueCache.get(objectId);
        }

        String value = null;
        String decryptedString = value;
        //Integer typeId = null;
        PreparedStatement ps = null;
        ResultSet rset = null;
        int objectsTextId = 0;

        try {
            ps = getPreparedStatement(SELECT_OBJECTS_VALUE_SQL);
            ps.setInt(1, objectId);
            rset = ps.executeQuery();
            if (rset.next()) {
                //typeId = rset.getInt(1);
                value = rset.getString(2);
                objectsTextId = rset.getInt(3);
            }
            if (value == null && objectsTextId != 0) { //value is in objects_text table
                value = getObjectsTextById(objectsTextId);
            }
            //decrypt object table string
            if (value != null) {
                decryptedString = value;
                try {
                    decryptedString = encryptionService.decrypt(value);
                } catch (EncryptionServiceException e) {
                    throw new SQLException(
                            "Unable to decrypt data from object table.", e);
                }
            } else {
                // set decryptedString to empty to translate back from database null.
                // this is a valid entry since we are storing an empty "" on the way in.
                decryptedString = "";
            }
        } finally {
            close(rset);
        }

        // Push into the cache
        objectValueCache.put(objectId, decryptedString);
        objectValueReverseCache.put(decryptedString, objectId);

        return decryptedString;
    }

    /**
     * TODO: THis will fail if the object value is stored in the text table
     *
     * @param objectValue
     * @return
     * @throws SQLException
     */
    private Integer getObjectIdByValue(final String objectValue)
            throws SQLException {
        if (objectValue == null) {
            return null;
        }
        // Check to see if the objectValue has already been seen
        if (objectValueReverseCache.containsKey(objectValue)) {
            return objectValueReverseCache.get(objectValue);
        }

        ResultSet rset = null;
        Integer objectId = null;

        try {
            String encryptedValue = encryptionService.encrypt(objectValue);

            PreparedStatement ps = getPreparedStatement(SELECT_OBJECTS_ID_SQL);
            ps.setString(1, encryptedValue);

            rset = ps.executeQuery();

            if (rset.next()) {
                objectId = rset.getInt(1);
            }
        } catch (EncryptionServiceException e) {
            throw new SQLException(
                    "Unable to encrypt data to lookup in object table.", e);
        } finally {
            close(rset);
        }

        // Push into the cache
        if (objectId != null) {
            objectValueCache.put(objectId, objectValue);
            objectValueReverseCache.put(objectValue, objectId);
        }

        return objectId;
    }

    /**
     * Retrieves a {@link Set set} of all {@link String citation definitions}.
     *
     * @return the {@link Set set}
     */
    private static Set<String> getCitationDefinitions() {

        final Set<String> citationKeys =
                sizedHashSet(KAMStoreConstants.CITATION_ANNOTATION_DEFINITION_IDS.length);
        citationKeys.addAll(Arrays
                .asList(KAMStoreConstants.CITATION_ANNOTATION_DEFINITION_IDS));

        return citationKeys;
    }

    /**
     *
     * @author julianjray
     */
    public final static class TermParameter extends KamElementImpl {
        private final Namespace namespace;
        private final String parameterValue;

        protected TermParameter(Integer id, Namespace namespace,
                String parameterValue) {
            super(null, id);
            this.namespace = namespace;
            this.parameterValue = parameterValue;
        }

        public Namespace getNamespace() {
            return namespace;
        }

        public String getParameterValue() {
            return parameterValue;
        }
    }

    /**
     * {@link KamProtoNode} represents a {@link Kam kam} node retrieved from
     * a KAM schema.
     *
     * @author julianjray
     */
    public final static class KamProtoNode extends KamElementImpl {
        private final FunctionEnum functionType;
        private final String label;
        private int hash;

        /**
         * Constructs the {@link KamProtoNode kam proto node}.
         *
         * <p>
         * The hashcode is precomputed since {@link KamProtoNode} is immutable.
         * </p>
         *
         * @param id the database {@link Integer id} of the {@link Kam kam}
         * node.
         * @param functionType the {@link Kam kam} node
         * {@link FunctionEnum function}
         * @param label the node {@link String label}
         * @throws InvalidArgument Thrown if {@code functionType} or
         * {@code label} is {@code null}
         */
        private KamProtoNode(final Integer id, final FunctionEnum functionType,
                final String label) {
            super(null, id);

            if (functionType == null) {
                throw new InvalidArgument("functionType", functionType);
            }

            if (label == null) {
                throw new InvalidArgument("label", label);
            }

            this.functionType = functionType;
            this.label = label;
            hash = computeHash();
        }

        /**
         * Retrieves the {@link FunctionEnum function type}.
         *
         * @return the {@link FunctionEnum function type}, which will be
         * {@code non-null}
         */
        public FunctionEnum getFunctionType() {
            return functionType;
        }

        /**
         * Retrieves the {@link String label}.
         *
         * @return the {@link String label}, which will be {@code non-null}
         */
        public String getLabel() {
            return label;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
            if (o == null || !(o instanceof KamProtoNode)) {
                return false;
            }
            KamProtoNode other = (KamProtoNode) o;

            if (!(this.functionType.equals(other.functionType))) {
                return false;
            }

            // The label may contain '#' instead of a string of numbers, so this method
            // needs to check number-patterns separately.  '#' matches '#' or any
            // number, but otherwise the numbers and non-number substrings are matched
            // exactly.

            final Matcher thisMatcher =
                    ANY_NUMBER_REGEX_PATTERN.matcher(this.label), otherMatcher =
                    ANY_NUMBER_REGEX_PATTERN.matcher(other.label);

            int i = 0, j = 0;
            final int iMax = this.label.length(), jMax = other.label.length();
            while (true) {
                final boolean b1 = thisMatcher.find(i);
                final boolean b2 = otherMatcher.find(j);
                if (b1 != b2) {
                    // One label has more instances of the pattern than the other,
                    // so they can not be equal.
                    return false;

                } else if (!b1) {
                    // There are no more number patterns in either this.label or
                    // other.label, so simply compare the rest of the labels.
                    return substringEquals(this.label, i, iMax, other.label, j,
                            jMax);

                } else if (!substringEquals(this.label, i, thisMatcher.start(),
                        other.label, j, otherMatcher.start())) {
                    // The substring before the next pattern match are not equal,
                    // so the labels can not be equal.
                    return false;

                } else if (substringEquals(this.label, thisMatcher.start(),
                        thisMatcher.end(),
                        ANY_NUMBER_PLACEHOLDER, 0,
                        ANY_NUMBER_PLACEHOLDER_LENGTH)
                        ||
                        substringEquals(other.label, otherMatcher.start(),
                                otherMatcher.end(),
                                ANY_NUMBER_PLACEHOLDER, 0,
                                ANY_NUMBER_PLACEHOLDER_LENGTH)
                        ||
                        substringEquals(this.label, thisMatcher.start(),
                                thisMatcher.end(),
                                other.label, otherMatcher.start(),
                                otherMatcher.end())) {

                    // this.label and other.label have matching patterns, so continue the search
                    // after incrementing i and j.
                    i = thisMatcher.end();
                    j = otherMatcher.end();

                } else {
                    // The numbers do not match, so the labels are not equal.
                    return false;
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return hash;
        }

        private int computeHash() {
            // The label may contain '#' instead of a number.  Compute the hash
            // as if all numbers in the label have been replaced with '#'.
            final int labelHashCode = NUMBER_REGEX_PATTERN.matcher(this.label)
                    .replaceAll(ANY_NUMBER_PLACEHOLDER).hashCode();

            // TODO improve implementation
            return this.functionType.hashCode() ^ labelHashCode;
        }
    }

    /**
     * {@link KamProtoEdge} represents a {@link Kam kam} edge retrieved from
     * a KAM schema.
     *
     * @author julianjray
     */
    public final static class KamProtoEdge extends KamElementImpl {
        private final KamProtoNode sourceNode;
        private final KamProtoNode targetNode;
        private final RelationshipType relationshipType;
        private int hash;

        /**
         * Constructs the {@link KamProtoEdge kam proto edge}.
         *
         * <p>
         * The hashcode is precomputed since {@link KamProtoEdge} is immutable.
         * </p>
         *
         * @param kamEdgeId the database {@link Integer id} of the
         * {@link Kam kam} node.
         * @param sourceNode the source {@link KamProtoNode node} for this edge
         * @param relationshipType the {@link RelationshipType relationship}
         * for this edge
         * @param targetNode the target {@link KamProtoNode node} for this edge
         * @throws InvalidArgument Thrown if {@code sourceNode},
         * {@code RelationshipType}, or {@code targetNode} is {@code null}
         */
        private KamProtoEdge(Integer kamEdgeId, KamProtoNode sourceNode,
                RelationshipType relationshipType, KamProtoNode targetNode) {
            super(null, kamEdgeId);

            if (sourceNode == null) {
                throw new InvalidArgument("sourceNode", sourceNode);
            }

            if (relationshipType == null) {
                throw new InvalidArgument("relationshipType", relationshipType);
            }

            if (targetNode == null) {
                throw new InvalidArgument("targetNode", targetNode);
            }

            this.sourceNode = sourceNode;
            this.relationshipType = relationshipType;
            this.targetNode = targetNode;
            this.hash = computeHash();
        }

        /**
         * Retrieves the source {@link KamProtoNode node}.
         *
         * @return the source {@link KamProtoNode node}, which will be
         * {@code non-null}
         */
        public KamProtoNode getSourceNode() {
            return sourceNode;
        }

        /**
         * Retrieves the target {@link KamProtoNode node}.
         *
         * @return the target {@link KamProtoNode node}, which will be
         * {@code non-null}
         */
        public KamProtoNode getTargetNode() {
            return targetNode;
        }

        /**
         * Retrieves the {@link RelationshipType relationship}.
         *
         * @return the edge's {@link RelationshipType relationship}, which will
         * be {@code non-null}
         */
        public RelationshipType getRelationship() {
            return relationshipType;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
            if ((o == null) || !(o instanceof KamProtoEdge)) {
                return false;
            }
            KamProtoEdge other = (KamProtoEdge) o;
            return (this.sourceNode.equals(other.sourceNode) &&
                    this.targetNode.equals(other.targetNode) && this.relationshipType
                        .equals(other.relationshipType));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return hash;
        }

        private int computeHash() {
            // TODO improve implementation
            return this.sourceNode.hashCode() ^ this.targetNode.hashCode()
                    ^ this.relationshipType.hashCode();
        }
    }

    /**
     *
     * @author julianjray
     */
    public final static class Namespace extends KamStoreObjectImpl {
        private final String prefix;
        private final String resourceLocation;

        protected Namespace(Integer namespaceId, String prefix,
                String resourceLocation) {
            super(namespaceId);
            this.prefix = prefix;
            this.resourceLocation = resourceLocation;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getResourceLocation() {
            return resourceLocation;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj == null) {
                return false;
            } else if (!(obj instanceof Namespace)) {
                return false;
            } else {
                Namespace other = (Namespace) obj;
                return (BELUtilities.equals(getId(), other.getId()) &&
                        BELUtilities.equals(prefix, other.prefix) && BELUtilities
                            .equals(resourceLocation, other.resourceLocation));
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hash = 0;

            final Integer id = getId();
            hash += (id != null ? id.hashCode() : 0);
            hash *= prime;
            hash += (prefix != null ? prefix.hashCode() : 0);
            hash *= prime;
            hash +=
                    (resourceLocation != null ? resourceLocation.hashCode() : 0);
            return hash;
        }
    }

    /**
     *
     * @author julianjray
     */
    public static final class BelDocumentInfo extends KamStoreObjectImpl {
        private final String name;
        private final String description;
        private final String version;
        private final String copyright;
        private final String disclaimer;
        private final String contactInfo;
        private final String licenseInfo;
        private final String authors;
        private final List<AnnotationType> annotationTypes;
        private final List<Namespace> namespaces;

        public BelDocumentInfo(Integer belDocumentId, String name,
                String description, String version, String copyright,
                String disclaimer, String contactInfo, String licenseInfo,
                String authors, List<AnnotationType> annotationTypes,
                List<Namespace> namespaces) {
            super(belDocumentId);
            this.name = name;
            this.description = description;
            this.version = version;
            this.copyright = copyright;
            this.disclaimer = disclaimer;
            this.contactInfo = contactInfo;
            this.licenseInfo = licenseInfo;
            this.authors = authors;

            this.annotationTypes = new ArrayList<AnnotationType>();
            this.annotationTypes.addAll(annotationTypes);

            this.namespaces = new ArrayList<Namespace>();
            this.namespaces.addAll(namespaces);
        }

        public List<AnnotationType> getAnnotationTypes() {
            List<AnnotationType> list = new ArrayList<AnnotationType>();
            list.addAll(this.annotationTypes);
            return list;
        }

        public List<Namespace> getNamespaces() {
            List<Namespace> list = new ArrayList<Namespace>();
            list.addAll(this.namespaces);
            return list;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getVersion() {
            return version;
        }

        public String getCopyright() {
            return copyright;
        }

        public String getDisclaimer() {
            return disclaimer;
        }

        public String getContactInfo() {
            return contactInfo;
        }

        public String getLicenseInfo() {
            return licenseInfo;
        }

        public String getAuthors() {
            return authors;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj == null) {
                return false;
            } else if (!(obj instanceof BelDocumentInfo)) {
                return false;
            } else {
                BelDocumentInfo other = (BelDocumentInfo) obj;
                return (BELUtilities.equals(getId(), other.getId())
                        &&
                        BELUtilities.equals(name, other.name)
                        &&
                        BELUtilities.equals(description, other.description)
                        &&
                        BELUtilities.equals(version, other.version)
                        &&
                        BELUtilities.equals(copyright, other.copyright)
                        &&
                        BELUtilities.equals(disclaimer, other.disclaimer)
                        &&
                        BELUtilities.equals(contactInfo, other.contactInfo)
                        &&
                        BELUtilities.equals(licenseInfo, other.licenseInfo)
                        &&
                        BELUtilities.equals(authors, other.authors)
                        &&
                        BELUtilities.equals(annotationTypes,
                                other.annotationTypes) && BELUtilities.equals(
                        namespaces, other.namespaces));
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hash = 0, annotationTypesHash = 0, namespacesHash = 0;
            if (annotationTypes != null) {
                for (AnnotationType annotationType : annotationTypes) {
                    annotationTypesHash ^= annotationType.hashCode();
                }
            }
            if (namespaces != null) {
                for (Namespace namespace : namespaces) {
                    namespacesHash ^= namespace.hashCode();
                }
            }

            final Integer id = getId();
            hash += (id != null ? id.hashCode() : 0);
            hash *= prime;
            hash += (name != null ? name.hashCode() : 0);
            hash *= prime;
            hash += (description != null ? description.hashCode() : 0);
            hash *= prime;
            hash += (version != null ? version.hashCode() : 0);
            hash *= prime;
            hash += (copyright != null ? copyright.hashCode() : 0);
            hash *= prime;
            hash += (disclaimer != null ? disclaimer.hashCode() : 0);
            hash *= prime;
            hash += (contactInfo != null ? contactInfo.hashCode() : 0);
            hash *= prime;
            hash += (licenseInfo != null ? licenseInfo.hashCode() : 0);
            hash *= prime;
            hash += (authors != null ? authors.hashCode() : 0);
            hash *= prime;
            hash += annotationTypesHash;
            hash *= prime;
            hash += namespacesHash;
            return hash;
        }
    }

    /**
     *
     * @author julianjray
     */
    public static final class Citation {
        private final String name;
        private final String id;
        private final String comment;
        private final Date publicationDate;
        private final List<String> authors;
        private final CitationType citationType;

        public Citation(String name, String id, String comment,
                Date publicationDate, List<String> authors,
                CitationType citationType) {
            this.name = name;
            this.id = id;
            this.comment = comment;
            this.publicationDate = publicationDate;
            this.authors = authors;
            this.citationType = citationType;
        }

        private Citation(List<Annotation> annotationList) {
            String name = null;
            String id = null;
            String comment = null;
            Date publicationDate = null;
            List<String> authors = null;
            CitationType citationType = null;

            for (Annotation annotation : annotationList) {
                if (CitationReferenceAnnotationDefinition.ANNOTATION_DEFINITION_ID
                        .equals(annotation.getAnnotationType().getName())) {
                    //citation reference id
                    id = annotation.getValue();
                } else if (CitationDateAnnotationDefinition.ANNOTATION_DEFINITION_ID
                        .equals(annotation.getAnnotationType().getName())) {
                    if (annotation.getValue() != null) {
                        try {
                            publicationDate =
                                    dateFormat.parse(annotation.getValue());
                        } catch (ParseException e) {
                            publicationDate = null;
                        }
                    }
                } else if (CitationNameAnnotationDefinition.ANNOTATION_DEFINITION_ID
                        .equals(annotation.getAnnotationType().getName())) {
                    name = annotation.getValue();
                } else if (CitationCommentAnnotationDefinition.ANNOTATION_DEFINITION_ID
                        .equals(annotation.getAnnotationType().getName())) {
                    comment = annotation.getValue();
                } else if (CitationTypeAnnotationDefinition.ANNOTATION_DEFINITION_ID
                        .equals(annotation.getAnnotationType().getName())) {
                    citationType =
                            CitationType.getCitationType(annotation.getValue());
                } else if (CitationAuthorsAnnotationDefinition.ANNOTATION_DEFINITION_ID
                        .equals(annotation.getAnnotationType().getName())) {
                    if (annotation.getValue() != null) {
                        authors = PackUtils.unpackValues(annotation.getValue());
                    }
                }
            }
            this.name = name;
            this.id = id;
            this.comment = comment;
            this.publicationDate = publicationDate;
            this.authors = authors;
            this.citationType = citationType;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public String getComment() {
            return comment;
        }

        public Date getPublicationDate() {
            return publicationDate;
        }

        public List<String> getAuthors() {
            return authors;
        }

        public CitationType getCitationType() {
            return citationType;
        }

        @Override
        public boolean equals(Object obj) {

            if (obj == null) {
                return false;
            } else if (!(obj instanceof Citation)) {
                return false;
            } else {
                Citation other = (Citation) obj;
                return (BELUtilities.equals(name, other.name)
                        &&
                        BELUtilities.equals(id, other.id)
                        &&
                        BELUtilities.equals(comment, other.comment)
                        &&
                        BELUtilities.equals(publicationDate,
                                other.publicationDate) &&
                        BELUtilities.equals(authors, other.authors) && BELUtilities
                            .equals(citationType, other.citationType));
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hash = 0;
            int authorsHash = 0;

            if (authors != null) {
                for (String author : authors) {
                    authorsHash ^= author.hashCode();
                }
            }

            hash *= prime;
            hash += (name != null ? name.hashCode() : 0);
            hash *= prime;
            hash += (id != null ? id.hashCode() : 0);
            hash *= prime;
            hash += (comment != null ? comment.hashCode() : 0);
            hash *= prime;
            hash += (publicationDate != null ? publicationDate.hashCode() : 0);
            hash *= prime;
            hash += authorsHash;
            hash *= prime;
            hash += (citationType != null ? citationType.hashCode() : 0);
            return hash;
        }
    }

    /**
     *
     * @author julianjray
     */
    public final static class BelStatement extends BelElement {
        private final BelDocumentInfo belDocumentInfo;
        private final BelTerm subject;
        private final RelationshipType relationshipType;
        private final BelElement object;
        private final List<Annotation> annotationList;
        private final Citation citation;

        private BelStatement(Integer belStatementId, BelTerm subject,
                BelDocumentInfo belDocumentInfo, List<Annotation> annotationList) {
            super(belStatementId);
            this.subject = subject;
            this.relationshipType = null;
            this.object = null;
            this.belDocumentInfo = belDocumentInfo;
            this.annotationList = new ArrayList<Annotation>();
            for (Annotation annotation : annotationList) {
                if (!ArrayUtils.contains(
                        KAMStoreConstants.CITATION_ANNOTATION_DEFINITION_IDS,
                        annotation.getAnnotationType().getName())) {
                    //non citation annotations
                    this.annotationList.add(annotation);
                }
            }
            this.citation = new Citation(annotationList);
        }

        private BelStatement(Integer belStatementId, BelTerm subject,
                RelationshipType relationshipType, BelElement object,
                BelDocumentInfo belDocumentInfo, List<Annotation> annotationList) {
            super(belStatementId);
            this.subject = subject;
            this.relationshipType = relationshipType;
            this.object = object;
            this.belDocumentInfo = belDocumentInfo;
            this.annotationList = new ArrayList<Annotation>();
            for (Annotation annotation : annotationList) {
                if (!ArrayUtils.contains(
                        KAMStoreConstants.CITATION_ANNOTATION_DEFINITION_IDS,
                        annotation.getAnnotationType().getName())) {
                    //non citation annotations
                    this.annotationList.add(annotation);
                }
            }
            this.citation = new Citation(annotationList);
        }

        /**
         * returns a list which immutable with respect to the statement
         * @return
         */
        public List<Annotation> getAnnotationList() {
            List<Annotation> list = new ArrayList<Annotation>();
            list.addAll(this.annotationList);
            return list;
        }

        /**
         * returns a citation object associated with this statement
         * @return
         */
        public Citation getCitation() {
            return citation;
        }

        public BelDocumentInfo getBelDocumentInfo() {
            return belDocumentInfo;
        }

        public BelTerm getSubject() {
            return subject;
        }

        public RelationshipType getRelationshipType() {
            return relationshipType;
        }

        public BelElement getObject() {
            return object;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(subject.toString());
            if (relationshipType == null) {
                // definitional statement
                return sb.toString();
            }

            sb.append(" ");
            sb.append(relationshipType.getDisplayValue());

            if (object instanceof BelStatement) {
                // nested statement
                sb.append(" (");
                sb.append(object.toString());
                sb.append(" )");
                return sb.toString();
            }

            // simple statement
            sb.append(" ");
            sb.append(object.toString());
            return sb.toString();
        }
    }

    /**
     *
     * @author julianjray
     */
    public final static class BelTerm extends BelElement {
        private final String label;

        protected BelTerm(Integer belTermId, String label) {
            super(belTermId);
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    /**
     *
     * @author julianjray
     */
    public final static class Annotation extends KamStoreObjectImpl {

        private final AnnotationType annotationType;
        private final String value;

        private Annotation(Integer annotationId, AnnotationType annotationType,
                String value) {
            super(annotationId);
            this.value = value;
            this.annotationType = annotationType;
        }

        public AnnotationType getAnnotationType() {
            return annotationType;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     *
     * @author julianjray
     *
     */
    public static final class AnnotationType extends KamStoreObjectImpl {

        private final String name;
        private final String description;
        private final String usage;
        private final AnnotationDefinitionType annotationDefinitionType;
        private final String url;

        public AnnotationType(Integer annotationTypeId, String name,
                String description, String usage,
                AnnotationDefinitionType annotationDefinitionType) {
            super(annotationTypeId);
            this.name = name;
            this.description = description;
            this.usage = usage;
            this.annotationDefinitionType = annotationDefinitionType;
            this.url = null;
        }

        private AnnotationType(Integer annotationTypeId, String name,
                String description, String usage,
                AnnotationDefinitionType annotationDefinitionType, String url) {
            super(annotationTypeId);
            this.name = name;
            this.description = description;
            this.usage = usage;
            this.annotationDefinitionType = annotationDefinitionType;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getUsage() {
            return usage;
        }

        public AnnotationDefinitionType getAnnotationDefinitionType() {
            return annotationDefinitionType;
        }

        /**
         * Returns the url of this annotation type.
         *
         * @return {@link String} the url where the annotation is defined,
         * which may be <tt>null</tt>
         */
        public String getUrl() {
            return url;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj == null) {
                return false;
            } else if (!(obj instanceof AnnotationType)) {
                return false;
            } else {
                AnnotationType other = (AnnotationType) obj;
                return (BELUtilities.equals(getId(), other.getId())
                        &&
                        BELUtilities.equals(name, other.name)
                        &&
                        BELUtilities.equals(description, other.description)
                        &&
                        BELUtilities.equals(usage, other.usage)
                        &&
                        BELUtilities.equals(annotationDefinitionType,
                                other.annotationDefinitionType) && BELUtilities
                            .equals(url, other.url));
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hash = 0;
            final Integer id = getId();
            hash += (id != null ? id.hashCode() : 0);
            hash *= prime;
            hash += (name != null ? name.hashCode() : 0);
            hash *= prime;
            hash += (description != null ? description.hashCode() : 0);
            hash *= prime;
            hash += (usage != null ? usage.hashCode() : 0);
            hash *= prime;
            hash +=
                    (annotationDefinitionType != null ? annotationDefinitionType
                            .hashCode()
                            : 0);
            hash *= prime;
            hash += (url != null ? url.hashCode() : 0);
            return hash;
        }
    }

    /**
     * Annotation definition type
     *
     */
    public enum AnnotationDefinitionType {

        /**
         * An annotation whose value must match one from an enumerated list.
         */
        ENUMERATION(0, "listAnnotation"),

        /**
         * An annotation whose value must match a regular expression.
         */
        REGULAR_EXPRESSION(1, "patternAnnotation"),

        /**
         * An annotation whose value is specified by a URL
         */
        URL(2, "urlAnnotation");

        /**
         * Value unique to each enumeration.
         */
        private final Integer value;
        /**
         * Enumeration display value.
         */
        private final String displayValue;

        /**
         * Constructor for setting enum and display value.
         *
         * @param value Enum value
         * @param displayValue Display value
         */
        private AnnotationDefinitionType(Integer value, String displayValue) {
            this.value = value;
            this.displayValue = displayValue;
        }

        /**
         * Returns the annotation type by its integer value.
         * @param value
         * @return
         */
        public static AnnotationDefinitionType fromValue(final Integer value) {
            AnnotationDefinitionType type = null;
            if (value != null) {
                for (AnnotationDefinitionType adt : AnnotationDefinitionType
                        .values()) {
                    if (value.equals(adt.value)) {
                        type = adt;
                        break;
                    }
                }
            }
            return type;
        }

        public Integer getValue() {
            return value;
        }

        public String getDisplayValue() {
            return displayValue;
        }
    }
}
