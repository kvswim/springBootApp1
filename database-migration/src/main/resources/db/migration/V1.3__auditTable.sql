create table reports_aud
(
          rev                 integer, -- txn id
          rev_type            integer, -- 0 create, 1 update, 2 delete
          id                  INTEGER,
          version             INTEGER,
          description         TEXT,
          display_name        VARCHAR(255),
          reviewed            BOOLEAN,
          reference_source    INTEGER,
          priority            INTEGER,
          created_date        TIMESTAMP,
          last_modified_date  TIMESTAMP,
          is_custom_report    BOOLEAN DEFAULT FALSE,
          reserved            BOOLEAN,
          reserved_by         VARCHAR(255)
);