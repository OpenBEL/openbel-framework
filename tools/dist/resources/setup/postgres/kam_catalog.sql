CREATE TABLE kam ( 
    kam_id        SERIAL,
    name          VARCHAR(255) NOT NULL UNIQUE,
    description   VARCHAR(255) NOT NULL,
    last_compiled TIMESTAMP NOT NULL,
    schema_name   VARCHAR(64) NOT NULL UNIQUE,
    PRIMARY KEY(kam_id)
);
