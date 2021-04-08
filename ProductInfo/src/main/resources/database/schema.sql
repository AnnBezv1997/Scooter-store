DROP TYPE IF EXISTS discount_type CASCADE ;
create type discount_type as enum ('TEN_PERCENT', 'TWENTY_PERCENT', 'FIFTY_PERCENT');
DROP TABLE IF EXISTS category CASCADE;
CREATE TABLE category
(
    id  bigserial not null
        constraint category_pk
               primary key,
    name character varying(150) not null,
    description character varying(1000) not null,
    parent_id integer
            constraint product_category_id_fk
            references category

);

DROP TABLE IF EXISTS product CASCADE ;
CREATE TABLE product
(
    id  bigserial not null
        constraint product_pk
               primary key,
    name character varying(150),
    description character varying(1000),
    price integer ,
    image character varying(1000),
    stock_status_id integer,
    category_id integer
            constraint product_category_id_fk
            references category,
    discount_id integer
                constraint product_discount_id_fk
                references discount
);

DROP TABLE IF EXISTS stock_status CASCADE;
CREATE TABLE  stock_status (
    id  bigserial not null
        unique references product(id) ,
    count integer not null

);

DROP TABLE IF EXISTS discount CASCADE;
CREATE TABLE discount (
    id  bigserial not null
        constraint discount_id
               primary key,
    name character varying(150),
    description character varying(1000),
    type discount_type,
    value integer
);



