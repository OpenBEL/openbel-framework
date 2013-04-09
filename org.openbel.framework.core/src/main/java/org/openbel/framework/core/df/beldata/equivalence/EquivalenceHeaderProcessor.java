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

import static org.openbel.framework.common.BELUtilities.asPath;
import static org.openbel.framework.common.BELUtilities.createDirectories;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.openbel.framework.core.df.beldata.BELDataConversionException;
import org.openbel.framework.core.df.beldata.BELDataMissingPropertyException;
import org.openbel.framework.core.indexer.IndexingFailure;

public class EquivalenceHeaderProcessor {

    private long characterStopOffset = -1;

    public long getCharacterStopOffset() {
        return characterStopOffset;
    }

    public File processEquivalenceHeader(final String resourceLocation,
            final File rawEquivalence, final File equivalenceHomeFile)
            throws IndexingFailure {
        EquivalenceHeaderParser equivalenceParser =
                new EquivalenceHeaderParser();
        EquivalenceHeader equivalenceHeader = null;
        final String name = rawEquivalence.getAbsolutePath();

        try {
            equivalenceHeader = equivalenceParser.parseEquivalence(
                    resourceLocation, rawEquivalence);
            characterStopOffset = equivalenceParser.getNextCharacterOffset();
        } catch (IOException e) {
            throw new IndexingFailure(name, e);
        } catch (BELDataMissingPropertyException e) {
            throw new IndexingFailure(name, e);
        } catch (BELDataConversionException e) {
            throw new IndexingFailure(name, e);
        }

        final EquivalenceBlock eqblock =
                equivalenceHeader.getEquivalenceBlock();
        final String eqhome = equivalenceHomeFile.getAbsolutePath();
        String eqname = eqblock.getNameString().replace(' ', '-');
        String eqversion = eqblock.getVersionString().replace(' ', '-');
        String path = asPath(eqhome, eqname, eqversion);

        createDirectories(path);
        final String outputPath = asPath(path, "equivalence.header");

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(outputPath));
            oos.writeObject(equivalenceHeader);
            return new File(outputPath);
        } catch (IOException e) {
            final String msg = "Error writing equivalencer header.";
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
