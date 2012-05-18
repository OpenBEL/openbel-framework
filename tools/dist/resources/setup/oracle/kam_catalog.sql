-- This script creates the necessary database objects
-- for the KAM_CATALOG schema.

-- create tables
-- TABLE: kam
CREATE TABLE kam ( 
    kam_id        NUMBER(11,0) PRIMARY KEY NOT NULL,
    name          VARCHAR(255) NOT NULL UNIQUE,
    description   VARCHAR(255) NOT NULL,
    last_compiled TIMESTAMP NOT NULL,
    schema_name   VARCHAR(64) NOT NULL UNIQUE
);

CREATE SEQUENCE kam_pk_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE
TRIGGER kam_pk_trigger
BEFORE INSERT ON kam
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW BEGIN
  IF INSERTING THEN
    IF :NEW.kam_id IS NULL THEN
      SELECT kam_pk_seq.nextval INTO :NEW.kam_id FROM dual;
    END IF;
  END IF;
END;
/
