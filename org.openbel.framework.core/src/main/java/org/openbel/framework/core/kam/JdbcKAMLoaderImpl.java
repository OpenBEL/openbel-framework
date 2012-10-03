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
package org.openbel.framework.core.kam;

import static org.openbel.framework.common.BELUtilities.hasItems;
import static org.openbel.framework.common.BELUtilities.sizedHashSet;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.AnnotationType;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.protonetwork.model.AnnotationDefinitionTable;
import org.openbel.framework.common.protonetwork.model.AnnotationDefinitionTable.TableAnnotationDefinition;
import org.openbel.framework.common.protonetwork.model.AnnotationValueTable;
import org.openbel.framework.common.protonetwork.model.AnnotationValueTable.TableAnnotationValue;
import org.openbel.framework.common.protonetwork.model.DocumentTable.DocumentHeader;
import org.openbel.framework.common.protonetwork.model.NamespaceTable;
import org.openbel.framework.common.protonetwork.model.NamespaceTable.TableNamespace;
import org.openbel.framework.common.protonetwork.model.ParameterTable;
import org.openbel.framework.common.protonetwork.model.ParameterTable.TableParameter;
import org.openbel.framework.common.protonetwork.model.ProtoEdgeTable;
import org.openbel.framework.common.protonetwork.model.ProtoEdgeTable.TableProtoEdge;
import org.openbel.framework.common.protonetwork.model.ProtoNodeTable;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.common.protonetwork.model.StatementAnnotationMapTable;
import org.openbel.framework.common.protonetwork.model.StatementAnnotationMapTable.AnnotationPair;
import org.openbel.framework.common.protonetwork.model.StatementTable;
import org.openbel.framework.common.protonetwork.model.StatementTable.TableStatement;
import org.openbel.framework.common.protonetwork.model.TermParameterMapTable;
import org.openbel.framework.common.protonetwork.model.TermTable;
import org.openbel.framework.core.df.AbstractJdbcDAO;
import org.openbel.framework.core.df.DBConnection;

