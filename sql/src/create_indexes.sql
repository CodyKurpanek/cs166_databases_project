CREATE INDEX index1
ON orders
(timeStampRecieved);

CREATE INDEX index2
ON ItemStatus
( orderid );

CREATE INDEX menuItemType
ON Menu
(type);

CREATE INDEX menuItemName
ON Menu
(itemName);
