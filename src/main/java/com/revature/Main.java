package com.revature;

import com.revature.exception.NoUserException;
import com.revature.exception.UserNotFoundException;
import com.revature.model.*;

/** 
 * Create an instance of your controller and launch your application.
 * 
 * Try not to have any logic at all on this class.
 */
public class Main {

	public static void main(String[] args) {
		Bank b = new Bank();
		b.logout();
		System.out.println(b);
		int testId = b.register();
		b.login(testId);
		b.deposit(13.0*40.0);
		b.logout();
		System.out.println(b);
	}
}
