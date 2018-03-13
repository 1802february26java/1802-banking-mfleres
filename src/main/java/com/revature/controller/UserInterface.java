package com.revature.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.revature.exception.InvalidLoginException;
import com.revature.exception.NoUserException;
import com.revature.exception.UserNotFoundException;
import com.revature.model.Bank;
import com.revature.model.Banking;
import com.revature.model.Transaction;
import com.revature.repository.BankJdbc;

public class UserInterface {
	Scanner scanner;
	Banking bank;
	
	public UserInterface(Banking b) {
		if(b == null) {
			throw new IllegalArgumentException();
		} else {
			this.bank = b;
		}
	}
	
	public void startPrompts() {
		String line = "";
		boolean exitFlag = false;
		
		System.out.println("Welcome to the Banking App!");
		System.out.println("Enter command \"help\" for total list of commands");
		while(!exitFlag) {
			System.out.print("> ");
			scanner = new Scanner(System.in);
			try {
				line = scanner.nextLine();
			} catch (NoSuchElementException e) {
				line = "";
			}
			String args[] = line.split("\\s+");
			if(args.length > 0) {
				String command = args[0].toLowerCase();
				switch(command) {
				case "exit":
					try{
						if(bank.getCurrentUser() != 0) {
							System.out.println("User ID " + bank.getCurrentUser() + " has logged out.");
							bank.logout();
						}
					} catch (UserNotFoundException e){
						
					}
					System.out.println("Exiting Banking App");
					exitFlag = true;
					break;
				case "login":
					if(args.length > 2) {
						int id = 0;
						try {
							id = Integer.parseInt(args[1]);
							bank.login(id,args[2]);
							System.out.println("Successfully logged in with User ID " + id);
						} catch (InvalidLoginException | NumberFormatException | UserNotFoundException e) {
							System.out.println("Invalid Login Information");
							break;
						}
					}else {
						System.out.println("To login, you need to input your user ID and password.");
						System.out.println("Syntax: \"login <user ID> <password>");
					}
					break;
				case "help":
					listOptions();
					break;
				case "register":
					if(args.length > 1) {
						int newId = bank.register(args[1]);
						System.out.println("Congratulations on your new account!");
						System.out.println("Your user ID is " + newId);
					}else {
						System.out.println("To register, you need a password.");
						System.out.println("Syntax: \"register <password>\"");
					}
					break;
				case "logout":
					if(bank.getCurrentUser() != 0) {
						System.out.println("User ID " + bank.getCurrentUser() + " has logged out.");
						bank.logout();
					}
					else
					{
						System.out.println("There is no user currently logged in.");
					}
					break;
				case "withdraw":
					try {
						double amount = Double.parseDouble(args[1]);
						bank.withdraw(amount);
						System.out.println("Withdrew $"+amount);
					} catch (NumberFormatException|ArrayIndexOutOfBoundsException e)  {
						System.out.println("To withdraw, you need to enter an amount.");
						System.out.println("Syntax: \"register <password>\"");
						
					} catch (NoUserException e) {
						System.out.println("There is no user currently logged in.");
					}
					break;
				case "deposit":
					try {
						double amount = Double.parseDouble(args[1]);
						bank.deposit(amount);
						System.out.println("Deposited $"+amount);
					} catch (NumberFormatException|ArrayIndexOutOfBoundsException e)  {
						System.out.println("To deposit, you need to enter an amount.");
						System.out.println("Syntax: \"register <password>\"");
						
					} catch (NoUserException e) {
						System.out.println("There is no user currently logged in.");
					}
					break;
				case "balance":
					try {
						double balance = bank.getBalance();
						System.out.println("Your current balance is $" + balance);
					} catch (NoUserException e) {
						System.out.println("There is no user currently logged in.");
					}
					break;
				case "display":
					if(bank.getCurrentUser() == 1) {
						((BankJdbc) bank).viewBank();
					}
					break;
				case "history":
					//Display transaction history
					try {
						List<Transaction> transactionList = bank.getTransactionHistory();
						System.out.println("|  ID  |       Timestamp         | Alteration |    Balance    |");
						System.out.println("|------|-------------------------|------------|---------------|");
						for(Transaction t : transactionList) {
							System.out.println(t);
						}
					} catch (NoUserException e) {
						System.out.println("There is no user currently logged in.");
					}
				}
			}
		}
	}
	
	private void listOptions() {
		System.out.println("login <ID> <password>");
		System.out.println("register <password>");
		System.out.println("deposit <amount>");
		System.out.println("withdraw <amount>");
		System.out.println("balance");
		System.out.println("history");
		System.out.println("logout");
		System.out.println("exit");
	}
}
