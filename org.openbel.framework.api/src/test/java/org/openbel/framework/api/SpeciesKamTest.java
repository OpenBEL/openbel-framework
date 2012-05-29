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
import static org.openbel.framework.common.enums.FunctionEnum.GENE_ABUNDANCE;
import static org.openbel.framework.common.enums.FunctionEnum.KINASE_ACTIVITY;
import static org.openbel.framework.common.enums.FunctionEnum.PROTEIN_ABUNDANCE;
import static org.openbel.framework.common.enums.FunctionEnum.RNA_ABUNDANCE;
import static org.openbel.framework.common.enums.RelationshipType.ACTS_IN;
import static org.openbel.framework.common.enums.RelationshipType.ORTHOLOGOUS;
import static org.openbel.framework.common.enums.RelationshipType.TRANSCRIBED_TO;
import static org.openbel.framework.common.enums.RelationshipType.TRANSLATED_TO;
import static org.openbel.framework.internal.KamStoreUtil.createBelTerm;
import static org.openbel.framework.internal.KamStoreUtil.createNamespace;
import static org.openbel.framework.internal.KamStoreUtil.createTermParameter;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.Kam.KamNode;
import org.openbel.framework.api.KamTestUtil.TestKamEdge;
import org.openbel.framework.api.KamTestUtil.TestKamNode;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.SystemConfigurationFile;
import org.openbel.framework.common.cfg.SystemConfigurationBasedTest;
import org.openbel.framework.common.lang.GeneAbundance;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.internal.KAMStoreDaoImpl.BelTerm;
import org.openbel.framework.internal.KAMStoreDaoImpl.Namespace;
import org.openbel.framework.internal.KAMStoreDaoImpl.TermParameter;
import org.openbel.framework.internal.KamInfoUtil;

@SystemConfigurationFile(path = "src/test/resources/org/openbel/framework/api/belframework.cfg")
public class SpeciesKamTest extends SystemConfigurationBasedTest {
    private static KamInfo info;

    private KamStore kamstore;

