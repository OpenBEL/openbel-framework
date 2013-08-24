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
