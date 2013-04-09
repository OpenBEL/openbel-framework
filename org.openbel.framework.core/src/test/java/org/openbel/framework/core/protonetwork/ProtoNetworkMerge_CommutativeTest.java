package org.openbel.framework.core.protonetwork;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.openbel.framework.common.bel.parser.BELParser;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.Header;
import org.openbel.framework.common.model.NamespaceGroup;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.StatementGroup;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.core.compiler.expansion.ExpansionService;
import org.openbel.framework.core.compiler.expansion.ExpansionServiceImpl;

/**
 * Unit test for {@link ProtoNetworkMerger}.  This unit test's purpose is to
 * exercise the merge routines for completeness and correctness.
 *
 * <p>
 * Created to assert the commutative property of network merges holds true.
 * </p>
 *
 * @see https://github.com/OpenBEL/openbel-framework/issues/49
 */
public class ProtoNetworkMerge_CommutativeTest {

    private static final String URL = "http://resource.belframework.org/" +
    		"belframework/1.0/namespace/hgnc-approved-symbols.belns";

    @Test
    /**
     * Tests that statement triples in two {@link Document documents} will
     * produce the same merged result regardless of document sequence.
     */
    public void commutative_simple() {
        Document a = make_document(new Header("Doc 1", "Doc 1", "1.0"),
                new NamespaceGroup(URL, null),
                parse("g(AKT2) -> r(AKT3)"),
                parse("p(AKT1) => complex(p(DUSP1), p(WRN))"));
        Document b = make_document(new Header("Doc 2", "Doc 2", "1.0"),
                new NamespaceGroup(URL, null),
                parse("m(AKT1) =| p(AKT2)"),
                parse("cat(g(AKT2)) -| p(PARP1)"));

        // merge in different orders
        ProtoNetwork ab = merge(a, b);
        ProtoNetwork ba = merge(b, a);

        network_asserts(ab, ba);
    }

    @Test
    /**
     * Tests that subject-only statements in two {@link Document documents}
     * will produce the same merged result regardless of document sequence.
     */
    public void commutative_subject_only() {
        Document a = make_document(new Header("Doc 1", "Doc 1", "1.0"),
                new NamespaceGroup(URL, null),
                parse("r(AKT3)"),
                parse("p(AKT1)"));
        Document b = make_document(new Header("Doc 2", "Doc 2", "1.0"),
                new NamespaceGroup(URL, null),
                parse("m(AKT1)"),
                parse("g(AKT2)"));

        // merge in different orders
        ProtoNetwork ab = merge(a, b);
        ProtoNetwork ba = merge(b, a);

        network_asserts(ab, ba);
    }

    @Test
    /**
     * Tests that nested statements in two {@link Document documents} will
     * produce the same merged result regardless of document sequence.
     */
    public void commutative_nested() {
        Document a = make_document(new Header("Doc 1", "Doc 1", "1.0"),
                new NamespaceGroup(URL, null),
                parse("r(AKT3) => (g(PARP1) -| cat(p(AKT2)))"),
                parse("p(AKT1) -> (kin(p(PARP1)) -| p(AKT3))"));
        Document b = make_document(new Header("Doc 2", "Doc 2", "1.0"),
                new NamespaceGroup(URL, null),
                parse("m(AKT1) => (g(PARP1) -| p(AKT2))"),
                parse("g(AKT2) -> (r(AKT1) => p(PARP1))"));

        // merge in different orders
        ProtoNetwork ab = merge(a, b);
        ProtoNetwork ba = merge(b, a);

        network_asserts(ab, ba);
    }

    /**
     * Asserts equal table counts.  The {@link ProtoNetwork} objects cannot be
     * compared directly because {@link ProtoNetwork#equals(Object)} relies on
     * collection ordering which will not be consistent when {@link Document}s
     * are merged in different sequences.
     *
     * @param ab {@link ProtoNetwork}
     * @param ba {@link ProtoNetwork}
     */
    private void network_asserts(ProtoNetwork ab, ProtoNetwork ba) {
        assertThat(ab.getProtoEdgeTable().getProtoEdges().size(), is(ba
                .getProtoEdgeTable().getProtoEdges().size()));
        assertThat(ab.getProtoNodeTable().getProtoNodes().size(), is(ba
                .getProtoNodeTable().getProtoNodes().size()));
        assertThat(ab.getStatementTable().getStatements().size(), is(ba
                .getStatementTable().getStatements().size()));
        assertThat(ab.getTermTable().getTermValues().size(), is(ba
                .getTermTable().getTermValues().size()));
        assertThat(ab.getParameterTable().getTableParameterArray().length,
                is(ba.getParameterTable().getTableParameterArray().length));
    }

    /**
     * Merges two {@link Document}s together after each has been compiled.
     * The compilation step builds the {@link ProtoNetwork} and expands
     * additional knowledge.
     *
     * @param a {@link Document}
     * @param b {@link Document}
     * @return merged {@link ProtoNetwork}
     */
    private ProtoNetwork merge(Document a, Document b) {
        ExpansionService es = new ExpansionServiceImpl();
        ProtoNetworkBuilder builder = new ProtoNetworkBuilder(a);
        ProtoNetwork networkA = builder.buildProtoNetwork();
        es.expandTerms(a, networkA);
        builder = new ProtoNetworkBuilder(b);
        ProtoNetwork n2 = builder.buildProtoNetwork();
        es.expandTerms(b, n2);
        ProtoNetworkMerger merger = new ProtoNetworkMerger();
        merger.merge(networkA, n2);
        return networkA;
    }

    /**
     * Build a {@link Document} for testing.
     *
     * @param h {@link Header}; document header
     * @param n {@link NamespaceGroup}; namespace group
     * @param statements {@link Statement}; bel statements
     * @return {@link Document}
     */
    private Document make_document(Header h, NamespaceGroup n, Statement... statements) {
        StatementGroup g = new StatementGroup();
        g.setStatements(Arrays.asList(statements));
        return new Document(h, Arrays.asList(g), n, null);
    }

    /**
     * Parses a {@link String string} into a {@link Statement bel statement}.
     *
     * @param statement {@link String}
     * @return {@link Statement}
     */
    private Statement parse(String statement) {
        return BELParser.parseStatement(statement);
    }
}
