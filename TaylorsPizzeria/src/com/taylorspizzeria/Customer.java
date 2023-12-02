package com.taylorspizzeria;

public class Customer 
{
	
	/*
	 * 
	 * Standard Java object class. 
	 *  
	 * This file can be modified to match your design, or you can keep it as-is.
	 * 
	 * */
	
	private int CustomerID;
	private String CustomerName;
	private String CustomerPhNum;
	private String Address;
	
	
	public Customer(int CustomerID, String CustomerName, String CustomerPhNum) {
		CustomerID = CustomerID;
		CustomerName = CustomerName;
		CustomerPhNum = CustomerPhNum;
	}

	public int getCustomerID() {
		return CustomerID;
	}

	public String getCustomerName() {
		return CustomerName;
	}


	public String getCustomerPhNum() {
		return CustomerPhNum;
	}

	public void setCustomerID(int CustomerID) {
		CustomerID = CustomerID;
	}

	public void setCustomerName(String CustomerName) {
		CustomerName = CustomerName;
	}


	public void setCustomerPhNum(String CustomerPhNum) {
		CustomerPhNum = CustomerPhNum;
	}
	

	
	@Override
	public String toString() {
		return "CustomerID=" + CustomerID + " | Name= " + CustomerName  + ", CustomerPhNum= " + CustomerPhNum;
	}
	
	
}
