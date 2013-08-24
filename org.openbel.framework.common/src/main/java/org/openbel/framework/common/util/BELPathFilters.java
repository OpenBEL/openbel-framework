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
package org.openbel.framework.common.util;

import static org.openbel.framework.common.BELUtilities.nulls;
import static org.openbel.framework.common.PathConstants.BEL_SCRIPT_EXTENSION;
import static org.openbel.framework.common.PathConstants.GLOBAL_PROTO_NETWORK_FILENAME;
import static org.openbel.framework.common.PathConstants.XBEL_EXTENSION;

import java.io.File;
import java.io.FileFilter;

import org.openbel.framework.common.PathConstants;

/**
 * BEL file filters, useful for capturing various BEL files by extension.
 *
 */
public final class BELPathFilters {

    /**
     * Returns {@code true} if {@code pathname} ends with {@code extension},
     * ignoring case, {@code false} otherwise.
     *
     * @param extension File extension
     * @param pathname Path name
     * @return boolean
     */
    private static boolean accept(final String extension, final File pathname) {
        if (nulls(extension, pathname)) return false;
        String absPath = pathname.getAbsolutePath();
        final int extLength = extension.length();
        int start = absPath.length() - extLength, end = absPath.length();
        String ext = absPath.substring(start, end);
        if (extension.equalsIgnoreCase(ext)) return true;
        return false;
    }

    /**
     * XBEL file filter for files with the {@code .xbel} extension (case
     * insensitive).
     *
     */
    public static class XBELFileFilter implements FileFilter {
        private final static String EXTENSION = XBEL_EXTENSION;

        /**
         * Returns {@code true} if the pathname ends with {@code .xbel},
         * ignoring case, {@code false} otherwise.
         *
         * @return boolean
         */
        @Override
        public boolean accept(final File pathname) {
            return BELPathFilters.accept(EXTENSION, pathname);
        }

    }

    /**
     * BEL file filter for files with the {@code .bel} extension (case
     * insensitive).
     *
     */
    public static class BELFileFilter implements FileFilter {
        private final static String EXTENSION = BEL_SCRIPT_EXTENSION;

        /**
         * Returns {@code true} if the provided pathname ends with {@code .bel},
         * ignoring case, {@code false} otherwise.
         *
         * @return boolean
         */
        @Override
        public boolean accept(final File pathname) {
            return BELPathFilters.accept(EXTENSION, pathname);
        }

    }

    /**
     * Proto-network filter for directories ending with the {@code .pn} suffix
     * (case sensitive).
     *
     */
    public static class ProtonetworkFilter implements FileFilter {
        /**
         * Returns {@code true} if the provided pathname is a directory with
         * a name that is an integer greater than or equal to 1,
         * {@code false} otherwise.
         *
         * @return boolean
         */
        @Override
        public boolean accept(File pathname) {
            if (pathname == null) return false;
            if (!pathname.isDirectory()) return false;
            try {
                return (Integer.parseInt(pathname.getName()) >= 1);
            } catch (NumberFormatException ex) {}
            return false;
        }

    }

    /**
     * Global proto-network filter for paths ending with the
     * {@link PathConstants#GLOBAL_PROTO_NETWORK_FILENAME} filename.
     *
     */
    public static class GlobalProtonetworkFilter implements FileFilter {
        private final static String FILENAME = GLOBAL_PROTO_NETWORK_FILENAME;

        /**
         * Returns {@code true} if the provided pathname is a path ending with
         * the filename {@link PathConstants#GLOBAL_PROTO_NETWORK_FILENAME},
         * {@code false} otherwise.
         *
         * @return boolean
         */
        @Override
        public boolean accept(File pathname) {
            if (pathname == null) return false;
            if (pathname.isDirectory()) return false;
            if (pathname.getName().equals(FILENAME)) return true;
            return false;
        }

    }

}
