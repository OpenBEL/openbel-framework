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
