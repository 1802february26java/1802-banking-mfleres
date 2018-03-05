package com.revature.model;

import java.util.Map;

import com.revature.exception.*;

public interface Banking {
	public double getBalance();
	public void login(int userID);
	public void logout();
	public double withdraw(double amount) throws NoUserException;
	public void deposit(double amount);
	public int register();
}
