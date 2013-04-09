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
package org.openbel.framework.core.compiler;

import static org.openbel.framework.common.BELUtilities.noItems;

import java.util.List;

import org.openbel.framework.common.BELWarningException;
import org.openbel.framework.common.InvalidArgument;

/**
 * A BEL warning indicating semantic verification has failed for a
 * {@link org.openbel.framework.common.model.Document BEL document}.
 */
public class SemanticFailure extends BELWarningException {
    private static final long serialVersionUID = 1850854733665972431L;
    private final List<SemanticWarning> causes;

    /**
     * Creates a semantic failure with the supplied name, message, and causes.
     *
     * @param name
     *            Resource name failing validation
     * @param msg
     *            Message indicative of semantic failure
     * @param causes
     *            List of {@link SemanticWarning semantic exceptions}
     * @throws InvalidArgument
     *             Thrown if {@code causes} is null or empty
     */
    public SemanticFailure(String name, String msg,
            List<SemanticWarning> causes) {
        super(name, msg);
        if (noItems(causes))
            throw new InvalidArgument("causes has no items");
        this.causes = causes;
    }

    /**
     * Creates a semantic failure with the supplied name, message, and cause.
     *
     * @param name
     *            Resource name failing validation
     * @param msg
     *            Message indicative of semantic failure
     * @param cause
     *            The cause of the exception
     */
    public SemanticFailure(String name, String msg, Throwable cause) {
        super(name, msg, cause);
        this.causes = null;
    }

    /**
     * Obtain all {@link SemanticWarning}s that caused the
     * {@link SemanticFailure}
     *
     * @return the {@link List} of {@link SemanticWarning}s
     */
    public List<SemanticWarning> getCauses() {
        return causes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        bldr.append("SEMANTIC FAILURE");

        final String name = getName();
        if (name != null) {
            bldr.append(" in ");
            bldr.append(name);
        }

        bldr.append("\n\treason: ");
        final String msg = getMessage();
        if (msg != null)
            bldr.append(msg);
        else
            bldr.append("Unknown");
        bldr.append("\n");

        if (causes != null) {
            for (final SemanticWarning se : causes) {
                String[] userMsg = se.getUserFacingMessage().split("\n");
                for (final String s : userMsg) {
                    bldr.append("\n\t");
                    bldr.append(s);
                }
            }
            return bldr.toString();
        }
        bldr.append(getCause());
        return bldr.toString();
    }

}
