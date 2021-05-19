INSERT INTO category VALUES (1, 'Bread and Pastries','Fresh bread and pastries');
INSERT INTO category VALUES (2, 'Dairy products', 'Dairy products');
INSERT INTO category VALUES (3, 'Cheese', 'Different types of cheese', 2);
INSERT INTO category VALUES (4, 'Meat and fish', 'Meat and fish products');
INSERT INTO category VALUES (5, 'Meat','Fresh meat', 4);
INSERT INTO category VALUES (6, 'Poultry', 'Poultry meat', 4 );
INSERT INTO category VALUES (7, 'Minced', 'Minced', 5);


INSERT INTO discount VALUES (1, 'Best Offer', 'Discount','PERCENT', 10 );
INSERT INTO discount VALUES (2, 'Seasonal Discount', 'Seasonal Discount','ABSOLUTE', 10 );
INSERT INTO discount VALUES (3, 'Sale', 'Sale','PERCENT', 50 );

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

INSERT INTO product VALUES (1, 'Mini baguette', ' Manual production: from kneading dough to packaging.Clean clear composition:wheat flour, salt, sugar, yeast.',50,'1.png',1,1);
INSERT INTO product VALUES (2, 'Country bread', ' Manual production: from kneading dough to packaging.Clean clear composition:wheat flour, salt, sugar, yeast, butter, natural honey.', 66, '2.png', 2, 1);
INSERT INTO product VALUES (3, 'Sunny Bun','High-grade wheat flour, drinking water, sunflower oil, salt, sugar, yeast.',45,'3.png',3,1);
INSERT INTO product VALUES (4, 'Sliced loaf', ' Premium wheat flour, drinking water,sunflower oil,salt,sugar, yeast.',55,'4.png',4,1);
INSERT INTO product VALUES (5, 'Camembert cheese', ' Soft farm cheese with white mold. Natural milk and mold crops.200g', 245, '5.png', 5, 3, 1);
INSERT INTO product VALUES (6, 'Tilsiter cheese','Natural cheese made from fresh cream and pure milk.Without the addition of powdered milk.200g.', 190, '6.png', 6, 3, 1);
INSERT INTO product VALUES (7, 'Russian cheese','Natural cheese made from fresh cream and pure milk.Without adding milk powder. 150g.', 150, '7.png', 7, 3);
INSERT INTO product VALUES (8, 'Steak minute','Marble beef 190g.', 400, '8.png', 8, 5);
INSERT INTO product VALUES (9, 'Turkey thigh fillet','Turkey meat.410g', 200, '9.png', 9, 6);
INSERT INTO product VALUES (10, 'Minced meat at home', ' Minced meat at home chilled, pork beef. 400g.', 190, '10.png', 10, 7);
