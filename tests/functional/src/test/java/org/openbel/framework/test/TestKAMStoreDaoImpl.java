package org.openbel.framework.test;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.openbel.framework.api.AnnotationFilterCriteria;
import org.openbel.framework.api.BelDocumentFilterCriteria;
import org.openbel.framework.api.CitationFilterCriteria;
import org.openbel.framework.api.FilterCriteria;
import org.openbel.framework.api.RelationshipTypeFilterCriteria;
import org.openbel.framework.common.enums.CitationType;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.core.df.AbstractJdbcDAO;
import org.openbel.framework.internal.KAMCatalogDao.KamFilter;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.internal.KAMStoreDao.KamProtoNodesAndEdges;
import org.openbel.framework.internal.KAMStoreDaoImpl;
import org.openbel.framework.internal.KAMStoreDaoImpl.AnnotationDefinitionType;
import org.openbel.framework.internal.KAMStoreDaoImpl.AnnotationType;
import org.openbel.framework.internal.KAMStoreDaoImpl.Citation;
import org.openbel.framework.internal.KAMStoreDaoImpl.KamProtoEdge;
import org.openbel.framework.internal.KAMStoreDaoImpl.KamProtoNode;
import org.openbel.framework.test.KAMFilterHelper;

public class TestKAMStoreDaoImpl extends KAMStoreTest {

    private static KAMStoreDaoImpl dao = null;
    private static KAMFilterHelper kamFilterHelper;

    @Before
    public void setup() throws SQLException, NoSuchMethodException,
            SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        setupKamStore(Constants.SMALL_CORPUS_KAM_NAME);

        // Create an instance of the KAMStoreDaoImpl class to test.
        KamInfo kamInfo = testKam.getKamInfo();
        dao = new KAMStoreDaoImpl(kamInfo.getKamDbObject().getSchemaName(), dbc);
        kamFilterHelper = new KAMFilterHelper(dao);

