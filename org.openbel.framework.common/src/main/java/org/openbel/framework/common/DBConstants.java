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
package org.openbel.framework.common;

/**
 * BEL framework database constants.
 */
public class DBConstants {

    /**
     * JDBC driver class name for MySQL configurations, used in
     * {@link #getDocstoreJdbcDriver()}.
     * <p>
     * Defined as: <tt>{@value}</tt>
     * </p>
     */
    public static final String MYSQL_JDBC_DRIVER_CLASS =
            "com.mysql.jdbc.Driver";

    /**
     * JDBC driver class name for Derby configurations, used in
     * {@link #getDocstoreJdbcDriver()}.
     * <p>
     * Defined as: <tt>{@value}</tt>
     * </p>
     */
    public static final String DERBY_JDBC_DRIVER_CLASS =
            "org.apache.derby.jdbc.EmbeddedDriver";

    /**
     * JDBC driver class name for Oracle configurations, used in
     * {@link #getDocstoreJdbcDriver()}.
     * <p>
     * Defined as: <tt>{@value}</tt>
     * </p>
     */
    public static final String ORACLE_JDBC_DRIVER_CLASS =
            "oracle.jdbc.driver.OracleDriver";

    /**
     * JDBC driver class name for PostgreSQL configurations
     */
    public static final String POSTGRESQL_JDBC_DRIVER_CLASS =
            "org.postgresql.Driver";

    /**
     * Default private constructor.
     */
    private DBConstants() {
    }

}
