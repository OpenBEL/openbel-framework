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
