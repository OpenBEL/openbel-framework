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
package org.openbel.framework.common;

import static java.io.File.separator;
import static java.lang.Integer.parseInt;
import static java.lang.System.arraycopy;
import static java.lang.Thread.currentThread;
import static java.lang.reflect.Array.newInstance;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.openbel.framework.common.PathConstants.BEL_SCRIPT_EXTENSION;
import static org.openbel.framework.common.PathConstants.XBEL_EXTENSION;
import static org.openbel.framework.common.Strings.SHA_256;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openbel.framework.common.bel.parser.BELParser;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.model.Term;

/**
 * Utility methods used throughout the BEL codebase.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public class BELUtilities {
    private static final long[] byteTable = createLookupTable();
    private static final long HMULT = 7664345821815920749L;

    private static final long HSTART = 0xBB40E64DA205B064L;
    private static int ONE_MEGABYTE = 1024 * 1024;
    private static int pid = -1;
    private static final int MIN_PORT = 0;
    private static final int MAX_PORT = 65535;
    private static final int MIN_EPHEMERAL_PORT = 49152;
    private static final Pattern NON_WORD_PATTERN = Pattern.compile("[\\W_]");
    private static final Set<String> _functions = new HashSet<String>();
    private static final Set<String> _relationships = new HashSet<String>();
    static {
        for (FunctionEnum fx : FunctionEnum.values()) {
            _functions.add(fx.getAbbreviation());
            _functions.add(fx.getDisplayValue());
        }
        for (RelationshipType r : RelationshipType.values()) {
            _relationships.add(r.getAbbreviation());
            _relationships.add(r.getDisplayValue());
        }
    }

    /**
     * Returns a hash set of type {@code E} optimized to the
     * {@link Collection#size() size} of the supplied {@link Collection
     * collection}.
     *
     * @param <E> Formal type parameter collection element
     * @param c {@link Collection}; may be null
     * @return Hash set of type {@code E}
     */
    public static <E> HashSet<E> asHashSet(final Collection<E> c) {
        if (noItems(c)) {
            return new HashSet<E>();
        }
        return new HashSet<E>(c);
    }

    /**
     * Inserts the platform-specific filesystem path separator between the
     * provided strings.
     *
     * @param strings
     * @return String in the following form:
     * {@code strings[0]<path_separator>strings[1]<path_separator>...<strings[n]>}
     * , or <b>null</b> if {@code strings} is null
     */
    public static String asPath(final String... strings) {
        if (strings == null) return null;
        final StringBuilder bldr = new StringBuilder();
        for (final String string : strings) {
            if (bldr.length() != 0) bldr.append(separator);
            bldr.append(string);
        }
        return bldr.toString();
    }

    /**
     * Inserts the platform-specific filesystem path separator between
     * {@code directory} and {@code filename} and returns the resulting string.
     *
     * @param directory Non-null string
     * @param filename Non-null string
     * @return String following the format
     * {@code directory<path_separator>filename}
     */
    public static String asPath(final String directory, final String filename) {
        return directory.concat(separator).concat(filename);
    }

    /**
     * Closes a {@link Closeable closeable resource} without reporting an
     * {@link IOException io error}.
     * <p>
     * This method is a no-op if {@code closeable} is {@code null}.
     * </p>
     *
     * @param closeable the {@link Closeable closeable} to close
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {}
        }
    }

    /**
     * Computes a 64bit hash code of a {@link CharSequence character sequence}.
     * <p>
     * Using a 64bit hash code minimizes collisions at the cost of size.
     * </p>
     *
     * @param cs the {@link CharSequence character sequence} to hash
     * @return the 64bit hash
     */
    public static long computeHash64(CharSequence cs) {
        long h = HSTART;
        final long hmult = HMULT;
        final long[] ht = byteTable;
        final int len = cs.length();
        for (int i = 0; i < len; i++) {
            char ch = cs.charAt(i);
            h = (h * hmult) ^ ht[ch & 0xff];
            h = (h * hmult) ^ ht[(ch >>> 8) & 0xff];
        }
        return h;
    }

    /**
     * Computes a SHA-256 hash of data from the {@link InputStream input}.
     *
     * @param input the data {@link InputStream input stream}, which cannot be
     * {@code null}
     * @return the {@link String SHA-256 hash}
     * @throws IOException Thrown if an IO error occurred reading from the
     * {@link InputStream input}
     * @throws InvalidArgument Thrown if {@code input} is {@code null}
     */
    public static String computeHashSHA256(final InputStream input)
            throws IOException {
        if (input == null) {
            throw new InvalidArgument("input", input);
        }

        MessageDigest sha256;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new MissingAlgorithmException(SHA_256, e);
        }

        final DigestInputStream dis = new DigestInputStream(input, sha256);
        while (dis.read() != -1) {}

        byte[] mdbytes = sha256.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }

        return sb.toString();
    }

    /**
     * Concatenates the two arrays {@code a} and {@code b} and returns the
     * result.
     *
     * @param a {@code int[]} a
     * @param b {@code int[]} b
     * @return {@code int[]} concatenation
     */
    public static int[] concatArrays(int[] a, int[] b) {
        final int alen = a.length;
        final int blen = b.length;
        if (alen == 0) {
            return b;
        }
        if (blen == 0) {
            return a;
        }
        final int[] result = new int[alen + blen];
        arraycopy(a, 0, result, 0, alen);
        arraycopy(b, 0, result, alen, blen);
        return result;
    }

    /**
     * Returns a hash map of type {@code K, V} optimized to trade memory
     * efficiency for CPU time.
     * <p>
     * Use constrained hash maps when the capacity of a hash map is known to be
     * greater than sixteen (the default initial capacity) and the addition of
     * elements beyond the map's capacity will not occur. The hash map
     * implementation will automatically adjust the size to the next nearest
     * power of two.
     * </p>
     *
     * @param <K> Formal type parameter key
     * @param <V> Formal type parameter value
     * @param s Initial hash map capacity
     * @return Hash map of type {@code K, V}
     */
    public static <K, V> HashMap<K, V> constrainedHashMap(final int s) {
        return new HashMap<K, V>(s, 1.0F);
    }

    /**
     * Returns a hash set of type {@code T} optimized to trade memory efficiency
     * for CPU time.
     * <p>
     * Use constrained hash sets when the capacity of a hash set is known to be
     * greater than sixteen (the default initial capacity) and the addition of
     * elements beyond the set's capacity will not occur. The hash set
     * implementation will automatically adjust the size to the next nearest
     * power of two.
     * </p>
     *
     * @param <T> Formal type parameter
     * @param s Initial hash set capacity
     * @return Hash set of type {@code T}
     */
    public static <T> HashSet<T> constrainedHashSet(final int s) {
        return new HashSet<T>(s, 1.0F);
    }

    /**
     * Copy bytes from the {@link InputStream input stream} to the
     * {@link OutputStream output stream} in 4 kilobyte increments.
     *
     * @param input the {@link InputStream input stream} to read from, which
     * cannot be null
     * @param output the {@link OutputStream output stream} to write to, which
     * cannot be null
     * @return the number of {@code bytes} read
     * @throws IOException Thrown if an IO error occurred while copying data
     * @throws InvalidArgument Thrown if either {@code input} or {@code output}
     * is {@code null}
     */
    public static long copy(final InputStream input, final OutputStream output)
            throws IOException {
        // guard against null streams
        if (input == null) {
            throw new InvalidArgument("input", input);
        }
        if (output == null) {
            throw new InvalidArgument("output", output);
        }

        byte[] buf = new byte[4096];
        long count = 0;
        int bytesRead = 0;
        while ((bytesRead = input.read(buf)) != -1) {
            output.write(buf, 0, bytesRead);
            count += bytesRead;
        }

        return count;
    }

    /**
     * Copies a {@link File source file} to a {@link File destination file}.
     *
     * @param src the {@link File source file}, which must be non-null and
     * readable
     * @param dest the {@link File destination file}, which must be non-null and
     * writable
     * @throws IOException Thrown if an IO error occurred copying the
     * {@link File src} to {@link File dest}
     * @throws InvalidArgument Thrown if
     * <ul>
     * <li>{@code src} or {@code dest} is {@code null}</li>
     * <li>{@code src} is not readable</li>
     * <li>{@code dest} is not writeable</li>
     * </ul>
     */
    public static void copyFile(final File src, final File dest)
            throws IOException {
        if (src == null || !src.canRead()) {
            throw new InvalidArgument("src", src);
        }

        if (dest == null || !dest.canWrite()) {
            throw new InvalidArgument("dest", dest);
        }

        // initialize streams/channels
        FileInputStream srcInput = null;
        FileChannel srcChannel = null;
        FileOutputStream destOutput = null;
        FileChannel destChannel = null;
        try {
            srcInput = new FileInputStream(src);
            srcChannel = srcInput.getChannel();
            destOutput = new FileOutputStream(dest);
            destChannel = destOutput.getChannel();

            // transfer from src to dest channel, one megabyte at a time
            long size = srcChannel.size();
            long pos = 0;
            long count = 0;
            while (pos < size) {
                count =
                        (size - pos) > ONE_MEGABYTE ? ONE_MEGABYTE
                                : (size - pos);
                pos += destChannel.transferFrom(srcChannel, pos, count);
            }
        } finally {
            // close channels and streams
            if (destChannel != null) {
                destChannel.close();
            }
            if (destOutput != null) {
                destOutput.close();
            }
            if (srcChannel != null) {
                srcChannel.close();
            }
            if (srcInput != null) {
                srcInput.close();
            }
        }
    }

    /**
     * Create the provided directory, and all necessary subdirectories, if they
     * do not already exist.
     *
     * @param directory Path to create
     * @throws RuntimeException Thrown if directory creation failed
     */
    public static void createDirectories(final String directory) {
        if (directory == null) return;
        final File f = new File(directory);
        if (!f.isDirectory()) {
            if (!f.mkdirs()) throw new RuntimeException("couldn't create "
                    + directory);
        }
    }

    /**
     * Create the provided directory, if it does not already exist.
     *
     * @param directory Path to create
     * @throws RuntimeException Thrown if directory creation failed
     */
    public static void createDirectory(final String directory) {
        if (directory == null) return;
        final File f = new File(directory);
        if (!f.isDirectory()) {
            if (!f.mkdir()) throw new RuntimeException("couldn't create "
                    + directory);
        }
    }

    /**
     * Deletes the directory {@code dir}, and all of its contents.
     *
     * @param dir {@link File}
     * @return boolean {@code true} if success, {@code false} otherwise
     */
    public static boolean deleteDirectory(final File dir) {
        if (!deleteDirectoryContents(dir)) {
            return false;
        }

        return dir.delete();
    }

    /**
     * Recursively deletes all files and folders within the directory
     * <tt>dir</tt>.
     *
     * @param dir {@link File}, the directory to empty contents for
     * @return boolean determines whether or not the delete was successful,
     * <tt>true</tt> if success, <tt>false</tt> otherwise
     */
    public static boolean deleteDirectoryContents(final File dir) {
        if (dir == null || !dir.isDirectory()) {
            return false;
        }

        File[] files = dir.listFiles();
        for (final File file : files) {
            if (file.isDirectory()) {
                if (!deleteDirectory(file)) {
                    return false;
                }
            } else {
                if (!file.delete()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns a {@link Set} view of the entries contained in this map.
     * <p>
     * This method should <strong>always</strong> be preferred instead of
     * iterating a maps keys and invoking {@link Map#get(Object) get()} on each
     * iteration. The following code should never be used:
     *
     * <pre>
     * <code>
     * Map<String, String> map = [...]
     * for (final String key : map.keySet()) {
     *     String value = map.get(key);
     *     // do something with key/value
     * }
     * </code>
     * </pre>
     *
     * Instead, write:
     *
     * <pre>
     * <code>
     * Map<String, String> map = [...]
     * for (final Entry<String, String> e : entries(map)) {
     *     // do something with e.getKey/e.getValue
     * }
     * </code>
     * </pre>
     *
     * </p>
     *
     * @param m {@link Map}; may be null
     * @return {@link Set} of {@link java.util.Map.Entry map entries}
     */
    public static <K, V> Set<Map.Entry<K, V>> entries(Map<K, V> m) {
        if (m == null) {
            return emptySet();
        }
        return m.entrySet();
    }

    /**
     * Extends {@code Object.equals()} to make {@code null}s equal.
     *
     * @param o1 an object
     * @param o2 another object
     * @return {@code true} if both {@code o1} and {@code o2} are null or
     * {@code o1} equals {@code o2} in the sense of
     * {@link Object#equals(Object)} .
     */
    public static boolean equals(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        }
        return o1.equals(o2);
    }

    /**
     * Returns the first cause (<em>primium movens</em>) for a {@link Throwable
     * throwable}.
     *
     * @param t the {@link Throwable throwable}
     * @return {@link Throwable} the first cause for the throwable, the original
     * throwable if this is the first cause, or <tt>null</tt> if the <tt>t</tt>
     * throwable was null
     */
    public static Throwable getFirstCause(Throwable t) {
        if (t == null) {
            return null;
        }

        Throwable cause = t;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        return cause;
    }

    /**
     * Returns the first message available in a stack trace.
     * <p>
     * This method can be used to obtain the first occurrence of
     * {@link Exception#getMessage() the exception message} through a series of
     * {@link Exception#getCause() causes}.
     * </p>
     *
     * @param t the {@link Throwable throwable}
     * @return {@link String}; may be null
     */
    public static String getFirstMessage(Throwable t) {
        if (t == null) {
            return null;
        }

        String ret = t.getMessage();
        Throwable cause = t;
        while (cause.getCause() != null) {
            cause = cause.getCause();
            if (cause.getMessage() != null) {
                ret = cause.getMessage();
            }
        }
        return ret;
    }

    /**
     * Returns the virtual machine's process identifier.
     *
     * @return {@code int}
     */
    public static int getPID() {
        if (pid == -1) {
            RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
            String name = mx.getName();
            String token = name.split("@")[0];
            pid = parseInt(token);
        }
        return pid;
    }

    /**
     * Returns the {@link Thread#currentThread() current thread's} identifier.
     *
     * @return {@code long}
     */
    public static long getThreadID() {
        return currentThread().getId();
    }

    /**
     * Returns {@code true} if the collection is non-null and non-empty,
     * {@code false} otherwise.
     *
     * @param c Collection, may be null
     * @return boolean
     */
    public static boolean hasItems(final Collection<?> c) {
        return c != null && !c.isEmpty();
    }

    /**
     * Returns {@code true} if the map is non-null and is non-empty,
     * {@code false} otherwise.
     *
     * @param <K> Captured key type
     * @param <V> Captured value type
     * @param m Map of type {@code <K, V>}, may be null
     * @return boolean
     */
    public static <K, V> boolean hasItems(final Map<K, V> m) {
        return m != null && !m.isEmpty();
    }

    /**
     * Returns {@code true} if the array is non-null and has a length greater
     * than zero, {@code false} otherwise.
     *
     * @param <T> Captured array type
     * @param t Array of type {@code <T>}
     * @return boolean
     */
    public static <T> boolean hasItems(final T[] t) {
        return t != null && t.length > 0;
    }

    /**
     * Returns {@code true} if the string is non-null and non-empty,
     * {@code false} otherwise.
     *
     * @param s String, may be null
     * @return boolean
     */
    public static boolean hasLength(final String s) {
        return s != null && !s.isEmpty();
    }

    /**
     * Returns an array-based variant of the provided indexed map. This method
     * <b>should not</b> be used if any of the keys used are greater than the
     * size of the map. This is very useful if you have a {@link Map map} where
     * each value is given an index starting from zero. This is a very common
     * pattern in the BEL framework.
     * <p>
     * Here is an example where the use of this method would be beneficial.
     * Given some {@code list} of 100,000 elements, an index is assigned to each
     * element of the list:
     *
     * <pre>
     * <code>
     * Map<Integer, MyObject> map = sizedHashMap(list.size());
     * for (int i = 0; i < list.size(); i++) {
     *     map.put(i, list.get(i));
     * }
     * </code>
     * </pre>
     *
     * A typical use of {@code map} follows:
     *
     * <pre>
     * <code>
     * for (int i = 0; i < map.size(); i++) {
     *     MyObject m = map.get(i);
     *     // use "m"
     * }
     * </code>
     * </pre>
     *
     * The problem with such code is the costly invocation to
     * {@link Map#get(Object)} on every iteration. This becomes very expensive
     * with very large collections.
     * </p>
     * <p>
     * Different ways of accessing the data becomes necessary (for example,
     * using a {@link List list} structure and calling {@link List#get(int)},
     * see guidelines below). Taking this a step further, we can very easily
     * rewrite this map as an array and get considerable speed improvements.
     *
     * <pre>
     * <code>
     * </code>
     * MyObject[] indexed = index(map);
     * for (int i = 0; i < indexed.length; i++) {
     *     MyObject m = indexed[i];
     *     // use "m"
     * }
     * </pre>
     *
     * Whatever costs you pay up front for indexing the map quickly vanish on
     * each iteration of the loop.
     * </p>
     * <p>
     * <h3>Guidelines</h3> Considering the following as a good baseline when it
     * comes to using large numbers of objects.
     * <ol>
     * <li>{@link Map#get(Object)} can be fast when using optimized maps and
     * hash functions though will usually perform worse than {@link List lists}.
     * <li>{@link List#get(int)} almost always beats {@link Map#get(Object)} in
     * performance.</li>
     * <li>Index access into an array always beats accessing either a
     * {@link List list} or {@link Map map}.
     * </ol>
     *
     * @param map The {@link Map map} to index. The keys used by the map
     * <b>should never</b> be greater than the {@link Map#size() size} of the
     * map. Under ideal conditions, each value {@code 0 <= x < = map.size()}
     * should have a key assigned. Otherwise, the resulting array will waste
     * memory (i.e., it will be a spare array).
     * @return {@code T[]}
     */
    public static <T> T[] index(Class<T> cls, Map<Integer, T> map) {
        final int size = map.size();
        @SuppressWarnings("unchecked")
        T[] ret = (T[]) newInstance(cls, size);
        Set<Entry<Integer, T>> entries = entries(map);

        for (final Entry<Integer, T> e : entries) {
            if (e.getKey() == null) continue;
            int key = e.getKey();
            T value = e.getValue();
            ret[key] = value;
        }
        return ret;
    }

    /**
     * Applies the function {@code fx} to each of the
     * {@link BELUtilities#entries(Map) entries} within the supplied map.
     *
     * @param map Non-null {@link Map}
     * @param fx Non-null {@link MapFunction} to apply
     */
    public static <K, V> void mapfx(Map<K, V> map, MapFunction<K, V> fx) {
        Set<Entry<K, V>> entries = entries(map);
        for (final Entry<K, V> entry : entries) {
            K key = entry.getKey();
            V value = entry.getValue();
            fx.apply(key, value);
        }
    }

    /**
     * Applies the search function {@code fx} to each of the items in the
     * {@link Iterable iterable}, returning the first found match or null.
     *
     * @param iter {@link Iterable}
     * @param fx {@link SearchFunction}
     * @return {@code <T>}
     */
    public static <T> T search(Iterable<T> iter, SearchFunction<T> fx) {
        for (final T t : iter) {
            if (fx.match(t)) return t;
        }
        return null;
    }

    /**
     * Returns true if a string contains one or more alphanumeric (i.e., the
     * {@code Alnum} character class) characters and nothing else.
     *
     * @param s {@link String}
     * @return boolean
     */
    public static boolean isAlphanumeric(String s) {
        if (!hasLength(s)) {
            return false;
        }
        Pattern p = Pattern.compile("\\p{Alnum}+");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * Returns {@code true} if {@link String s} is numeric (i.e., the
     * {@code Digit} POSIX character class) characters and nothing else.
     *
     * @param s {@link String}
     * @return boolean
     */
    public static boolean isNumeric(String s) {
        if (!hasLength(s)) {
            return false;
        }
        Pattern p = Pattern.compile("\\p{Digit}+");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * Returns {@code true} if {@code file} ends with
     * {@value PathConstants#BEL_SCRIPT_EXTENSION} or
     * {@value PathConstants#XBEL_EXTENSION}, {@code false} otherwise.
     *
     * @param file {@link File}
     * @return boolean
     */
    public static boolean isBELDocument(final File file) {
        if (file == null) {
            return false;
        }
        return isBELDocument(file.getPath());
    }

    /**
     * Returns {@code true} if {@code path} ends with
     * {@value PathConstants#BEL_SCRIPT_EXTENSION} or
     * {@value PathConstants#XBEL_EXTENSION}, {@code false} otherwise.
     *
     * @param path {@link String} path
     * @return boolean
     */
    public static boolean isBELDocument(final String path) {
        if (path == null) {
            return false;
        }
        if (path.endsWith(BEL_SCRIPT_EXTENSION)) {
            return true;
        }
        if (path.endsWith(XBEL_EXTENSION)) {
            return true;
        }
        return false;
    }

    /**
     * Returns {@code true} if {@code file} ends with
     * {@value PathConstants#BEL_SCRIPT_EXTENSION}, {@code false} otherwise.
     *
     * @param file {@link File}
     * @return boolean
     */
    public static boolean isBELScript(final File file) {
        if (file == null) {
            return false;
        }
        return isBELScript(file.getPath());
    }

    /**
     * Returns {@code true} if {@code path} ends with
     * {@value PathConstants#BEL_SCRIPT_EXTENSION}, {@code false} otherwise.
     *
     * @param path {@link String} path
     * @return boolean
     */
    public static boolean isBELScript(final String path) {
        if (path == null) {
            return false;
        }
        if (path.endsWith(BEL_SCRIPT_EXTENSION)) {
            return true;
        }
        return false;
    }

    /**
     * Returns {@code true} if {@code file} ends with
     * {@value PathConstants#XBEL_EXTENSION}, {@code false} otherwise.
     *
     * @param file {@link File}
     * @return boolean
     */
    public static boolean isXBEL(final File file) {
        if (file == null) {
            return false;
        }
        return isXBEL(file.getPath());
    }

    /**
     * Returns {@code true} if {@code path} ends with
     * {@value PathConstants#XBEL_EXTENSION}, {@code false} otherwise.
     *
     * @param path {@link String} path
     * @return boolean
     */
    public static boolean isXBEL(final String path) {
        if (path == null) {
            return false;
        }
        if (path.endsWith(XBEL_EXTENSION)) {
            return true;
        }
        return false;
    }

    /**
     * Joins the elements of the provided {@link Collection collection} into a
     * single {@link String string}, using the provided {@code separator} and
     * string elements.
     * <p>
     * For example: <br>
     * <blockquote> {@code join(asList("foo", "bar"), "*")}<br>
     * </blockquote> returns<br>
     * <blockquote> {@code "foo*bar"} </blockquote>
     * </p>
     *
     * @param strings {@link String Strings} to join together
     * @param separator Separator {@link String string}
     * @return String
     */
    public static String join(Collection<String> strings, String separator) {
        StringBuilder sb = new StringBuilder();
        if (strings != null) {
            Iterator<String> i = strings.iterator();
            while (i.hasNext()) {
                sb.append(i.next());
                if (i.hasNext()) {
                    sb.append(separator);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Returns {@code true} if the collection is null or empty, {@code false}
     * otherwise.
     *
     * @param c Collection, may be null
     * @return boolean
     */
    public static boolean noItems(final Collection<?> c) {
        return !hasItems(c);
    }

    /**
     * Returns {@code true} if the map is null or empty, {@code false}
     * otherwise.
     *
     * @param <K> Captured key type
     * @param <V> Captured value type
     * @param m Map of type {@code <K, V>}, may be null
     * @return boolean
     */
    public static <K, V> boolean noItems(final Map<K, V> m) {
        return !hasItems(m);
    }

    /**
     * Returns {@code true} if the array is null or has no elements,
     * {@code false} otherwise.
     *
     * @param <T> Captured array type
     * @param t Array of type {@code <T>}, may be null
     * @return boolean
     */
    public static <T> boolean noItems(final T[] t) {
        return !hasItems(t);
    }

    /**
     * Returns {@code true} if the string is null or empty, {@code false}
     * otherwise.
     *
     * @param s String, may be null
     * @return boolean
     */
    public static boolean noLength(final String s) {
        return !hasLength(s);
    }

    /**
     * Returns {@code true} if any {@link String} in <tt>strings</tt> is null or
     * empty, {@code false} otherwise.
     *
     * @param strings {@link String[]}, may be null
     * @return boolean
     */
    public static boolean noLength(final String... strings) {
        if (strings == null) {
            return true;
        }

        for (final String string : strings) {
            if (!hasLength(string)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns {@code true} if no null arguments are provided, {@code false}
     * otherwise.
     *
     * @param objects Objects, may be null
     * @return boolean
     */
    public static boolean noNulls(final Object... objects) {
        if (objects == null) return false;
        for (final Object object : objects) {
            if (object == null) return false;
        }
        return true;
    }

    /**
     * Returns {@code true} if null arguments are provided, {@code false}
     * otherwise.
     *
     * @param objects Objects, may be null
     * @return boolean
     */
    public static boolean nulls(final Object... objects) {
        if (objects == null) return true;
        for (final Object object : objects) {
            if (object == null) return true;
        }
        return false;
    }

    /**
     * Returns a hash map of type {@code K, V} optimized to the specified
     * capacity and load factor.
     * <p>
     * Use optimized hash maps when the capacity of a hash map is known to be
     * greater than sixteen (the default initial capacity) and a load factor is
     * desired to control resizing behavior. The hash map implementation will
     * automatically adjust the size to the next nearest power of two.
     * </p>
     *
     * @param <K> Formal type parameter key
     * @param <V> Formal type parameter value
     * @param s Initial hash map capacity
     * @param lf Hash map load factor
     * @return Hash map of type {@code K, V}
     */
    public static <K, V> HashMap<K, V> optimizedHashMap(final int s,
            final float lf) {
        return new HashMap<K, V>(s, lf);
    }

    /**
     * Returns a hash set of type {@code T} optimized to the specified capacity
     * and load factor.
     * <p>
     * Use optimized hash sets when the capacity of a hash set is known to be
     * greater than sixteen (the default initial capacity) and a load factor is
     * desired to control resizing behavior. The hash set implementation will
     * automatically adjust the size to the next nearest power of two.
     * </p>
     *
     * @param <T> Formal type parameter
     * @param s Initial hash set capacity
     * @param lf Hash set load factor
     * @return Hash set of type {@code T}
     */
    public static <T> HashSet<T> optimizedHashSet(final int s, final float lf) {
        return new HashSet<T>(s, lf);
    }

    /**
     * Returns {@code true} if {@code file} is non-null and can be read,
     * {@code false} otherwise.
     *
     * @param file File; may be null
     * @return boolean
     */
    public static boolean readable(final File file) {
        if (file != null && file.canRead()) return true;
        return false;
    }

    /**
     * Returns a sized array list of type {@code T}.
     *
     * @param <T> Formal type parameter
     * @param size Array list size
     * @return Array list of type {@code T}
     */
    public static <T> ArrayList<T> sizedArrayList(final int size) {
        return new ArrayList<T>(size);
    }

    /**
     * Returns a hash map of type {@code K, V} with initial capacity
     * {@code size}.
     * <p>
     * Use sized hash maps when the capacity of a hash map is known to be
     * greater than sixteen (the default initial capacity). The hash map
     * implementation will automatically adjust the size to the next nearest
     * power of two.
     * </p>
     *
     * @param <K> Formal type parameter key
     * @param <V> Formal type parameter value
     * @param size Hash map initial capacity
     * @return Hash map of type {@code K, V}
     */
    public static <K, V> HashMap<K, V> sizedHashMap(final int size) {
        return new HashMap<K, V>(size);
    }

    /**
     * Returns a hash set of type {@code T} with initial capacity {@code size}.
     * <p>
     * Use sized hash sets when the capacity of a hash set is known to be
     * greater than sixteen (the default initial capacity). The hash set
     * implementation will automatically adjust the size to the next nearest
     * power of two.
     * </p>
     *
     * @param <T> Formal type parameter
     * @param size Hash set initial capacity
     * @return Hash set of type {@code T}
     */
    public static <T> HashSet<T> sizedHashSet(final int size) {
        return new HashSet<T>(size);
    }

    /**
     * Check equality of two substrings. This method does not create
     * intermediate {@link String} objects and is roughly equivalent to:
     *
     * <pre>
     * <code>
     * String sub1 = s1.substring(fromIndex1, toIndex1);
     * String sub2 = s2.substring(fromIndex2, toIndex2);
     * sub1.equals(sub2);
     * </code>
     * </pre>
     *
     * @param s1 First string
     * @param fromIndex1 Starting index within {@code s1}
     * @param toIndex1 Ending index within {@code s1}
     * @param s2 Second string
     * @param fromIndex2 Starting index within {@code s2}
     * @param toIndex2 Ending index within {@code s2}
     * @return {@code boolean}
     */
    public static boolean substringEquals(final String s1,
            final int fromIndex1, final int toIndex1,
            final String s2, final int fromIndex2, final int toIndex2) {

        if (toIndex1 < fromIndex1) {
            throw new IndexOutOfBoundsException();
        }
        if (toIndex2 < fromIndex2) {
            throw new IndexOutOfBoundsException();
        }

        final int len1 = toIndex1 - fromIndex1;
        final int len2 = toIndex2 - fromIndex2;
        if (len1 != len2) {
            return false;
        }

        for (int i = 0; i < len1; ++i) {
            if (s1.charAt(fromIndex1 + i) != s2.charAt(fromIndex2 + i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Converts into seconds and returns a string in the format
     * {@code <seconds>.<milliseconds>}.
     *
     * @param milliseconds
     * @return String
     */
    public static String timeFormat(final long milliseconds) {
        double seconds = milliseconds / 1000.0d;
        final NumberFormat fmt = new DecimalFormat("#0.000");
        return fmt.format(seconds);
    }

    /**
     * Captures all objects of type {@code <T>} contained in the provided list
     * as a new checked list.
     *
     * @param <T> Captured type for new checked list
     * @param objects List of objects
     * @param t Class type to capture
     * @return Checked list of type {@code T}; may be empty
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> typedList(List<?> objects, Class<T> t) {
        if (objects == null || objects.isEmpty()) {
            return emptyList();
        }
        List<T> ret = new ArrayList<T>();
        for (final Object o : objects) {
            Class<?> oc = o.getClass();
            if (oc == t || t.isAssignableFrom(oc)) {
                ret.add((T) o);
            }
        }
        return ret;
    }

    private static final long[] createLookupTable() {
        long[] byteTable = new long[256];
        long h = 0x544B2FBACAAF1684L;
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 31; j++) {
                h = (h >>> 7) ^ h;
                h = (h << 11) ^ h;
                h = (h >>> 10) ^ h;
            }
            byteTable[i] = h;
        }
        return byteTable;
    }

    /**
     * Retrieve an available ephemeral port.
     *
     * <p>
     * The number of attempts to obtain an available port is controlled by
     * {@code attempts}.  A {@code -1} is returned if no available port could
     * be found in this number of attempts.
     * </p>
     *
     * @see http://en.wikipedia.org/wiki/Ephemeral_port
     * @return {@code int} the available ephemeral port, or {@code -1} if an
     * ephemeral port could not be found.
     */
    public static int ephemeralPort() {
        for (int i = MIN_EPHEMERAL_PORT; i < MAX_PORT; i++) {
            if (portAvailable(i)) return i;
        }
        return -1;
    }

    /**
     * Checks whether {@code port} is available.
     *
     * @param port the port to check, which must be between
     * {@link BELUtilities#MIN_PORT} and {@link BELUtilities#MAX_PORT}
     * @return {@code true} if the port is available for use, {@false}
     * otherwise
     */
    public static boolean portAvailable(int port) {
        if (port < MIN_PORT || port > MAX_PORT) {
            throw new InvalidArgument("port is out of range");
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            // Do nothing
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }
    
    /**
     * Return a quoted {@link String param} if necessary.  A quoted parameter
     * is required in order to parse as a {@link Term BEL term} using the
     * {@link BELParser BEL parser}.
     * 
     * @param param {@link String}; {@code null} returns {@code null}
     * @return quoted {@link String} if necessary, the original
     * {@link String param}, or {@code null} if {@code param} was {@code null}
     */
    public static String quoteParameter(final String param) {
        // return null if null
        if (noLength(param))
            return param;
        
        // return immediately if already quoted
        if (param.startsWith("\"") && param.endsWith("\""))
            return param;

        // return quoted if string contains non-word character
        Matcher m = NON_WORD_PATTERN.matcher(param);
        if (m.find()) {
            return "\"" + param + "\"";
        }
        
        // return quoted if string matches a function
        if (_functions.contains(param))
            return "\"" + param + "\"";
        
        // return quoted if string matches a relationship
        if (_relationships.contains(param))
            return "\"" + param + "\"";
        
        // return as is
        return param;
    }

    /**
     * Default private constructor.
     */
    private BELUtilities() {
    }
}
