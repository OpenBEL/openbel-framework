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
package org.openbel.framework.api.internal;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class NullResultSet implements ResultSet {

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
    public boolean next() throws SQLException {
        return false;
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
    public boolean wasNull() throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getByte(int columnIndex) throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short getShort(int columnIndex) throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(int columnIndex) throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(int columnIndex) throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloat(int columnIndex) throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(int columnIndex) throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale)
            throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDate(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time getTime(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getByte(String columnLabel) throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short getShort(String columnLabel) throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(String columnLabel) throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(String columnLabel) throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloat(String columnLabel) throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(String columnLabel) throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getBigDecimal(String columnLabel, int scale)
            throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getBytes(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDate(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time getTime(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        return null;
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
    public String getCursorName() throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int findColumn(String columnLabel) throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reader getCharacterStream(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBeforeFirst() throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAfterLast() throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFirst() throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLast() throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeFirst() throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterLast() throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean first() throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean last() throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRow() throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean absolute(int row) throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean relative(int rows) throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean previous() throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFetchDirection(int direction) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFetchDirection() throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFetchSize(int rows) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFetchSize() throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getType() throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getConcurrency() throws SQLException {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean rowUpdated() throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean rowInserted() throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean rowDeleted() throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNull(int columnIndex) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateByte(int columnIndex, byte x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateShort(int columnIndex, short x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateInt(int columnIndex, int x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLong(int columnIndex, long x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateFloat(int columnIndex, float x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDouble(int columnIndex, double x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateString(int columnIndex, String x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDate(int columnIndex, Date x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTime(int columnIndex, Time x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTimestamp(int columnIndex, Timestamp x)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateObject(int columnIndex, Object x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNull(String columnLabel) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBoolean(String columnLabel, boolean x)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateByte(String columnLabel, byte x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateShort(String columnLabel, short x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateInt(String columnLabel, int x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLong(String columnLabel, long x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateFloat(String columnLabel, float x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDouble(String columnLabel, double x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBigDecimal(String columnLabel, BigDecimal x)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateString(String columnLabel, String x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBytes(String columnLabel, byte[] x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDate(String columnLabel, Date x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTime(String columnLabel, Time x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTimestamp(String columnLabel, Timestamp x)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAsciiStream(String columnLabel, InputStream x,
            int length) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBinaryStream(String columnLabel, InputStream x,
            int length) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCharacterStream(String columnLabel, Reader reader,
            int length) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateObject(String columnLabel, Object x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertRow() throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateRow() throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRow() throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshRow() throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelRowUpdates() throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveToInsertRow() throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveToCurrentRow() throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement getStatement() throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(int columnIndex, Map<String, Class<?>> map)
            throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Ref getRef(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clob getClob(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Array getArray(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject(String columnLabel, Map<String, Class<?>> map)
            throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Ref getRef(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Blob getBlob(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Clob getClob(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Array getArray(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal)
            throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar cal)
            throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getURL(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getURL(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateRef(int columnIndex, Ref x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateRef(String columnLabel, Ref x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBlob(int columnIndex, Blob x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBlob(String columnLabel, Blob x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateClob(int columnIndex, Clob x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateClob(String columnLabel, Clob x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateArray(int columnIndex, Array x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateArray(String columnLabel, Array x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {

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
    public boolean isClosed() throws SQLException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNString(int columnIndex, String nString)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNString(String columnLabel, String nString)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNClob(String columnLabel, NClob nClob)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNString(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNString(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader,
            long length) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void
            updateAsciiStream(String columnLabel, InputStream x, long length)
                    throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBinaryStream(String columnLabel, InputStream x,
            long length) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCharacterStream(String columnLabel, Reader reader,
            long length) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void
            updateBlob(int columnIndex, InputStream inputStream, long length)
                    throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBlob(String columnLabel, InputStream inputStream,
            long length) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateClob(int columnIndex, Reader reader, long length)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateClob(String columnLabel, Reader reader, long length)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNClob(int columnIndex, Reader reader, long length)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNClob(String columnLabel, Reader reader, long length)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNCharacterStream(int columnIndex, Reader x)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAsciiStream(int columnIndex, InputStream x)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBinaryStream(int columnIndex, InputStream x)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCharacterStream(int columnIndex, Reader x)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAsciiStream(String columnLabel, InputStream x)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBinaryStream(String columnLabel, InputStream x)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCharacterStream(String columnLabel, Reader reader)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBlob(int columnIndex, InputStream inputStream)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBlob(String columnLabel, InputStream inputStream)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateClob(String columnLabel, Reader reader)
            throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNClob(String columnLabel, Reader reader)
            throws SQLException {

    }

}
