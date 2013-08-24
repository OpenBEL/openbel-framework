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
package org.openbel.framework.core.df.beldata.annotation;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.core.df.beldata.AuthorBlock;
import org.openbel.framework.core.df.beldata.BELDataHeader;
import org.openbel.framework.core.df.beldata.CitationBlock;
import org.openbel.framework.core.df.beldata.ProcessingBlock;

public class AnnotationHeader extends BELDataHeader {
    private static final long serialVersionUID = 1694374088957777403L;
    private AnnotationBlock annotationBlock;

    public AnnotationHeader(final AnnotationBlock annotationBlock,
            final AuthorBlock authorBlock,
            final CitationBlock citationBlock,
            final ProcessingBlock processingBlock) {
        super(authorBlock, citationBlock, processingBlock);

        if (annotationBlock == null) {
            throw new InvalidArgument("annotationBlock", annotationBlock);
        }

        this.annotationBlock = annotationBlock;
    }

    /**
     * Retrieves the annotation block for the annotation header.
     *
     * @return {@link AnnotationBlock} the annotation block, will not be null
     */
    public AnnotationBlock getAnnotationBlock() {
        return annotationBlock;
    }
}
