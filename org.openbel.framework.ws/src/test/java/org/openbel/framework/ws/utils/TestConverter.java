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
package org.openbel.framework.ws.utils;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openbel.framework.api.KamStoreObject;
import org.openbel.framework.ws.utils.Converter.KamStoreObjectRef;

public class TestConverter {

    private static final long SEED = 71073753702L;
    private static final Random random = new Random(SEED);

    // Reflected from Converter
    private static Charset ASCII;
    private static Method typeEncode;

    // Reflected from Converter.KamStoreObjectType
    private static Class<?> kamStoreObjectTypeClass;
    private static String kamStoreObjectTypeClassName;
    private static Field repField;
    private static Method sortAndCreateIndex, getRepresentation,
            getRepresentedClass;
    private byte[] representations;
    private int[] repIndex;

    // Reflected from Converter.KamStoreObjectRef
    private static Method encode, decode;

    @BeforeClass
    public static void setUpBeforeClass() throws Throwable {

        final Field asciiField = Converter.class.getDeclaredField("ASCII");
        asciiField.setAccessible(true);
        ASCII = (Charset) asciiField.get(null);

        kamStoreObjectTypeClass = null;
        Class<?>[] converterInnerClasses = Converter.class.getDeclaredClasses();
        for (int i = 0; i < converterInnerClasses.length; ++i) {
            final Class<?> converterInnerClass = converterInnerClasses[i];
            if (converterInnerClass.getSimpleName()
                    .equals("KamStoreObjectType")) {
                kamStoreObjectTypeClass = converterInnerClass;
                break;
            }
        }
        assertThat(kamStoreObjectTypeClass, is(not(nullValue())));
        kamStoreObjectTypeClassName = kamStoreObjectTypeClass.getSimpleName();

        repField = kamStoreObjectTypeClass.getDeclaredField("rep");
        repField.setAccessible(true);

        getRepresentation =
                kamStoreObjectTypeClass.getDeclaredMethod("getRepresentation");
        getRepresentedClass =
                kamStoreObjectTypeClass
                        .getDeclaredMethod("getRepresentedClass");

        sortAndCreateIndex = kamStoreObjectTypeClass.getDeclaredMethod(
                "sortAndCreateIndex", byte[].class);
        sortAndCreateIndex.setAccessible(true);

        typeEncode =
                Converter.class.getDeclaredMethod("encode",
                        CharsetEncoder.class, char.class);
        typeEncode.setAccessible(true);

        encode =
                KamStoreObjectRef.class.getDeclaredMethod("encode", int.class,
                        int.class, byte.class);
        encode.setAccessible(true);

        decode =
                KamStoreObjectRef.class.getDeclaredMethod("decode",
                        String.class, kamStoreObjectTypeClass);
        decode.setAccessible(true);
    }

    @Before
    public void setup() throws Throwable {
        try {
            final Field representationsField =
                    kamStoreObjectTypeClass.getDeclaredField("representations");
            representationsField.setAccessible(true);
            representations = (byte[]) representationsField.get(null);

            final Field repIndexField =
                    kamStoreObjectTypeClass.getDeclaredField("repIndex");
            repIndexField.setAccessible(true);
            repIndex = (int[]) repIndexField.get(null);
        } catch (ExceptionInInitializerError e) {
            throw e.getCause();
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testThatDecodeEncodeIsIdentity() throws Throwable {

        try {
            for (Object type : kamStoreObjectTypeClass.getEnumConstants()) {
                int kamInfoId = random.nextInt();
                int id = 0;
                // A valid ID is not less than 1
                while ((id = random.nextInt()) < 1) {}
                final byte prefix =
                        ((Byte) getRepresentation.invoke(type)).byteValue();
                final Class<? extends KamStoreObject> typeClass =
                        (Class<? extends KamStoreObject>) getRepresentedClass
                                .invoke(type);

                final String encoded =
                        (String) encode.invoke(null, kamInfoId, id, prefix);

                final KamStoreObjectRef objRef =
                        (KamStoreObjectRef) decode.invoke(
                                null, encoded, type);

                assertTrue(kamInfoId == objRef.getKamInfoId());
                assertTrue(id == objRef.getKamStoreObjectId());
                assertTrue(typeClass.equals(objRef.getKamStoreObjectClass()));
            }
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testThatKamStoreObjectTypeRepsCanBeEncoded()
            throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {

        final CharsetEncoder asciiEncoder = ASCII.newEncoder();

        final int n = kamStoreObjectTypeClass.getEnumConstants().length;
        for (int i = 0; i < n; ++i) {
            Enum<?> type =
                    (Enum<?>) kamStoreObjectTypeClass.getEnumConstants()[i];
            Byte rep =
                    (Byte) typeEncode.invoke(null, asciiEncoder,
                            repField.getChar(type));
            if (rep == null) {
                fail(format(
                        "%s.%s must have a one-byte ASCII character representation!",
                        kamStoreObjectTypeClassName,
                        type.name()));
            }
        }
    }

    @Test
    public void testThatKamStoreObjectTypeRepsAreUnique()
            throws IllegalArgumentException,
            IllegalAccessException {
        final int n = kamStoreObjectTypeClass.getEnumConstants().length;
        for (int i = 1; i < n; ++i) {
            if (representations[i - 1] == representations[i]) {
                final Enum<?> prevType = (Enum<?>) kamStoreObjectTypeClass
                        .getEnumConstants()[repIndex[i - 1]];
                final Enum<?> type = (Enum<?>) kamStoreObjectTypeClass
                        .getEnumConstants()[repIndex[i]];

                fail(format(
                        "%s.%s and %s.%s have the same single character representation '%c'!",
                        kamStoreObjectTypeClassName,
                        prevType.name(),
                        kamStoreObjectTypeClassName,
                        type.name(),
                        repField.getChar(type)));
            }
        }
    }

    @Test
    public void testSortAndCreateIndex() throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {

        final int n = kamStoreObjectTypeClass.getEnumConstants().length;

        byte[] bytes = new byte[n];
        random.nextBytes(bytes);
        byte[] copy1 = Arrays.copyOf(bytes, n);
        byte[] copy2 = Arrays.copyOf(bytes, n);

        // Sort with sortAndCreateIndex and Arrays.sort.
        int[] index = (int[]) sortAndCreateIndex.invoke(null, copy1);
        Arrays.sort(copy2);
        assertTrue(Arrays.equals(copy1, copy2));

        // Check the index
        for (int i = 0; i < n; ++i) {
            assertTrue(bytes[index[i]] == copy1[i]);
        }
    }
}
