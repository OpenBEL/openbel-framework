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

import java.sql.*;

import org.openbel.framework.api.internal.KAMCatalogDao.KamInfo;
import org.openbel.framework.common.enums.DatabaseType;
import org.openbel.framework.core.df.DBConnection;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KamInfoUtil {

    static ResultSet mockRs = mock(ResultSet.class);
    static PreparedStatement mockStmt = mock(PreparedStatement.class);
    static Connection mockConn = mock(Connection.class);
    static {
        try {
            when(mockRs.next()).thenReturn(true);
            when(mockRs.getString(anyInt())).thenReturn("test");
            when(mockRs.getInt(anyInt())).thenReturn(1);
            when(mockRs.getDate(anyInt())).thenReturn(new Date(-1));
            when(mockStmt.executeQuery()).thenReturn(mockRs);
            when(mockConn.prepareStatement(anyString(),
                    anyInt())).thenReturn(mockStmt);
        } catch (SQLException e) { /* ignoring */ }
    }

    /**
     * Creates a test {@link KamInfo} object.
     *
     * @return a test {@link KamInfo}
     * @throws SQLException Thrown if a SQL error occurred accessing the KAM
     * catalog
     */
    public static KamInfo createKamInfo() throws SQLException {
        DBConnection dbc =
                new DBConnection(mockConn, DatabaseType.DERBY, "", "", "");
        KAMCatalogDao kcdao = new KAMCatalogDao(dbc, "foo", "bar");
        return kcdao.getKamInfoByName("test");
    }
}
