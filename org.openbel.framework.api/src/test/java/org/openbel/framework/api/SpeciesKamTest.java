package org.openbel.framework.api;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.openbel.framework.common.enums.FunctionEnum.GENE_ABUNDANCE;
import static org.openbel.framework.common.enums.FunctionEnum.KINASE_ACTIVITY;
import static org.openbel.framework.common.enums.FunctionEnum.PROTEIN_ABUNDANCE;
import static org.openbel.framework.common.enums.FunctionEnum.RNA_ABUNDANCE;
import static org.openbel.framework.common.enums.RelationshipType.ACTS_IN;
import static org.openbel.framework.common.enums.RelationshipType.ORTHOLOGOUS;
import static org.openbel.framework.common.enums.RelationshipType.TRANSCRIBED_TO;
import static org.openbel.framework.common.enums.RelationshipType.TRANSLATED_TO;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openbel.framework.api.Kam.KamEdge;
import org.openbel.framework.api.KamTestUtil.TestKamEdge;
import org.openbel.framework.api.KamTestUtil.TestKamNode;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.lang.GeneAbundance;
import org.openbel.framework.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.internal.KamInfoUtil;

public class SpeciesKamTest {
    private static KamInfo info;

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
        final Kam kam = simpleKam(info);
        KamSpecies skam;
        try {
            skam = new KamSpecies(kam, new DefaultSpeciesDialect(null, 9606), null);
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
        assertThat(it.next().toString(), is("proteinAbundance(MGI:Mapk1) actsIn kinaseActivity(proteinAbundance(MGI:Mapk1))"));
        assertThat(it.next().toString(), is("rnaAbundance(EG:26413) translatedTo proteinAbundance(MGI:Mapk1)"));
        assertThat(it.next().toString(), is("geneAbundance(HGNC:MAPK1) transcribedTo rnaAbundance(EG:26413)"));
    }

    @BeforeClass
    public static void setup() {
        try {
            info = KamInfoUtil.createKamInfo();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private Kam simpleKam(KamInfo testKAMInfo) {
        // create orthology kam
        final TestKamNode[] n = new TestKamNode[] {
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
        final TestKamEdge[] e = new TestKamEdge[] {
                new TestKamEdge(1, n[1], ACTS_IN, n[0]),
                new TestKamEdge(2, n[2], TRANSLATED_TO, n[1]),
                new TestKamEdge(3, n[3], TRANSCRIBED_TO, n[2]),
                new TestKamEdge(4, n[3], ORTHOLOGOUS, n[4]),
                new TestKamEdge(5, n[4], ORTHOLOGOUS, n[3]),
                new TestKamEdge(6, n[4], ORTHOLOGOUS, n[5]),
                new TestKamEdge(7, n[5], ORTHOLOGOUS, n[4]),
                new TestKamEdge(8, n[5], TRANSCRIBED_TO, n[6]),
                new TestKamEdge(9, n[6], TRANSLATED_TO, n[7]),
                new TestKamEdge(10, n[7], ACTS_IN, n[8])
        };
        final Kam kam = KamTestUtil.createKam(testKAMInfo, n, e);

        // create orthology map of orthologous to species
        final Map<Integer, Integer> orthologous = new LinkedHashMap<Integer, Integer>();
        orthologous.put(4, 5);
        orthologous.put(6, 5);
        return kam;
    }
}
