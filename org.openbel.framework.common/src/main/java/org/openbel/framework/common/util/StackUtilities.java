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

import static java.lang.Thread.currentThread;
import static java.lang.Thread.getAllStackTraces;

/**
 * Utility methods for accessing and leveraging the stack-based architecture of
 * the virtual machine.
 *
 */
public class StackUtilities {
    private final static String CLASS = StackUtilities.class.getName();

    /**
     * Returns the {@link StackTraceElement} of the caller.
     * <p>
     * You should consider use of this method carefully. Unwinding the stack
     * should be considered an expensive operation.
     * </p>
     *
     * @return StackTraceElement of the caller
     * @see StackTraceElement
     */
    public static StackTraceElement callerFrame() {
        final Thread me = currentThread();
        StackTraceElement[] se = me.getStackTrace();
        for (int i = 0; i < se.length; i++) {
            String sClass = se[i].getClassName();
            if (!sClass.equals(CLASS)) continue;
            String method = se[i].getMethodName();
            if (!"callerFrame".equals(method)) continue;
            if (i + 2 < se.length) return se[i + 2];
        }
        throw new IllegalStateException("bad stack");
    }

    /**
     * Returns your {@link StackTraceElement}.
     * <p>
     * You should consider use of this method carefully. Unwinding the stack
     * should be considered an expensive operation.
     * </p>
     *
     * @return StackTraceElement of the caller
     * @see StackTraceElement
     */
    public static StackTraceElement myFrame() {
        final Thread me = currentThread();
        StackTraceElement[] se = getAllStackTraces().get(me);
        for (int i = 0; i < se.length; i++) {
            String sClass = se[i].getClassName();
            if (!sClass.equals(CLASS)) continue;
            String method = se[i].getMethodName();
            if (!"myFrame".equals(method)) continue;
            if (i + 1 < se.length) return se[i + 1];
        }
        throw new IllegalStateException("bad stack");
    }

    /**
     * Default private constructor.
     */
    private StackUtilities() {

    }
}
