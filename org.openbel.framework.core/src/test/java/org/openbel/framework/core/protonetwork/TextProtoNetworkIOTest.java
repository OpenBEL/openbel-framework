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
package org.openbel.framework.core.protonetwork;

import static java.util.Arrays.asList;
import static junit.framework.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.model.*;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.core.protonetwork.ProtoNetworkBuilder;
import org.openbel.framework.core.protonetwork.ProtoNetworkDescriptor;
import org.openbel.framework.core.protonetwork.ProtoNetworkExternalizer;
import org.openbel.framework.core.protonetwork.TextProtoNetworkDescriptor;
import org.openbel.framework.core.protonetwork.TextProtoNetworkExternalizer;

/**
 * TextProtoNetworkIOTest tests the reading and writing of a text table-based
 * {@link ProtoNetwork}.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class TextProtoNetworkIOTest {
    /**
     * Defines the test {@link ProtoNetwork}.
     */
    private ProtoNetwork protoNetwork;

    /**
     * Defines the test {@link TextProtoNetworkDescriptor}. This must be cleaned
     * up after each test runs.
     */
    private TextProtoNetworkDescriptor protoNetworkDescriptor;

    /**
     * Testcase - Dummy test to see output of text table-based
     * {@link ProtoNetwork}.
     */
    @Test
    public void testProtoNetworkIO() {
        ProtoNetworkExternalizer protoNetworkExternalizer =
                new TextProtoNetworkExternalizer();

        try {
            protoNetworkDescriptor =
                    (TextProtoNetworkDescriptor) protoNetworkExternalizer
                            .writeProtoNetwork(protoNetwork,
                                    System.getProperty("user.dir"));

            for (final File symt : protoNetworkDescriptor.getSymbolTables()) {
                FileUtils.readFileToString(symt);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
    }

    /**
     * Setup - Set up the BEL {@link Document} and {@link ProtoNetwork} to test.
     */
    @Before
    public void setupTest() {
        NamespaceGroup namespaceGroup = new NamespaceGroup();
        Namespace hgncNs =
                new Namespace("HGNC", "http://www.belscript.org/ns/hgnc");
        namespaceGroup.setNamespaces(asList(hgncNs));

        StatementGroup statementGroup = new StatementGroup();

        AnnotationDefinition speciesDefinition =
                new AnnotationDefinition("species", "Description", "Usage",
                        asList("9606"));
        AnnotationDefinition tissueDefinition =
                new AnnotationDefinition("tissue", "Description", "Usage",
                        asList("TH1", "TH2", "AB3"));

        AnnotationGroup annotationGroup = new AnnotationGroup();
        annotationGroup.setAnnotations(asList(CommonModelFactory.getInstance()
                .createAnnotation("9606", speciesDefinition)));
        annotationGroup
                .setEvidence(CommonModelFactory
                        .getInstance()
                        .createEvidence(
                                "insulin activates the expression of SREBP-1c gene in primary hepatocytes whereas glucagon "
                                        + "and cAMP have the opposite effect suggests that SREBP could mediate some of the effects of insulin "
                                        + "on lipogenic gene expression in the liver, indirectly, by increasing glucose flux through glucokinase, "
                                        + "and/or directly, by activation of lipogenic gene expression"));

        statementGroup.setAnnotationGroup(annotationGroup);

        FunctionEnum fp = FunctionEnum.PROTEIN_ABUNDANCE;
        List<BELObject> args = new ArrayList<BELObject>();
        args.add(CommonModelFactory.getInstance().createParameter(hgncNs,
                "TXNIP"));
        Term t = new Term(fp, args);

        FunctionEnum ft = FunctionEnum.TRANSCRIPTIONAL_ACTIVITY;
        args = new ArrayList<BELObject>();
        args.add(CommonModelFactory.getInstance().createParameter(hgncNs,
                "IL3RA"));
        Term t2 = new Term(ft, args);

        FunctionEnum fr = FunctionEnum.RIBOSYLATION_ACTIVITY;
        args = new ArrayList<BELObject>();
        args.add(t2);
        Term t3 = new Term(fr, args);

        args = new ArrayList<BELObject>();
        args.add(CommonModelFactory.getInstance().createParameter(hgncNs,
                "ESR1"));
        Term t4 = new Term(fp, args);

        args = new ArrayList<BELObject>();
        args.add(CommonModelFactory.getInstance().createParameter(hgncNs,
                "BRCA1"));
        Term t5 = new Term(fp, args);

        args = new ArrayList<BELObject>();
        args.add(t4);
        Term t6 = new Term(ft, args);

        FunctionEnum fg = FunctionEnum.GENE_ABUNDANCE;
        args = new ArrayList<BELObject>();
        args.add(CommonModelFactory.getInstance().createParameter(hgncNs,
                "TXNIP"));
        Term t7 = new Term(fg, args);

        statementGroup
                .setStatements(asList(
                        new Statement(
                                t,
                                "The first statement",
                                annotationGroup,
                                new org.openbel.framework.common.model.Statement.Object(
                                        t3),
                                RelationshipType.DECREASES),
                        new Statement(
                                t5,
                                "The second statement",
                                annotationGroup,
                                new Statement.Object(
                                        new Statement(
                                                t6,
                                                "The third statement",
                                                annotationGroup,
                                                new Statement.Object(t7),
                                                RelationshipType.INCREASES)
                                ),
                                RelationshipType.DECREASES)));

        Header header = new Header("", asList("Another"),
                asList("Shrubbery"), "", "", "", "", "");
        Document document = new Document(header, statementGroup);
        document.setDefinitions(asList(speciesDefinition, tissueDefinition));
        document.setNamespaceGroup(namespaceGroup);

        protoNetwork = new ProtoNetworkBuilder(document).buildProtoNetwork();
    }

    /**
     * Teardown - Remove the {@link ProtoNetworkDescriptor} files written to
     * disk.
     */
    @After
    public void teardownTest() {
        boolean d = false;
        for (final File symt : protoNetworkDescriptor.getSymbolTables()) {
            d = symt.delete();
            assert d;
        }

        d = new File("all.tbl").delete();
        assert d;
    }
}
