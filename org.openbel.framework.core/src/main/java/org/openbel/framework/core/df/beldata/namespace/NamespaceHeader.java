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
