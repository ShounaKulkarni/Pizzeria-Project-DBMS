DROP SCHEMA IF EXISTS Pizzeria;
CREATE SCHEMA Pizzeria;
use Pizzeria;
BEGIN;



-- customer

CREATE TABLE customer (
	CustomerID int NOT NULL ,
	CustomerName varchar(25),
	CustomerPhNum varchar(25),
	PRIMARY KEY (CustomerID)
) ;
SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE customer MODIFY COLUMN CustomerID int auto_increment;



-- Order

CREATE TABLE orderTable (
	OrderId int(15) NOT NULL UNIQUE ,
	OrderCustomerId int(15) ,
    OrderTimestamp datetime NOT NULL,
    OrderPriceToCustomer decimal(5,2) NOT NULL,
    OrderCostToBusiness decimal(5,2) NOT NULL,
    OrderState boolean,
    OrderType varchar(20),
    PRIMARY KEY (OrderId),
    FOREIGN KEY (OrderCustomerId) REFERENCES customer(CustomerId)
);
SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE orderTable MODIFY COLUMN OrderId int auto_increment;


-- dineIn

CREATE TABLE dinein (
	OrderId int NOT NULL,
	TableNumber int NOT NULL ,
    PRIMARY KEY (TableNumber),
	FOREIGN KEY (OrderId) REFERENCES orderTable(OrderId)
);



-- delivery

CREATE TABLE delivery (
	OrderId int NOT NULL,
    deliveryCustomerId int NOT NULL,
	CustomerAddr varchar(50) NOT NULL ,
    PRIMARY KEY (OrderId),
	FOREIGN KEY (OrderId) REFERENCES orderTable(OrderId),
    FOREIGN KEY (deliveryCustomerId) REFERENCES customer(CustomerID)
);



-- pickup

CREATE TABLE pickup (
	OrderId int NOT NULL,
    deliveryCustomerId int NOT NULL,
	CustomerName varchar(25) NOT NULL ,
    CustomerPhonenumber varchar(15) NOT NULL,
    PRIMARY KEY (OrderId),
	FOREIGN KEY (OrderId) REFERENCES orderTable(OrderId),
    FOREIGN KEY (deliveryCustomerId) REFERENCES customer(CustomerID)
);


-- pizza

CREATE TABLE pizza (
	PizzaId int(15) NOT NULL UNIQUE ,
	PizzaOrderId int(15) NOT NULL ,
    PizzaCrust varchar(12) NOT NULL,
    PizzaSize varchar(7) NOT NULL,
    PizzaPriceToCustomer decimal(6,2) NOT NULL,
    PizzaCostToBusiness decimal(6,2) NOT NULL,
    PizzaState boolean,
    PRIMARY KEY (PizzaId),
    FOREIGN KEY (PizzaOrderId) REFERENCES orderTable(OrderId)
);

SET FOREIGN_KEY_CHECKS = 0;
SET GLOBAL FOREIGN_KEY_CHECKS=0;

-- ALTER TABLE pizza
-- DROP FOREIGN KEY orderTable;

-- ALTER TABLE pizza
-- ADD PizzaOrderTime datetime;

ALTER TABLE pizza MODIFY COLUMN PizzaId int auto_increment;



-- discount

CREATE TABLE discount (
  `DiscountId` int NOT NULL AUTO_INCREMENT,
  `DiscountName` varchar(20) NOT NULL,
  `IsFlatDiscount` boolean NOT NULL,
  `DiscountAmount` decimal(10,2) DEFAULT NULL,
  `DiscountPercent` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`DiscountId`)
) ;



-- discountOrder

CREATE TABLE discountOrder (
	OrderId int NOT NULL,
	DiscountId int NOT NULL ,
    PRIMARY KEY (OrderId, DiscountId),
    CONSTRAINT `orderdiscount_ibfk_1` FOREIGN KEY (OrderId) REFERENCES orderTable(OrderId),
    CONSTRAINT `orderdiscount_ibfk_2` FOREIGN KEY (DiscountId) REFERENCES discount (DiscountId)
);



-- discountPizza

CREATE TABLE discountPizza (
	PizzaId int NOT NULL,
	DiscountId int NOT NULL,
    PRIMARY KEY (PizzaId, DiscountId),
    FOREIGN KEY (PizzaId) REFERENCES pizza (PizzaId),
    FOREIGN KEY (DiscountId) REFERENCES discount (DiscountId)
);



-- pizzaTopping

CREATE TABLE pizzaTopping (
	PizzaId int(15) NOT NULL ,
	ToppingId int(15) NOT NULL ,
    ExtraToppings boolean,
    FOREIGN KEY (PizzaId) REFERENCES pizza(PizzaId)
);



-- topping

CREATE TABLE topping (
	ToppingId int NOT NULL,
	ToppingName varchar(25) NOT NULL,
	ToppingCustomerPrice decimal(4,2) NOT NULL,
	ToppingBusinessCost decimal(4,2) NOT NULL,
	ToppingMinInvLvl int NOT NULL,
	ToppingCurrentInvLvl int NOT NULL,
	ToppingSmallAmt decimal(4,2) NOT NULL,
	ToppingMedAmt decimal(4,2) NOT NULL,
	ToppingLargeAmt decimal(4,2) NOT NULL,
	ToppingXLAmt decimal(4,2) NOT NULL,
	PRIMARY KEY (ToppingId)
) ;



-- BASE PRICES

CREATE TABLE basePrice (
    BasePriceSize varchar(25),
    BasePriceCrust varchar(25),
    BasePricePrice decimal(4,2),
    BasePriceCost decimal(4,2),
    primary key(BasePriceSize,BasePriceCrust)
);
