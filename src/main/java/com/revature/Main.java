package com.revature;

import com.revature.controller.UserInterface;
import com.revature.exception.NoUserException;
import com.revature.exception.UserNotFoundException;
import com.revature.model.*;
import com.revature.repository.BankJdbc;

/** 
 * Create an instance of your controller and launch your application.
 * 
 * Try not to have any logic at all on this class.
 */
public class Main {

	public static void main(String[] args) {
		UserInterface ui = new UserInterface(BankJdbc.getInstance());
		ui.startPrompts();
	}
}
