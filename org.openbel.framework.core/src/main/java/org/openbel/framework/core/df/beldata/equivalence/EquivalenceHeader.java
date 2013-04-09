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
