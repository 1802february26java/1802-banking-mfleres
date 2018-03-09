package com.revature.model;

import java.util.List;

import com.revature.exception.*;

public interface Banking {
	public double getBalance();
	
	/**
	 * Throws InvalidLoginException
	 * 
	 */
	public void login(int userID, String password);
	
	public void logout();
	public double withdraw(double amount);
	public double deposit(double amount);
	public int register(String password);
	public void register(int id, String password);
	public void deregister(String password);
	public int getCurrentUser();
	public List<Transaction> getTransactionHistory();
}
