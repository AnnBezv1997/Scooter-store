DROP TYPE IF EXISTS order_status CASCADE ;
create type order_status as enum ('DELIVERED', 'CANCELED');

DROP TYPE IF EXISTS order_status_pay CASCADE ;
create type order_status_pay as enum ('YES', 'NOT');

DROP TABLE IF EXISTS user_order CASCADE ;
CREATE TABLE user_order
(
    id  bigserial not null
        constraint order_pk
               primary key,
    user_id integer,
    address character varying (1000),
    date timestamp ,
    total_price integer ,
    status_order order_status,
    status_pay order_status_pay


);

DROP TABLE IF EXISTS basket CASCADE ;
CREATE TABLE basket
(   id  bigserial not null
        constraint basket_pk
        primary key,
        user_id integer,
    user_order_id integer
            constraint basket_order_id_fk
            references user_order,
    product_id integer,
    product_price integer,
    count_product integer,
    date timestamp
);


