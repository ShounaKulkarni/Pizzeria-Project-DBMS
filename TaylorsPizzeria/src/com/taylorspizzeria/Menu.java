package com.taylorspizzeria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/*
 * This file is where the front end magic happens.
 * 
 * You will have to write the functionality of each of these menu options' respective functions.
 * 
 * This file should need to access your DB at all, it should make calls to the DBNinja that will do all the connections.
 * 
 * You can add and remove functions as you see necessary. But you MUST have all 8 menu functions (9 including exit)
 * 
 * Simply removing menu functions because you don't know how to implement it will result in a major error penalty (akin to your program crashing)
 * 
 * Speaking of crashing. Your program shouldn't do it. Use exceptions, or if statements, or whatever it is you need to do to keep your program from breaking.
 * 
 * 
 */

public class Menu {
	static DBNinja obj1 = new DBNinja();
	private static Connection connection = null;
	public static void main(String[] args) throws SQLException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("\n\n Welcome to Shounak&Nathan's Pizzeria!");

		int menu_option = 0;

		// present a menu of options and take their selection
		PrintMenu();
		String option = reader.readLine();
		menu_option = Integer.parseInt(option);

		while (menu_option != 9) {
			switch (menu_option) {
			case 1:// enter order
				EnterOrder();
				break;
			case 2:// view customers
				viewCustomers();
				break;
			case 3:// enter customer
				EnterCustomer();
				break;
			case 4:// view order
				// open/closed/date
				ViewOrders();
				break;
			case 5:// mark order as complete
				MarkOrderAsComplete();
				break;
			case 6:// view inventory levels
				ViewInventoryLevels();
				break;
			case 7:// add to inventory
				AddInventory();
				break;
			case 8:// view reports
				PrintReports();
				break;
			}
			PrintMenu();
			option = reader.readLine();
			menu_option = Integer.parseInt(option);
		}

	}

	public static void PrintMenu() {
		System.out.println("\n\nPlease enter a menu option:");
		System.out.println("1. Enter a new order");
		System.out.println("2. View Customers ");
		System.out.println("3. Enter a new Customer ");
		System.out.println("4. View orders");
		System.out.println("5. Mark an order as completed");
		System.out.println("6. View Inventory Levels");
		System.out.println("7. Add Inventory");
		System.out.println("8. View Reports");
		System.out.println("9. Exit\n\n");
		System.out.println("Enter your option: ");
	}

	public static void PrintReportMenu() {
		System.out.println("\n\nPlease enter a menu option:");
		System.out.println("1. printToppingPopReport");
		System.out.println("2. printProfitByPizzaReport ");
		System.out.println("3. printProfitByOrderType");
		System.out.println("4. Exit\n\n");
		System.out.println("Enter your option: ");
	}

	// allow for a new order to be placed
	public static void EnterOrder() throws SQLException, IOException 
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		Scanner sc = new Scanner(System.in);
		/*
		 * EnterOrder should do the following:
		 * Ask if the order is for an existing customer -> If yes, select the customer. If no -> create the customer (as if the menu option 2 was selected).
		 * 
		 * Ask if the order is delivery, pickup, or dinein (ask for orderType specific information when needed)
		 * 
		 * Build the pizza (there's a function for this)
		 * 
		 * ask if more pizzas should be be created. if yes, go back to building your pizza. 
		 * 
		 * Apply order discounts as needed (including to the DB)
		 * 
		 * apply the pizza to the order (including to the DB)
		 * 
		 * return to menu
		 */
		int tablenumber = 0;
		String addr = "";
		double OrderPriceToCustomer = 0;
		double OrderCostToBusiness = 0;
		double discountOrderPriceToCustomer = 0;
		int breakcondn = 9;
		int orderID = 0;

		System.out.println("1. Add order for existing customer"
				+ "\n2. Add order for new customer");
		int customerStatus = Integer.parseInt(reader.readLine());
		if(customerStatus == 2){
			EnterCustomer();
		}
		viewCustomers();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String dburl = "jdbc:mysql://localhost:3306/Pizzeria";
			String dbuser = "root";
			String dbpassword = "SK@root1";

			connection = DriverManager.getConnection(dburl, dbuser, dbpassword);

			Statement statement = connection.createStatement();

			String query = "SELECT COUNT(OrderId) from ordertable";

			ResultSet result = statement.executeQuery(query);

			while (result.next()) {
				orderID = result.getInt(1) +1;
//				System.out.println(" OrderId "+orderID);
			}//while end

			connection.close();

		}//try end
		catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}// catch end
