use Pizzeria;

SELECT * FROM ToppingPopularity;
SELECT * FROM ProfitByPizza;
SELECT * FROM ProfitByOrderType;


create view ToppingPopularity as
select  topping.ToppingName as Topping ,
count(topping.ToppingName) + sum(pizzatopping.ExtraToppings) as  ToppingCount from pizzatopping
right join topping  on pizzatopping.ToppingId=topping.ToppingId
group by topping.ToppingName
order by ToppingCount desc;


create view ProfitByPizza as
select  baseprice.BasePriceSize as 'Pizza Size',
baseprice.BasePriceCrust as 'Pizza Crust',
sum(pizza.PizzaPriceToCustomer-pizza.PizzaCostToBusiness) as Profit ,
DATE_FORMAT(max(pizza.PizzaOrderTime), '%M %e %Y') as LastOrderDate from baseprice
right join pizza  on baseprice.BasePriceSize=pizza.PizzaSize and baseprice.BasePriceCrust=pizza.PizzaCrust
group by baseprice.BasePriceSize,baseprice.BasePriceCrust
order by profit desc;


create view ProfitByOrderType as
select  OrderType as CustomerType,
DATE_FORMAT(OrderTimestamp,'%Y %M') as OrderMonth,
OrderPriceToCustomer as TotalOrderPrice ,
OrderCostToBusiness as TotalOrderCost ,
(OrderPriceToCustomer-OrderCostToBusiness) as Profit
from ordertable
group by CustomerType,orderMonth
union
select ' ','Final Total' as OrderMonth,
sum(OrderPriceToCustomer) as TotalOrderPrice ,
sum(OrderCostToBusiness) as TotalOrderCost,
sum(OrderPriceToCustomer-OrderCostToBusiness) as profit
from ordertable;