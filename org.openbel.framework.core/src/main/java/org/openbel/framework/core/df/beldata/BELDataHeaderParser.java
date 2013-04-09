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
package org.openbel.framework.core.df.beldata;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * BELDataHeaderParser represents a BEL data document parser for the header
 * section. This parser understands header blocks and block properties like
 *
 * <pre>
 *    [SomeValue]
 *    PropertyKey1=PropertyValue1
 *    PropertyKey2=PropertyValue2
 * </pre>
 *
 * Block header values, property key, and property values are all trimmed of
 * leading/trailing whitespace using {@link String#trim()}. Additionally, lines
 * that are blank or start with the comment character '#' are ignored.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public abstract class BELDataHeaderParser {
    private static final Pattern PROPERTY_LINE_REGEX = Pattern
            .compile("([a-zA-Z0-9\\s]+)\\=(.*)");

    private static final String VALUES_BLOCK = "Values";

    /**
     * Defines the next character stop offset and initializes it to -1.
     */
    private long nextCharacterOffset = -1;

    /**
     * Retrieves the next character offset of the file parsed.
     *
     * @return long, the character stop offset, some positive number indicating
     *         the offset of the next character to read, which could be -1 if
     *         the file was fully read
     */
    public long getNextCharacterOffset() {
        return nextCharacterOffset;
    }

    /**
     * Parse the BEL Data header {@link File} to a {@link Map} of block name
     * to block properties held in a {@link Properties} object.
     *
     * @param belDataFile {@link File}, the BEL data document file to parse
     * @return {@link Map} of {@link String} to {@link Properties} the
     * properties indexed in a block name
     * @throws IOException, if there was an error reading from {@code belDataFile}
     */
    public Map<String, Properties> parse(File belDataFile) throws IOException {
        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(belDataFile, "r");

            Map<String, Properties> blockProperties =
                    new LinkedHashMap<String, Properties>();

            String line;
            String currentBlock = null;
            boolean aborted = false;
            while (!aborted && (line = raf.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#") || line.equals("")) {
                    // skip commented or blank lines.
                    continue;
                } else if (line.charAt(0) == '['
                        && line.charAt(line.length() - 1) == ']') {
                    currentBlock = line.substring(1, line.length() - 1).trim();

                    // if we have reached the VALUE_BLOCK then we're done
                    // parsing the header.
                    if (VALUES_BLOCK.equals(currentBlock)) {
                        nextCharacterOffset = raf.getFilePointer();
                        aborted = true;
                        continue;
                    }

                    blockProperties.put(currentBlock, new Properties());
                } else {
                    Matcher propertyLineMatcher = PROPERTY_LINE_REGEX
                            .matcher(line);

                    if (propertyLineMatcher.matches()) {
                        blockProperties.get(currentBlock).put(
                                propertyLineMatcher.group(1).trim(),
                                propertyLineMatcher.group(2).trim());
                    }
                }
            }

            return blockProperties;
        } finally {
            if (raf != null) {
                raf.close();
            }
        }
    }
}
