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
package org.openbel.framework.core.kam;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.openbel.framework.common.enums.AnnotationType;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.enums.RelationshipType;
import org.openbel.framework.common.protonetwork.model.*;
import org.openbel.framework.common.protonetwork.model.DocumentTable.DocumentHeader;
import org.openbel.framework.common.protonetwork.model.NamespaceTable.TableNamespace;

/**
 * KamLoader defines a database loader to load a KAM into the KAMstore schema.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface KAMLoader {

    /**
     * Returns {@code true} if the KAM schema to be loaded exists, {@code false}
     * otherwise.
     *
     * @return boolean
     */
    public boolean schemaExists();

    /**
     * Loads static type data for object types.
     *
     * @return {@link Map} of {@link String} to {@link Integer} of object type
     * names to primary key
     * @throws SQLException - Thrown if a sql error occurred while loading
     * object types.
     */
    public Map<String, Integer> loadObjectTypes() throws SQLException;

    /**
     * Loads static type data for {@link FunctionEnum}.
     *
     * @return {@link Map} of {@link String} to {@link Integer} of function
     * names to primary key
     * @throws SQLException - Thrown if a sql error occurred while loading
     * function types.
     */
    public Map<String, Integer> loadFunctionTypes() throws SQLException;

    /**
     * Loads static type data for {@link RelationshipType}.
     *
     * @throws SQLException Thrown if a SQL error occurred while loading
     * relationship types.
     */
    public void loadRelationshipTypes() throws SQLException;

    /**
     * Loads static type data for {@link AnnotationType}.
     *
     * @throws SQLException - Thrown if a SQL error occurred while loading
     * annotation definition types.
     */
    public void loadAnnotationDefinitionTypes()
            throws SQLException;

    /**
     * Loads a {@link List} of {@link DocumentHeader}, which represents the
     * documents, into the KAM.
     *
     * @param documents {@link List} of {@link DocumentHeader}, the documents to
     * load
     * @throws SQLException Thrown if a SQL error occurred while loading the
     * documents.
     */
    public void loadDocuments(List<DocumentHeader> documents)
            throws SQLException;

    /**
     * Loads a {@link TableNamespace} into the KAM.
     *
     * @param i {@code int}, the namespace index to use as the primary key
     * @param ns {@link TableNamespace}, the namespace to load
     * @throws SQLException - Thrown if a SQL error occurred while loading the
     * namespace.
     */
    public void loadNamespace(int i, TableNamespace ns) throws SQLException;

    /**
     * Loads the {@link DocumentHeader} to {@link TableNamespace} index map into
     * the KAM.
     *
     * @param dnsm {@link Map}, the map of document index to namespace indexes
     * @throws SQLException - Thrown if a SQL error occurred while loading the
     * document to namespace map.
     */
    public void loadDocumentNamespaceMap(Map<Integer, List<Integer>> dnsm)
            throws SQLException;

    /**
     * Loads the global KAM nodes into the KAM including the parameter and term
     * objects.
     *
     * @param nt {@link NamespaceTable}, the namespace table where the
     * parameter's namespaces are associated
     * @param pt {@link ParameterTable}, the parameter table where the
     * parameters are stored
     * @param tt {@link TermTable}, the term table where the terms are stored
     * @param tpmt {@link TermParameterMapTable}, the term parameter map table
     * where the relation between parameters and terms are stored
     * @throws SQLException - Thrown if a SQL error occurred while loading the
     * parameters.
     */
    public void loadNodes(NamespaceTable nt, ParameterTable pt, TermTable tt,
            TermParameterMapTable tpmt, ProtoNodeTable pnt) throws SQLException;

    /**
     * Loads the global KAM edges into the KAM including the statements.
     *
     * @param st {@link StatementTable}, the statement table to read the
     * statements from
     * @param tt {@link TermTable}, the term table to get the global term ids
     * from
     * @throws SQLException - Thrown if a SQL error occurred while loading the
     * parameters.
     */
    public void loadEdges(StatementTable st, TermTable tt, ProtoNodeTable pnt,
            ProtoEdgeTable pet)
            throws SQLException;

    /**
     * Loads the annotation definitions into the KAM.
     *
     * @param adt {@link AnnotationDefinitionTable}, the annotation definition
     * table
     * @throws SQLException - Thrown if a SQL error occurred while loading the
     * annotation definitions.
     */
    public void loadAnnotationDefinitions(AnnotationDefinitionTable adt)
            throws SQLException;

    /**
     * Loads the annotation values, associated to an annotation definition, into
     * the KAM.
     *
     * @param avt {@link AnnotationValueTable}, the annotation value table
     * @throws SQLException - Thrown if a SQL error occurred while loading the
     * parameters.
     */
    public void loadAnnotationValues(AnnotationValueTable avt)
            throws SQLException;

    /**
     * Loads the statement annotation map data.
     *
     * @param samt {@link StatementAnnotationMapTable}, the statement annotation
     * map data
     * @throws SQLException - Thrown if a SQL error occurred while loading the
     * statement annotation map table.
     */
    public void loadStatementAnnotationMap(StatementAnnotationMapTable samt)
            throws SQLException;
}
