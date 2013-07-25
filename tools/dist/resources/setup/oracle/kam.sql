-- This script creates the necessary Oracle database objects
-- for the KAM schema. It needs to be run for every predefined
-- KAM schemas.

-- create tables
-- TABLE: annotation
CREATE TABLE annotation (
    annotation_id            NUMBER(11,0) PRIMARY KEY NOT NULL,
    value_oid                NUMBER(11,0) NOT NULL,
    annotation_definition_id NUMBER(11,0) NOT NULL
);

-- TABLE: annotation_definition
CREATE TABLE annotation_definition (
    annotation_definition_id      NUMBER(11,0) PRIMARY KEY NOT NULL,
    name                          VARCHAR(255) NOT NULL,
    description                   VARCHAR(255),
    annotation_usage              VARCHAR(255),
    domain_value_oid              NUMBER(11,0) NOT NULL,
    annotation_definition_type_id NUMBER(11,0) NOT NULL
);


-- TABLE: annotation_definition_type
CREATE TABLE annotation_definition_type (
    annotation_definition_type_id NUMBER(11,0) PRIMARY KEY NOT NULL,
    name                          VARCHAR(64) NOT NULL
);


-- TABLE: document_header_information
CREATE TABLE document_header_information (
    document_id         NUMBER(11,0) PRIMARY KEY NOT NULL,
    name                VARCHAR(255) NOT NULL,
    description         VARCHAR(255) NULL,
    version             VARCHAR(64) NULL,
    copyright           VARCHAR(4000) NULL,
    disclaimer          VARCHAR(4000) NULL,
    contact_information VARCHAR(4000) NULL,
    license_information VARCHAR(4000) NULL,
    authors             VARCHAR(255) NULL
);


-- TABLE: document_annotation_def_map
CREATE TABLE document_annotation_def_map (
    document_annotation_definition NUMBER(11,0) PRIMARY KEY NOT NULL,
    document_id                    NUMBER(11,0) NOT NULL,
    annotation_definition_id       NUMBER(11,0) NOT NULL
);


CREATE SEQUENCE dadm_pk_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE
TRIGGER dadm_pk_trigger
BEFORE INSERT ON document_annotation_def_map
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW BEGIN
  IF INSERTING THEN
    IF :NEW.document_annotation_definition IS NULL THEN
      SELECT dadm_pk_seq.nextval INTO :NEW.document_annotation_definition FROM dual;
    END IF;
  END IF;
END;
/

-- TABLE: document_namespace_map
CREATE TABLE document_namespace_map (
    document_namespace_id     NUMBER(11,0) PRIMARY KEY NOT NULL,
    document_id               NUMBER(11,0) NOT NULL,
    namespace_id              NUMBER(11,0) NOT NULL
);


CREATE SEQUENCE dnm_pk_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE
TRIGGER dnm_pk_trigger
BEFORE INSERT ON document_namespace_map
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW BEGIN
  IF INSERTING THEN
    IF :NEW.document_namespace_id IS NULL THEN
      SELECT dnm_pk_seq.nextval INTO :NEW.document_namespace_id FROM dual;
    END IF;
  END IF;
END;
/

-- TABLE: function_type
CREATE TABLE function_type (
    function_type_id NUMBER(11,0) PRIMARY KEY NOT NULL,
    name             VARCHAR(64) NOT NULL
);


-- TABLE: kam_edge
CREATE TABLE kam_edge (
    kam_edge_id          NUMBER(11,0) PRIMARY KEY NOT NULL,
    kam_source_node_id   NUMBER(11,0) NOT NULL,
    kam_target_node_id   NUMBER(11,0) NOT NULL,
    relationship_type_id NUMBER(11,0) NOT NULL
);


-- TABLE: kam_node
CREATE TABLE kam_node (
    kam_node_id      NUMBER(11,0) PRIMARY KEY NOT NULL,
    function_type_id NUMBER(11,0) NOT NULL,
    node_label_oid   NUMBER(11,0) NOT NULL
);


-- TABLE: kam_node_parameter
CREATE TABLE kam_node_parameter (
    kam_node_parameter_id   NUMBER(11,0) PRIMARY KEY NOT NULL,
    kam_global_parameter_id NUMBER(11,0) NOT NULL,
    kam_node_id             NUMBER(11,0) NOT NULL,
    ordinal                 NUMBER(11,0) NOT NULL
);


CREATE SEQUENCE knp_pk_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE
TRIGGER knp_pk_trigger
BEFORE INSERT ON kam_node_parameter
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW BEGIN
  IF INSERTING THEN
    IF :NEW.kam_node_parameter_id IS NULL THEN
      SELECT knp_pk_seq.nextval INTO :NEW.kam_node_parameter_id FROM dual;
    END IF;
  END IF;
END;
/

-- TABLE: kam_parameter_uuid
CREATE TABLE kam_parameter_uuid  ( 
    kam_parameter_uuid_id    NUMBER(11,0) PRIMARY KEY NOT NULL,
    kam_global_parameter_id  NUMBER(11,0) NOT NULL,
    most_significant_bits    NUMBER(19,0) NOT NULL,
    least_significant_bits   NUMBER(19,0) NOT NULL

);