//		System.out.println("Enter the orderId:");
//		int orderID = Integer.parseInt(reader.readLine());
//		System.out.println(" OrderId "+orderID);
		System.out.println("Select the customer and enter customerID from above:");
		int placeOrderForCust = Integer.parseInt(reader.readLine());
		System.out.println("Enter the orderType:(dinein/pickup/delivery)");
		String orderType = reader.readLine();
		if(orderType.equalsIgnoreCase("dinein"))
		{
			System.out.println("Enter Table number:");
			tablenumber = reader.read();
		}
		else if(orderType.equalsIgnoreCase("delivery"))
		{
			System.out.println("Enter delivery address:");
			addr = reader.readLine();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String currentTimeStamp = sdf.format(timestamp);

		do {
			double[] prices=buildPizza(orderID);           //obtain the array  

			OrderPriceToCustomer =+ prices[0];
			OrderCostToBusiness =+ prices[1];

			System.out.println("Enter any other number to add more pizza"
					+ "\n Enter 0 to stop adding pizzas" );
			breakcondn = Integer.parseInt(sc.nextLine());
		}while(breakcondn !=0);

		System.out.println("Do you want to apply any discount on order?(yes:1/no:0)");
		int dinput = Integer.parseInt(sc.nextLine());

		if(dinput ==1) {
			OrderPriceToCustomer  = discount(OrderPriceToCustomer);
		}

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String dburl = "jdbc:mysql://localhost:3306/Pizzeria";
			String dbuser = "root";
			String dbpassword = "SK@root1";

			connection = DriverManager.getConnection(dburl, dbuser, dbpassword);

			Statement statement = connection.createStatement();

			//			String SQL = "INSERT INTO customer(CustomerID,CustomerName,CustomerPhNum) "
			//	                + "VALUES("+id +",'"+Name+"','"+phno+"')";
			String SQL = "INSERT INTO ordertable(OrderId,OrderCustomerId,OrderTimestamp,OrderPriceToCustomer,OrderCostToBusiness,OrderState,OrderType) "
					+ "VALUES('"+orderID+"','"+placeOrderForCust+"','"+currentTimeStamp+"','"+OrderPriceToCustomer+"','"+OrderCostToBusiness+"','"+0+"','"+orderType+"')";

			int result = statement.executeUpdate(SQL);

			// if result is greater than 0, it means values
			// has been added
			if (result > 0)
				System.out.println("Order successfully inserted");

			else
				System.out.println(
						"Order unsucessful insertion ");

			connection.close();

		}//try end
		catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}// catch end


		System.out.println("Finished adding order...Returning to menu...");
	}


	public static void viewCustomers()
	{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String dburl = "jdbc:mysql://localhost:3306/Pizzeria";
			String dbuser = "root";
			String dbpassword = "SK@root1";

			connection = DriverManager.getConnection(dburl, dbuser, dbpassword);

			Statement statement = connection.createStatement();

			String query = "select * from customer";

			ResultSet result = statement.executeQuery(query);

			while (result.next()) {
				System.out.println(
						result.getString(1) + "  " + result.getString(2) + "  "
								+ result.getString(3));
			}//while end
			connection.close();

		}//try end
		catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}// catch end

	}// END of viewCustomers()


	// Enter a new customer in the database
	public static void EnterCustomer() throws SQLException, IOException 
	{
		/*
		 * Ask what the name of the customer is. YOU MUST TELL ME (the grader) HOW TO FORMAT THE FIRST NAME, LAST NAME, AND PHONE NUMBER.
		 * If you ask for first and last name one at a time, tell me to insert First name <enter> Last Name (or separate them by different print statements)
		 * If you want them in the same line, tell me (First Name <space> Last Name).
		 * 
		 * same with phone number. If there's hyphens, tell me XXX-XXX-XXXX. For spaces, XXX XXX XXXX. For nothing XXXXXXXXXXXX.
		 * 
		 * I don't care what the format is as long as you tell me what it is, but if I have to guess what your input is I will not be a happy grader
		 * 
		 * Once you get the name and phone number (and anything else your design might have) add it to the DB
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		Scanner sc = new Scanner(System.in);

		//		System.out.println("Please enter customer id: ");
		//		String id = sc.next();

		System.out.println("please Enter the customer name(First name <Space> Last name): ");
		String Name = reader.readLine();

		System.out.println("Please enter customer phone number(xxx-xxx-xxxx): ");
		String phno = reader.readLine();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String dburl = "jdbc:mysql://localhost:3306/Pizzeria";
			String dbuser = "root";
			String dbpassword = "SK@root1";

			connection = DriverManager.getConnection(dburl, dbuser, dbpassword);

			Statement statement = connection.createStatement();

			//			String SQL = "INSERT INTO customer(CustomerID,CustomerName,CustomerPhNum) "
			//	                + "VALUES("+id +",'"+Name+"','"+phno+"')";
			String SQL = "INSERT INTO customer(CustomerName,CustomerPhNum) "
					+ "VALUES('"+Name+"','"+phno+"')";
			//			String SQL1 = "INSERT INTO customer(CustomerID,CustomerName,CustomerPhNum) "
			//	                + "VALUES(5,'matt',864-207-2150)";
			int result = statement.executeUpdate(SQL);

			// if result is greater than 0, it means values
			// has been added
			if (result > 0)
				System.out.println("customer successfully inserted");

			else
				System.out.println(
						"Customer unsucessful insertion ");

			connection.close();

		}//try end
		catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}// catch end

	}// END of EnterCustomer()

	// View any orders that are not marked as completed
	public static void ViewOrders() throws SQLException, IOException 
	{
		/*
		 * This should be subdivided into two options: print all orders (using simplified view) and print all orders (using simplified view) since a specific date.
		 * 
		 * Once you print the orders (using either sub option) you should then ask which order I want to see in detail
		 * 
		 * When I enter the order, print out all the information about that order, not just the simplified view.
		 * 
		 */
		// ALL ORDERS:
		Scanner sc = new Scanner(System.in);
		int input = 0;
		while(input != 3) {

			System.out.println("\nWould you like to:");
			System.out.println("1. View all orders: ");
			System.out.println("2. orders from specific date:");
			System.out.println("3. Go back to main menu:\n");
			input = sc.nextInt();

			if(input ==1) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");

					String dburl = "jdbc:mysql://localhost:3306/Pizzeria";
					String dbuser = "root";
					String dbpassword = "SK@root1";

					connection = DriverManager.getConnection(dburl, dbuser, dbpassword);

					Statement statement = connection.createStatement();

					String query = "select OrderId,OrderTimestamp,OrderState,OrderType from ordertable ORDER BY OrderTimestamp";

					ResultSet result = statement.executeQuery(query);

					while (result.next()) {
						System.out.println(" OrderID: "+
								result.getString(1) + " |Order date: " + result.getString(2) + "  |Order State: "
								+ result.getString(3)+ " |OrderType " + result.getString(4)+ "  ");
					}//while end
					connection.close();

				}//try end
				catch (ClassNotFoundException | SQLException e) {

					e.printStackTrace();
				}// catch end
				System.out.println("\nSelect orderID whose details you would like to see:");
				int detailOrderId = sc.nextInt();
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");

					String dburl = "jdbc:mysql://localhost:3306/Pizzeria";
					String dbuser = "root";
					String dbpassword = "SK@root1";

					connection = DriverManager.getConnection(dburl, dbuser, dbpassword);

					Statement statement = connection.createStatement();

					String query = "select * from ordertable where OrderId = "+ detailOrderId +";";

					ResultSet result = statement.executeQuery(query);

					while (result.next()) {
						System.out.println("OrderId: "+
								result.getString(1) + " |CustomerID: " + result.getString(2) + " |Orderdate: "
								+ result.getString(3)+ " |PriceToCustomer " + result.getString(4)+ " |CostToBussines  "
								+ result.getString(5)+ " |OrderStatus: " + result.getString(6)+ " |OrderType:  "
								+ result.getString(7));
					}//while end
					connection.close();

				}//try end
				catch (ClassNotFoundException | SQLException e) {

					e.printStackTrace();
				}// catch end



			}//End of if==1

			else if(input == 2) {
				System.out.println("what is the date you want restrict by:(Format: YYYY-MM-DD HH:MI:SS)");
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				String daterestriction = reader.readLine();	
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");

					String dburl = "jdbc:mysql://localhost:3306/Pizzeria";
					String dbuser = "root";
					String dbpassword = "SK@root1";

					connection = DriverManager.getConnection(dburl, dbuser, dbpassword);

					Statement statement = connection.createStatement();

					String query = "select OrderId,OrderTimestamp,OrderState,OrderType from ordertable where OrderTimestamp BETWEEN '"+ daterestriction +"' AND '2230-12-31 00:00:00' ORDER BY OrderTimestamp;";

					ResultSet result = statement.executeQuery(query);

					while (result.next()) {
						System.out.println(" OrderID: "+
								result.getString(1) + " |Order date: " + result.getString(2) + "  |Order State: "
								+ result.getString(3)+ " |OrderType " + result.getString(4)+ "  ");
					}//while end
					connection.close();

				}//try end
				catch (ClassNotFoundException | SQLException e) {

					e.printStackTrace();
				}// catch end
				System.out.println("\nSelect orderID whose details you would like to see:");
				int detailOrderId = sc.nextInt();
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");

					String dburl = "jdbc:mysql://localhost:3306/Pizzeria";
					String dbuser = "root";
					String dbpassword = "SK@root1";

					connection = DriverManager.getConnection(dburl, dbuser, dbpassword);

					Statement statement = connection.createStatement();

					String query = "select * from ordertable where OrderId = "+ detailOrderId +";";

					ResultSet result = statement.executeQuery(query);

					while (result.next()) {
						System.out.println("OrderId: "+
								result.getString(1) + " |CustomerID: " + result.getString(2) + " |Orderdate: "
								+ result.getString(3)+ " |PriceToCustomer " + result.getString(4)+ " |CostToBussines  "
								+ result.getString(5)+ " |OrderStatus: " + result.getString(6)+ " |OrderType:  "
								+ result.getString(7));
					}//while end
					connection.close();

				}//try end
				catch (ClassNotFoundException | SQLException e) {

					e.printStackTrace();
				}// catch end
			}

		}// END of while
	}// END of viewOrders()



	// When an order is completed, we need to make sure it is marked as complete
	public static void MarkOrderAsComplete() throws SQLException, IOException 
	{
		/*All orders that are created through java (part 3, not the 7 orders from part 2) should start as incomplete
		 * 
		 * When this function is called, you should print all of the orders marked as complete 
		 * and allow the user to choose which of the incomplete orders they wish to mark as complete
		 * 
		 */
		System.out.println("All orders that are in Incomplete statement: ");


		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String dburl = "jdbc:mysql://localhost:3306/Pizzeria";
			String dbuser = "root";
			String dbpassword = "SK@root1";

			connection = DriverManager.getConnection(dburl, dbuser, dbpassword);

			Statement statement = connection.createStatement();

			String query = "select OrderId,OrderTimestamp,OrderState,OrderType from ordertable where OrderState = 0 ORDER BY OrderTimestamp";

			ResultSet result = statement.executeQuery(query);

			while (result.next()) {
				System.out.println(" OrderID: "+
						result.getString(1) + " |Order date: " + result.getString(2) + "  |Order State: "
						+ result.getString(3)+ " |OrderType " + result.getString(4)+ "  ");
			}//while end

			System.out.println(" \n Enter orderId to mark order As comlplete:");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String OrderIdToMarkCompleted = reader.readLine();

			String SQL = "UPDATE ordertable\n" + 
					"SET OrderState = 1\n" + 
					"WHERE OrderId = "+ OrderIdToMarkCompleted +";";

			int result2 = statement.executeUpdate(SQL);

			// if result is greater than 0, it means values
			// has been added
			if (result2 > 0)
				System.out.println("Order Marked Completed successfully ");

			else
				System.out.println(
						"Order Completion unsucessful ");


			connection.close();

		}//try end
		catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}// catch end


	}

	// See the list of inventory and it's current level
	public static void ViewInventoryLevels() throws SQLException, IOException 
	{
		//print the inventory. I am really just concerned with the ID, the name, and the current inventory
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String dburl = "jdbc:mysql://localhost:3306/Pizzeria";
			String dbuser = "root";
			String dbpassword = "SK@root1";

			connection = DriverManager.getConnection(dburl, dbuser, dbpassword);

			Statement statement = connection.createStatement();

			String query = "select ToppingId,ToppingName,ToppingCurrentInvLvl from Topping ORDER BY ToppingName ASC;";

			ResultSet result = statement.executeQuery(query);

			while (result.next()) {
				System.out.println("ToppingID: "+
						result.getString(1) + " |ToppingName: " + result.getString(2) + " |ToppingQuantity: "
						+ result.getString(3));
			}//while end
			connection.close();

		}//try end
		catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}// catch end

	}

	// Select an inventory item and add more to the inventory level to re-stock the
	// inventory
	public static void AddInventory() throws SQLException, IOException 
	{
		/*
		 * This should print the current inventory and then ask the user which topping they want to add more to and how much to add
		 */
		int condition = 1;
		while(condition == 1) {
			System.out.println("Current Inventory level: ");
			ViewInventoryLevels();
			System.out.println("please select which topping you want to add to inventory: ");
			Scanner sc = new Scanner(System.in);
			int ToppingId = sc.nextInt();
			System.out.println("Enter the amount of topping: ");
			int toppingAmount = sc.nextInt();

			try {
				Class.forName("com.mysql.cj.jdbc.Driver");

				String dburl = "jdbc:mysql://localhost:3306/Pizzeria";
				String dbuser = "root";
				String dbpassword = "SK@root1";

				connection = DriverManager.getConnection(dburl, dbuser, dbpassword);

				Statement statement = connection.createStatement();

				String SQL = "UPDATE Topping SET ToppingCurrentInvLvl = ToppingCurrentInvLvl + "+ toppingAmount +
						" WHERE ToppingId = '"+ToppingId +"';  ";

				int result = statement.executeUpdate(SQL);

				// if result is greater than 0, it means values
				// has been added
				if (result > 0)
					System.out.println("Toppings inventory updated successfully ");

				else
					System.out.println(
							"Toppings inventory update unsucessful insertion ");

				connection.close();

			}//try end
			catch (ClassNotFoundException | SQLException e) {

				e.printStackTrace();
			}// catch end


			System.out.println("please enter 1 to add more inventory or enter 0 to exit");
			condition = sc.nextInt();
		}

	}

	// A function that builds a pizza. Used in our add new order function
	public static double[] buildPizza(int orderID) throws SQLException, IOException 
	{

		/*
		 * This is a helper function for first menu option.
		 * 
		 * It should ask which size pizza the user wants and the crustType.
		 * 
		 * Once the pizza is created, it should be added to the DB.
		 * 
		 * We also need to add toppings to the pizza. (Which means we not only need to add toppings here, but also our bridge table)
		 * 
		 * We then need to add pizza discounts (again, to here and to the database)
		 * 
		 * Once the discounts are added, we can return the pizza
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		Scanner sc = new Scanner(System.in);
		int pizzaId = 1;
		double basePizzaPriceToCustomer = 0;
		double basePizzaCostToBusiness = 0;
		double  discountPizzaPriceToCustomer = 0;
		Pizza ret = null;
		System.out.println("Let's build a pizza!!:");
		System.out.println("Enter the size of pizza (small/medium/large/x-large):");
		String pSize = reader.readLine();
		System.out.println("Enter the crust of pizza (Thin/pan/Original/Gluten-Free):");
		String pCrust = reader.readLine();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String dburl = "jdbc:mysql://localhost:3306/Pizzeria";
			String dbuser = "root";
			String dbpassword = "SK@root1";

			connection = DriverManager.getConnection(dburl, dbuser, dbpassword);

			Statement statement = connection.createStatement();

			String query = "select BasePricePrice,BasePriceCost from baseprice where BasePriceCrust = '" + pCrust
					+ "' and BasePriceSize = '" + pSize + "';";

			ResultSet result = statement.executeQuery(query);

			while (result.next()) {
				basePizzaPriceToCustomer = result.getDouble(1);
				basePizzaCostToBusiness = result.getDouble(2);

			}//while end
			//			System.out.println("Prices before adding toppings:");
			//			System.out.println(basePizzaPriceToCustomer);
			//			System.out.println(basePizzaCostToBusiness);
			int exitCodn = 99;
			int arrayi = 0;
			int toppingArray[];    
			toppingArray = new int[5];
			System.out.println("Lets add toppings!");

			ViewInventoryLevels();
			System.out.println("\nchoose a toppingId: ");
			int faddtoppingId = Integer.parseInt(sc.nextLine());
			toppingArray[arrayi]= faddtoppingId;
			arrayi++;
			System.out.println("Enter 1 for additional toppings or \n any other number if you dont want additional topics");
			int addinput = Integer.parseInt(sc.nextLine());
			if(addinput == 1)
			{
				do {
					ViewInventoryLevels();
					System.out.println("\nchoose a toppingId: ");
					int eAddtoppingId = Integer.parseInt(sc.nextLine());
					toppingArray[arrayi]= eAddtoppingId;
					arrayi++;
					System.out.println("Enter 0 if done with adding toppings other wise enter any other number: ");
					exitCodn = Integer.parseInt(sc.nextLine());
				} while (exitCodn !=0);
			}
			//			System.out.println(Arrays.toString(toppingArray));
			//			System.out.println(arrayi);
			for (int i = 0; i < arrayi; i++) {


				String SQL = "UPDATE Topping SET ToppingCurrentInvLvl = ToppingCurrentInvLvl - 1"
						+" WHERE ToppingId = '"+toppingArray[i] +"';  ";

				int resultToppingSubstraction = statement.executeUpdate(SQL);

				// if result is greater than 0, it means values has been added
				if (resultToppingSubstraction > 0)
					System.out.println("Toppings inventory updated successfully ");
				else
					System.out.println(
							"Toppings inventory update unsucessful insertion 111");
			}
			for (int i = 0; i < arrayi; i++) {

				String priceQuery = "select ToppingCustomerPrice,ToppingBusinessCost from Topping"
						+ " WHERE ToppingId = '"+toppingArray[i] +"';";

				ResultSet resultBill = statement.executeQuery(query);

				while (resultBill.next()) {
					double pp = resultBill.getDouble(1);
					basePizzaPriceToCustomer = basePizzaPriceToCustomer+pp;
					double cc = resultBill.getDouble(2);
					basePizzaCostToBusiness = basePizzaCostToBusiness+cc;

				}//while end
			}
			//			System.out.println("pizza bill after toppings");
			//			System.out.println(basePizzaPriceToCustomer);
			//			System.out.println(basePizzaCostToBusiness);


			discountPizzaPriceToCustomer = discount(basePizzaPriceToCustomer);
			// Add pizza
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String currentTimeStamp = sdf.format(timestamp);
			String SQLInsertPizza = "INSERT INTO pizza(PizzaOrderId,PizzaCrust,PizzaSize,PizzaPriceToCustomer,PizzaCostToBusiness,PizzaState,PizzaOrderTime) "
					+ "VALUES('"+orderID+"','"+pCrust+"','"+pSize+"','"+discountPizzaPriceToCustomer+"','"+basePizzaCostToBusiness+"','"+1+"','"+currentTimeStamp+"')";
			int resultInsertPizza = statement.executeUpdate(SQLInsertPizza,Statement.RETURN_GENERATED_KEYS);

			// if result is greater than 0, it means values
			// has been added
			if (resultInsertPizza > 0)
				System.out.println("Pizza added successfully ");

			else
				System.out.println(
						"Pizza unsucessful insertion ");
			ResultSet rs = statement.getGeneratedKeys();

			while (rs.next()) {
				pizzaId = rs.getInt(1);
				System.out.println("pizzaId"+pizzaId);
			}

			// adding bridge table
			int extratoppings = arrayi -1;
			for (int i = 0; i < arrayi; i++) {


				String resultToppingBridgeSQL =  "INSERT INTO pizzatopping(PizzaId,ToppingId,ExtraToppings) "
						+ "VALUES('"+ pizzaId +"','"+toppingArray[i]+"','"+extratoppings+"')";

				int resultToppingBridge = statement.executeUpdate(resultToppingBridgeSQL);

				// if result is greater than 0, it means values has been added
				if (resultToppingBridge > 0)
					System.out.println("Toppings inventory updated successfully ");
				else
					System.out.println(
							"Toppings inventory update unsucessful insertion 111");
			}
			connection.close();
		}//try end
		catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}// catch end

		double[] prices = new double [2];    // Creating an array of 2 elements  to return prices
		prices[0]=discountPizzaPriceToCustomer;  
		prices [1]=basePizzaCostToBusiness;  
		return prices;
	}
	public static double discount(double basePizzaPriceToCustomer ) {
		Scanner sc = new Scanner(System.in);
		double discountedPizzaPriceToCustomer = 0;
		int dCondition = 1;
		do {

			try {
				Class.forName("com.mysql.cj.jdbc.Driver");

				String dburl = "jdbc:mysql://localhost:3306/Pizzeria";
				String dbuser = "root";
				String dbpassword = "SK@root1";

				connection = DriverManager.getConnection(dburl, dbuser, dbpassword);

				Statement statement = connection.createStatement();

				String query = "select * from discount;";

				ResultSet result = statement.executeQuery(query);

				while (result.next()) {
					System.out.println("DiscountId: "+
							result.getString(1) + " |DiscountName: " + result.getString(2) + " |IsFlatDiscount:  "
							+ result.getString(3)+ " |DiscountAmount: " + result.getString(4)
							+ " |DiscountPercent: " + result.getString(5));
				}//while end
				System.out.println("Select DiscountId to apply on:");
				int DiscountId = Integer.parseInt(sc.nextLine());
				String checkflatdiscount = "select IsFlatDiscount,DiscountAmount,DiscountPercent"
						+ " from discount where DiscountId = "+DiscountId+";";

				ResultSet result2 = statement.executeQuery(checkflatdiscount);
				int 	IsFlatDiscount = 0;
				double 	discountamount = 0;
				double 	discountpercent = 0;
				while (result2.next()) {
					IsFlatDiscount =result2.getInt(1);
					discountamount =result2.getDouble(2);
					discountpercent =result2.getDouble(3);
				}//while end
				//				System.out.println("1 "+IsFlatDiscount);
				//				System.out.println("2 "+discountamount);
				//				System.out.println("3 "+discountpercent);
				if(IsFlatDiscount == 0) {
					discountedPizzaPriceToCustomer = ((basePizzaPriceToCustomer * (100 - discountpercent))/100);
				}
				else if (IsFlatDiscount == 1) {
					discountedPizzaPriceToCustomer = basePizzaPriceToCustomer - discountamount;
				}
				System.out.println(discountedPizzaPriceToCustomer);
				connection.close();

			}//try end
			catch (ClassNotFoundException | SQLException e) {

				e.printStackTrace();
			}// catch end
			System.out.println("Do you wish to apply more discount:(1/0)");
			dCondition = Integer.parseInt(sc.nextLine());
		} while (dCondition == 1);

		return discountedPizzaPriceToCustomer;
	}

	private static int getTopIndexFromList(int TopID, ArrayList<Topping> tops)
	{
		/*
		 * This is a helper function I used to get a topping index from a list of toppings
		 * It's very possible you never need a function like this
		 * 
		 */
		int ret = -1;

		return ret;
	}


	public static void PrintReports() throws SQLException, NumberFormatException, IOException
	{
		/*
		 * This function calls the DBNinja functions to print the three reports.
		 * 
		 * You should ask the user which report to print
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Here are all the reports!");

		int menu_option = 0;

		// present a menu of options and take their selection
		PrintReportMenu();
		String option = reader.readLine();
		menu_option = Integer.parseInt(option);

		while (menu_option != 4) {
			switch (menu_option) {
			case 1:// ToppingPopularity
				obj1.printToppingPopReport();
				break;
			case 2:// ProfitByPizza
				obj1.printProfitByPizzaReport();
				break;
			case 3:// ProfitByOrderType
				obj1.printProfitByOrderType();;
				break;
			} //END of PrintReports() Method's switch

			PrintReportMenu();
			option = reader.readLine();
			menu_option = Integer.parseInt(option);

		}// END of PrintReports() Method's while

	}// END of PrintReports() Method

}


