CREATE TABLE @.kam ( 
    kam_id        INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    name          VARCHAR(255) NOT NULL UNIQUE,
    description   VARCHAR(255) NOT NULL,
    last_compiled TIMESTAMP NOT NULL,
    schema_name   VARCHAR(64) NOT NULL UNIQUE,
    CONSTRAINT kam_pk PRIMARY KEY(kam_id)
)
##