CREATE SEQUENCE kpu_pk_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE
TRIGGER kpu_pk_trigger
BEFORE INSERT ON kam_parameter_uuid
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW BEGIN
  IF INSERTING THEN
    IF :NEW.kam_parameter_uuid_id IS NULL THEN
      SELECT kpu_pk_seq.nextval INTO :NEW.kam_parameter_uuid_id FROM dual;
    END IF;
  END IF;
END;
/

-- TABLE: namespace
CREATE TABLE namespace (
    namespace_id          NUMBER(11,0) PRIMARY KEY NOT NULL,
    prefix                VARCHAR(8) NULL,
    resource_location_oid NUMBER(11,0) NOT NULL
);


-- TABLE: objects
CREATE TABLE objects (
    objects_id     NUMBER(11,0) PRIMARY KEY NOT NULL,
    type_id        NUMBER(11,0) NOT NULL,
    varchar_value  VARCHAR(4000) NULL,
    objects_text_id number NULL
);


CREATE SEQUENCE o_pk_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE
TRIGGER o_pk_trigger
BEFORE INSERT ON objects
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW BEGIN
  IF INSERTING THEN
    IF :NEW.objects_id IS NULL THEN
      SELECT o_pk_seq.nextval INTO :NEW.objects_id FROM dual;
    END IF;
  END IF;
END;
/

-- TABLE: object_text
CREATE TABLE objects_text (
    objects_text_id NUMBER(11,0) PRIMARY KEY NOT NULL,
    text_value      CLOB NOT NULL
);


CREATE SEQUENCE ot_pk_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE
TRIGGER ot_pk_trigger
BEFORE INSERT ON objects_text
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW BEGIN
  IF INSERTING THEN
    IF :NEW.objects_text_id IS NULL THEN
      SELECT ot_pk_seq.nextval INTO :NEW.objects_text_id FROM dual;
    END IF;
  END IF;
END;
/

-- TABLE: object_type
CREATE TABLE objects_type (
    objects_type_id NUMBER(11,0) PRIMARY KEY NOT NULL,
    name            VARCHAR(64) NOT NULL
);


-- TABLE: relationship_type
CREATE TABLE relationship_type (
    relationship_type_id NUMBER(11,0) PRIMARY KEY NOT NULL,
    name                 VARCHAR(64) NOT NULL
);


-- TABLE: statement
CREATE TABLE statement (
    statement_id                NUMBER(11,0) PRIMARY KEY NOT NULL,
    document_id                 NUMBER(11,0) NOT NULL,
    subject_term_id             NUMBER(11,0) NOT NULL,
    relationship_type_id        NUMBER(11,0) NULL,
    object_term_id              NUMBER(11,0) NULL,
    nested_subject_id           NUMBER(11,0) NULL,
    nested_relationship_type_id NUMBER(11,0) NULL,
    nested_object_id            NUMBER(11,0) NULL,
    bel_statement               VARCHAR(4000) NOT NULL
);


-- TABLE: kam_edge_statement_map
CREATE TABLE kam_edge_statement_map (
    kam_edge_statement_id    NUMBER(11,0) PRIMARY KEY NOT NULL,
    kam_edge_id              NUMBER(11,0) NOT NULL,
    statement_id             NUMBER(11,0) NOT NULL
);


CREATE SEQUENCE kesm_pk_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE
TRIGGER kesm_pk_seq
BEFORE INSERT ON kam_edge_statement_map
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW BEGIN
  IF INSERTING THEN
    IF :NEW.kam_edge_statement_id IS NULL THEN
      SELECT kesm_pk_seq.nextval INTO :NEW.kam_edge_statement_id FROM dual;
    END IF;
  END IF;
END;
/

-- TABLE: statement_annotation_map
CREATE TABLE statement_annotation_map (
    statement_annotation_id NUMBER(11,0) PRIMARY KEY NOT NULL,
    statement_id            NUMBER(11,0) NOT NULL,
    annotation_id           NUMBER(11,0) NOT NULL
);


CREATE SEQUENCE sam_pk_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE
TRIGGER sam_pk_trigger
BEFORE INSERT ON statement_annotation_map
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW BEGIN
  IF INSERTING THEN
    IF :NEW.statement_annotation_id IS NULL THEN
      SELECT sam_pk_seq.nextval INTO :NEW.statement_annotation_id FROM dual;
    END IF;
  END IF;
END;
/

-- TABLE: term
CREATE TABLE term (
    term_id        NUMBER(11,0) PRIMARY KEY NOT NULL,
    kam_node_id    NUMBER(11,0) NOT NULL,
    term_label_oid NUMBER(11,0) NOT NULL
);


-- TABLE: term_parameter
CREATE TABLE term_parameter (
    term_parameter_id       NUMBER(11,0) PRIMARY KEY NOT NULL,
    kam_global_parameter_id NUMBER(11,0) NOT NULL,
    term_id                 NUMBER(11,0) NOT NULL,
    namespace_id            NUMBER(11,0) NULL,
    parameter_value_oid     NUMBER(11,0) NOT NULL,
    ordinal                 NUMBER(11,0) NOT NULL
);


-- Foreign key constraints
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

CREATE INDEX tp_ordinal
    ON @.term_parameter (ordinal)

CREATE INDEX kn_ordinal
    ON @.kam_node_parameter (ordinal)

