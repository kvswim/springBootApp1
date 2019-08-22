create table IND_TYPES
(
    id      INTEGER,
    value   VARCHAR(255) NOT NULL
);

create table LINK_REPORTS_INDICATORS
(
    id          INTEGER,
    report      INTEGER NOT NULL,
    indicator   INTEGER NOT NULL
);

alter table indicators add column ind_type INTEGER null;