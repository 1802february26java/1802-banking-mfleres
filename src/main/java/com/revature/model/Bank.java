package com.revature.model;

import java.util.HashMap;

import com.revature.exception.*;

public class Bank implements Banking {
	private HashMap<Integer,Double> vault = new HashMap<>();
	private int currentUser = 0;

	@Override
	public double getBalance() throws NoUserException{
		if(currentUser != 0) {
			System.out.println(vault.get(currentUser));
			return vault.get(currentUser);
		} else {
			throw new NoUserException();
		}
	}

	@Override
	public void login(int userID) throws UserNotFoundException{
		if(vault.containsKey(userID)) {
			System.out.println("Logged in as user ID " + userID);
			currentUser = userID;
		} else {
			throw new UserNotFoundException();
		}
	}

	@Override
	public void logout() {
		if(currentUser != 0 ) {
			System.out.println("Logged out");
		} else {
			System.out.println("No user is currently logged in");
		}
		currentUser = 0;
	}

	@Override
	public double withdraw(double amount) throws NoUserException{
		if(currentUser != 0) {
			vault.put(currentUser, vault.get(currentUser) - amount);
		}
		else
		{
			throw new NoUserException();
		}
		return 0;
	}

	@Override
	public void deposit(double amount) throws NoUserException{
		if(currentUser != 0) {
			vault.put(currentUser, vault.get(currentUser) + amount);
		}
		else
		{
			throw new NoUserException();
		}
	}
}
