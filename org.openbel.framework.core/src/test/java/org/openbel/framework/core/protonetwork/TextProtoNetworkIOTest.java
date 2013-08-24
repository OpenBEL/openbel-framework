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
