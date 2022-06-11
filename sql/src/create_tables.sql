DROP TABLE Users CASCADE;
DROP TABLE Orders CASCADE;
DROP TABLE Menu CASCADE;
DROP TABLE ItemStatus CASCADE;
DROP TABLE placesOrder;
DROP TABLE favoriteItem;
DROP TABLE ManagerUser CASCADE;
DROP TABLE EmployeeUser CASCADE;
DROP TABLE Modifies;
DROP TABLE Updates;
DROP TABLE setsType;
DROP TABLE orderHistory;
DROP TABLE views;

CREATE TABLE Users(
	login char(50) UNIQUE NOT NULL, 
	phoneNum char(16) UNIQUE, 
	password char(50) NOT NULL,
	favItems char(400),
	type char(8) NOT NULL,
	PRIMARY KEY(login));

CREATE TABLE Menu(
	itemName char(50) UNIQUE NOT NULL,
	type char(20) NOT NULL,
	price real NOT NULL,
	description char(400),
	imageURL char(256),
	PRIMARY KEY(itemName));

CREATE TABLE Orders(
	orderid serial UNIQUE NOT NULL,
	login char(50), 
	paid boolean,
	timeStampRecieved timestamp NOT NULL,
	total real NOT NULL,
	PRIMARY KEY(orderid));

CREATE TABLE ItemStatus(
	orderid integer,
	itemName char(50), 
	lastUpdated timestamp NOT NULL,
	status char(20), 
	comments char(130), 
	PRIMARY KEY(orderid,itemName),
	FOREIGN KEY(orderid) REFERENCES Orders(orderid),
	FOREIGN KEY(itemName) REFERENCES Menu(itemName));

CREATE TABLE placesOrder(
	orderid integer UNIQUE NOT NULL,
	itemName char(30) NOT NULL,
	PRIMARY KEY(orderid, itemName),
	FOREIGN KEY(orderid) REFERENCES Orders(orderid),
	FOREIGN KEY(itemName) REFERENCES Menu(itemName));

CREATE TABLE favoriteItem(
	login char(50) UNIQUE NOT NULL,
	itemName char(30) NOT NULL,
	PRIMARY KEY(login, itemName),
	FOREIGN KEY(login) REFERENCES Users(login),
	FOREIGN KEY(itemName) REFERENCES Menu(itemName));

CREATE TABLE ManagerUser(
	Managerlogin char(50) UNIQUE NOT NULL,
	PRIMARY KEY(Managerlogin),
	FOREIGN KEY(Managerlogin) REFERENCES Users(login));

CREATE TABLE EmployeeUser(
	Employeelogin char(50) UNIQUE NOT NULL,
	PRIMARY KEY(Employeelogin),
	FOREIGN KEY(Employeelogin) REFERENCES Users(login));

CREATE TABLE Modifies(
	Employeelogin char(50),
	Managerlogin char(50),
	orderid integer NOT NULL,
	itemName char(50) NOT NULL,
	PRIMARY KEY(orderid, itemName),
	FOREIGN KEY(Employeelogin) REFERENCES EmployeeUser(Employeelogin),
	FOREIGN KEY(Managerlogin) REFERENCES ManagerUser(Managerlogin),
	FOREIGN KEY(orderid) REFERENCES Orders(orderid),
	FOREIGN KEY(itemName) REFERENCES Menu(itemName));

CREATE TABLE Updates(
	login char(50),
	itemName char(30),
	PRIMARY KEY(login, itemName),
	FOREIGN KEY(login) REFERENCES ManagerUser(Managerlogin),
	FOREIGN KEY(itemName) REFERENCES Menu(itemName));

CREATE TABLE setsType(
	login char(50),
	Managerlogin char(50),
	PRIMARY KEY(login, Managerlogin),
	FOREIGN KEY(login) REFERENCES Users(login),
	FOREIGN KEY(Managerlogin) REFERENCES ManagerUser(Managerlogin));

CREATE TABLE orderHistory(
	login char(50),
	orderid integer,
	PRIMARY KEY(login, orderid),
	FOREIGN KEY(login) REFERENCES Users(login),
	FOREIGN KEY(orderid) REFERENCES Orders(orderid));

CREATE TABLE views(
	login char(50),
	itemName char(50),
	PRIMARY KEY(login, itemName),
	FOREIGN KEY(login) REFERENCES Users(login),
	FOREIGN KEY(itemName) REFERENCES Menu(itemName));
