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
package org.openbel.framework.api.internal;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

public class NullConnection implements Connection {

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement createStatement() throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String nativeSQL(String sql) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getAutoCommit() throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback() throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isReadOnly() throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCatalog(String catalog) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCatalog() throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTransactionIsolation(int level) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTransactionIsolation() throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearWarnings() throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement createStatement(int resultSetType,
            int resultSetConcurrency) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHoldability(int holdability) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHoldability() throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Savepoint setSavepoint() throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback(Savepoint savepoint) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement createStatement(int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement prepareStatement(String sql,
            int autoGeneratedKeys) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
            throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames)
            throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clob createClob() throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Blob createBlob() throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NClob createNClob() throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SQLXML createSQLXML() throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(int timeout) throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClientInfo(String name, String value)
            throws SQLClientInfoException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClientInfo(Properties properties)
            throws SQLClientInfoException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClientInfo(String name) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Properties getClientInfo() throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Array createArrayOf(String typeName, Object[] elements)
            throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Struct createStruct(String typeName, Object[] attributes)
            throws SQLException {
        return null;
    }

}
