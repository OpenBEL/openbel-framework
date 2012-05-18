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
package org.openbel.framework.core.df.beldata;

import static org.openbel.framework.common.BELUtilities.nulls;

import java.io.Serializable;

import org.openbel.framework.common.InvalidArgument;

public abstract class BELDataHeader implements Serializable {
    private static final long serialVersionUID = -8103791645981547681L;
    private final AuthorBlock authorBlock;
    private final CitationBlock citationBlock;
    private final ProcessingBlock processingBlock;

    public BELDataHeader(final AuthorBlock authorBlock,
            final CitationBlock citationBlock,
            final ProcessingBlock processingBlock) {
        if (nulls(authorBlock, citationBlock, processingBlock)) {
            throw new InvalidArgument("Header argument(s) are null.");
        }

        this.authorBlock = authorBlock;
        this.citationBlock = citationBlock;
        this.processingBlock = processingBlock;
    }

    /**
     * Retrieves the {@link AuthorBlock author block}.
     * <p>
     * This describes the author of the namespace, and defines the namespace's
     * second block.
     * </p>
     * 
     * @return {@link AuthorBlock}, the author block, which cannot be null
     */
    public AuthorBlock getAuthorBlock() {
        return authorBlock;
    }

    /**
     * Retrieves the {@link CitationBlock citation block}.
     * <p>
     * This describes the original location of the namespace, and defines the
     * namespace's third block.
     * </p>
     * 
     * @return {@link CitationBlock}, the citation block, which cannot be null
     */
    public CitationBlock getCitationBlock() {
        return citationBlock;
    }

    /**
     * Retrieves the {@link ProcessingBlock processing block}.
     * <p>
     * This describes how the namespace should be processed by the framework,
     * and defines the namespace's fourth block.
     * </p>
     * 
     * @return {@link ProcessingBlock}, the processing block, which cannot be
     * null
     */
    public ProcessingBlock getProcessingBlock() {
        return processingBlock;
    }
}
