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
package org.openbel.framework.core.compiler;

import static java.lang.System.err;
import static java.util.Arrays.asList;
import static org.junit.Assert.fail;
import static org.openbel.framework.common.model.CommonModelFactory.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Test;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.enums.ReturnType;
import org.openbel.framework.common.enums.ValueEncoding;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.common.model.Term;
import org.openbel.framework.core.compiler.SemanticServiceImpl;
import org.openbel.framework.core.compiler.SemanticWarning;
import org.openbel.framework.core.compiler.SymbolWarning;
import org.openbel.framework.core.indexer.IndexingFailure;
import org.openbel.framework.core.namespace.NamespaceService;
import org.openbel.framework.core.namespace.NamespaceSyntaxWarning;
import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * Semantic service implementation unit tests.
 */
public class SemanticServiceImplTest {
    private static final SemanticServiceImpl subject =
            new SemanticServiceImpl(new MockLookupService());
    private static final Random random = new Random();

    private static FunctionEnum randomFunctionEnum() {
        final int count = FunctionEnum.values().length;
        return FunctionEnum.values()[random.nextInt(count)];
    }

    @Test(expected = SemanticWarning.class)
    public void testNoArgumentFailure() throws SemanticWarning {
        err.println("testNoArgumentFailure");
        FunctionEnum funcEnum = FunctionEnum.COMPLEX_ABUNDANCE;
        List<BELObject> args = new ArrayList<BELObject>();
        Term t = new Term(funcEnum, args);

        subject.checkParameterizedTerm(t);
        fail("expected semantic failure");
    }

    @Test(expected = SemanticWarning.class)
    public void testMissingFunctionArgumentFailure() throws SemanticWarning {
        err.println("testMissingFunctionArgumentFailure");
        FunctionEnum funcEnum = FunctionEnum.REACTION;
        Term t = new Term(funcEnum);

        subject.checkTerm(t);
        fail("expected semantic failure");
    }

    @Test
    public void testFunctionOnlyArguments() {
        err.println("testFunctionOnlyArguments");
        FunctionEnum funcEnum = FunctionEnum.REACTION;
        List<BELObject> args = new ArrayList<BELObject>();
        args.add(new Term(FunctionEnum.REACTANTS));
        args.add(new Term(FunctionEnum.PRODUCTS));
        Term t = new Term(funcEnum, args);

        try {
            subject.checkTerm(t);
        } catch (SemanticWarning se) {
            fail("unexpected semantic failure: " + se.getUserFacingMessage());
        }
    }

    @Test
    public void testEncodingArgument() {
        err.println("testEncodingArgument");
        FunctionEnum funcEnum = FunctionEnum.ABUNDANCE;
        List<BELObject> args = new ArrayList<BELObject>();
        Parameter abundance = getInstance().createParameter(null, "value");
        args.add(abundance);
        Term t = new Term(funcEnum, args);

        try {
            subject.checkParameterizedTerm(t);
        } catch (SemanticWarning se) {
            fail("unexpected semantic failure: " + se.getUserFacingMessage());
        }
    }

    @Test
    public void testFunctionArgument() {
        err.println("testFunctionArgument");
        FunctionEnum funcEnum = FunctionEnum.CATALYTIC_ACTIVITY;
        List<BELObject> args = new ArrayList<BELObject>();
        Term complexAbundance = new Term(FunctionEnum.COMPLEX_ABUNDANCE);
        args.add(complexAbundance);
        Term t = new Term(funcEnum, args);

        try {
            subject.checkTerm(t);
        } catch (SemanticWarning se) {
            fail("unexpected semantic failure: " + se.getUserFacingMessage());
        }
    }

    @Test
    public void testComplexArgument() {
        err.println("testComplexArgument");
        FunctionEnum funcEnum = FunctionEnum.COMPLEX_ABUNDANCE;
        List<BELObject> args = new ArrayList<BELObject>();
        Parameter parameter = getInstance().createParameter(null, "value");
        args.add(parameter);
        Term t = new Term(funcEnum, args);

        try {
            subject.checkParameterizedTerm(t);
        } catch (SemanticWarning se) {
            fail("unexpected semantic failure: " + se.getUserFacingMessage());
        }

        args.clear();

        int abundances = random.nextInt(10) + 1;
        for (int i = 0; i < abundances; i++) {
            FunctionEnum fe = randomFunctionEnum();
            while (fe.getReturnType() != ReturnType.ABUNDANCE) {
                fe = randomFunctionEnum();
            }
            args.add(new Term(fe));
        }
        t = new Term(funcEnum, args);

        try {
            subject.checkParameterizedTerm(t);
        } catch (SemanticWarning se) {
            fail("unexpected semantic failure: " + se.getUserFacingMessage());
        }
    }

