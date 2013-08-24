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
