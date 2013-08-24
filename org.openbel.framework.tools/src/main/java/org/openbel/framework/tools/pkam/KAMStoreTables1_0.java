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
package org.openbel.framework.tools.pkam;

import static org.openbel.framework.common.BELUtilities.nulls;
import static org.openbel.framework.common.cfg.SystemConfiguration.getSystemConfiguration;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openbel.framework.common.InvalidArgument;

/**
 * KAMStoreTables1_0 defines the 1.0 version of the KamStore in order to
 * understand how to read / write KAM data for Portable KAM format.
 *
 * <p>
 * Ordinal in {@link KAMStoreTables1_0} enum is important
 * in correct processing of SQL insert statements in order to maintatin
 * referential integrity constraints.
 * </p>
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
enum KAMStoreTables1_0 {
    KAM_CATALOG_KAM(getSystemConfiguration().getKamCatalogSchema(), "kam",
            false, "kam_id", new String[] { "name", "description",
                    "last_compiled",
                    "schema_name" }, new Integer[] { Types.VARCHAR,
                    Types.VARCHAR,
                    Types.TIMESTAMP, Types.VARCHAR }),
    KAM_OBJECTS_TYPE(null, "objects_type", true, "objects_type_id",
            new String[] { "name" }, new Integer[] { Types.VARCHAR }),
    KAM_OBJECTS_TEXT(null, "objects_text", false, "objects_text_id",
            new String[] { "text_value" }, new Integer[] { Types.CLOB }),
    KAM_OBJECTS(null, "objects", false, "objects_id", new String[] { "type_id",
            "varchar_value", "objects_text_id" }, new Integer[] {
            Types.INTEGER,
            Types.VARCHAR, Types.INTEGER }),
    KAM_ANNOTATION_DEFINITION_TYPE(null, "annotation_definition_type",
            true, "annotation_definition_type_id", new String[] { "name" },
            new Integer[] { Types.VARCHAR }),
    KAM_DOCUMENT_HEADER_INFORMATION(null, "document_header_information",
            true, "document_id", new String[] { "name", "description",
                    "version",
                    "copyright", "disclaimer", "contact_information",
                    "license_information", "authors" },
            new Integer[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                    Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                    Types.VARCHAR }),
    KAM_FUNCTION_TYPE(null, "function_type", true, "function_type_id",
            new String[] { "name" }, new Integer[] { Types.VARCHAR }),
    KAM_NAMESPACE(null, "namespace", true, "namespace_id", new String[] {
            "prefix",
            "resource_location_oid" },
            new Integer[] { Types.VARCHAR, Types.INTEGER }),
    KAM_RELATIONSHIP_TYPE(null, "relationship_type", true,
            "relationship_type_id",
            new String[] { "name" }, new Integer[] { Types.VARCHAR }),
    KAM_ANNOTATION_DEFINITION(null, "annotation_definition",
            true, "annotation_definition_id", new String[] {
                    "annotation_definition_type_id", "annotation_usage",
                    "description", "domain_value_oid", "name" }, new Integer[] {
                    Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER,
                    Types.VARCHAR }),
    KAM_ANNOTATION(null, "annotation", true, "annotation_id",
            new String[] { "annotation_definition_id", "value_oid" },
            new Integer[] { Types.INTEGER, Types.INTEGER }),
    KAM_DOCUMENT_ANNOTATION_DEF_MAP(null, "document_annotation_def_map",
            false, "document_annotation_definition", new String[] {
                    "document_id",
                    "annotation_definition_id" }, new Integer[] {
                    Types.INTEGER,
                    Types.INTEGER }),
    KAM_DOCUMENT_NAMESPACE_MAP(null, "document_namespace_map",
            false, "document_namespace_id", new String[] { "document_id",
                    "namespace_id" },
            new Integer[] { Types.INTEGER, Types.INTEGER }),
    KAM_KAM_NODE(null, "kam_node", true, "kam_node_id", new String[] {
            "function_type_id", "node_label_oid" }, new Integer[] {
            Types.INTEGER,
            Types.INTEGER }),
    KAM_KAM_NODE_PARAMETER(
            null,
            "kam_node_parameter",
            false,
            "kam_node_parameter_id",
            new String[] { "kam_global_parameter_id", "kam_node_id", "ordinal" },
            new Integer[] { Types.INTEGER, Types.INTEGER, Types.INTEGER }),
    KAM_TERM(null, "term", true, "term_id", new String[] { "kam_node_id",
            "term_label_oid" }, new Integer[] { Types.INTEGER, Types.INTEGER }),
    KAM_TERM_PARAMETER(null, "term_parameter", true, "term_parameter_id",
            new String[] { "kam_global_parameter_id", "term_id",
                    "namespace_id", "parameter_value_oid", "ordinal" },
            new Integer[] { Types.INTEGER, Types.INTEGER, Types.INTEGER,
                    Types.INTEGER, Types.INTEGER }),
    KAM_KAM_EDGE(null, "kam_edge", true, "kam_edge_id",
            new String[] { "kam_source_node_id", "kam_target_node_id",
                    "relationship_type_id" }, new Integer[] { Types.INTEGER,
                    Types.INTEGER, Types.INTEGER }),
    KAM_STATEMENT(
            null,
            "statement",
            true,
            "statement_id",
            new String[] {
                    "document_id", "subject_term_id",
                    "relationship_type_id", "object_term_id",
                    "nested_subject_id",
                    "nested_relationship_type_id", "nested_object_id" },
            new Integer[] { Types.INTEGER, Types.INTEGER, Types.INTEGER,
                    Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER }),
    KAM_EDGE_STATEMENT_MAP(null, "kam_edge_statement_map", false,
            "kam_edge_statement_id", new String[] { "kam_edge_id",
                    "statement_id" }, new Integer[] { Types.INTEGER,
                    Types.INTEGER }),
    KAM_STATEMENT_ANNOTATION_MAP(null, "statement_annotation_map",
            false, "statement_annotation_id", new String[] { "statement_id",
                    "annotation_id" },
            new Integer[] { Types.INTEGER, Types.INTEGER }),
    KAM_PARAMETER_UUID(
            null,
            "kam_parameter_uuid",
            false,
            "kam_parameter_uuid_id",
            new String[] { "kam_global_parameter_id", "most_significant_bits",
                    "least_significant_bits" },
            new Integer[] { Types.INTEGER, Types.BIGINT, Types.BIGINT });

    private static final String SQL_SELECT_FORMAT = "SELECT %s FROM %s.%s %s";
    private static final String SQL_INSERT_FORMAT =
            "INSERT INTO %s.%s(%s) VALUES(%s)";
    private final String schemaOverride;
    private final String tableName;
    private final boolean insertPrimaryKey;
    private final String primaryKeyColumn;
    private final String[] otherColumnNames;
    private final String[] columnNames;
    private final Integer[] columnTypes;

    private KAMStoreTables1_0(final String schemaOverride,
            final String tableName, final boolean insertPrimaryKey,
            final String primaryKeyColumn,
            final String[] otherColumnNames, final Integer[] otherColumnTypes) {
        if (nulls(primaryKeyColumn, tableName, otherColumnNames,
                otherColumnTypes)) {
            throw new InvalidArgument("inputs to enum instance are null");
        }

        if (schemaOverride != null) {
            this.schemaOverride = schemaOverride;
        } else {
            this.schemaOverride = null;
        }

        this.tableName = tableName;
        this.insertPrimaryKey = insertPrimaryKey;
        this.primaryKeyColumn = primaryKeyColumn;
        this.otherColumnNames = otherColumnNames;

        List<String> columns = new ArrayList<String>();
        columns.add(primaryKeyColumn);
        columns.addAll(Arrays.asList(otherColumnNames));

        // primary key is always an INTEGER type
        List<Integer> types = new ArrayList<Integer>();
        types.add(Types.INTEGER);
        types.addAll(Arrays.asList(otherColumnTypes));

        this.columnNames = columns.toArray(new String[columns.size()]);
        this.columnTypes = types.toArray(new Integer[types.size()]);
    }

    public String getTableName() {
        return tableName;
    }

    public boolean getInsertPrimaryKey() {
        return insertPrimaryKey;
    }

    public String getPrimaryKeyColumn() {
        return primaryKeyColumn;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public Integer[] getColumnTypes() {
        return columnTypes;
    }

    public String getSQLSelect(final String kamSchema) {
        final String schema = determineSchema(kamSchema);

        final String whereClause;
        if (this == KAM_CATALOG_KAM) {
            whereClause = "WHERE schema_name = '" + kamSchema + "'";
        } else {
            whereClause = "";
        }

        List<String> columns = new ArrayList<String>();
        columns.add(primaryKeyColumn);
        columns.addAll(Arrays.asList(columnNames));

        return String.format(SQL_SELECT_FORMAT, StringUtils.join(columns, ","),
                schema, tableName, whereClause);
    }

    public String getSQLInsert(final String kamSchema) {
        final String schema = determineSchema(kamSchema);

        final List<String> columns = new ArrayList<String>();
        final String[] params;

        if (insertPrimaryKey) {
            columns.addAll(Arrays.asList(columnNames));
            params = new String[columnNames.length];
        } else {
            columns.addAll(Arrays.asList(otherColumnNames));
            params = new String[otherColumnNames.length];
        }

        Arrays.fill(params, "?");
        return String.format(SQL_INSERT_FORMAT, schema, tableName,
                StringUtils.join(columns, ","), StringUtils.join(params, ","));
    }

    private String determineSchema(final String schemaName) {
        final String schema;
        if (schemaOverride != null) {
            schema = schemaOverride;
        } else {
            schema = schemaName;
        }

        return schema;
    }

    public static KAMStoreTables1_0 getTableByName(final String tableName) {
        if (tableName == null) {
            throw new InvalidArgument("tableName", tableName);
        }

        KAMStoreTables1_0[] tables = values();
        for (KAMStoreTables1_0 table : tables) {
            if (tableName.equals(table.getTableName())) {
                return table;
            }
        }

        return null;
    }
}
