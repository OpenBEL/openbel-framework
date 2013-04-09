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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.openbel.framework.common.bel.parser.ParserUtil;

/**
 * {@link ParserUtilTest} tests the {@link ParserUtil} utility class to make
 * sure CSV-like list records are parsed correctly.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class ParserUtilTest {

    /**
     * Test common forms of CSV list records.
     */
    @Test
    public void testBasic() {
        String csv = "{\"One\", \"Two\", \"Three\"}";
        String[] tokens = ParserUtil.parseListRecord(csv);
        assertThat(tokens, notNullValue());
        assertThat(tokens.length, is(3));
        assertThat(tokens[0], is("One"));
        assertThat(tokens[1], is("Two"));
        assertThat(tokens[2], is("Three"));

        csv = "{\"One\"  ,\"Two\",  \"Three\"}";
        tokens = ParserUtil.parseListRecord(csv);
        assertThat(tokens, notNullValue());
        assertThat(tokens.length, is(3));
        assertThat(tokens[0], is("One"));
        assertThat(tokens[1], is("Two"));
        assertThat(tokens[2], is("Three"));

        csv = "{  \"One\"  ,  \"Two\"  ,  \"Three\"  }";
        tokens = ParserUtil.parseListRecord(csv);
        assertThat(tokens, notNullValue());
        assertThat(tokens.length, is(3));
        assertThat(tokens[0], is("One"));
        assertThat(tokens[1], is("Two"));
        assertThat(tokens[2], is("Three"));

        csv = "{\"One, Two, Three\", \",,,\", \"\"}";
        tokens = ParserUtil.parseListRecord(csv);
        assertThat(tokens, notNullValue());
        assertThat(tokens.length, is(3));
        assertThat(tokens[0], is("One, Two, Three"));
        assertThat(tokens[1], is(",,,"));
        assertThat(tokens[2], is(""));

        csv = "{\"\", \"\", \"\"}";
        tokens = ParserUtil.parseListRecord(csv);
        assertThat(tokens, notNullValue());
        assertThat(tokens.length, is(3));
        assertThat(tokens[0], is(""));
        assertThat(tokens[1], is(""));
        assertThat(tokens[2], is(""));
    }

    /**
     * Test fields with escaped quotes.
     */
    @Test
    public void testEscapedQuotes() {
        String csv =
                "{\"This is a \\\"Test\\\", \", \"of the \\\"emergency\\\" \", \"broadcasting \\\"system\\\".\"}";
        String[] tokens = ParserUtil.parseListRecord(csv);
        assertThat(tokens, notNullValue());
        assertThat(tokens.length, is(3));
        assertThat(tokens[0], is("This is a \"Test\", ")); // This is a "Test",
        assertThat(tokens[1], is("of the \"emergency\" ")); // of the "emergency"
        assertThat(tokens[2], is("broadcasting \"system\".")); // broadcasting "system".

        csv = "{\"\\\"\\\"\\\"\\\"\", \"\\\"\\\"\", \"\\\"\\\"\"}";
        tokens = ParserUtil.parseListRecord(csv);
        assertThat(tokens, notNullValue());
        assertThat(tokens.length, is(3));
        assertThat(tokens[0], is("\"\"\"\"")); // """" in quotes
        assertThat(tokens[1], is("\"\"")); // "" in quotes
        assertThat(tokens[2], is("\"\"")); // "" in quotes
    }

    /**
     * Test list record forms that are syntactically invalid but should yield
     * reasonable results.
     */
    @Test
    public void testUnsupportedForms() {
        String csv = "{ , , , ,}";
        String[] tokens = ParserUtil.parseListRecord(csv);
        assertThat(tokens, notNullValue());
        assertThat(tokens.length, is(4));

        csv = "{}";
        tokens = ParserUtil.parseListRecord(csv);
        assertThat(tokens, notNullValue());
        assertThat(tokens.length, is(0));

        csv = "{     }";
        tokens = ParserUtil.parseListRecord(csv);
        assertThat(tokens, notNullValue());
        assertThat(tokens.length, is(0));
    }

    /**
     * Test fields with unicode characters and escape sequences.
     */
    @Test
    public void testUnicodeFields() {
        String csv = "{\"µ½æƺɑɣʘ\", \"ʬʗɮɆ\"}";
        String[] tokens = ParserUtil.parseListRecord(csv);
        assertThat(tokens, notNullValue());
        assertThat(tokens.length, is(2));
        assertThat(tokens[0], is("µ½æƺɑɣʘ"));
        assertThat(tokens[1], is("ʬʗɮɆ"));

        csv = "{\"\u039A\u03A0\", \"\u03BA\u03BF\"}";
        tokens = ParserUtil.parseListRecord(csv);
        assertThat(tokens, notNullValue());
        assertThat(tokens.length, is(2));
        assertThat(tokens[0], is("ΚΠ"));
        assertThat(tokens[1], is("κο"));
    }
}