    @Test(expected = SemanticWarning.class)
    public void testMultipleEncodings() throws SemanticWarning {
        err.println("testMultipleEncodings");
        FunctionEnum funcEnum = FunctionEnum.COMPLEX_ABUNDANCE;
        List<BELObject> args = new ArrayList<BELObject>();
        Parameter g1 = getInstance().createParameter(null, "value1");
        Parameter g2 = getInstance().createParameter(null, "value2");
        args.add(g1);
        args.add(g2);

        Term t = new Term(funcEnum, args);

        subject.checkParameterizedTerm(t);
        fail("expected semantic failure");
    }

    @Test(expected = SemanticWarning.class)
    public void testTooManyArgumentsFailure() throws SemanticWarning {
        err.println("testTooManyArgumentsFailure");
        FunctionEnum funcEnum = FunctionEnum.PHOSPHATASE_ACTIVITY;
        List<BELObject> args = new ArrayList<BELObject>();
        Parameter abundance1 = getInstance().createParameter(null, "value1");
        Parameter abundance2 = getInstance().createParameter(null, "value2");
        args.add(abundance1);
        args.add(abundance2);

        Term t = new Term(funcEnum, args);
        subject.checkParameterizedTerm(t);
        fail("expected semantic failure");
    }

    @Test(expected = SemanticWarning.class)
    public void testTooFewArgumentsFailure() throws SemanticWarning {
        err.println("testTooFewArgumentsFailure");
        FunctionEnum funcEnum = FunctionEnum.TRANSLOCATION;
        List<BELObject> args = new ArrayList<BELObject>();

        Term abundance = new Term(FunctionEnum.COMPOSITE_ABUNDANCE);
        args.add(abundance);
        args.add(getInstance().createParameter(null, "value1"));

        Term t = new Term(funcEnum, args);
        subject.checkParameterizedTerm(t);
        fail("expected semantic failure");
    }

    @Test(expected = SemanticWarning.class)
    public void testTicket106_1() throws SemanticWarning {
        err.println("testTicket106_1");
        FunctionEnum funcEnum = FunctionEnum.PROTEIN_ABUNDANCE;
        List<BELObject> args = new ArrayList<BELObject>();

        Parameter arg1 = getInstance().createParameter(null, "value1");
        Term arg2 = new Term(FunctionEnum.SUBSTITUTION);
        Term arg3 = new Term(FunctionEnum.SUBSTITUTION);
        Term arg4 = new Term(FunctionEnum.SUBSTITUTION);
        args.addAll(asList(new BELObject[] { arg1, arg2, arg3, arg4 }));

        Term t = new Term(funcEnum, args);
        subject.checkParameterizedTerm(t);
        fail("expected semantic failure");
    }

    @Test(expected = SemanticWarning.class)
    public void testTicket106_2() throws SemanticWarning {
        err.println("testTicket106_2");
        FunctionEnum funcEnum = FunctionEnum.COMPLEX_ABUNDANCE;
        List<BELObject> args = new ArrayList<BELObject>();

        Term arg1 = new Term(FunctionEnum.ABUNDANCE);
        Term arg2 = new Term(FunctionEnum.BIOLOGICAL_PROCESS);
        args.addAll(asList(new BELObject[] { arg1, arg2 }));

        Term t = new Term(funcEnum, args);
        subject.checkParameterizedTerm(t);
        fail("expected semantic failure");
    }

    private static List<BELObject> asBelObjs(BELObject... args) {
        return Arrays.asList(args);
    }

