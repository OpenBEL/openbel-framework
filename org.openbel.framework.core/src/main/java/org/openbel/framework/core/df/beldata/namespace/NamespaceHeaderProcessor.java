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
