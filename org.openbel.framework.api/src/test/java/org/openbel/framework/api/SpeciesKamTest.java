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
package org.openbel.framework.api;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.openbel.framework.api.KamBuilder.edge;
import static org.openbel.framework.api.internal.KamStoreUtil.*;
import static org.openbel.framework.common.enums.RelationshipType.ACTS_IN;
import static org.openbel.framework.common.enums.RelationshipType.ORTHOLOGOUS;
import static org.openbel.framework.common.enums.RelationshipType.TRANSCRIBED_TO;
import static org.openbel.framework.common.enums.RelationshipType.TRANSLATED_TO;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.antlr.runtime.RecognitionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.api.internal.KamInfoUtil;
import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.BelTerm;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.Namespace;
import org.openbel.framework.api.internal.KAMStoreDaoImpl.TermParameter;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.SystemConfigurationFile;
import org.openbel.framework.common.cfg.SystemConfigurationBasedTest;
import org.openbel.framework.common.lang.ComplexAbundance;
import org.openbel.framework.common.lang.GeneAbundance;
import org.openbel.framework.common.lang.ProteinAbundance;

@SystemConfigurationFile(path = "src/test/resources/org/openbel/framework/api/belframework.cfg")
public class SpeciesKamTest extends SystemConfigurationBasedTest {
    private static KamInfo info;

    private KAMStore kamstore;

    /**
     * Tests collapsing of orthologous {@link GeneAbundance gene abundance}
     * nodes.  Additionally the surrounding gene activation pathway and
     * activities should be inferred as orthologous and collapsed.  The kam
     * should collapse to:
     * <p>
     * <pre>
     *     proteinAbundance(HGNC:MAPK1) actsIn kinaseActivity(proteinAbundance(HGNC:MAPK1))
     *     rnaAbundance(HGNC:MAPK1) translatedTo proteinAbundance(HGNC:MAPK1)
     *     geneAbundance(HGNC:MAPK1) transcribedTo rnaAbundance(HGNC:MAPK1)
     * </pre>
     * </p>
     */
    @Test
    public void speciesSimple() {
        kamstore = mock(KAMStore.class);
        final Kam kam;
        try {
            kam = simpleKam();
        } catch (RecognitionException e) {
            e.printStackTrace();
            fail("Simple KAM was not built correctly, error: " + e.getMessage());
            return;
        }

        KamSpecies skam;
        try {
            skam = new KamSpecies(kam, new DefaultSpeciesDialect(info,
                    kamstore, 9606, true),
                    kamstore);
        } catch (InvalidArgument e) {
            e.printStackTrace();
            fail("Failed to load species-specific KAM.");
            return;
        } catch (KAMStoreException e) {
            e.printStackTrace();
            fail("Failed to load species-specific KAM.");
            return;
        }

        // assert the topology of the graph
        assertThat(skam.getNodes().size(), is(4));
        assertThat(skam.getEdges().size(), is(3));

        // assert that ORTHOLOGOUS edges no longer exist
        for (final KamEdge e : skam.getEdges()) {
            if (ORTHOLOGOUS.equals(e.getRelationshipType())) {
                fail("Orthologous edge exists in the Kam after collapsing.");
            }
        }

        // assert deterministic collapsing
        final Iterator<KamEdge> it = skam.getEdges().iterator();
        assertThat(
                it.next().toString(),
                is("proteinAbundance(HGNC:MAPK1) actsIn kinaseActivity(proteinAbundance(HGNC:MAPK1))"));
        assertThat(
                it.next().toString(),
                is("rnaAbundance(HGNC:MAPK1) translatedTo proteinAbundance(HGNC:MAPK1)"));
        assertThat(
                it.next().toString(),
                is("geneAbundance(HGNC:MAPK1) transcribedTo rnaAbundance(HGNC:MAPK1)"));
    }

