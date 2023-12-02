use Pizzeria;

-- -- Inserting into Topping table

 INSERT INTO `Topping`
(`ToppingId`,
`ToppingName`, 
`ToppingCustomerPrice`, 
`ToppingBusinessCost`, 
`ToppingMinInvLvl`, 
`ToppingCurrentInvLvl`, 
`ToppingSmallAmt`, 
`ToppingMedAmt`,
`ToppingLargeAmt`,
`ToppingXLAmt`)
VALUES(1,'Pepperoni', 1.25, 0.2, 100, 100, 2, 2.75, 3.5, 4.5),
 (2,'Sausage', 1.25, 0.15, 100, 100, 2.5, 3, 3.5, 4.25),
 (3,'Ham', 1.5, 0.15, 78, 78, 2, 2.5, 3.25, 4),
 (4,'Chicken', 1.75, 0.25, 56, 56, 1.5, 2, 2.25, 3),
 (5,'Green Pepper', 0.5, 0.02, 79, 79, 1, 1.5, 2, 2.5),
 (6,'Onion', 0.5, 0.02, 85, 85, 1, 1.5, 2, 2.75),
 (7,'Roma Tomato', 0.75, 0.03, 86, 86, 2, 3, 3.5, 4.5),
 (8,'Mushrooms', 0.75, 0.1, 52, 52, 1.5, 2, 2.5, 3),
 (9,'Black Olives', 0.6, 0.1, 39, 39, 0.75, 1, 1.5, 2),
 (10,'Pineapple', 1, 0.25, 15, 15, 1, 1.25, 1.75, 2),
 (11,'Jalapenos', 0.5, 0.05, 64, 64, 0.5, 0.75, 1.25, 1.75),
 (12,'Banana Peppers', 0.5, 0.05, 36, 36, 0.6, 1, 1.3, 1.75),
 (13,'Regular Cheese', 1.5, 0.12, 250, 250, 2, 3.5, 5, 7),
 (14,'Four Cheese Blend', 2, 0.15, 150, 150, 2, 3.5, 5, 7),
 (15,'Feta Cheese', 2, 0.18, 75, 75, 1.75, 3, 4, 5.5),
 (16,'Goat Cheese', 2, 0.2, 54, 54, 1.6, 2.75, 4, 5.5),
 (17,'Bacon', 1.5, 0.25, 89, 89, 1, 1.5, 2, 3);



-- -- Inserting records in Discount table

INSERT INTO `discount`
(`DiscountName`, `IsFlatDiscount` , `DiscountAmount`, `DiscountPercent`)
VALUES ('Employee',false, 15, 0),
('Lunch Special Medium',true, 0, 1.00),
('Lunch Special Large' ,true,0, 2.00),
('Specialty Pizza',true, 0, 1.50),
('Gameday Special',false, 20, 0);



-- -- inserting records into BasePrice Table:

INSERT INTO `basePrice`
(`BasePriceSize`, `BasePriceCrust`, `BasePricePrice`, `BasePriceCost`)
VALUES ("small" ,"Thin", 3, 0.5 ),
("small", "Original", 3, 0.75),
("small", "Pan", 3.5, 1),
('small' ,'Gluten-Free' ,4 ,2),
('medium', 'Thin', 5, 1),
('medium', 'Original', 5, 1.5),
('medium', 'Pan', 6, 2.25),
('medium', 'Gluten-Free', 6.25, 3),
('large', 'Thin', 8 ,1.25),
('large', 'Original', 8, 2),
('large' ,'Pan', 9 ,3),
('large', 'Gluten-Free', 9.5 ,4),
('x-large', 'Thin', 10, 2),
('x-large', 'Original', 10, 3),
('x-large', 'Pan', 11.5, 4.5),
('x-large', 'Gluten-Free', 12.5, 6);

-- inserting records into customer Table:
insert into customer(CustomerId,CustomerName,CustomerPhNum)
values
(1,'Andrew Wilkes-Krier','864-254-5861'),
(2,'Matt Engers','864-474-9953'),
(3,'Frank Turner','864-232-8944'),
(4,'Milo Auckerman ','864-878-5679.');

-- inserting records into discountPizza Table:

