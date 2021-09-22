DROP TABLE IF EXISTS role CASCADE;
CREATE TABLE role
(
    id  bigserial not null
        constraint role_pk
               primary key,
    name character varying(20) not null
);
DROP TABLE IF EXISTS user_account CASCADE;
CREATE TABLE user_account (
    id   serial not null
         constraint user_pk
                primary key ,
    login character varying(500),
    password character varying(255),
    name character varying(300),
    role_id integer
            constraint user_role_id_fk
            references role
);
DROP SEQUENCE IF EXISTS user_account_id_seq CASCADE;
CREATE SEQUENCE user_account_id_seq START WITH 3 INCREMENT BY 1;
DROP TABLE IF EXISTS address;
CREATE TABLE address (
    id   bigserial not null
         constraint address_pk
                primary key,
    address character varying(1500),
    user_account_id integer
            constraint address_user_id_fk
            references user_account
);

