DROP TABLE IF EXISTS role CASCADE;
CREATE TABLE role
(
    id  bigserial not null
        constraint role_pk
               primary key,
    name character varying(20) not null
);
DROP TABLE IF EXISTS user_account;
CREATE TABLE user_account (
    id   bigserial not null
         constraint user_pk
                primary key,
    login character varying(50),
    password character varying(255),
    address character varying(500),
    role_id integer
            constraint user_role_id_fk
            references role
);
