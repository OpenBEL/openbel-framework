/**
 * Copyright (C) 2012-2013 Selventa, Inc.
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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.model.*;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.ProtoNetworkError;
import org.openbel.framework.core.protonetwork.BinaryProtoNetworkDescriptor;
import org.openbel.framework.core.protonetwork.BinaryProtoNetworkExternalizer;
import org.openbel.framework.core.protonetwork.ProtoNetworkBuilder;
import org.openbel.framework.core.protonetwork.ProtoNetworkDescriptor;
import org.openbel.framework.core.protonetwork.ProtoNetworkExternalizer;

/**
 * BinaryProtoNetworkIOTest tests the reading and writing of a binary
 * {@link ProtoNetwork}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class BinaryProtoNetworkIOTest {

    /**
     * Define the test {@link ProtoNetwork}.
     */
    private ProtoNetwork protoNetwork;

    /**
     * Define the test {@link ProtoNetworkDescriptor}.
     */
    private ProtoNetworkDescriptor protoNetworkDescriptor;

    /**
     * Testcase - Test the roundtrip input/output of a binary
     * {@link ProtoNetwork}.
     */
    @Test
    public void testProtoNetworkIO() {
        ProtoNetworkExternalizer protoNetworkExternalizer =
                new BinaryProtoNetworkExternalizer();

        try {
            // test write
            protoNetworkDescriptor =
                    protoNetworkExternalizer.writeProtoNetwork(protoNetwork,
                            System.getProperty("user.dir"));

            // test read
            ProtoNetwork serializedProtoNetwork =
                    protoNetworkExternalizer
                            .readProtoNetwork(protoNetworkDescriptor);

            Assert.assertEquals(protoNetwork, serializedProtoNetwork);
        } catch (ProtoNetworkError e) {
            e.printStackTrace();
            Assert.fail("unexpected exception");
        }
    }

    /**
     * Setup - Set up a test BEL {@link Document}.
     */
    @Before
    public void setupTest() {
        NamespaceGroup namespaceGroup = new NamespaceGroup();
        Namespace entrezGeneNs =
                new Namespace("EG", "http://www.belscript.org/ns/entrez-gene");
        Namespace hgncNs =
                new Namespace("HGNC", "http://www.belscript.org/ns/hgnc");
        namespaceGroup.setNamespaces(asList(entrezGeneNs, hgncNs));

        StatementGroup statementGroup = new StatementGroup();

        AnnotationDefinition speciesDefinition =
                new AnnotationDefinition("species", "Description", "Usage",
                        asList("9606"));
        AnnotationDefinition tissueDefinition =
                new AnnotationDefinition("tissue", "Description", "Usage",
                        asList("TH1", "TH2", "AB3"));

        AnnotationGroup annotationGroup = new AnnotationGroup();
        annotationGroup.setAnnotations(asList(
                CommonModelFactory.getInstance().createAnnotation("9606",
                        speciesDefinition),
                CommonModelFactory.getInstance().createAnnotation("TH1",
                        tissueDefinition)));
        annotationGroup
                .setEvidence(CommonModelFactory
                        .getInstance()
                        .createEvidence(
                                "the elevation of intracellular cAMP concentration that results from increased glucagon production also plays a role in the down-regulation process"));

        statementGroup.setAnnotationGroup(annotationGroup);

        FunctionEnum p = FunctionEnum.PROTEIN_ABUNDANCE;
        List<BELObject> args = new ArrayList<BELObject>();

        args.add(CommonModelFactory.getInstance().createParameter(hgncNs,
                "NFKB"));
        Term t = new Term(p, args);

        FunctionEnum r = FunctionEnum.RNA_ABUNDANCE;
        args = new ArrayList<BELObject>();
        args.add(CommonModelFactory.getInstance()
                .createParameter(hgncNs, "FAS"));
        Term t2 = new Term(r, args);

        Statement simpleIncrease = new Statement(t,
                "", annotationGroup,
                new org.openbel.framework.common.model.Statement.Object(t2),
                RelationshipType.INCREASES);
        statementGroup.setStatements(asList(simpleIncrease));

        Header header = new Header("", asList("Unladen"), asList("Swallow"),
                "", "", "", "", "");
        Document document = new Document(header, statementGroup);
        document.setDefinitions(asList(speciesDefinition, tissueDefinition));
        document.setNamespaceGroup(namespaceGroup);

        protoNetwork = new ProtoNetworkBuilder(document).buildProtoNetwork();
    }

    /**
     * Teardown - Remove the file artifacts for the test {@link ProtoNetwork}.
     */
    @After
    public void teardownTest() {
        boolean deleted =
                ((BinaryProtoNetworkDescriptor) protoNetworkDescriptor)
                        .getProtoNetwork().delete();
        assert deleted;
    }
}
