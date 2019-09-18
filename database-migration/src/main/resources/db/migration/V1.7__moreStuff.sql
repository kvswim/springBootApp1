alter table reports drop column last_modified_date;
alter table reports drop column created_date;

alter table reports_aud drop column last_modified_date;
alter table reports_aud drop column created_date;
alter table reports_aud add column username VARCHAR(100);

alter table indicators drop column created_date;
alter table indicators drop column created_by;

create table indicators_aud
    (
            id              INTEGER,
            value           VARCHAR(100),
            ind_type        INTEGER,
            rev             integer, -- txn id
            rev_type        integer, -- 0 create, 1 update, 2 delete
            timestamp       TIMESTAMP,
            username        VARCHAR(100)
    );