/**
 * JdbcKAMLoaderImpl implements a {@link KAMLoader} to load a KAM into the
 * KAMstore schema using Jdbc.
 * <p>
 * The {@link PreparedStatement} resources are managed in
 * {@link AbstractJdbcDAO} so one must take care in calling
 * {@link AbstractJdbcDAO#terminate()} after the KAMLoader DAO is used.
 * </p>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class JdbcKAMLoaderImpl extends AbstractJdbcDAO implements KAMLoader {

    // max varchar length, anything longer than this length will be stored as
    // text
    private static final int MAX_VARCHAR_LENGTH = 4000;
    private static final int MAX_MEDIUM_VARCHAR_LENGTH = 255;

    private static final String FUNCTION_TYPE_SQL =
            "insert into @.function_type(function_type_id,name) values(?,?)";
    private static final String OBJECT_TYPE_SQL =
            "insert into @.objects_type(objects_type_id,name) values(?,?)";
    private static final String RELATIONSHIP_TYPE_SQL =
            "insert into @.relationship_type(relationship_type_id,name) " +
                    "values(?, ?)";
    private static final String ANNOTATION_DEFINITION_TYPE_SQL =
            "insert into @.annotation_definition_type" +
                    "(annotation_definition_type_id,name) " +
                    "values(?, ?)";
    private static final String DOCUMENT_HEADER_INFORMATION_SQL =
            "insert into @.document_header_information(document_id,name,description,"
                    + "version,copyright,disclaimer,contact_information,authors,license_information) "
                    + "values(?,?,?,?,?,?,?,?,?)";
    private static final String OBJECTS_SQL =
            "insert into @.objects(type_id,varchar_value,objects_text_id) values(?,?,?)";
    private static final String OBJECTS_ID_COLUMN = "OBJECTS_ID";
    private static final String OBJECTS_ID_COLUMN_POSTGRESQL = "objects_id";
    private static final String OBJECTS_TEXT_SQL =
            "insert into @.objects_text(text_value) values(?)";
    private static final String OBJECTS_TEXT_COLUMN = "OBJECTS_TEXT_ID";
    private static final String OBJECTS_TEXT_COLUMN_POSTGRESQL =
            "objects_text_id";
    private static final String NAMESPACE_SQL =
            "insert into @.namespace(namespace_id,prefix,resource_location_oid) "
                    +
                    "values(?,?,?)";
    private static final String DOCUMENT_NAMESPACE_SQL =
            "insert into @.document_namespace_map(document_id,namespace_id) " +
                    "values(?,?)";
    private static final String TERM_PARAMETER_SQL =
            "insert into @.term_parameter(term_parameter_id,kam_global_parameter_id,"
                    +
                    "term_id,namespace_id,parameter_value_oid,ordinal) values(?,?,?,?,?,?)";
    private static final String TERM_SQL =
            "insert into @.term(term_id,kam_node_id,term_label_oid) " +
                    "values(?,?,?)";
    private static final String KAM_NODE_SQL =
            "insert into @.kam_node(kam_node_id,function_type_id,node_label_oid) "
                    +
                    "values(?,?,?)";
    private static final String KAM_NODE_PARAMETER_SQL =
            "insert into @.kam_node_parameter(kam_global_parameter_id,kam_node_id,"
                    +
                    "ordinal) values(?,?,?)";
    private static final String KAM_EDGE_SQL =
            "insert into @.kam_edge(kam_edge_id,kam_source_node_id," +
                    "kam_target_node_id,relationship_type_id) values(?,?,?,?)";
    private static final String KAM_PARAMETER_UUID_SQL =
            "insert into @.kam_parameter_uuid(kam_global_parameter_id,most_significant_bits,"
                    + "least_significant_bits) values(?,?,?)";
    private static final String STATEMENT_SQL =
            "insert into @.statement(statement_id,document_id,"
                    +
                    "subject_term_id,relationship_type_id,object_term_id,"
                    +
                    "nested_subject_id,nested_relationship_type_id,nested_object_id) "
                    +
                    "values(?,?,?,?,?,?,?,?)";
    private static final String KAM_EDGE_STATEMENT_SQL =
            "insert into @.kam_edge_statement_map(kam_edge_id,statement_id) values(?,?)";
    private static final String ANNOTATION_DEFINITION_SQL =
            "insert into @.annotation_definition(annotation_definition_id," +
                    "name,description,annotation_usage,domain_value_oid," +
                    "annotation_definition_type_id) values(?,?,?,?,?,?)";
    private static final String DOCUMENT_ANNOTATION_DEFINITION_MAP_SQL =
            "insert into @.document_annotation_def_map(document_id," +
                    "annotation_definition_id) values(?,?)";
    private static final String ANNOTATION_SQL =
            "insert into @.annotation(annotation_id,value_oid," +
                    "annotation_definition_id) values(?,?,?)";
    private static final String STATEMENT_ANNOTATION_SQL =
            "insert into @.statement_annotation_map(statement_id,annotation_id) "
                    +
                    "values(?,?)";
    private static final String SIMPLE_SELECT_SQL =
            "select 1 from @.annotation_definition_type";

    private Map<String, Integer> valueIndexMap =
            new HashMap<String, Integer>();

    /**
     * Creates a JdbcKAMLoaderImpl from the Jdbc {@link Connection} that will be
     * used to load the KAM.
     *
     * @param dbc {@link Connection}, the database connection which must be
     * non-null and already open for sql execution.
     * @param schemaName {@link String}, the schema name for the KAM which must
     * be non-null
     * @throws InvalidArgument - Thrown if <tt>dbc</tt> is null,
     * <tt>schemaName</tt> is null, or the SQL connection is already closed.
     * @throws SQLException - Thrown if a SQL error occurred while loading the
     * KAM.
     */
    public JdbcKAMLoaderImpl(DBConnection dbc, String schemaName)
            throws SQLException {
        super(dbc, schemaName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean schemaExists() {
        PreparedStatement ps = null;
        try {
            ps = getPreparedStatement(SIMPLE_SELECT_SQL);
            ps.executeQuery();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    // Ignore it
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Integer> loadFunctionTypes() throws SQLException {
        PreparedStatement ps = getPreparedStatement(FUNCTION_TYPE_SQL);

        Map<String, Integer> functionTypeIdMap =
                new HashMap<String, Integer>();

        int ftid = 0;
        for (FunctionEnum f : FunctionEnum.values()) {
            String functionName = f.getDisplayValue();
            ps.setInt(1, ftid);
            ps.setString(2, functionName);
            ps.addBatch();
            functionTypeIdMap.put(functionName, ftid);
            ftid++;
        }

        ps.executeBatch();
        return functionTypeIdMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Integer> loadObjectTypes() throws SQLException {
        int otid = 0;

        PreparedStatement ps = getPreparedStatement(OBJECT_TYPE_SQL);

        Map<String, Integer> objectTypeIdMap = new HashMap<String, Integer>();
        ps.setInt(1, otid);
        ps.setString(2, "number");
        ps.addBatch();
        objectTypeIdMap.put("number", otid);
        otid++;
        ps.setInt(1, otid);
        ps.setString(2, "varchar");
        ps.addBatch();
        objectTypeIdMap.put("varchar", otid);
        otid++;
        ps.setInt(1, otid);
        ps.setString(2, "clob");
        ps.addBatch();
        objectTypeIdMap.put("clob", otid);

        ps.executeBatch();
        return objectTypeIdMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadRelationshipTypes() throws SQLException {
        PreparedStatement ps = getPreparedStatement(RELATIONSHIP_TYPE_SQL);
        for (RelationshipType r : RelationshipType.values()) {
            String relationshipName = r.getDisplayValue();
            ps.setInt(1, r.getValue());
            ps.setString(2, relationshipName);
            ps.addBatch();
        }

        ps.executeBatch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadAnnotationDefinitionTypes()
            throws SQLException {
        int adtid = 0;

        PreparedStatement ps =
                getPreparedStatement(ANNOTATION_DEFINITION_TYPE_SQL);
        for (AnnotationType at : AnnotationType.values()) {
            String annotationTypeName = at.getDisplayValue();
            ps.setInt(1, adtid);
            ps.setString(2, annotationTypeName);
            ps.addBatch();
            adtid++;
        }

        // FIXME [Hack] add URL annotation type
        ps.setInt(1, AnnotationDefinitionTable.URL_ANNOTATION_TYPE_ID);
        ps.setString(2, AnnotationDefinitionTable.URL_ANNOTATION_TYPE);
        ps.addBatch();

        ps.executeBatch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadDocuments(List<DocumentHeader> documents)
            throws SQLException {
        PreparedStatement ps =
                getPreparedStatement(DOCUMENT_HEADER_INFORMATION_SQL);

        for (int i = 0; i < documents.size(); i++) {
            DocumentHeader dh = documents.get(i);

            ps.setInt(1, (i + 1));
            ps.setString(2, StringUtils.left(dh.getName(), 255));
            ps.setString(3, StringUtils.left(dh.getDescription(), 255));
            ps.setString(4, StringUtils.left(dh.getVersion(), 64));
            ps.setString(5, StringUtils.left(dh.getCopyright(), 4000));
            ps.setString(6, StringUtils.left(dh.getDisclaimer(), 4000));
            ps.setString(7, StringUtils.left(dh.getContactInfo(), 4000));
            ps.setString(8, StringUtils.left(dh.getAuthors(), 4000));
            ps.setString(9, StringUtils.left(dh.getLicenses(), 4000));

            ps.execute();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadNamespace(int i, TableNamespace ns) throws SQLException {
        PreparedStatement ps;

        int rloid = saveObject(1, ns.getResourceLocation());

        ps = getPreparedStatement(NAMESPACE_SQL);
        ps.setInt(1, (i + 1));
        ps.setString(2, ns.getPrefix());
        ps.setInt(3, rloid);

        ps.execute();
    }

    @Override
    public void loadDocumentNamespaceMap(Map<Integer, List<Integer>> dnsm)
            throws SQLException {
        PreparedStatement ps = getPreparedStatement(DOCUMENT_NAMESPACE_SQL);

        Set<Entry<Integer, List<Integer>>> entries = dnsm.entrySet();
        for (final Entry<Integer, List<Integer>> entry : entries) {
            final Integer key = entry.getKey();
            for (final Integer nsi : entry.getValue()) {
                ps.setInt(1, (key + 1));
                ps.setInt(2, (nsi + 1));
                ps.addBatch();
            }
        }
        ps.executeBatch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadNodes(NamespaceTable nt, ParameterTable pt, TermTable tt,
            TermParameterMapTable tpmt, ProtoNodeTable pnt) throws SQLException {
        List<String> terms = tt.getTermValues();
        Map<Integer, List<Integer>> tpmtidx = tpmt.getTermParameterIndex();

        PreparedStatement knps = getPreparedStatement(KAM_NODE_SQL);

        // load kam nodes
        final List<String> nodes = pnt.getProtoNodes();
        final Map<Integer, Integer> eqn = pnt.getEquivalences();
        final Set<Integer> added = new HashSet<Integer>();
        for (int i = 0, n = nodes.size(); i < n; i++) {
            final Integer eqId = eqn.get(i);

            // continue if we have already seen this equivalent proto node
            if (added.contains(eqId)) {
                continue;
            }

            added.add(eqId);

            final String nl = nodes.get(i);

            Integer knl = valueIndexMap.get(nl);
            if (knl == null) {
                knl = saveObject(1, nl);
                valueIndexMap.put(nl, knl);
            }

            String tf = nl.substring(0, nl.indexOf('('));
            int fv = FunctionEnum.getFunctionEnum(tf).getValue();

            // XXX offset
            knps.setInt(1, eqId + 1);
            knps.setInt(2, fv);
            knps.setInt(3, knl);
            knps.addBatch();
        }

        knps.executeBatch();

        PreparedStatement knpps = getPreparedStatement(KAM_NODE_PARAMETER_SQL);
        PreparedStatement knups = getPreparedStatement(KAM_PARAMETER_UUID_SQL);
        final Map<Integer, Integer> gpi = pt.getGlobalIndex();
        final Map<Integer, SkinnyUUID> guu = pt.getGlobalUUIDs();

        final Set<Integer> seenKamNodes = sizedHashSet(nodes.size());
        final Set<Integer> seenGlobalIds = sizedHashSet(gpi.size());

        // load kam node parameters
        final Map<Integer, Integer> nti = pnt.getNodeTermIndex();
        for (int i = 0, n = nodes.size(); i < n; i++) {
            final Integer ti = nti.get(i);

            // XXX offset
            final Integer eqId = eqn.get(i) + 1;

            // don't add kam node parameters if we have already seen the
            // equivalent node.
            if (seenKamNodes.contains(eqId)) {
                continue;
            }

            List<Integer> gtpl = tpmtidx.get(ti);
            if (hasItems(gtpl)) {
                for (int j = 0; j < gtpl.size(); j++) {
                    // get parameter index, retrieve global parameter, and set
                    Integer parameterIndex = gtpl.get(j);

                    final Integer gid = gpi.get(parameterIndex);

                    // XXX offset
                    knpps.setInt(1, gid + 1);
                    knpps.setInt(2, eqId);
                    knpps.setInt(3, j);
                    knpps.addBatch();

                    if (seenGlobalIds.contains(gid)) {
                        continue;
                    }
                    SkinnyUUID uuid = guu.get(gid);
                    if (uuid != null) {
                        // XXX offset
                        knups.setInt(1, gid + 1);
                        knups.setLong(2, uuid.getMostSignificantBits());
                        knups.setLong(3, uuid.getLeastSignificantBits());
                        knups.addBatch();
                    }
                    seenGlobalIds.add(gid);
                }
            }

            // track equivalent kam node
            seenKamNodes.add(eqId);
        }

        knpps.executeBatch();

        PreparedStatement pps = getPreparedStatement(TERM_PARAMETER_SQL);
        PreparedStatement tps = getPreparedStatement(TERM_SQL);
        final Map<Integer, Integer> termNodes = pnt.getTermNodeIndex();
        // load term parameters and terms
        int tpindex = 0;
        for (int ti = 0; ti < terms.size(); ti++) {
            String t = terms.get(ti);

            // find node equivalence
            Integer nodeId = termNodes.get(ti);
            // XXX offset
            final Integer eqId = eqn.get(nodeId) + 1;

            Integer ctl = valueIndexMap.get(t);
            if (ctl == null) {
                ctl = saveObject(1, t);
                valueIndexMap.put(t, ctl);
            }

            // XXX offset
            tps.setInt(1, ti + 1);
            // XXX offset
            tps.setInt(2, eqId);
            tps.setInt(3, ctl);
            tps.addBatch();

            int ord = 0;
            List<Integer> pl = tpmtidx.get(ti);
            if (hasItems(pl)) {
                for (Integer pi : pl) {
                    TableParameter p = pt.getIndexTableParameter().get(pi);
                    Integer cpv = valueIndexMap.get(p.getValue());
                    if (cpv == null) {
                        cpv = saveObject(1, p.getValue());
                        valueIndexMap.put(p.getValue(), cpv);
                    }

                    final Integer gid = gpi.get(pi);

                    // XXX offset
                    pps.setInt(1, tpindex + 1);
                    // XXX offset
                    pps.setInt(2, gid + 1);
                    // XXX offset
                    pps.setInt(3, ti + 1);

                    // find index for the parameter's namespace
                    TableNamespace tn = p.getNamespace();
                    Integer ni = null;
                    if (tn != null) {
                        ni = nt.getNamespaceIndex().get(tn);
                    }

                    if (ni == null) {
                        pps.setNull(4, Types.INTEGER);
                    } else {
                        // XXX offset
                        pps.setInt(4, ni + 1);
                    }

                    pps.setInt(5, cpv);
                    pps.setInt(6, ord);
                    pps.addBatch();

                    ord++;
                    tpindex++;

                    if (seenGlobalIds.contains(gid)) {
                        continue;
                    }
                    SkinnyUUID uuid = guu.get(gid);
                    if (uuid != null) {
                        // XXX offset
                        knups.setInt(1, gid + 1);
                        knups.setLong(2, uuid.getMostSignificantBits());
                        knups.setLong(3, uuid.getLeastSignificantBits());
                        knups.addBatch();
                    }
                    seenGlobalIds.add(gid);
                }
            }
        }

        tps.executeBatch();
        pps.executeBatch();
        knups.executeBatch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadEdges(StatementTable st, TermTable tt, ProtoNodeTable pnt,
            ProtoEdgeTable pet)
            throws SQLException {
        // load kam edges and associate to kam nodes (global terms)
        PreparedStatement keps = getPreparedStatement(KAM_EDGE_SQL);

        final Map<Integer, Integer> eqn = pnt.getEquivalences();
        final Map<Integer, Integer> eqs = pet.getEquivalences();
        final List<TableProtoEdge> edges = pet.getProtoEdges();
        Set<Integer> added = new HashSet<Integer>();

        for (int i = 0, n = edges.size(); i < n; i++) {
            final Integer eqId = eqs.get(i);

            // continue if we have already seen this equivalent proto edge
            if (added.contains(eqId)) {
                continue;
            }

            added.add(eqId);

            final TableProtoEdge edge = edges.get(i);

            // XXX offset
            keps.setInt(1, eqId + 1);
            // XXX offset
            keps.setInt(2, eqn.get(edge.getSource()) + 1);
            // XXX offset
            keps.setInt(3, eqn.get(edge.getTarget()) + 1);

            RelationshipType r = RelationshipType.getRelationshipType(edge
                    .getRel());
            keps.setInt(4, r.getValue());
            keps.addBatch();
        }
        added.clear();
        keps.executeBatch();

        // load statements
        final List<StatementTable.TableStatement> ts = st.getStatements();
        final Map<Integer, Integer> sdm = st.getStatementDocument();

        PreparedStatement sps = getPreparedStatement(STATEMENT_SQL);
        for (int i = 0, n = ts.size(); i < n; i++) {
            final TableStatement stmt = ts.get(i);

            // XXX offset
            sps.setInt(1, i + 1);
            // XXX offset
            sps.setInt(2, sdm.get(i) + 1);
            // XXX offset
            sps.setInt(3, stmt.getSubjectTermId() + 1);

            if (stmt.getRelationshipName() == null) {
                // load definitional statement
                sps.setNull(4, Types.INTEGER);
                sps.setNull(5, Types.INTEGER);
                sps.setNull(6, Types.INTEGER);
                sps.setNull(7, Types.INTEGER);
                sps.setNull(8, Types.INTEGER);
            } else if (stmt.getObjectTermId() != null) {
                // load simple statement
                RelationshipType r = RelationshipType.getRelationshipType(stmt
                        .getRelationshipName());
                sps.setInt(4, r.getValue());

                // XXX offset
                sps.setInt(5, stmt.getObjectTermId() + 1);
                sps.setNull(6, Types.INTEGER);
                sps.setNull(7, Types.INTEGER);
                sps.setNull(8, Types.INTEGER);
            } else {
                // load nested statement
                RelationshipType r = RelationshipType.getRelationshipType(stmt
                        .getRelationshipName());
                sps.setInt(4, r.getValue());

                // set null for object term since this is a nested statement
                sps.setNull(5, Types.INTEGER);

                // XXX offset
                sps.setInt(6, stmt.getNestedSubject() + 1);

                RelationshipType nr = RelationshipType.getRelationshipType(stmt
                        .getNestedRelationship());

                sps.setInt(7, nr.getValue());

                // XXX offset
                sps.setInt(8, stmt.getNestedObject() + 1);
            }
            sps.addBatch();
        }

        sps.executeBatch();

        // load many-to-many association of edges to statements
        PreparedStatement skes = getPreparedStatement(KAM_EDGE_STATEMENT_SQL);
        final Map<Integer, Set<Integer>> edgeStmts = pet.getEdgeStatements();

        // collect all non-unique kam edge / statement combinations
        final Map<Integer, Set<Integer>> edgeMap =
                new HashMap<Integer, Set<Integer>>();
        for (int i = 0, n = edges.size(); i < n; i++) {
            // lookup individual edge statements and the equivalent edge id
            final Integer edgeId = i;
            Set<Integer> es = edgeStmts.get(edgeId);
            final Integer eqEdgeId = pet.getEquivalences().get(edgeId);

            // create statement set for equivalent edge
            Set<Integer> stmts = edgeMap.get(eqEdgeId);
            if (stmts == null) {
                stmts = new HashSet<Integer>();
                edgeMap.put(eqEdgeId, stmts);
            }

            // map statements (from individual edges) to the equivalent
            if (hasItems(es)) {
                stmts.addAll(es);
            }
        }

        // iterate kam edges, insert all unique edge / stmt mappings
        Set<Entry<Integer, Set<Integer>>> edgeMapEntries = edgeMap.entrySet();
        for (Map.Entry<Integer, Set<Integer>> e : edgeMapEntries) {
            // XXX offset
            int edgeId = e.getKey() + 1;
            Set<Integer> stmts = e.getValue();

            for (final Integer stmt : stmts) {
                skes.setInt(1, edgeId);
                // XXX offset
                skes.setInt(2, stmt + 1);
                skes.addBatch();
            }
        }

        skes.executeBatch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadAnnotationDefinitions(AnnotationDefinitionTable adt)
            throws SQLException {
        PreparedStatement adps =
                getPreparedStatement(ANNOTATION_DEFINITION_SQL);

        for (Map.Entry<Integer, TableAnnotationDefinition> ade : adt
                .getIndexDefinition().entrySet()) {
            TableAnnotationDefinition ad = ade.getValue();
            adps.setInt(1, (ade.getKey() + 1));
            adps.setString(2, ad.getName());

            if (AnnotationDefinitionTable.URL_ANNOTATION_TYPE_ID == ad
                    .getAnnotationType()) {
                adps.setNull(3, Types.VARCHAR);
                adps.setNull(4, Types.VARCHAR);
            } else {
                adps.setString(3, StringUtils.abbreviate(ad.getDescription(),
                        MAX_MEDIUM_VARCHAR_LENGTH));
                adps.setString(4, StringUtils.abbreviate(ad.getUsage(),
                        MAX_MEDIUM_VARCHAR_LENGTH));
            }

            final int oid;
            final String domain = ad.getAnnotationDomain();
            final Integer objectId = valueIndexMap.get(domain);
            if (objectId != null) {
                oid = objectId;
            } else {
                oid = saveObject(1, domain);
                valueIndexMap.put(domain, oid);
            }

            adps.setInt(5, oid);
            adps.setInt(6, ad.getAnnotationType());
            adps.addBatch();
        }

        adps.executeBatch();

        // associate annotate definitions to documents
        PreparedStatement dadmps =
                getPreparedStatement(DOCUMENT_ANNOTATION_DEFINITION_MAP_SQL);
        Map<Integer, Set<Integer>> dadm = adt
                .getDocumentAnnotationDefinitions();

        Set<Entry<Integer, Set<Integer>>> entries = dadm.entrySet();
        for (final Entry<Integer, Set<Integer>> entry : entries) {
            final Integer key = entry.getKey();
            for (final Integer adid : entry.getValue()) {
                dadmps.setInt(1, (key + 1));
                dadmps.setInt(2, (adid + 1));
                dadmps.addBatch();
            }
            dadmps.executeBatch();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadAnnotationValues(AnnotationValueTable avt)
            throws SQLException {
        PreparedStatement aps = getPreparedStatement(ANNOTATION_SQL);
        Set<Entry<Integer, TableAnnotationValue>> annotationEntries = avt
                .getIndexValue().entrySet();
        for (Entry<Integer, TableAnnotationValue> annotationEntry : annotationEntries) {
            aps.setInt(1, (annotationEntry.getKey() + 1));

            TableAnnotationValue tableValue = annotationEntry.getValue();
            String value = tableValue.getAnnotationValue();

            int oid;
            Integer objectId = valueIndexMap.get(value);
            if (objectId != null) {
                oid = objectId;
            } else {
                oid = saveObject(1, value);
                valueIndexMap.put(value, oid);
            }

            aps.setInt(2, oid);
            aps.setInt(3, (tableValue.getAnnotationDefinitionId() + 1));
            aps.addBatch();
        }

        aps.executeBatch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadStatementAnnotationMap(StatementAnnotationMapTable samt)
            throws SQLException {
        Map<Integer, Set<AnnotationPair>> sidAnnotationIndex =
                samt.getStatementAnnotationPairsIndex();

        PreparedStatement saps = getPreparedStatement(STATEMENT_ANNOTATION_SQL);

        final Set<Entry<Integer, Set<AnnotationPair>>> entries =
                sidAnnotationIndex.entrySet();
        for (final Entry<Integer, Set<AnnotationPair>> entry : entries) {
            final Integer sid = entry.getKey();
            for (final AnnotationPair annotation : entry.getValue()) {
                // XXX offset
                saps.setInt(1, sid + 1);
                // XXX offset
                saps.setInt(2, annotation.getAnnotationValueId() + 1);
                saps.addBatch();
            }
        }
        saps.executeBatch();
    }

    /**
     * Saves an entry to the object table.
     *
     * @param tid {@code int}, the object type id
     * @param v {@link String}, the non-null object value
     * @return {@code int}, the object primary key
     * @throws SQLException - Thrown if a sql error occurred saving an entry to
     * the object table
     */
    protected int saveObject(int tid, String v) throws SQLException {
        final String objectsIdColumn = (dbConnection.isPostgresql() ?
                OBJECTS_ID_COLUMN_POSTGRESQL : OBJECTS_ID_COLUMN);
        PreparedStatement ps = getPreparedStatement(OBJECTS_SQL,
                new String[] { objectsIdColumn });
        ResultSet rs = null;

        if (v == null) {
            throw new InvalidArgument("object value cannot be null");
        }

        try {
            // Insert into objects_text if we are over MAX_VARCHAR_LENGTH
            Integer objectsTextId = null;
            if (v.length() > MAX_VARCHAR_LENGTH) {
                final String objectsTextColumn = (dbConnection.isPostgresql() ?
                        OBJECTS_TEXT_COLUMN_POSTGRESQL : OBJECTS_TEXT_COLUMN);
                PreparedStatement otps = getPreparedStatement(OBJECTS_TEXT_SQL,
                        new String[] { objectsTextColumn });
                ResultSet otrs = null;
                StringReader sr = null;

                try {
                    sr = new StringReader(v);
                    otps.setClob(1, sr, v.length());
                    otps.execute();
                    otrs = otps.getGeneratedKeys();
                    if (otrs.next()) {
                        objectsTextId = otrs.getInt(1);
                    }
                } finally {
                    close(otrs);

                    if (sr != null) {
                        sr.close();
                    }
                }
            }

            // FIXME Hardcoding objects_type to 1?
            ps.setInt(1, 1);

            if (objectsTextId == null) {
                // insert value into objects table
                ps.setString(2, v);
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setNull(2, Types.VARCHAR);
                ps.setInt(3, objectsTextId);
            }

            ps.execute();
            rs = ps.getGeneratedKeys();
            int oid;
            if (rs.next()) {
                oid = rs.getInt(1);
            } else {
                throw new IllegalStateException("object insert failed.");
            }
            return oid;
        } finally {
            close(rs);

        }
    }
}
