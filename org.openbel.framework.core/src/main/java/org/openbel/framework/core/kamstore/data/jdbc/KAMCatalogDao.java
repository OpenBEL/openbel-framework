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
package org.openbel.framework.core.kamstore.data.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openbel.framework.common.BELUtilities;
import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.cfg.SystemConfiguration;
import org.openbel.framework.core.df.AbstractJdbcDAO;
import org.openbel.framework.core.df.DBConnection;
import org.openbel.framework.core.kamstore.KamDbObject;
import org.openbel.framework.core.kamstore.model.Filter;
import org.openbel.framework.core.kamstore.model.KamStoreObjectImpl;
import org.openbel.framework.core.kamstore.model.filter.AnnotationFilterCriteria;
import org.openbel.framework.core.kamstore.model.filter.BelDocumentFilterCriteria;
import org.openbel.framework.core.kamstore.model.filter.CitationFilterCriteria;
import org.openbel.framework.core.kamstore.model.filter.FilterCriteria;
import org.openbel.framework.core.kamstore.model.filter.NamespaceFilterCriteria;
import org.openbel.framework.core.kamstore.model.filter.RelationshipTypeFilterCriteria;

/**
 * KAMCatalogDao provides a JDBC-driven DAO for accessing the KAM catalog
 * of the KAMStore.
 *
 * @author Julian Ray {@code jray@selventa.com}
 */
public final class KAMCatalogDao extends AbstractJdbcDAO {

    /**
     * The SQL query to select all {@link KamInfo} objects from the KAM catalog.
     */
    private static final String SELECT_KAM_CATALOG_SQL =
            "SELECT " +
                    "kam_id, name, description, last_compiled, schema_name " +
                    "FROM " +
                    "@.kam " +
                    "ORDER BY name";

    /**
     * The SQL query to select a {@link KamInfo} object by kam name.
     */
    private static final String SELECT_KAM_BY_NAME_SQL =
            "SELECT " +
                    "kam_id, name, description, last_compiled, schema_name " +
                    "FROM " +
                    "@.kam " +
                    "WHERE " +
                    "name = ?";

    /**
     * The SQL query to select a {@link KamInfo} object by kam id.
     */
    private static final String SELECT_KAM_BY_ID_SQL =
            "SELECT " +
                    "kam_id, name, description, last_compiled, schema_name " +
                    "FROM " +
                    "@.kam " +
                    "WHERE " +
                    "kam_id = ?";

    /**
     * The SQL statement to insert a KAM into the KAM catalog.
     */
    private static final String INSERT_KAM_SQL =
            "INSERT INTO " +
                    "@.kam(name, description, last_compiled, schema_name) " +
                    "VALUES(?, ?, ?, ?)";

    /**
     * The SQL statement to update a KAM in the KAM catalog.
     */
    private static final String UPDATE_KAM_SQL =
            "UPDATE "
                    +
                    "@.kam "
                    +
                    "SET name = ?, description = ?, last_compiled = ?, schema_name = ? "
                    +
                    "WHERE " +
                    "kam_id = ?";

    /**
     * The SQL statement to delete a KAM from the KAM catalog.
     */
    private static final String DELETE_KAM_SQL =
            "DELETE " +
                    "FROM @.kam " +
                    "WHERE " +
                    "kam_id = ?";

    private final String kamSchemaPrefix;

    /**
     * Creates a KAMStoreDaoImpl from the Jdbc {@link Connection} that will
     * be used to load the KAM.
     *
     * @param dbc {@link Connection}, the database connection which should be
     * non-null and already open for sql execution.
     * @param kamCatalogSchema {@link String}, the kam catalog schema
     * @throws InvalidArgument Thrown if {@code dbc} is null or the sql
     * connection is already closed.
     * @throws SQLException Thrown if a sql error occurred while loading
     * the KAM.
     */
    public KAMCatalogDao(DBConnection dbc, String kamCatalogSchema,
            String kamSchemaPrefix) throws SQLException {
        super(dbc, kamCatalogSchema);

        if (StringUtils.isBlank(kamCatalogSchema)) {
            throw new InvalidArgument("kamCatalogSchema is not set");
        }

        if (StringUtils.isBlank(kamSchemaPrefix)) {
            throw new InvalidArgument("kamSchemaPrefix is not set");
        }

        if (dbc == null) {
            throw new InvalidArgument("dbc is null");
        }

        if (dbc.getConnection().isClosed()) {
            throw new InvalidArgument("dbc is closed and cannot be used");
        }

        this.kamSchemaPrefix = kamSchemaPrefix;
    }