    /**
     * Tests collapsing of orthologous {@link ProteinAbundance protein family}
     * nodes and associated activities.  The kam should collapse to:
     * <p>
     * <pre>
     *     p(PFH:"14-3-3 Family") actsIn phos(p(PFH:"14-3-3 Family\))
           p(PFH:"14-3-3 Family") actsIn kin(p(PFH:"14-3-3 Family"))
     * </pre>
     * </p>
     */
    @Test
    public void speciesFamily() {
        kamstore = mock(KAMStore.class);
        final Kam kam;
        try {
            kam = familyKam();
        } catch (RecognitionException e) {
            e.printStackTrace();
            fail("Family KAM was not built correctly, error: " + e.getMessage());
            return;
        }

        KamSpecies skam;
        try {
            skam = new KamSpecies(kam,
                    new DefaultSpeciesDialect(info, kamstore, 9606, true),
                    kamstore);
        } catch (InvalidArgument e) {
            e.printStackTrace();
            fail("Failed to load species-specific KAM.");
            return;
        } catch (KAMStoreException e) {
            e.printStackTrace();
            fail("Failed to load species-specific KAM.");
            return;
        }

        // assert the topology of the graph
        assertThat(skam.getNodes().size(), is(3));
        assertThat(skam.getEdges().size(), is(2));

        // assert that ORTHOLOGOUS edges no longer exist
        for (final KamEdge e : skam.getEdges()) {
            if (ORTHOLOGOUS.equals(e.getRelationshipType())) {
                fail("Orthologous edge exists in the Kam after collapsing.");
            }
        }

        // assert deterministic collapsing
        final Iterator<KamEdge> it = skam.getEdges().iterator();
        assertThat(it.next().toString(),
                is("proteinAbundance(PFH:\"14-3-3 Family\") actsIn phosphataseActivity(proteinAbundance(PFH:\"14-3-3 Family\"))"));
        assertThat(it.next().toString(),
                is("proteinAbundance(PFH:\"14-3-3 Family\") actsIn kinaseActivity(proteinAbundance(PFH:\"14-3-3 Family\"))"));
    }

    /**
     * Tests collapsing of orthologous {@link ComplexAbundance protein complex}
     * nodes and associated activities.  The kam should collapse to:
     * <p>
     * <pre>
     *     complex(NCH:"AP-1 Complex") actsIn kin(complex(NCH:"AP-1 Complex"))
     * </pre>
     * </p>
     */
    @Test
    public void speciesComplex() {
        kamstore = mock(KAMStore.class);
        final Kam kam;
        try {
            kam = complexKam();
        } catch (RecognitionException e) {
            e.printStackTrace();
            fail("Complex KAM was not built correctly, error: " + e.getMessage());
            return;
        }

        KamSpecies skam;
        try {
            skam = new KamSpecies(kam,
                    new DefaultSpeciesDialect(info, kamstore, 9606, true),
                    kamstore);
        } catch (InvalidArgument e) {
            e.printStackTrace();
            fail("Failed to load species-specific KAM.");
            return;
        } catch (KAMStoreException e) {
            e.printStackTrace();
            fail("Failed to load species-specific KAM.");
            return;
        }

        // assert the topology of the graph
        assertThat(skam.getNodes().size(), is(2));
        assertThat(skam.getEdges().size(), is(1));

        // assert that ORTHOLOGOUS edges no longer exist
        for (final KamEdge e : skam.getEdges()) {
            if (ORTHOLOGOUS.equals(e.getRelationshipType())) {
                fail("Orthologous edge exists in the Kam after collapsing.");
            }
        }

        // assert deterministic collapsing
        final Iterator<KamEdge> it = skam.getEdges().iterator();
        assertThat(it.next().toString(),
                is("complexAbundance(NCH:\"AP-1 Complex\") actsIn kinaseActivity(complexAbundance(NCH:\"AP-1 Complex\"))"));
    }

