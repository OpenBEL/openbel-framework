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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openbel.framework.common.model.AnnotationDefinition;
import org.openbel.framework.common.model.AnnotationGroup;
import org.openbel.framework.common.model.CommonModelFactory;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.protonetwork.model.NamespaceTable;
import org.openbel.framework.common.protonetwork.model.ParameterTable;
import org.openbel.framework.common.protonetwork.model.ProtoNetwork;
import org.openbel.framework.common.protonetwork.model.StatementTable;
import org.openbel.framework.common.protonetwork.model.TermTable;
import org.openbel.framework.common.protonetwork.model.NamespaceTable.TableNamespace;
import org.openbel.framework.common.protonetwork.model.ParameterTable.TableParameter;
import org.openbel.framework.core.XBELConverterService;
import org.openbel.framework.core.XBELConverterServiceImpl;
import org.openbel.framework.core.protonetwork.ProtoNetworkBuilder;

/**
 * ProtoNetworkBuilderTest tests the proto network building using {@link ProtoNetworkBuilder}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class ProtoNetworkBuilderTest {
    /**
     * Defines the test BEL {@link Document}.
     */
    private Document document;

    /**
     * Testcase - Test the successful building of a simple statement {@link ProtoNetwork}.
     */
    @Test
    @Ignore
    public void testSimpleStatementProtoNetworkBuilding() {
        ProtoNetworkBuilder protoNetworkBuilder =
                new ProtoNetworkBuilder(document);
        ProtoNetwork protoNetwork = protoNetworkBuilder.buildProtoNetwork();

        //Test extracted symbols
        NamespaceTable namespaceTable = protoNetwork.getNamespaceTable();
        ParameterTable parameterTable = protoNetwork.getParameterTable();
        TermTable termTable = protoNetwork.getTermTable();
        Namespace hgncNs =
                new Namespace(
                        "HGNC",
                        "http://resource.belframework.org/belframework/1.0/ns/hgnc-approved-symbols.belns");

        //check eg default namespace and hgnc specified namespace
        assertEquals(3, namespaceTable.getNamespaces().size());
        assertEquals(11, parameterTable.getTableParameters().size());

        assertEquals(9, termTable.getTermValues().size());
        assertTrue(parameterTable.getTableParameters().contains(
                new TableParameter(new TableNamespace(hgncNs), "AIFM1")));
        assertTrue(termTable.getTermValues().contains("proteinAbundance(#)"));
        assertTrue(termTable.getTermValues().contains("geneAbundance(#)"));

        //Test extracted statements
        StatementTable statementTable = protoNetwork.getStatementTable();

        assertEquals(6, statementTable.getStatements().size());
    }

    /**
     * Setup - Set up the test BEL {@link Document}.
     */
    @Before
    public void setupDocument() {
        XBELConverterService converterService;
        try {
            converterService = new XBELConverterServiceImpl();
            document = converterService.toCommon(new File(
                    "../docs/xbel/examples/valid/beldocument_variations.xml"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Create a test {@link AnnotationGroup}.
     *
     * @return {@link AnnotationGroup}, the test annotation group
     */
    public AnnotationGroup createAnnotationGroup() {
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
                CommonModelFactory.getInstance().createAnnotation("9606",
                        tissueDefinition)));
        annotationGroup
                .setEvidence(CommonModelFactory
                        .getInstance()
                        .createEvidence(
                                "the elevation of intracellular cAMP concentration that results from increased glucagon production also plays a role in the down-regulation process"));

        return annotationGroup;
    }
}
