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
package org.openbel.framework.common.record;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.openbel.framework.common.BELUtilities.computeHash64;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.openbel.framework.common.record.NamespaceRecordFile;
import org.openbel.framework.common.record.RecordMode;
import org.openbel.framework.common.record.NamespaceRecordFile.NamespaceEntry;
import org.openbel.framework.common.record.NamespaceRecordFile.NamespaceRecord;

/**
 * {@link NamespaceRecordFileTest} is a junit test for reading and writing
 * {@link NamespaceRecordFile namespace record files}.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class NamespaceRecordFileTest {
    private static final Random rand = new Random();
    private File recfile;

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Test
    public void testBuildNamespaceRecordFile() throws IOException {
        recfile = temp.newFile("ns.rec");
        final NamespaceRecordFile record = new NamespaceRecordFile(recfile,
                RecordMode.READ_WRITE,
                new NamespaceRecord(14));

        assertThat(record, is(notNullValue()));

        // write namespace records
        final int enc = 4;
        for (int i = 0, n = 20000; i < n; i++) {
            final String val = randomValue(10);
            final NamespaceEntry entry = new NamespaceEntry(computeHash64(val),
                    enc, val);

            // write entry to record file
            try {
                record.append(entry);
            } catch (IOException e) {
                e.printStackTrace();
                fail("Did not expect IOException when writing record.");
            }

            assertThat(record.getRecordCount(), is((long) i + 1));
        }

        // read namespace records
        // TODO assert the entry data
        NamespaceEntry entry5000 = record.readEntry(5000);
        assertThat(entry5000, is(notNullValue()));

        NamespaceEntry entry10000 = record.readEntry(10000);
        assertThat(entry10000, is(notNullValue()));

        NamespaceEntry entry15000 = record.readEntry(15000);
        assertThat(entry15000, is(notNullValue()));

        // iterate namespace records
        Iterator<byte[]> nsit = record.iterator();
        int count = 0;
        while (nsit.hasNext()) {
            byte[] recbytes = nsit.next();
            assertThat(recbytes, is(notNullValue()));
            assertThat(recbytes.length, is(14));

            count += 1;
        }

        assertThat(count, is(20000));
    }

    private static final String randomValue(int maxChars) {
        // random - 1 ... maxChars
        int randChars = rand.nextInt(maxChars) + 1;

        char[] chars = new char[randChars];
        for (int i = 0, n = chars.length; i < n; i++) {
            // random - 'a' ... 'z'
            chars[i] = (char) (rand.nextInt(26) + 'a');
        }

        return new String(chars);
    }
}
