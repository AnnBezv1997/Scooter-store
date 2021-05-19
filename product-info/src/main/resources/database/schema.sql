DROP TYPE IF EXISTS discount_type CASCADE ;
create type discount_type as enum ('PERCENT','ABSOLUTE');

DROP TABLE IF EXISTS category CASCADE;
CREATE TABLE category
(
    id  integer not null
        constraint category_pk
               primary key,
    name character varying(150) not null,
    description character varying(1000) not null,
    parent_id integer references category

);


DROP SEQUENCE IF EXISTS category_id_seq CASCADE;
CREATE SEQUENCE category_id_seq START WITH 8 INCREMENT BY 1;

DROP TABLE IF EXISTS discount CASCADE;
CREATE TABLE discount
(
    id  integer not null
        constraint discount_id
               primary key,
    name character varying(150),
    description character varying(1000),
    type  discount_type ,
    value integer
);
DROP SEQUENCE IF EXISTS discount_id_seq CASCADE;
CREATE SEQUENCE discount_id_seq START WITH 4 INCREMENT BY 1;

DROP TABLE IF EXISTS stock_status CASCADE;
CREATE TABLE  stock_status (
    id  integer not null
     constraint stock_status_pk
     primary key ,
    count integer not null

);
DROP SEQUENCE IF EXISTS stock_status_id_seq CASCADE;
CREATE SEQUENCE stock_status_id_seq START WITH 11 INCREMENT BY 1;


DROP TABLE IF EXISTS product CASCADE ;
CREATE TABLE product
(
    id  integer not null
        constraint product_pk
               primary key,
    name character varying(150),
    description character varying(1000),
    price integer ,
    image character varying(1000),
    stock_status_id integer unique references stock_status(id) ,
    category_id integer
            constraint product_category_id_fk
            references category,
    discount_id integer
                constraint product_discount_id_fk
                references discount
);
DROP SEQUENCE IF EXISTS product_id_seq CASCADE;
CREATE SEQUENCE product_id_seq START WITH 11 INCREMENT BY 1;





