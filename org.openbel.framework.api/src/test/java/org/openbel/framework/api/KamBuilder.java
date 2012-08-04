package org.openbel.framework.api;

import static org.openbel.framework.common.BELUtilities.hasItems;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openbel.framework.api.KamTestUtil.TestKamEdge;
import org.openbel.framework.api.KamTestUtil.TestKamNode;
import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.common.bel.parser.BELParser;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Term;

public class KamBuilder {
    private KamInfo kamInfo;
    private final Map<String, TestKamNode> knodes;
    private final Map<String, TestKamEdge> kedges;
    private final boolean usingLongForm;

    public KamBuilder(final KamInfo kamInfo, final boolean usingLongForm) {
        this.kamInfo = kamInfo;
        this.knodes = new LinkedHashMap<String, TestKamNode>();
        this.kedges = new LinkedHashMap<String, TestKamEdge>();
        this.usingLongForm = usingLongForm;
    }

    public KamBuilder addNodes(final String... nodes) {
        if (hasItems(nodes)) {
            for (final String n : nodes) {
                final Term term = BELParser.parseTerm(n);
                final String bel = getBEL(term);
                knodes.put(bel, new TestKamNode(knodes.size(), term
                        .getFunctionEnum(), bel));
            }
        }

        return this;
    }

    public KamBuilder addEdges(final Edge... edges) {
        if (hasItems(edges)) {
            for (final Edge e : edges) {
                final TestKamNode subjectNode = knodes.get(e.subject);
                if (subjectNode == null) {
                    throw new IllegalStateException("Subject node does not exist, error on: " + e);
                }
                final TestKamNode objectNode = knodes.get(e.object);
                if (objectNode == null) {
                    throw new IllegalStateException("Object node does not exist, error on: " + e);
                }

                String bel = e.subject + " " + e.rel + " " + e.object;
                kedges.put(bel, new TestKamEdge(kedges.size(), subjectNode,
                        e.rel, objectNode));
            }
        }

        return this;
    }

    public Kam create() {
        Collection<TestKamNode> nodes = knodes.values();
        Collection<TestKamEdge> edges = kedges.values();
        return KamTestUtil.createKam(kamInfo,
                nodes.toArray(new TestKamNode[nodes.size()]),
                edges.toArray(new TestKamEdge[edges.size()]));
    }

    public KamBuilder clear() {
        knodes.clear();
        kedges.clear();

        return this;
    }

    public static Edge edge(final String subject, final RelationshipType rel, final String object) {
        return new Edge(subject, rel, object);
    }

    private String getBEL(final BELObject o) {
        return usingLongForm ? o.toBELLongForm() : o.toBELShortForm();
    }

    public static final class Edge {
        private final String subject;
        private final RelationshipType rel;
        private final String object;

        public Edge(String subject, RelationshipType rel, String object) {
            this.subject = subject;
            this.rel = rel;
            this.object = object;
        }

        public String getSubject() {
            return subject;
        }

        public RelationshipType getRel() {
            return rel;
        }

        public String getObject() {
            return object;
        }
    }
}
