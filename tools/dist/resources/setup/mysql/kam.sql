-- This script creates the necessary MySQL database objects
-- for the KAM schema. It needs to be run for every predefined
-- KAM schemas.

CREATE TABLE annotation (
    annotation_id                 int(11) NOT NULL,
    value_oid                     int(11) NOT NULL,
    annotation_definition_id      int(11) NOT NULL,
    PRIMARY KEY(annotation_id)
);
CREATE TABLE annotation_definition (
    annotation_definition_id        int(11) NOT NULL,
    name                            varchar(255) NOT NULL,
    description                     varchar(255),
    annotation_usage                varchar(255),
    domain_value_oid                int(11) NOT NULL,
    annotation_definition_type_id   int(11) NOT NULL,
    CONSTRAINT annotation_definition_id_pk PRIMARY KEY(annotation_definition_id)
);
CREATE TABLE annotation_definition_type (
    annotation_definition_type_id    int(11) NOT NULL,
    name                             varchar(64) NOT NULL,
    PRIMARY KEY(annotation_definition_type_id)
);
CREATE TABLE document_header_information (
    document_id         int(11) NOT NULL,
    name                varchar(255) NOT NULL,
    description         varchar(255) NULL,
    version             varchar(64) NULL,
    copyright           varchar(4000) NULL,
    disclaimer          varchar(4000) NULL,
    contact_information varchar(4000) NULL,
    license_information varchar(4000) NULL,
    authors             varchar(255) NULL,
    PRIMARY KEY(document_id)
);
CREATE TABLE document_annotation_def_map (
    document_annotation_definition    int(11) AUTO_INCREMENT NOT NULL,
    document_id                       int(11) NOT NULL,
    annotation_definition_id          int(11) NOT NULL,
    PRIMARY KEY(document_annotation_definition)
);
CREATE TABLE document_namespace_map (
    document_namespace_id    int(11) AUTO_INCREMENT NOT NULL,
    document_id              int(11) NOT NULL,
    namespace_id             int(11) NOT NULL,
    PRIMARY KEY(document_namespace_id)
);
CREATE TABLE function_type (
    function_type_id    int(11) NOT NULL,
    name                varchar(64) NOT NULL,
    PRIMARY KEY(function_type_id)
);
CREATE TABLE kam_edge (
    kam_edge_id             int(11) NOT NULL,
    kam_source_node_id      int(11) NOT NULL,
    kam_target_node_id      int(11) NOT NULL,
    relationship_type_id    int(11) NOT NULL,
    PRIMARY KEY(kam_edge_id)
);
CREATE TABLE kam_node (
    kam_node_id         int(11) NOT NULL,
    function_type_id    int(11) NOT NULL,
    node_label_oid      int(11) NOT NULL,
    PRIMARY KEY(kam_node_id)
);
CREATE TABLE kam_node_parameter (
    kam_node_parameter_id    int(11) AUTO_INCREMENT NOT NULL,
    kam_global_parameter_id  int(11) NOT NULL,
    kam_node_id              int(11) NOT NULL,
    ordinal                  int(11) NOT NULL,
    PRIMARY KEY(kam_node_parameter_id)
);
CREATE TABLE namespace (
    namespace_id             int(11) NOT NULL,
    prefix                   varchar(8) NULL,
    resource_location_oid    int(11) NOT NULL,
    PRIMARY KEY(namespace_id)
);
CREATE TABLE objects (
    objects_id         int(11) AUTO_INCREMENT NOT NULL,
    type_id           int(11) NOT NULL,
    varchar_value       text NULL,
    objects_text_id    int(11) NULL,
    PRIMARY KEY(objects_id)
);
CREATE TABLE objects_text (
    objects_text_id    int(11) AUTO_INCREMENT NOT NULL,
    text_value          longtext NOT NULL,
    PRIMARY KEY(objects_text_id)
);
CREATE TABLE objects_type (
    objects_type_id    int(11) NOT NULL,
    name              varchar(64) NOT NULL,
    PRIMARY KEY(objects_type_id)
);
CREATE TABLE relationship_type (
    relationship_type_id    int(11) NOT NULL,
    name                    varchar(64) NOT NULL,
    PRIMARY KEY(relationship_type_id)
);
CREATE TABLE statement (
    statement_id                int(15) NOT NULL,
    document_id                 int(15) NOT NULL,
    subject_term_id             int(15) NOT NULL,
    relationship_type_id        int(15) NULL,
    object_term_id              int(15) NULL,
    nested_subject_id           int(15) NULL,
    nested_relationship_type_id int(15) NULL,
    nested_object_id            int(15) NULL,
    PRIMARY KEY(statement_id)
);
CREATE TABLE kam_edge_statement_map (
    kam_edge_statement_id    int(15) AUTO_INCREMENT NOT NULL,
    kam_edge_id              int(15) NOT NULL,
    statement_id             int(15) NOT NULL,
    PRIMARY KEY(kam_edge_statement_id)
);
CREATE TABLE statement_annotation_map (
    statement_annotation_id    int(11) AUTO_INCREMENT NOT NULL,
    statement_id               int(11) NOT NULL,
    annotation_id              int(11) NOT NULL,
    PRIMARY KEY(statement_annotation_id)
);
CREATE TABLE term (
    term_id           int(11) NOT NULL,
    kam_node_id       int(11) NOT NULL,
    term_label_oid    int(11) NOT NULL,
    PRIMARY KEY(term_id)
);
CREATE TABLE term_parameter (
    term_parameter_id       int(11) NOT NULL,
    kam_global_parameter_id int(11) NOT NULL,
    term_id                 int(11) NOT NULL,
    namespace_id            int(11) NULL,
    parameter_value_oid     int(11) NOT NULL,
    ordinal                 int(11) NOT NULL,
    PRIMARY KEY(term_parameter_id)
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
    ADD CONSTRAINT d_s_fk
    FOREIGN KEY(document_id)
    REFERENCES document_header_information(document_id);
ALTER TABLE document_namespace_map
    ADD CONSTRAINT d_dn_fk
    FOREIGN KEY(document_id)
    REFERENCES document_header_information(document_id);
ALTER TABLE document_annotation_def_map
    ADD CONSTRAINT d_dad_fk
    FOREIGN KEY(document_id)
    REFERENCES document_header_information(document_id);
ALTER TABLE kam_node
    ADD CONSTRAINT ft_kn_fk
    FOREIGN KEY(function_type_id)
    REFERENCES function_type(function_type_id);
ALTER TABLE kam_edge_statement_map
    ADD CONSTRAINT ke_s_stmt_fk
    FOREIGN KEY(statement_id)
    REFERENCES statement(statement_id);
ALTER TABLE kam_edge_statement_map
    ADD CONSTRAINT ke_s_ke_fk
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
ALTER TABLE objects
    ADD CONSTRAINT otext_o_fk
    FOREIGN KEY(objects_text_id)
    REFERENCES objects_text(objects_text_id);
ALTER TABLE objects
    ADD CONSTRAINT otype_o_fk
    FOREIGN KEY(type_id)
    REFERENCES objects_type(objects_type_id);
ALTER TABLE kam_edge
    ADD CONSTRAINT rt_ke_fk
    FOREIGN KEY(relationship_type_id)
    REFERENCES relationship_type(relationship_type_id);
ALTER TABLE statement
    ADD CONSTRAINT rt_s_fk
    FOREIGN KEY(relationship_type_id)
    REFERENCES relationship_type(relationship_type_id);
ALTER TABLE statement_annotation_map
    ADD CONSTRAINT s_sa_fk
    FOREIGN KEY(statement_id)
    REFERENCES statement(statement_id);
ALTER TABLE statement
    ADD CONSTRAINT kn_tst_fk
    FOREIGN KEY(subject_term_id)
    REFERENCES term(term_id);
ALTER TABLE statement
    ADD CONSTRAINT rt_s_fk
    FOREIGN KEY(relationship_type_id)
    REFERENCES relationship_type(relationship_type_id);
ALTER TABLE statement
    ADD CONSTRAINT kn_tot_fk
    FOREIGN KEY(object_term_id)
    REFERENCES term(term_id);
ALTER TABLE statement
    ADD CONSTRAINT kn_ns_fk
    FOREIGN KEY(nested_subject_id)
    REFERENCES term(term_id);
ALTER TABLE statement
    ADD CONSTRAINT nrt_s_fk
    FOREIGN KEY(nested_relationship_type_id)
    REFERENCES relationship_type(relationship_type_id);
ALTER TABLE statement
    ADD CONSTRAINT kn_no_fk
    FOREIGN KEY(nested_object_id)
    REFERENCES term(term_id);
ALTER TABLE term_parameter
    ADD CONSTRAINT t_tp_fk
    FOREIGN KEY(term_id)
    REFERENCES term(term_id);
CREATE INDEX knp_global_parameter_idx
    ON kam_node_parameter (kam_global_parameter_id);
CREATE INDEX tp_global_parameter_idx
    ON term_parameter (kam_global_parameter_id);
CREATE INDEX kpu_global_parameter_idx
    ON kam_parameter_uuid (kam_global_parameter_id);
CREATE INDEX kpu_uuid_idx
    ON kam_parameter_uuid (most_significant_bits, least_significant_bits);