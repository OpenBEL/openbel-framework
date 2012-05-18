-- This script creates the necessary Derby database objects
-- for the KAM schema. It needs to be run for every predefined
-- KAM schemas.

CREATE SCHEMA kam1;

CREATE TABLE annotation (
    annotation_id               INTEGER NOT NULL,
    value_oid                   INTEGER NOT NULL,
    annotation_definition_id    INTEGER NOT NULL,
    CONSTRAINT annotation_id_pk PRIMARY KEY(annotation_id)
);

CREATE TABLE annotation_definition (
    annotation_definition_id         INTEGER NOT NULL,
    name                             VARCHAR(255) NOT NULL,
    description                      VARCHAR(255),
    annotation_usage                 VARCHAR(255),
    domain_value_oid                 INTEGER NOT NULL,
    annotation_definition_type_id    INTEGER NOT NULL,
    CONSTRAINT annotation_definition_id_pk PRIMARY KEY(annotation_definition_id)
);

CREATE TABLE annotation_definition_type (
    annotation_definition_type_id    INTEGER NOT NULL,
    name                             VARCHAR(64) NOT NULL,
    CONSTRAINT annotation_definition_type_pk PRIMARY KEY(annotation_definition_type_id)
);

CREATE TABLE document_header_information (
    document_id          INTEGER NOT NULL,
    name                 VARCHAR(255) NOT NULL,
    description          VARCHAR(255),
    version              VARCHAR(64),
    copyright            VARCHAR(4000),
    disclaimer           VARCHAR(4000),
    contact_information  VARCHAR(4000),
    license_information  VARCHAR(4000),
    authors              VARCHAR(255),
    CONSTRAINT document_id_pk PRIMARY KEY(document_id)
);

CREATE TABLE document_annotation_def_map (
    document_annotation_definition    INTEGER GENERATED ALWAYS AS IDENTITY NOT NULL,
    document_id                       INTEGER NOT NULL,
    annotation_definition_id          INTEGER NOT NULL,
    CONSTRAINT document_annotation_definition_pk PRIMARY KEY(document_annotation_definition)
);

CREATE TABLE document_namespace_map (
    document_namespace_id    INTEGER GENERATED ALWAYS AS IDENTITY NOT NULL,
    document_id              INTEGER NOT NULL,
    namespace_id             INTEGER NOT NULL,
    CONSTRAINT document_namespace_pk PRIMARY KEY(document_namespace_id)
);

CREATE TABLE function_type (
    function_type_id    INTEGER NOT NULL,
    name                VARCHAR(64) NOT NULL,
    CONSTRAINT function_type_pk PRIMARY KEY(function_type_id)
);

CREATE TABLE kam_edge (
    kam_edge_id             INTEGER NOT NULL,
    kam_source_node_id      INTEGER NOT NULL,
    kam_target_node_id      INTEGER NOT NULL,
    relationship_type_id    INTEGER NOT NULL,
    CONSTRAINT kam_edge_fk PRIMARY KEY(kam_edge_id)
);

CREATE TABLE kam_node (
    kam_node_id         INTEGER NOT NULL,
    function_type_id    INTEGER NOT NULL,
    node_label_oid      INTEGER NOT NULL,
    CONSTRAINT kam_node_pk PRIMARY KEY(kam_node_id)
);

CREATE TABLE kam_node_parameter (
    kam_node_parameter_id    INTEGER GENERATED ALWAYS AS IDENTITY NOT NULL,
    kam_global_parameter_id  INTEGER NOT NULL,
    kam_node_id              INTEGER NOT NULL,
    ordinal                  INTEGER NOT NULL,
    CONSTRAINT kam_node_parameter_pk PRIMARY KEY(kam_node_parameter_id)
);

CREATE TABLE kam_parameter_uuid  ( 
    kam_parameter_uuid_id    INTEGER GENERATED ALWAYS AS IDENTITY NOT NULL,
    kam_global_parameter_id  INTEGER NOT NULL,
    most_significant_bits    BIGINT NOT NULL,
    least_significant_bits   BIGINT NOT NULL,
    CONSTRAINT kam_parameter_uuid_pk PRIMARY KEY(kam_parameter_uuid_id)
);

CREATE TABLE namespace (
    namespace_id             INTEGER NOT NULL,
    prefix                   VARCHAR(8),
    resource_location_oid    INTEGER NOT NULL,
    CONSTRAINT namespace_pk PRIMARY KEY(namespace_id)
);