    /**
     * {@inheritDoc}
     */
    @Before
    @Override
    public void setup() {
        super.setup();

        try {
            info = KamInfoUtil.createKamInfo();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @After
    @Override
    public void teardown() {
        super.teardown();
    }

    /**
     * <pre>
     *     k(p(MGI:Mapk1))
     *           ^
     *         actsIn
     *           |
     *      p(MGI:Mapk1)
     *           ^
     *      translatedTo
     *           |
     *      r(EG:26413)
     *           ^
     *     transcribedTo
     *           |
     *     g(MGI:Mapk1) <--orthologous--> g(HGNC:MAPK1) <--orthologous--> g(RGD:Mapk1)
     *                                                                          |
     *                                                                    transcribedTo
     *                                                                          v
     *                                                                     r(EG:116590)
     *                                                                          |
     *                                                                     translatedTo
     *                                                                          v
     *                                                                     p(RGD:Mapk1)
     *                                                                          |
     *                                                                        actsIn
     *                                                                          v
     *                                                                   k(p(RGD:Mapk1))
     * </pre>
     *
     * @throws RecognitionException
     */
    private Kam simpleKam() throws RecognitionException {
        final KamBuilder kb = new KamBuilder(info, true);
        Kam kam = kb.addNodes(
                "kinaseActivity(proteinAbundance(MGI:Mapk1))",
                "proteinAbundance(MGI:Mapk1)",
                "rnaAbundance(EG:26413)",
                "geneAbundance(EG:26413)",
                "geneAbundance(HGNC:MAPK1)",
                "geneAbundance(EG:116590)",
                "rnaAbundance(EG:116590)",
                "proteinAbundance(RGD:Mapk1)",
                "kinaseActivity(proteinAbundance(RGD:Mapk1))").
           addEdges(
                   edge("proteinAbundance(MGI:Mapk1)", ACTS_IN, "kinaseActivity(proteinAbundance(MGI:Mapk1))"),
                   edge("rnaAbundance(EG:26413)", TRANSLATED_TO, "proteinAbundance(MGI:Mapk1)"),
                   edge("geneAbundance(EG:26413)", TRANSCRIBED_TO, "rnaAbundance(EG:26413)"),
                   edge("geneAbundance(EG:26413)", ORTHOLOGOUS, "geneAbundance(HGNC:MAPK1)"),
                   edge("geneAbundance(HGNC:MAPK1)", ORTHOLOGOUS, "geneAbundance(EG:26413)"),
                   edge("geneAbundance(HGNC:MAPK1)", ORTHOLOGOUS, "geneAbundance(EG:116590)"),
                   edge("geneAbundance(EG:116590)", ORTHOLOGOUS, "geneAbundance(HGNC:MAPK1)"),
                   edge("geneAbundance(EG:116590)", TRANSCRIBED_TO, "rnaAbundance(EG:116590)"),
                   edge("rnaAbundance(EG:116590)", TRANSLATED_TO, "proteinAbundance(RGD:Mapk1)"),
                   edge("proteinAbundance(RGD:Mapk1)", ACTS_IN, "kinaseActivity(proteinAbundance(RGD:Mapk1))")).create();

        try {
            final Namespace mgi = createNamespace(
                    0,
                    "MGI",
                    "http://resource.belframework.org/belframework/1.0/namespace/mgi-approved-symbols.belns");
            final Namespace eg = createNamespace(
                    1,
                    "EG",
                    "http://resource.belframework.org/belframework/1.0/namespace/entrez-gene-ids-hmr.belns");
            final Namespace hgnc = createNamespace(
                    2,
                    "HGNC",
                    "http://resource.belframework.org/belframework/1.0/namespace/hgnc-approved-symbols.belns");
            final Namespace rgd = createNamespace(
                    3,
                    "RGD",
                    "http://resource.belframework.org/belframework/1.0/namespace/rgd-approved-symbols.belns");

            when(kamstore.getNamespaces(info)).thenReturn(
                    Arrays.asList(eg, hgnc, mgi, rgd));

            final TermParameter mgiMapk1 = createTermParameter(0, mgi, "Mapk1");
            final TermParameter eg26413 = createTermParameter(1, eg, "26413");
            final TermParameter hgncMAPK1 = createTermParameter(2, hgnc,
                    "MAPK1");
            final TermParameter eg116590 = createTermParameter(3, eg, "116590");
            final TermParameter rgdMapk1 = createTermParameter(4, rgd, "Mapk1");

            Collection<KamNode> kn = kam.getNodes();
            final KamNode[] nodes = kn.toArray(new KamNode[kn.size()]);
            BelTerm belTerm = createBelTerm(0,
                    "kinaseActivity(proteinAbundance(MGI:Mapk1))");
            when(kamstore.getSupportingTerms(nodes[0])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(mgiMapk1));

            belTerm = createBelTerm(1, "proteinAbundance(MGI:Mapk1)");
            when(kamstore.getSupportingTerms(nodes[1])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(mgiMapk1));

            belTerm = createBelTerm(2, "rnaAbundance(EG:26413)");
            when(kamstore.getSupportingTerms(nodes[2])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(eg26413));

            belTerm = createBelTerm(3, "geneAbundance(EG:26413)");
            when(kamstore.getSupportingTerms(nodes[3])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(eg26413));

            belTerm = createBelTerm(4, "geneAbundance(HGNC:MAPK1)");
            when(kamstore.getSupportingTerms(nodes[4])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(hgncMAPK1));

            belTerm = createBelTerm(5, "geneAbundance(EG:116590)");
            when(kamstore.getSupportingTerms(nodes[5])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(eg116590));

            belTerm = createBelTerm(6, "rnaAbundance(EG:116590)");
            when(kamstore.getSupportingTerms(nodes[6])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(eg116590));

            belTerm = createBelTerm(7, "proteinAbundance(RGD:Mapk1)");
            when(kamstore.getSupportingTerms(nodes[7])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(rgdMapk1));

            belTerm = createBelTerm(8,
                    "kinaseActivity(proteinAbundance(RGD:Mapk1))");
            when(kamstore.getSupportingTerms(nodes[8])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(rgdMapk1));
        } catch (KAMStoreException e) {
            e.printStackTrace();
            fail("Failed retrieving mocked supporting terms.");
        }

        return kam;
    }

    /**
     * <pre>
     * k(p(PFM:"14-3-3 Family"))                   k(p(PFH:"14-3-3 Family"))
     *           ^                                             ^
     *           |                                             |
     *           |                                             |
     *         actsIn                                        actsIn
     *           |                                             |
     *           |                                             |
     *     p(PFM:"14-3-3 Family") <--orthologous--> p(PFH:"14-3-3 Family") --actsIn--> phos(p(PFH:"14-3-3 Family"))
     *                ^                                      ^
     *                 \                                    /
     *                  \                                  /
     *                 orthologous                 orthologous
     *                          \                    /
     *                           \                  /
     *                            v                v
     *                            p(PFR:"14-3-3 Family")
     *                                      |
     *                                      |
     *                                    actsIn
     *                                      |
     *                                      |
     *                                      v
     *                          kin(p(PFR:"14-3-3 Family")
     * </pre>
     * @throws RecognitionException
     */
    private Kam familyKam() throws RecognitionException {
        final KamBuilder kb = new KamBuilder(info, true);
        Kam kam = kb.addNodes(
                "phosphataseActivity(proteinAbundance(PFH:\"14-3-3 Family\"))",
                "kinaseActivity(proteinAbundance(PFH:\"14-3-3 Family\"))",
                "proteinAbundance(PFH:\"14-3-3 Family\")",
                "kinaseActivity(proteinAbundance(PFM:\"14-3-3 Family\"))",
                "proteinAbundance(PFM:\"14-3-3 Family\")",
                "kinaseActivity(proteinAbundance(PFR:\"14-3-3 Family\"))",
                "proteinAbundance(PFR:\"14-3-3 Family\")").
           addEdges(
                   edge("proteinAbundance(PFH:\"14-3-3 Family\")", ACTS_IN, "phosphataseActivity(proteinAbundance(PFH:\"14-3-3 Family\"))"),
                   edge("proteinAbundance(PFH:\"14-3-3 Family\")", ACTS_IN, "kinaseActivity(proteinAbundance(PFH:\"14-3-3 Family\"))"),
                   edge("proteinAbundance(PFM:\"14-3-3 Family\")", ACTS_IN, "kinaseActivity(proteinAbundance(PFM:\"14-3-3 Family\"))"),
                   edge("proteinAbundance(PFR:\"14-3-3 Family\")", ACTS_IN, "kinaseActivity(proteinAbundance(PFR:\"14-3-3 Family\"))"),
                   edge("proteinAbundance(PFH:\"14-3-3 Family\")", ORTHOLOGOUS, "proteinAbundance(PFM:\"14-3-3 Family\")"),
                   edge("proteinAbundance(PFM:\"14-3-3 Family\")", ORTHOLOGOUS, "proteinAbundance(PFH:\"14-3-3 Family\")"),
                   edge("proteinAbundance(PFM:\"14-3-3 Family\")", ORTHOLOGOUS, "proteinAbundance(PFR:\"14-3-3 Family\")"),
                   edge("proteinAbundance(PFR:\"14-3-3 Family\")", ORTHOLOGOUS, "proteinAbundance(PFM:\"14-3-3 Family\")"),
                   edge("proteinAbundance(PFR:\"14-3-3 Family\")", ORTHOLOGOUS, "proteinAbundance(PFH:\"14-3-3 Family\")"),
                   edge("proteinAbundance(PFH:\"14-3-3 Family\")", ORTHOLOGOUS, "proteinAbundance(PFR:\"14-3-3 Family\")")).create();

        try {
            final Namespace pfh = createNamespace(
                    0,
                    "PFH",
                    "http://resource.belframework.org/belframework/1.0/namespace/selventa-named-human-protein-families.belns");
            final Namespace pfm = createNamespace(
                    1,
                    "PFM",
                    "http://resource.belframework.org/belframework/1.0/namespace/selventa-named-mouse-protein-families.belns");
            final Namespace pfr = createNamespace(
                    2,
                    "PFR",
                    "http://resource.belframework.org/belframework/1.0/namespace/selventa-named-rat-protein-families.belns");

            when(kamstore.getNamespaces(info)).thenReturn(
                    Arrays.asList(pfh, pfm, pfr));

            final TermParameter pfh1433 = createTermParameter(0, pfh,
                    "14-3-3 Family");
            final TermParameter pfm1433 = createTermParameter(1, pfm,
                    "14-3-3 Family");
            final TermParameter pfr1433 = createTermParameter(2, pfr,
                    "14-3-3 Family");

            final Collection<KamNode> kn = kam.getNodes();
            final KamNode[] nodes = kn.toArray(new KamNode[kn.size()]);
            BelTerm belTerm = createBelTerm(0,
                    "phosphataseActivity(proteinAbundance(PFH:\"14-3-3 Family\"))");
            when(kamstore.getSupportingTerms(nodes[0])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(pfh1433));

            belTerm = createBelTerm(1,
                    "kinaseActivity(proteinAbundance(PFH:\"14-3-3 Family\"))");
            when(kamstore.getSupportingTerms(nodes[1])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(pfh1433));

            belTerm = createBelTerm(2,
                    "proteinAbundance(PFH:\"14-3-3 Family\")");
            when(kamstore.getSupportingTerms(nodes[2])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(pfh1433));

            belTerm = createBelTerm(3,
                    "kinaseActivity(proteinAbundance(PFM:\"14-3-3 Family\"))");
            when(kamstore.getSupportingTerms(nodes[3])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(pfm1433));

            belTerm = createBelTerm(4,
                    "proteinAbundance(PFM:\"14-3-3 Family\")");
            when(kamstore.getSupportingTerms(nodes[4])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(pfm1433));

            belTerm = createBelTerm(5,
                    "kinaseActivity(proteinAbundance(PFR:\"14-3-3 Family\"))");
            when(kamstore.getSupportingTerms(nodes[5])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(pfr1433));

            belTerm = createBelTerm(6,
                    "proteinAbundance(PFR:\"14-3-3 Family\")");
            when(kamstore.getSupportingTerms(nodes[6])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(pfr1433));
        } catch (KAMStoreException e) {
            e.printStackTrace();
            fail("Failed retrieving mocked supporting terms.");
        }

        return kam;
    }

    /**
     * <pre>
     * kin(complex(NCM:"AP-1 Family"))                   kin(complex(NCH:"AP-1 Family"))
     *           ^                                             ^
     *           |                                             |
     *           |                                             |
     *         actsIn                                        actsIn
     *           |                                             |
     *           |                                             |
     *     complex(NCM:"AP-1 Family") <--orthologous--> complex(NCH:"AP-1 Family")
     *                ^                                      ^
     *                 \                                    /
     *                  \                                  /
     *                 orthologous                 orthologous
     *                          \                    /
     *                           \                  /
     *                            v                v
     *                            complex(NCR:"AP-1 Family")
     *                                      |
     *                                      |
     *                                    actsIn
     *                                      |
     *                                      |
     *                                      v
     *                          kin(complex(NCH:"AP-1 Family"))
     * </pre>
     * @throws RecognitionException
     */
    private Kam complexKam() throws RecognitionException {
        final KamBuilder kb = new KamBuilder(info, true);
        Kam kam = kb.addNodes(
                "kinaseActivity(complexAbundance(NCH:\"AP-1 Complex\"))",
                "complexAbundance(NCH:\"AP-1 Complex\")",
                "kinaseActivity(complexAbundance(NCM:\"AP-1 Complex\"))",
                "complexAbundance(NCM:\"AP-1 Complex\")",
                "kinaseActivity(complexAbundance(NCR:\"AP-1 Complex\"))",
                "complexAbundance(NCR:\"AP-1 Complex\")").
           addEdges(
                   edge("complexAbundance(NCH:\"AP-1 Complex\")", ACTS_IN, "kinaseActivity(complexAbundance(NCH:\"AP-1 Complex\"))"),
                   edge("complexAbundance(NCM:\"AP-1 Complex\")", ACTS_IN, "kinaseActivity(complexAbundance(NCM:\"AP-1 Complex\"))"),
                   edge("complexAbundance(NCR:\"AP-1 Complex\")", ACTS_IN, "kinaseActivity(complexAbundance(NCR:\"AP-1 Complex\"))"),
                   edge("complexAbundance(NCH:\"AP-1 Complex\")", ORTHOLOGOUS, "complexAbundance(NCM:\"AP-1 Complex\")"),
                   edge("complexAbundance(NCM:\"AP-1 Complex\")", ORTHOLOGOUS, "complexAbundance(NCH:\"AP-1 Complex\")"),
                   edge("complexAbundance(NCM:\"AP-1 Complex\")", ORTHOLOGOUS, "complexAbundance(NCR:\"AP-1 Complex\")"),
                   edge("complexAbundance(NCR:\"AP-1 Complex\")", ORTHOLOGOUS, "complexAbundance(NCM:\"AP-1 Complex\")"),
                   edge("complexAbundance(NCR:\"AP-1 Complex\")", ORTHOLOGOUS, "complexAbundance(NCH:\"AP-1 Complex\")"),
                   edge("complexAbundance(NCH:\"AP-1 Complex\")", ORTHOLOGOUS, "complexAbundance(NCR:\"AP-1 Complex\")")).create();

        try {
            final Namespace nch = createNamespace(
                    0,
                    "NCH",
                    "http://resource.belframework.org/belframework/1.0/namespace/selventa-named-human-complexes.belns");
            final Namespace ncm = createNamespace(
                    1,
                    "NCM",
                    "http://resource.belframework.org/belframework/1.0/namespace/selventa-named-mouse-complexes.belns");
            final Namespace ncr = createNamespace(
                    2,
                    "NCR",
                    "http://resource.belframework.org/belframework/1.0/namespace/selventa-named-rat-complexes.belns");

            when(kamstore.getNamespaces(info)).thenReturn(
                    Arrays.asList(nch, ncm, ncr));

            final TermParameter nchAP1 = createTermParameter(0, nch,
                    "AP-1 Complex");
            final TermParameter ncmAP1 = createTermParameter(1, ncm,
                    "AP-1 Complex");
            final TermParameter ncrAP1 = createTermParameter(2, ncr,
                    "AP-1 Complex");

            final Collection<KamNode> kn = kam.getNodes();
            final KamNode[] nodes = kn.toArray(new KamNode[kn.size()]);
            BelTerm belTerm = createBelTerm(0,
                    "kinaseActivity(complexAbundance(NCH:\"AP-1 Complex\"))");
            when(kamstore.getSupportingTerms(nodes[0])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(nchAP1));

            belTerm = createBelTerm(1,
                    "complexAbundance(NCH:\"AP-1 Complex\")");
            when(kamstore.getSupportingTerms(nodes[1])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(nchAP1));

            belTerm = createBelTerm(2,
                    "kinaseActivity(complexAbundance(NCM:\"AP-1 Complex\"))");
            when(kamstore.getSupportingTerms(nodes[2])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(ncmAP1));

            belTerm = createBelTerm(3,
                    "complexAbundance(NCM:\"AP-1 Complex\")");
            when(kamstore.getSupportingTerms(nodes[3])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(ncmAP1));

            belTerm = createBelTerm(4,
                    "kinaseActivity(complexAbundance(NCR:\"AP-1 Complex\"))");
            when(kamstore.getSupportingTerms(nodes[4])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(ncrAP1));

            belTerm = createBelTerm(5,
                    "complexAbundance(NCR:\"AP-1 Complex\")");
            when(kamstore.getSupportingTerms(nodes[5])).thenReturn(
                    Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(
                    Arrays.asList(ncrAP1));
        } catch (KAMStoreException e) {
            e.printStackTrace();
            fail("Failed retrieving mocked supporting terms.");
        }

        return kam;
    }
}