    /**
     * Tests collapsing of orthologous {@link GeneAbundance gene abundance}
     * nodes.  Additionally the surrounding gene activation pathway and
     * activities should be inferred as orthologous and collapsed.  The kam
     * should collapse from:
     * <p>
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
     * </p>
     *
     * to:
     * <p>
     * <pre>
     *     g(HGNC:MAPK1) -transcribedTo> r(EG:26413) -translatedTo> p(MGI:Mapk1) -actsIn> k(p(MGI:Mapk1))
     *
     * </pre>
     * </p>
     */
    @Test
    public void speciesSimple() {
        kamstore = mock(KamStore.class);
        final Kam kam = simpleKam(info);

        KamSpecies skam;
        try {
            skam = new KamSpecies(kam,
                    new DefaultSpeciesDialect(info, kamstore, 9606, true),
                    new DefaultDialect(info, kamstore, true),
                    kamstore);
        } catch (InvalidArgument e) {
            e.printStackTrace();
            fail("Failed to load species-specific KAM.");
            return;
        } catch (KamStoreException e) {
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
        assertThat(it.next().toString(), is("proteinAbundance(HGNC:MAPK1) actsIn kinaseActivity(proteinAbundance(HGNC:MAPK1))"));
        assertThat(it.next().toString(), is("rnaAbundance(HGNC:MAPK1) translatedTo proteinAbundance(HGNC:MAPK1)"));
        assertThat(it.next().toString(), is("geneAbundance(HGNC:MAPK1) transcribedTo rnaAbundance(HGNC:MAPK1)"));
    }

    @Test
    public void speciesFamily() {
        kamstore = mock(KamStore.class);
        final Kam kam = familyKam(info);

        KamSpecies skam;
        try {
            skam = new KamSpecies(kam,
                    new DefaultSpeciesDialect(info, kamstore, 9606, true),
                    new DefaultDialect(info, kamstore, true),
                    kamstore);
        } catch (InvalidArgument e) {
            e.printStackTrace();
            fail("Failed to load species-specific KAM.");
            return;
        } catch (KamStoreException e) {
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
        assertThat(it.next().toString(), is("proteinAbundance(PFH:\"14-3-3 Family\") actsIn kinaseActivity(proteinAbundance(PFH:\"14-3-3 Family\"))"));
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
     */
    private Kam simpleKam(KamInfo testKAMInfo) {
        // create orthology kam
        final TestKamNode[] kn = new TestKamNode[] {
                new TestKamNode(1, KINASE_ACTIVITY, "kinaseActivity(proteinAbundance(MGI:Mapk1))"),
                new TestKamNode(2, PROTEIN_ABUNDANCE, "proteinAbundance(MGI:Mapk1)"),
                new TestKamNode(3, RNA_ABUNDANCE, "rnaAbundance(EG:26413)"),
                new TestKamNode(4, GENE_ABUNDANCE, "geneAbundance(EG:26413)"),
                new TestKamNode(5, GENE_ABUNDANCE, "geneAbundance(HGNC:MAPK1)"),
                new TestKamNode(6, GENE_ABUNDANCE, "geneAbundance(EG:116590)"),
                new TestKamNode(7, RNA_ABUNDANCE, "rnaAbundance(EG:116590)"),
                new TestKamNode(8, PROTEIN_ABUNDANCE, "proteinAbundance(RGD:Mapk1)"),
                new TestKamNode(9, KINASE_ACTIVITY, "kinaseActivity(proteinAbundance(RGD:Mapk1))")
        };
        final TestKamEdge[] ke = new TestKamEdge[] {
                new TestKamEdge(1, kn[1], ACTS_IN, kn[0]),
                new TestKamEdge(2, kn[2], TRANSLATED_TO, kn[1]),
                new TestKamEdge(3, kn[3], TRANSCRIBED_TO, kn[2]),
                new TestKamEdge(4, kn[3], ORTHOLOGOUS, kn[4]),
                new TestKamEdge(5, kn[4], ORTHOLOGOUS, kn[3]),
                new TestKamEdge(6, kn[4], ORTHOLOGOUS, kn[5]),
                new TestKamEdge(7, kn[5], ORTHOLOGOUS, kn[4]),
                new TestKamEdge(8, kn[5], TRANSCRIBED_TO, kn[6]),
                new TestKamEdge(9, kn[6], TRANSLATED_TO, kn[7]),
                new TestKamEdge(10, kn[7], ACTS_IN, kn[8])
        };
        final Kam kam = KamTestUtil.createKam(testKAMInfo, kn, ke);

        try {
            final Namespace mgi = createNamespace(0, "MGI", "http://resource.belframework.org/belframework/1.0/namespace/mgi-approved-symbols.belns");
            final Namespace eg = createNamespace(1, "EG", "http://resource.belframework.org/belframework/1.0/namespace/entrez-gene-ids-hmr.belns");
            final Namespace hgnc = createNamespace(2, "HGNC", "http://resource.belframework.org/belframework/1.0/namespace/hgnc-approved-symbols.belns");
            final Namespace rgd = createNamespace(3, "RGD", "http://resource.belframework.org/belframework/1.0/namespace/rgd-approved-symbols.belns");

            when(kamstore.getNamespaces(info)).thenReturn(Arrays.asList(eg, hgnc, mgi, rgd));

            final TermParameter mgiMapk1 = createTermParameter(0, mgi, "Mapk1");
            final TermParameter eg26413 = createTermParameter(1, eg, "26413");
            final TermParameter hgncMAPK1 = createTermParameter(2, hgnc, "MAPK1");
            final TermParameter eg116590 = createTermParameter(3, eg, "116590");
            final TermParameter rgdMapk1 = createTermParameter(4, rgd, "Mapk1");

            final KamNode[] nodes = kam.getNodes().toArray(new KamNode[kn.length]);
            BelTerm belTerm = createBelTerm(0, "kinaseActivity(proteinAbundance(MGI:Mapk1))");
            when(kamstore.getSupportingTerms(nodes[0])).thenReturn(Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(Arrays.asList(mgiMapk1));

            belTerm = createBelTerm(1, "proteinAbundance(MGI:Mapk1)");
            when(kamstore.getSupportingTerms(nodes[1])).thenReturn(Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(Arrays.asList(mgiMapk1));

            belTerm = createBelTerm(2, "rnaAbundance(EG:26413)");
            when(kamstore.getSupportingTerms(nodes[2])).thenReturn(Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(Arrays.asList(eg26413));

            belTerm = createBelTerm(3, "geneAbundance(EG:26413)");
            when(kamstore.getSupportingTerms(nodes[3])).thenReturn(Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(Arrays.asList(eg26413));

            belTerm = createBelTerm(4, "geneAbundance(HGNC:MAPK1)");
            when(kamstore.getSupportingTerms(nodes[4])).thenReturn(Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(Arrays.asList(hgncMAPK1));

            belTerm = createBelTerm(5, "geneAbundance(EG:116590)");
            when(kamstore.getSupportingTerms(nodes[5])).thenReturn(Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(Arrays.asList(eg116590));

            belTerm = createBelTerm(6, "rnaAbundance(EG:116590)");
            when(kamstore.getSupportingTerms(nodes[6])).thenReturn(Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(Arrays.asList(eg116590));

            belTerm = createBelTerm(7, "proteinAbundance(RGD:Mapk1)");
            when(kamstore.getSupportingTerms(nodes[7])).thenReturn(Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(Arrays.asList(rgdMapk1));

            belTerm = createBelTerm(8, "kinaseActivity(proteinAbundance(RGD:Mapk1))");
            when(kamstore.getSupportingTerms(nodes[8])).thenReturn(Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(Arrays.asList(rgdMapk1));
        } catch (KamStoreException e) {
            e.printStackTrace();
            fail("Failed retrieving mocked supporting terms.");
        }

        return kam;
    }

    private Kam familyKam(KamInfo testKAMInfo) {
        // create orthology kam
        final TestKamNode[] kn = new TestKamNode[] {
                new TestKamNode(1, KINASE_ACTIVITY, "kinaseActivity(proteinAbundance(PFH:\"14-3-3 Family\"))"),
                new TestKamNode(2, PROTEIN_ABUNDANCE, "proteinAbundance(PFH:\"14-3-3 Family\")"),
                new TestKamNode(3, KINASE_ACTIVITY, "kinaseActivity(proteinAbundance(PFM:\"14-3-3 Family\"))"),
                new TestKamNode(4, PROTEIN_ABUNDANCE, "proteinAbundance(PFM:\"14-3-3 Family\")"),
                new TestKamNode(5, KINASE_ACTIVITY, "kinaseActivity(proteinAbundance(PFR:\"14-3-3 Family\"))"),
                new TestKamNode(6, PROTEIN_ABUNDANCE, "proteinAbundance(PFR:\"14-3-3 Family\")"),
        };
        final TestKamEdge[] ke = new TestKamEdge[] {
                new TestKamEdge(1, kn[1], ACTS_IN, kn[0]),
                new TestKamEdge(2, kn[3], ACTS_IN, kn[2]),
                new TestKamEdge(3, kn[5], ACTS_IN, kn[4]),
                new TestKamEdge(4, kn[1], ORTHOLOGOUS, kn[3]),
                new TestKamEdge(5, kn[3], ORTHOLOGOUS, kn[1]),
                new TestKamEdge(6, kn[3], ORTHOLOGOUS, kn[5]),
                new TestKamEdge(7, kn[5], ORTHOLOGOUS, kn[3]),
                new TestKamEdge(8, kn[5], ORTHOLOGOUS, kn[1]),
                new TestKamEdge(9, kn[1], ORTHOLOGOUS, kn[5]),
        };
        final Kam kam = KamTestUtil.createKam(testKAMInfo, kn, ke);

        try {
            final Namespace pfh = createNamespace(0, "PFH", "http://resource.belframework.org/belframework/1.0/namespace/selventa-named-human-protein-families.belns");
            final Namespace pfm = createNamespace(1, "PFM", "http://resource.belframework.org/belframework/1.0/namespace/selventa-named-mouse-protein-families.belns");
            final Namespace pfr = createNamespace(2, "PFR", "http://resource.belframework.org/belframework/1.0/namespace/selventa-named-rat-protein-families.belns");

            when(kamstore.getNamespaces(info)).thenReturn(Arrays.asList(pfh, pfm, pfr));

            final TermParameter pfh1433 = createTermParameter(0, pfh, "14-3-3 Family");
            final TermParameter pfm1433 = createTermParameter(1, pfm, "14-3-3 Family");
            final TermParameter pfr1433 = createTermParameter(2, pfr, "14-3-3 Family");

            final KamNode[] nodes = kam.getNodes().toArray(new KamNode[kn.length]);
            BelTerm belTerm = createBelTerm(0, "kinaseActivity(proteinAbundance(PFH:\"14-3-3 Family\"))");
            when(kamstore.getSupportingTerms(nodes[0])).thenReturn(Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(Arrays.asList(pfh1433));

            belTerm = createBelTerm(1, "proteinAbundance(PFH:\"14-3-3 Family\")");
            when(kamstore.getSupportingTerms(nodes[1])).thenReturn(Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(Arrays.asList(pfh1433));

            belTerm = createBelTerm(2, "kinaseActivity(proteinAbundance(PFM:\"14-3-3 Family\"))");
            when(kamstore.getSupportingTerms(nodes[2])).thenReturn(Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(Arrays.asList(pfm1433));

            belTerm = createBelTerm(3, "proteinAbundance(PFM:\"14-3-3 Family\")");
            when(kamstore.getSupportingTerms(nodes[3])).thenReturn(Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(Arrays.asList(pfm1433));

            belTerm = createBelTerm(4, "kinaseActivity(proteinAbundance(PFR:\"14-3-3 Family\"))");
            when(kamstore.getSupportingTerms(nodes[4])).thenReturn(Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(Arrays.asList(pfr1433));

            belTerm = createBelTerm(5, "proteinAbundance(PFR:\"14-3-3 Family\")");
            when(kamstore.getSupportingTerms(nodes[5])).thenReturn(Arrays.asList(belTerm));
            when(kamstore.getTermParameters(info, belTerm)).thenReturn(Arrays.asList(pfr1433));
        } catch (KamStoreException e) {
            e.printStackTrace();
            fail("Failed retrieving mocked supporting terms.");
        }

        return kam;
    }
}
