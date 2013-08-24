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
