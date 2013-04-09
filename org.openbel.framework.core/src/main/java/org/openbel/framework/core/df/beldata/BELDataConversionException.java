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
package org.openbel.framework.core.df.beldata;

import static org.openbel.framework.common.BELUtilities.getFirstCause;
import static org.openbel.framework.common.BELUtilities.getFirstMessage;

import org.openbel.framework.common.BELErrorException;

public class BELDataConversionException extends BELErrorException {
    private static final long serialVersionUID = -6828651970299348458L;
    private static final String MSG_FMT =
            "Cannot parse '%s' for property name '%s'.";
    private final String propertyName;
    private final String propertyValue;

    public BELDataConversionException(String resourceLocation,
            String propertyName,
            String propertyValue) {
        super(resourceLocation, buildMessage(propertyName, propertyValue, null));
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public BELDataConversionException(String resourceLocation,
            String propertyName,
            String propertyValue, Throwable cause) {
        super(resourceLocation,
                buildMessage(propertyName, propertyValue, cause), cause);
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        bldr.append("PARSE ERROR");
        final String name = getName();
        if (name != null) {
            bldr.append(" in ");
            bldr.append(name);
        }
        bldr.append("\n\treason: ");
        bldr.append(String.format(MSG_FMT, propertyValue, propertyName));
        bldr.append("\n");
        return bldr.toString();
    }

    private static String buildMessage(final String propertyName,
            final String propertyValue, final Throwable cause) {

        final StringBuilder bldr = new StringBuilder();
        bldr.append(String.format(MSG_FMT, propertyValue, propertyName));

        if (cause != null) {
            bldr.append(" (reason: ");
            String reason = getFirstMessage(cause);
            if (reason == null) {
                reason = getFirstCause(cause).getClass().getSimpleName();
            }
            bldr.append(reason);
            bldr.append(")");
        }

        return bldr.toString();
    }
}
