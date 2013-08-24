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
package org.openbel.framework.core.df.beldata.namespace;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.core.df.beldata.AuthorBlock;
import org.openbel.framework.core.df.beldata.BELDataHeader;
import org.openbel.framework.core.df.beldata.CitationBlock;
import org.openbel.framework.core.df.beldata.ProcessingBlock;

/**
 * NamespaceHeader represents a BEL namespace header section including the four,
 * required blocks:
 * <ul>
 * <li>Namespace block - Describes the namespace and what type of data is
 * defined</li>
 * <li>Author block - Describes the author of this namespace</li>
 * <li>Citation block - Describes where the namespace values were referenced
 * from</li>
 * <li>Processing block - States how the BEL framework should process this
 * namespace</li>
 * </ul>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class NamespaceHeader extends BELDataHeader {
    private static final long serialVersionUID = 8730379855061543298L;

    private final NamespaceBlock namespaceBlock;

    public NamespaceHeader(final NamespaceBlock namespaceBlock,
            final AuthorBlock authorBlock, final CitationBlock citationBlock,
            final ProcessingBlock processingBlock) {
        super(authorBlock, citationBlock, processingBlock);

        if (namespaceBlock == null) {
            throw new InvalidArgument("namespaceBlock", namespaceBlock);
        }

        this.namespaceBlock = namespaceBlock;
    }

    /**
     * Retrieves the {@link NamespaceBlock namespace block}.
     * <p>
     * This describes the namespace and its type of data. This is the first
     * block of the namespace.
     * </p>
     *
     * @return {@link NamespaceBlock}, the namespace block, which cannot be null
     */
    public NamespaceBlock getNamespaceBlock() {
        return namespaceBlock;
    }
}
