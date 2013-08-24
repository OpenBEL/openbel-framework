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
package org.openbel.framework.core.df.beldata.equivalence;

import static org.openbel.framework.common.BELUtilities.nulls;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.core.df.beldata.AuthorBlock;
import org.openbel.framework.core.df.beldata.BELDataHeader;
import org.openbel.framework.core.df.beldata.CitationBlock;
import org.openbel.framework.core.df.beldata.ProcessingBlock;

/**
 * EquivalenceHeader represents a BEL equivalence file's header section including the four, required
 * blocks:<ul>
 * <li>Equivalence block - Describes the equivalence document and what type of data is defined</li>
 * <li>Author block - Describes the author of this equivalence document</li>
 * <li>Citation block - Describes the citation of this equivalence document</li>
 * <li>Processing block - States how the BEL framework should process this equivalence document</li>
 * <li>NamespaceReference block - States which namespace this equivalence document maps</li></ul>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class EquivalenceHeader extends BELDataHeader {
    private static final long serialVersionUID = 8730379855061543298L;

    /**
     * Defines the first block of a BEL equivalence document.  This block describes the
     * equivalence document and type of data it defines.
     */
    private EquivalenceBlock equivalenceBlock;

    /**
     * Defines the first namespace reference block of a BEL equivalence document.  This block
     * states the the source namespace reference in the mapping.
     */
    private NamespaceReferenceBlock namespaceReferenceBlock;

    public EquivalenceHeader(final EquivalenceBlock equivalenceBlock,
            final NamespaceReferenceBlock namespaceReferenceBlock,
            final AuthorBlock authorBlock, final CitationBlock citationBlock,
            final ProcessingBlock processingBlock) {
        super(authorBlock, citationBlock, processingBlock);

        if (nulls(equivalenceBlock, namespaceReferenceBlock)) {
            throw new InvalidArgument("Null argument(s) to equivalence header");
        }

        this.equivalenceBlock = equivalenceBlock;
        this.namespaceReferenceBlock = namespaceReferenceBlock;
    }

    /**
     * Retrieves the equivalence block.
     *
     * @return {@link EquivalenceBlock}, the equivalence block, which cannot be null
     */
    public EquivalenceBlock getEquivalenceBlock() {
        return equivalenceBlock;
    }

    /**
     * Retrieves the namespace reference block.
     *
     * @return {@link NamespaceReferenceBlock}, the namespace reference block,
     * which cannot be null
     */
    public NamespaceReferenceBlock getNamespaceReferenceBlock() {
        return namespaceReferenceBlock;
    }
}