CREATE TABLE objects (
    objects_id         INTEGER GENERATED ALWAYS AS IDENTITY NOT NULL,
    type_id            INTEGER NOT NULL,
    varchar_value      VARCHAR(4000),
    objects_text_id    INTEGER,
    CONSTRAINT objects_id_pk PRIMARY KEY(objects_id)
);

CREATE TABLE objects_text (
    objects_text_id    INTEGER GENERATED ALWAYS AS IDENTITY NOT NULL,
    text_value        CLOB(2147483646) NOT NULL,
    CONSTRAINT objects_text_pk PRIMARY KEY(objects_text_id)
);

CREATE TABLE objects_type (
    objects_type_id    INTEGER NOT NULL,
    name              VARCHAR(64) NOT NULL,
    CONSTRAINT objects_type_pk PRIMARY KEY(objects_type_id)
);

CREATE TABLE relationship_type (
    relationship_type_id    INTEGER NOT NULL,
    name                    VARCHAR(64) NOT NULL,
    CONSTRAINT relationship_type_id_pk PRIMARY KEY(relationship_type_id)
);

CREATE TABLE statement (
    statement_id                INTEGER NOT NULL,
    document_id                 INTEGER NOT NULL,
    subject_term_id             INTEGER NOT NULL,
    relationship_type_id        INTEGER,
    object_term_id              INTEGER,
    nested_subject_id           INTEGER,
    nested_relationship_type_id INTEGER,
    nested_object_id            INTEGER,
    CONSTRAINT statement_id_pk PRIMARY KEY(statement_id)
);

CREATE TABLE kam_edge_statement_map (
    kam_edge_statement_id    INTEGER GENERATED ALWAYS AS IDENTITY NOT NULL,
    kam_edge_id              INTEGER NOT NULL,
    statement_id             INTEGER NOT NULL,
    PRIMARY KEY(kam_edge_statement_id)
);

CREATE TABLE statement_annotation_map (
    statement_annotation_id    INTEGER GENERATED ALWAYS AS IDENTITY NOT NULL,
    statement_id               INTEGER NOT NULL,
    annotation_id              INTEGER NOT NULL,
    CONSTRAINT statement_annotation_id_pk PRIMARY KEY(statement_annotation_id)
);

CREATE TABLE term (
    term_id           INTEGER NOT NULL,
    kam_node_id       INTEGER NOT NULL,
    term_label_oid    INTEGER NOT NULL,
    CONSTRAINT term_pk PRIMARY KEY(term_id)
);

CREATE TABLE term_parameter (
    term_parameter_id       INTEGER NOT NULL,
    kam_global_parameter_id INTEGER NOT NULL,
    term_id                 INTEGER NOT NULL,
    namespace_id            INTEGER,
    parameter_value_oid     INTEGER NOT NULL,
    ordinal                 INTEGER NOT NULL,
    CONSTRAINT parameter_pk PRIMARY KEY(term_parameter_id)
);

ALTER TABLE statement_annotation_map
    ADD CONSTRAINT a_sa_fk
    FOREIGN KEY(annotation_id)
    REFERENCES annotation(annotation_id);

ALTER TABLE document_annotation_def_map
    ADD CONSTRAINT ad_dad_fk
    FOREIGN KEY(annotation_definition_id)
    REFERENCES annotation_definition(annotation_definition_id);

ALTER TABLE annotation
    ADD CONSTRAINT ad_a_fk
    FOREIGN KEY(annotation_definition_id)
    REFERENCES annotation_definition(annotation_definition_id);

ALTER TABLE annotation_definition
    ADD CONSTRAINT ad_adt_fk
    FOREIGN KEY(annotation_definition_type_id)
    REFERENCES annotation_definition_type(annotation_definition_type_id);

ALTER TABLE statement
    ADD CONSTRAINT dhi_s_fk
    FOREIGN KEY(document_id)
    REFERENCES document_header_information(document_id);

ALTER TABLE document_namespace_map
    ADD CONSTRAINT d_dn_fk
    FOREIGN KEY(document_id)
    REFERENCES document_header_information(document_id);

ALTER TABLE document_annotation_def_map
    ADD CONSTRAINT dhi_dad_fk
    FOREIGN KEY(document_id)
    REFERENCES document_header_information(document_id);

ALTER TABLE kam_node
    ADD CONSTRAINT ft_kn_fk
    FOREIGN KEY(function_type_id)
    REFERENCES function_type(function_type_id);

ALTER TABLE kam_edge_statement_map
    ADD CONSTRAINT ke_kas_fk
    FOREIGN KEY(kam_edge_id)
    REFERENCES kam_edge(kam_edge_id);

