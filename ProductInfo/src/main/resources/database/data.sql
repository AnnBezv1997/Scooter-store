INSERT INTO category VALUES (1,' Xлеб и выпечка','Свежий хлеб и выпечка');
INSERT INTO category VALUES (2,'Молочные продукты','Молочные продукты');
INSERT INTO category VALUES (3,'Сыр','Различные сорта сыра', 2);
INSERT INTO category VALUES (4,'Мясо и рыба','Мясная и рыбная продукция');
INSERT INTO category VALUES (5,'Мясо','Свежее мясо',4);
INSERT INTO category VALUES (6,' Птица','Мясо птицы',4 );
INSERT INTO category VALUES (7,'Фарш','Фарш',5);


INSERT INTO discount VALUES (1,'Выгодное предложение', 'Скидка','TEN_PERCENT',10 );
INSERT INTO discount VALUES (2,'Сезонная скидка', 'Сезонная скидка','TEN_PERCENT',10 );
INSERT INTO discount VALUES (3,'Распродажа', 'Распродажа','FIFTY_PERCENT',50 );

INSERT INTO product VALUES (1,'Мини-багет','Ручное производство: от замешивания теста до упаковки.Чистый понятный состав:мука пшеничная,соль,сахар,дрожжи.',50,'/',1,1);
INSERT INTO product VALUES (2,'Деревенский','Ручное производство: от замешивания теста до упаковки.Чистый понятный состав:мука пшеничная,соль,сахар,дрожжи,сливочное масло натуральный мед.',66,'/',2,1);
INSERT INTO product VALUES (3,'Булочка солнышко','Мука пшеничная хлебопекарная высшего сорта, вода питьевая,масло подсолнечное,соль,сахар,дрожжи.',45,'/',3,1);
INSERT INTO product VALUES (4,'Батон нарезной','Мука пшеничная хлебопекарная высшего сорта, вода питьевая,масло подсолнечное,соль,сахар,дрожжи.',55,'/',4,1);
INSERT INTO product VALUES (5,'Сыр камамбер','Мягкий фермерский сыр с белой плесенью. Натуральное молоко и плесневые культуры.200г',245,'/',5,3,1);
INSERT INTO product VALUES (6,'Сыр тильзитер','Сыр натуральный из свежих сливок и чистого молока.Без добавления сухого молока.200г.',190,'/',6,3,1);
INSERT INTO product VALUES (7,'Сыр российский','Сыр натуральный из свежих сливок и чистого молока.Без добавления сухого молока.150г.',150,'/',7,3);
INSERT INTO product VALUES (8,'Стейк минутка','Мраморная говядина 190г.',400,'/',8,5);
INSERT INTO product VALUES (9,'Филе бедра индейки','Индюшатина.410г',200,9,6);
INSERT INTO product VALUES (10,'Фарш по-домашнему','Фарш по-домашнему охлажденный, свинина говядина. 400г.',190,'/',10,7);
INSERT INTO stock_status VALUES (1, 100 );
INSERT INTO stock_status VALUES (2, 1500 );
INSERT INTO stock_status VALUES (3, 200 );
INSERT INTO stock_status VALUES (4, 170 );
INSERT INTO stock_status VALUES (5, 75 );
INSERT INTO stock_status VALUES (6, 90 );
INSERT INTO stock_status VALUES (7, 210 );
INSERT INTO stock_status VALUES (8, 10 );
INSERT INTO stock_status VALUES (9, 30 );
INSERT INTO stock_status VALUES (10, 40 );