    @Test
    public void testProperListUsage() {
        err.println("testProperListUsage");

        // Test that SemanticServiceImpl#checkListUsage() does not fail
        // on proper uses of the LIST function type.  The examples here
        // are taken from docs/xbel/corpus/small_corpus.bel.

        final Namespace pfh =
                new Namespace(
                        "PFH",
                        "http://resource.belframework.org/"
                                +
                                "belframework/1.0/namespace/selventa-named-human-protein-families.belns");
        final Namespace hgnc =
                new Namespace(
                        "HGNC",
                        "http://resource.belframework.org/"
                                +
                                "belframework/1.0/namespace/hgnc-approved-symbols.belns");
        final Namespace nch =
                new Namespace(
                        "NCH",
                        "http://resource.belframework.org/"
                                +
                                "belframework/1.0/namespace/selventa-named-human-complexes.belns");

        // Example 1:
        // proteinAbundance(PFH:"RAF Family") hasMembers list(
        //         proteinAbundance(HGNC:ARAF),
        //         proteinAbundance(HGNC:BRAF),
        //         proteinAbundance(HGNC:RAF1))

        Term subject1 = new Term(FunctionEnum.PROTEIN_ABUNDANCE,
                asBelObjs(new Parameter(pfh, "RAF Family")));

        Statement.Object object1 =
                new Statement.Object(new Term(FunctionEnum.LIST, asBelObjs(
                        new Term(FunctionEnum.PROTEIN_ABUNDANCE,
                                asBelObjs(new Parameter(hgnc, "ARAF"))),
                        new Term(FunctionEnum.PROTEIN_ABUNDANCE,
                                asBelObjs(new Parameter(hgnc, "BRAF"))),
                        new Term(FunctionEnum.PROTEIN_ABUNDANCE,
                                asBelObjs(new Parameter(hgnc, "RAF1")))
                        )));

        Statement s1 =
                new Statement(subject1, null, null, object1,
                        RelationshipType.HAS_MEMBERS);

        // Example 2:
        // complexAbundance(NCH:"NADPH Oxidase Complex") hasComponents list(
        //         proteinAbundance(HGNC:NCF1),
        //         proteinAbundance(HGNC:CYBB))

        Term subject2 = new Term(FunctionEnum.COMPLEX_ABUNDANCE,
                asBelObjs(new Parameter(nch, "NADPH Oxidase Complex")));

        Statement.Object object2 =
                new Statement.Object(new Term(FunctionEnum.LIST, asBelObjs(
                        new Term(FunctionEnum.PROTEIN_ABUNDANCE,
                                asBelObjs(new Parameter(hgnc, "NCF1"))),
                        new Term(FunctionEnum.PROTEIN_ABUNDANCE,
                                asBelObjs(new Parameter(hgnc, "CYBB"))))));

        Statement s2 =
                new Statement(subject2, null, null, object2,
                        RelationshipType.HAS_COMPONENTS);

        try {
            subject.checkListUsage(s1, null);
            subject.checkListUsage(s2, null);
        } catch (SemanticWarning se) {
            fail("unexpected semantic failure: " + se.getUserFacingMessage());
        }
    }

    @Test(expected = SemanticWarning.class)
    public void testImproperListUsage() throws SemanticWarning {
        err.println("testImproperListUsage");

        // Test that SemanticServiceImpl#checkListUsage() fails
        // on improper uses of the LIST function type.

        final Namespace meshd =
                new Namespace("MESHD", "http://resource.belframework.org/" +
                        "belframework/1.0/namespace/mesh-diseases.belns");
        final Namespace go =
                new Namespace(
                        "GO",
                        "http://resource.belframework.org/"
                                +
                                "belframework/1.0/namespace/go-biological-processes-names.belns");

        // The example:
        // pathology(MESHD:"Atherosclerosis") positiveCorrelation list(
        //         biologicalProcess(GO:"lipid oxidation"),
        //         biologicalProcess(GO:"protein oxidation"))

        Term stmtSubject = new Term(FunctionEnum.PATHOLOGY,
                asBelObjs(new Parameter(meshd, "Atherosclerosis")));

        Statement.Object stmtObject =
                new Statement.Object(new Term(FunctionEnum.LIST,
                        asBelObjs(
                                new Term(FunctionEnum.BIOLOGICAL_PROCESS,
                                        asBelObjs(new Parameter(go,
                                                "lipid oxidation"))),
                                new Term(FunctionEnum.BIOLOGICAL_PROCESS,
                                        asBelObjs(new Parameter(go,
                                                "protein oxidation"))))));

        Statement s = new Statement(stmtSubject, null, null, stmtObject,
                RelationshipType.POSITIVE_CORRELATION);

        subject.checkListUsage(s, null);
        fail("expected semantic failure");
    }

    private static class MockLookupService implements NamespaceService {
        @Override
        public boolean isOpen(String resourceLocation) {
            return false;
        }

        // overridden
        @Override
        public String lookup(Parameter p) {
            return ValueEncoding.ALL_VALUE_ENCODINGS;
        }

        // no-op
        @Override
        public void compileNamespace(String resourceLocation)
                throws ResourceDownloadError, IndexingFailure {
        }

        @Override
        public void verify(Parameter p) throws NamespaceSyntaxWarning {
        }

        @Override
        public void verify(Document d) throws SymbolWarning {
        }

        @Override
        public void verify(Statement s) throws SymbolWarning, IndexingFailure,
                ResourceDownloadError {
        }

        @Override
        public Set<String> search(String resourceLocation, Pattern pattern)
                throws IndexingFailure, ResourceDownloadError {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
