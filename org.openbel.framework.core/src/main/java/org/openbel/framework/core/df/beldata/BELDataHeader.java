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
