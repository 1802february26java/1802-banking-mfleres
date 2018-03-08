package com.revature.model;

import java.io.Serializable;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private double balance;
	private int hashedPassword;
	
	public User(int id,String password) {
		this.id = id;
		this.balance = 0.0;
		this.hashedPassword = password.hashCode();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public boolean checkPassword(String password) {
		return hashedPassword == password.hashCode();
	}
}
