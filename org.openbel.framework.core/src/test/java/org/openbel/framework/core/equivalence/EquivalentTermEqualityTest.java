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
package org.openbel.framework.core.equivalence;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.openbel.framework.common.enums.FunctionEnum.GENE_ABUNDANCE;
import static org.openbel.framework.common.enums.FunctionEnum.PROTEIN_ABUNDANCE;
import static org.openbel.framework.common.enums.FunctionEnum.values;

import java.util.Arrays;

import org.junit.Test;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.model.BELObject;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.core.equivalence.EquivalentParameter;
import org.openbel.framework.core.equivalence.EquivalentTerm;

/**
 * {@link EquivalentTermEqualityTest} tests the equality check between different
 * pairs of {@link EquivalentTerm equivalent terms}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class EquivalentTermEqualityTest {

    /**
     * Test identity comparison of BEL Term:
     * <p>
     * {@code p(HGNC:AKT1)}
     * </p>
     */
    @Test
    public void testTermIdentityEquivalent() {
        final Namespace hgnc =
                new Namespace(
                        "HGNC",
                        "http://resource.belframework.org/belframework/1.0/namespace/hgnc-approved-symbols.belns");

        EquivalentParameter param1 = new EquivalentParameter(hgnc, "AKT1",
                SkinnyUUID.fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
        EquivalentTerm term1 = new EquivalentTerm(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) param1));

        assertThat(term1.equals(term1), is(true));
    }

    /**
     * Test mismatched {@link Object object types}.
     */
    @Test
    public void testMismatchedTypeUnequivalent() {
        final Namespace hgnc =
                new Namespace(
                        "HGNC",
                        "http://resource.belframework.org/belframework/1.0/namespace/hgnc-approved-symbols.belns");

        EquivalentParameter param1 = new EquivalentParameter(hgnc, "AKT1",
                SkinnyUUID.fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
        EquivalentTerm term1 = new EquivalentTerm(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) param1));

        assertThat(term1.equals(new Object()), is(false));
    }

    /**
     * Test exact match of BEL Terms:
     * <p>
     * {@code p(HGNC:AKT1)}<br>
     * {@code p(HGNC:AKT1)}
     * </p>
     */
    @Test
    public void testExactMatchEquivalent() {
        final Namespace hgnc =
                new Namespace(
                        "HGNC",
                        "http://resource.belframework.org/belframework/1.0/namespace/hgnc-approved-symbols.belns");
        EquivalentParameter param1 = new EquivalentParameter(hgnc, "AKT1",
                SkinnyUUID.fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
        EquivalentTerm term1 = new EquivalentTerm(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) param1));
        EquivalentParameter param2 = new EquivalentParameter(hgnc, "AKT1",
                SkinnyUUID.fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
        EquivalentTerm term2 = new EquivalentTerm(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) param2));

        assertThat(term1.equals(term2), is(true));
    }

    /**
     * Test mismatched {@link FunctionEnum function types} of BEL Terms:
     * <p>
     * {@code p(HGNC:AKT1)}<br>
     * {@code g(HGNC:AKT1)}
     * </p>
     */
    @Test
    public void testMismatchedFunctionUnequivalent() {
        final Namespace hgnc =
                new Namespace(
                        "HGNC",
                        "http://resource.belframework.org/belframework/1.0/namespace/hgnc-approved-symbols.belns");
        EquivalentParameter param1 = new EquivalentParameter(hgnc, "AKT1",
                SkinnyUUID.fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
        EquivalentTerm term1 = new EquivalentTerm(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) param1));
        EquivalentParameter param2 = new EquivalentParameter(hgnc, "AKT1",
                SkinnyUUID.fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
        EquivalentTerm term2 = new EquivalentTerm(GENE_ABUNDANCE,
                Arrays.asList((BELObject) param2));

        assertThat(term1.equals(term2), is(false));
    }

    /**
     * Test mismatched argument cardinality of BEL Terms:
     * <p>
     * {@code ALL_FUNCTIONS(HGNC:AKT1, HGNC:AKT2)}<br>
     * {@code ALL_FUNCTIONS(EGID:207)}
     * </p>
     */
    @Test
    public void testMismatchedArgumentCardinalityUnequivalent() {
        final Namespace hgnc =
                new Namespace(
                        "HGNC",
                        "http://resource.belframework.org/belframework/1.0/namespace/hgnc-approved-symbols.belns");
        final Namespace egid =
                new Namespace(
                        "EGID",
                        "http://resource.belframework.org/belframework/1.0/namespace/entrez-gene-ids-hmr.belns");

        for (final FunctionEnum f : values()) {
            EquivalentParameter param1 =
                    new EquivalentParameter(
                            hgnc,
                            "AKT1",
                            SkinnyUUID
                                    .fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
            EquivalentParameter param2 =
                    new EquivalentParameter(
                            hgnc,
                            "AKT2",
                            SkinnyUUID
                                    .fromString("9be88c14-6311-4452-a911-9f02abcf6a1e"));
            EquivalentTerm term1 = new EquivalentTerm(f, Arrays.asList(
                    (BELObject) param1, (BELObject) param2));

            EquivalentParameter param4 =
                    new EquivalentParameter(
                            egid,
                            "207",
                            SkinnyUUID
                                    .fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
            EquivalentTerm term2 = new EquivalentTerm(f,
                    Arrays.asList((BELObject) param4));

            assertThat(term1.equals(term2), is(false));
        }
    }

    /**
     * Test different parameter UUIDS of BEL Terms:
     * <p>
     * {@code p(HGNC:AKT1) with UUID a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f}<br>
     * {@code p(HGNC:AKT1) with UUID 27e0ab06-6644-42fc-b1f7-0b98bf4e6858}
     * </p>
     *
     * <p>
     * Not likely since the same namespace and value is used, but worth testing
     * the UUID in isolation.
     * </p>
     */
    @Test
    public void testDifferentParameterUUIDSUnequivalent() {
        final Namespace hgnc =
                new Namespace(
                        "HGNC",
                        "http://resource.belframework.org/belframework/1.0/namespace/hgnc-approved-symbols.belns");

        // different parameter uuids, not equivalent
        EquivalentParameter param1 = new EquivalentParameter(hgnc, "AKT1",
                SkinnyUUID.fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
        EquivalentTerm term1 = new EquivalentTerm(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) param1));
        EquivalentParameter param2 = new EquivalentParameter(hgnc, "AKT1",
                SkinnyUUID.fromString("27e0ab06-6644-42fc-b1f7-0b98bf4e6858"));
        EquivalentTerm term2 = new EquivalentTerm(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) param2));

        assertThat(term1.equals(term2), is(false));
    }

    /**
     * Test different parameter values of BEL Terms:
     * <p>
     * {@code p(HGNC:AKT1) with UUID a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f}<br>
     * {@code p(HGNC:AKT2) with UUID 9be88c14-6311-4452-a911-9f02abcf6a1e}
     * </p>
     */
    @Test
    public void testDifferentParameterValuesUnequivalent() {
        final Namespace hgnc =
                new Namespace(
                        "HGNC",
                        "http://resource.belframework.org/belframework/1.0/namespace/hgnc-approved-symbols.belns");

        EquivalentParameter param1 = new EquivalentParameter(hgnc, "AKT1",
                SkinnyUUID.fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
        EquivalentTerm term1 = new EquivalentTerm(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) param1));
        EquivalentParameter param2 = new EquivalentParameter(hgnc, "AKT2",
                SkinnyUUID.fromString("9be88c14-6311-4452-a911-9f02abcf6a1e"));
        EquivalentTerm term2 = new EquivalentTerm(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) param2));

        assertThat(term1.equals(term2), is(false));
    }

    /**
     * Test different namespaces with the same UUIDS of BEL Terms:
     * <p>
     * {@code p(HGNC:AKT1) with UUID a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f}<br>
     * {@code p(EGID:207) with UUID a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f}
     * </p>
     */
    @Test
    public void testDifferentNamespacesSameUUIDSEquivalent() {
        final Namespace hgnc =
                new Namespace(
                        "HGNC",
                        "http://resource.belframework.org/belframework/1.0/namespace/hgnc-approved-symbols.belns");
        final Namespace egid =
                new Namespace(
                        "EGID",
                        "http://resource.belframework.org/belframework/1.0/namespace/entrez-gene-ids-hmr.belns");

        EquivalentParameter param1 = new EquivalentParameter(hgnc, "AKT1",
                SkinnyUUID.fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
        EquivalentTerm term1 = new EquivalentTerm(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) param1));
        EquivalentParameter param2 = new EquivalentParameter(egid, "207",
                SkinnyUUID.fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
        EquivalentTerm term2 = new EquivalentTerm(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) param2));

        assertThat(term1.equals(term2), is(true));
    }

    /**
     * Test different namespaces with different values of BEL Terms:
     * <p>
     * {@code p(HGNC:AKT1) with UUID a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f}<br>
     * {@code p(EGID:257) with UUID aae305e5-bb6d-4e75-ac0b-85a76e1e8e09}
     * </p>
     */
    @Test
    public void testDifferentNamespacesDifferentValuesUnequivalent() {
        final Namespace hgnc =
                new Namespace(
                        "HGNC",
                        "http://resource.belframework.org/belframework/1.0/namespace/hgnc-approved-symbols.belns");
        final Namespace egid =
                new Namespace(
                        "EGID",
                        "http://resource.belframework.org/belframework/1.0/namespace/entrez-gene-ids-hmr.belns");

        EquivalentParameter param1 = new EquivalentParameter(hgnc, "AKT1",
                SkinnyUUID.fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
        EquivalentTerm term1 = new EquivalentTerm(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) param1));
        EquivalentParameter param2 = new EquivalentParameter(egid, "257",
                SkinnyUUID.fromString("aae305e5-bb6d-4e75-ac0b-85a76e1e8e09"));
        EquivalentTerm term2 = new EquivalentTerm(PROTEIN_ABUNDANCE,
                Arrays.asList((BELObject) param2));

        assertThat(term1.equals(term2), is(false));
    }

    /**
     * Test sequential matches of BEL Terms:
     * <p>
     * {@code SEQUENTIAL_FUNCTION(HGNC:AKT1, HGNC:AKT2)}<br>
     * {@code SEQUENTIAL_FUNCTION(EGID:207, EGID:208)}<br>
     * </p>
     */
    @Test
    public void testSequentialMatchEquivalent() {
        final Namespace hgnc =
                new Namespace(
                        "HGNC",
                        "http://resource.belframework.org/belframework/1.0/namespace/hgnc-approved-symbols.belns");
        final Namespace egid =
                new Namespace(
                        "EGID",
                        "http://resource.belframework.org/belframework/1.0/namespace/entrez-gene-ids-hmr.belns");

        for (final FunctionEnum f : values()) {
            if (f.isSequential()) {
                EquivalentParameter param1 =
                        new EquivalentParameter(
                                hgnc,
                                "AKT1",
                                SkinnyUUID
                                        .fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
                EquivalentParameter param2 =
                        new EquivalentParameter(
                                hgnc,
                                "AKT2",
                                SkinnyUUID
                                        .fromString("9be88c14-6311-4452-a911-9f02abcf6a1e"));
                EquivalentTerm term1 = new EquivalentTerm(f, Arrays.asList(
                        (BELObject) param1, (BELObject) param2));

                EquivalentParameter param3 =
                        new EquivalentParameter(
                                egid,
                                "207",
                                SkinnyUUID
                                        .fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
                EquivalentParameter param4 =
                        new EquivalentParameter(
                                egid,
                                "208",
                                SkinnyUUID
                                        .fromString("9be88c14-6311-4452-a911-9f02abcf6a1e"));
                EquivalentTerm term2 = new EquivalentTerm(f, Arrays.asList(
                        (BELObject) param3, (BELObject) param4));

                assertThat(term1.equals(term2), is(true));
            }
        }
    }

    /**
     * Test mismatched arguments of BEL Terms:
     * <p>
     * {@code SEQUENTIAL_FUNCTION(HGNC:AKT1, HGNC:AKT2)}<br>
     * {@code SEQUENTIAL_FUNCTION(EGID:208, EGID:207)}<br>
     * </p>
     */
    @Test
    public void testSequentialMismatchUnequivalent() {
        final Namespace hgnc =
                new Namespace(
                        "HGNC",
                        "http://resource.belframework.org/belframework/1.0/namespace/hgnc-approved-symbols.belns");
        final Namespace egid =
                new Namespace(
                        "EGID",
                        "http://resource.belframework.org/belframework/1.0/namespace/entrez-gene-ids-hmr.belns");

        for (final FunctionEnum f : values()) {
            if (f.isSequential()) {
                EquivalentParameter param1 =
                        new EquivalentParameter(
                                hgnc,
                                "AKT1",
                                SkinnyUUID
                                        .fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
                EquivalentParameter param2 =
                        new EquivalentParameter(
                                hgnc,
                                "AKT2",
                                SkinnyUUID
                                        .fromString("9be88c14-6311-4452-a911-9f02abcf6a1e"));
                EquivalentTerm term1 = new EquivalentTerm(f, Arrays.asList(
                        (BELObject) param1, (BELObject) param2));

                EquivalentParameter param3 =
                        new EquivalentParameter(
                                egid,
                                "208",
                                SkinnyUUID
                                        .fromString("9be88c14-6311-4452-a911-9f02abcf6a1e"));
                EquivalentParameter param4 =
                        new EquivalentParameter(
                                egid,
                                "207",
                                SkinnyUUID
                                        .fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
                EquivalentTerm term2 = new EquivalentTerm(f, Arrays.asList(
                        (BELObject) param3, (BELObject) param4));

                assertThat(term1.equals(term2), is(false));
            }
        }
    }

    /**
     * Test unconsecutive argument matches of BEL Terms:
     * <p>
     * {@code NON_SEQUENTIAL_FUNCTION(HGNC:AKT1, HGNC:AKT2)}<br>
     * {@code NON_SEQUENTIAL_FUNCTION(EGID:208, EGID:207)}<br>
     * </p>
     */
    @Test
    public void testUnconsecutiveMatchEquivalent() {
        final Namespace hgnc =
                new Namespace(
                        "HGNC",
                        "http://resource.belframework.org/belframework/1.0/namespace/hgnc-approved-symbols.belns");
        final Namespace egid =
                new Namespace(
                        "EGID",
                        "http://resource.belframework.org/belframework/1.0/namespace/entrez-gene-ids-hmr.belns");

        for (final FunctionEnum f : values()) {
            if (!f.isSequential()) {
                EquivalentParameter param1 =
                        new EquivalentParameter(
                                hgnc,
                                "AKT1",
                                SkinnyUUID
                                        .fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
                EquivalentParameter param2 =
                        new EquivalentParameter(
                                hgnc,
                                "AKT2",
                                SkinnyUUID
                                        .fromString("9be88c14-6311-4452-a911-9f02abcf6a1e"));
                EquivalentTerm term1 = new EquivalentTerm(f, Arrays.asList(
                        (BELObject) param1, (BELObject) param2));

                EquivalentParameter param3 =
                        new EquivalentParameter(
                                egid,
                                "208",
                                SkinnyUUID
                                        .fromString("9be88c14-6311-4452-a911-9f02abcf6a1e"));
                EquivalentParameter param4 =
                        new EquivalentParameter(
                                egid,
                                "207",
                                SkinnyUUID
                                        .fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
                EquivalentTerm term2 = new EquivalentTerm(f, Arrays.asList(
                        (BELObject) param3, (BELObject) param4));

                assertThat(term1.equals(term2), is(true));
            }
        }
    }

    /**
     * Test mismatched unconsecutive argument of BEL Terms:
     * <p>
     * {@code NON_SEQUENTIAL_FUNCTION(HGNC:AKT1, HGNC:AKT2)}<br>
     * {@code NON_SEQUENTIAL_FUNCTION(EGID:171, EGID:207)}<br>
     * </p>
     */
    @Test
    public void testUnconsecutiveMismatchUnequivalent() {
        final Namespace hgnc =
                new Namespace(
                        "HGNC",
                        "http://resource.belframework.org/belframework/1.0/namespace/hgnc-approved-symbols.belns");
        final Namespace egid =
                new Namespace(
                        "EGID",
                        "http://resource.belframework.org/belframework/1.0/namespace/entrez-gene-ids-hmr.belns");

        for (final FunctionEnum f : values()) {
            if (!f.isSequential()) {
                EquivalentParameter param1 =
                        new EquivalentParameter(
                                hgnc,
                                "AKT1",
                                SkinnyUUID
                                        .fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
                EquivalentParameter param2 =
                        new EquivalentParameter(
                                hgnc,
                                "AKT2",
                                SkinnyUUID
                                        .fromString("9be88c14-6311-4452-a911-9f02abcf6a1e"));
                EquivalentTerm term1 = new EquivalentTerm(f, Arrays.asList(
                        (BELObject) param1, (BELObject) param2));

                EquivalentParameter param3 =
                        new EquivalentParameter(
                                egid,
                                "171",
                                SkinnyUUID
                                        .fromString("1075d064-35f5-4418-97af-7a168ee54309"));
                EquivalentParameter param4 =
                        new EquivalentParameter(
                                egid,
                                "207",
                                SkinnyUUID
                                        .fromString("a32fd1cb-7d97-44ee-a0d0-db16c2a93b3f"));
                EquivalentTerm term2 = new EquivalentTerm(f, Arrays.asList(
                        (BELObject) param3, (BELObject) param4));

                assertThat(term1.equals(term2), is(false));
            }
        }
    }
}