        reflectNecessaryCode();
    }

    @After
    public void teardown() {
        teardownKamStore();
    }

    @Test
    public void testGetKamWithAnnotationFilter() throws SQLException,
            InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        KamInfo kamInfo = testKam.getKamInfo();
        KamFilter filter = kamInfo.createKamFilter();

        AnnotationType[] types = new AnnotationType[] {
                annotationType.newInstance(
                        14, "HemicAndImmuneSystem", null, null, AnnotationDefinitionType.URL,
                        "http://resource.belframework.org/belframework/1.0/annotation/mesh-hemic-and-immune-system.belanno"),
                annotationType.newInstance(
                        9, "UrogenitalSystem", null, null, AnnotationDefinitionType.URL,
                        "http://resource.belframework.org/belframework/1.0/annotation/mesh-urogenital-system.belanno")
        };

        AnnotationFilterCriteria ac1 = new AnnotationFilterCriteria(types[0]),
                ac2 = new AnnotationFilterCriteria(types[1]);

        ac1.add("Hematopoietic Stem Cells");
        ac1.add("Blood");
        ac1.add("Macrophages");
        ac1.add("Monocytes");

        ac2.add("Urinary Bladder");

        ac1.setInclude(true);
        ac2.setInclude(false);

        filter.add(ac1);
        filter.add(ac2);

        testSameResultAsPreviousImplementation(filter);
    }

    @Test
    public void testGetKamWithOppositeFiltersReturnsNothing()
            throws SQLException {
        KamInfo kamInfo = testKam.getKamInfo();
        KamFilter kamFilter = kamInfo.createKamFilter();

        RelationshipTypeFilterCriteria c1 = new RelationshipTypeFilterCriteria(),
                c2 = new RelationshipTypeFilterCriteria();
        c1.add(RelationshipType.INCREASES);
        c2.add(RelationshipType.DECREASES);
        kamFilter.add(c1);
        kamFilter.add(c2);

        KamProtoNodesAndEdges all = dao.getKamProtoNodesAndEdges(kamInfo, kamFilter);
        Map<Integer, KamProtoNode> nodes = all.getKamProtoNodes();
        Map<Integer, KamProtoEdge> edges = all.getKamProtoEdges();

        // The filtered KAM should contain nothing because opposite filters were applied.
        assertEquals(0, nodes.size());
        assertEquals(0, edges.size());
    }

    @Test
    public void testGetKamWithDuplicateFiltersIsIdempotent()
            throws SQLException {

        RelationshipTypeFilterCriteria c = new RelationshipTypeFilterCriteria();
        c.add(RelationshipType.HAS_COMPONENT);

        testIdempotency(c);
    }

    @Test
    public void testGetKamIsCommutative() throws SQLException {
        RelationshipTypeFilterCriteria c1 = new RelationshipTypeFilterCriteria(),
                c2 = new RelationshipTypeFilterCriteria();
        c1.add(RelationshipType.INCREASES);
        c2.add(RelationshipType.DECREASES);
        c2.setInclude(false);

        testCommutativity(c1, c2);
    }

    @Test
    public void testGetKamIsCorrect() throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            InstantiationException, SQLException, ParseException {
        KamFilter f = testKam.getKamInfo().createKamFilter();

        RelationshipTypeFilterCriteria r = new RelationshipTypeFilterCriteria();
        r.add(RelationshipType.ANALOGOUS);
        r.add(RelationshipType.INCLUDES);
        r.add(RelationshipType.INCREASES);
        r.setInclude(false);

        CitationFilterCriteria c = new CitationFilterCriteria();
        Citation[] citations = new Citation[] {
                citation.newInstance(
                        "Trends in molecular medicine", "12928037", "", null,
                        Arrays.asList(StringUtils.split("de Nigris F|Lerman A|Ignarro LJ|Williams-Ignarro S|Sica V|Baker AH|Lerman LO|Geng YJ|Napoli C", '|')),
                        CitationType.PUBMED),
                citation.newInstance(
                        "Cell", "16962653", "", dateFormat.parse("2006-10-07"),
                        Arrays.asList(StringUtils.split("Jacinto E|Facchinetti V|Liu D|Soto N|Wei S|Jung SY|Huang Q|Qin J|Su B", '|')),
                        CitationType.PUBMED),
                citation.newInstance(
                        "Trends in molecular medicine", "15350900", "", null,
                        Arrays.asList(StringUtils.split("Barry RE|Krek W", '|')),
                        CitationType.PUBMED),
                citation.newInstance(
                        "Journal of clinical oncology : official journal of the American Society of Clinical Oncology",
                        "16170185", "", null,
                        Arrays.asList(StringUtils.split("Beeram M|Patnaik A|Rowinsky EK", '|')),
                        CitationType.PUBMED),
                citation.newInstance(
                        "The Biochemical journal", "12444918", "", null,
                        Arrays.asList(StringUtils.split("Houslay MD|Adams DR", '|')),
                        CitationType.PUBMED)
        };
        for (Citation cit : citations) {
            c.add(cit);
        }

        f.add(r);
        f.add(c);
        testSameResultAsPreviousImplementation(f);
    }

    private static <T> void assertSame(Map<Integer, T> m1, Map<Integer, T> m2) {
        assertEquals(m1.size(), m2.size());
        assertTrue(m1.keySet().containsAll(m2.keySet()));
        for (Integer key : m1.keySet()) {
            assertEquals(m1.get(key), m2.get(key));
        }
    }

    private static void addFilterCriteria(KamFilter f, FilterCriteria c) {
        if (c instanceof CitationFilterCriteria) {
            f.add((CitationFilterCriteria) c);
        } else if (c instanceof AnnotationFilterCriteria) {
            f.add((AnnotationFilterCriteria) c);
        } else if (c instanceof RelationshipTypeFilterCriteria) {
            f.add((RelationshipTypeFilterCriteria) c);
        } else if (c instanceof BelDocumentFilterCriteria) {
            f.add((BelDocumentFilterCriteria) c);
        }
    }

    private void testIdempotency(FilterCriteria c) throws SQLException {
        KamInfo kamInfo = testKam.getKamInfo();
        KamFilter f = kamInfo.createKamFilter(),
                f2 = kamInfo.createKamFilter();

        addFilterCriteria(f, c);
        addFilterCriteria(f2, c);
        addFilterCriteria(f2, c);

        KamProtoNodesAndEdges all_f = dao.getKamProtoNodesAndEdges(kamInfo, f),
                all_f2 = dao.getKamProtoNodesAndEdges(kamInfo, f2);
        Map<Integer, KamProtoNode> nodes_f = all_f.getKamProtoNodes(),
                nodes_f2 = all_f2.getKamProtoNodes();
        Map<Integer, KamProtoEdge> edges_f = all_f.getKamProtoEdges(),
                edges_f2 = all_f2.getKamProtoEdges();

        assertSame(nodes_f, nodes_f2);
        assertSame(edges_f, edges_f2);
    }

    private void testCommutativity(FilterCriteria a, FilterCriteria b)
            throws SQLException {
        KamInfo kamInfo = testKam.getKamInfo();
        KamFilter ab = kamInfo.createKamFilter(),
                ba = kamInfo.createKamFilter();

        addFilterCriteria(ab, a);
        addFilterCriteria(ab, b);
        addFilterCriteria(ba, b);
        addFilterCriteria(ba, a);

        KamProtoNodesAndEdges all_ab = dao.getKamProtoNodesAndEdges(kamInfo, ab),
                all_ba = dao.getKamProtoNodesAndEdges(kamInfo, ba);
        Map<Integer, KamProtoNode> nodes_ab = all_ab.getKamProtoNodes(),
                nodes_ba = all_ba.getKamProtoNodes();
        Map<Integer, KamProtoEdge> edges_ab = all_ab.getKamProtoEdges(),
                edges_ba = all_ba.getKamProtoEdges();

        assertSame(nodes_ab, nodes_ba);
        assertSame(edges_ab, edges_ba);
    }

    private void testSameResultAsPreviousImplementation(KamFilter kamFilter)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, InstantiationException, SQLException {

        // Test against the previous implementation.
        KamInfo kamInfo = testKam.getKamInfo();

        Map<Integer, KamProtoNode> prevNodes = getKamProtoNodes(kamInfo, kamFilter);
        Map<Integer, KamProtoEdge> prevEdges = getKamProtoEdges(kamInfo, prevNodes, kamFilter);

        KamProtoNodesAndEdges current = dao.getKamProtoNodesAndEdges(kamInfo, kamFilter);
        Map<Integer, KamProtoNode> currentNodes = current.getKamProtoNodes();
        Map<Integer, KamProtoEdge> currentEdges = current.getKamProtoEdges();

        assertSame(prevNodes, currentNodes);
        assertSame(prevEdges, currentEdges);
    }

    private static Method getPreparedStatement = null, getPreparedStatement2 = null,
            close = null, getKamProtoNode = null;
    private static Constructor<KamProtoEdge> kamProtoEdge = null;
    private static Constructor<Citation> citation = null;
    private static Constructor<AnnotationType> annotationType = null;
    private static SimpleDateFormat dateFormat = null;
    private static String SELECT_PROTO_EDGES_SQL = null;
    private static String SELECT_PROTO_NODES_SQL = null;
    private static String SELECT_KAM_NODE_PARAMETERS_PREFIX_SQL = null;
    private static String SELECT_KAM_NODE_PARAMETERS_ORDER_SQL = null;

    private static void reflectNecessaryCode() throws NoSuchMethodException,
            SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        getPreparedStatement = AbstractJdbcDAO.class.getDeclaredMethod("getPreparedStatement", String.class);
        getPreparedStatement.setAccessible(true);

        getPreparedStatement2 = AbstractJdbcDAO.class.getDeclaredMethod(
                "getPreparedStatement", String.class, int.class, int.class);
        getPreparedStatement2.setAccessible(true);

        close = AbstractJdbcDAO.class.getDeclaredMethod("close", ResultSet.class);
        close.setAccessible(true);

        kamProtoEdge = KamProtoEdge.class.getDeclaredConstructor(
                KAMStoreDaoImpl.class, Integer.class, KamProtoNode.class, RelationshipType.class, KamProtoNode.class);
        kamProtoEdge.setAccessible(true);

        citation = Citation.class.getDeclaredConstructor(
                String.class, String.class, String.class, Date.class, List.class, CitationType.class);
        citation.setAccessible(true);

        annotationType = AnnotationType.class.getDeclaredConstructor(
                Integer.class, String.class, String.class, String.class, AnnotationDefinitionType.class, String.class);
        annotationType.setAccessible(true);

        getKamProtoNode = KAMStoreDaoImpl.class.getDeclaredMethod("getKamProtoNode",
                ResultSet.class, ResultSet.class);
        getKamProtoNode.setAccessible(true);

        final Field dateFormatField = KAMStoreDaoImpl.class.getDeclaredField("dateFormat");
        dateFormatField.setAccessible(true);
        dateFormat = (SimpleDateFormat) dateFormatField.get(null);

        final Field selectProtoEdgesSqlField =
                KAMStoreDaoImpl.class.getDeclaredField("SELECT_PROTO_EDGES_SQL");
        selectProtoEdgesSqlField.setAccessible(true);
        SELECT_PROTO_EDGES_SQL = (String) selectProtoEdgesSqlField.get(null);

        final Field selectProtoNodesSqlField =
                KAMStoreDaoImpl.class.getDeclaredField("SELECT_PROTO_NODES_SQL");
        selectProtoNodesSqlField.setAccessible(true);
        SELECT_PROTO_NODES_SQL = (String) selectProtoNodesSqlField.get(null);

        final Field selectKamNodeParametersPrefixSqlField =
                KAMStoreDaoImpl.class.getDeclaredField("SELECT_KAM_NODE_PARAMETERS_PREFIX_SQL");
        selectKamNodeParametersPrefixSqlField.setAccessible(true);
        SELECT_KAM_NODE_PARAMETERS_PREFIX_SQL = (String) selectKamNodeParametersPrefixSqlField.get(null);

        final Field selectKamNodeParametersOrderSqlField =
                KAMStoreDaoImpl.class.getDeclaredField("SELECT_KAM_NODE_PARAMETERS_ORDER_SQL");
        selectKamNodeParametersOrderSqlField.setAccessible(true);
        SELECT_KAM_NODE_PARAMETERS_ORDER_SQL = (String) selectKamNodeParametersOrderSqlField.get(null);
    }

    public Map<Integer, KamProtoEdge> getKamProtoEdges(
            @SuppressWarnings("unused") KamInfo kamInfo,
            Map<Integer, KamProtoNode> kamProtoNodeMap, KamFilter kamFilter)
            throws SQLException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            InstantiationException {
        Map<Integer, KamProtoEdge> map = new HashMap<Integer, KamProtoEdge>();
        ResultSet rset = null;

        try {
            PreparedStatement ps = (PreparedStatement) getPreparedStatement.invoke(
                    dao, SELECT_PROTO_EDGES_SQL);
            rset = ps.executeQuery();

            while (rset.next()) {
                Integer kamEdgeId = rset.getInt(1);
                KamProtoNode sourceKamProtoNode = kamProtoNodeMap.get(rset.getInt(2));
                Integer relationshipTypeId = rset.getInt(3);
                KamProtoNode targetKamProtoNode = kamProtoNodeMap.get(rset.getInt(4));

                // Sanity checks
                if (null == sourceKamProtoNode) {
                    throw new SQLException(String.format("Source node for edge %d is missing.", kamEdgeId));
                }
                if (null == targetKamProtoNode) {
                    throw new SQLException(String.format("Target node for edge %d is missing.", kamEdgeId));
                }

                map.put(kamEdgeId, kamProtoEdge.newInstance(dao, kamEdgeId, sourceKamProtoNode, RelationshipType.fromValue(relationshipTypeId), targetKamProtoNode));
            }

            //filter edges if a kam filter is specified
            if( kamFilter != null ) {
                kamFilterHelper.filter(kamProtoNodeMap, map, kamFilter);
            }
        } finally {
            close.invoke(dao, rset);
        }

        return map;
    }

    public Map<Integer, KamProtoNode> getKamProtoNodes(
            @SuppressWarnings("unused") KamInfo kamInfo,
            @SuppressWarnings("unused") KamFilter kamFilter)
            throws SQLException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        Map<Integer, KamProtoNode> map = new HashMap<Integer, KamProtoNode>();

        // FIXME: don't ignore 'kamFilter'

        ResultSet nodesRs = null;
        ResultSet paramsRs = null;
        try {
            PreparedStatement nps = (PreparedStatement) getPreparedStatement.invoke(dao, SELECT_PROTO_NODES_SQL);
            nodesRs = nps.executeQuery();

            PreparedStatement pps = (PreparedStatement) getPreparedStatement2.invoke(dao,
                    SELECT_KAM_NODE_PARAMETERS_PREFIX_SQL
                    + SELECT_KAM_NODE_PARAMETERS_ORDER_SQL,
                    TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY);
            paramsRs = pps.executeQuery();

            while (nodesRs.next()) {
                KamProtoNode kamProtoNode = (KamProtoNode) getKamProtoNode.invoke(
                        dao, nodesRs, paramsRs);
                map.put(kamProtoNode.getId(), kamProtoNode);
            }
        } finally {
            close.invoke(dao, nodesRs);
            close.invoke(dao, paramsRs);
        }

        return map;
    }
}
