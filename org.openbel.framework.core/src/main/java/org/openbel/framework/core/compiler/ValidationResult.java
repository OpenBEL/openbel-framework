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

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * Container Object to hold all warnings and errors that occurred as the result
 * of a validation action.
 *
 * @author Steve Ungerer
 */
public class ValidationResult {
    private final List<String> warnings;
    private final List<String> errors;

    public ValidationResult(List<String> warnings,
            List<String> errors) {
        if (warnings == null) {
            throw new IllegalArgumentException("warnings must not be null");
        }
        this.warnings = warnings;
        if (errors == null) {
            throw new IllegalArgumentException("errors must not be null");
        }
        this.errors = errors;
    }

    /**
     * @return {@link List} of warnings that occurred, never
     *         <code>null</code>
     */
    public List<String> getWarnings() {
        return warnings;
    }

    /**
     * @return {@link List} of errors that occurred, never <code>null</code>
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * @return true if the object has warnings, false if not
     */
    public boolean hasWarnings() {
        return CollectionUtils.isNotEmpty(warnings);
    }

    /**
     * @return true if the object has errors, false if not
     */
    public boolean hasErrors() {
        return CollectionUtils.isNotEmpty(errors);
    }

}
