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
package org.openbel.framework.common.enums;

import static org.openbel.framework.common.DBConstants.DERBY_JDBC_DRIVER_CLASS;
import static org.openbel.framework.common.DBConstants.MYSQL_JDBC_DRIVER_CLASS;
import static org.openbel.framework.common.DBConstants.ORACLE_JDBC_DRIVER_CLASS;
import static org.openbel.framework.common.DBConstants.POSTGRESQL_JDBC_DRIVER_CLASS;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumerated representation of DatabaseType.
 * <p>
 * Portions of this enum have been automatically generated.
 * </p>
 */
public enum DatabaseType {

    /**
     * <a href="http://db.apache.org/derby/">Apache Derby</a>.
     */
    DERBY(0, "Derby", DERBY_JDBC_DRIVER_CLASS),

    /**
     * <a href="http://www.mysql.com/">MySQL</a>.
     */
    MYSQL(1, "MySQL", MYSQL_JDBC_DRIVER_CLASS),

    /**
     * <a href="http://www.oracle.com/us/products/database/index.html">Oracle
     * </a>.
     */
    ORACLE(2, "Oracle", ORACLE_JDBC_DRIVER_CLASS),

    /**
     * <a href="http://www.postgresql.org/">PostgreSQL</a>.
     */
    POSTGRESQL(3, "PostgreSQL", POSTGRESQL_JDBC_DRIVER_CLASS);

    /* Value unique to each enumeration. */
    private final Integer value;
    /* Enumeration display value. */
    private String displayValue;
    /* Database driver class name. */
    private String className;

    /**
     * Static null output stream to swallow output. !Used to disable derby log!
     * FIXME Remove this
     */
    public static final OutputStream NULL_OUTPUT_STREAM = new OutputStream() {
        @Override
        public void write(int b) throws IOException {
        }
    };

    /* Static cache of enum by string representation. */
    private static final Map<String, DatabaseType> STRINGTOENUM =
            new HashMap<String, DatabaseType>(values().length, 1F);

    static {
        for (final DatabaseType e : values())
            STRINGTOENUM.put(e.toString(), e);
    }

    /**
     * Constructor for setting enum and display value.
     *
     * @param value Enum value
     * @param displayValue Display value
     * @param className The driver's class name
     */
    private DatabaseType(Integer value, String displayValue, String className) {
        this.value = value;
        this.displayValue = displayValue;
        this.className = className;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return displayValue;
    }

    /**
     * Returns the database type's value.
     *
     * @return value
     * @see java.lang.Enum#ordinal() Contrast with {@code ordinal}
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Returns the database type's driver class name.
     *
     * @return {@link String}
     */
    public String getDriverClassName() {
        return className;
    }

    /**
     * Returns the database type's display value.
     *
     * @return display value
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * Returns the database type by its string representation
     * (case-insensitive).
     *
     * @param s Database type string representation
     * @return DatabaseType, may be null if the provided string has no
     * DatabaseType representation
     */
    public static DatabaseType getDatabaseType(final String s) {
        DatabaseType e = STRINGTOENUM.get(s);
        if (e != null) return e;

        for (final String dispval : STRINGTOENUM.keySet()) {
            if (dispval.equalsIgnoreCase(s))
                return STRINGTOENUM.get(dispval);
        }

        return null;
    }

    /**
     * Returns the {@link DatabaseType} for the provided JDBC URL.
     *
     * @param jdbcURL JDBC URL
     * @return {@link DatabaseType}; may be null
     */
    public static DatabaseType getDatabaseTypeForURL(final String jdbcURL) {
        if (jdbcURL == null) {
            return null;
        }
        if (jdbcURL.contains("derby")) {
            // It's darby!
            return DERBY;
        } else if (jdbcURL.contains("mysql")) {
            return MYSQL;
        } else if (jdbcURL.contains("oracle")) {
            // You didn't come here to make a choice. You already made it.
            return ORACLE;
        } else if (jdbcURL.contains("postgresql")) {
            return POSTGRESQL;
        }
        return null;
    }
}