ALTER TABLE kam_node_parameter
    ADD CONSTRAINT kn_knp_fk
    FOREIGN KEY(kam_node_id)
    REFERENCES kam_node(kam_node_id);

ALTER TABLE kam_edge
    ADD CONSTRAINT kn_kes_fk
    FOREIGN KEY(kam_source_node_id)
    REFERENCES kam_node(kam_node_id);

ALTER TABLE kam_edge
    ADD CONSTRAINT kn_ket_fk
    FOREIGN KEY(kam_target_node_id)
    REFERENCES kam_node(kam_node_id);

ALTER TABLE term
    ADD CONSTRAINT kn_t_fk
    FOREIGN KEY(kam_node_id)
    REFERENCES kam_node(kam_node_id);

ALTER TABLE document_namespace_map
    ADD CONSTRAINT n_dn_fk
    FOREIGN KEY(namespace_id)
    REFERENCES namespace(namespace_id);

ALTER TABLE term_parameter
    ADD CONSTRAINT n_p_fk
    FOREIGN KEY(namespace_id)
    REFERENCES namespace(namespace_id);

ALTER TABLE annotation
    ADD CONSTRAINT o_a_n_fk
    FOREIGN KEY(value_oid)
    REFERENCES objects(objects_id);

ALTER TABLE annotation_definition
    ADD CONSTRAINT o_ad_fk
    FOREIGN KEY(domain_value_oid)
    REFERENCES objects(objects_id);

ALTER TABLE term_parameter
    ADD CONSTRAINT o_p_fk
    FOREIGN KEY(parameter_value_oid)
    REFERENCES objects(objects_id);

ALTER TABLE namespace
    ADD CONSTRAINT o_n_fk
    FOREIGN KEY(resource_location_oid)
    REFERENCES objects(objects_id);

ALTER TABLE term
    ADD CONSTRAINT o_t_fk
    FOREIGN KEY(term_label_oid)
    REFERENCES objects(objects_id);

ALTER TABLE kam_node
    ADD CONSTRAINT o_kn_fk
    FOREIGN KEY(node_label_oid)
    REFERENCES objects(objects_id);

ALTER TABLE objects
    ADD CONSTRAINT otx_o_fk
    FOREIGN KEY(objects_text_id)
    REFERENCES objects_text(objects_text_id);

ALTER TABLE objects
    ADD CONSTRAINT oty_o_fk
    FOREIGN KEY(type_id)
    REFERENCES objects_type(objects_type_id);

ALTER TABLE kam_edge
    ADD CONSTRAINT rt_ke_fk
    FOREIGN KEY(relationship_type_id)
    REFERENCES relationship_type(relationship_type_id);

ALTER TABLE statement
    ADD CONSTRAINT rt_srt_fk
    FOREIGN KEY(relationship_type_id)
    REFERENCES relationship_type(relationship_type_id);

ALTER TABLE statement
    ADD CONSTRAINT rt_snr_fk
    FOREIGN KEY(nested_relationship_type_id)
    REFERENCES relationship_type(relationship_type_id);

ALTER TABLE statement_annotation_map
    ADD CONSTRAINT s_sa_fk
    FOREIGN KEY(statement_id)
    REFERENCES statement(statement_id);

ALTER TABLE kam_edge_statement_map
    ADD CONSTRAINT s_kes_fk
    FOREIGN KEY(statement_id)
    REFERENCES statement(statement_id);

ALTER TABLE statement
    ADD CONSTRAINT t_sst_fk
    FOREIGN KEY(subject_term_id)
    REFERENCES term(term_id);

ALTER TABLE statement
    ADD CONSTRAINT t_sot_fk
    FOREIGN KEY(object_term_id)
    REFERENCES term(term_id);

ALTER TABLE term_parameter
    ADD CONSTRAINT t_tp_fk
    FOREIGN KEY(term_id)
    REFERENCES term(term_id);

ALTER TABLE statement
    ADD CONSTRAINT t_sns_fk
    FOREIGN KEY(nested_subject_id)
    REFERENCES term(term_id);

ALTER TABLE statement
    ADD CONSTRAINT t_s_no_fk
    FOREIGN KEY(nested_object_id)
    REFERENCES term(term_id);

CREATE INDEX knp_global_parameter_idx
    ON kam_node_parameter (kam_global_parameter_id);

CREATE INDEX tp_global_parameter_idx
    ON term_parameter (kam_global_parameter_id);

CREATE INDEX kpu_global_parameter_idx
    ON kam_parameter_uuid (kam_global_parameter_id);
    
CREATE INDEX kpu_uuid_idx
    ON kam_parameter_uuid (most_significant_bits, least_significant_bits);
