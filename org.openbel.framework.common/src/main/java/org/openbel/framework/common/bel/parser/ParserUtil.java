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
package org.openbel.framework.common.bel.parser;

import static org.openbel.framework.common.BELUtilities.noLength;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link ParserUtil} is a utility class for parsing records in BEL Script.
 */
public class ParserUtil {
    private static final char VALUE_DELIMITER = '|';
    private static final char FIELD_SEPARATOR = ',';
    private static final char FIELD_BOUNDARY = '"';
    private static final char ESCAPE = '\\';

    /**
     * Parse a BELScript list expression into a {@code String[] array}.
     *
     * @param line the list expression, which should not be {@code null}
     * @return the parsed tokens in the list, or {@code null} if {@code line}
     * is {@code null}
     */
    public static String[] parseListRecord(String line) {
        // return null if line is blank
        if (noLength(line)) {
            return null;
        }

        // trim away leading / trailing whitespace from list record
        line = line.trim();

        // remove list boundaries
        if (line.startsWith("{")) {
            line = line.substring(1);
        }
        if (line.endsWith("}")) {
            line = line.substring(0, line.length() - 1);
        }

        // return no fields if list record is empty
        if (line.trim().length() == 0) {
            return new String[0];
        }

        // split fields
        final CharacterIterator chit = new StringCharacterIterator(line);
        final List<String> fields = new ArrayList<String>();
        final StringBuilder fb = new StringBuilder();
        boolean inField = false;
        for (char c = chit.first(); c != CharacterIterator.DONE; c = chit
                .next()) {
            if (!inField && c == FIELD_BOUNDARY) {
                inField = true;
                fb.setLength(0);
            } else if (inField && c == FIELD_BOUNDARY) {
                int mark = chit.getIndex();
                char previousChar = chit.previous();
                if (previousChar == ESCAPE) {
                    fb.deleteCharAt(fb.length() - 1);
                    fb.append(c);
                } else {
                    inField = false;
                }
                chit.setIndex(mark);
            } else if (!inField && c == FIELD_SEPARATOR) {
                fields.add(fb.toString());
            } else if (inField) {
                fb.append(c);
            }
        }

        if (!line.endsWith(",")) {
            fields.add(fb.toString());
        }

        return fields.toArray(new String[fields.size()]);
    }

    /**
     * Parses tokens from a {@link ParserUtil#VALUE_DELIMITER} delimited
     * {@link String value}.
     *
     * @param value the {@link String value} to parse, which should not be
     * {@code null}
     * @return the {@code String[]} parsed tokens, or {@code null} if
     * {@code value} is {@code null}
     */
    public static String[] parseValueSeparated(String value) {
        if (noLength(value)) {
            return null;
        }

        return value.split("\\" + VALUE_DELIMITER);
    }

    /**
     * Private constructor for utility class.
     */
    private ParserUtil() {

    }
}
