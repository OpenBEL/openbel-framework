CREATE SEQUENCE @.kam_kam_id_seq
##
CREATE TABLE @.kam ( 
    kam_id        INTEGER NOT NULL DEFAULT NEXTVAL('@.kam_kam_id_seq'),
    name          VARCHAR(255) NOT NULL UNIQUE,
    description   VARCHAR(255) NOT NULL,
    last_compiled TIMESTAMP NOT NULL,
    schema_name   VARCHAR(64) NOT NULL UNIQUE,
    PRIMARY KEY(kam_id)
)
##
ALTER SEQUENCE @.kam_kam_id_seq OWNED BY @.kam.kam_id
##