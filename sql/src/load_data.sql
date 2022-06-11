--user 1's path
COPY MENU
FROM '/extra/ckurp001/cs166/cs166_project/data/menu.csv'
WITH DELIMITER ';';

COPY USERS
FROM '/extra/ckurp001/cs166/cs166_project/data/users.csv'
WITH DELIMITER ';';

COPY ORDERS
FROM '/extra/ckurp001/cs166/cs166_project/data/orders.csv'
WITH DELIMITER ';';
ALTER SEQUENCE orders_orderid_seq RESTART 87257;

COPY ITEMSTATUS
FROM '/extra/ckurp001/cs166/cs166_project/data/itemStatus.csv'
WITH DELIMITER ';';


--user 2's path
COPY MENU
FROM 'YOUR PATH HERE/data/menu.csv'
WITH DELIMITER ';';

COPY USERS
FROM 'YOUR PATH HERE/data/users.csv'
WITH DELIMITER ';';

COPY ORDERS
FROM 'YOUR PATH HERE/data/orders.csv'
WITH DELIMITER ';';
ALTER SEQUENCE orders_orderid_seq RESTART 87257;

COPY ITEMSTATUS
FROM 'YOUR PATH HERE/data/itemStatus.csv'
WITH DELIMITER ';';


--user 3's path
COPY MENU
FROM 'YOUR PATH HERE/data/menu.csv'
WITH DELIMITER ';';

COPY USERS
FROM 'YOUR PATH HERE/data/users.csv'
WITH DELIMITER ';';

COPY ORDERS
FROM 'YOUR PATH HERE/data/orders.csv'
WITH DELIMITER ';';
ALTER SEQUENCE orders_orderid_seq RESTART 87257;

COPY ITEMSTATUS
FROM 'YOUR PATH HERE/data/itemStatus.csv'
WITH DELIMITER ';';


