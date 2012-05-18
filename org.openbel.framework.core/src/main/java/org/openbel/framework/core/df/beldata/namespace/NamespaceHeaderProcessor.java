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
package org.openbel.framework.core.df.beldata.namespace;

import static org.openbel.framework.common.BELUtilities.asPath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.openbel.framework.core.df.beldata.BELDataConversionException;
import org.openbel.framework.core.df.beldata.BELDataMissingPropertyException;
import org.openbel.framework.core.indexer.IndexingFailure;

/**
 * NamespaceHeaderProcessor processes a namespace {@link File} to produce a
 * {@link NamespaceHeader} that is written to the file system.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class NamespaceHeaderProcessor {

    /**
     * Defines the byte offset in the namespace file where the namespace header
     * parsing ended.  This allows further parsing of a namespace file from a
     * known offset.
     */
    private long characterStopOffset = -1;

    /**
     * Returns the byte offset in the namespace file where the namespace header
     * parsing ended.
     *
     * @return <tt>long</tt> the character offset where the parsing of a
     * namespace file ended
     */
    public long getCharacterStopOffset() {
        return characterStopOffset;
    }

    /**
     * Processes a namespace {@link File} into a {@link NamespaceHeader} that
     * is then written to the file system.
     *
     * @param rawNamespace {@link File}, the raw namespace file to parse
     * @param namespaceHomeFile {@link File}, the namespace header file to
     * write to
     * @return {@link File} the file location of the local parsed namespace
     * header file
     * @throws IndexingFailure Throws an namespace indexing exception is the
     * namespace header processing fails
     */
    public File processNamespaceHeader(String resourceLocation,
            File rawNamespace, File namespaceHomeFile)
            throws IndexingFailure {
        NamespaceHeaderParser nshParser = new NamespaceHeaderParser();
        NamespaceHeader namespaceHeader;

        try {
            namespaceHeader = nshParser.parseNamespace(resourceLocation,
                    rawNamespace);
            characterStopOffset = nshParser.getNextCharacterOffset();
        } catch (IOException e) {
            final String name = rawNamespace.getAbsolutePath();
            throw new IndexingFailure(name, e);
        } catch (BELDataMissingPropertyException e) {
            final String name = rawNamespace.getAbsolutePath();
            throw new IndexingFailure(name, e);
        } catch (BELDataConversionException e) {
            final String name = rawNamespace.getAbsolutePath();
            throw new IndexingFailure(name, e);
        }

        String output = asPath(namespaceHomeFile.getAbsolutePath(),
                "namespace.header");
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(
                    new FileOutputStream(output));
            oos.writeObject(namespaceHeader);
            return new File(output);
        } catch (IOException e) {
            final String msg = "Error writing namespace header.";
            throw new IndexingFailure(resourceLocation, msg, e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {}
            }
        }
    }
}
