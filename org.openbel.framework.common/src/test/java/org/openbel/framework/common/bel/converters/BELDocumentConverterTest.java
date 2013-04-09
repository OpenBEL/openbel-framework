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
package org.openbel.framework.common.bel.converters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.openbel.framework.common.BELUtilities.constrainedHashSet;
import static org.openbel.framework.common.BELUtilities.sizedArrayList;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.openbel.bel.model.BELDocument;
import org.openbel.bel.model.BELDocumentHeader;
import org.openbel.bel.model.BELNamespaceDefinition;
import org.openbel.bel.model.BELStatement;
import org.openbel.bel.model.BELStatementGroup;
import org.openbel.framework.common.bel.converters.BELDocumentConverter;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.NamespaceGroup;

/**
 * {@link BELDocumentConverterTest} tests the conversion from the
 * {@link BELDocument BEL model} to the {@link Document common document model}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class BELDocumentConverterTest {
    private static final String DEFAULT_NS =
            "http://resource.belframework.org/belframework/1.0/namespace/hgnc-approved-symbols.belns";
    private static final String NS =
            "http://resource.belframework.org/belframework/1.0/namespace/entrez-gene-ids-hmr.belns";

    /**
     * Tests the successful conversion of namespaces defined in the
     * {@link BELDocument BEL} model.  Specifically this tests the conversion
     * treatment of both custom and default namespaces.
     */
    @Test
    public void testNamespaceGroupConversion() {
        // setup test BELDocument with one default and one custom namespace
        final BELDocumentHeader dh = new BELDocumentHeader("Unit Test",
                "Unit Test", "1.0");
        final Set<BELNamespaceDefinition> nsset = constrainedHashSet(2);
        nsset.add(new BELNamespaceDefinition("HGNC", DEFAULT_NS, true));
        nsset.add(new BELNamespaceDefinition("EGID", NS, false));
        final List<BELStatementGroup> sglist = sizedArrayList(1);
        final BELStatementGroup sg = new BELStatementGroup("Example");
        sg.getStatements().add(new BELStatement("p(AKT1) => g(EGID:1024)"));
        sglist.add(sg);
        final BELDocument beldoc = new BELDocument(dh, null, nsset, sglist);

        // convert to common model
        final BELDocumentConverter converter = new BELDocumentConverter();
        final Document doc = converter.convert(beldoc);

        // validate namespace group
        final NamespaceGroup nsgroup = doc.getNamespaceGroup();
        assertThat("More than one custom namespace exists.", nsgroup
                .getNamespaces().size(), is(1));
        assertThat("The custom namespace does not match.", nsgroup
                .getNamespaces().get(0).getResourceLocation(), is(NS));
        assertThat("The default namespace does not match.",
                nsgroup.getDefaultResourceLocation(), is(DEFAULT_NS));
    }
}
