DROP TABLE IF EXISTS product;
CREATE TABLE product (
    code BIGSERIAL PRIMARY KEY NOT NULL,
    name character varying(150),
    category character varying(150),
    price double precision,
    description character varying(1000),
    manufacturer character varying(150)
);