    /**
     * Retrieves the {@link KamInfo} objects from the KAM catalog database.
     *
     * <p>
     * If the <tt>kam</tt> doesn't exist then a null {@link KamInfo} is returned.
     * </p>
     * @return {@link KamInfo}, the kam info object from the
     * kam catalog database or null if the kam name cannot be found.
     * @throws SQLException Thrown if a SQL error occurred while retrieving
     * the {@link KamInfo} objects from the kam catalog.
     */
    public List<KamInfo> getCatalog() throws SQLException {
        List<KamInfo> list = new ArrayList<KamInfo>();
        ResultSet rset = null;

        try {
            PreparedStatement ps = getPreparedStatement(SELECT_KAM_CATALOG_SQL);
            rset = ps.executeQuery();

            while (rset.next()) {
                list.add(getKamInfo(rset));
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            close(rset);
        }

        return list;
    }

    /**
     * Retrieves a {@link KamInfo} object, from the KAM catalog database,
     * using the KAM's name.
     *
     * @param name {@link String}, the KAM name
     * @return {@link KamInfo}, the queried KAM or null if no KAM is found by
     * the <tt>name</tt>
     * @throws SQLException Thrown if a SQL error occurred while retrieving
     * the {@link KamInfo} object by kam name.
     */
    public KamInfo getKamInfoByName(String kamName) throws SQLException {
        KamInfo kamInfo = null;
        ResultSet rset = null;

        try {
            PreparedStatement ps = getPreparedStatement(SELECT_KAM_BY_NAME_SQL);
            ps.setString(1, kamName);

            rset = ps.executeQuery();

            if (rset.next()) {
                kamInfo = getKamInfo(rset);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            close(rset);
        }

        return kamInfo;
    }

    /**
     * Retrieves a {@link KamInfo} object, from the KAM catalog database,
     * using the KAM's id.
     *
     * @param id, the KAM id
     * @return {@link KamInfo}, the queried KAM or null if no KAM is found by
     * that <tt>id</tt>
     * @throws SQLException Thrown if a SQL error occurred while retrieving
     * the {@link KamInfo} object by kam id.
     */
    public KamInfo getKamInfoById(final int id) throws SQLException {
        KamInfo kamInfo = null;
        ResultSet rset = null;

        try {
            PreparedStatement ps = getPreparedStatement(SELECT_KAM_BY_ID_SQL);
            ps.setInt(1, id);

            rset = ps.executeQuery();

            if (rset.next()) {
                kamInfo = getKamInfo(rset);
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            close(rset);
        }

        return kamInfo;
    }

    /**
     * Saves the {@link KamDbObject} object to the KAM catalog database.
     *
     * <p>
     * If the <tt>kamDb</tt> doesn't exist then create it, otherwise update
     * the record's information.  This method will look for an existing
     * <tt>kamDb</tt> first by id, if that is not <tt>null</tt>, then
     * by name.  It can be used to update the name of the record, but it
     * cannot be used to update the id.
     * </p>
     *
     * @param kamInfo {@link KamDbObject}, the kam info to save to the kam catalog,
     * which cannot be null, and must contain a non-null name
     * @throws SQLException Thrown if a SQL error occurred saving the kam info
     * to the kam catalog
     * @throws InvalidArgument Thrown if <tt>kamDb</tt> is null or contains
     * a null name.
     */
    public void saveToCatalog(KamDbObject updated) throws SQLException {

        if (updated == null) {
            throw new InvalidArgument("kamInfo", updated);
        }

        if (updated.getName() == null) {
            throw new InvalidArgument("kamInfo contains a null name");
        }

        if (updated.getDescription() == null) {
            throw new InvalidArgument("kamInfo contains a null description");
        }

        // First check to see if the KAM already exists in the Catalog. This
        // returns the name of the schema for the KAM or null if the KAM is
        // not already there.  The existence of KAMs is checked first by id
        // then by name.
        KamInfo originalInfo = null;
        final Integer updatedId = updated.getId();
        if (updatedId != null) {
            originalInfo = getKamInfoById(updatedId.intValue());
        }
        if (originalInfo == null) {
            originalInfo = getKamInfoByName(updated.getName());
        }

        // If the KAM exists we update the current catalog entry
        try {
            if (null != originalInfo) {
                KamDbObject original = originalInfo.getKamDbObject();
                updated.setSchemaName(original.getSchemaName());

                //must update the kam info record.
                PreparedStatement skips = getPreparedStatement(UPDATE_KAM_SQL);
                skips.setString(1, updated.getName());
                skips.setString(2, updated.getDescription());
                skips.setTimestamp(3, new Timestamp(updated.getLastCompiled()
                        .getTime()));
                skips.setString(4, original.getSchemaName());
                skips.setInt(5, original.getId());
                skips.execute();
            } else {
                // Otherwise we insert a new kam. Schema name is automatically
                // generated

                // find next available schema name
                String schemaName = findNextSchemaName();
                updated.setSchemaName(schemaName);

                PreparedStatement ps = getPreparedStatement(INSERT_KAM_SQL,
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, updated.getName());
                ps.setString(2, updated.getDescription());
                ps.setTimestamp(3, new Timestamp(updated.getLastCompiled()
                        .getTime()));
                ps.setString(4, updated.getSchemaName());
                ps.execute();
            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            // Nothing to do
        }
    }

    /**
     * Deletes the {@link KamInfo} object with a provided name from the KAM catalog database.
     *
     * @param kamName, the name of a kam info object to delete from the kam catalog
     * @throws SQLException Thrown if a SQL error occurred deleting the kam info object
     * from the kam catalog
     */
    public void deleteFromCatalog(final String kamName) throws SQLException {

        if (kamName != null) {
            KamInfo kamInfo = getKamInfoByName(kamName);
            if (null != kamInfo) {
                try {
                    PreparedStatement ps = getPreparedStatement(DELETE_KAM_SQL);
                    ps.setInt(1, kamInfo.getId());
                    ps.execute();
                } catch (SQLException ex) {
                    throw ex;
                } finally {
                    // Nothing to do
                }
            }
        }
    }

    /**
     * Find the next available schema name in the KAM catalog based on the
     * {@link SystemConfiguration#getKamSchemaPrefix() KAM schema prefix}.
     *
     * @return the next available schema name
     * @throws SQLException Thrown if the SQL query to the KAM catalog failed
     */
    private String findNextSchemaName() throws SQLException {

        ResultSet rset = null;
        try {
            // Find next available schema name.
            PreparedStatement ps = getPreparedStatement(SELECT_KAM_CATALOG_SQL);
            rset = ps.executeQuery();
            int maxId = 0;
            while (rset.next()) {
                // read schemaName value, skip if it does not look like a
                // schema prefix
                String schema = rset.getString(5);
                if (schema == null || !schema.startsWith(kamSchemaPrefix)) {
                    continue;
                }

                // extract the schema number
                String id = schema.substring(schema.indexOf(kamSchemaPrefix)
                        + kamSchemaPrefix.length());

                // if the schema number is numeric, compare to current max
                if (StringUtils.isNumeric(id)) {
                    maxId = Math.max(Integer.parseInt(id), maxId);
                }
            }
            String schemaName = kamSchemaPrefix + (++maxId);
            return schemaName;
        } catch (SQLException ex) {
            throw ex;
        } finally {
            close(rset);
        }
    }

    /**
     *
     * @param rset
     * @return
     * @throws SQLException
     */
    private KamInfo getKamInfo(ResultSet rset) throws SQLException {

        Integer kamId = rset.getInt(1);
        String name = rset.getString(2);
        String description = rset.getString(3);

        // handle timestamp as date+time
        Timestamp cts = rset.getTimestamp(4);
        Date lastCompiled = null;
        if (cts != null) {
            lastCompiled = new Date(cts.getTime());
        }

        String schemaName = rset.getString(5);
        return new KamInfo(new KamDbObject(kamId, name, description,
                lastCompiled, schemaName));
    }

    /**
     *
     * @author julianjray
     *
     */
    public static class KamInfo extends KamStoreObjectImpl {

        private final KamDbObject kamDb;

        /**
         * Precalculate the kam info hash code since the object is immutable.
         */
        private final int hashCode;

        public KamInfo(KamDbObject kamDb) {
            super(kamDb.getId()); //kamDb must not be null
            this.kamDb = kamDb;
            if (kamDb.getId() == null) {
                throw new InvalidArgument(
                        "KamDbObject and KamDbObject Id cannot be null.");
            }

            this.hashCode = generateHashCode();
        }

        public KamDbObject getKamDbObject() {
            return kamDb;
        }

        private int generateHashCode() {
            return 31 * this.getId().hashCode();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
            if (o instanceof KamInfo) {
                KamInfo ki = (KamInfo) o;
                return this.getId().equals(ki.getId());
            }

            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return hashCode;
        }

        /**
         *
         * @return
         */
        public String getSchemaName() {
            return kamDb.getSchemaName();
        }

        /**
         *
         * @return
         */
        public Date getLastCompiled() {
            return kamDb.getLastCompiled();
        }

        /**
         *
         * @return
         */
        public String getDescription() {
            return kamDb.getDescription();
        }

        /**
         *
         * @return
         */
        public String getName() {
            return kamDb.getName();
        }

        /**
         * @return
         * @throws InvalidArgument
         */
        public AnnotationFilter createAnnotationFilter() throws InvalidArgument {
            return new AnnotationFilter(this);
        }

        /**
         * @return
         * @throws InvalidArgument
         */
        public NamespaceFilter createNamespaceFilter() throws InvalidArgument {
            return new NamespaceFilter(this);
        }

        /**
         * @return
         * @throws InvalidArgument
         */
        public KamFilter createKamFilter() throws InvalidArgument {
            return new KamFilter(this);
        }
    }

    /**
     * @author julianjray
     */
    public static class AnnotationFilter extends Filter {

        private AnnotationFilter(KamInfo kamInfo) {
            super(kamInfo);
        }

        /**
         * Adds a new AnnotationFilterCriteria to the Filter
         *
         * @param annotationFilterCriteria
         */
        public void add(AnnotationFilterCriteria annotationFilterCriteria) {
            getFilterCriteria().add(annotationFilterCriteria);
        }
    }

    /**
     * @author julianjray
     */
    public static class NamespaceFilter extends Filter {

        private NamespaceFilter(KamInfo kamInfo) {
            super(kamInfo);
        }

        /**
         * Adds a new NamespaceFilterCritera to the Filter
         *
         * @param criteria
         */
        public void add(NamespaceFilterCriteria criteria) {
            getFilterCriteria().add(criteria);
        }
    }

    /**
     * @author julianjray
     */
    public static class CitationFilter extends Filter {

        private CitationFilter(KamInfo kamInfo) {
            super(kamInfo);
        }

        /**
         * Adds a new CitationFilterCriteria to the Filter
         *
         * @param citationFilterCriteria
         */
        public void add(CitationFilterCriteria citationFilterCriteria) {
            getFilterCriteria().add(citationFilterCriteria);
        }
    }

    /**
     * @author julianjray
     */
    public static class KamFilter extends Filter {

        private KamFilter(KamInfo kamInfo) {
            super(kamInfo);
        }

        public void add(CitationFilterCriteria citationFilterCriteria) {
            getFilterCriteria().add(citationFilterCriteria);
        }

        /**
         * Adds a new AnnotationFilterCriteria to the Filter
         *
         * @param annotationFilterCriteria
         */
        public void add(AnnotationFilterCriteria annotationFilterCriteria) {
            getFilterCriteria().add(annotationFilterCriteria);
        }

        /**
         * Adds a new RelationshipTypeFilterCriteria to the Filter
         *
         * @param relationshipTypeFilterCriteria
         */
        public void add(
                RelationshipTypeFilterCriteria relationshipTypeFilterCriteria) {
            getFilterCriteria().add(relationshipTypeFilterCriteria);
        }

        /**
         * Adds a new BelDocumentFilterCriteria to the Filter
         *
         * @param belDocumentFilterCriteria
         */
        public void add(BelDocumentFilterCriteria belDocumentFilterCriteria) {
            getFilterCriteria().add(belDocumentFilterCriteria);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj == null) {
                return false;
            } else if (!(obj instanceof KamFilter)) {
                return false;
            } else {
                KamFilter other = (KamFilter) obj;
                return (BELUtilities.equals(getKamInfo(), other.getKamInfo()) && BELUtilities
                        .equals(getFilterCriteria(), other.getFilterCriteria()));
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int hash = 0;

            int criteriaHash = 0;
            final List<FilterCriteria> criteria = getFilterCriteria();
            if (criteria != null) {
                for (FilterCriteria criterion : criteria) {
                    criteriaHash ^= criterion.hashCode();
                }
            }

            hash += getKamInfo().hashCode();
            hash *= prime;
            hash += criteriaHash;
            return hash;
        }
    }
}
