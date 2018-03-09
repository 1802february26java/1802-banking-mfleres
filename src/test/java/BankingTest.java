import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assume.assumeNoException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.revature.exception.InvalidLoginException;
import com.revature.exception.UserNotFoundException;
import com.revature.model.Bank;
import com.revature.repository.BankJdbc;
import com.revature.repository.ConnectionUtil;

public class BankingTest {

	private static Logger logger = Logger.getLogger(ConnectionUtil.class);
	static {
		logger.setLevel(Level.ALL);
	}
	
	BankJdbc testBank;
	int userA;
	int userB;
	
	
	@Before
	public void setup() {
		logger.trace("Starting test setup");
		testBank = BankJdbc.getInstance();
		logger.trace("Test Setup done");
	}
	
	@Test
	public void checkAccurateDeposit() {
		userA = testBank.register("Password");
		testBank.deposit(50000.0);
		double oldBalance = testBank.getBalance();
		logger.info("Starting Balance: " + oldBalance);
		testBank.deposit(5.0);
		assertEquals(oldBalance+5.0, testBank.getBalance(),0.1);
		assertNotEquals(oldBalance, testBank.getBalance(), 0.1);
		testBank.deregister("Password");
	}
	
	@Test
	public void checkAccurateWithdrawl() {
		userA = testBank.register("Password");
		testBank.deposit(5000000.0);
		double oldBalance = testBank.getBalance();
		logger.info("Starting Balance: " + oldBalance);
		testBank.withdraw(5.0);
		assertEquals(oldBalance-5.0, testBank.getBalance(),0.1);
		assertNotEquals(oldBalance, testBank.getBalance(), 0.1);
		testBank.deregister("Password");
	}
	
	@Test
	public void displayAllTest() {
		testBank.viewBank();
	}
	
	@Test (expected = InvalidLoginException.class)
	public void testBadLogin() {
		testBank.login(1, "Password2");
	}
	
	@Test
	public void testGoodLogin() {
		int newUser = testBank.register("password");
		testBank.logout();
		testBank.login(newUser, "password");
		testBank.deregister("password");
	}
}
