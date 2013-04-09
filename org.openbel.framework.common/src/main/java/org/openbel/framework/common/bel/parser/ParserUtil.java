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
