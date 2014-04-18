package org.openbel.framework.api.internal;

import org.openbel.framework.api.AnnotationFilterCriteria;
import org.openbel.framework.api.FilterCriteria;
import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.common.bel.parser.BELParser;
import org.openbel.framework.common.model.*;
import org.openbel.framework.core.df.AbstractJdbcDAO;
import org.openbel.framework.core.df.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Arrays.asList;
import static org.openbel.framework.common.BELUtilities.sizedHashMap;
import static org.openbel.framework.common.enums.CitationType.fromString;

/**
 * Implements a {@link KAMDao} that retrieves BEL evidence for one or more
 * edges.
 *
 * <p>
 * This implementation is in place to avoid the heavy use of caching in
 * {@link org.openbel.framework.api.internal.KAMStoreDaoImpl}.
 */
public class EvidenceDaoImpl extends AbstractJdbcDAO implements EvidenceDao {

    private static final String SQL =
            "select s.statement_id, s.bel_statement, ad.name, o.varchar_value " +
            "from @.kam_edge ke INNER JOIN @.kam_edge_statement_map kesm ON ke.kam_edge_id = kesm.kam_edge_id " +
            "INNER JOIN @.statement s ON kesm.statement_id = s.statement_id " +
            "LEFT JOIN @.statement_annotation_map sam ON s.statement_id = sam.statement_id " +
            "LEFT JOIN @.annotation a ON sam.annotation_id = a.annotation_id " +
            "LEFT JOIN @.annotation_definition ad ON a.annotation_definition_id = ad.annotation_definition_id " +
            "LEFT JOIN @.objects o ON a.value_oid = o.objects_id where ke.kam_edge_id = ? order by s.statement_id";
    private static final SimpleDateFormat iso8601DateFormat =
            new SimpleDateFormat("yyyy-MM-dd");

    public EvidenceDaoImpl(DBConnection dbConnection, String schemaName)
            throws SQLException {
        super(dbConnection, schemaName);
    }

    /**
     * {@inheritDoc}
     */
    public Map<KamEdge, List<Statement>>
            evidence(Collection<KamEdge> edges) throws SQLException {
        if (edges == null) throw new IllegalArgumentException("edges is null");

        PreparedStatement ps;
        ResultSet rs = null;
        Map<KamEdge, List<Statement>> results = new HashMap<KamEdge, List<Statement>>();
        try {
            for (KamEdge edge : edges) {
                if (edge == null) continue;

                List<Statement> evidence = new ArrayList<Statement>();
                ps = getPreparedStatement(SQL);
                ps.setInt(1, edge.getId());
                rs = ps.executeQuery();
                int id = -1;
                Statement stmt = null;
                Map<String, String> citation = new HashMap<String, String>();
                while (rs.next()) {
                    int next = rs.getInt(1);
                    if (next != id ) {
                        if (id != -1) {
                            // iterating a different statement,
                            // save previous
                            if (stmt != null) {
                                if (!citation.isEmpty()) {
                                    stmt.getAnnotationGroup().setCitation(fromMap(citation));
                                }

                                evidence.add(stmt);
                                citation.clear();
                            }
                        }
                        id = next;

                        stmt = BELParser.parseStatement(rs.getString(2));
                        if (stmt == null) continue;
                    }

                    String name = rs.getString(3);
                    String value = rs.getString(4);
                    if (name != null)
                        directAnnotation(name, value, stmt, citation);
                }

                // add set of rows for last statement
                if (stmt != null) {
                    if (!citation.isEmpty())
                        stmt.getAnnotationGroup().setCitation(fromMap(citation));
                    evidence.add(stmt);
                    citation.clear();
                }
                close(rs);

                results.put(edge, evidence);
            }
            return results;
        } finally {
            close(rs);
        }
    }

    public Map<KamEdge, List<Statement>> evidence(Collection<KamEdge> edges,
            KAMCatalogDao.AnnotationFilter filter) throws SQLException {
        final Map<KamEdge, List<Statement>> results = evidence(edges);
        if (filter == null) {
            return results;
        }

        final List<FilterCriteria> criteria = filter.getFilterCriteria();
        final Map<KAMStoreDaoImpl.AnnotationType, AnnotationFilterCriteria> amap =
                sizedHashMap(criteria.size());
        for (final FilterCriteria c : criteria) {
            final AnnotationFilterCriteria afc = (AnnotationFilterCriteria) c;
            amap.put(afc.getAnnotationType(), afc);
        }

        for (Collection<Statement> stmts : results.values()) {
            final Iterator<Statement> stmtIt = stmts.iterator();
            while (stmtIt.hasNext()) {
                final Statement stmt = stmtIt.next();

                // guard against empty annotations
                if (stmt == null ||
                        stmt.getAnnotationGroup() == null ||
                        stmt.getAnnotationGroup().getAnnotations() == null)
                    continue;

                final List<Annotation> annotations = stmt.getAnnotationGroup().getAnnotations();
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
                        if (annotation.getDefinition().getId().equals(afc.getAnnotationType().getName())) {
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
        }

        return results;
    }

    private void directAnnotation(String name, String value,
            Statement stmt, Map<String, String> citation) {
        if (stmt.getAnnotationGroup() == null) {
            AnnotationGroup group = new AnnotationGroup();
            group.setAnnotations(new ArrayList<Annotation>());
            stmt.setAnnotationGroup(group);
        }
        if (name.equals("CitationReference") || name.equals("CitationName") ||
                name.equals("CitationType") || name.equals("CitationDate") ||
                name.equals("CitationAuthors") ||
                name.equals("CitationComment")) {
            citation.put(name, value);
        } else {
            Annotation a = new Annotation(value, new AnnotationDefinition(name));
            stmt.getAnnotationGroup().getAnnotations().add(a);
        }
    }

    private Citation fromMap(Map<String, String> m) {
        String name = m.get("CitationName");
        if (name == null) return null;

        Citation c = new Citation(name);
        c.setReference(m.get("CitationReference"));
        c.setType(fromString(m.get("CitationType")));
        String date = m.get("CitationDate");
        if (date != null) {
            try {
                Date d = iso8601DateFormat.parse(date);
                Calendar cal = Calendar.getInstance();
                cal.setTime(d); c.setDate(cal);
            } catch (ParseException e) {
                // invalid date; do not set
            }
        }
        String authors = m.get("CitationAuthors");
        if (authors != null) {
            String[] a = authors.split("\\s*,\\s*");
            c.setAuthors(asList(a));
        }
        c.setComment(m.get("CitationComment"));

        return c;
    }
}
