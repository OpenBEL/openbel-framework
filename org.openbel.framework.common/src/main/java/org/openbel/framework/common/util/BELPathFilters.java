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
