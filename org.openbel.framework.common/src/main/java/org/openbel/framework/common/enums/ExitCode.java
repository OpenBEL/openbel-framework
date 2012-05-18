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
package org.openbel.framework.common.enums;

import static org.openbel.framework.common.Strings.*;

import java.util.HashMap;
import java.util.Map;

import org.openbel.framework.common.Strings;

/**
 * BEL exit codes and their respective messages.
 * <p>
 * Pedantic error return code start at <tt>101</tt>.
 * </p>
 * <p>
 * Portions of this enum have been automatically generated.
 * </p>
 */
public enum ExitCode {

    /**
     * Success.
     */
    SUCCESS(0, Strings.SUCCESS),

    /**
     * General failure.
     */
    GENERAL_FAILURE(1, Strings.GENERAL_FAILURE),

    /**
     * Unrecoverable error.
     */
    UNRECOVERABLE_ERROR(2, Strings.UNRECOVERABLE_ERROR_MSG),

    /**
     * All documents failed validation.
     */
    NO_VALID_DOCUMENTS(3, ALL_DOCUMENTS_FAILED_VALIDATION),

    /**
     * All documents failed conversion.
     */
    NO_CONVERTED_DOCUMENTS(4, ALL_DOCUMENTS_FAILED_CONVERSION),

    /**
     * No proto-networks saved.
     */
    NO_PROTO_NETWORKS_SAVED(5, NO_NETWORKS_SAVED),

    /**
     * No XBEL documents.
     */
    NO_XBEL_DOCUMENTS(6, Strings.NO_XBEL_DOCUMENTS),

    /**
     * Bad output directory.
     */
    BAD_OUTPUT_DIRECTORY(7, BAD_OUTPUT_DIR),

    /**
     * Invalid namespace resource location.
     */
    INVALID_NAMESPACE_RESOURCE_LOCATION(8,
            Strings.INVALID_NAMESPACE_RESOURCE_LOCATION),

    /**
     * Parse error.
     */
    PARSE_ERROR(9, Strings.PARSE_ERROR),

    /**
     * KAM connection failure.
     */
    KAM_CONNECTION_FAILURE(10, Strings.KAM_CONNECTION_FAILURE),

    /**
     * No global proto network.
     */
    NO_GLOBAL_PROTO_NETWORK(11, Strings.NO_GLOBAL_PROTO_NETWORK),

    /**
     * Failed to merge proto-networks.
     */
    FAILED_TO_MERGE_PROTO_NETWORKS(12, Strings.NETWORK_MERGE_FAILURE),

    /**
     * Missing system configuration.
     */
    MISSING_SYSTEM_CONFIGURATION(13, Strings.MISSING_SYSCFG),

    /**
     * Failure loading into the KAM catalog.
     */
    KAM_CATALOG_LOAD_FAILED(14, Strings.KAM_CATALOG_LOAD_FAILED),

    /**
     * Failure deleting a directory.
     */
    DIRECTORY_DELETION_FAILED(15, Strings.DIRECTORY_DELETION_FAILED),

    /**
     * Failed creating a directory.
     */
    DIRECTORY_CREATION_FAILED(16, Strings.DIRECTORY_CREATION_FAILED),

    /**
     * Failed loading the KAM.
     */
    KAM_LOAD_FAILURE(17, Strings.KAM_LOAD_FAILURE),

    /**
     * Out of memory.
     */
    OOM_ERROR(42, Strings.OOM),

    // PEDANTIC RETURN CODES BEYOND HERE

    /**
     * Pedantic validation failure.
     */
    VALIDATION_FAILURE(101, PEDANTIC_VALIDATION_FAILURE),

    /**
     * Pedantic conversion failure.
     */
    CONVERSION_FAILURE(102, PEDANTIC_CONVERSION_FAILURE),

    /**
     * Pedantic namespace resolution failure.
     */
    NAMESPACE_RESOLUTION_FAILURE(103, PEDANTIC_NAMESPACE_RESOLUTION_FAILURE),

    /**
     * Pedantic namespace indexing failure.
     */
    NAMESPACE_INDEXING_FAILURE(104, PEDANTIC_NAMESPACE_INDEXING_FAILURE),

    /**
     * Pedantic semantic verification failure.
     */
    SEMANTIC_VERIFICATION_FAILURE(105, PEDANTIC_SEMANTIC_VERIFICATION_FAILURE),

    /**
     * Pedantic symbol verification failure.
     */
    SYMBOL_VERIFICATION_FAILURE(106, PEDANTIC_SYMBOL_VERIFICATION_FAILURE),

    /**
     * Pedantic proto-network save failure.
     */
    PROTO_NETWORK_SAVE_FAILURE(107, PEDANTIC_PROTO_NETWORK_SAVE_FAILURE),

    /**
     * Term expansion failure.
     */
    TERM_EXPANSION_FAILURE(108, PEDANTIC_TERM_EXPANSION_FAILURE),

    /**
     * Statement expansion failure.
     */
    STATEMENT_EXPANSION_FAILURE(109, PEDANTIC_STATEMENT_EXPANSION_FAILURE);

    /* Value unique to each enumeration. */
    private final Integer value;
    /* Enumeration error message. */
    private String errorMessage;

    /* Static cache of enum by string representation. */
    private static final Map<String, ExitCode> STRINGTOENUM =
            new HashMap<String, ExitCode>(values().length, 1F);

    static {
        for (final ExitCode e : values())
            STRINGTOENUM.put(e.toString(), e);
    }

    /**
     * Constructor for setting enum and display value.
     *
     * @param value Enum value
     * @param errorMessage Error message
     */
    private ExitCode(Integer value, String errorMessage) {
        this.value = value;
        this.errorMessage = errorMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "(" + value + ") " + errorMessage;
    }

    /**
     * Returns the exit code's value, for {@link System#exit system exit}.
     *
     * @return value
     * @see java.lang.Enum#ordinal() Contrast with {@code ordinal}
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Returns the exit code's error message.
     *
     * @return error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Returns the exit code by its string representation (case-insensitive).
     *
     * @param s ExitCode string representation
     * @return ExitCode, may be null if the provided string has no ExitCode
     * representation
     */
    public static ExitCode getExitCode(final String s) {
        ExitCode e = STRINGTOENUM.get(s);
        if (e != null) return e;

        for (final String dispval : STRINGTOENUM.keySet()) {
            if (dispval.equalsIgnoreCase(s))
                return STRINGTOENUM.get(dispval);
        }

        return null;
    }

    /**
     * Returns {@code true} if exit code is {@link #SUCCESS}, {@code false}
     * otherwise.
     *
     * @return boolean
     */
    public boolean isSuccess() {
        return isSuccess(this);
    }

    /**
     * Returns {@code true} if {@code e} is {@link #SUCCESS}, {@code false}
     * otherwise.
     *
     * @param e {@link ExitCode}
     * @return boolean
     */
    public static boolean isSuccess(final ExitCode e) {
        return e == SUCCESS ? true : false;
    }

    /**
     * Returns {@code true} if exit code is not {@link #SUCCESS}, {@code false}
     * otherwise.
     *
     * @return boolean
     */
    public boolean isFailure() {
        return isFailure(this);
    }

    /**
     * Returns {@code true} if {@code e} is not {@link #SUCCESS}, {@code false}
     * otherwise.
     *
     * @param e {@link ExitCode}
     * @return boolean
     */
    public static boolean isFailure(final ExitCode e) {
        return e != SUCCESS ? true : false;
    }
}
