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
package org.openbel.framework.core.compiler;

import static org.openbel.framework.common.BELUtilities.noItems;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.openbel.framework.common.BELWarningException;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.enums.SemanticStatus;
import org.openbel.framework.common.lang.Signature;

/**
 * A BEL warning indicating invalid BEL semantics were encountered.
 * <p>
 * TODO: Change output to expected/provided form instead of current
 * signature-based output.
 * E.g., {@code function argument is not valid: expected foo, got bar}
 * </p>
 */
public class SemanticWarning extends BELWarningException {
    private static final long serialVersionUID = 3772934427619446793L;
    private final Signature suppliedSignature;
    private final Map<Signature, SemanticStatus> signatureStatus;

    /**
     * Creates a semantic warning with the supplied name and message.
     * 
     * @param name Resource name failing validation
     * @param msg Message indicative of semantic failure
     */
    public SemanticWarning(String name, String msg) {
        super(name, msg);
        suppliedSignature = null;
        signatureStatus = null;
    }

    /**
     * Creates a semantic warning with the supplied name, message, and cause.
     * 
     * @param name Resource name failing validation
     * @param msg Message indicative of semantic failure
     * @param cause The cause of the exception
     */
    public SemanticWarning(String name, String msg, Throwable cause) {
        super(name, msg, cause);
        suppliedSignature = null;
        signatureStatus = null;
    }

    /**
     * Creates a semantic warning with the supplied name and message. The
     * {@code suppliedSignature} is the signature failing semantic verification.
     * The supplied {@code map} contains the semantic status of the failing
     * signature against all signatures of a function. provided signature-status
     * map and supplied signature.
     * 
     * @param name Resource name failing validation
     * @param msg Message indicative of semantic failure
     * @param suppliedSignature Signature failing semantic verification
     * @param map Map of signatures to semantic status
     */
    public SemanticWarning(String name, String msg,
            Signature suppliedSignature,
            Map<Signature, SemanticStatus> map) {
        super(name, msg);
        if (noItems(map)) throw new InvalidArgument("map has no items");
        if (suppliedSignature == null)
            throw new InvalidArgument("invalid supplied signature");
        this.signatureStatus = map;
        this.suppliedSignature = suppliedSignature;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserFacingMessage() {
        final StringBuilder bldr = new StringBuilder();

        if (signatureStatus != null && signatureStatus.size() > 1) {
            bldr.append("SEMANTIC WARNINGS");
        } else {
            bldr.append("SEMANTIC WARNING");
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

        bldr.append("\n");

        if (signatureStatus != null) {
            // Signature status is non-null, suppliedSignature is as well
            bldr.append("\tsignature: ");
            bldr.append(suppliedSignature);
            bldr.append("\n");
            bldr.append("\tfunction signatures: ");
            bldr.append(signatureStatus.size());
            bldr.append("\n");

            final Set<Entry<Signature, SemanticStatus>> entrySet =
                    signatureStatus.entrySet();

            for (final Entry<Signature, SemanticStatus> entry : entrySet) {
                Signature sig = entry.getKey();
                SemanticStatus status = entry.getValue();
                bldr.append("\t\t");
                bldr.append(status);
                bldr.append(" for signature ");
                bldr.append(sig);
                bldr.append("\n");
            }
        }

        return bldr.toString();
    }
}