insert into ordertable(
OrderId,OrderCustomerId,
OrderTimestamp,
OrderPriceToCustomer,OrderCostToBusiness,
OrderState,OrderType)
values
(1,NULL,'2022-03-05 12:03:00',13.5,3.68,true,'dinein'),
(2,NULL,'2022-04-03 12:05:00',17.35,4.63,true,'dinein'),
(3,1,'2022-03-03 21:30:00',64.5,19.8,true,'pickup'),
(4,1,'2022-04-20 19:11:00',45.5,19.8,true,'delivery'),
(5,2,'2022-03-02 17:30:00',16.85,7.85,true,'pickup'),
(6,3,'2022-03-02 18:17:00',13.25,3.20,true,'delivery'),
(7,4,'2022-04-13 20:32:00',24,6.3,true,'delivery');

-- inserting records into discountOrder Table:
insert into discountorder (OrderId,DiscountId)
values
(1,3),
(4,5),
(7,1);


-- inserting records into pizza Table:
insert into pizza(PizzaId,PizzaOrderId,
PizzaSize,PizzaCrust,
PizzaState,PizzaOrderTime,
PizzaCostToBusiness,PizzaPriceToCustomer)
values
(1,1,'large','thin',TRUE,'2022-03-05 12:03:00',3.68,13.5),
(2,2,'medium','pan',TRUE,'2022-04-03 12:05:00',3.23,10.6),
(3,2,'small','original',TRUE,'2022-04-03 12:05:00',1.4,6.75),
(4,3,'large','original',TRUE,'2022-03-03 21:30:00',3.3,10.75),
(5,3,'large','original',TRUE,'2022-03-03 21:30:00',3.3,10.75),
(6,3,'large','original',TRUE,'2022-03-03 21:30:00',3.3,10.75),
(7,3,'large','original',TRUE,'2022-03-03 21:30:00',3.3,10.75),
(8,3,'large','original',TRUE,'2022-03-03 21:30:00',3.3,10.75),
(9,3,'large','original',TRUE,'2022-03-03 21:30:00',3.3,10.75),
(10,4,'x-large','original',TRUE,'2022-04-20 19:11:00',5.59,14.5),
(11,4,'x-large','original',TRUE,'2022-04-20 19:11:00',5.59,17),
(12,4,'x-large','original',TRUE,'2022-04-20 19:11:00',5.68,14),
(13,5,'x-large','gluten free',TRUE,'2022-03-02 17:30:00',7.85,16.85),
(14,6,'large','thin',TRUE,'2022-03-02 18:17:00',3.2,13.25),
(15,7,'large','thin',TRUE,'2022-03-02 18:17:00',3.75,12),
(16,7,'large','thin',TRUE,'2022-03-02 18:17:00',2.55,12);


-- inserting records into discountPizza Table:
insert into discountpizza(PizzaId,DiscountId)
values
(2,2),
(2,4),
(11,4),
(13,4);

-- inserting records into pizzaTopping Table:
insert into pizzaTopping(PizzaId,ToppingId,ExtraToppings)
values
(1,13,true),
(1,1,false),
(1,2,false),
(2,15,false),
(2,9,false),
(2,7,false),
(2,8,false),
(2,12,false),
(3,13,false),
(3,4,false),
(3,12,false),
(4,13,false),
(4,1,false),
(5,13,false),
(5,1,false),
(6,13,false),
(6,1,false),
(7,13,false),
(7,1,false),
(8,13,false),
(8,1,false),
(9,13,false),
(9,1,false),
(10,1,false),
(10,2,false),
(10,14,false),
(11,3,true),
(11,10,true),
(11,14,false),
(12,11,false),
(12,17,false),
(12,14,false),
(13,5,false),
(13,6,false),
(13,7,false),
(13,8,false),
(13,9,false),
(13,16,false),
(14,4,false),
(14,5,false),
(14,6,false),
(14,8,false),
(14,14,true),
(15,14,true),
(16,13,false),
(16,1,true);

-- inserting records into dinein Table:

insert into dinein(OrderId,TableNumber)
values
(1,14),
(2,4);



-- inserting records into delivery Table:

insert into delivery (OrderId,deliveryCustomerId,CustomerAddr)
values
(4,1,'115 Party Blvd, Anderson SC 29621'),
(6,3,'6745 Wessex St, Anderson SC 29621'	),
(7,4,'8879 Suburban Home, Anderson SC 29621');


-- inserting records into pickup Table:

insert into pickup (OrderId,deliveryCustomerId,CustomerName,CustomerPhonenumber)
values
(3,1,'Andrew Wilkes-Krier',864-254-5861),
(5,4,'Matt Engers',864-474-9953);