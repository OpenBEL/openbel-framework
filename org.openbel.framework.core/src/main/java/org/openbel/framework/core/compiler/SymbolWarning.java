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
package org.openbel.framework.core.compiler;

import static java.lang.String.format;
import static org.openbel.framework.common.BELUtilities.noItems;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openbel.framework.common.BELWarningException;
import org.openbel.framework.common.InvalidArgument;

/**
 * A BEL warning indicating an invalid symbol was encountered
 */
public class SymbolWarning extends BELWarningException {
    private static final long serialVersionUID = -7928587618855463703L;
    private final List<? extends ResourceSyntaxWarning> causes;

    /**
     * Creates a symbol warning with the supplied name, message, and cause.
     *
     * @param name Resource name failing validation
     * @param msg Message indicative of verification failure
     * @param cause The cause of the exception
     */
    public SymbolWarning(String name, String msg, Throwable cause) {
        super(name, msg, cause);
        causes = null;
    }

    /**
     * Creates a symbol warning with the supplied name and message, from the
     * provided {@link ResourceSyntaxWarning resource syntax warnings}.
     *
     * @param name Resource name failing validation
     * @param msg Message indicative of verification failure
     * @param causes List of resource syntax warnings
     * @throws InvalidArgument Thrown if {@code causes} is null or empty
     */
    public SymbolWarning(String name, String msg,
            List<? extends ResourceSyntaxWarning> causes) {
        super(name, msg);
        if (noItems(causes)) throw new InvalidArgument("causes has no items");
        this.causes = causes;
    }

    public List<? extends ResourceSyntaxWarning> getResourceSyntaxWarnings() {
        return causes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        if (causes != null && causes.size() > 1) {
            bldr.append("SYMBOL WARNINGS");
        } else {
            bldr.append("SYMBOL WARNING");
        }

        final String name = getName();
        if (name != null) {
            bldr.append(" in ");
            bldr.append(name);
        }
        bldr.append("\n\treason: ");

        final String msg = getMessage();
        if (msg != null) {
            bldr.append(msg);
        } else {
            bldr.append("Unknown");
        }

        if (causes != null) {
            Set<String> messages = new HashSet<String>();
            for (final ResourceSyntaxWarning rsw : causes) {
                messages.add(rsw.getUserFacingMessage());
            }
            final int numMessages = messages.size(), numCauses = causes.size();
            if (numCauses > numMessages) {
                bldr.append(format(", %d unique warning(s)", numMessages));
            }
            bldr.append("\n");

            for (final String message : messages) {
                bldr.append("\t\t");
                bldr.append(message);
                bldr.append("\n");
            }
        } else {
            bldr.append("\n");
        }

        return bldr.toString();
    }
}
