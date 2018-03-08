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
		testBank.login(userB,"myPassy");
		System.out.println(testBank.getBalance());
		double oldBalance = testBank.getBalance();
		testBank.deposit(5.0);
		assertEquals(oldBalance+5.0, testBank.getBalance(),0.1);
		assertNotEquals(oldBalance, testBank.getBalance(), 0.1);
		testBank.logout();
	}
	
	@Test
	public void displayAllTest() {
		testBank.viewBank();
	}
	
	@Test
	public void testNextId() {
		int newId = testBank.register("Password");
		assertEquals(3, newId);
		testBank.deregister("Password");
	}
	
	/*@Test
	public void testFileSaving() {
		System.out.println("testBank: "+testBank);
		Bank copy = new Bank("src/test/resources/TestVault.bnk");
		System.out.println("Copy: "+copy);
		Bank extra = new Bank("src/test/resources/TestVault2.bnk");
		int extraUser = extra.register("dummy");
		System.out.println("Extra: "+extra);
		copy.login(userB,"myPassy");
		testBank.login(userB,"myPassy");
		assertEquals(copy.getBalance(), testBank.getBalance(),0.5);
		testBank.logout();
		testBank.login(userA,"Hunter1");
		extra.login(extraUser,"dummy");
		assertNotEquals(extra.getBalance(), testBank.getBalance(), 0.5);
	}*/
	
	@Test (expected = InvalidLoginException.class)
	public void testBadPassword() {
		testBank.login(2, "Password2");
	}
	
	@Test
	public void testGoodLogin() {
		testBank.login(2, "Password");
	}
}
